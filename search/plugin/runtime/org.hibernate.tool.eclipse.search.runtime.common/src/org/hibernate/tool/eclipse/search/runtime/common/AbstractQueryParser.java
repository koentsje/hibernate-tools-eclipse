package org.hibernate.tool.eclipse.search.runtime.common;

import org.hibernate.tool.eclipse.runtime.common.Util;
import org.hibernate.tool.eclipse.search.runtime.spi.ILuceneQuery;
import org.hibernate.tool.eclipse.search.runtime.spi.IQueryParser;

public abstract class AbstractQueryParser extends AbstractFacade implements IQueryParser {

	public AbstractQueryParser(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}

	@Override
	public ILuceneQuery parse(String request) {
		Object targetLuceneQuery = Util.invokeMethod(
				getTarget(), 
				"parse", 
				new Class[] { String.class }, 
				new Object[] { request });
		return new AbstractLuceneQuery(getFacadeFactory(), targetLuceneQuery) {};
	}

}
