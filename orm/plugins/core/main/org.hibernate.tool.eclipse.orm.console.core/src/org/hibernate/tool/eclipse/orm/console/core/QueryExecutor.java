package org.hibernate.tool.eclipse.orm.console.core;

import org.hibernate.tool.eclipse.orm.query.HQLQueryPage;
import org.hibernate.tool.eclipse.orm.query.JavaPage;
import org.hibernate.tool.eclipse.orm.query.QueryHelper;
import org.hibernate.tool.eclipse.orm.query.QueryInputModel;
import org.hibernate.tool.eclipse.orm.query.QueryPage;
import org.hibernate.tool.eclipse.orm.runtime.spi.ISession;

public class QueryExecutor {

	private final ConsoleConfiguration consoleConfiguration;
	private int execcount;

	public QueryExecutor(ConsoleConfiguration consoleConfiguration) {
		this.consoleConfiguration = consoleConfiguration;
	}

	public QueryPage executeHQLQuery(final String hql) {
		return executeHQLQuery(hql, new QueryInputModel(
				consoleConfiguration.getRuntimeManager().getHibernateService()));
	}

	public QueryPage executeHQLQuery(final String hql, final QueryInputModel queryParameters) {
		QueryPage qp = (QueryPage) consoleConfiguration.execute(() -> {
			ISession session = consoleConfiguration.getSessionFactory().openSession();
			QueryPage page = new HQLQueryPage(
					consoleConfiguration.getRuntimeManager().getHibernateService(),
					consoleConfiguration.getName(), hql, queryParameters);
			page.setSession(session);
			return page;
		});
		qp.setId(++execcount);
		consoleConfiguration.fireQueryPageCreated(qp);
		return qp;
	}

	public QueryPage executeBSHQuery(final String queryString, final QueryInputModel model) {
		QueryPage qp = (QueryPage) consoleConfiguration.execute(() -> {
			ISession session = consoleConfiguration.getSessionFactory().openSession();
			QueryPage page = new JavaPage(consoleConfiguration.getName(), queryString, model);
			page.setSession(session);
			return page;
		});
		qp.setId(++execcount);
		consoleConfiguration.fireQueryPageCreated(qp);
		return qp;
	}

	public String generateSQL(final String query) {
		return (String) consoleConfiguration.execute(() -> {
			return QueryHelper.generateSQL(
					consoleConfiguration.getSessionFactory(), query,
					consoleConfiguration.getRuntimeManager().getHibernateService());
		});
	}
}
