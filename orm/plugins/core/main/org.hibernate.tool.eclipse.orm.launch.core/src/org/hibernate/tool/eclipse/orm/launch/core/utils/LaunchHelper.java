package org.hibernate.tool.eclipse.orm.launch.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.osgi.util.NLS;
import org.hibernate.tool.eclipse.orm.launch.core.nls.Messages;
import org.hibernate.tool.eclipse.orm.console.core.eclipse.HibernateConsoleCorePlugin;
import org.hibernate.tool.eclipse.orm.launch.core.ICodeGenerationLaunchConstants;

public class LaunchHelper {

	public static final String TEMPORARY_CONFIG_FLAG = "_TEMPORARY_CONFIG_"; //$NON-NLS-1$

	public static ILaunchConfiguration findHibernateLaunchConfig(String name) throws CoreException {
		return findLaunchConfigurationByName(
			ICodeGenerationLaunchConstants.CONSOLE_CONFIGURATION_LAUNCH_TYPE_ID, name);
	}

	public static ILaunchConfigurationType getHibernateLaunchConfigsType(){
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		return launchManager.getLaunchConfigurationType(
			ICodeGenerationLaunchConstants.CONSOLE_CONFIGURATION_LAUNCH_TYPE_ID);
	}

	public static ILaunchConfiguration[] findHibernateLaunchConfigs() throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		return launchManager.getLaunchConfigurations(getHibernateLaunchConfigsType());
	}

	public static ILaunchConfiguration findLaunchConfigurationByName(String launchConfigurationTypeId, String name) throws CoreException {
		Assert.isNotNull(launchConfigurationTypeId, Messages.LaunchHelper_launch_cfg_type_cannot_be_null);
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();

		ILaunchConfigurationType launchConfigurationType = launchManager
				.getLaunchConfigurationType(launchConfigurationTypeId);

		ILaunchConfiguration[] launchConfigurations = launchManager
				.getLaunchConfigurations(launchConfigurationType);

		for (int i = 0; i < launchConfigurations.length; i++) {
			ILaunchConfiguration launchConfiguration = launchConfigurations[i];
			if (launchConfiguration.getName().equals(name)) {
				return launchConfiguration;
			}
		}
		return null;
	}

	public static ILaunchConfiguration[] findProjectRelatedHibernateLaunchConfigs(String projectName) throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfiguration[] configs = launchManager.getLaunchConfigurations(getHibernateLaunchConfigsType());
		List<ILaunchConfiguration> list = new ArrayList<ILaunchConfiguration>();
		for(int i = 0; i < configs.length && configs[i].exists(); i++) {
			String project = configs[i].getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, (String)null);
			if (projectName.equals(project)) list.add(configs[i]);
		}
		return list.toArray(new ILaunchConfiguration[list.size()]);
	}

	public static String verifyConfigurationName(String currentName) {
		if (currentName == null || currentName.length() < 1) {
			return Messages.ConsoleConfigurationWizardPage_name_must_specified;
		}
		if (Platform.OS_WIN32.equals(Platform.getOS())) {
			String[] badnames = new String[] { "aux", "clock$", "com1", "com2", "com3", "com4", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
					"com5", "com6", "com7", "com8", "com9", "con", "lpt1", "lpt2", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
					"lpt3", "lpt4", "lpt5", "lpt6", "lpt7", "lpt8", "lpt9", "nul", "prn" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
			for (int i = 0; i < badnames.length; i++) {
				if (currentName.equals(badnames[i])) {
					return NLS.bind(Messages.ConsoleConfigurationWizardPage_bad_name, currentName);
				}
			}
		}
		char[] disallowedChars = new char[] { '@', '&', '\\', '/', ':', '*', '?', '"', '<', '>', '|', '\0' };
		for (int i = 0; i < disallowedChars.length; i++) {
			char c = disallowedChars[i];
			if (currentName.indexOf(c) > -1) {
				return NLS.bind(Messages.ConsoleConfigurationWizardPage_bad_char, c);
			}
		}

		if(existingLaunchConfiguration(currentName)) {
			return Messages.ConsoleConfigurationWizardPage_config_name_already_exist;
		}
		return null;
	}

	public static boolean existingLaunchConfiguration(String name) {
		try {
			ILaunchConfiguration config = findHibernateLaunchConfig(name);
			if(config != null && !config.getAttribute(TEMPORARY_CONFIG_FLAG, false)) {
				if (name.equalsIgnoreCase(config.getName())) {
					return true;
				}
			}
		} catch (CoreException e) {
			HibernateConsoleCorePlugin.getDefault().logErrorMessage(e.getMessage(), e);
		}
		return false;
	}

	public static ILaunchConfiguration[] findCodeGenerationConfigs() throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		return launchManager.getLaunchConfigurations(getCodeGenerationType());
	}

	public static ILaunchConfiguration[] findCodeGenerationConfigsSortedByName() throws CoreException {
		ILaunchConfiguration[] launchConfigs = findCodeGenerationConfigs();
		Comparator<ILaunchConfiguration> comparator = new Comparator<ILaunchConfiguration>() {
			public int compare(ILaunchConfiguration o1, ILaunchConfiguration o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		};
		Arrays.sort(launchConfigs, comparator);
		return launchConfigs;
	}

	public static ILaunchConfigurationType getCodeGenerationType(){
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		return launchManager.getLaunchConfigurationType(
			ICodeGenerationLaunchConstants.CODE_GENERATION_LAUNCH_TYPE_ID);
	}
}
