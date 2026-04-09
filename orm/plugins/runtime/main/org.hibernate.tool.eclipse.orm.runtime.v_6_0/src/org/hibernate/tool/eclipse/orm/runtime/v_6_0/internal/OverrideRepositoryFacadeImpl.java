package org.hibernate.tool.eclipse.orm.runtime.v_6_0.internal;

import org.hibernate.tool.api.reveng.RevengStrategy;
import org.hibernate.tool.internal.reveng.strategy.OverrideRepository;
import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractOverrideRepositoryFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;
import org.hibernate.tool.eclipse.orm.runtime.spi.IReverseEngineeringStrategy;

public class OverrideRepositoryFacadeImpl extends AbstractOverrideRepositoryFacade {

	public OverrideRepositoryFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}
	
	@Override
	public IReverseEngineeringStrategy getReverseEngineeringStrategy(
			IReverseEngineeringStrategy res) {
		assert res instanceof IFacade;
		RevengStrategy resTarget = (RevengStrategy)((IFacade)res).getTarget();
		return getFacadeFactory().createReverseEngineeringStrategy(
				((OverrideRepository)getTarget()).getReverseEngineeringStrategy(resTarget));
	}
	
	@Override
	protected String getTableFilterClassName() {
		return "org.hibernate.tool.internal.reveng.strategy.TableFilter";
	}
	
}
