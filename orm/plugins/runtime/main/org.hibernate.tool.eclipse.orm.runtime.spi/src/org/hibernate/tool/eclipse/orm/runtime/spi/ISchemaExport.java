package org.hibernate.tool.eclipse.orm.runtime.spi;

import java.util.List;

public interface ISchemaExport {

	void create();
	List<Throwable> getExceptions();

}
