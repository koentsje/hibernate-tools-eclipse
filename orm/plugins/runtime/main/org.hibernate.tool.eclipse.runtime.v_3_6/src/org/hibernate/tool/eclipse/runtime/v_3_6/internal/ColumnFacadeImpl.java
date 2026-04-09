package org.hibernate.tool.eclipse.runtime.v_3_6.internal;

import org.hibernate.mapping.Column;
import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractColumnFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;

public class ColumnFacadeImpl extends AbstractColumnFacade {
	
	public ColumnFacadeImpl(
			IFacadeFactory facadeFactory, 
			Column column) {
		super(facadeFactory, column);
	}	

	protected String getMappingClassName() {
		return "org.hibernate.engine.Mapping";
	}
	
}
