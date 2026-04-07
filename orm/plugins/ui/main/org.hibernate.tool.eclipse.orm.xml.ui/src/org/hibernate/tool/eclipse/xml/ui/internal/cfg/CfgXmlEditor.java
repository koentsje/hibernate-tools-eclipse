package org.hibernate.tool.eclipse.xml.ui.internal.cfg;

import org.eclipse.ui.PartInitException;
import org.hibernate.tool.eclipse.xml.ui.internal.common.AbstractHibernateFormEditor;

public class CfgXmlEditor extends AbstractHibernateFormEditor {

	public static final String EDITOR_ID = "org.hibernate.tool.eclipse.xml.ui.cfg.CfgXmlEditor";

	@Override
	protected void addFormPages() throws PartInitException {
		addPage(0, new CfgXmlFormPage(this));
		setPageText(0, "Configuration");
	}
}
