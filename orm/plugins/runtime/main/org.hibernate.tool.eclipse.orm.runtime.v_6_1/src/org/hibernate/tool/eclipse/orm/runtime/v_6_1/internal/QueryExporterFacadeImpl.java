package org.hibernate.tool.eclipse.orm.runtime.v_6_1.internal;

import org.hibernate.tool.api.export.ExporterConstants;
import org.hibernate.tool.internal.export.query.QueryExporter;
import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractQueryExporterFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;

public class QueryExporterFacadeImpl extends AbstractQueryExporterFacade {

	public QueryExporterFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}
	
	@Override
	public void setFilename(String fileName) {
		((QueryExporter)getTarget()).getProperties().put(ExporterConstants.OUTPUT_FILE_NAME, fileName);
	}

}
