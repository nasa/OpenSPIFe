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
package gov.nasa.arc.spife.core.plan.editor.timeline.policies;

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineViewer;
import gov.nasa.arc.spife.core.plan.editor.timeline.parts.TemporalNodeEditPart;
import gov.nasa.arc.spife.core.plan.editor.timeline.preferences.PlanTextDecoratorFieldProvider;
import gov.nasa.arc.spife.core.plan.editor.timeline.util.ITimelineTextDecoratorColorProvider;
import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.model.Alignment;
import gov.nasa.arc.spife.timeline.model.Section;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ERGB;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.ui.color.ColorMap;
import gov.nasa.ensemble.common.ui.color.ColorUtils;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor.PropertyValueWrapper;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.handles.MoveHandleLocator;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;

public class TemporalNodeDecoratorTextEditPolicy extends PlanTimelineViewerEditPolicy {

	public static final String ROLE = "TemporalNodeDecoratorTextEditPolicy";
	public static final List<Provider> PROVIDERS = ClassRegistry.createInstances(Provider.class);
	public static final ITimelineTextDecoratorColorProvider COLOR_PROVIDER = ClassRegistry.createInstance(ITimelineTextDecoratorColorProvider.class);
	
	private Provider provider = null;
	private String nodeText = null;
	private Dimension textSize;
	private Color textColor = null;
	private Font textFont = null;
	private Alignment desiredAlignment = null;
	private TextOverlay textOverlayFigure;
	private final Map<String, Provider> providerMap = new HashMap<String, Provider>();
	private Listener listener = new Listener();
	
	public TemporalNodeDecoratorTextEditPolicy() {
		for (Provider provider : PROVIDERS) {
			providerMap.put(provider.getKey(), provider);
		}
	}
	
	@Override
	public void activate() {
		super.activate();
		//
		// Activate preference listeners
		TIMELINE_PREFERENCES.addPropertyChangeListener(listener);
		//
		// Activate EMF listeners
		EPlanElement pe = ((TemporalNodeEditPart)getHost()).getModel();
		pe.eAdapters().add(listener);
		pe.getMember(CommonMember.class).eAdapters().add(listener);
		EObject data = pe.getData();
		if (data != null) {
			data.eAdapters().add(listener);
		}
		Section section = getViewer().getTimelineSectionModel();
		if (section != null) {
			section.eAdapters().add(listener);
		}
		//
		// Activate graphics
		IFigure layer = getLayer(TimelineConstants.LAYER_FEEDBACK_DATA);
		textOverlayFigure = new TextOverlay();
		layer.add(textOverlayFigure);
		textFont = TimelineUtils.deriveRowElementHeightFont(textFont);
		updateColor();
		updateText();
	}

	private Alignment getDesiredAlignment() {
		if (desiredAlignment == null) {
			PlanTimelineViewer viewer = getViewer();
			if (viewer != null) {
				Section section = viewer.getTimelineSectionModel();
				desiredAlignment = section.getAlignment();
			}
			if (desiredAlignment == null) {
				desiredAlignment = Alignment.TRUNCATE;
			}
		}
		return desiredAlignment;
	}

	private Provider getProvider() {
		if (provider == null) {
			String key = TIMELINE_PREFERENCES.getString(TimelinePreferencePage.P_DECORATOR_TEXT_KEY);
			if (key != null) {
				provider = providerMap.get(key);
				if (provider == null) {
					provider = new ParameterProvider(key);
				}
			}
		}
		return provider;
	}

	private void updateColor() {
		EPlanElement ePlanElement = ((TemporalNodeEditPart)getHost()).getModel();
		ERGB eRGB = ePlanElement.getMember(CommonMember.class).getColor();
		RGB rgb = ColorUtils.getAsRGB(eRGB);
		if (rgb != null) {
			float hsb[] = ColorUtils.getHSB(rgb);
			rgb = ColorUtils.getRGB(hsb[0], hsb[1], hsb[2]*0.6f);
		}
		final Color color = rgb == null ? ColorConstants.black : ColorMap.RGB_INSTANCE.getColor(rgb);
		GEFUtils.runInDisplayThread(getHost(), new Runnable() {
			@Override
			public void run() {
				textOverlayFigure.setForegroundColor(color);
				setTextColor();
			}
		});
	}
	
	private void setTextColor() {
		EPlanElement ePlanElement = ((TemporalNodeEditPart)getHost()).getModel();
		if(COLOR_PROVIDER != null) {
			textColor = COLOR_PROVIDER.getForeground(ePlanElement);
		} else {
			textColor = ColorConstants.black;
		}
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		EPlanElement pe = ((TemporalNodeEditPart)getHost()).getModel();
		pe.eAdapters().remove(listener);
		pe.getMember(CommonMember.class).eAdapters().remove(listener);
		EObject data = pe.getData();
		if (data != null) {
			data.eAdapters().remove(listener);
		}
		Section section = getViewer().getTimelineSectionModel();
		if (section != null) {
			section.eAdapters().remove(listener);
		}
		IFigure layer = getLayer(TimelineConstants.LAYER_FEEDBACK_DATA);
		layer.remove(textOverlayFigure);
		TIMELINE_PREFERENCES.removePropertyChangeListener(listener);
		//textFont.dispose();
		textFont = null;
	}

	private void updateText() {
		nodeText = getText();
		IFigure hostFigure = getHostFigure();
		Font font = textFont != null ? textFont : hostFigure.getFont();
		textSize = FigureUtilities.getTextExtents(nodeText, font);
		refreshFeedbackInDisplayThread();
	}

	private String getText() {
		EPlanElement ePlanElement = ((TemporalNodeEditPart)getHost()).getModel();
		if (ePlanElement.getMember(CommonMember.class).isVisible()) {
			Provider provider = getProvider();
			if (provider != null) {
				String text = provider.getText(ePlanElement);
				return text == null ? "" : text;
			}
		}
		return null;
	}

	private void refreshFeedbackInDisplayThread() {
		GEFUtils.runLaterInDisplayThread(getHost(), new Runnable() {
			@Override
			public void run() {
				IFigure f = getHostFigure();
				LayoutManager layout = f.getParent().getLayoutManager();
				layout.invalidate();
				f.getParent().repaint();
				textOverlayFigure.revalidate();
				textOverlayFigure.repaint();
			}
		});
	}

	private class Listener extends AdapterImpl implements IPropertyChangeListener {
	
		@Override
		public void notifyChanged(Notification notification) {
			Object feature = notification.getFeature();
			if (feature instanceof EStructuralFeature) {
				Provider provider = getProvider();
				boolean updatedColor = false;
				if (provider != null && provider.affects(notification)) {
					updateText();
				} else if (PlanPackage.Literals.COMMON_MEMBER__COLOR == feature) {
					updateColor();
					updatedColor = true;
				} else if (TimelinePackage.Literals.SECTION__ALIGNMENT == feature) {
					desiredAlignment = null;
					refreshFeedbackInDisplayThread();
				} else if(COLOR_PROVIDER != null && COLOR_PROVIDER.isRelevant(feature) && !updatedColor) {
					updateColor();
				}
			}
		}

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			String property = event.getProperty();
			if (TimelinePreferencePage.P_DECORATOR_TEXT_KEY.equals(property)) {
				provider = null;
				updateText();
			} else if (TimelinePreferencePage.P_CONTENT_FONT_SIZE.equals(property)) {
				textFont = TimelineUtils.deriveRowElementHeightFont(getHostFigure().getFont());
				refreshFeedbackInDisplayThread();
			}
		}
		
	}
	
	private class TextOverlay extends MoveHandle {

		public TextOverlay() {
			super((GraphicalEditPart) getHost(), new MoveHandleLocator(getHostFigure()) {

				@Override
				public void relocate(IFigure target) {
					if (!getHostFigure().isShowing() 
							|| nodeText == null || nodeText.length() == 0) {
						super.relocate(target);
						return;
					}

					Rectangle figureBounds;
					if (getReference() instanceof HandleBounds) {
						figureBounds = ((HandleBounds)getReference()).getHandleBounds();
					} else {
						figureBounds = getReference().getBounds();
					}
					figureBounds = new Rectangle(figureBounds.getResized(-1, -1));
					
					int xInset = 3;
					switch (getDesiredAlignment()) {
					case TRUNCATE:
						figureBounds.x += xInset;
						figureBounds.width = figureBounds.width - 2*xInset;
						break;
					case LEFT:
						figureBounds.x += xInset;
						figureBounds.width = textSize.width + 2*xInset;
						break;
					case CENTER:
						figureBounds.x = figureBounds.x + figureBounds.width/2 - textSize.width/2;
						figureBounds.width = textSize.width + 2*xInset;
						break;
					case RIGHT:
						figureBounds.x = figureBounds.x + figureBounds.width - textSize.width - xInset;
						figureBounds.width = textSize.width + 2*xInset;
						break;
					case TRAILING:
						figureBounds.x = figureBounds.x + figureBounds.width + xInset;
						figureBounds.width = textSize.width + 2*xInset;
						break;
					}
					figureBounds.y = figureBounds.y + figureBounds.height/2 - textSize.height/2;
					
					Insets insets = target.getInsets();
					figureBounds.translate(-insets.left, -insets.top);
					figureBounds.resize(insets.getWidth() + 1, insets.getHeight() + 1);
					if (!CommonUtils.equals(target.getBounds(), figureBounds)) {
						target.setBounds(figureBounds);
					}
				}
				
			});
		}
		
		@Override
		protected void initialize() {
			// no initialization
		}
		
		@Override
		protected void paintFigure(Graphics g) {
			if (!getHostFigure().isShowing()) {
				return;
			}
			if (nodeText != null) {
				if (textFont != null) {
					g.setFont(textFont);
				}
				g.setForegroundColor(textColor);
				g.drawText(nodeText, bounds.x, bounds.y);
			}
		}
		
	}
	
	public static abstract class Provider {
		
		public String getDisplayName() {
			return getKey();
		}
		
		public abstract String getKey();
		
		public abstract boolean affects(Notification notification);
		
		public abstract String getText(EPlanElement object);

		protected String getText(EPlanElement pe, String key) {
			if (TimelinePreferencePage.P_DECORATOR_TEXT_KEY_NONE.equals(key)) {
				return null;
			}
			
			if (PlanTextDecoratorFieldProvider.P_DECORATOR_TEXT_KEY_NAME.equals(key)) {
				return pe.getName();
			}
			
			if (PlanTextDecoratorFieldProvider.P_DECORATOR_TEXT_KEY_NOTES.equals(key)) {
				return pe.getMember(CommonMember.class).getNotes();
			}
			
			EObject object = pe.getData();
			IItemPropertySource source = EMFUtils.adapt(object, IItemPropertySource.class);
			if (source != null) {
				EStructuralFeature f = object.eClass().getEStructuralFeature(key);
				if (f != null) {
					IItemPropertyDescriptor pd = source.getPropertyDescriptor(object, f);
					if (pd != null) {
						IItemLabelProvider lp = pd.getLabelProvider(object);
						if (lp != null) {
							Object value = pd.getPropertyValue(object);
							if (value instanceof PropertyValueWrapper) {
								value = ((PropertyValueWrapper)value).getEditableValue(object);
							}
							return lp.getText(value);
						}
					}
				}
			}
			return null;
		}
		
	}
	
	public static class ParameterProvider extends Provider {

		private final String featureKey;
		
		public ParameterProvider(String parameterName) {
			this.featureKey = parameterName;
		}
		
		@Override
		public String getKey() {
			return featureKey;
		}

		@Override
		public String getText(EPlanElement pe) {
			return getText(pe, featureKey);
		}

		@Override
		public boolean affects(Notification notification) {
			Object feature = notification.getFeature();
			if (feature instanceof EStructuralFeature) {
				String name = ((EStructuralFeature)feature).getName();
				if (name.equals(featureKey)) {
					return true;
				} else if ((PlanTextDecoratorFieldProvider.P_DECORATOR_TEXT_KEY_NAME.equals(featureKey)
								&& PlanPackage.Literals.EPLAN_ELEMENT__NAME == feature)) {
					return true;
				} else if ((PlanTextDecoratorFieldProvider.P_DECORATOR_TEXT_KEY_NOTES.equals(featureKey)
								&& PlanPackage.Literals.COMMON_MEMBER__NOTES == feature)) {
					return true;
				}
			}
			return false;
		}
		
	}
	
}
