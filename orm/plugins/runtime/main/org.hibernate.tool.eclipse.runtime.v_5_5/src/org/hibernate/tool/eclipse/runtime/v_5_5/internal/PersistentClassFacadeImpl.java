package org.hibernate.tool.eclipse.runtime.v_5_5.internal;

import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractPersistentClassFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;
import org.hibernate.tool.eclipse.common.runtime.Util;
import org.hibernate.tool.eclipse.runtime.spi.IValue;

public class PersistentClassFacadeImpl extends AbstractPersistentClassFacade {

	public PersistentClassFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}
	
	@Override
	public void setKey(IValue keyValueFacade) {
		Util.invokeMethod(
				getTarget(), 
				"setIdentifier", 
				new Class[] { getKeyValueClass() }, 
				new Object[] { ((IFacade)keyValueFacade).getTarget() });
	}

}
