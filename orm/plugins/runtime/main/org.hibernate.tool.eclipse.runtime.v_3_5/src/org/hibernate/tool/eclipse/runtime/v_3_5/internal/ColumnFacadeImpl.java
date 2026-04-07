package org.hibernate.tool.eclipse.runtime.v_3_5.internal;

import org.hibernate.mapping.Column;
import org.hibernate.tool.eclipse.runtime.common.AbstractColumnFacade;
import org.hibernate.tool.eclipse.runtime.common.IFacadeFactory;

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
