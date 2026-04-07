package org.hibernate.tool.eclipse.runtime.spi;

import java.util.List;

public interface ICriteria {

	void setMaxResults(int intValue);
	List<?> list();

}
