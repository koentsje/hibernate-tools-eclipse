package org.hibernate.tool.eclipse.orm.runtime.v_3_5.internal;

import org.hibernate.mapping.Column;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;
import org.hibernate.type.TypeFactory;
import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractFacadeFactory;
import org.hibernate.tool.eclipse.orm.runtime.spi.IClassMetadata;
import org.hibernate.tool.eclipse.orm.runtime.spi.IColumn;
import org.hibernate.tool.eclipse.orm.runtime.spi.IPersistentClass;
import org.hibernate.tool.eclipse.orm.runtime.spi.IProperty;
import org.hibernate.tool.eclipse.orm.runtime.spi.IType;
import org.hibernate.tool.eclipse.orm.runtime.spi.ITypeFactory;

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
	public ITypeFactory createTypeFactory() {
		return new TypeFactoryFacadeImpl(this, (TypeFactory)null);
	}

	@Override
	public IType createType(Object target) {
		return new TypeFacadeImpl(this, (Type)target);
	}

	@Override
	public IPersistentClass createPersistentClass(Object target) {
		return new PersistentClassFacadeImpl(this, target);
	}
	
}
