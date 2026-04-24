package org.hibernate.tool.eclipse.orm.launch.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.hibernate.tool.eclipse.orm.launch.core.nls.Messages;

public class ConsoleConfigurationLaunchDelegate extends LaunchConfigurationDelegate {

	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		throw new IllegalStateException(Messages.ConsoleConfigurationLaunchDelegate_direct_launch_not_supported);
	}


}
