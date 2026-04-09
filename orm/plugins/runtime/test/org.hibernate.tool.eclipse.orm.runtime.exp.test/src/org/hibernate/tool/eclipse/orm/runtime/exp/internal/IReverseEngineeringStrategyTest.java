package org.hibernate.tool.eclipse.orm.runtime.exp.internal;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Field;

import org.hibernate.tool.internal.reveng.strategy.AbstractStrategy;
import org.hibernate.tool.orm.jbt.wrp.WrapperFactory;
import org.hibernate.tool.eclipse.orm.runtime.common.GenericFacadeFactory;
import org.hibernate.tool.eclipse.common.runtime.IFacade;
import org.hibernate.tool.eclipse.orm.runtime.spi.IReverseEngineeringSettings;
import org.hibernate.tool.eclipse.orm.runtime.spi.IReverseEngineeringStrategy;
import org.junit.jupiter.api.Test;

public class IReverseEngineeringStrategyTest {

	@Test
	public void testSetSettings() throws Exception {
		IReverseEngineeringStrategy revengStrategyFacade = (IReverseEngineeringStrategy)GenericFacadeFactory.createFacade(
				IReverseEngineeringStrategy.class, 
				WrapperFactory.createRevengStrategyWrapper());
		Object revengStrategyTarget = ((IFacade)revengStrategyFacade).getTarget();
		IReverseEngineeringSettings revengSettingsFacade = 
				(IReverseEngineeringSettings)GenericFacadeFactory.createFacade(
						IReverseEngineeringSettings.class, 
						WrapperFactory.createRevengSettingsWrapper(revengStrategyTarget));
		Object revengSettingsTarget = ((IFacade)revengSettingsFacade).getTarget();
		Field field = AbstractStrategy.class.getDeclaredField("settings");
		field.setAccessible(true);
		assertNotSame(field.get(revengStrategyTarget), revengSettingsTarget);
		revengStrategyFacade.setSettings(revengSettingsFacade);
		assertSame(field.get(revengStrategyTarget), revengSettingsTarget);
	}
	
}
