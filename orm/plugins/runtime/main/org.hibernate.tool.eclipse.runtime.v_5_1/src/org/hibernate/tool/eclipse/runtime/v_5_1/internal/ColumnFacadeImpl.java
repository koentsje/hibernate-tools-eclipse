package org.hibernate.tool.eclipse.runtime.v_5_1.internal;

import java.util.Properties;

import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.dialect.spi.DialectFactory;
import org.hibernate.mapping.Column;
import org.hibernate.tool.util.MetadataHelper;
import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractColumnFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;
import org.hibernate.tool.eclipse.runtime.spi.IConfiguration;

public class ColumnFacadeImpl extends AbstractColumnFacade {

	public ColumnFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}

	@Override
	public String getSqlType(IConfiguration configuration) {
		Column targetColumn = (Column)getTarget();
		Configuration configurationTarget = 
				(Configuration)((IFacade)configuration).getTarget();
		Properties properties = configurationTarget.getProperties();
		StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder();
		ssrb.applySettings(properties);
		StandardServiceRegistry ssr = ssrb.build();
		DialectFactory df = ssr.getService(DialectFactory.class);
		Dialect dialectTarget = df.buildDialect(properties, null);
		return targetColumn.getSqlType(
				dialectTarget, 
				MetadataHelper.getMetadata(configurationTarget));
	}
	
}
