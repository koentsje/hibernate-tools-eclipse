package org.hibernate.tool.eclipse.orm.runtime.spi;

import java.util.List;

public interface IQueryExporter {

	void setQueries(List<String> queryStrings);
	void setFilename(String fileNAme);

}
