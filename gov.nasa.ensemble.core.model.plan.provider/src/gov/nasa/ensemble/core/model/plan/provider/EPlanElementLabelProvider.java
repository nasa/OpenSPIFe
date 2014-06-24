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
package gov.nasa.ensemble.core.model.plan.provider;

import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.color.ColorMap;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

public class EPlanElementLabelProvider extends AdapterFactoryLabelProvider implements Adapter {

	private static Map<EPlanElementImageDescriptor, Image> ePlanElementImageDescriptorToImageMap = new HashMap<EPlanElementImageDescriptor, Image>();
	private static List<ILabelDecorator> labelDecorators = null;
	private static Listener WORKSPACE_LISTENER = new Listener();
//	private static Set<String> traces = new HashSet<String>();
	
	public EPlanElementLabelProvider(AdapterFactory factory) {
		this(factory, true);
	}
	
	public EPlanElementLabelProvider() {
		this(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE), false);
	}
	
	public EPlanElementLabelProvider(AdapterFactory factory, boolean markerUpdates) {
		super(factory);
		if(labelDecorators == null) {
			labelDecorators = ClassRegistry.createInstances(ILabelDecorator.class);
		}
		if (markerUpdates) {
			WORKSPACE_LISTENER.register(this);
		}
		
//		int i=0;
//		StringBuffer buffer = new StringBuffer();
//		buffer.append(Thread.currentThread().getName()+"\n");
//		for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
//			buffer.append("\tat "+element.getClassName()+"."+element.getMethodName()+"("+element.getFileName()+":"+element.getLineNumber()+")\n");
//			if (++i > 20) {
//				break;
//			}
//		}
//		String trace = buffer.toString();
//		if (!traces.contains(trace)) {
//			System.out.println(trace);
//			traces.add(trace);
//		}
	}

	@Override
	public void dispose() {
		super.dispose();
		WORKSPACE_LISTENER.unregister(this);
	}

	/**
	 * Return the background RGB value for the given object.
	 * @param object
	 * @return an RGB color descriptor
	 */
	private RGB getBackgroundRGB(Object object) {
		try {
			if (MissionExtender.hasMissionSpecificClass(ItemColorProvider.class)) {
				ItemColorProvider provider = MissionExtender.construct(ItemColorProvider.class);
				RGB background = (RGB) provider.getBackground(object);
				if (background != null) {
					return background;
				}
			}
		} catch (ConstructionException e) {
			LogUtil.error(e);
		}
		IItemColorProvider itemColorProvider = getAdapter(object, IItemColorProvider.class);
		RGB backgroundColor = (itemColorProvider != null)
			? (RGB)itemColorProvider.getBackground(object) : null;
		return backgroundColor;
	}

	/**
	 * Return an unmodified image for the given object
	 * @param object
	 * @return an unmodified image for the given object.
	 */
	private Image getOriginalImage(Object object) {
		IItemLabelProvider itemLabelProvider = getAdapter(object, IItemLabelProvider.class);
		Image image = itemLabelProvider != null ? getImageFromObject(itemLabelProvider
				.getImage(object))
				: getDefaultImage(object);

		return image;
	}

	private <T> T getAdapter(Object object, Class<T> adapter) {
		return (T) adapterFactory.adapt(object, adapter);
	}

	/**
	 * Given an object, get the associated image from the cache (if there is one).
	 * If no image exists in the cache, generate one using the base image, and
	 * store the new image in the cache or later use.
	 *
	 * @param object the object whose decorated image is desired
	 * @return an image for the given object (a fully decorated one, ready for use)
	 */
	@Override
	public Image getImage(Object object) {
		RGB backgroundColor = getBackgroundRGB(object);
		Image originalImage = getOriginalImage(object);
		int severity = EPlanUtils.getSeverity(object);

		EPlanElementImageDescriptor ePlanElementImageDescriptor
			= new EPlanElementImageDescriptor(backgroundColor, originalImage, severity);

		Image image = ePlanElementImageDescriptorToImageMap.get(ePlanElementImageDescriptor);

		if(image == null) {
			image = createImage(ePlanElementImageDescriptor, object);
			ePlanElementImageDescriptorToImageMap.put(ePlanElementImageDescriptor
					, image);
		}

		return image;
	}

	/**
	 * Using a descriptor, create the appropriate image.
	 * @param ePlanElementImageDescriptor a descriptor that describes an image
	 * that should be used for the given object.
	 * @param object the object for which an image should be created
	 * @param ePlanElementImageDescriptor a descriptor that describes the type of image
	 * that should be created.
	 * @return a newly generated image (with badges, ready to use)
	 */
	private Image createImage(EPlanElementImageDescriptor ePlanElementImageDescriptor
			, Object object) {
		RGB rgb = ePlanElementImageDescriptor.getBackgroundColor();
		Image image = ePlanElementImageDescriptor.getImage();

		if (rgb != null) {
			image = getImageOverlay(image, rgb);
			Image previousImage = image;
			image = getImageWithBadges(image, object);
			if(!previousImage.equals(image)) {
				previousImage.dispose();
			}
		}

		return image;
	}

	@Override
	public void notifyChanged(Notification n) {
		Object feature = n.getFeature();
		if (PlanPackage.Literals.COMMON_MEMBER__COLOR == feature) {
			EMember member = (EMember) n.getNotifier();
			EPlanElement planElement = member.getPlanElement();
//			if (images.containsKey(planElement)) {
//				images.remove(planElement);
//			}
			for (ILabelProviderListener l : labelProviderListeners) {
				l.labelProviderChanged(new LabelProviderChangedEvent(this, planElement));
			}
		} else if (PlanPackage.Literals.EPLAN_ELEMENT__NAME == feature) {
			for (ILabelProviderListener l : labelProviderListeners) {
				l.labelProviderChanged(new LabelProviderChangedEvent(this, n.getNotifier()));
			}
		}
		super.notifyChanged(n);
	}

	private Image getImageOverlay(Image image, RGB rgb) {
		Rectangle rect = image.getBounds();
		Display display = WidgetUtils.getDisplay();

		// 1. create image

		PaletteData palette = new PaletteData(0xFF0000, 0xFF00, 0xFF);
		ImageData imageData = new ImageData(rect.width, rect.height, 24, palette);
		Image imageOverlay = new Image(display, imageData);
		GC gc = new GC(imageOverlay);
		Color backgroudColor = ColorMap.RGB_INSTANCE.getColor(rgb);
		gc.setBackground(backgroudColor);
		gc.fillOval(rect.x, rect.y, rect.width, rect.height);
		gc.drawImage(image, rect.x, rect.y);
		gc.dispose();
		//backgroudColor.dispose();
 		imageData = imageOverlay.getImageData();
 		imageOverlay.dispose();

 		// 2. create alpha channel

		PaletteData grayPalette = new PaletteData(0xFF, 0xFF, 0xFF);
		ImageData alphaImageData = new ImageData(rect.width, rect.height, 8, grayPalette);
		Image imageAlpha = new Image(display, alphaImageData);
		gc = new GC(imageAlpha);
		gc.setAntialias(SWT.ON);
		gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.fillOval(rect.x, rect.y, rect.width, rect.height);
		gc.dispose();
 		alphaImageData = imageAlpha.getImageData();
 		imageAlpha.dispose();

 		// 3. Apply alpha channel

 		for (int x = 0; x < imageData.width; ++x)
 			for (int y = 0; y < imageData.height; ++y)
 				imageData.setAlpha(x, y, (int) (alphaImageData.palette.getRGB(alphaImageData.getPixel(x, y)).getHSB()[2] * 255));

		return new Image(display, imageData);
	}

	/**
	 * Add badges to an existing image given an object. Label decorators that are
	 * loaded through the ClassRegistry extension point are programmed to decorate
	 * and existing image given an element.
	 *
	 * @param image the image to decorate
	 * @param element the element which shall determine the decoration
	 * @return a new decorated image if a decoration was performed, the original
	 * image otherwise.
	 */
	private Image getImageWithBadges(Image image, Object element) {
		Image decoratedImage = image;
		Image previousImage = null;
		for(ILabelDecorator labelDecorator : labelDecorators) {
			if(!decoratedImage.equals(image)) {
				previousImage = decoratedImage;
			}

			decoratedImage = labelDecorator.decorateImage(image, element);

			if(previousImage != null && !previousImage.isDisposed()) {
				previousImage.dispose();
			}
		}
		return decoratedImage;
	}

	@Override
	public Notifier getTarget() {
		return null;
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return false;
	}

	@Override
	public void setTarget(Notifier newTarget) {
		// no implementation
	}

	/**
	 * This class does not extend ImageDescriptor. The sole purpose of this class
	 * is to provide a means to caching the images needed by this label provider.
	 * By creating a new descriptor for each image request, we can see if such an
	 * image has been created before, and return the request image.
	 */
	static class EPlanElementImageDescriptor {
		private RGB backgroundColor;
		private Image image;
		private int severity;

		public EPlanElementImageDescriptor(RGB backgroundColor, Image image
				, int severity) {
			this.backgroundColor = backgroundColor;
			this.image = image;
			this.severity = severity;
		}

		/**
		 * Custom hash code, since using the default hash code will return a different
		 * code for two EPlanElementImageDescriptor instances which would be deemed
		 * "equal"
		 * @return the calculated hashCode
		 */
		@Override
		public int hashCode() {
			return backgroundColor.hashCode() + image.hashCode() + severity;
		}

		@Override
		public boolean equals(Object object) {
			if(object instanceof EPlanElementImageDescriptor) {
				EPlanElementImageDescriptor ePlanElementImageDescriptor
					= (EPlanElementImageDescriptor)object;
				if(this.backgroundColor.equals(ePlanElementImageDescriptor.getBackgroundColor())
						&& this.image.equals(ePlanElementImageDescriptor.getImage())
						&& this.severity == ePlanElementImageDescriptor.getSeverity()) {
					return true;
				}
			}
			return false;
		}

		public RGB getBackgroundColor() {
			return this.backgroundColor;
		}

		public Image getImage() {
			return this.image;
		}

		public int getSeverity() {
			return this.severity;
		}
	}

	private static class Listener implements IResourceChangeListener {

		private final List<EPlanElementLabelProvider> providers = new ArrayList<EPlanElementLabelProvider>();

		public void register(EPlanElementLabelProvider lp) {
			synchronized (providers) {
				if (providers.isEmpty()) {
					ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
				}
				providers.add(lp);
			}
		}

		public void unregister(EPlanElementLabelProvider lp) {
			synchronized (providers) {
				providers.remove(lp);
				if (providers.isEmpty()) {
					ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
				}
			}
		}

		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			final List<EPlanElementLabelProvider> markedProviders = new ArrayList<EPlanElementLabelProvider>();
			synchronized (providers) {
				for (EPlanElementLabelProvider provider : providers) {
					if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
						boolean includeSubTypes = true;
						IMarkerDelta[] markerDeltas = event.findMarkerDeltas(IMarker.MARKER, includeSubTypes);
						if (markerDeltas.length > 0) {
							markedProviders.add(provider);
						}
					}
				}
			}
			WidgetUtils.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					for (EPlanElementLabelProvider provider : markedProviders) {
						provider.fireLabelProviderChanged();
					}
				}
			});
		}
	}

}
