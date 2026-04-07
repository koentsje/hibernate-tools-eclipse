package org.hibernate.tool.eclipse.runtime.spi;

public interface IGenericExporter {

	void setFilePattern(String string);
	void setTemplateName(String string);
	void setForEach(String string);
	String getFilePattern();
	String getTemplateName();

}
