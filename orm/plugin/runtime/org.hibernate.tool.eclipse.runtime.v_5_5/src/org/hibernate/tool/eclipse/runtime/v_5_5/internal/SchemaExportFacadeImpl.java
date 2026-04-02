package org.hibernate.tool.eclipse.runtime.v_5_5.internal;

import java.util.EnumSet;

import org.hibernate.boot.Metadata;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.hibernate.tool.eclipse.runtime.common.AbstractSchemaExportFacade;
import org.hibernate.tool.eclipse.runtime.common.IFacadeFactory;
import org.hibernate.tool.eclipse.runtime.spi.IConfiguration;

public class SchemaExportFacadeImpl extends AbstractSchemaExportFacade {

	private Metadata metadata = null;

	public SchemaExportFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}

	public void setConfiguration(IConfiguration configuration) {
		metadata = ((ConfigurationFacadeImpl)configuration).getMetadata();
	}

	@Override
	public void create() {
		((SchemaExport)getTarget()).create(EnumSet.of(TargetType.DATABASE), metadata);
	}

}
