package org.hibernate.tool.eclipse.exception;

import org.hibernate.tool.eclipse.orm.runtime.spi.HibernateException;


@SuppressWarnings("serial")
public class MappingException extends HibernateException {


	public MappingException(String msg, Throwable root) {
		super( msg, root );
	}

	public MappingException(Throwable root) {
		super(root);
	}

	public MappingException(String s) {
		super(s);
	}

}
