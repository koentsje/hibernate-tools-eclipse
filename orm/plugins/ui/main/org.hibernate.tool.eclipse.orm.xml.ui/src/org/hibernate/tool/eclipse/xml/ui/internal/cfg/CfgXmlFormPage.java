package org.hibernate.tool.eclipse.xml.ui.internal.cfg;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.hibernate.tool.eclipse.xml.core.common.DomHelper;
import org.w3c.dom.Element;

@SuppressWarnings("restriction")
public class CfgXmlFormPage extends FormPage {

	private final CfgXmlEditor editor;

	private SessionFactorySection sessionFactorySection;
	private ConnectionSection connectionSection;
	private PropertiesSection propertiesSection;
	private MappingsSection mappingsSection;

	public CfgXmlFormPage(CfgXmlEditor editor) {
		super(editor, "cfgxml.form", "Configuration");
		this.editor = editor;
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = managedForm.getForm();
		form.setText("Hibernate Configuration");
		Composite body = form.getBody();

		ColumnLayout layout = new ColumnLayout();
		layout.maxNumColumns = 2;
		body.setLayout(layout);

		sessionFactorySection = new SessionFactorySection(body, managedForm, this);
		managedForm.addPart(sessionFactorySection);

		connectionSection = new ConnectionSection(body, managedForm, this);
		managedForm.addPart(connectionSection);

		propertiesSection = new PropertiesSection(body, managedForm, this);
		managedForm.addPart(propertiesSection);

		mappingsSection = new MappingsSection(body, managedForm, this);
		managedForm.addPart(mappingsSection);
	}

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if (active) {
			refreshAllSections();
		}
	}

	public Element getSessionFactoryElement() {
		IDOMDocument doc = editor.getDOMDocument();
		if (doc == null) return null;
		Element root = doc.getDocumentElement();
		if (root == null) return null;
		return DomHelper.getFirstChildElement(root, "session-factory");
	}

	void refreshAllSections() {
		if (sessionFactorySection != null) sessionFactorySection.refresh();
		if (connectionSection != null) connectionSection.refresh();
		if (propertiesSection != null) propertiesSection.refresh();
		if (mappingsSection != null) mappingsSection.refresh();
	}
}
