package org.hibernate.tool.eclipse.orm.jdt.core.nls;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.hibernate.tool.eclipse.orm.jdt.core.nls.Messages"; //$NON-NLS-1$
	public static String SaveQueryEditorListener_composite_change_name;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
