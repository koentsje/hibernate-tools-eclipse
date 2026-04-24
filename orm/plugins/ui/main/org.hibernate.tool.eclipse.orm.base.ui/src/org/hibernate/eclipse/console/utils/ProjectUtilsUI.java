package org.hibernate.eclipse.console.utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.hibernate.eclipse.console.HibernateBasePlugin;
import org.hibernate.tool.eclipse.orm.console.core.ConsoleConfiguration;
import org.hibernate.tool.eclipse.orm.console.core.preferences.ConsoleConfigurationPreferences;
import org.hibernate.tool.eclipse.orm.launch.core.utils.LaunchHelper;
import org.hibernate.tool.eclipse.orm.console.core.utils.ProjectUtils;

public class ProjectUtilsUI {

	private ProjectUtilsUI() {
	}

	static public IJavaProject findJavaProject(IEditorPart part) {
		if(part!=null) return findJavaProject(part.getEditorInput());
		return null;
	}

	static public IJavaProject findJavaProject(IEditorInput input) {
		if(input!=null && input instanceof IFileEditorInput) {
	         IFile file = null;
	         IProject project = null;
	         IJavaProject jProject = null;

	         IFileEditorInput fileInput = (IFileEditorInput) input;
	         file = fileInput.getFile();
	         project = file.getProject();
	         jProject = JavaCore.create(project);

	         return jProject;
	      }

		return null;
	}

	public static IProject findProject(ConsoleConfiguration consoleConfiguration) {
		if (consoleConfiguration != null) {
			try {
				ILaunchConfiguration launchConfiguration = LaunchHelper.findHibernateLaunchConfig(
						consoleConfiguration.getName());
				if (launchConfiguration != null) {
					String projName = launchConfiguration.getAttribute(
							IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, ""); //$NON-NLS-1$
					return ProjectUtils.findProject(projName);
				}
			} catch (CoreException e1) {
				HibernateBasePlugin.getDefault().log(e1);
			}
		}
		return null;
	}

	public static IJavaProject findJavaProject(ConsoleConfiguration consoleConfiguration) {
		IProject project = findProject(consoleConfiguration);
		if (project != null) {
			return JavaCore.create(project);
		}
		return null;
	}

	public static IJavaProject[] findJavaProjects(ConsoleConfiguration consoleConfiguration) {
		ConsoleConfigurationPreferences ccp = consoleConfiguration.getPreferences();
		URL[] classPathURLs = new URL[0];
		if (ccp != null) {
			classPathURLs = ccp.getCustomClassPathURLS();
		}
		ArrayList<IProject> projects = new ArrayList<IProject>();
		IFile file = null;
		for (int i = 0; i < classPathURLs.length; i++) {
			IPath path = Path.fromOSString(classPathURLs[i].getFile());
			file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			if (file == null || !ProjectUtils.exists(file)) {
				file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);
			}
			if (file != null && ProjectUtils.exists(file)) {
				updateCollection(projects, file.getProject());
			}
		}
		updateCollection(projects, findProject(consoleConfiguration));
		List<IJavaProject> res = new ArrayList<IJavaProject>();
		for (int i = 0; i < projects.size(); i++) {
			try {
				if (projects.get(i).isOpen() && projects.get(i).hasNature(JavaCore.NATURE_ID)){
					res.add(JavaCore.create(projects.get(i)));
				}
			} catch (CoreException e) {
				HibernateBasePlugin.getDefault().log(e);
			}
		}
		return res.toArray(new IJavaProject[0]);
	}

	private static boolean updateCollection(ArrayList<IProject> projects, IProject project) {
		if (project == null) {
			return false;
		}
		for (Iterator<IProject> it = projects.iterator(); it.hasNext();) {
			if (project.equals(it.next())) {
				return false;
			}
		}
		projects.add(project);
		return true;
	}
}
