package org.hibernate.tool.eclipse.orm.runtime.v_6_0.internal;

import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractFacadeFactory;
import org.hibernate.tool.eclipse.orm.runtime.spi.IClassMetadata;
import org.hibernate.tool.eclipse.orm.runtime.spi.IColumn;
import org.hibernate.tool.eclipse.orm.runtime.spi.IConfiguration;
import org.hibernate.tool.eclipse.orm.runtime.spi.ICriteria;
import org.hibernate.tool.eclipse.orm.runtime.spi.IEnvironment;
import org.hibernate.tool.eclipse.orm.runtime.spi.IExporter;
import org.hibernate.tool.eclipse.orm.runtime.spi.IForeignKey;
import org.hibernate.tool.eclipse.orm.runtime.spi.IGenericExporter;
import org.hibernate.tool.eclipse.orm.runtime.spi.IHQLQueryPlan;
import org.hibernate.tool.eclipse.orm.runtime.spi.IHbm2DDLExporter;
import org.hibernate.tool.eclipse.orm.runtime.spi.IHibernateMappingExporter;
import org.hibernate.tool.eclipse.orm.runtime.spi.IOverrideRepository;
import org.hibernate.tool.eclipse.orm.runtime.spi.IPersistentClass;
import org.hibernate.tool.eclipse.orm.runtime.spi.IProperty;
import org.hibernate.tool.eclipse.orm.runtime.spi.IQuery;
import org.hibernate.tool.eclipse.orm.runtime.spi.IQueryExporter;
import org.hibernate.tool.eclipse.orm.runtime.spi.IQueryTranslator;
import org.hibernate.tool.eclipse.orm.runtime.spi.IReverseEngineeringStrategy;
import org.hibernate.tool.eclipse.orm.runtime.spi.ISchemaExport;
import org.hibernate.tool.eclipse.orm.runtime.spi.ISession;
import org.hibernate.tool.eclipse.orm.runtime.spi.ISessionFactory;
import org.hibernate.tool.eclipse.orm.runtime.spi.IType;
import org.hibernate.tool.eclipse.orm.runtime.spi.ITypeFactory;

public class FacadeFactoryImpl  extends AbstractFacadeFactory {

	@Override
	public ClassLoader getClassLoader() {
		return FacadeFactoryImpl.class.getClassLoader();
	}

	public IOverrideRepository createOverrideRepository(Object target) {
		return new OverrideRepositoryFacadeImpl(this, target);
	}
	
	@Override
	public ISchemaExport createSchemaExport(Object target) {
		return new SchemaExportFacadeImpl(this, target);
	}
	
	@Override
	public IGenericExporter createGenericExporter(Object target) {
		return new GenericExporterFacadeImpl(this, target);
	}

	@Override
	public IExporter createExporter(Object target) {
		return new ExporterFacadeImpl(this, target);
	}
	
	@Override
	public IHbm2DDLExporter createHbm2DDLExporter(Object target) {
		return new Hbm2DDLExporterFacadeImpl(this, target);
	}

	@Override
	public IColumn createColumn(Object target) {
		return new ColumnFacadeImpl(this, target);
	}

	@Override
	public IConfiguration createConfiguration(Object target) {
		return new ConfigurationFacadeImpl(this, target);
	}

	@Override
	public ICriteria createCriteria(Object target) {
		return new CriteriaFacadeImpl(this, target);
	}

	@Override
	public IEnvironment createEnvironment() {
		return new EnvironmentFacadeImpl(this);
	}

	@Override
	public IForeignKey createForeignKey(Object target) {
		return new ForeignKeyFacadeImpl(this, target);
	}

	@Override
	public IHibernateMappingExporter createHibernateMappingExporter(Object target) {
		return new HibernateMappingExporterFacadeImpl(this, target);
	}

	@Override
	public IClassMetadata createClassMetadata(Object target) {
		return new ClassMetadataFacadeImpl(this, target);
	}
	
	@Override
	public IHQLQueryPlan createHQLQueryPlan(Object target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IQuery createQuery(Object target) {
		return new QueryFacadeImpl(this, target);
	}
	
	@Override
	public IQueryExporter createQueryExporter(Object target) {
		return new QueryExporterFacadeImpl(this, target);
	}

	@Override
	public IQueryTranslator createQueryTranslator(Object target) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public IPersistentClass createPersistentClass(Object target) {
		return new PersistentClassFacadeImpl(this, target);
	}
	
	@Override
	public IReverseEngineeringStrategy createReverseEngineeringStrategy(Object target) {
		return new ReverseEngineeringStrategyFacadeImpl(this, target);
	}

	@Override
	public ISessionFactory createSessionFactory(Object target) {
		return new SessionFactoryFacadeImpl(this, target);
	}
	
	@Override
	public ISession createSession(Object target) {
		return new SessionFacadeImpl(this, target);
	}

	@Override
	public IPersistentClass createSpecialRootClass(IProperty property) {
		return new SpecialRootClassFacadeImpl(this, property);
	}
	
	@Override
	public IType createType(Object target) {
		return new TypeFacadeImpl(this, target);
	}

	@Override
	public ITypeFactory createTypeFactory() {
		return new TypeFactoryFacadeImpl(this, null);
	}

}
