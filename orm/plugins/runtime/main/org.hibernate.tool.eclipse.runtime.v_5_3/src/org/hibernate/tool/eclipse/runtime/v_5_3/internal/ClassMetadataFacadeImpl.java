package org.hibernate.tool.eclipse.runtime.v_5_3.internal;

import org.hibernate.tool.eclipse.runtime.common.AbstractClassMetadataFacade;
import org.hibernate.tool.eclipse.runtime.common.IFacadeFactory;

public class ClassMetadataFacadeImpl extends AbstractClassMetadataFacade {

	public ClassMetadataFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}

	@Override
	protected String getSessionImplementorClassName() {
		return "org.hibernate.engine.spi.SharedSessionContractImplementor";
	}

}
