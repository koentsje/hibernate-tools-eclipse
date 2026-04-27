package org.hibernate.tool.eclipse.orm.base.ui.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.hibernate.tool.eclipse.orm.launch.core.model.ExporterDefinition;

public class ExporterDefinitionUI {

    public static ImageDescriptor getIconDescriptor(ExporterDefinition definition) {
        if (definition.getIconPath() != null && definition.getContributorName() != null) {
            return AbstractUIPlugin.imageDescriptorFromPlugin(
                    definition.getContributorName(), definition.getIconPath());
        }
        return null;
    }
}
