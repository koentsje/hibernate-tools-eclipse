package org.hibernate.tool.eclipse.orm.runtime.legacy;

import org.hibernate.tool.eclipse.common.runtime.Util;

import org.hibernate.tool.eclipse.orm.runtime.spi.ICfg2HbmTool;
import org.hibernate.tool.eclipse.orm.runtime.spi.IPersistentClass;
import org.hibernate.tool.eclipse.orm.runtime.spi.IProperty;

public abstract class AbstractCfg2HbmToolFacade 
extends AbstractFacade 
implements ICfg2HbmTool {
	
	public AbstractCfg2HbmToolFacade(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}

	private Class<?> getPersistentClassClass() {
		return Util.getClass("org.hibernate.mapping.PersistentClass", getFacadeFactoryClassLoader());
	}
	
	private Class<?> getPropertyClass() {
		return Util.getClass("org.hibernate.mapping.Property", getFacadeFactoryClassLoader());
	}
	
	private Object getTarget(Object object) {
		return Util.invokeMethod(object, "getTarget", new Class[] {}, new Object[] {});
	}
	
	@Override
	public String getTag(IPersistentClass persistentClass) {
		return (String)Util.invokeMethod(
				getTarget(), 
				"getTag", 
				new Class[] { getPersistentClassClass() }, 
				new Object[] { getTarget(persistentClass) });
	}
	
	@Override
	public String getTag(IProperty property) {
		return (String)Util.invokeMethod(
				getTarget(), 
				"getTag", 
				new Class[] { getPropertyClass() }, 
				new Object[] { getTarget(property) });
	}

}
