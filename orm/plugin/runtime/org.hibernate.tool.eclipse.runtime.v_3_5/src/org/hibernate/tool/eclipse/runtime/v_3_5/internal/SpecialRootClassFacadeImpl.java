package org.hibernate.tool.eclipse.runtime.v_3_5.internal;

import org.hibernate.mapping.RootClass;
import org.hibernate.tool.eclipse.runtime.common.AbstractSpecialRootClassFacade;
import org.hibernate.tool.eclipse.runtime.common.IFacadeFactory;
import org.hibernate.tool.eclipse.runtime.spi.IProperty;

public class SpecialRootClassFacadeImpl extends AbstractSpecialRootClassFacade {

	public SpecialRootClassFacadeImpl(
			IFacadeFactory facadeFactory, 
			IProperty property) {
		super(facadeFactory, new RootClass());
		this.property = property;
		generate();
	}

}
