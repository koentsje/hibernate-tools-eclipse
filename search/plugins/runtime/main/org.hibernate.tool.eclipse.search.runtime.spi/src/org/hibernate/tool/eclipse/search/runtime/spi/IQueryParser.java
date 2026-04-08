package org.hibernate.tool.eclipse.search.runtime.spi;

public interface IQueryParser {
	ILuceneQuery parse(String request);
}
