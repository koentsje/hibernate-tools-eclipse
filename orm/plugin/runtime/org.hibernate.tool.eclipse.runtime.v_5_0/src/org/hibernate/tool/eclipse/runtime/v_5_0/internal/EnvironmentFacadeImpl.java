package org.hibernate.tool.eclipse.runtime.v_5_0.internal;

import org.hibernate.cfg.Environment;
import org.hibernate.tool.eclipse.runtime.common.AbstractEnvironmentFacade;
import org.hibernate.tool.eclipse.runtime.common.IFacadeFactory;

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
