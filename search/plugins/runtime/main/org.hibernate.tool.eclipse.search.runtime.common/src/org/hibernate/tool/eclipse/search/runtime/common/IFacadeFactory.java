package org.hibernate.tool.eclipse.search.runtime.common;

import org.hibernate.tool.eclipse.runtime.spi.ISessionFactory;
import org.hibernate.tool.eclipse.search.runtime.spi.IAnalyzer;
import org.hibernate.tool.eclipse.search.runtime.spi.IFullTextSession;
import org.hibernate.tool.eclipse.search.runtime.spi.IQueryParser;
import org.hibernate.tool.eclipse.search.runtime.spi.ISearchFactory;

public interface IFacadeFactory {
	org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory getHibernateFacadeFactory();
	ClassLoader getClassLoader();
	IFullTextSession createFullTextSession(ISessionFactory sessionFactory);
	ISearchFactory createSearchFactory(Object target);
	IQueryParser createQueryParser(String defaultField, IAnalyzer analyzer);
	IAnalyzer createAnalyzerByName(String analyzerClassName, Object luceneVersion);
}
