package org.hibernate.tool.eclipse.xml.ui.internal.hbm;

import org.eclipse.ui.PartInitException;
import org.hibernate.tool.eclipse.xml.ui.internal.common.AbstractHibernateFormEditor;

public class HbmXmlEditor extends AbstractHibernateFormEditor {

	public static final String EDITOR_ID = "org.hibernate.tool.eclipse.xml.ui.hbm.HbmXmlEditor";

	private HbmOverviewPage overviewPage;
	private ClassDetailPage classDetailPage;

	@Override
	protected void addFormPages() throws PartInitException {
		overviewPage = new HbmOverviewPage(this);
		addPage(0, overviewPage);
		setPageText(0, "Overview");

		classDetailPage = new ClassDetailPage(this);
		addPage(1, classDetailPage);
		setPageText(1, "Class Details");
	}

	void selectClass(int index) {
		classDetailPage.setClassIndex(index);
		setActivePage(1);
	}
}
