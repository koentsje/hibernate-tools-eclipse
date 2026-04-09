package org.hibernate.tool.eclipse.orm.runtime.v_5_5.internal;

import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractHbm2DDLExporterFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;
import org.hibernate.tool.eclipse.orm.runtime.spi.IHbm2DDLExporter;

public class Hbm2DDLExporterFacadeImpl extends AbstractHbm2DDLExporterFacade implements IHbm2DDLExporter {

	public Hbm2DDLExporterFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}

}
