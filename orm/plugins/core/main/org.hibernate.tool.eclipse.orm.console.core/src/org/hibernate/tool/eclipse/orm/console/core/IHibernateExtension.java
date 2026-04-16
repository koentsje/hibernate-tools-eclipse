package org.hibernate.tool.eclipse.orm.console.core;

import java.net.URL;

import org.hibernate.tool.eclipse.orm.console.core.execution.ExecutionContext;
import org.hibernate.tool.eclipse.orm.runtime.spi.IConfiguration;
import org.hibernate.tool.eclipse.orm.runtime.spi.IService;
import org.hibernate.tool.eclipse.orm.runtime.spi.ISessionFactory;

public interface IHibernateExtension {

	void build();

	void buildSessionFactory();

	boolean closeSessionFactory();

	void buildMappings();

	boolean reset();

	IConfiguration buildWith(IConfiguration cfg, boolean includeMappings);

	IConfiguration getConfiguration();

	boolean hasConfiguration();

	ISessionFactory getSessionFactory();

	boolean isSessionFactoryCreated();

	Object execute(ExecutionContext.Command c);

	boolean hasExecutionContext();

	URL findResource(String name);

	ClassLoader getClassLoader();

	IService getHibernateService();

	String getHibernateVersion();

	String getConsoleConfigurationName();

}
