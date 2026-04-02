package org.hibernate.tool.eclipse.runtime.v_5_2.internal;

import org.hibernate.tool.eclipse.runtime.common.AbstractPersistentClassFacade;
import org.hibernate.tool.eclipse.runtime.common.IFacade;
import org.hibernate.tool.eclipse.runtime.common.IFacadeFactory;
import org.hibernate.tool.eclipse.runtime.common.Util;
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
