package org.hibernate.tool.eclipse.orm.runtime.spi;


public interface IHQLCodeAssist {

	void codeComplete(String query, int currentOffset,
			IHQLCompletionHandler handler);

}
