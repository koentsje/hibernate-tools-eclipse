package org.hibernate.tool.eclipse.xml.ui.internal.common;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextInputListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.w3c.dom.Document;

@SuppressWarnings("restriction")
public abstract class AbstractHibernateFormEditor extends FormEditor {

	private StructuredTextEditor sourceEditor;
	private int sourcePageIndex;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
	}

	@Override
	protected void addPages() {
		try {
			createSourceEditor();
			addFormPages();
			addSourcePage();
		} catch (PartInitException e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract void addFormPages() throws PartInitException;

	private void createSourceEditor() throws PartInitException {
		sourceEditor = new StructuredTextEditor();
		sourceEditor.setEditorPart(this);
		sourceEditor.addPropertyListener(new PropertyListener());
	}

	private void addSourcePage() throws PartInitException {
		sourcePageIndex = addPage(sourceEditor, getEditorInput());
		setPageText(sourcePageIndex, "Source");
		sourceEditor.update();
		firePropertyChange(PROP_TITLE);
		sourceEditor.getTextViewer().addTextInputListener(new TextInputListener());
	}

	public IDOMDocument getDOMDocument() {
		if (sourceEditor != null) {
			return (IDOMDocument) sourceEditor.getAdapter(Document.class);
		}
		return null;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		sourceEditor.doSave(monitor);
	}

	@Override
	public void doSaveAs() {
		sourceEditor.doSaveAs();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return sourceEditor.isSaveAsAllowed();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getAdapter(Class<T> adapter) {
		if (sourceEditor != null) {
			T result = sourceEditor.getAdapter(adapter);
			if (result != null) {
				return result;
			}
		}
		return super.getAdapter(adapter);
	}

	private class PropertyListener implements IPropertyListener {
		@Override
		public void propertyChanged(Object source, int propId) {
			if (source == sourceEditor) {
				switch (propId) {
				case IEditorPart.PROP_INPUT:
				case IEditorPart.PROP_DIRTY:
					if (sourceEditor.getEditorInput() != getEditorInput()) {
						setInput(sourceEditor.getEditorInput());
						postOnDisplayQueue(() -> firePropertyChange(IWorkbenchPart.PROP_TITLE));
					}
					break;
				case IWorkbenchPart.PROP_TITLE:
					if (sourceEditor.getEditorInput() != getEditorInput()) {
						setInput(sourceEditor.getEditorInput());
					}
					break;
				default:
					firePropertyChange(propId);
					break;
				}
			}
		}
	}

	private class TextInputListener implements ITextInputListener {
		@Override
		public void inputDocumentAboutToBeChanged(IDocument oldInput, IDocument newInput) {
		}

		@Override
		public void inputDocumentChanged(IDocument oldInput, IDocument newInput) {
		}
	}

	private void postOnDisplayQueue(Runnable runnable) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
		if (windows != null && windows.length > 0) {
			Display display = windows[0].getShell().getDisplay();
			display.asyncExec(runnable);
		} else {
			runnable.run();
		}
	}
}
