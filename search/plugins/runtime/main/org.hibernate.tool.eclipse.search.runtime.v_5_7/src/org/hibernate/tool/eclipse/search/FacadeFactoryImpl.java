package org.hibernate.tool.eclipse.search;

import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;
import org.hibernate.tool.eclipse.search.runtime.common.AbstractFacadeFactory;

public class FacadeFactoryImpl extends AbstractFacadeFactory {
	
	private IFacadeFactory hibernateFacadeFactory = 
			new org.hibernate.tool.eclipse.orm.runtime.v_5_2.internal.FacadeFactoryImpl();

	@Override
	public ClassLoader getClassLoader() {
		return FacadeFactoryImpl.class.getClassLoader();
	}

	@Override
	public IFacadeFactory getHibernateFacadeFactory() {
		return hibernateFacadeFactory;
	}

}
