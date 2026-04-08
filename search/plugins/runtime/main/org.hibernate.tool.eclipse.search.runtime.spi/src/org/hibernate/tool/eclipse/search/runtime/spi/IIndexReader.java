package org.hibernate.tool.eclipse.search.runtime.spi;

public interface IIndexReader {
	
	int numDocs();
	
	IDocument document(int n);

}
