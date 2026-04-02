package org.hibernate.tool.eclipse.runtime.v_6_0.internal;

import org.hibernate.persister.entity.EntityPersister;
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

	@Override
	public Object getTuplizerPropertyValue(Object entity, int i) {
		return ((EntityPersister)getTarget()).getPropertyValue(entity, i);
	}
	
	@Override
	public Integer getPropertyIndexOrNull(String id) {
		return ((EntityPersister)getTarget()).getEntityMetamodel().getPropertyIndexOrNull(id);
	}
	
}
