package org.hibernate.console;

import org.hibernate.tool.eclipse.orm.runtime.spi.IConfiguration;
import org.hibernate.tool.eclipse.orm.runtime.spi.IService;

public interface IHibernateExtension {

	void build();

	void buildSessionFactory();

	boolean closeSessionFactory();

	void buildMappings();

	boolean reset();

	IConfiguration buildWith(IConfiguration cfg, boolean includeMappings);

	IConfiguration getConfiguration();

	boolean hasConfiguration();

	boolean isSessionFactoryCreated();

	boolean hasExecutionContext();

	IService getHibernateService();

	String getHibernateVersion();

	String getConsoleConfigurationName();

}
