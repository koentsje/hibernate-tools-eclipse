package org.hibernate.tool.eclipse.orm.runtime.v_6_0.internal;

import org.hibernate.tool.api.export.ExporterConstants;
import org.hibernate.tool.internal.export.common.GenericExporter;
import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractGenericExporterFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;

public class GenericExporterFacadeImpl extends AbstractGenericExporterFacade {

	public GenericExporterFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}
	
	@Override 
	public void setFilePattern(String filePattern) {
		((GenericExporter)getTarget()).getProperties().setProperty(
				ExporterConstants.FILE_PATTERN, 
				filePattern);
	}

	@Override 
	public void setTemplateName(String templateName) {
		((GenericExporter)getTarget()).getProperties().setProperty(
				ExporterConstants.TEMPLATE_NAME, 
				templateName);
	}

	@Override 
	public void setForEach(String forEach) {
		((GenericExporter)getTarget()).getProperties().setProperty(
				ExporterConstants.FOR_EACH, 
				forEach);
	}
	
	@Override
	public String getFilePattern() {
		return ((GenericExporter)getTarget()).getProperties().getProperty(ExporterConstants.FILE_PATTERN);
	}
	
	@Override
	public String getTemplateName() {
		return ((GenericExporter)getTarget()).getProperties().getProperty(ExporterConstants.TEMPLATE_NAME);
	}

}
