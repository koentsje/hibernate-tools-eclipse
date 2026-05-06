package org.hibernate.tool.eclipse.orm.jpt.java.context.java;

import java.util.ListIterator;

import org.eclipse.jpt.jpa.core.context.persistence.PersistenceUnit;

public interface IHibernatePersistenceUnit extends PersistenceUnit {

	ListIterator<JavaTypeDef> typeDefs();

	void addTypeDef(JavaTypeDef typeDef);

	String[] uniqueTypeDefNames();

	boolean hasTypeDef(String name);

}
