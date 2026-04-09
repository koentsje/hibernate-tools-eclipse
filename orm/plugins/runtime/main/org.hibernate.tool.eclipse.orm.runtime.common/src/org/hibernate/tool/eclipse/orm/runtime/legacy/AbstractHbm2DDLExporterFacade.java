package org.hibernate.tool.eclipse.orm.runtime.legacy;

import org.hibernate.tool.eclipse.common.runtime.Util;

import java.util.Hashtable;

import org.hibernate.tool.eclipse.orm.runtime.spi.IHbm2DDLExporter;

public abstract class AbstractHbm2DDLExporterFacade 
extends AbstractFacade 
implements IHbm2DDLExporter {

	public AbstractHbm2DDLExporterFacade(
			IFacadeFactory facadeFactory, 
			Object target) {
		super(facadeFactory, target);
	}

	@Override
	public void setExport(boolean export) {
		Util.invokeMethod(
				getTarget(), 
				"setExport", 
				new Class[] { boolean.class }, 
				new Object[] { export });
	}

	@SuppressWarnings("unchecked")
	@Override
	public Hashtable<Object, Object> getProperties() {
		return (Hashtable<Object, Object>)Util.invokeMethod(
				getTarget(), 
				"getProperties", 
				new Class[] {}, 
				new Object[] {});
	}

}
