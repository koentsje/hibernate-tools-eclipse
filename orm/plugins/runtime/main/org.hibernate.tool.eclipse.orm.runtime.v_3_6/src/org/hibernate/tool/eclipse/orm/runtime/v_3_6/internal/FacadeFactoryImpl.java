package org.hibernate.tool.eclipse.orm.runtime.v_3_6.internal;

import org.hibernate.mapping.Column;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractFacadeFactory;
import org.hibernate.tool.eclipse.orm.runtime.spi.IClassMetadata;
import org.hibernate.tool.eclipse.orm.runtime.spi.IColumn;
import org.hibernate.tool.eclipse.orm.runtime.spi.IPersistentClass;
import org.hibernate.tool.eclipse.orm.runtime.spi.IProperty;

public class FacadeFactoryImpl extends AbstractFacadeFactory {
	
	public ClassLoader getClassLoader() {
		return FacadeFactoryImpl.class.getClassLoader();
	}
	
	@Override
	public IClassMetadata createClassMetadata(Object target) {
		return new ClassMetadataFacadeImpl(this, (ClassMetadata)target);
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
