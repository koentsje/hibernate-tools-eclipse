package org.hibernate.tool.eclipse.runtime.spi;


public interface ICfg2HbmTool {

	String getTag(IPersistentClass persistentClass);
	String getTag(IProperty property);

}
