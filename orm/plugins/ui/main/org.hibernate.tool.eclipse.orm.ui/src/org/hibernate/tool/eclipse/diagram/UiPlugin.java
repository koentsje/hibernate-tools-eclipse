/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.hibernate.tool.eclipse.ui.diagram;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.hibernate.eclipse.console.HibernateConsolePlugin;

/**
 *
 */
public class UiPlugin {

	public final static String ID = "org.hibernate.tool.eclipse.ui"; //$NON-NLS-1$

	private static final UiPlugin INSTANCE = new UiPlugin();

	public static UiPlugin getDefault() {
		return INSTANCE;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(
				HibernateConsolePlugin.getDefault().getBundle().getSymbolicName(), path);
	}

	public static ImageDescriptor getImageDescriptor2(String name) {
		final String iconPath = "images/"; //$NON-NLS-1$
		final URL installURL = HibernateConsolePlugin.getDefault().getBundle().getEntry("/"); //$NON-NLS-1$
		URL url = null;
		try {
			url = new URL(installURL, iconPath + name);
		} catch (MalformedURLException e) {
		}
		return ImageDescriptor.createFromURL(url);
	}

	public static IWorkbenchPage getPage() {
	    IWorkbench workbench = PlatformUI.getWorkbench();
	    IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
	    return window.getActivePage();
	}

	public static Shell getShell() {
	    IWorkbench workbench = PlatformUI.getWorkbench();
	    IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
	    return window.getShell();
	}

	public org.eclipse.core.runtime.IPath getStateLocation() {
		return HibernateConsolePlugin.getDefault().getStateLocation();
	}
}
