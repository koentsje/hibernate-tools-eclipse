package org.hibernate.tool.eclipse.runtime.v_6_0.internal;

import org.hibernate.type.BasicType;
import org.hibernate.type.EntityType;
import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractTypeFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;
import org.hibernate.tool.eclipse.runtime.v_6_0.internal.legacy.IntegerType;

public class TypeFacadeImpl extends AbstractTypeFacade {

	public TypeFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}
	
	@Override
	public String getReturnedClassName() {
		if (isEntityType()) {
			return ((EntityType)getTarget()).getAssociatedEntityName();
		} else {
			return super.getReturnedClassName();
		}
	}

	@Override
	public String toString(Object value) {
		String result = null;
		Object target = getTarget();
		if (target instanceof BasicType) {
			return ((BasicType)target).getJavaTypeDescriptor().toString(value);
		}
		return result;
	}
	
	@Override
	protected String getIntegerTypeClassName() {
		return IntegerType.class.getName();
	}
	
}
