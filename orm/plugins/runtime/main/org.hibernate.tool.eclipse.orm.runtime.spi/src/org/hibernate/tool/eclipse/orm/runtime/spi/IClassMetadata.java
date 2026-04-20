package org.hibernate.tool.eclipse.orm.runtime.spi;


public interface IClassMetadata {

	String getEntityName();
	String getIdentifierPropertyName();
	String[] getPropertyNames();
	IType[] getPropertyTypes();
	Class<?> getMappedClass();
	IType getIdentifierType();
	Object getPropertyValue(Object object, String name);
	boolean hasIdentifierProperty();
	Object getIdentifier(Object object, ISession implementor);
	boolean isInstanceOfAbstractEntityPersister();
	Integer getPropertyIndexOrNull(String id);
	Object getTuplizerPropertyValue(Object entity, int i);

}
