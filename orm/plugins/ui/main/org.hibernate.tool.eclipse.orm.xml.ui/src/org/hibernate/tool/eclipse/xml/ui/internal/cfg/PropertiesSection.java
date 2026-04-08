package org.hibernate.tool.eclipse.xml.ui.internal.cfg;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.hibernate.tool.eclipse.xml.core.common.DomHelper;
import org.w3c.dom.Element;

public class PropertiesSection extends SectionPart {

	private final CfgXmlFormPage page;
	private TableViewer tableViewer;

	public PropertiesSection(Composite parent, IManagedForm managedForm, CfgXmlFormPage page) {
		super(parent, managedForm.getToolkit(), ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE);
		this.page = page;
		createContent(getSection(), managedForm.getToolkit());
	}

	private void createContent(Section section, FormToolkit toolkit) {
		section.setText("Properties");

		Composite client = toolkit.createComposite(section);
		client.setLayout(new GridLayout(2, false));

		tableViewer = new TableViewer(client, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint = 150;
		tableViewer.getTable().setLayoutData(gd);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		TableViewerColumn nameCol = new TableViewerColumn(tableViewer, SWT.NONE);
		nameCol.getColumn().setText("Name");
		nameCol.getColumn().setWidth(250);
		nameCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Element) element).getAttribute("name");
			}
		});

		TableViewerColumn valueCol = new TableViewerColumn(tableViewer, SWT.NONE);
		valueCol.getColumn().setText("Value");
		valueCol.getColumn().setWidth(250);
		valueCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return DomHelper.getTextContent((Element) element);
			}
		});

		Composite buttons = toolkit.createComposite(client);
		buttons.setLayout(new GridLayout());
		buttons.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

		Button addBtn = toolkit.createButton(buttons, "Add...", SWT.PUSH);
		addBtn.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		addBtn.addListener(SWT.Selection, e -> onAdd());

		Button editBtn = toolkit.createButton(buttons, "Edit...", SWT.PUSH);
		editBtn.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		editBtn.addListener(SWT.Selection, e -> onEdit());

		Button removeBtn = toolkit.createButton(buttons, "Remove", SWT.PUSH);
		removeBtn.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		removeBtn.addListener(SWT.Selection, e -> onRemove());

		section.setClient(client);
	}

	private void onAdd() {
		Element sf = page.getSessionFactoryElement();
		if (sf == null) return;
		PropertyDialog dialog = new PropertyDialog(getSection().getShell(), "", "");
		if (dialog.open() == Dialog.OK) {
			DomHelper.setPropertyValue(sf, dialog.getName(), dialog.getValue());
			refresh();
			markDirty();
		}
	}

	private void onEdit() {
		IStructuredSelection sel = tableViewer.getStructuredSelection();
		if (sel.isEmpty()) return;
		Element prop = (Element) sel.getFirstElement();
		String name = prop.getAttribute("name");
		String value = DomHelper.getTextContent(prop);
		PropertyDialog dialog = new PropertyDialog(getSection().getShell(), name, value);
		if (dialog.open() == Dialog.OK) {
			if (!dialog.getName().equals(name)) {
				DomHelper.removeElement(prop);
			}
			Element sf = page.getSessionFactoryElement();
			DomHelper.setPropertyValue(sf, dialog.getName(), dialog.getValue());
			refresh();
			markDirty();
		}
	}

	private void onRemove() {
		IStructuredSelection sel = tableViewer.getStructuredSelection();
		for (Object obj : sel.toList()) {
			DomHelper.removeElement((Element) obj);
		}
		refresh();
		markDirty();
	}

	@Override
	public void refresh() {
		Element sf = page.getSessionFactoryElement();
		if (sf != null) {
			List<Element> props = DomHelper.getChildElements(sf, "property");
			tableViewer.setInput(props);
		} else {
			tableViewer.setInput(new Object[0]);
		}
		super.refresh();
	}

	private static class PropertyDialog extends Dialog {
		private String name;
		private String value;
		private Text nameText;
		private Text valueText;

		protected PropertyDialog(Shell shell, String name, String value) {
			super(shell);
			this.name = name;
			this.value = value;
		}

		@Override
		protected void configureShell(Shell shell) {
			super.configureShell(shell);
			shell.setText("Hibernate Property");
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Composite composite = (Composite) super.createDialogArea(parent);
			composite.setLayout(new GridLayout(2, false));

			new Label(composite, SWT.NONE).setText("Name:");
			nameText = new Text(composite, SWT.BORDER);
			nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			nameText.setText(name);

			new Label(composite, SWT.NONE).setText("Value:");
			valueText = new Text(composite, SWT.BORDER);
			valueText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			valueText.setText(value);

			return composite;
		}

		@Override
		protected void okPressed() {
			name = nameText.getText().trim();
			value = valueText.getText().trim();
			super.okPressed();
		}

		String getName() { return name; }
		String getValue() { return value; }
	}
}
