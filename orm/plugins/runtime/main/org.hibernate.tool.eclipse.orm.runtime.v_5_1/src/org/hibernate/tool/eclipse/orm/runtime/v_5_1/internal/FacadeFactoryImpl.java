package org.hibernate.tool.eclipse.orm.runtime.v_5_1.internal;

import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractFacadeFactory;
import org.hibernate.tool.eclipse.orm.runtime.spi.IColumn;
import org.hibernate.tool.eclipse.orm.runtime.spi.IConfiguration;
import org.hibernate.tool.eclipse.orm.runtime.spi.IEnvironment;
import org.hibernate.tool.eclipse.orm.runtime.spi.IPersistentClass;
import org.hibernate.tool.eclipse.orm.runtime.spi.IProperty;
import org.hibernate.tool.eclipse.orm.runtime.spi.ISchemaExport;

public class FacadeFactoryImpl extends AbstractFacadeFactory {

	@Override
	public ClassLoader getClassLoader() {
		return FacadeFactoryImpl.class.getClassLoader();
	}

	@Override
	public IEnvironment createEnvironment() {
		return new EnvironmentFacadeImpl(this);
	}
	
	@Override
	public IPersistentClass createSpecialRootClass(IProperty property) {
		return new SpecialRootClassFacadeImpl(this, property);
	}
	
	@Override
	public IConfiguration createConfiguration(Object target) {
		return new ConfigurationFacadeImpl(this, target);
	}
	
	@Override
	public IColumn createColumn(Object target) {
		return new ColumnFacadeImpl(this, target);
	}
	
	@Override
	public IPersistentClass createPersistentClass(Object target) {
		return new PersistentClassFacadeImpl(this, target);
	}
	
	@Override
	public ISchemaExport createSchemaExport(Object target) {
		return new SchemaExportFacadeImpl(this, target);
	}
	
}
