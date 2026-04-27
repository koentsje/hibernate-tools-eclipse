package org.hibernate.tool.eclipse.orm.base.ui.console.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.hibernate.tool.eclipse.orm.console.core.ConsoleConfiguration;
import org.hibernate.tool.eclipse.orm.base.ui.nls.Messages;
import org.hibernate.tool.eclipse.orm.base.ui.console.HibernateBasePlugin;

public abstract class OpenQueryEditorAction extends SelectionListenerAction {

	protected OpenQueryEditorAction(String text) {
		super( text );
	}

	public void runWithEvent(Event event) {
		boolean showed = false;
		IStructuredSelection sel = getStructuredSelection();
		if (sel instanceof TreeSelection){
			TreePath[] paths = ((TreeSelection)sel).getPaths();
			showed = doRun(paths);
		}
		if(!showed) {
			openQueryEditor( null, "" );			 //$NON-NLS-1$
		}
	}

	protected boolean doRun(TreePath[] paths) {
		boolean showed = false;
		for (int i = 0; i < paths.length; i++) {
			TreePath path = paths[i];
			ConsoleConfiguration config = (ConsoleConfiguration) path.getSegment(0);
			try {
			  openQueryEditor( config, generateQuery(path) );
			  showed = true;
			} catch(Exception he) {
				HibernateBasePlugin.getDefault().showError(null, Messages.OpenQueryEditorAction_exception_open_hql_editor, he);
			}
		}
		return showed;
	}

	protected abstract void openQueryEditor(final ConsoleConfiguration config, String query);

	/**
	 * Generates default query for selected element.
	 * @param selection
	 * @return
	 */
	protected abstract String generateQuery(TreePath path);
}
