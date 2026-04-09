package org.hibernate.eclipse.launch;

import org.eclipse.core.runtime.Plugin;
import org.hibernate.eclipse.console.common.HibernateExtension;
import org.hibernate.eclipse.launch.exporter.ConsoleExtension;
import org.osgi.framework.BundleContext;

public class LaunchCorePlugin extends Plugin {

	private static LaunchCorePlugin plugin;

	public LaunchCorePlugin() {
		super();
		plugin = this;
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		HibernateExtension.setConsoleExtensionFactory(he -> {
			ConsoleExtension ce = new ConsoleExtension();
			ce.setHibernateExtention(he);
			return ce;
		});
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	public static LaunchCorePlugin getDefault() {
		return plugin;
	}

}
