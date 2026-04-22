package org.hibernate.tool.eclipse.orm.jpt.core.nls;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.hibernate.tool.eclipse.orm.jpt.core.nls.Messages"; //$NON-NLS-1$
	public static String HibernateJpaProject_Update_Hibernate_properties;
	public static String SYNC_CLASSES_JOB;
	public static String SYNC_CLASSES_TASK;
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
