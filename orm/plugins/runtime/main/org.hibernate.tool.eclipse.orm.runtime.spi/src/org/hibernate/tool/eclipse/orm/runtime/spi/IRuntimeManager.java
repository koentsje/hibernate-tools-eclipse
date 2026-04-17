package org.hibernate.tool.eclipse.orm.runtime.spi;

import java.net.URL;
import java.util.concurrent.Callable;

public interface IRuntimeManager {

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

	Object execute(Callable<Object> c);

	URL findResource(String name);

	ClassLoader getClassLoader();

	IService getHibernateService();

	String getHibernateVersion();

	String getConsoleConfigurationName();

}
