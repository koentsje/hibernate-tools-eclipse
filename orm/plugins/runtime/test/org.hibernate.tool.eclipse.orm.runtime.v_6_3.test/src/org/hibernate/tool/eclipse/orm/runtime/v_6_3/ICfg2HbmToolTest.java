package org.hibernate.tool.eclipse.orm.runtime.v_6_3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hibernate.mapping.BasicValue;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.RootClass;
import org.hibernate.tool.orm.jbt.util.DummyMetadataBuildingContext;
import org.hibernate.tool.orm.jbt.wrp.WrapperFactory;
import org.hibernate.tool.eclipse.orm.runtime.common.GenericFacadeFactory;
import org.hibernate.tool.eclipse.common.runtime.IFacade;
import org.hibernate.tool.eclipse.orm.runtime.spi.ICfg2HbmTool;
import org.hibernate.tool.eclipse.orm.runtime.spi.IPersistentClass;
import org.hibernate.tool.eclipse.orm.runtime.spi.IProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ICfg2HbmToolTest {

	private ICfg2HbmTool facade = null;
	
	@BeforeEach
	public void beforeEach() {
		facade = (ICfg2HbmTool)GenericFacadeFactory.createFacade(
				ICfg2HbmTool.class,
				WrapperFactory.createCfg2HbmWrapper());;
	}

	@Test
	public void testGetTagPersistentClass() {
		IPersistentClass persistentClassFacade = (IPersistentClass)GenericFacadeFactory.createFacade(
				IPersistentClass.class, 
				WrapperFactory.createRootClassWrapper());
		assertEquals("class", facade.getTag(persistentClassFacade));
	}

	@Test
	public void testGetTagProperty() throws Exception {
		IProperty propertyFacade = (IProperty)GenericFacadeFactory.createFacade(
				IProperty.class, 
				WrapperFactory.createPropertyWrapper());
		Property propertyTarget = (Property)((IFacade)propertyFacade).getTarget();
		RootClass rc = new RootClass(DummyMetadataBuildingContext.INSTANCE);
		BasicValue basicValue = new BasicValue(DummyMetadataBuildingContext.INSTANCE);
		basicValue.setTypeName("foobar");
		propertyTarget.setValue(basicValue);
		propertyTarget.setPersistentClass(rc);
		rc.setVersion(propertyTarget);
		assertEquals("version", facade.getTag(propertyFacade));
	}
	
}
