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
package org.hibernate.eclipse.codegen;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.hibernate.tool.eclipse.common.base.core.messages.BasicHibernateMessages;
import org.hibernate.eclipse.console.HibernateBasePlugin;

/**
 * Export wizard responsible to generate Ant code generation script
 * from existing Hibernate Tools code generation launch configuration.
 * 
 * @author Vitali Yemialyanchyk
 */
public class ExportAntCodeGenWizard extends Wizard implements IExportWizard {

	protected ExportAntCodeGenWizardPage exportAntCodeGenWizardPage = null;
    /**
     * The workbench.
     */
    private IWorkbench workbench;
    /**
     * The current selection.
     */
    protected IStructuredSelection selection;
	
    /**
     * Creates a wizard for creating a new file resource in the workspace.
     */
	public ExportAntCodeGenWizard() {
		setWindowTitle(BasicHibernateMessages.ExportAntCodeGenWizard_title);
		ImageDescriptor descriptor = HibernateBasePlugin.getImageDescriptor("icons/images/newhibernate_wiz.gif"); //$NON-NLS-1$
		setDefaultPageImageDescriptor(descriptor);
	}

    /* (non-Javadoc)
     * Method declared on IWizard.
     */
	public boolean performFinish() {
		IFile file = exportAntCodeGenWizardPage.createNewFile();
		if (file == null || file.getParent() == null) {
			return false;
		}
		try {
			file.getParent().refreshLocal(IResource.DEPTH_ZERO, null);
		} catch (CoreException e) {
			HibernateBasePlugin.getDefault()
				.logErrorMessage("Refresh parent: ", e); //$NON-NLS-1$
		}
		return true;
	}
	
    /* (non-Javadoc)
     * Method declared on IWizard.
     */
	public void addPages() {
		exportAntCodeGenWizardPage = new ExportAntCodeGenWizardPage(
			BasicHibernateMessages.ExportAntCodeGenWizard_page_name, getSelection());
		exportAntCodeGenWizardPage.setTitle(BasicHibernateMessages.ExportAntCodeGenWizard_title);
		exportAntCodeGenWizardPage.setDescription(BasicHibernateMessages.ExportAntCodeGenWizard_description);
		exportAntCodeGenWizardPage.setFileExtension("xml"); //$NON-NLS-1$
		addPage(exportAntCodeGenWizardPage);
	}
	
    /* (non-Javadoc)
     * Method declared on IWorkbenchWizard.
     */
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		this.workbench = workbench;
		this.selection = currentSelection;
		setWindowTitle(BasicHibernateMessages.ExportAntCodeGenWizard_title);
		ImageDescriptor descriptor = HibernateBasePlugin.getImageDescriptor("icons/images/newhibernate_wiz.gif"); //$NON-NLS-1$
		setDefaultPageImageDescriptor(descriptor);
	}

	/**
     * Returns the selection which was passed to <code>init</code>.
     *
     * @return the selection
     */
    public IStructuredSelection getSelection() {
        return selection;
    }
    
    /**
     * Returns the workbench which was passed to <code>init</code>.
     *
     * @return the workbench
     */
    public IWorkbench getWorkbench() {
        return workbench;
    }
}
