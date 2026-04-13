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
package org.hibernate.tool.eclipse.orm.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;

public class HibernateNature implements IProjectNature {

	final public static String ID = "org.hibernate.eclipse.console.hibernateNature"; //$NON-NLS-1$

	private IProject project;

	public void configure() throws CoreException {
		IProjectDescription desc = project.getDescription();
		ICommand[] commands = desc.getBuildSpec();
		boolean found = false;

		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(HibernateBuilder.BUILDER_ID)) {
				found = true;
				break;
			}
		}
		if (!found) {
			ICommand command = desc.newCommand();
			command.setBuilderName(HibernateBuilder.BUILDER_ID);
			List<ICommand> list = new ArrayList<ICommand>();
			list.addAll(Arrays.asList(commands));
			list.add(command);
			desc.setBuildSpec(list.toArray(new ICommand[]{}));
			project.setDescription(desc, new NullProgressMonitor());
		}
	}

	public void deconfigure() throws CoreException {
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	/** return HibernateNature or null for a project **/
	public static HibernateNature getHibernateNature(IJavaProject project) {
		try {
			if(project!=null && project.getProject().isOpen() && project.getProject().hasNature(HibernateNature.ID)) {
				return (HibernateNature) project.getProject().getNature(HibernateNature.ID);
			}
		}
		catch (CoreException e) {
			Platform.getLog(HibernateNature.class).log(
				new Status(IStatus.ERROR, "org.hibernate.tool.eclipse.orm.base.core",
					"Exception when trying to locate Hibernate Nature", e));
		}
		return null;
	}

}
