package org.hibernate.tool.eclipse.orm.console.core;

import org.hibernate.tool.eclipse.orm.query.HQLQueryPage;
import org.hibernate.tool.eclipse.orm.query.CriteriaQueryPage;
import org.hibernate.tool.eclipse.orm.query.QueryHelper;
import org.hibernate.tool.eclipse.orm.query.QueryInputModel;
import org.hibernate.tool.eclipse.orm.query.IQueryPage;
import org.hibernate.tool.eclipse.orm.runtime.spi.ISession;

public class QueryExecutor {

	private final ConsoleConfiguration consoleConfiguration;
	private int execcount;

	public QueryExecutor(ConsoleConfiguration consoleConfiguration) {
		this.consoleConfiguration = consoleConfiguration;
	}

	public IQueryPage executeHQLQuery(final String hql) {
		return executeHQLQuery(hql, new QueryInputModel(
				consoleConfiguration.getRuntimeManager().getHibernateService()));
	}

	public IQueryPage executeHQLQuery(final String hql, final QueryInputModel queryParameters) {
		IQueryPage qp = (IQueryPage) consoleConfiguration.execute(() -> {
			ISession session = consoleConfiguration.getSessionFactory().openSession();
			IQueryPage page = new HQLQueryPage(
					consoleConfiguration.getRuntimeManager().getHibernateService(),
					consoleConfiguration.getName(), hql, queryParameters);
			page.setSession(session);
			return page;
		});
		qp.setId(++execcount);
		consoleConfiguration.fireQueryPageCreated(qp);
		return qp;
	}

	public IQueryPage executeBSHQuery(final String queryString, final QueryInputModel model) {
		IQueryPage qp = (IQueryPage) consoleConfiguration.execute(() -> {
			ISession session = consoleConfiguration.getSessionFactory().openSession();
			IQueryPage page = new CriteriaQueryPage(consoleConfiguration.getName(), queryString, model);
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
