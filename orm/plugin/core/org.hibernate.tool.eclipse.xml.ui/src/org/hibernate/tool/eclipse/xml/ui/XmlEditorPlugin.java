package org.hibernate.tool.eclipse.xml.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class XmlEditorPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "org.hibernate.tool.eclipse.xml.ui";

	private static XmlEditorPlugin instance;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		instance = null;
		super.stop(context);
	}

	public static XmlEditorPlugin getDefault() {
		return instance;
	}
}
