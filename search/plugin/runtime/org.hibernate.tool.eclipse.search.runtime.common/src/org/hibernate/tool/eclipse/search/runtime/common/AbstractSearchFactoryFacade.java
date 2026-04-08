package org.hibernate.tool.eclipse.search.runtime.common;

import java.util.Set;

import org.hibernate.tool.eclipse.common.runtime.Util;
import org.hibernate.tool.eclipse.search.runtime.spi.IIndexReader;
import org.hibernate.tool.eclipse.search.runtime.spi.ISearchFactory;

public abstract class AbstractSearchFactoryFacade extends AbstractFacade implements ISearchFactory {

	public AbstractSearchFactoryFacade(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<?>> getIndexedTypes() {
		return (Set<Class<?>>)Util.invokeMethod(
				getTarget(), 
				"getIndexedTypes", 
				new Class[] {}, 
				new Object[] {});
	}

	@Override
	public IIndexReader getIndexReader(Class<?>... entities) {
		Object indexReaderAccessor = 
				Util.invokeMethod(
						getTarget(), 
						"getIndexReaderAccessor", 
						new Class[] {}, 
						new Object[] {});
		Object targetIndexReader = 
				Util.invokeMethod(
						indexReaderAccessor, 
						"open", 
						new Class[] { Class[].class }, 
						new Object[] { entities });
		return new AbstractIndexReader(getFacadeFactory(), targetIndexReader) {};
	}

}
