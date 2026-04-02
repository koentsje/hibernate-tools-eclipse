package org.hibernate.tool.eclipse.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;
import org.hibernate.tool.eclipse.runtime.common.IFacade;
import org.hibernate.tool.eclipse.runtime.common.IFacadeFactory;
import org.hibernate.tool.eclipse.runtime.common.Util;
import org.hibernate.tool.eclipse.search.runtime.common.AbstractFacadeFactory;
import org.hibernate.tool.eclipse.search.runtime.common.AbstractQueryParser;
import org.hibernate.tool.eclipse.search.runtime.spi.IAnalyzer;
import org.hibernate.tool.eclipse.search.runtime.spi.IQueryParser;

public class FacadeFactoryImpl extends AbstractFacadeFactory {
	
	private IFacadeFactory hibernateFacadeFactory = 
			new org.hibernate.tool.eclipse.runtime.v_4_0.internal.FacadeFactoryImpl();
	
	@Override
	protected Class<?> getQueryParserClass() {
		return Util.getClass("org.apache.lucene.queryParser.QueryParser", getClassLoader());
	}

	@Override
	public ClassLoader getClassLoader() {
		return FacadeFactoryImpl.class.getClassLoader();
	}

	@Override
	public IFacadeFactory getHibernateFacadeFactory() {
		return hibernateFacadeFactory;
	}
	
	@Override
	public IQueryParser createQueryParser(String defaultField, IAnalyzer analyzer) {
		Analyzer luceneAnalyzer = (Analyzer)((IFacade)analyzer).getTarget();
		QueryParser targetParser = new QueryParser(Version.LUCENE_34, defaultField, luceneAnalyzer);
		return new AbstractQueryParser(this, targetParser) {};
	}
}
