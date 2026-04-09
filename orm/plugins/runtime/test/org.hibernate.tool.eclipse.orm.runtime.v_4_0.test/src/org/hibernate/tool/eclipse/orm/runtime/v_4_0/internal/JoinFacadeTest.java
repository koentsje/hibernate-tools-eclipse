package org.hibernate.tool.eclipse.orm.runtime.v_4_0.internal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.hibernate.mapping.Join;
import org.hibernate.mapping.Property;
import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractJoinFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;
import org.hibernate.tool.eclipse.orm.runtime.spi.IJoin;
import org.hibernate.tool.eclipse.orm.runtime.spi.IProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JoinFacadeTest {

	private final IFacadeFactory FACADE_FACTORY = new FacadeFactoryImpl();
	
	private IJoin joinFacade = null;
	private Join joinTarget = null;
	
	@BeforeEach
	public void beforeEach() {
		joinTarget = new Join();
	}
	
	@Test
	public void testGetPropertyIterator() {
		joinFacade = new AbstractJoinFacade(FACADE_FACTORY, joinTarget) {};
		Iterator<IProperty> propertyIterator = joinFacade.getPropertyIterator();
		assertFalse(propertyIterator.hasNext());
		Property property = new Property();
		joinTarget.addProperty(property);
		joinFacade = new AbstractJoinFacade(FACADE_FACTORY, joinTarget) {};
		propertyIterator = joinFacade.getPropertyIterator();
		assertTrue(propertyIterator.hasNext());
		assertSame(property, (((IFacade)propertyIterator.next()).getTarget()));
	}
	
}
