/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.hibernate.tool.eclipse.orm.launch.core.refactoring;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.NullChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.hibernate.tool.eclipse.orm.console.core.eclipse.HibernateConsoleCorePlugin;
import org.hibernate.tool.eclipse.orm.launch.core.nls.Messages;
import org.hibernate.tool.eclipse.orm.console.core.properties.HibernatePropertiesConstants;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * @author Dmitry Geraskov
 *
 */
public class ProjectDefaultConfigurationChange extends Change {

	private IProject project;

	private String newConsoleName;

	public ProjectDefaultConfigurationChange(IProject project, String newCCName){
		this.project = project;
		this.newConsoleName = newCCName;
	}

	@Override
	public String getName() {
		return Messages.ProjectDefaultConfigurationChange_name + project.getName();
	}

	@Override
	public void initializeValidationData(IProgressMonitor pm) {}

	@Override
	public RefactoringStatus isValid(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		return new RefactoringStatus();
	}

	@Override
	public Change perform(IProgressMonitor pm) throws CoreException {
		try {
			IScopeContext scope = new ProjectScope(project);

			Preferences node = scope.getNode(HibernatePropertiesConstants.HIBERNATE_CONSOLE_NODE);
			String oldName = node.get(HibernatePropertiesConstants.DEFAULT_CONFIGURATION, null);

			node.putBoolean(HibernatePropertiesConstants.HIBERNATE3_ENABLED, true );
			node.put(HibernatePropertiesConstants.DEFAULT_CONFIGURATION, newConsoleName );
			node.flush();
			return new ProjectDefaultConfigurationChange(project, oldName);
		} catch (BackingStoreException e) {
			HibernateConsoleCorePlugin.getDefault().logErrorMessage(
					Messages.ProjectDefaultConfigurationChange_error_title + ": " + e.getLocalizedMessage(), e); //$NON-NLS-1$
			return new NullChange();
		}
	}

	@Override
	public Object getModifiedElement() {
		return project;
	}

}
