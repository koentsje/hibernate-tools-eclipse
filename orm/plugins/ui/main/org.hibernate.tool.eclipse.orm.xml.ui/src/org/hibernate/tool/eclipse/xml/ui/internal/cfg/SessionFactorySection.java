package org.hibernate.tool.eclipse.xml.ui.internal.cfg;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.hibernate.tool.eclipse.xml.ui.internal.common.DomHelper;
import org.w3c.dom.Element;

public class SessionFactorySection extends SectionPart {

	private final CfgXmlFormPage page;
	private Text nameText;
	private boolean refreshing;

	public SessionFactorySection(Composite parent, IManagedForm managedForm, CfgXmlFormPage page) {
		super(parent, managedForm.getToolkit(), ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
		this.page = page;
		createContent(getSection(), managedForm.getToolkit());
	}

	private void createContent(Section section, FormToolkit toolkit) {
		section.setText("Session Factory");

		Composite client = toolkit.createComposite(section);
		client.setLayout(new GridLayout(2, false));

		toolkit.createLabel(client, "Name:");
		nameText = toolkit.createText(client, "", SWT.SINGLE);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		nameText.addModifyListener(createModifyListener());

		section.setClient(client);
	}

	private ModifyListener createModifyListener() {
		return e -> {
			if (!refreshing) {
				applyToModel();
				markDirty();
			}
		};
	}

	private void applyToModel() {
		Element sf = page.getSessionFactoryElement();
		if (sf != null) {
			DomHelper.setAttributeValue(sf, "name", nameText.getText());
		}
	}

	@Override
	public void refresh() {
		refreshing = true;
		try {
			Element sf = page.getSessionFactoryElement();
			nameText.setText(DomHelper.getAttributeValue(sf, "name"));
		} finally {
			refreshing = false;
		}
		super.refresh();
	}
}
