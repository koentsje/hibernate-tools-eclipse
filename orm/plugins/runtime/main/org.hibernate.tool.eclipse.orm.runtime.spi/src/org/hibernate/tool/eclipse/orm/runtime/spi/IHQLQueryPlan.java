package org.hibernate.tool.eclipse.orm.runtime.spi;


public interface IHQLQueryPlan {

	IQueryTranslator[] getTranslators();

}
