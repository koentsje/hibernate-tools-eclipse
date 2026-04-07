package org.hibernate.eclipse.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.hibernate.eclipse.console.model.impl.ExporterDefinition;

public class ExporterDefinitionUI {

    public static ImageDescriptor getIconDescriptor(ExporterDefinition definition) {
        if (definition.getIconPath() != null && definition.getContributorName() != null) {
            return AbstractUIPlugin.imageDescriptorFromPlugin(
                    definition.getContributorName(), definition.getIconPath());
        }
        return null;
    }
}
