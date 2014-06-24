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
package gov.nasa.arc.spife.ui.timeline.util;

import gov.nasa.arc.spife.timeline.TimelineFactory;
import gov.nasa.arc.spife.timeline.model.ETimeline;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.IOException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.MultiPageEditorPart;

public class TimelineEditorUtils {
	
	public static ETimeline getTimelineModel(IEditorPart editor) {
		IEditorInput input = editor.getEditorInput();
		IFile file = CommonUtils.getAdapter(input, IFile.class);
		EditingDomain domain = CommonUtils.getAdapter(editor, EditingDomain.class);
		return getTimelineModel(domain, file);
	}
	
	public static ETimeline getTimelineModel(EditingDomain domain, IFile file) {
		ResourceSet resourceSet = domain.getResourceSet();
		return createTimeline(resourceSet, file);
	}
	
	public static ETimeline getDefaultTimelineModel(Object object) {
		IEditorPart timelineEditor = getTimelineEditor(object);
		String propertyName = TimelineConstants.TIMELINE_FILE_EXT + ".template.source.uri";
		String sourcePath = EnsembleProperties.getProperty(propertyName);
		if (sourcePath != null) {
			 URI uri = URI.createURI(sourcePath);
			 Object editingDomain = timelineEditor.getAdapter(EditingDomain.class);
			 if (editingDomain instanceof EditingDomain) {
				ResourceSet resourceSet = ((EditingDomain) editingDomain).getResourceSet();
				Resource defaultResource = resourceSet.createResource(uri);
				try {
					// Load the resource through the editing domain.
					defaultResource.load(null);
				} catch (Exception e) {
					LogUtil.warn(e.getMessage());
					try {
						defaultResource.load(null);
					} catch (Exception x) {
						LogUtil.error(e.getMessage());
						return null;
					}
				}
				EObject eTimeline = EMFUtils.getLoadedContent(defaultResource);
				if (eTimeline instanceof ETimeline) {
					return (ETimeline) eTimeline;
				}
			 }
		}
		return getTimelineModel(timelineEditor); //else well... do an empty one!
	}
	
	public static IEditorPart getTimelineEditor(Object object) {
		IEditorPart editor = null;
		if (object instanceof IEditorPart) {
			editor = (IEditorPart) object;
		} else if (object instanceof ExecutionEvent) {
			ExecutionEvent event = (ExecutionEvent) object;
			IEditorPart activeEditor = HandlerUtil.getActiveEditor(event);
			if (activeEditor instanceof MultiPageEditorPart) {
				MultiPageEditorPart multiPageEditorPart = (MultiPageEditorPart) activeEditor;
				IEditorPart[] editorParts = multiPageEditorPart.findEditors(multiPageEditorPart.getEditorInput());
				for (IEditorPart editorPart : editorParts) {
					Timeline timeline = (Timeline) editorPart.getAdapter(Timeline.class);
					if (timeline != null) {
						editor = editorPart;
						break;
					}
				}
			}
		}
		
		return editor;
	}
	
	private static ETimeline createTimeline(ResourceSet resourceSet, IFile file) {
		URI inputUri = EMFUtils.getURI(file);
		URI uri = inputUri.trimFileExtension().appendFileExtension(TimelineConstants.TIMELINE_FILE_EXT);
		ETimeline timeline = TimelineFactory.eINSTANCE.createETimeline();
		if (timeline != null) {
			saveTimeline(resourceSet, uri, timeline);
		}
		return timeline;
	}

	private static void saveTimeline(ResourceSet resourceSet, URI uri, ETimeline timeline) {
		Resource resource = resourceSet.createResource(uri);
		resource.getContents().add(timeline);
		try {
			resource.save(null);
		} catch (IOException e) {
			LogUtil.error("saving "+uri, e);
		}
	}

	public static ETimeline getTimelineModel(EditingDomain domain, URI uri) {
		ResourceSet resourceSet = domain.getResourceSet();
		Resource timelineResource = loadResource(resourceSet, uri);
		if (timelineResource == null) {
			timelineResource = resourceSet.createResource(uri);
		}
		
		for (EObject e : timelineResource.getContents()) {
			if (e instanceof ETimeline) {
				return (ETimeline) e;
			}
		}
		
		final ETimeline timelineModel = TimelineFactory.eINSTANCE.createETimeline();
		timelineResource.getContents().add(timelineModel);
		return timelineModel;
	}

	private static Resource loadResource(ResourceSet resourceSet, URI uri) {
		Resource timelineResource = null;
		if (resourceSet.getURIConverter().exists(uri, null)) {
			try {
				timelineResource = resourceSet.getResource(uri, true);
			} catch (Exception e) {
				try {
					// try again
					timelineResource = resourceSet.getResource(uri, true);
				} catch (Exception x) {
					// fall through and create a new one
					LogUtil.error("error reading "+uri, e);
				}
			}
		}
		return timelineResource;
	}

}
