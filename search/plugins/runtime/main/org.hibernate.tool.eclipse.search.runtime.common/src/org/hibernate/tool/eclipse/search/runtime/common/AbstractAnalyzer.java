package org.hibernate.tool.eclipse.search.runtime.common;

import org.hibernate.tool.eclipse.search.runtime.spi.IAnalyzer;

public abstract class AbstractAnalyzer extends AbstractFacade implements IAnalyzer {

	public AbstractAnalyzer(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}

}
