package org.hibernate.tool.eclipse.orm.console.core;

import java.io.File;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;

public interface IConsoleExtension {

	Map<String, File[]> launchExporters(ILaunchConfiguration configuration,
			String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException;

	HibernateExtension getHibernateExtension();

}
