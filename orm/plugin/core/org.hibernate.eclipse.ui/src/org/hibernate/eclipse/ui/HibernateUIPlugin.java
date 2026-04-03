package org.hibernate.eclipse.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.hibernate.console.KnownConfigurations;
import org.hibernate.eclipse.ui.logging.EclipseLoggingStreamProvider;
import org.osgi.framework.BundleContext;

public class HibernateUIPlugin extends AbstractUIPlugin {

    private static HibernateUIPlugin plugin;

    public HibernateUIPlugin() {
        super();
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        KnownConfigurations.getInstance().setLoggingStreamProvider(new EclipseLoggingStreamProvider());
    }

    public void stop(BundleContext context) throws Exception {
        KnownConfigurations.getInstance().setLoggingStreamProvider(null);
        plugin = null;
        super.stop(context);
    }

    public static HibernateUIPlugin getDefault() {
        return plugin;
    }
}
