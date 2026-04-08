package org.hibernate.tool.eclipse.runtime.common;

import org.hibernate.tool.eclipse.common.runtime.Util;

import org.hibernate.tool.eclipse.runtime.spi.IReverseEngineeringSettings;
import org.hibernate.tool.eclipse.runtime.spi.IReverseEngineeringStrategy;

public abstract class AbstractReverseEngineeringStrategyFacade 
extends AbstractFacade 
implements IReverseEngineeringStrategy {

	public AbstractReverseEngineeringStrategyFacade(
			IFacadeFactory facadeFactory, 
			Object target) {
		super(facadeFactory, target);
	}

	@Override
	public void setSettings(IReverseEngineeringSettings settings) {
		assert settings instanceof IFacade;
		Util.invokeMethod(
				getTarget(), 
				"setSettings", 
				new Class[] { getReverseEngineeringSettingsClass() }, 
				new Object[] { ((IFacade)settings).getTarget() });
	}
	
	protected Class<?> getReverseEngineeringSettingsClass() {
		return Util.getClass(
				getReverseEngineeringSettingsClassName(), 
				getFacadeFactoryClassLoader());
	}
	
	protected String getReverseEngineeringSettingsClassName() {
		return "org.hibernate.cfg.reveng.ReverseEngineeringSettings";
	}

}
