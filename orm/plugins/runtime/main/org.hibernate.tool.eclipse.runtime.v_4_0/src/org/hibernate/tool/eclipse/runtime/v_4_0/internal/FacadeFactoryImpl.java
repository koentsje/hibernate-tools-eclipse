package org.hibernate.tool.eclipse.runtime.v_4_0.internal;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.tool.eclipse.runtime.common.AbstractFacadeFactory;
import org.hibernate.tool.eclipse.runtime.spi.IColumn;
import org.hibernate.tool.eclipse.runtime.spi.IConfiguration;
import org.hibernate.tool.eclipse.runtime.spi.IPersistentClass;
import org.hibernate.tool.eclipse.runtime.spi.IProperty;

public class FacadeFactoryImpl extends AbstractFacadeFactory {
	
	public ClassLoader getClassLoader() {
		return FacadeFactoryImpl.class.getClassLoader();
	}
	
	@Override
	public IConfiguration createConfiguration(Object target) {
		return new ConfigurationFacadeImpl(this, (Configuration)target);
	}
	
	@Override
	public IColumn createColumn(Object target) {
		return new ColumnFacadeImpl(this, (Column)target);
	}
	
	@Override
	public IPersistentClass createSpecialRootClass(IProperty property) {
		return new SpecialRootClassFacadeImpl(this, property);
	}
	
	@Override
	public IPersistentClass createPersistentClass(Object target) {
		return new PersistentClassFacadeImpl(this, target);
	}
	
}
