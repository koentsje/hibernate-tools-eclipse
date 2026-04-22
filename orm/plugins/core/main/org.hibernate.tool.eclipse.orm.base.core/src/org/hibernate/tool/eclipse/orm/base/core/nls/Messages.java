package org.hibernate.tool.eclipse.orm.base.core.nls;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.hibernate.tool.eclipse.orm.base.core.nls.Messages"; //$NON-NLS-1$
	public static String ClassLoaderHelper_could_not_determine_physical_location_for;
	public static String HQLEditorStorage_none;
	public static String ProjectCompilerVersionChecker_title;
	public static String ProjectCompilerVersionChecker_message;
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
