package org.hibernate.tool.eclipse.orm.runtime.spi;


public interface ICfg2HbmTool {

	String getTag(IPersistentClass persistentClass);
	String getTag(IProperty property);

}
