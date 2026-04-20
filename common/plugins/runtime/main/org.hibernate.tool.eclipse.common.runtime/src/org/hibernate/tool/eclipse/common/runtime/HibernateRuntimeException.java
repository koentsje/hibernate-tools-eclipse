package org.hibernate.tool.eclipse.common.runtime;

@SuppressWarnings("serial")
public class HibernateRuntimeException extends RuntimeException {

	public HibernateRuntimeException(Throwable root) {
		super(root);
	}

	public HibernateRuntimeException(String string, Throwable root) {
		super(string, root);
	}

	public HibernateRuntimeException(String s) {
		super(s);
	}

}
