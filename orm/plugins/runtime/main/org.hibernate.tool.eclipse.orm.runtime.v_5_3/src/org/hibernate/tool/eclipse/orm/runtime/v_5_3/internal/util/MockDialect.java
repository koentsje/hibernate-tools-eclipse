package org.hibernate.tool.eclipse.orm.runtime.v_5_3.internal.util;

import org.hibernate.dialect.Dialect;

public class MockDialect extends Dialect {

	public int getVersion() {
		return 0;
	}
	
}
