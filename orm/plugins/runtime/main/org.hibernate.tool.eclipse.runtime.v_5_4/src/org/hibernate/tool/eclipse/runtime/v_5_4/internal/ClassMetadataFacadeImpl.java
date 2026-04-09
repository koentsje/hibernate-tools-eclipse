package org.hibernate.tool.eclipse.runtime.v_5_4.internal;

import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractClassMetadataFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;

public class ClassMetadataFacadeImpl extends AbstractClassMetadataFacade {

	public ClassMetadataFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}

	@Override
	protected String getSessionImplementorClassName() {
		return "org.hibernate.engine.spi.SharedSessionContractImplementor";
	}

}
