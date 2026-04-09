package org.hibernate.tool.eclipse.runtime.v_3_5.internal;

import org.hibernate.type.Type;
import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractTypeFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;

public class TypeFacadeImpl extends AbstractTypeFacade {
	
	public TypeFacadeImpl(
			IFacadeFactory facadeFactory,
			Type type) {
		super(facadeFactory, type);
	}	

	protected String getStringRepresentableTypeClassName() {
		return "org.hibernate.type.NullableType";
	}

}
