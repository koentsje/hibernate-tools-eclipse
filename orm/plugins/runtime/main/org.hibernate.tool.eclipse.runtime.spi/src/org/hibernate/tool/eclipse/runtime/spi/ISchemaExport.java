package org.hibernate.tool.eclipse.runtime.spi;

import java.util.List;

public interface ISchemaExport {

	void create();
	List<Throwable> getExceptions();

}
