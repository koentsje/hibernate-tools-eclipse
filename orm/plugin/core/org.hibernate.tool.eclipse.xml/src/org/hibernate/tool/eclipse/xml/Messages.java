package org.hibernate.tool.eclipse.xml;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.hibernate.tool.eclipse.xml.messages"; //$NON-NLS-1$
	public static String DatabaseObjectImpl_GenericName;
	public static String IdStructureHelper_CloseOption;
	public static String IdStructureHelper_ShouldNotRemoveLastAttribute;
	public static String IdStructureHelper_WarningTitle;
	public static String PasteUniqueHandler_Cancel;
	public static String PasteUniqueHandler_OK;
	public static String PasteUniqueHandler_PasteTitle;
	public static String PasteUniqueHandler_ReplaceExistingElement;
	public static String RegularObject2Impl_NoIdAttribute;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
