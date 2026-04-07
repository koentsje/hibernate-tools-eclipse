package org.hibernate.tool.eclipse.xml.ui.internal.cfg;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
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
import org.hibernate.tool.eclipse.xml.ui.internal.common.DomHelper;
import org.w3c.dom.Element;

public class MappingsSection extends SectionPart {

	private final CfgXmlFormPage page;
	private TableViewer tableViewer;

	public MappingsSection(Composite parent, IManagedForm managedForm, CfgXmlFormPage page) {
		super(parent, managedForm.getToolkit(), ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE);
		this.page = page;
		createContent(getSection(), managedForm.getToolkit());
	}

	private void createContent(Section section, FormToolkit toolkit) {
		section.setText("Mappings");

		Composite client = toolkit.createComposite(section);
		client.setLayout(new GridLayout(2, false));

		tableViewer = new TableViewer(client, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint = 120;
		tableViewer.getTable().setLayoutData(gd);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		createColumn("resource", 200);
		createColumn("class", 200);
		createColumn("file", 150);
		createColumn("jar", 150);
		createColumn("package", 150);

		Composite buttons = toolkit.createComposite(client);
		buttons.setLayout(new GridLayout());
		buttons.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

		Button addBtn = toolkit.createButton(buttons, "Add...", SWT.PUSH);
		addBtn.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		addBtn.addListener(SWT.Selection, e -> onAdd());

		Button removeBtn = toolkit.createButton(buttons, "Remove", SWT.PUSH);
		removeBtn.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		removeBtn.addListener(SWT.Selection, e -> onRemove());

		section.setClient(client);
	}

	private void createColumn(String attribute, int width) {
		TableViewerColumn col = new TableViewerColumn(tableViewer, SWT.NONE);
		col.getColumn().setText(attribute);
		col.getColumn().setWidth(width);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return DomHelper.getAttributeValue((Element) element, attribute);
			}
		});
	}

	private void onAdd() {
		Element sf = page.getSessionFactoryElement();
		if (sf == null) return;
		MappingDialog dialog = new MappingDialog(getSection().getShell());
		if (dialog.open() == Dialog.OK) {
			Element mapping = DomHelper.addElement(sf, "mapping");
			String resource = dialog.getResource();
			if (resource != null && !resource.isEmpty()) {
				mapping.setAttribute("resource", resource);
			}
			String className = dialog.getClassName();
			if (className != null && !className.isEmpty()) {
				mapping.setAttribute("class", className);
			}
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
			List<Element> mappings = DomHelper.getChildElements(sf, "mapping");
			tableViewer.setInput(mappings);
		} else {
			tableViewer.setInput(new Object[0]);
		}
		super.refresh();
	}

	private static class MappingDialog extends Dialog {
		private Text resourceText;
		private Text classText;
		private String resource = "";
		private String className = "";

		protected MappingDialog(Shell shell) {
			super(shell);
		}

		@Override
		protected void configureShell(Shell shell) {
			super.configureShell(shell);
			shell.setText("Add Mapping");
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Composite composite = (Composite) super.createDialogArea(parent);
			composite.setLayout(new GridLayout(2, false));

			new Label(composite, SWT.NONE).setText("Resource:");
			resourceText = new Text(composite, SWT.BORDER);
			resourceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

			new Label(composite, SWT.NONE).setText("Class:");
			classText = new Text(composite, SWT.BORDER);
			classText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

			return composite;
		}

		@Override
		protected void okPressed() {
			resource = resourceText.getText().trim();
			className = classText.getText().trim();
			super.okPressed();
		}

		String getResource() { return resource; }
		String getClassName() { return className; }
	}
}
