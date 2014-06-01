/*******************************************************************************
 * Copyright 2014 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package gov.nasa.ensemble.core.plan.resources.ui.constraint;

import static gov.nasa.ensemble.core.plan.formula.js.constraint.JSConstraintUtils.*;
import static gov.nasa.ensemble.emf.util.EMFUtils.*;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.core.plan.formula.js.constraint.JSEvaluator;
import gov.nasa.ensemble.emf.model.common.CommonFactory;
import gov.nasa.ensemble.emf.model.common.CommonPackage;
import gov.nasa.ensemble.emf.model.common.ObjectFeature;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.javascript.rhino.FormulaInfo;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.ConstraintStatus;
import org.eclipse.swt.widgets.Shell;

public class CheckResourceExistsConstraint extends AbstractModelConstraint {
	
	private static final String ANNOTATION_SOURCE_DETAIL = "detail";
	private static final String ANNOTATION_DETAIL_CHECK = "checkExists";
	private static final String ANNOTATION_DETAIL_TYPE = "type";
	private static final String ANNOTATION_DETAIL_EXTENSION_FILTER = "extensionFilter";
	private static final String SPECIAL_CONTAINER_FEATURE = "container";
	private static WorkspaceListener workspaceListener = null;
	private static Map<IResource, List<ObjectFeature>> resourceToObjectFeatureMap = new HashMap<IResource, List<ObjectFeature>>();
	private static final Pattern DOT_PATTERN = Pattern.compile("\\.");
	
	public static void addResourceAffectsObjectFeature(IResource resource, EObject object, EAttribute attribute) {
		if (workspaceListener == null) {
			workspaceListener = new WorkspaceListener();
			ResourcesPlugin.getWorkspace().addResourceChangeListener(workspaceListener, IResourceChangeEvent.POST_CHANGE);
		}
		ObjectFeature affected = CommonFactory.eINSTANCE.createObjectFeature();
		affected.setObject(object);
		affected.setFeature(attribute);
		List<ObjectFeature> affectedObjectFeatures = resourceToObjectFeatureMap.get(resource);
		if (affectedObjectFeatures == null) {
			affectedObjectFeatures = new ArrayList<ObjectFeature>(3);
			resourceToObjectFeatureMap.put(resource, affectedObjectFeatures);
			affectedObjectFeatures.add(affected);
		} else if (!affectedObjectFeatures.contains(affected)) {
			affectedObjectFeatures.add(affected);
		}
	}
	
	public static void removeResourceAffectsObjectFeature(IResource resource, ObjectFeature affected) {
		List<ObjectFeature> affectedObjectFeatures = resourceToObjectFeatureMap.get(resource);
		if (affectedObjectFeatures != null) {
			affectedObjectFeatures.remove(affected);
			if (affectedObjectFeatures.isEmpty()) {
				resourceToObjectFeatureMap.remove(resource);
			}
		}
	}
	
	private JSEvaluator evaluator = new JSEvaluator();
	
	public CheckResourceExistsConstraint() {
		super();
	}

	/**
	 * Validates an object in the given context to ensure that attribute values
	 * conform to any minimum and/or maximum limit restrictions
	 * 
	 * @param ctx the IValidationContext used for the validation
	 */
	@Override
	public IStatus validate(IValidationContext ctx) {
		EObject target = ctx.getTarget();
		EMFEventType eventType = ctx.getEventType();
		try {
			// In the case of batch mode.
			if (eventType == EMFEventType.NULL) {
				EClass eClass = target.eClass();
				List<IStatus> statusList = null;
				for (EAttribute attribute : eClass.getEAllAttributes()) {
					IStatus failure = validateResourceExists(ctx, target, attribute, target.eGet(attribute));
					if (failure != null) {
						if (statusList == null) {
							statusList = new ArrayList<IStatus>();
						}
						statusList.add(failure);
					}
				}
				if (statusList != null) {
					return combinedStatus(ctx, target, statusList);
				}
			} else {
				// In the case of live mode
				EStructuralFeature feature = ctx.getFeature();
				if (feature instanceof EAttribute) {
					IStatus status =  validateResourceExists(ctx, target, (EAttribute)feature, ctx.getFeatureNewValue());
					if (status != null) {
						return status;
					}
				}
			}
		} catch (Exception e) {
			LogUtil.error("Run time exception while checking the resource exists constraint", e);
		}
		return ctx.createSuccessStatus();
	}
	
	/**
	 * Tests the value of an attribute of a target object to determine whether it has a checkExists annotation
	 * and has an IPath or EURL datatype and, if so, verify that the referenced resource exists
	 * 
	 * @param ctx the IValidationContext used for the validation
	 * @param target the EObject to be validated
	 * @param attribute the EAttribute whose value is to be validated
	 * @param value the Object value of the attribute to be validated
	 * @return IStatus a failure status object if the value does not reference an existing resource, else null
	 */
	private IStatus validateResourceExists(IValidationContext ctx, EObject target, EAttribute attribute, Object value) {
		String annotation = EMFUtils.getAnnotation(attribute, ANNOTATION_SOURCE_DETAIL, ANNOTATION_DETAIL_CHECK);
		if (annotation != null && annotation.length() > 0) {
			EDataType dataType = attribute.getEAttributeType();
			if (dataType != CommonPackage.Literals.IPATH && dataType != CommonPackage.Literals.EURL) {
				return validateReferencedAttributes(ctx, target, annotation);
			}
			boolean isBatch = (ctx.getEventType() == EMFEventType.NULL);
			// default to checking both that a non-empty value is specified and that the resource referenced
			// by that value can be located
			boolean checkFilled = true, checkPath = true;
			String expression = null;
			Collection<ObjectFeature> dependencies = null;
			String severity = annotation;
			String[] components = CommonUtils.COMMA_PATTERN.split(annotation);
			int compNumber = components.length;
			if (compNumber > 0) {
				severity = components[0].trim();
				if (compNumber > 1) {
					expression = components[1].trim();
					checkFilled = checkPath = false;
					if ("filled".equalsIgnoreCase(expression)) {
						checkFilled = true;
						expression = null;
					} else if ("path".equalsIgnoreCase(expression)) {
						checkPath = true;
						expression = null;
					} else if ("both".equalsIgnoreCase(expression)) {
						checkPath = true;
						checkFilled = true;
						expression = null;
					} else {
						checkPath = true;
						checkFilled = true;
					}
					if (compNumber > 2) {
						expression = components[2].trim();
					}
					if (expression != null && expression.length() > 0){
						Object result = evaluator.getValue(target, expression, true, value);
						if (!(result instanceof Boolean) || !((Boolean)result).booleanValue()) {
							checkFilled = false;
							checkPath = false;
						}
						Set<String> expressionPaths = FormulaInfo.getFeaturePaths(expression);
						dependencies = getObjectFeatures(target, expressionPaths);
					}
				}
			}
			if (dataType == CommonPackage.Literals.IPATH) {
				String type = EMFUtils.getAnnotation(attribute, ANNOTATION_SOURCE_DETAIL, ANNOTATION_DETAIL_TYPE);
				String path = CommonFactory.eINSTANCE.convertToString(dataType, value);
				if (path == null || path.length() == 0) {
					if (checkFilled) {
						String attributeName =  EMFUtils.getDisplayName(target, attribute);
						return ConstraintStatus.createStatus(ctx, getConstraintStatusLocus(target, attribute, dependencies), getSeverityCode(severity), 100,
							"No value is specified for the {0} of {1}", attributeName, targetName(target));
					}
					return null;
				}
				if ("File".equalsIgnoreCase(type) || "IFile".equalsIgnoreCase(type)) {
					String extensionsString = EMFUtils.getAnnotation(attribute, ANNOTATION_SOURCE_DETAIL, ANNOTATION_DETAIL_EXTENSION_FILTER);
					if (!checkExtension(attribute, path, extensionsString)) {
						String attributeName =  EMFUtils.getDisplayName(target, attribute);
						String pattern = "The file extension must be {0} for the {1} of {2}";
						if (extensionsString.indexOf(",") > 0) {
							pattern = "The file extension must be one of {0} for the {1} of {2}";
						}
						return ConstraintStatus.createStatus(ctx, getConstraintStatusLocus(target, attribute, dependencies), getSeverityCode(severity), 100,
								pattern, extensionsString, attributeName, targetName(target));
					}
				}
				if (!checkPath) {
					return null;
				}
				URI baseURI = target.eResource().getURI();
				File file = new File(path);
				// Interpret relative paths with respect to the target resource's URI
				if (!file.isAbsolute()) {
					if ("File".equalsIgnoreCase(type)) {
						type = "IFile";
					} else if ("Directory".equalsIgnoreCase(type)) {
						type = "IFolder";
					}
				}
				if ("IFile".equalsIgnoreCase(type)) {
					URI oldURI = URI.createURI(path).resolve(baseURI);
					IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
					String platformString = oldURI.toPlatformString(true);
					if (platformString == null) {
						return ConstraintStatus.createStatus(ctx, getConstraintStatusLocus(target, attribute, dependencies), getSeverityCode(severity), 100, "Unable to locate file {0} in project", path);
					}
					IResource resource = root.getFile(new Path(platformString));
					if (!isBatch) {
						// save information so that EMFDetailUtils can be informed on resource addition or removal
						addResourceAffectsObjectFeature(resource, target, attribute);
					}
					if (!resource.exists()) {
						return ConstraintStatus.createStatus(ctx, getConstraintStatusLocus(target, attribute, dependencies), getSeverityCode(severity), 100, "Unable to locate file {0} in project", path);
					} 
					if (isBatch) {
						// return information from which the resource can be determined to record a dependency
						return ConstraintStatus.createSuccessStatus(ctx, target, getConstraintStatusLocus(target, attribute, dependencies));
					}
				} else if ("IPath".equalsIgnoreCase(type) || "IContainer".equalsIgnoreCase(type) || "IFolder".equalsIgnoreCase(type)) {
					URI oldURI = URI.createURI(path).resolve(baseURI);
					IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
					String platformString = oldURI.toPlatformString(true);
					IResource resource = root.getFolder(new Path(platformString));
					if (!isBatch) {
						// save information so that EMFDetailUtils can be informed on resource addition or removal
						addResourceAffectsObjectFeature(resource, target, attribute);
					}
					if (!resource.exists()) {
						return ConstraintStatus.createStatus(ctx, getConstraintStatusLocus(target, attribute, dependencies), getSeverityCode(severity), 100, "Unable to locate directory {0} in project", path);
					} 
					if (isBatch) {
						// return information from which the resource can be determined to record a dependency
						return ConstraintStatus.createSuccessStatus(ctx, target, getConstraintStatusLocus(target, attribute, dependencies));
					}
				} else if ("Directory".equalsIgnoreCase(type)) {
		            if (!file.isDirectory()) {
		            	return ConstraintStatus.createStatus(ctx, getConstraintStatusLocus(target, attribute, dependencies), getSeverityCode(severity), 100, "Unable to locate directory {0}", path);
		            }
				} else {
		            if (!file.isFile()) {
		            	return ConstraintStatus.createStatus(ctx, getConstraintStatusLocus(target, attribute, dependencies), getSeverityCode(severity), 100, "Unable to locate file {0}", path);
		            }
				}
			} else if (dataType == CommonPackage.Literals.EURL) {
				if (value == null) {
					if (checkFilled) {
						String attributeName =  EMFUtils.getDisplayName(target, attribute);
						return ConstraintStatus.createStatus(ctx, getConstraintStatusLocus(target, attribute, dependencies), getSeverityCode(severity), 100,
							"No value is specified for the {0} of {1}", attributeName, targetName(target));
					}
					return null;
				}
				if (!checkPath) {
					return null;
				}
				URL url = (URL)value;
				URI uri = URI.createURI(url.toExternalForm());
				URIHandler handler = URIConverter.INSTANCE.getURIHandler(uri);
				if (!handler.exists(uri, null)) {
					return ConstraintStatus.createStatus(ctx, getConstraintStatusLocus(target, attribute, dependencies), getSeverityCode(severity), 100, "URL {0} cannot be located", url);
				}
			}
		}
		return null;
	}
	
	private boolean checkExtension(EAttribute attribute, String path, String extensionsString) {
		if (extensionsString != null && extensionsString.length() > 0) {
			String[] extensions = CommonUtils.COMMA_PATTERN.split(extensionsString);
			int lastDotPos = path.lastIndexOf('.');
			if (lastDotPos >= 0) {
				String extension = path.substring(lastDotPos + 1);
				for (String allowedExtension : extensions) {
					if (extension.equals(allowedExtension)) {
						return true;
					}
				}
			}
			return false;
		}
		return true;
	}

	private IStatus validateReferencedAttributes(IValidationContext ctx, EObject target, String annotation) {
		if (ctx.getEventType() == EMFEventType.NULL) {
			// no need to validate referenced attributes in Batch mode as they
			// will be evaluated directly
			return null;
		}
		
		List<IStatus> statusList = null;
		
		for (String path : CommonUtils.COMMA_PATTERN.split(annotation)) {
			EObject object = target;
			EClass eClass = object.eClass();
			String[] components = DOT_PATTERN.split(path.trim());
			int componentNum = components.length;
			if (componentNum > 1) {
				for (int i = 0; i < componentNum - 1; i++) {
					String component = components[i];
					EObject referenced = null;
					if (component.equals(SPECIAL_CONTAINER_FEATURE)) {
						referenced = object.eContainer();
					} else {
						EStructuralFeature feature = eClass.getEStructuralFeature(component);
						if (feature instanceof EReference) {
							referenced = (EObject)object.eGet(feature);
						}
					}
					object = referenced;
					if (object == null) {
						break;
					} else {
						eClass = object.eClass();
					}
				}
			}
			if (object == null) {
				continue;
			}
			EStructuralFeature referenced = eClass.getEStructuralFeature(components[componentNum - 1]);
			if (referenced instanceof EAttribute) {
				EAttribute attribute = (EAttribute)referenced;
				IStatus failure = validateResourceExists(ctx, object, attribute, object.eGet(attribute)); 
				if (failure != null) {
					if (statusList == null) {
						statusList = new ArrayList<IStatus>();
					}
					statusList.add(failure);
				}
			}
		}
		if (statusList != null) {
			return combinedStatus(ctx, target, statusList);
		}
		return null;
	}
	
	private Collection<ObjectFeature> getObjectFeatures(EObject target, Set<String> expressionPaths) {
		Collection<ObjectFeature> objectFeatures = new ArrayList<ObjectFeature>();
		for (String path : expressionPaths) {
			EObject object = target;
			EClass eClass = object.eClass();
			String[] components = DOT_PATTERN.split(path.trim());
			int componentNum = components.length;
			if (componentNum > 1) {
				for (int i = 0; i < componentNum - 1; i++) {
					String component = components[i];
					EObject referenced = null;
						EStructuralFeature feature = eClass.getEStructuralFeature(component);
						if (feature instanceof EReference) {
							referenced = (EObject)object.eGet(feature);
						}
					object = referenced;
					if (object == null) {
						break;
					} else {
						eClass = object.eClass();
					}
				}
			}
			if (object == null) {
				continue;
			}
			EStructuralFeature feature = eClass.getEStructuralFeature(components[componentNum - 1]);
			if (feature != null) {
				ObjectFeature objectFeature = CommonFactory.eINSTANCE.createObjectFeature();
				objectFeature.setObject(object);
				objectFeature.setFeature(feature);
				objectFeatures.add(objectFeature);
			}
		}
		return objectFeatures;
	}
	
	private static class WorkspaceListener implements IResourceChangeListener {
		
		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			resourceChanged(event.getDelta());
		}
		
		private void resourceChanged(IResourceDelta delta) {
			if (delta != null) {
				processDelta(delta);
				for (IResourceDelta child : delta.getAffectedChildren()) {
					resourceChanged(child);
				}
			}
		}

		@SuppressWarnings("unchecked")
		private void processDelta(IResourceDelta delta) {
			IResource resource = delta.getResource();
			int flags = delta.getFlags();
			if (resource == null
					|| IResourceDelta.MARKERS == flags 
					|| IResourceDelta.DESCRIPTION == flags) {
				return;
			}
			final List<ObjectFeature> affected = resourceToObjectFeatureMap.get(resource);
			if (affected == null) {
				return;
			}
			Shell shell = WidgetUtils.getShell();
			shell.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					for (ObjectFeature objectFeature : affected) {
						EMFDetailUtils.reValidateFeature(objectFeature.getObject(), objectFeature.getFeature());
					}
				}
			});
		}
	}
	
}
