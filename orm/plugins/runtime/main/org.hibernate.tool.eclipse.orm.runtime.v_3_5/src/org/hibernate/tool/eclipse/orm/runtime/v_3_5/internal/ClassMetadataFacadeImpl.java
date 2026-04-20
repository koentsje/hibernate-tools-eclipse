package org.hibernate.tool.eclipse.orm.runtime.v_3_5.internal;

import org.hibernate.EntityMode;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractClassMetadataFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;
import org.hibernate.tool.eclipse.common.runtime.HibernateRuntimeException;

public class ClassMetadataFacadeImpl extends AbstractClassMetadataFacade {
	
	public ClassMetadataFacadeImpl(
			IFacadeFactory facadeFactory,
			ClassMetadata classMetadata) {
		super(facadeFactory, classMetadata);
	}
	
	@Override
	public Class<?> getMappedClass() {
		return ((ClassMetadata)getTarget()).getMappedClass(EntityMode.POJO);
	}

	@Override
	public Object getPropertyValue(Object object, String name) {
		try {
			return ((ClassMetadata)getTarget()).getPropertyValue(
					object, name, EntityMode.POJO);
		} catch (org.hibernate.HibernateException e) {
			throw new HibernateRuntimeException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public Object getTuplizerPropertyValue(Object entity, int i) {
		Object result = null;
		if (isInstanceOfAbstractEntityPersister()) {
			result = ((EntityPersister)getTarget())
					.getEntityMetamodel()
					.getTuplizer(EntityMode.POJO)
					.getPropertyValue(entity, i);
		}
		return result;
	}

	protected String getSessionImplementorClassName() {
		return "org.hibernate.engine.SessionImplementor";
	}

}
