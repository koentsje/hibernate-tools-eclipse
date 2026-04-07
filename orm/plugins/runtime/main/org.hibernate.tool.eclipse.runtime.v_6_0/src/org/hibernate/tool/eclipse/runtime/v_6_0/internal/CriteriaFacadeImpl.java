package org.hibernate.tool.eclipse.runtime.v_6_0.internal;

import java.util.List;

import jakarta.persistence.Query;

import org.hibernate.tool.eclipse.runtime.common.AbstractCriteriaFacade;
import org.hibernate.tool.eclipse.runtime.common.IFacadeFactory;

public class CriteriaFacadeImpl extends AbstractCriteriaFacade {

	public CriteriaFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}
	
	@Override
	public List<?> list() {
		return ((Query)getTarget()).getResultList();
	}

}
