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

	QueryPage executeHQLQuery(String hql, QueryInputModel queryParameters);

	QueryPage executeCriteriaQuery(String criteriaCode, QueryInputModel model);

	String generateSQL(String query);

	IService getHibernateService();

	String getHibernateVersion();

	String getConsoleConfigurationName();

}
