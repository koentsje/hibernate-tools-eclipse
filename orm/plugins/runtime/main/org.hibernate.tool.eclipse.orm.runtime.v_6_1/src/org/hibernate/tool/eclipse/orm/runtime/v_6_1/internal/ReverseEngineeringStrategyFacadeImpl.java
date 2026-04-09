package org.hibernate.tool.eclipse.orm.runtime.v_6_1.internal;

import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractReverseEngineeringStrategyFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;

public class ReverseEngineeringStrategyFacadeImpl extends AbstractReverseEngineeringStrategyFacade {

	public ReverseEngineeringStrategyFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}

	protected String getReverseEngineeringSettingsClassName() {
		return "org.hibernate.tool.api.reveng.RevengSettings";
	}

}
