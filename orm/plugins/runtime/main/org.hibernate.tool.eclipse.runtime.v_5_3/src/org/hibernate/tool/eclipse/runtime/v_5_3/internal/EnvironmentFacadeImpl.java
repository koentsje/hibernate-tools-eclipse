package org.hibernate.tool.eclipse.runtime.v_5_3.internal;

import org.hibernate.cfg.Environment;
import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractEnvironmentFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;

public class EnvironmentFacadeImpl extends AbstractEnvironmentFacade {

	public EnvironmentFacadeImpl(
			IFacadeFactory facadeFactory) {
		super(facadeFactory, null);
	}

	@Override
	public String getTransactionManagerStrategy() {
		return Environment.TRANSACTION_COORDINATOR_STRATEGY;
	}

}
