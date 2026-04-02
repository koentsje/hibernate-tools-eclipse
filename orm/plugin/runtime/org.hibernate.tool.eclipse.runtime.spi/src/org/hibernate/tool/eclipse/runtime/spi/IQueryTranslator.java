package org.hibernate.tool.eclipse.runtime.spi;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface IQueryTranslator {

	boolean isManipulationStatement();
	Set<Serializable> getQuerySpaces();
	IType[] getReturnTypes();
	List<String> collectSqlStrings();

}
