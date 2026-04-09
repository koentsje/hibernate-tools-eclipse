package org.hibernate.tool.eclipse.orm.runtime.spi;

import java.util.Iterator;

public interface IJoin {

	Iterator<IProperty> getPropertyIterator();

}
