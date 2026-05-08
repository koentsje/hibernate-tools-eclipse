package org.hibernate.tool.eclipse.orm.xml.core.nls;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.hibernate.tool.eclipse.orm.xml.core.nls.Messages"; //$NON-NLS-1$
	public static String DOMReverseEngineeringDefinition_unknown_change;
	public static String RevEngPrimaryKeyAdapter_column;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
