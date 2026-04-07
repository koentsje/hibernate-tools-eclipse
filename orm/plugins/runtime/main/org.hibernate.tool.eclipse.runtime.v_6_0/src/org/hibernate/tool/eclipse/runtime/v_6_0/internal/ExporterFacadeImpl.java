package org.hibernate.tool.eclipse.runtime.v_6_0.internal;

import java.io.File;

import org.hibernate.tool.api.export.Exporter;
import org.hibernate.tool.api.export.ExporterConstants;
import org.hibernate.tool.eclipse.runtime.common.AbstractExporterFacade;
import org.hibernate.tool.eclipse.runtime.common.IFacade;
import org.hibernate.tool.eclipse.runtime.common.IFacadeFactory;
import org.hibernate.tool.eclipse.runtime.spi.IArtifactCollector;
import org.hibernate.tool.eclipse.runtime.spi.IConfiguration;
import org.hibernate.tool.eclipse.runtime.v_6_0.internal.util.ConfigurationMetadataDescriptor;

public class ExporterFacadeImpl extends AbstractExporterFacade {

	public ExporterFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}

	@Override
	public void setConfiguration(IConfiguration configuration) {
		setCustomProperties(configuration.getProperties());
		((Exporter)getTarget()).getProperties().put(
				ExporterConstants.METADATA_DESCRIPTOR, 
				new ConfigurationMetadataDescriptor((ConfigurationFacadeImpl)configuration));
	}
	
	@Override
	public void setArtifactCollector(IArtifactCollector artifactCollector) {
		((Exporter)getTarget()).getProperties().put(
				ExporterConstants.ARTIFACT_COLLECTOR,
				((IFacade)artifactCollector).getTarget());
	}

	@Override
	public void setOutputDirectory(File file) {
		((Exporter)getTarget()).getProperties().put(ExporterConstants.DESTINATION_FOLDER, file);
	}

	@Override
	public void setTemplatePath(String[] templatePath) {
		((Exporter)getTarget()).getProperties().put(ExporterConstants.TEMPLATE_PATH, templatePath);
	}

	@Override
	protected String getHibernateConfigurationExporterClassName() {
		return "org.hibernate.tool.internal.export.cfg.CfgExporter";
	}
	
	@Override
	protected String getGenericExporterClassName() {
		return "org.hibernate.tool.internal.export.common.GenericExporter";
	}

	@Override
	protected String getHbm2DDLExporterClassName() {
		return "org.hibernate.tool.internal.export.ddl.DdlExporter";
	}

	@Override
	protected String getQueryExporterClassName() {
		return "org.hibernate.tool.internal.export.query.QueryExporter";
	}

}
