package org.hibernate.tool.eclipse.runtime.common;

import org.hibernate.tool.eclipse.common.runtime.Util;

import java.util.HashSet;
import java.util.Iterator;

import org.hibernate.tool.eclipse.runtime.spi.IJoin;
import org.hibernate.tool.eclipse.runtime.spi.IProperty;

public abstract class AbstractJoinFacade 
extends AbstractFacade 
implements IJoin {

	protected HashSet<IProperty> properties = null;

	public AbstractJoinFacade(
			IFacadeFactory facadeFactory, 
			Object target) {
		super(facadeFactory, target);
	}

	@Override
	public Iterator<IProperty> getPropertyIterator() {
		if (properties == null) {
			initializeProperties();
		}
		return properties.iterator();
	}
	
	protected void initializeProperties() {
		properties = new HashSet<IProperty>();
		Iterator<?> iterator = (Iterator<?>)Util.invokeMethod(
				getTarget(), 
				"getPropertyIterator", 
				new Class[] {}, 
				new Object[] {});
		while (iterator.hasNext()) {
			properties.add(getFacadeFactory().createProperty(iterator.next()));
		}
	}

}
