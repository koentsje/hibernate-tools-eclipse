package org.hibernate.tool.eclipse.runtime.v_5_6.internal;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractCriteriaFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;
import org.hibernate.tool.eclipse.runtime.spi.ICriteria;

public class CriteriaFacadeImpl extends AbstractCriteriaFacade implements ICriteria {

	public CriteriaFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}

	@Override
	public List<?> list() {
		return ((Query)getTarget()).getResultList();
	}

}
