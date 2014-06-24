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
package gov.nasa.ensemble.core.plan.resources.ui.profile.provider;

import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.common.type.AbstractTrimmingStringifier;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.ui.treetable.ITreeTableColumn;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.treetable.EMFTreeTableColumn;
import gov.nasa.ensemble.core.detail.emf.treetable.IEMFTreeTableProvider;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileReference;
import gov.nasa.ensemble.core.plan.resources.profile.provider.ProfileItemProviderAdapterFactory;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;
import gov.nasa.ensemble.emf.util.EDataTypeStringifier;

import java.text.NumberFormat;
import java.text.ParseException;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.jscience.physics.amount.Amount;

public class ProfileItemProviderAdapterFactoryExt extends ProfileItemProviderAdapterFactory implements IEMFTreeTableProvider {

	@Override
	public boolean isFactoryForType(Object type) {
		return super.isFactoryForType(type)
				|| IEMFTreeTableProvider.class == type;
	}

	@Override
	public Adapter adapt(Notifier target, Object type) {
		if (IEMFTreeTableProvider.class == type) {
			return this;
		}
		return super.adapt(target, type);
	}

	@Override
	public ITreeTableColumn getTreeTableColumn(DetailProviderParameter parameter) {
		EObject model = parameter.getTarget();
		IItemPropertyDescriptor pd = parameter.getPropertyDescriptor();
		Object feature = pd.getFeature(model);
		if (ProfilePackage.Literals.PROFILE_EFFECT__END_VALUE_LITERAL == feature
				|| ProfilePackage.Literals.PROFILE_EFFECT__START_VALUE_LITERAL == feature
				|| ProfilePackage.Literals.PROFILE_EQUALITY_CONSTRAINT__VALUE_LITERAL == feature
				|| ProfilePackage.Literals.PROFILE_ENVELOPE_CONSTRAINT__MIN_LITERAL == feature
				|| ProfilePackage.Literals.PROFILE_ENVELOPE_CONSTRAINT__MAX_LITERAL == feature) {
			return new ProfileReferenceValueLiteralColumn(pd, pd.getDisplayName(model), 100);
		} else if (ProfilePackage.Literals.PROFILE_EQUALITY_CONSTRAINT__MAXIMUM_GAP == feature) {
			return new MaximumGapColumn(pd, pd.getDisplayName(model), 100);
		}
		return null;
	}

	@Override
	public void notifyChanged(Notification notification) 	{ /* no implementation */ }
	@Override
	public Notifier getTarget() 							{ return null; }
	@Override
	public void setTarget(Notifier newTarget) 				{ /* no implementation */ }
	@Override
	public boolean isAdapterForType(Object type) 			{ return false; }

	private final static class ProfileReferenceValueLiteralColumn extends EMFTreeTableColumn<ProfileReference> {

		public ProfileReferenceValueLiteralColumn(IItemPropertyDescriptor itemPropertyDescriptor, String headerName, int defaultWidth) {
			super(itemPropertyDescriptor, headerName, defaultWidth);
		}
		
		
		
		@Override
		protected IStringifier getStringifier(EObject reference) {
			EDataType eDataType = getEDataType((ProfileReference) reference);
			return eDataType == null ? null : new EDataTypeStringifier(eDataType);
		}

		@Override
		public void modify(ProfileReference reference, Object value, IUndoContext undoContext) {
			EDataType eDataType = getEDataType(reference);
			String stringValue = EcoreUtil.convertToString(eDataType, value);
			super.modify(reference, stringValue, undoContext);
		}

		private EDataType getEDataType(ProfileReference reference) {
			Profile<?> profile = ResourceUtils.getProfile(reference);
			if (profile == null) {
				return null;
			}
			EDataType eDataType = profile.getDataType();
			return eDataType;
		}
		
	}
	
	private static final class MaximumGapColumn extends EMFTreeTableColumn<ProfileReference> {

		private static final MaximumGapStringifier MAXIMUM_GAP_STRINGIFIER = new MaximumGapStringifier();

		public MaximumGapColumn(IItemPropertyDescriptor itemPropertyDescriptor, String headerName, int defaultWidth) {
			super(itemPropertyDescriptor, headerName, defaultWidth);
		}

		@Override
		protected IStringifier getStringifier(EObject facet) {
			return MAXIMUM_GAP_STRINGIFIER;
		}
		
	}
	
	private static final class MaximumGapStringifier extends AbstractTrimmingStringifier<Amount<Duration>> {

		@Override
		public String getDisplayString(Amount<Duration> duration) {
			if (duration == null) { 
				return null;
			}
			String hms = DurationFormat.getHHMMSSDuration(duration.longValue(SI.SECOND));
			long ms = duration.longValue(DateUtils.MILLISECONDS) % 1000;
			if (ms != 0) {
				NumberFormat format = NumberFormat.getInstance();
				format.setMinimumIntegerDigits(3);
				format.setMaximumFractionDigits(3);
				hms += "/"+ format.format(ms);
			}
			return hms;
		}

		@Override
		@SuppressWarnings("unused")
		protected Amount<Duration> getJavaObjectFromTrimmed(String string, Amount<Duration> defaultObject) throws ParseException {
			int index = string.indexOf("/");
			if (index == -1) {
				return Amount.valueOf(DurationFormat.parseFormattedDuration(string), SI.SECOND);
			}
			// else...
			String hmsString = string.substring(0, index);
			String msString = string.substring(index+1);
			return Amount.valueOf(DurationFormat.parseFormattedDuration(hmsString), SI.SECOND).plus(Amount.valueOf(Integer.parseInt(msString), DateUtils.MILLISECONDS));
		}
		
	}
	
}
