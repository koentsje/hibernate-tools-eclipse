package org.hibernate.tool.eclipse.orm.runtime.v_5_3.internal;

import org.hibernate.tool.hbm2x.Exporter;
import org.hibernate.tool.eclipse.orm.runtime.legacy.AbstractExporterFacade;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;
import org.hibernate.tool.eclipse.orm.runtime.spi.IConfiguration;
import org.hibernate.tool.eclipse.orm.runtime.v_5_3.internal.util.ConfigurationMetadataDescriptor;

public class ExporterFacadeImpl extends AbstractExporterFacade {

	public ExporterFacadeImpl(IFacadeFactory facadeFactory, Object target) {
		super(facadeFactory, target);
	}
	
	@Override
	public void setConfiguration(IConfiguration configuration) {
		Exporter exporter = (Exporter)getTarget();
		setCustomProperties(configuration.getProperties());
		exporter.setMetadataDescriptor(new ConfigurationMetadataDescriptor(configuration));
	}

}
