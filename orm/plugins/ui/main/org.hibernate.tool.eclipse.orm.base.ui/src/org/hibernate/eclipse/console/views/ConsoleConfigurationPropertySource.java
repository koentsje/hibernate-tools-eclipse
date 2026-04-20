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
package org.hibernate.eclipse.console.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.ProfileManager;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.core.LaunchConfiguration;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.hibernate.tool.eclipse.orm.console.core.ConsoleConfiguration;
import org.hibernate.tool.eclipse.orm.console.core.KnownConfigurations;
import org.hibernate.tool.eclipse.orm.console.core.preferences.ConsoleConfigurationPreferences;
import org.hibernate.tool.eclipse.orm.console.core.preferences.ConsoleConfigurationPreferences.ConfigurationMode;
import org.hibernate.tool.eclipse.common.base.core.messages.BasicHibernateMessages;
import org.hibernate.eclipse.console.HibernateBasePlugin;
import org.hibernate.eclipse.launch.utils.LaunchHelper;
import org.hibernate.eclipse.launch.IBasicHibernateLaunchConstants;

@SuppressWarnings("restriction")
public class ConsoleConfigurationPropertySource implements IPropertySource {

	private ConsoleConfiguration cfg;

	static List<IPropertyDescriptor> pd;
	static {
		ComboBoxPropertyDescriptor modeDescriptor = new ComboBoxPropertyDescriptor(
			"mode",  //$NON-NLS-1$
			BasicHibernateMessages.ConsoleConfigurationPropertySource_mode,
			ConfigurationMode.labels());
		
		List<IPropertyDescriptor> l = new ArrayList<IPropertyDescriptor>();
		l.add(new TextPropertyDescriptor("name", BasicHibernateMessages.ConsoleConfigurationPropertySource_name)); //$NON-NLS-1$
		l.add(modeDescriptor);
		l.add(new PropertyDescriptor("hibernate.cfg.xml", BasicHibernateMessages.ConsoleConfigurationPropertySource_config_file)); //$NON-NLS-1$
		l.add(new PropertyDescriptor("hibernate.properties", BasicHibernateMessages.ConsoleConfigurationPropertySource_properties_file)); //$NON-NLS-1$
		l.add(new PropertyDescriptor("mapping.files", BasicHibernateMessages.ConsoleConfigurationPropertySource_additional_mapping_files)); //$NON-NLS-1$

		pd = l;
	}

	public ConsoleConfigurationPropertySource(ConsoleConfiguration cfg) {
		this.cfg = cfg;
	}

	public Object getEditableValue() {
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[pd.size() + 2];
		pd.toArray(propertyDescriptors);
		propertyDescriptors[propertyDescriptors.length - 2] = createProjectDescriptor();
		propertyDescriptors[propertyDescriptors.length - 1] = createConnectionDescriptor();
		return propertyDescriptors;
	}

	public Object getPropertyValue(Object id) {
		try {
		if("name".equals(id)) { //$NON-NLS-1$
			return cfg.getName();
		}		
		// TODO: bring back more eclipse friendly file names
		ConsoleConfigurationPreferences preferences = cfg.getPreferences();
		if ("project".equals(id)){ //$NON-NLS-1$
			try {
				ILaunchConfiguration lc = HibernateBasePlugin.getDefault().findLaunchConfig(cfg.getName());
				if (lc != null){
					String projectName = lc.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, "");  //$NON-NLS-1$
					return Arrays.binarySearch(getSortedProjectNames(), projectName);
				} else {
					HibernateBasePlugin.getDefault().log("Can't find Console Configuration \"" + cfg.getName() + "\"");//$NON-NLS-1$//$NON-NLS-2$
				}
			} catch (CoreException e) {
				HibernateBasePlugin.getDefault().log(e);
			}
		}
		if("mode".equals(id)) { //$NON-NLS-1$
			String[] values = ConfigurationMode.values();
			String value = preferences.getConfigurationMode().toString();
			for (int i = 0; i < values.length; i++) {
				if (value.equals(values[i])){
					return i;
				}
			}
			return new RuntimeException("Unknown ConsoleConfiguration mode: " + value);	//$NON-NLS-1$
		}
		if("connection".equals(id)) { //$NON-NLS-1$
			try {
				ILaunchConfiguration lc = HibernateBasePlugin.getDefault().findLaunchConfig(cfg.getName());
				if (lc != null){
					String connectionName = lc.getAttribute(IBasicHibernateLaunchConstants.CONNECTION_PROFILE_NAME, (String)null);  
					if (connectionName == null){
						connectionName = lc.getAttribute(IBasicHibernateLaunchConstants.USE_JPA_PROJECT_PROFILE, Boolean.FALSE.toString());
						if (Boolean.TRUE.toString().equalsIgnoreCase(connectionName)){
							connectionName = BasicHibernateMessages.ConnectionProfileCtrl_JPAConfiguredConnection;
						} else {
							connectionName = BasicHibernateMessages.ConnectionProfileCtrl_HibernateConfiguredConnection;
						}
					}
					String[] values = getConnectionNames();
					for (int i = 0; i < values.length; i++) {
						if (values[i].equals(connectionName)){
							return i;
						}
					}
				} else {
					HibernateBasePlugin.getDefault().log("Can't find Console Configuration \"" + cfg.getName() + "\"");//$NON-NLS-1$//$NON-NLS-2$
				}
			} catch (CoreException e) {
				HibernateBasePlugin.getDefault().log(e);
			}
		}
		if("hibernate.cfg.xml".equals(id)) { //$NON-NLS-1$
			return preferences.getConfigXMLFile();
		}
		if("hibernate.properties".equals(id)) { //$NON-NLS-1$
			return preferences.getPropertyFile();
		}
		if("mapping.files".equals(id)) { //$NON-NLS-1$
			return Integer.valueOf(preferences.getMappingFiles().length);
		}

		return null;
		} catch(RuntimeException e) {
			return BasicHibernateMessages.ConsoleConfigurationPropertySource_error + e.getMessage();
		}
	}

	public boolean isPropertySet(Object id) {
		return true;
	}

	public void resetPropertyValue(Object id) {
	}

	public void setPropertyValue(Object id, Object value) {
		if("name".equals(id) && value instanceof String) { //$NON-NLS-1$
			String newName = (String) value;
			if (LaunchHelper.verifyConfigurationName(newName) != null) {
				return;//just do not change name
			}
			String oldName = cfg.getName();			
			try {
				ILaunchConfiguration lc = HibernateBasePlugin.getDefault().findLaunchConfig(oldName);
				if (lc != null){
					ILaunchConfigurationWorkingCopy wc = lc.getWorkingCopy();
					wc.rename(newName);
					wc.doSave();
					//find newly created console configuration
					cfg = KnownConfigurations.getInstance().find(newName);
				} else {
					HibernateBasePlugin.getDefault().log("Can't find Console Configuration \"" + oldName + "\"");//$NON-NLS-1$//$NON-NLS-2$
				}
			} catch (CoreException e) {
				HibernateBasePlugin.getDefault().log(e);
			}
		} else if ("mode".equals(id) && value instanceof Integer){		//$NON-NLS-1$	
			int index = (Integer) value;
			try {
				ILaunchConfiguration lc = HibernateBasePlugin.getDefault().findLaunchConfig(cfg.getName());
				if (lc != null){
					ILaunchConfigurationWorkingCopy wc = lc.getWorkingCopy();
					wc.setAttribute("org.hibernate.eclipse.launch.CONFIGURATION_FACTORY", ConfigurationMode.values()[index]);////$NON-NLS-1$
					wc.doSave();
				} else {
					HibernateBasePlugin.getDefault().log("Can't find Console Configuration \"" + cfg.getName() + "\"");//$NON-NLS-1$//$NON-NLS-2$
				}
			} catch (CoreException e) {
				HibernateBasePlugin.getDefault().log("Can't find Console Configuration \"" + cfg.getName() + "\"");//$NON-NLS-1$//$NON-NLS-2$
			}			
		}  else if ("project".equals(id) && value instanceof Integer){		//$NON-NLS-1$	
			int index = (Integer) value;
			try {
				ILaunchConfiguration lc = HibernateBasePlugin.getDefault().findLaunchConfig(cfg.getName());
				if (lc != null){
					ILaunchConfigurationWorkingCopy wc = lc.getWorkingCopy();
					String projectName = getSortedProjectNames()[index];
					wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, projectName);
					if (projectName != null){
						wc.setAttribute(LaunchConfiguration.ATTR_MAPPED_RESOURCE_PATHS,
								Collections.singletonList(projectName));
						wc.setAttribute(LaunchConfiguration.ATTR_MAPPED_RESOURCE_TYPES, Collections.singletonList(Integer.toString(IResource.PROJECT)));
					} else {
						wc.removeAttribute(LaunchConfiguration.ATTR_MAPPED_RESOURCE_PATHS);
						wc.removeAttribute(LaunchConfiguration.ATTR_MAPPED_RESOURCE_TYPES);
					}
					wc.doSave();
				} else {
					HibernateBasePlugin.getDefault().log("Can't find Console Configuration \"" + cfg.getName() + "\"");//$NON-NLS-1$//$NON-NLS-2$
				}
			} catch (CoreException e) {
				HibernateBasePlugin.getDefault().log("Can't find Console Configuration \"" + cfg.getName() + "\"");//$NON-NLS-1$//$NON-NLS-2$
			}
		} else if ("connection".equals(id) && value instanceof Integer){		//$NON-NLS-1$	
			int index = (Integer) value;
			try {
				ILaunchConfiguration lc = HibernateBasePlugin.getDefault().findLaunchConfig(cfg.getName());
				if (lc != null){
					ILaunchConfigurationWorkingCopy wc = lc.getWorkingCopy();
					if (index == 0){//jpa
						wc.setAttribute(IBasicHibernateLaunchConstants.USE_JPA_PROJECT_PROFILE, Boolean.TRUE.toString());
						wc.removeAttribute(IBasicHibernateLaunchConstants.CONNECTION_PROFILE_NAME);
					} else if (index == 1){//hibernate
						wc.removeAttribute(IBasicHibernateLaunchConstants.USE_JPA_PROJECT_PROFILE);
						wc.removeAttribute(IBasicHibernateLaunchConstants.CONNECTION_PROFILE_NAME);
					} else {//connection profile
						String[] values = getConnectionNames();
						wc.setAttribute(IBasicHibernateLaunchConstants.CONNECTION_PROFILE_NAME, values[index]);
						wc.removeAttribute(IBasicHibernateLaunchConstants.USE_JPA_PROJECT_PROFILE);
					}
					wc.doSave();
				} else {
					HibernateBasePlugin.getDefault().log("Can't find Console Configuration \"" + cfg.getName() + "\"");//$NON-NLS-1$//$NON-NLS-2$
				}
			} catch (CoreException e) {
				HibernateBasePlugin.getDefault().log("Can't find Console Configuration \"" + cfg.getName() + "\"");//$NON-NLS-1$//$NON-NLS-2$
			}
		}
	}

	private IPropertyDescriptor createProjectDescriptor(){		
		ComboBoxPropertyDescriptor projectDescriptor = new ComboBoxPropertyDescriptor(
			"project",  //$NON-NLS-1$
			BasicHibernateMessages.ConsoleConfigurationPropertySource_project,
			getSortedProjectNames());
		projectDescriptor.setValidator(new ICellEditorValidator(){		
			public String isValid(Object value) {				
				if (value instanceof Integer){
					if (((Integer)value).intValue() < 0){
						try {
							ILaunchConfiguration lc = HibernateBasePlugin.getDefault().findLaunchConfig(cfg.getName());
							if (lc != null){
								String projectName = lc.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, (String)null);
								if (projectName != null){
									return NLS.bind(BasicHibernateMessages.ConsoleConfigurationMainTab_the_java_project_does_not_exist, projectName);
								}
							} else {
								HibernateBasePlugin.getDefault().log("Can't find Console Configuration \"" + cfg.getName() + "\"");//$NON-NLS-1$//$NON-NLS-2$
							}
						} catch (CoreException e) {
							HibernateBasePlugin.getDefault().log(e);
						}
					}
				}
				return null;
			}
		});
		return projectDescriptor;
	}
	
	private IPropertyDescriptor createConnectionDescriptor(){
		ComboBoxPropertyDescriptor connectionDescriptor = new ComboBoxPropertyDescriptor(
			"connection",  //$NON-NLS-1$
			BasicHibernateMessages.ConsoleConfigurationPropertySource_connection,
			getConnectionNames());
		return connectionDescriptor;
	}
	
	private String[] getSortedProjectNames(){
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();//get all projects
		String[] projectNames = new String[projects.length];
		for (int i = 0; i < projects.length; i++ ) {
			projectNames[i] = projects[i].getName();
		};		
		Arrays.sort(projectNames);
		return projectNames;
	}
	
	private String[] getConnectionNames(){
		IConnectionProfile[] profiles = ProfileManager.getInstance()
		.getProfilesByCategory("org.eclipse.datatools.connectivity.db.category"); //$NON-NLS-1$
		String[] names = new String[profiles.length];
		for (int i = 0; i < profiles.length; i ++){
			names[i] = profiles[i].getName();
		}
		Arrays.sort(names);
		String[] resNames = new String[names.length + 2];
		resNames[0] = BasicHibernateMessages.ConnectionProfileCtrl_JPAConfiguredConnection;
		resNames[1] = BasicHibernateMessages.ConnectionProfileCtrl_HibernateConfiguredConnection;		
		System.arraycopy(names, 0, resNames, 2, names.length);
		return resNames;
	}

}
