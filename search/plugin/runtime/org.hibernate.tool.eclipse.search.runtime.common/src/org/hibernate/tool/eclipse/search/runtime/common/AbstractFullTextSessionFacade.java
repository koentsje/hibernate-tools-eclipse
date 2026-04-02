package org.hibernate.tool.eclipse.search.runtime.common;

import org.hibernate.tool.eclipse.runtime.common.IFacade;
import org.hibernate.tool.eclipse.runtime.common.Util;
import org.hibernate.tool.eclipse.runtime.spi.IQuery;
import org.hibernate.tool.eclipse.search.runtime.spi.IFullTextSession;
import org.hibernate.tool.eclipse.search.runtime.spi.ILuceneQuery;
import org.hibernate.tool.eclipse.search.runtime.spi.ISearchFactory;

public abstract class AbstractFullTextSessionFacade extends AbstractFacade implements IFullTextSession {

	public AbstractFullTextSessionFacade(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}

	@Override
	public ISearchFactory getSearchFactory() {
		Object targetSearchFactory = Util.invokeMethod(
				getTarget(), 
				"getSearchFactory", 
				new Class[] {}, 
				new Object[] {});
		return getFacadeFactory().createSearchFactory(targetSearchFactory);
	}

	@Override
	public void createIndexerStartAndWait(Class<?>[] entities) {
		Object massIndexer = Util.invokeMethod(
				getTarget(), 
				"createIndexer", 
				new Class<?>[] { Class[].class }, 
				new Object[] { entities });
		Util.invokeMethod(
				massIndexer, 
				"startAndWait", 
				new Class[] {}, 
				new Object[] {});
	}

	@Override
	public IQuery createFullTextQuery(ILuceneQuery luceneQuery, Class<?> entity) {
		assert luceneQuery instanceof IFacade;
		Object targetQuery = Util.invokeMethod(
				getTarget(), 
				"createFullTextQuery", 
				new Class<?>[] { getLuceneQueryClass(), Class[].class }, 
				new Object[] { ((IFacade)luceneQuery).getTarget(), new Class[] { entity } });
		return getFacadeFactory().getHibernateFacadeFactory().createQuery(targetQuery);

	}
	
	protected Class<?> getLuceneQueryClass() {
		return Util.getClass("org.apache.lucene.search.Query", getFacadeFactoryClassLoader());
	}

}
