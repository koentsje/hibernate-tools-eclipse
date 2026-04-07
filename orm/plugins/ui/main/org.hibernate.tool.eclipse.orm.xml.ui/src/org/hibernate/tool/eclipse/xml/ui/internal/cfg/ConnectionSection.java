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

public class ConnectionSection extends SectionPart {

	private static final String PROP_DRIVER = "hibernate.connection.driver_class";
	private static final String PROP_URL = "hibernate.connection.url";
	private static final String PROP_DIALECT = "hibernate.dialect";
	private static final String PROP_SCHEMA = "hibernate.default_schema";
	private static final String PROP_CATALOG = "hibernate.default_catalog";
	private static final String PROP_USERNAME = "hibernate.connection.username";
	private static final String PROP_PASSWORD = "hibernate.connection.password";

	private final CfgXmlFormPage page;
	private Text dialectText;
	private Text driverText;
	private Text urlText;
	private Text schemaText;
	private Text catalogText;
	private Text usernameText;
	private Text passwordText;
	private boolean refreshing;

	public ConnectionSection(Composite parent, IManagedForm managedForm, CfgXmlFormPage page) {
		super(parent, managedForm.getToolkit(), ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
		this.page = page;
		createContent(getSection(), managedForm.getToolkit());
	}

	private void createContent(Section section, FormToolkit toolkit) {
		section.setText("Connection");

		Composite client = toolkit.createComposite(section);
		client.setLayout(new GridLayout(2, false));

		ModifyListener modifyListener = e -> {
			if (!refreshing) {
				applyToModel();
				markDirty();
			}
		};

		dialectText = createLabeledText(client, toolkit, "Dialect:", modifyListener);
		driverText = createLabeledText(client, toolkit, "Driver class:", modifyListener);
		urlText = createLabeledText(client, toolkit, "Connection URL:", modifyListener);
		schemaText = createLabeledText(client, toolkit, "Default schema:", modifyListener);
		catalogText = createLabeledText(client, toolkit, "Default catalog:", modifyListener);
		usernameText = createLabeledText(client, toolkit, "Username:", modifyListener);
		passwordText = createLabeledText(client, toolkit, "Password:", modifyListener);

		section.setClient(client);
	}

	private Text createLabeledText(Composite parent, FormToolkit toolkit, String label, ModifyListener listener) {
		toolkit.createLabel(parent, label);
		Text text = toolkit.createText(parent, "", SWT.SINGLE);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		text.addModifyListener(listener);
		return text;
	}

	private void applyToModel() {
		Element sf = page.getSessionFactoryElement();
		if (sf == null) return;
		DomHelper.setPropertyValue(sf, PROP_DIALECT, dialectText.getText());
		DomHelper.setPropertyValue(sf, PROP_DRIVER, driverText.getText());
		DomHelper.setPropertyValue(sf, PROP_URL, urlText.getText());
		DomHelper.setPropertyValue(sf, PROP_SCHEMA, schemaText.getText());
		DomHelper.setPropertyValue(sf, PROP_CATALOG, catalogText.getText());
		DomHelper.setPropertyValue(sf, PROP_USERNAME, usernameText.getText());
		DomHelper.setPropertyValue(sf, PROP_PASSWORD, passwordText.getText());
	}

	@Override
	public void refresh() {
		refreshing = true;
		try {
			Element sf = page.getSessionFactoryElement();
			dialectText.setText(DomHelper.getPropertyValue(sf, PROP_DIALECT));
			driverText.setText(DomHelper.getPropertyValue(sf, PROP_DRIVER));
			urlText.setText(DomHelper.getPropertyValue(sf, PROP_URL));
			schemaText.setText(DomHelper.getPropertyValue(sf, PROP_SCHEMA));
			catalogText.setText(DomHelper.getPropertyValue(sf, PROP_CATALOG));
			usernameText.setText(DomHelper.getPropertyValue(sf, PROP_USERNAME));
			passwordText.setText(DomHelper.getPropertyValue(sf, PROP_PASSWORD));
		} finally {
			refreshing = false;
		}
		super.refresh();
	}
}
