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
package org.hibernate.tool.eclipse.orm.console.core;

import java.io.File;
import java.net.URI;

import org.hibernate.tool.eclipse.orm.console.core.nls.Messages;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.hibernate.tool.eclipse.orm.console.core.execution.ExecutionContext;
import org.hibernate.tool.eclipse.orm.console.core.execution.ExecutionContextHolder;
import org.hibernate.tool.eclipse.orm.console.core.preferences.ConsoleConfigurationPreferences;
import org.hibernate.tool.eclipse.orm.query.IQueryPage;
import org.hibernate.tool.eclipse.orm.runtime.spi.IConfiguration;
import org.hibernate.tool.eclipse.orm.runtime.spi.IEnvironment;
import org.hibernate.tool.eclipse.orm.runtime.spi.IRuntimeManager;
import org.hibernate.tool.eclipse.orm.runtime.spi.ISessionFactory;

public class ConsoleConfiguration implements ExecutionContextHolder {

	//****************************** RUNTIME MANAGER **********************
	private String hibernateVersion = "==<None>=="; //set to some unused value //$NON-NLS-1$

	private IRuntimeManager runtimeManager;

	private static java.util.function.Function<ConsoleConfigurationPreferences, IRuntimeManager> runtimeManagerFactory;

	public static void setRuntimeManagerFactory(
			java.util.function.Function<ConsoleConfigurationPreferences, IRuntimeManager> factory) {
		runtimeManagerFactory = factory;
	}

	protected IRuntimeManager createRuntimeManager(){
		if (runtimeManagerFactory != null) {
			return runtimeManagerFactory.apply(prefs);
		}
		throw new IllegalStateException(
			"No RuntimeManager factory registered. " + //$NON-NLS-1$
			"Ensure the plugin activator has called " + //$NON-NLS-1$
			"ConsoleConfiguration.setRuntimeManagerFactory()"); //$NON-NLS-1$
	}

	private void loadRuntimeManager(){
		runtimeManager = createRuntimeManager();
	}

	private void updateHibernateVersion(String hibernateVersion){
		if (!equals(this.hibernateVersion, hibernateVersion)){
			this.hibernateVersion = hibernateVersion;
			loadRuntimeManager();
		}
	}

    private boolean equals(String str1, String str2) {
        return (str1 == null ? str2 == null : str1.equals(str2) );
    }

    public IRuntimeManager getRuntimeManager(){
		updateHibernateVersion(prefs.getHibernateVersion());//reinit if necessary
		return this.runtimeManager;
	}

	//****************************** RUNTIME MANAGER **********************

	public ConsoleConfiguration(ConsoleConfigurationPreferences config) {
		prefs = config;
	}

	/** Unique name for this configuration */
	public String getName() {
		return prefs.getName();
	}

	public synchronized Object execute(Callable<Object> c) {
		return getRuntimeManager().execute(c);
	}

	public ConsoleConfigurationPreferences prefs = null;

	/**
	 * Reset configuration, session factory, class loader and execution context.
	 *
	 */
	public synchronized boolean reset() {
		boolean resetted = false;
		if (getRuntimeManager() != null ) {
			resetted = getRuntimeManager().reset();
		}
		if (resetted) {
			fireConfigurationReset();
		}
		return resetted;
	}

	public void build() {
		reset();
		getRuntimeManager().build();
		fireConfigurationBuilt();
	}

	/**
	 * @return
	 *
	 */
	public IConfiguration buildWith(final IConfiguration cfg, final boolean includeMappings) {
		return getRuntimeManager().buildWith(cfg, includeMappings);
	}

	public IConfiguration getConfiguration() {
		return getRuntimeManager().getConfiguration();
	}

	/**
	 * @return
	 */
	public boolean hasConfiguration() {
		return getRuntimeManager().hasConfiguration();
	}

	public void buildMappings(){
		getRuntimeManager().buildMappings();
	}

	public void buildSessionFactory() {
		if (isSessionFactoryCreated()) {
			throw new HibernateConsoleRuntimeException(Messages.ConsoleConfiguration_factory_not_closed_before_build_new_factory);
		}
		getRuntimeManager().buildSessionFactory();
		fireFactoryBuilt();
	}

	public ISessionFactory getSessionFactory() {
		return getRuntimeManager().getSessionFactory();
	}


	ArrayList<ConsoleConfigurationListener> consoleCfgListeners = new ArrayList<ConsoleConfigurationListener>();

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

	public void fireQueryPageCreated(IQueryPage qp) {
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
		return getRuntimeManager().isSessionFactoryCreated();
	}

	public ConsoleConfigurationPreferences getPreferences() {
		return prefs;
	}

	public File getConfigXMLFile() {
		IEnvironment environment = getRuntimeManager().getHibernateService().getEnvironment();
		File configXMLFile = null;
		if (prefs != null) {
			configXMLFile = prefs.getConfigXMLFile();
		}
		if (configXMLFile == null) {
			URL url = getRuntimeManager().findResource("hibernate.cfg.xml"); //$NON-NLS-1$
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
			public Object execute(ExecutionContext.Command c) { return ConsoleConfiguration.this.execute(() -> c.execute()); }
			public void uninstallLoader() { }
		};
	}

	public boolean closeSessionFactory() {
		ISessionFactory sf = getSessionFactory();
		if (sf != null) {
			fireFactoryClosing(sf);
		}
		return getRuntimeManager().closeSessionFactory();
	}

}
