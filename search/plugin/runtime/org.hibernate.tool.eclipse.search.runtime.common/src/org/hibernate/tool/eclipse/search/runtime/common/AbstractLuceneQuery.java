package org.hibernate.tool.eclipse.search.runtime.common;

import org.hibernate.tool.eclipse.search.runtime.spi.ILuceneQuery;

public abstract class AbstractLuceneQuery extends AbstractFacade implements ILuceneQuery {

	public AbstractLuceneQuery(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}

}
