package org.hibernate.tool.eclipse.search.runtime.spi;

import java.util.Set;

public interface ISearchFactory {
	Set<Class<?>> getIndexedTypes();
	IIndexReader getIndexReader(Class<?>... entities);
}
