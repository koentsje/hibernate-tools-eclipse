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
package org.hibernate.console;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import org.hibernate.console.execution.ExecutionContext;
import org.hibernate.console.execution.ExecutionContext.Command;
import org.hibernate.console.execution.ExecutionContextHolder;
import org.hibernate.console.preferences.ConsoleConfigurationPreferences;
import org.hibernate.tool.eclipse.orm.query.HQLQueryPage;
import org.hibernate.tool.eclipse.orm.query.JavaPage;
import org.hibernate.tool.eclipse.orm.query.QueryHelper;
import org.hibernate.tool.eclipse.orm.query.QueryInputModel;
import org.hibernate.tool.eclipse.orm.query.QueryPage;
import org.hibernate.tool.eclipse.orm.runtime.spi.IConfiguration;
import org.hibernate.tool.eclipse.orm.runtime.spi.IEnvironment;
import org.hibernate.tool.eclipse.orm.runtime.spi.ISession;
import org.hibernate.tool.eclipse.orm.runtime.spi.ISessionFactory;

public class ConsoleConfiguration implements ExecutionContextHolder {

	//****************************** EXTENSION **********************
	private String hibernateVersion = "==<None>=="; //set to some unused value //$NON-NLS-1$

	private IHibernateExtension extension;

	private static java.util.function.Function<ConsoleConfigurationPreferences, IHibernateExtension> extensionFactory;

	public static void setHibernateExtensionFactory(
			java.util.function.Function<ConsoleConfigurationPreferences, IHibernateExtension> factory) {
		extensionFactory = factory;
	}

	protected IHibernateExtension createHibernateExtension(){
		if (extensionFactory != null) {
			return extensionFactory.apply(prefs);
		}
		throw new IllegalStateException(
			"No HibernateExtension factory registered. " + //$NON-NLS-1$
			"Ensure the plugin activator has called " + //$NON-NLS-1$
			"ConsoleConfiguration.setHibernateExtensionFactory()"); //$NON-NLS-1$
	}

	private void loadHibernateExtension(){
		extension = createHibernateExtension();
	}

	private void updateHibernateVersion(String hibernateVersion){
		if (!equals(this.hibernateVersion, hibernateVersion)){
			this.hibernateVersion = hibernateVersion;
			loadHibernateExtension();
		}
	}

    private boolean equals(String str1, String str2) {
        return (str1 == null ? str2 == null : str1.equals(str2) );
    }

    public IHibernateExtension getHibernateExtension(){
		updateHibernateVersion(prefs.getHibernateVersion());//reinit if necessary
		return this.extension;
	}

	//****************************** EXTENSION **********************

	public ConsoleConfiguration(ConsoleConfigurationPreferences config) {
		prefs = config;
	}

	/** Unique name for this configuration */
	public String getName() {
		return prefs.getName();
	}

	public synchronized Object execute(Command c) {
		return getHibernateExtension().execute(c);
	}

	public ConsoleConfigurationPreferences prefs = null;

	/**
	 * Reset configuration, session factory, class loader and execution context.
	 *
	 */
	public synchronized boolean reset() {
		boolean resetted = false;
		if (getHibernateExtension() != null ) {
			resetted = getHibernateExtension().reset();
		}
		if (resetted) {
			fireConfigurationReset();
		}
		return resetted;
	}

	public void build() {
		reset();
		getHibernateExtension().build();
		fireConfigurationBuilt();
	}

	/**
	 * @return
	 *
	 */
	public IConfiguration buildWith(final IConfiguration cfg, final boolean includeMappings) {
		return getHibernateExtension().buildWith(cfg, includeMappings);
	}

	public IConfiguration getConfiguration() {
		return getHibernateExtension().getConfiguration();
	}

	/**
	 * @return
	 */
	public boolean hasConfiguration() {
		return getHibernateExtension().hasConfiguration();
	}

	public void buildMappings(){
		getHibernateExtension().buildMappings();
	}

	public void buildSessionFactory() {
		if (isSessionFactoryCreated()) {
			throw new HibernateConsoleRuntimeException(ConsoleMessages.ConsoleConfiguration_factory_not_closed_before_build_new_factory);
		}
		getHibernateExtension().buildSessionFactory();
		fireFactoryBuilt();
	}

	public ISessionFactory getSessionFactory() {
		return getHibernateExtension().getSessionFactory();
	}


	int execcount;
	ArrayList<ConsoleConfigurationListener> consoleCfgListeners = new ArrayList<ConsoleConfigurationListener>();

	public QueryPage executeHQLQuery(final String hql) {
		return executeHQLQuery(hql, new QueryInputModel(getHibernateExtension().getHibernateService()));
	}

	public QueryPage executeHQLQuery(final String hql, final QueryInputModel queryParameters) {
		QueryPage qp = (QueryPage)execute(new Command() {
			public Object execute() {
				ISession session = getSessionFactory().openSession();
				QueryPage qp = new HQLQueryPage(
						getHibernateExtension().getHibernateService(),
						getName(), hql, queryParameters);
				qp.setSession(session);
				return qp;
			}
		});
		qp.setId(++execcount);
		fireQueryPageCreated(qp);
		return qp;
	}

	public QueryPage executeBSHQuery(final String queryString, final QueryInputModel model) {
		QueryPage qp = (QueryPage)execute(new Command() {
			public Object execute() {
				ISession session = getSessionFactory().openSession();
				QueryPage qp = new JavaPage(getName(), queryString, model);
				qp.setSession(session);
				return qp;
			}
		});
		qp.setId(++execcount);
		fireQueryPageCreated(qp);
		return qp;
	}

	public String generateSQL(final String query) {
		return (String) execute(new Command() {
			public Object execute() {
				return QueryHelper.generateSQL(getSessionFactory(), query, getHibernateExtension().getHibernateService());
			}
		});
	}

	@SuppressWarnings("unchecked")
	// clone listeners to thread safe iterate over array
	private ArrayList<ConsoleConfigurationListener> cloneConsoleCfgListeners() {
		return (ArrayList<ConsoleConfigurationListener>)consoleCfgListeners.clone();
	}

	private void fireConfigurationBuilt() {
		for (ConsoleConfigurationListener view : cloneConsoleCfgListeners()) {
			view.configurationBuilt(this);
		}
	}

	private void fireConfigurationReset() {
		for (ConsoleConfigurationListener view : cloneConsoleCfgListeners()) {
			view.configurationReset(this);
		}
	}

	private void fireQueryPageCreated(QueryPage qp) {
		for (ConsoleConfigurationListener view : cloneConsoleCfgListeners()) {
			view.queryPageCreated(qp);
		}
	}

	private void fireFactoryBuilt() {
		for (ConsoleConfigurationListener view : cloneConsoleCfgListeners()) {
			view.sessionFactoryBuilt(this, getSessionFactory());
		}
	}

	private void fireFactoryClosing(ISessionFactory closingFactory) {
		for (ConsoleConfigurationListener view : cloneConsoleCfgListeners()) {
			view.sessionFactoryClosing(this, closingFactory);
		}
	}

	public void addConsoleConfigurationListener(ConsoleConfigurationListener v) {
		consoleCfgListeners.add(v);
	}

	public void removeConsoleConfigurationListener(ConsoleConfigurationListener sfListener) {
		consoleCfgListeners.remove(sfListener);
	}

	public ConsoleConfigurationListener[] getConsoleConfigurationListeners() {
		return consoleCfgListeners.toArray(new ConsoleConfigurationListener[consoleCfgListeners.size()]);
	}


	public boolean isSessionFactoryCreated() {
		return getHibernateExtension().isSessionFactoryCreated();
	}

	public ConsoleConfigurationPreferences getPreferences() {
		return prefs;
	}

	public File getConfigXMLFile() {
		IEnvironment environment = getHibernateExtension().getHibernateService().getEnvironment();
		File configXMLFile = null;
		if (prefs != null) {
			configXMLFile = prefs.getConfigXMLFile();
		}
		if (configXMLFile == null) {
			URL url = getHibernateExtension().findResource("hibernate.cfg.xml"); //$NON-NLS-1$
			if (url != null) {
				URI uri = null;
				try {
					uri = url.toURI();
					configXMLFile = new File(uri);
				} catch (URISyntaxException e) {
					// ignore
				}
			}
		}
		if (configXMLFile == null) {
			URL url = environment.getWrappedClass().getClassLoader().getResource("hibernate.cfg.xml"); //$NON-NLS-1$
			if (url != null) {
				URI uri = null;
				try {
					uri = url.toURI();
					configXMLFile = new File(uri);
				} catch (URISyntaxException e) {
					// ignore
				}
			}
		}
		return configXMLFile;
	}

	public String toString() {
		return getClass().getName() + ":" + getName(); //$NON-NLS-1$
	}

	public ExecutionContext getExecutionContext() {
		return new ExecutionContext() {
			public void installLoader() { }
			public Object execute(Command c) { return ConsoleConfiguration.this.execute(c); }
			public void uninstallLoader() { }
		};
	}

	public boolean closeSessionFactory() {
		ISessionFactory sf = getSessionFactory();
		if (sf != null) {
			fireFactoryClosing(sf);
		}
		return getHibernateExtension().closeSessionFactory();
	}

}
