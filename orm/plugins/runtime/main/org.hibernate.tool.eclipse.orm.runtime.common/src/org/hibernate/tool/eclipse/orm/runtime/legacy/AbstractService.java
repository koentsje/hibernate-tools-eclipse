package org.hibernate.tool.eclipse.orm.runtime.legacy;

import org.hibernate.tool.eclipse.common.runtime.UsageTracker;
import org.hibernate.tool.eclipse.orm.runtime.spi.IExporter;
import org.hibernate.tool.eclipse.orm.runtime.spi.IService;

public abstract class AbstractService implements IService {
	
	public IExporter createCfgExporter() {
		return createExporter(getCfgExporterClassName());
	}
	
	protected UsageTracker getUsageTracker() {
		return UsageTracker.getInstance();
	}
	
	protected String getCfgExporterClassName() {
		return "org.hibernate.tool.hbm2x.HibernateConfigurationExporter";
	}
	
}
