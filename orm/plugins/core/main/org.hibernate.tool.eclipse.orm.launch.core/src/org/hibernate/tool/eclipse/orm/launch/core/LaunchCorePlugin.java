package org.hibernate.tool.eclipse.orm.launch.core;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class LaunchCorePlugin extends Plugin {

	private static LaunchCorePlugin plugin;

	public LaunchCorePlugin() {
		super();
		plugin = this;
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	public static LaunchCorePlugin getDefault() {
		return plugin;
	}

}
