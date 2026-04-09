package org.hibernate.tool.eclipse.search;

import org.apache.lucene.util.Version;
import org.hibernate.tool.eclipse.orm.runtime.spi.IService;
import org.hibernate.tool.eclipse.orm.runtime.spi.RuntimeServiceManager;
import org.hibernate.tool.eclipse.search.runtime.common.AbstractHSearchService;
import org.hibernate.tool.eclipse.search.runtime.common.IFacadeFactory;
import org.hibernate.tool.eclipse.search.runtime.spi.IAnalyzer;
import org.hibernate.tool.eclipse.search.runtime.spi.IHSearchService;

public class HSearchServiceImpl extends AbstractHSearchService implements IHSearchService {
	
	private IFacadeFactory facadeFactory = new FacadeFactoryImpl();
	
	@Override
	public IService getHibernateService() {
		return RuntimeServiceManager.getInstance().findService("5.1");
	}
	
	@Override
	public IFacadeFactory getFacadeFactory() {
		return this.facadeFactory;
	}
	
	public IAnalyzer getAnalyzerByName(String analyzerClassName) {
		return facadeFactory.createAnalyzerByName(analyzerClassName, Version.LUCENE_5_3_1);
	}
}
