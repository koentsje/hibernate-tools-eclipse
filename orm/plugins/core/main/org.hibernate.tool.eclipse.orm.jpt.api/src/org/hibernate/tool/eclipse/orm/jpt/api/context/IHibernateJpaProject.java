package org.hibernate.tool.eclipse.orm.jpt.api.context;

import org.eclipse.jpt.jpa.core.JpaProject;
import org.hibernate.tool.eclipse.orm.runtime.spi.INamingStrategy;

public interface IHibernateJpaProject extends JpaProject {

	INamingStrategy getNamingStrategy();

	boolean isNamingStrategyEnabled();

}
