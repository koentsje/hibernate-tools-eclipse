/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibernate.tool.eclipse.orm.console.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.internal.resources.ICoreConstants;
import org.eclipse.core.internal.resources.ResourceInfo;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.core.SourceType;
import org.hibernate.tool.eclipse.orm.console.core.eclipse.HibernateConsoleCorePlugin;
import org.hibernate.tool.eclipse.common.base.core.messages.BasicHibernateMessages;
import org.hibernate.tool.eclipse.orm.console.core.properties.HibernatePropertiesConstants;
import org.hibernate.tool.eclipse.common.base.core.utils.StringHelper;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.xml.sax.InputSource;

@SuppressWarnings("restriction")
public class ProjectUtils {

	private ProjectUtils() {
	}

	public static boolean toggleHibernateOnProject(IProject project, boolean enable,String defaultConsoleName) {
		IScopeContext scope = new ProjectScope(project);

		Preferences node = scope.getNode(HibernatePropertiesConstants.HIBERNATE_CONSOLE_NODE);

		if(node!=null) {
			node.putBoolean(HibernatePropertiesConstants.HIBERNATE3_ENABLED, enable );
			node.put(HibernatePropertiesConstants.DEFAULT_CONFIGURATION, defaultConsoleName );
			try {
				node.flush();
			} catch (BackingStoreException e) {
				HibernateConsoleCorePlugin.getDefault().logErrorMessage(BasicHibernateMessages.ProjectUtils_could_not_save_changes_to_preferences, e);
				return false;
			}
		} else {
			return false;
		}

		try {
			if(enable) {
				return ProjectUtils.addProjectNature(project, HibernatePropertiesConstants.HIBERNATE_NATURE, new NullProgressMonitor() );
			} else {
				return ProjectUtils.removeProjectNature(project, HibernatePropertiesConstants.HIBERNATE_NATURE, new NullProgressMonitor() );
			}
		} catch(CoreException ce) {
			HibernateConsoleCorePlugin.getDefault().logErrorMessage(BasicHibernateMessages.ProjectUtils_could_not_activate_hibernate_nature_on_project + project.getName(), ce);
			HibernateConsoleCorePlugin.getDefault().log(ce.getStatus() );
			return false;
		}

	}

	public static boolean addProjectNature(IProject project, String nature, IProgressMonitor monitor) throws CoreException {
		if (monitor != null && monitor.isCanceled() ) {
			throw new OperationCanceledException();
		}

		if (!project.hasNature(nature) ) {
			IProjectDescription description = project.getDescription();
			String[] prevNatures= description.getNatureIds();
			String[] newNatures= new String[prevNatures.length + 1];
			System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
			newNatures[prevNatures.length]= nature;
			description.setNatureIds(newNatures);
			project.setDescription(description, monitor);
			return true;
		} else {
			monitor.worked(1);
			return false;
		}
	}

	public static boolean removeProjectNature(IProject project, String nature, NullProgressMonitor monitor) throws CoreException {
		if (monitor != null && monitor.isCanceled() ) {
			throw new OperationCanceledException();
		}

		if (project.hasNature(nature) ) {
			IProjectDescription description = project.getDescription();

			String[] natures = description.getNatureIds();
	        String[] newNatures = new String[natures.length - 1];
	        for(int i = 0, j = 0; i < natures.length; i++) {
	            if (!natures[i].equals(nature)) {
	                newNatures[j++] = natures[i];
	            }
	        }
	        description.setNatureIds(newNatures);
	        project.setDescription(description, monitor);
			return true;
		} else {
			monitor.worked(1);
			return false;
		}
	}

	public static IProject findProject(String name) {
		if (StringHelper.isEmpty(name)) {
			return null;
		}
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(name);
		return project;
	}

	public static IJavaProject findJavaProject(String name) {
		IProject project = findProject(name);
		if (project != null) {
			return JavaCore.create(project);
		}
		return null;
	}

	public static boolean exists(IFile f) {
		if (!(f instanceof File)) {
			return false;
		}
		File file = (File)f;
		ResourceInfo info = file.getResourceInfo(false, false);
		int flags = file.getFlags(info);
		if (flags != ICoreConstants.NULL_FLAG) {
			int type = ResourceInfo.getType(flags);
			if (type == IResource.FILE || type == IResource.FOLDER || type == IResource.PROJECT) {
				return true;
			}
		}
		return false;
	}

	static public org.eclipse.jdt.core.dom.CompilationUnit getCompilationUnit(
			ICompilationUnit source, boolean bindings) {
		ASTParser parser = ASTParser.newParser(AST.JLS9);
		parser.setSource(source);
		parser.setResolveBindings(bindings);
		org.eclipse.jdt.core.dom.CompilationUnit result = (org.eclipse.jdt.core.dom.CompilationUnit) parser.createAST(null);
		return result;
	}

	static public ICompilationUnit findCompilationUnit(IJavaProject javaProject,
			String fullyQualifiedName) {
		IType lwType = findType(javaProject, fullyQualifiedName);
		if (lwType != null) {
			return lwType.getCompilationUnit();
		}
		return null;
	}

	static public IType findType(IJavaProject javaProject,
			String fullyQualifiedName) {
		IType lwType = null;
		try {
			lwType = javaProject.findType(fullyQualifiedName);
		} catch (JavaModelException e) {
			// just ignore it!
		}
		return lwType;
	}

	static public String getParentTypename(IJavaProject proj, String fullyQualifiedName) {
		String res = null;
		ICompilationUnit icu = findCompilationUnit(proj, fullyQualifiedName);
		if (icu == null) {
			return res;
		}
		org.eclipse.jdt.core.dom.CompilationUnit cu = getCompilationUnit(icu, true);
		if (cu == null) {
			return res;
		}
		List<?> types = cu.types();
		for (int i = 0; i < types.size() && res == null; i++) {
			Object obj = types.get(i);
			if (!(obj instanceof TypeDeclaration)) {
				continue;
			}
			TypeDeclaration td = (TypeDeclaration)obj;
			Type superType = td.getSuperclassType();
			if (superType != null) {
				ITypeBinding tb = superType.resolveBinding();
				if (tb != null) {
					if (tb.getJavaElement() instanceof SourceType) {
						SourceType sourceT = (SourceType)tb.getJavaElement();
						try {
							res = sourceT.getFullyQualifiedParameterizedName();
						}
						catch (JavaModelException e) {
							HibernateConsoleCorePlugin.getDefault().logErrorMessage("JavaModelException: ", e); //$NON-NLS-1$
						}
					}
				}
			}
		}
		return res;
	}

	public static String[] availablePersistenceUnits(IJavaProject javaProject) {
		if (javaProject != null && javaProject.isOpen()){
			Set<IJavaProject> projects = new HashSet<IJavaProject>();
			projects.add(javaProject);
			try {
				String[] requiredProjectNames = javaProject.getRequiredProjectNames();
				for (String projectName : requiredProjectNames) {
					IProject project = findProject(projectName);
					try {
						if (project != null && project.isAccessible() && project.hasNature(JavaCore.NATURE_ID)){
							projects.add(JavaCore.create(project));
						}
					} catch (CoreException e) {
						HibernateConsoleCorePlugin.getDefault().log(e);
					}
				}
			} catch (JavaModelException e) {
				HibernateConsoleCorePlugin.getDefault().log(e);
			}
			Set<String> puNames = new TreeSet<String>();
			for (IJavaProject iJavaProject : projects) {
				for (String puName : projectPersistenceUnits(iJavaProject)) {
					puNames.add(puName);
				}
			}
			return puNames.toArray(new String[puNames.size()]);
		}
		return new String[0];
	}

	private static String[] projectPersistenceUnits(IJavaProject javaProject) {
		if (javaProject == null || javaProject.getResource() == null) {
			return new String[0];
		}
		IPath projPathFull = javaProject.getResource().getLocation();
		IPath projPath = javaProject.getPath();
		IPath path = javaProject.readOutputLocation().append("META-INF/persistence.xml"); //$NON-NLS-1$
		path = path.makeRelativeTo(projPath);
		path = projPathFull.append(path);
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);
		if (!exists(file)) {
			return new String[0];
		}
		InputStream is = null;
		org.dom4j.Document doc = null;
		try {
			is = file.getContents();
			SAXReader saxReader = new SAXReader();
			doc = saxReader.read(new InputSource(is));
		} catch (CoreException e) {
			HibernateConsoleCorePlugin.getDefault().logErrorMessage("CoreException: ", e); //$NON-NLS-1$
		} catch (DocumentException e) {
			HibernateConsoleCorePlugin.getDefault().logErrorMessage("DocumentException: ", e); //$NON-NLS-1$
		} finally {
			try {
				if (is != null) is.close();
			}
			catch (IOException ioe) {
				//ignore
			}
		}
		if (doc == null || doc.getRootElement() == null) {
			return new String[0];
		}
		Iterator<?> it = doc.getRootElement().elements("persistence-unit").iterator(); //$NON-NLS-1$
		ArrayList<String> res = new ArrayList<String>();
		while (it.hasNext()) {
			Element el = (Element)it.next();
			res.add(el.attributeValue("name")); //$NON-NLS-1$
		}
		return res.toArray(new String[0]);
	}
}
