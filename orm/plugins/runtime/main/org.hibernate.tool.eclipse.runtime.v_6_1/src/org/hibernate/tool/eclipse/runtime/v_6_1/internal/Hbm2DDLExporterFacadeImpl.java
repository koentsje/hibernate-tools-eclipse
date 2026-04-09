package org.hibernate.tool.eclipse.runtime.v_6_1.internal;

import org.hibernate.tool.api.export.ExporterConstants;
import org.hibernate.tool.internal.export.ddl.DdlExporter;
import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractHbm2DDLExporterFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;

public class Hbm2DDLExporterFacadeImpl extends AbstractHbm2DDLExporterFacade {

	public Hbm2DDLExporterFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}
	
	public void setExport(boolean b) {
		((DdlExporter)getTarget()).getProperties().put(ExporterConstants.EXPORT_TO_DATABASE, b);
	}

}
