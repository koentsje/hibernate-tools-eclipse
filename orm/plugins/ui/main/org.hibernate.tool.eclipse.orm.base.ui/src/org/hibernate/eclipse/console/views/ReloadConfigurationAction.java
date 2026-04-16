package org.hibernate.eclipse.console.views;

import java.util.Iterator;

import org.eclipse.jface.viewers.StructuredViewer;
import org.hibernate.tool.eclipse.orm.console.core.ConsoleConfiguration;
import org.hibernate.tool.eclipse.orm.console.core.HibernateConsoleRuntimeException;
import org.hibernate.tool.eclipse.orm.console.core.ui.ImageConstants;
import org.hibernate.tool.eclipse.common.base.core.messages.BasicHibernateMessages;
import org.hibernate.eclipse.console.HibernateBasePlugin;
import org.hibernate.eclipse.console.actions.ConsoleConfigurationBasedAction;
import org.hibernate.eclipse.ui.console.utils.EclipseImages;

public class ReloadConfigurationAction extends ConsoleConfigurationBasedAction {

	public static final String RELOADCONFIG_ACTIONID = "actionid.reloadconfig"; //$NON-NLS-1$

	private StructuredViewer viewer;

	protected ReloadConfigurationAction(StructuredViewer sv) {
		super(BasicHibernateMessages.ReloadConfigurationAction_rebuild_configuration);
		setEnabledWhenNoSessionFactory(true);
		viewer = sv;
		setImageDescriptor(EclipseImages.getImageDescriptor(ImageConstants.RELOAD) );
		setId(RELOADCONFIG_ACTIONID);
	}

	protected void doRun() {
		for (Iterator<?> i = getSelectedNonResources().iterator(); i.hasNext();) {
			try {
				Object node = i.next();
				if (node instanceof ConsoleConfiguration) {
					ConsoleConfiguration config = (ConsoleConfiguration) node;
					config.reset();
					updateState(config);
					viewer.refresh(node);
				}
			} catch (HibernateConsoleRuntimeException he) {
				HibernateBasePlugin.getDefault().showError(
						viewer.getControl().getShell(),
						BasicHibernateMessages.ReloadConfigurationAction_exception_while_start_hibernate, he);
			} catch (UnsupportedClassVersionError ucve) {
				HibernateBasePlugin
						.getDefault()
						.showError(
								viewer.getControl().getShell(),
								BasicHibernateMessages.ReloadConfigurationAction_starting_hibernate_resulted_exception,
								ucve);
			}
		}
	}

}
