package org.hibernate.tool.eclipse.xml.ui.internal.hbm;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
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
public class ClassDetailPage extends FormPage {

	private final HbmXmlEditor editor;
	private int classIndex = 0;

	private ClassBasicSection basicSection;
	private IdSection idSection;
	private PropertiesTableSection propsSection;
	private AssociationsSection assocSection;

	public ClassDetailPage(HbmXmlEditor editor) {
		super(editor, "hbm.classdetail", "Class Details");
		this.editor = editor;
	}

	void setClassIndex(int index) {
		this.classIndex = index;
		refreshAll();
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = managedForm.getForm();
		form.setText("Class Details");
		Composite body = form.getBody();

		ColumnLayout layout = new ColumnLayout();
		layout.maxNumColumns = 2;
		body.setLayout(layout);

		basicSection = new ClassBasicSection(body, managedForm);
		managedForm.addPart(basicSection);

		idSection = new IdSection(body, managedForm);
		managedForm.addPart(idSection);

		propsSection = new PropertiesTableSection(body, managedForm);
		managedForm.addPart(propsSection);

		assocSection = new AssociationsSection(body, managedForm);
		managedForm.addPart(assocSection);
	}

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if (active) {
			refreshAll();
		}
	}

	private void refreshAll() {
		if (basicSection != null) basicSection.refresh();
		if (idSection != null) idSection.refresh();
		if (propsSection != null) propsSection.refresh();
		if (assocSection != null) assocSection.refresh();
	}

	Element getClassElement() {
		IDOMDocument doc = editor.getDOMDocument();
		if (doc == null) return null;
		Element root = doc.getDocumentElement();
		if (root == null) return null;
		List<Element> classes = DomHelper.getChildElements(root, "class");
		if (classIndex >= 0 && classIndex < classes.size()) {
			return classes.get(classIndex);
		}
		return null;
	}

	// --- Class Basic Attributes Section ---

	private class ClassBasicSection extends SectionPart {
		private Text nameText;
		private Text tableText;
		private Text schemaText;
		private Text catalogText;
		private Text proxyText;
		private Text discriminatorText;
		private Combo lazyCombo;
		private Combo polymorphismCombo;
		private Combo optimisticLockCombo;
		private Button mutableCheck;
		private Button abstractCheck;
		private Button dynamicUpdateCheck;
		private Button dynamicInsertCheck;
		private Button selectBeforeUpdateCheck;
		private boolean refreshing;

		ClassBasicSection(Composite parent, IManagedForm managedForm) {
			super(parent, managedForm.getToolkit(), ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
			createContent(getSection(), managedForm.getToolkit());
		}

		private void createContent(Section section, FormToolkit toolkit) {
			section.setText("Class");
			Composite client = toolkit.createComposite(section);
			client.setLayout(new GridLayout(4, false));

			ModifyListener ml = e -> { if (!refreshing) { applyToModel(); markDirty(); } };

			nameText = createField(client, toolkit, "Name:", ml);
			tableText = createField(client, toolkit, "Table:", ml);
			schemaText = createField(client, toolkit, "Schema:", ml);
			catalogText = createField(client, toolkit, "Catalog:", ml);
			proxyText = createField(client, toolkit, "Proxy:", ml);
			discriminatorText = createField(client, toolkit, "Discriminator:", ml);

			toolkit.createLabel(client, "Lazy:");
			lazyCombo = new Combo(client, SWT.READ_ONLY);
			lazyCombo.setItems("", "true", "false");
			lazyCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			lazyCombo.addModifyListener(ml);

			toolkit.createLabel(client, "Polymorphism:");
			polymorphismCombo = new Combo(client, SWT.READ_ONLY);
			polymorphismCombo.setItems("implicit", "explicit");
			polymorphismCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			polymorphismCombo.addModifyListener(ml);

			toolkit.createLabel(client, "Optimistic lock:");
			optimisticLockCombo = new Combo(client, SWT.READ_ONLY);
			optimisticLockCombo.setItems("version", "none", "dirty", "all");
			optimisticLockCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			optimisticLockCombo.addModifyListener(ml);

			// spacer
			toolkit.createLabel(client, "");

			Composite checkRow = toolkit.createComposite(client);
			checkRow.setLayout(new GridLayout(5, false));
			GridData checkGd = new GridData(SWT.FILL, SWT.CENTER, true, false);
			checkGd.horizontalSpan = 3;
			checkRow.setLayoutData(checkGd);

			mutableCheck = toolkit.createButton(checkRow, "Mutable", SWT.CHECK);
			mutableCheck.addListener(SWT.Selection, e -> { if (!refreshing) { applyToModel(); markDirty(); } });
			abstractCheck = toolkit.createButton(checkRow, "Abstract", SWT.CHECK);
			abstractCheck.addListener(SWT.Selection, e -> { if (!refreshing) { applyToModel(); markDirty(); } });
			dynamicUpdateCheck = toolkit.createButton(checkRow, "Dynamic update", SWT.CHECK);
			dynamicUpdateCheck.addListener(SWT.Selection, e -> { if (!refreshing) { applyToModel(); markDirty(); } });
			dynamicInsertCheck = toolkit.createButton(checkRow, "Dynamic insert", SWT.CHECK);
			dynamicInsertCheck.addListener(SWT.Selection, e -> { if (!refreshing) { applyToModel(); markDirty(); } });
			selectBeforeUpdateCheck = toolkit.createButton(checkRow, "Select before update", SWT.CHECK);
			selectBeforeUpdateCheck.addListener(SWT.Selection, e -> { if (!refreshing) { applyToModel(); markDirty(); } });

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
			Element cls = getClassElement();
			if (cls == null) return;
			DomHelper.setAttributeValue(cls, "name", nameText.getText());
			DomHelper.setAttributeValue(cls, "table", tableText.getText());
			DomHelper.setAttributeValue(cls, "schema", schemaText.getText());
			DomHelper.setAttributeValue(cls, "catalog", catalogText.getText());
			DomHelper.setAttributeValue(cls, "proxy", proxyText.getText());
			DomHelper.setAttributeValue(cls, "discriminator-value", discriminatorText.getText());
			setComboAttribute(cls, "lazy", lazyCombo);
			setComboAttribute(cls, "polymorphism", polymorphismCombo);
			setComboAttribute(cls, "optimistic-lock", optimisticLockCombo);
			cls.setAttribute("mutable", String.valueOf(mutableCheck.getSelection()));
			setBoolAttribute(cls, "abstract", abstractCheck);
			cls.setAttribute("dynamic-update", String.valueOf(dynamicUpdateCheck.getSelection()));
			cls.setAttribute("dynamic-insert", String.valueOf(dynamicInsertCheck.getSelection()));
			cls.setAttribute("select-before-update", String.valueOf(selectBeforeUpdateCheck.getSelection()));
		}

		private void setComboAttribute(Element el, String attr, Combo combo) {
			String val = combo.getText();
			DomHelper.setAttributeValue(el, attr, val);
		}

		private void setBoolAttribute(Element el, String attr, Button check) {
			if (check.getSelection()) {
				el.setAttribute(attr, "true");
			} else {
				el.removeAttribute(attr);
			}
		}

		@Override
		public void refresh() {
			refreshing = true;
			try {
				Element cls = getClassElement();
				if (cls != null) {
					getSection().setText("Class: " + DomHelper.getAttributeValue(cls, "name"));
				} else {
					getSection().setText("Class (none selected)");
				}
				nameText.setText(DomHelper.getAttributeValue(cls, "name"));
				tableText.setText(DomHelper.getAttributeValue(cls, "table"));
				schemaText.setText(DomHelper.getAttributeValue(cls, "schema"));
				catalogText.setText(DomHelper.getAttributeValue(cls, "catalog"));
				proxyText.setText(DomHelper.getAttributeValue(cls, "proxy"));
				discriminatorText.setText(DomHelper.getAttributeValue(cls, "discriminator-value"));
				selectComboValue(lazyCombo, DomHelper.getAttributeValue(cls, "lazy"));
				selectComboValue(polymorphismCombo, DomHelper.getAttributeValue(cls, "polymorphism"));
				selectComboValue(optimisticLockCombo, DomHelper.getAttributeValue(cls, "optimistic-lock"));
				mutableCheck.setSelection(!"false".equals(DomHelper.getAttributeValue(cls, "mutable")));
				abstractCheck.setSelection("true".equals(DomHelper.getAttributeValue(cls, "abstract")));
				dynamicUpdateCheck.setSelection("true".equals(DomHelper.getAttributeValue(cls, "dynamic-update")));
				dynamicInsertCheck.setSelection("true".equals(DomHelper.getAttributeValue(cls, "dynamic-insert")));
				selectBeforeUpdateCheck.setSelection("true".equals(DomHelper.getAttributeValue(cls, "select-before-update")));
			} finally {
				refreshing = false;
			}
			super.refresh();
		}

		private void selectComboValue(Combo combo, String value) {
			int idx = -1;
			for (int i = 0; i < combo.getItemCount(); i++) {
				if (combo.getItem(i).equals(value)) {
					idx = i;
					break;
				}
			}
			combo.select(idx >= 0 ? idx : 0);
		}
	}

	// --- Id Section ---

	private class IdSection extends SectionPart {
		private Text idNameText;
		private Text idColumnText;
		private Text idTypeText;
		private Text generatorClassText;
		private boolean refreshing;

		IdSection(Composite parent, IManagedForm managedForm) {
			super(parent, managedForm.getToolkit(), ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
			createContent(getSection(), managedForm.getToolkit());
		}

		private void createContent(Section section, FormToolkit toolkit) {
			section.setText("Id");
			Composite client = toolkit.createComposite(section);
			client.setLayout(new GridLayout(2, false));

			ModifyListener ml = e -> { if (!refreshing) { applyToModel(); markDirty(); } };

			toolkit.createLabel(client, "Name:");
			idNameText = toolkit.createText(client, "", SWT.SINGLE);
			idNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			idNameText.addModifyListener(ml);

			toolkit.createLabel(client, "Column:");
			idColumnText = toolkit.createText(client, "", SWT.SINGLE);
			idColumnText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			idColumnText.addModifyListener(ml);

			toolkit.createLabel(client, "Type:");
			idTypeText = toolkit.createText(client, "", SWT.SINGLE);
			idTypeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			idTypeText.addModifyListener(ml);

			toolkit.createLabel(client, "Generator class:");
			generatorClassText = toolkit.createText(client, "", SWT.SINGLE);
			generatorClassText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			generatorClassText.addModifyListener(ml);

			section.setClient(client);
		}

		private void applyToModel() {
			Element cls = getClassElement();
			if (cls == null) return;
			Element id = DomHelper.getOrCreateElement(cls, "id");
			DomHelper.setAttributeValue(id, "name", idNameText.getText());
			DomHelper.setAttributeValue(id, "column", idColumnText.getText());
			DomHelper.setAttributeValue(id, "type", idTypeText.getText());
			String genClass = generatorClassText.getText().trim();
			Element generator = DomHelper.getFirstChildElement(id, "generator");
			if (!genClass.isEmpty()) {
				if (generator == null) {
					generator = DomHelper.addElement(id, "generator");
				}
				generator.setAttribute("class", genClass);
			} else if (generator != null) {
				DomHelper.removeElement(generator);
			}
		}

		@Override
		public void refresh() {
			refreshing = true;
			try {
				Element cls = getClassElement();
				Element id = cls != null ? DomHelper.getFirstChildElement(cls, "id") : null;
				idNameText.setText(DomHelper.getAttributeValue(id, "name"));
				idColumnText.setText(DomHelper.getAttributeValue(id, "column"));
				idTypeText.setText(DomHelper.getAttributeValue(id, "type"));
				Element generator = id != null ? DomHelper.getFirstChildElement(id, "generator") : null;
				generatorClassText.setText(DomHelper.getAttributeValue(generator, "class"));
			} finally {
				refreshing = false;
			}
			super.refresh();
		}
	}

	// --- Properties Table Section ---

	private class PropertiesTableSection extends SectionPart {
		private TableViewer tableViewer;

		PropertiesTableSection(Composite parent, IManagedForm managedForm) {
			super(parent, managedForm.getToolkit(), ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
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

			addColumn("name", 150);
			addColumn("type", 120);
			addColumn("column", 120);
			addColumn("not-null", 60);
			addColumn("unique", 60);
			addColumn("length", 60);

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

		private void addColumn(String attr, int width) {
			TableViewerColumn col = new TableViewerColumn(tableViewer, SWT.NONE);
			col.getColumn().setText(attr);
			col.getColumn().setWidth(width);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					return DomHelper.getAttributeValue((Element) element, attr);
				}
			});
		}

		private void onAdd() {
			Element cls = getClassElement();
			if (cls == null) return;
			HbmPropertyDialog dlg = new HbmPropertyDialog(getSection().getShell(), null);
			if (dlg.open() == Dialog.OK) {
				Element prop = DomHelper.addElement(cls, "property");
				dlg.applyTo(prop);
				refresh();
				markDirty();
			}
		}

		private void onEdit() {
			IStructuredSelection sel = tableViewer.getStructuredSelection();
			if (sel.isEmpty()) return;
			Element prop = (Element) sel.getFirstElement();
			HbmPropertyDialog dlg = new HbmPropertyDialog(getSection().getShell(), prop);
			if (dlg.open() == Dialog.OK) {
				dlg.applyTo(prop);
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
			Element cls = getClassElement();
			if (cls != null) {
				tableViewer.setInput(DomHelper.getChildElements(cls, "property"));
			} else {
				tableViewer.setInput(new Object[0]);
			}
			super.refresh();
		}
	}

	// --- Associations Section ---

	private static final String[] ASSOCIATION_TYPES = {
		"many-to-one", "one-to-one", "set", "bag", "list", "map"
	};

	private class AssociationsSection extends SectionPart {
		private TableViewer tableViewer;

		AssociationsSection(Composite parent, IManagedForm managedForm) {
			super(parent, managedForm.getToolkit(), ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
			createContent(getSection(), managedForm.getToolkit());
		}

		private void createContent(Section section, FormToolkit toolkit) {
			section.setText("Associations");
			Composite client = toolkit.createComposite(section);
			client.setLayout(new GridLayout(2, false));

			tableViewer = new TableViewer(client, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
			tableViewer.getTable().setHeaderVisible(true);
			tableViewer.getTable().setLinesVisible(true);
			GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
			gd.heightHint = 120;
			tableViewer.getTable().setLayoutData(gd);
			tableViewer.setContentProvider(ArrayContentProvider.getInstance());

			TableViewerColumn typeCol = new TableViewerColumn(tableViewer, SWT.NONE);
			typeCol.getColumn().setText("Type");
			typeCol.getColumn().setWidth(100);
			typeCol.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					return ((Element) element).getNodeName();
				}
			});

			TableViewerColumn nameCol = new TableViewerColumn(tableViewer, SWT.NONE);
			nameCol.getColumn().setText("Name");
			nameCol.getColumn().setWidth(150);
			nameCol.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					return DomHelper.getAttributeValue((Element) element, "name");
				}
			});

			TableViewerColumn classCol = new TableViewerColumn(tableViewer, SWT.NONE);
			classCol.getColumn().setText("Class/Entity");
			classCol.getColumn().setWidth(200);
			classCol.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					Element el = (Element) element;
					String cls = DomHelper.getAttributeValue(el, "class");
					if (cls.isEmpty()) {
						cls = DomHelper.getAttributeValue(el, "entity-name");
					}
					return cls;
				}
			});

			Composite buttons = toolkit.createComposite(client);
			buttons.setLayout(new GridLayout());
			buttons.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

			for (String assocType : ASSOCIATION_TYPES) {
				Button btn = toolkit.createButton(buttons, "Add " + assocType, SWT.PUSH);
				btn.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
				btn.addListener(SWT.Selection, e -> {
					Element cls = getClassElement();
					if (cls == null) return;
					Element assoc = DomHelper.addElement(cls, assocType);
					assoc.setAttribute("name", "newAssociation");
					if ("many-to-one".equals(assocType) || "one-to-one".equals(assocType)) {
						assoc.setAttribute("class", "");
					}
					if ("set".equals(assocType) || "bag".equals(assocType)
							|| "list".equals(assocType) || "map".equals(assocType)) {
						Element key = DomHelper.addElement(assoc, "key");
						key.setAttribute("column", "");
					}
					refresh();
					markDirty();
				});
			}

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
			Element cls = getClassElement();
			List<Element> assocs = new ArrayList<>();
			if (cls != null) {
				for (String type : ASSOCIATION_TYPES) {
					assocs.addAll(DomHelper.getChildElements(cls, type));
				}
			}
			tableViewer.setInput(assocs);
			super.refresh();
		}
	}

	// --- Property Edit Dialog ---

	static class HbmPropertyDialog extends Dialog {
		private Text nameText;
		private Text typeText;
		private Text columnText;
		private Text lengthText;
		private Button notNullCheck;
		private Button uniqueCheck;

		private String name = "";
		private String type = "";
		private String column = "";
		private String length = "";
		private boolean notNull = false;
		private boolean unique = false;

		HbmPropertyDialog(Shell shell, Element existing) {
			super(shell);
			if (existing != null) {
				name = DomHelper.getAttributeValue(existing, "name");
				type = DomHelper.getAttributeValue(existing, "type");
				column = DomHelper.getAttributeValue(existing, "column");
				length = DomHelper.getAttributeValue(existing, "length");
				notNull = "true".equals(DomHelper.getAttributeValue(existing, "not-null"));
				unique = "true".equals(DomHelper.getAttributeValue(existing, "unique"));
			}
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

			new Label(composite, SWT.NONE).setText("Type:");
			typeText = new Text(composite, SWT.BORDER);
			typeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			typeText.setText(type);

			new Label(composite, SWT.NONE).setText("Column:");
			columnText = new Text(composite, SWT.BORDER);
			columnText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			columnText.setText(column);

			new Label(composite, SWT.NONE).setText("Length:");
			lengthText = new Text(composite, SWT.BORDER);
			lengthText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			lengthText.setText(length);

			new Label(composite, SWT.NONE).setText("");
			notNullCheck = new Button(composite, SWT.CHECK);
			notNullCheck.setText("Not null");
			notNullCheck.setSelection(notNull);

			new Label(composite, SWT.NONE).setText("");
			uniqueCheck = new Button(composite, SWT.CHECK);
			uniqueCheck.setText("Unique");
			uniqueCheck.setSelection(unique);

			return composite;
		}

		@Override
		protected void okPressed() {
			name = nameText.getText().trim();
			type = typeText.getText().trim();
			column = columnText.getText().trim();
			length = lengthText.getText().trim();
			notNull = notNullCheck.getSelection();
			unique = uniqueCheck.getSelection();
			super.okPressed();
		}

		void applyTo(Element prop) {
			DomHelper.setAttributeValue(prop, "name", name);
			DomHelper.setAttributeValue(prop, "type", type);
			DomHelper.setAttributeValue(prop, "column", column);
			DomHelper.setAttributeValue(prop, "length", length);
			if (notNull) {
				prop.setAttribute("not-null", "true");
			} else {
				prop.removeAttribute("not-null");
			}
			if (unique) {
				prop.setAttribute("unique", "true");
			} else {
				prop.removeAttribute("unique");
			}
		}
	}
}
