package org.hibernate.tool.eclipse.orm.runtime.v_4_0.internal;

import org.hibernate.mapping.RootClass;
import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractSpecialRootClassFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;
import org.hibernate.tool.eclipse.orm.runtime.spi.IProperty;

public class SpecialRootClassFacadeImpl extends AbstractSpecialRootClassFacade {

	public SpecialRootClassFacadeImpl(
			IFacadeFactory facadeFactory, 
			IProperty property) {
		super(facadeFactory, new RootClass());
		this.property = property;
		generate();
	}

}
