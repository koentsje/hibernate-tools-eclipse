package org.hibernate.tool.eclipse.runtime.v_6_0.internal;

import org.hibernate.tool.eclipse.runtime.common.AbstractReverseEngineeringStrategyFacade;
import org.hibernate.tool.eclipse.runtime.common.IFacadeFactory;

public class ReverseEngineeringStrategyFacadeImpl extends AbstractReverseEngineeringStrategyFacade {

	public ReverseEngineeringStrategyFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}

	protected String getReverseEngineeringSettingsClassName() {
		return "org.hibernate.tool.api.reveng.RevengSettings";
	}

}
