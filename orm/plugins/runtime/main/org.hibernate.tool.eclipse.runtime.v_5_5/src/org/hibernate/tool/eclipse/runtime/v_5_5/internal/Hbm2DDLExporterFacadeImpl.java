package org.hibernate.tool.eclipse.runtime.v_5_5.internal;

import org.hibernate.tool.eclipse.runtime.common.AbstractHbm2DDLExporterFacade;
import org.hibernate.tool.eclipse.runtime.common.IFacadeFactory;
import org.hibernate.tool.eclipse.runtime.spi.IHbm2DDLExporter;

public class Hbm2DDLExporterFacadeImpl extends AbstractHbm2DDLExporterFacade implements IHbm2DDLExporter {

	public Hbm2DDLExporterFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}

}
