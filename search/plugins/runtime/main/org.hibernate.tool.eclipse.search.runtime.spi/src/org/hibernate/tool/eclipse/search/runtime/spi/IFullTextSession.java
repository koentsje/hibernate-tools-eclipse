package org.hibernate.tool.eclipse.search.runtime.spi;

import org.hibernate.tool.eclipse.orm.runtime.spi.IQuery;

public interface IFullTextSession {
	ISearchFactory getSearchFactory();
	void createIndexerStartAndWait(Class<?>[] entities);
	IQuery createFullTextQuery(ILuceneQuery luceneQuery, Class<?> entity);
}
