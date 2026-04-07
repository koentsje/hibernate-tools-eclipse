package org.hibernate.tool.eclipse.xml.ui.internal.hbm;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.hibernate.tool.eclipse.xml.ui.internal.common.DomHelper;
import org.w3c.dom.Element;

@SuppressWarnings("restriction")
public class HbmOverviewPage extends FormPage {

	private final HbmXmlEditor editor;
	private MappingAttributesSection mappingSection;
	private ClassListSection classListSection;

	public HbmOverviewPage(HbmXmlEditor editor) {
		super(editor, "hbm.overview", "Overview");
		this.editor = editor;
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = managedForm.getForm();
		form.setText("Hibernate Mapping");
		Composite body = form.getBody();

		ColumnLayout layout = new ColumnLayout();
		layout.maxNumColumns = 2;
		body.setLayout(layout);

		mappingSection = new MappingAttributesSection(body, managedForm);
		managedForm.addPart(mappingSection);

		classListSection = new ClassListSection(body, managedForm);
		managedForm.addPart(classListSection);
	}

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if (active) {
			if (mappingSection != null) mappingSection.refresh();
			if (classListSection != null) classListSection.refresh();
		}
	}

	Element getHibernateMappingElement() {
		IDOMDocument doc = editor.getDOMDocument();
		if (doc == null) return null;
		return doc.getDocumentElement();
	}

	private class MappingAttributesSection extends SectionPart {
		private Text packageText;
		private Text schemaText;
		private Text catalogText;
		private Text defaultCascadeText;
		private Text defaultAccessText;
		private boolean refreshing;

		MappingAttributesSection(Composite parent, IManagedForm managedForm) {
			super(parent, managedForm.getToolkit(), ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
			createContent(getSection(), managedForm.getToolkit());
		}

		private void createContent(Section section, FormToolkit toolkit) {
			section.setText("Mapping Attributes");
			Composite client = toolkit.createComposite(section);
			client.setLayout(new GridLayout(2, false));

			ModifyListener ml = e -> {
				if (!refreshing) {
					applyToModel();
					markDirty();
				}
			};

			packageText = createField(client, toolkit, "Package:", ml);
			schemaText = createField(client, toolkit, "Schema:", ml);
			catalogText = createField(client, toolkit, "Catalog:", ml);
			defaultCascadeText = createField(client, toolkit, "Default cascade:", ml);
			defaultAccessText = createField(client, toolkit, "Default access:", ml);

			section.setClient(client);
		}

		private Text createField(Composite parent, FormToolkit toolkit, String label, ModifyListener ml) {
			toolkit.createLabel(parent, label);
			Text text = toolkit.createText(parent, "", SWT.SINGLE);
			text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			text.addModifyListener(ml);
			return text;
		}

		private void applyToModel() {
			Element root = getHibernateMappingElement();
			if (root == null) return;
			DomHelper.setAttributeValue(root, "package", packageText.getText());
			DomHelper.setAttributeValue(root, "schema", schemaText.getText());
			DomHelper.setAttributeValue(root, "catalog", catalogText.getText());
			DomHelper.setAttributeValue(root, "default-cascade", defaultCascadeText.getText());
			DomHelper.setAttributeValue(root, "default-access", defaultAccessText.getText());
		}

		@Override
		public void refresh() {
			refreshing = true;
			try {
				Element root = getHibernateMappingElement();
				packageText.setText(DomHelper.getAttributeValue(root, "package"));
				schemaText.setText(DomHelper.getAttributeValue(root, "schema"));
				catalogText.setText(DomHelper.getAttributeValue(root, "catalog"));
				defaultCascadeText.setText(DomHelper.getAttributeValue(root, "default-cascade"));
				defaultAccessText.setText(DomHelper.getAttributeValue(root, "default-access"));
			} finally {
				refreshing = false;
			}
			super.refresh();
		}
	}

	private class ClassListSection extends SectionPart {
		private TableViewer tableViewer;

		ClassListSection(Composite parent, IManagedForm managedForm) {
			super(parent, managedForm.getToolkit(), ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
			createContent(getSection(), managedForm.getToolkit());
		}

		private void createContent(Section section, FormToolkit toolkit) {
			section.setText("Classes");
			Composite client = toolkit.createComposite(section);
			client.setLayout(new GridLayout(2, false));

			tableViewer = new TableViewer(client, SWT.BORDER | SWT.FULL_SELECTION);
			tableViewer.getTable().setHeaderVisible(true);
			tableViewer.getTable().setLinesVisible(true);
			GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
			gd.heightHint = 150;
			tableViewer.getTable().setLayoutData(gd);
			tableViewer.setContentProvider(ArrayContentProvider.getInstance());

			TableViewerColumn nameCol = new TableViewerColumn(tableViewer, SWT.NONE);
			nameCol.getColumn().setText("Class");
			nameCol.getColumn().setWidth(250);
			nameCol.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					return DomHelper.getAttributeValue((Element) element, "name");
				}
			});

			TableViewerColumn tableCol = new TableViewerColumn(tableViewer, SWT.NONE);
			tableCol.getColumn().setText("Table");
			tableCol.getColumn().setWidth(200);
			tableCol.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					return DomHelper.getAttributeValue((Element) element, "table");
				}
			});

			tableViewer.addDoubleClickListener(event -> {
				IStructuredSelection sel = tableViewer.getStructuredSelection();
				if (!sel.isEmpty()) {
					Element classEl = (Element) sel.getFirstElement();
					List<Element> classes = DomHelper.getChildElements(getHibernateMappingElement(), "class");
					int index = classes.indexOf(classEl);
					if (index >= 0) {
						editor.selectClass(index);
					}
				}
			});

			Composite buttons = toolkit.createComposite(client);
			buttons.setLayout(new GridLayout());
			buttons.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

			Button addBtn = toolkit.createButton(buttons, "Add...", SWT.PUSH);
			addBtn.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			addBtn.addListener(SWT.Selection, e -> {
				Element root = getHibernateMappingElement();
				if (root != null) {
					Element cls = DomHelper.addElement(root, "class");
					cls.setAttribute("name", "NewClass");
					cls.setAttribute("table", "new_table");
					refresh();
					markDirty();
				}
			});

			Button removeBtn = toolkit.createButton(buttons, "Remove", SWT.PUSH);
			removeBtn.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			removeBtn.addListener(SWT.Selection, e -> {
				IStructuredSelection sel = tableViewer.getStructuredSelection();
				for (Object obj : sel.toList()) {
					DomHelper.removeElement((Element) obj);
				}
				refresh();
				markDirty();
			});

			section.setClient(client);
		}

		@Override
		public void refresh() {
			Element root = getHibernateMappingElement();
			if (root != null) {
				tableViewer.setInput(DomHelper.getChildElements(root, "class"));
			} else {
				tableViewer.setInput(new Object[0]);
			}
			super.refresh();
		}
	}
}
