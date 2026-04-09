package org.hibernate.tool.eclipse.orm.runtime.spi;

import java.util.Map;

public interface IExportPOJODelegate {

	void exportPojo(Map<Object, Object> map, Object pojoClass, String fullyQualifiedName);
	
}
