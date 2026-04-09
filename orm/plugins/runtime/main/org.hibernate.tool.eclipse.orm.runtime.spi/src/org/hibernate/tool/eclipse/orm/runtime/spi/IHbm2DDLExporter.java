package org.hibernate.tool.eclipse.orm.runtime.spi;

import java.util.Hashtable;

public interface IHbm2DDLExporter {

	void setExport(boolean parseBoolean);
	Hashtable<Object, Object> getProperties();

}
