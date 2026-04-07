package org.hibernate.tool.eclipse.orm.runtime.common;

import java.util.List;
import java.util.Map;

import org.hibernate.tool.eclipse.runtime.spi.ITable;

public interface IDatabaseReader {
	
	Map<String, List<ITable>> collectDatabaseTables();

}
