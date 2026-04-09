package org.hibernate.tool.eclipse.orm.runtime.spi;

import java.io.File;

public interface IOverrideRepository {

	void addFile(File file);
	IReverseEngineeringStrategy getReverseEngineeringStrategy(IReverseEngineeringStrategy res);
	void addTableFilter(ITableFilter tf);

}
