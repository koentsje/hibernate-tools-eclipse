package org.hibernate.tool.eclipse.orm.base.ui.console.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.hibernate.tool.eclipse.orm.console.core.ConsoleConfiguration;
import org.hibernate.tool.eclipse.orm.console.core.KnownConfigurations;
import org.hibernate.tool.eclipse.orm.base.ui.console.HibernateBasePlugin;
import org.hibernate.tool.eclipse.orm.launch.core.utils.LaunchHelper;

@SuppressWarnings("restriction")
public class LaunchHelperUI {

	private LaunchHelperUI() {
	}

	public static ConsoleConfiguration[] findFilteredSortedConsoleConfigs() {
		ConsoleConfiguration[] ccs = KnownConfigurations.getInstance().getConfigurationsSortedByName();
		List<ConsoleConfiguration> consoleConfigurations = new ArrayList<ConsoleConfiguration>();
		for (ConsoleConfiguration cc : ccs) {
			boolean isAccepted = true;
			try {
				ILaunchConfiguration config = LaunchHelper.findHibernateLaunchConfig(cc.getName());
				if (config != null){
					isAccepted = org.eclipse.debug.internal.ui.DebugUIPlugin.doLaunchConfigurationFiltering(config);
				}
			} catch (CoreException e) {
				HibernateBasePlugin.getDefault().showError(null, e.getLocalizedMessage(), e);
			}
			if (isAccepted){
				consoleConfigurations .add(cc);
			}
		}
		return consoleConfigurations.toArray(new ConsoleConfiguration[consoleConfigurations.size()]);
	}

	public static ILaunchConfiguration[] filterCodeGenerationConfigs(ILaunchConfiguration[] launchConfigs) throws CoreException{
		List<ILaunchConfiguration> res = new ArrayList<ILaunchConfiguration>();
		for (ILaunchConfiguration config : launchConfigs) {
			if (DebugUIPlugin.doLaunchConfigurationFiltering(config)) {
				res.add(config);
			}
		}
		return res.toArray(new ILaunchConfiguration[res.size()]);
	}

	public static ILaunchConfiguration[] findFilteredCodeGenerationConfigs() throws CoreException{
		return filterCodeGenerationConfigs(LaunchHelper.findCodeGenerationConfigs());
	}

	public static ILaunchConfiguration[] findFilteredCodeGenerationConfigsSorted() throws CoreException{
		return filterCodeGenerationConfigs(LaunchHelper.findCodeGenerationConfigsSortedByName());
	}
}
