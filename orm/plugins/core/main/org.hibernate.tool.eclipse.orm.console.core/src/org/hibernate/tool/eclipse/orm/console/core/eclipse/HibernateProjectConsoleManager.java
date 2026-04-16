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
package org.hibernate.tool.eclipse.orm.console.core.eclipse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.hibernate.tool.eclipse.orm.console.core.ConsoleConfiguration;
import org.hibernate.tool.eclipse.orm.console.core.KnownConfigurations;
import org.hibernate.tool.eclipse.orm.console.core.execution.ExecutionContext.Command;
import org.hibernate.tool.eclipse.common.base.core.messages.BasicHibernateMessages;
import org.hibernate.tool.eclipse.orm.console.core.properties.HibernatePropertiesConstants;
import org.hibernate.tool.eclipse.orm.runtime.spi.IConfiguration;
import org.hibernate.tool.eclipse.orm.runtime.spi.IService;
import org.hibernate.tool.eclipse.orm.runtime.spi.ITable;
import org.osgi.service.prefs.Preferences;

/**
 * Provides console-configuration-related services for Hibernate-enabled projects.
 * This class extracts the console-specific functionality that was previously
 * embedded in HibernateNature.
 */
public class HibernateProjectConsoleManager {

	private static final Map<IProject, TableCache> tableCaches = new ConcurrentHashMap<>();

	public static ConsoleConfiguration getDefaultConsoleConfiguration(IJavaProject project) {
		String cfg = getDefaultConsoleConfigurationName(project);
		if (cfg == null) return null;
		return KnownConfigurations.getInstance().find(cfg);
	}

	public static String getDefaultConsoleConfigurationName(IJavaProject project) {
		if (project == null) return null;
		IScopeContext scope = new ProjectScope(project.getProject());
		Preferences node = scope.getNode(HibernatePropertiesConstants.HIBERNATE_CONSOLE_NODE);
		if (node != null) {
			return node.get(HibernatePropertiesConstants.DEFAULT_CONFIGURATION, project.getProject().getName());
		}
		return null;
	}

	public static List<ITable> getTables(IJavaProject project) {
		if (project == null) return Collections.emptyList();
		ConsoleConfiguration ccfg = getDefaultConsoleConfiguration(project);
		if (ccfg == null) return Collections.emptyList();

		TableCache cache = tableCaches.computeIfAbsent(
			project.getProject(), k -> new TableCache());

		if (cache.tables != null) {
			return cache.tables;
		}
		if (cache.job == null) {
			cache.job = new ReadDatabaseMetaData(project.getProject(), ccfg, cache);
			cache.job.setPriority(Job.DECORATE);
			cache.job.schedule();
		} else if (cache.job.getState() == Job.NONE) {
			cache.job.schedule();
		}
		return Collections.emptyList();
	}

	public static List<ITable> getMatchingTables(IJavaProject project, String tableName) {
		List<ITable> result = new ArrayList<ITable>();
		Iterator<ITable> tableMappings = getTables(project).iterator();
		while (tableMappings.hasNext()) {
			ITable table = tableMappings.next();
			if (table.getName().toUpperCase().startsWith(tableName.toUpperCase())) {
				result.add(table);
			}
		}
		return result;
	}

	public static ITable getTable(IJavaProject project, String tableName) {
		for (ITable table : getTables(project)) {
			if (tableName.equals(table.getName())) {
				return table;
			}
		}
		return null;
	}

	private static class TableCache {
		volatile List<ITable> tables;
		ReadDatabaseMetaData job;
	}

	private static class ReadDatabaseMetaData extends Job {

		private final ConsoleConfiguration ccfg;
		private final TableCache cache;

		public ReadDatabaseMetaData(IProject project, ConsoleConfiguration ccfg, TableCache cache) {
			super(BasicHibernateMessages.HibernateNature_reading_database_metadata_for + project.getName());
			this.ccfg = ccfg;
			this.cache = cache;
		}

		protected IStatus run(IProgressMonitor monitor) {
			IConfiguration cfg = ccfg.buildWith(null, false);
			IService service = ccfg.getHibernateExtension().getHibernateService();
			final IConfiguration jcfg = service.newJDBCMetaDataConfiguration();
			jcfg.setProperties(cfg.getProperties());
			monitor.beginTask(BasicHibernateMessages.HibernateNature_reading_database_metadata, IProgressMonitor.UNKNOWN);
			try {
				ccfg.execute(new Command() {
					public Object execute() {
						jcfg.readFromJDBC();
						return null;
					}
				});

				List<ITable> result = new ArrayList<ITable>();
				Iterator<ITable> tabs = jcfg.getTableMappings();
				while (tabs.hasNext()) {
					ITable table = tabs.next();
					monitor.subTask(table.getName());
					result.add(table);
				}

				cache.tables = result;
				monitor.done();
				return Status.OK_STATUS;
			} catch (Throwable t) {
				return new Status(IStatus.ERROR, HibernateConsoleCorePlugin.ID, 1,
					BasicHibernateMessages.HibernateNature_error_while_performing_background_reading_of_database_schema, t);
			}
		}
	}

}
