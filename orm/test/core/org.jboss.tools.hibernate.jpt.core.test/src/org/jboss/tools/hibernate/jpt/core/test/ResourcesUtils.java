package org.jboss.tools.hibernate.jpt.core.test;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;

public class ResourcesUtils {

	public static IProject importProject(Bundle bundle, String projectPath, IProgressMonitor monitor)
			throws CoreException, IOException {
		URL url = FileLocator.resolve(bundle.getEntry(projectPath));
		IPath locationPath = new Path(url.getFile());
		IPath dotProjectPath = locationPath.append(".project");
		IProjectDescription description = ResourcesPlugin.getWorkspace()
				.loadProjectDescription(dotProjectPath);
		String projectName = description.getName();
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (!project.exists()) {
			project.create(description, monitor);
		}
		if (!project.isOpen()) {
			project.open(monitor);
		}
		return project;
	}

	public static boolean setBuildAutomatically(boolean value) throws CoreException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceDescription desc = workspace.getDescription();
		boolean oldValue = desc.isAutoBuilding();
		if (oldValue != value) {
			desc.setAutoBuilding(value);
			workspace.setDescription(desc);
		}
		return oldValue;
	}
}
