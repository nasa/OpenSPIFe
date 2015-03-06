package gov.nasa.ensemble.dictionary.provider;

import gov.nasa.ensemble.dictionary.DictionaryPackage;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ILabelProvider;

public class DictionaryLabelProviderAdapterFactory  extends AdapterFactoryImpl {

	@Override
	public boolean isFactoryForType(Object type) {
		if (DictionaryPackage.eINSTANCE == type) {
			return true;
		}
		if (type instanceof EObject) {
			return ((EObject)type).eClass().getEPackage() == DictionaryPackage.eINSTANCE;
		}
		return false;
	}

	@Override
	public Adapter adapt(Notifier target, Object type) {
		if (target instanceof EActivityDef && ILabelProvider.class == type) {
			AdapterFactory factory = EMFUtils.getAdapterFactory(target);
			return new DictionaryLabelProvider(factory);
		}
		return super.adapt(target, type);
	}

}