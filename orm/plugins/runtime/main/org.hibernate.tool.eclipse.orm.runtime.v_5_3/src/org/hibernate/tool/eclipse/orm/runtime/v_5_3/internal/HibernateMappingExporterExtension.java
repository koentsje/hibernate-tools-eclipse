package org.hibernate.tool.eclipse.orm.runtime.v_5_3.internal;

import java.io.File;
import java.util.Map;

import org.hibernate.tool.hbm2x.HibernateMappingExporter;
import org.hibernate.tool.hbm2x.pojo.POJOClass;
import org.hibernate.tool.eclipse.orm.runtime.legacy.IFacadeFactory;
import org.hibernate.tool.eclipse.orm.runtime.spi.IConfiguration;
import org.hibernate.tool.eclipse.orm.runtime.spi.IExportPOJODelegate;
import org.hibernate.tool.eclipse.orm.runtime.v_5_3.internal.util.ConfigurationMetadataDescriptor;

public class HibernateMappingExporterExtension 
extends HibernateMappingExporter {
	
	private IExportPOJODelegate delegateExporter;
	
	public HibernateMappingExporterExtension(IFacadeFactory facadeFactory, IConfiguration cfg, File file) {
		setMetadataDescriptor(new ConfigurationMetadataDescriptor(cfg));
		setOutputDirectory(file);
	}
	
	public void setDelegate(IExportPOJODelegate delegate) {
		delegateExporter = delegate;
	}

	public void superExportPOJO(Map<String, Object> map, POJOClass pojoClass) {
		super.exportPOJO(map, pojoClass);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void exportPOJO(Map map, POJOClass pojoClass) {
		if (delegateExporter == null) {
			super.exportPOJO(map, pojoClass);
		} else {
			delegateExporter.exportPojo(
					(Map<Object, Object>)map, 
					pojoClass,
					pojoClass.getQualifiedDeclarationName());
		}
	}

}
