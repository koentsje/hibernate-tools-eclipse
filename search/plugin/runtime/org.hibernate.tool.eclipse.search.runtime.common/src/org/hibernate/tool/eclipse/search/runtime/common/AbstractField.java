package org.hibernate.tool.eclipse.search.runtime.common;

import org.hibernate.tool.eclipse.runtime.common.Util;
import org.hibernate.tool.eclipse.search.runtime.spi.IField;

public abstract class AbstractField extends AbstractFacade implements IField {

	public AbstractField(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}

	@Override
	public String name() {
		return Util.invokeMethod(getTarget(), "name", new Class[] {}, new Object[] {}).toString();
	}
	
	@Override
	public String stringValue() {
		return Util.invokeMethod(getTarget(), "stringValue", new Class[] {}, new Object[] {}).toString();
	}

}
