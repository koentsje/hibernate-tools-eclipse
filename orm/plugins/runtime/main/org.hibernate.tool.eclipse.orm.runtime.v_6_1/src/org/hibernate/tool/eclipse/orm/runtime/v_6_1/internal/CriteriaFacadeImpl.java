package org.hibernate.tool.eclipse.orm.runtime.v_6_1.internal;

import java.util.List;

import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractCriteriaFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;

import jakarta.persistence.Query;

public class CriteriaFacadeImpl extends AbstractCriteriaFacade {

	public CriteriaFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}

	@Override
	public List<?> list() {
		return ((Query)getTarget()).getResultList();
	}

}
