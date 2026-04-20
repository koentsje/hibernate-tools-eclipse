package org.hibernate.tool.eclipse.orm.console.core;

import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

import org.eclipse.osgi.util.NLS;
import org.hibernate.tool.eclipse.orm.console.core.config.ConfigurationFactory;
import org.hibernate.tool.eclipse.orm.console.core.config.ConsoleConfigClassLoader;
import org.hibernate.tool.eclipse.orm.console.core.ConsoleMessages;
import org.hibernate.tool.eclipse.orm.console.core.config.FakeDelegatingDriver;
import org.hibernate.tool.eclipse.orm.console.core.execution.DefaultExecutionContext;
import org.hibernate.tool.eclipse.orm.console.core.execution.ExecutionContext;
import org.hibernate.tool.eclipse.orm.console.core.preferences.ConsoleConfigurationPreferences;
import org.hibernate.tool.eclipse.orm.console.core.preferences.PreferencesClassPathUtils;
import org.hibernate.tool.eclipse.common.runtime.HibernateRuntimeException;
import org.hibernate.tool.eclipse.orm.runtime.spi.IConfiguration;
import org.hibernate.tool.eclipse.orm.runtime.spi.IRuntimeManager;
import org.hibernate.tool.eclipse.orm.runtime.spi.IService;
import org.hibernate.tool.eclipse.orm.runtime.spi.ISessionFactory;
import org.hibernate.tool.eclipse.orm.runtime.spi.RuntimeServiceManager;

public class RuntimeManager implements IRuntimeManager {

	private IConfiguration configuration;

	private ConsoleConfigClassLoader classLoader = null;

	private ExecutionContext executionContext;

	private ConsoleConfigurationPreferences prefs;

	private ISessionFactory sessionFactory;

	private Map<String, FakeDelegatingDriver> fakeDrivers = new HashMap<String, FakeDelegatingDriver>();

	public RuntimeManager(ConsoleConfigurationPreferences prefs) {
		this.prefs = prefs;
	}

	protected ConsoleConfigClassLoader createClassLoader(final URL[] customClassPathURLs) {
		ConsoleConfigClassLoader classLoader = AccessController.doPrivileged(new PrivilegedAction<ConsoleConfigClassLoader>() {
			public ConsoleConfigClassLoader run() {
				return new ConsoleConfigClassLoader(customClassPathURLs, getParentClassLoader()) {
					protected Class<?> findClass(String name) throws ClassNotFoundException {
						try {
							return super.findClass(name);
						} catch (ClassNotFoundException cnfe) {
							throw cnfe;
						} catch (IllegalStateException e){
							e.printStackTrace();
							throw e;
						}
					}
		
					protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
						try {
							return super.loadClass(name, resolve);
						} catch (ClassNotFoundException cnfe) {
							throw cnfe;
						}
					}
		
					public Class<?> loadClass(String name) throws ClassNotFoundException {
						try {
							return super.loadClass(name);
						} catch (ClassNotFoundException cnfe) {
							throw cnfe;
						}
					}
					
					public URL getResource(String name) {
					      return super.getResource(name);
					}
				};
			}
		});
		return classLoader;
	}
	
	private ClassLoader getParentClassLoader() {
		return getHibernateService().getClassLoader();
	}

	public String getHibernateVersion() {
		String result = prefs.getHibernateVersion();
		// return hibernate 3.5 by default
		// TODO do something smarter here
		return result != null ? result : "3.5"; //$NON-NLS-1$
	}

	public void build() {
		closeSessionFactory();
		reinitClassLoader();
		executionContext = new DefaultExecutionContext(prefs.getName(), classLoader);
		configuration = (IConfiguration)execute(() -> {
			ConfigurationFactory cf = new ConfigurationFactory(prefs, fakeDrivers);
			return cf.createConfiguration(null, true);
		});
	}

	public void buildSessionFactory() {
		execute(() -> {
			if (sessionFactory != null) {
				throw new HibernateRuntimeException("Factory was not closed before attempt to build a new Factory"); //$NON-NLS-1$
			}
			sessionFactory = configuration.buildSessionFactory();
			return null;
		});
	}

	public boolean closeSessionFactory() {
		boolean res = false;
		if (sessionFactory != null) {
			sessionFactory.close();
			sessionFactory = null;
			res = true;
		}
		return res;
	}

	public IConfiguration buildWith(final IConfiguration cfg, final boolean includeMappings) {
		if (executionContext == null) {
			reinitClassLoader();
			executionContext = new DefaultExecutionContext(prefs.getName(), classLoader);
		}
		return (IConfiguration)execute(() -> {
			ConfigurationFactory cf = new ConfigurationFactory(prefs, fakeDrivers);
			return cf.createConfiguration(cfg, includeMappings);
		});
	}
	
	/**
	 * Create class loader - so it uses the original urls list from preferences. 
	 */
	protected void reinitClassLoader() {
		//the class loader caches user's compiled classes
		//need to rebuild it on every console configuration rebuild to pick up latest versions.
		final URL[] customClassPathURLs = PreferencesClassPathUtils.getCustomClassPathURLs(prefs);
		cleanUpClassLoader();
		classLoader = createClassLoader(customClassPathURLs);
	}
	
	public String getName() {
		return prefs.getName();
	}
	
	public Object execute(Callable<Object> c) {
		if (executionContext != null) {
			return executionContext.execute(() -> {
				try {
					return c.call();
				} catch (RuntimeException e) {
					throw e;
				} catch (Exception e) {
					throw new HibernateRuntimeException(e);
				}
			});
		}
		final String msg = NLS.bind(ConsoleMessages.ConsoleConfiguration_null_execution_context, getName());
		throw new HibernateRuntimeException(msg);
	}

	public boolean reset() {
		boolean resetted = false;
		// reseting state
		if (configuration != null) {
			configuration = null;
			resetted = true;
		}
		
		resetted = resetted | closeSessionFactory() | cleanUpClassLoader();
		executionContext = null;
		return resetted;
	}
	
	protected boolean cleanUpClassLoader() {
		boolean resetted = false;
		if (executionContext != null) {
			executionContext.execute(() -> {
				Iterator<FakeDelegatingDriver> it = fakeDrivers.values().iterator();
				while (it.hasNext()) {
					try {
						DriverManager.deregisterDriver(it.next());
					} catch (SQLException e) {
						// ignore
					}
				}
				return null;
			});
		}
		if (fakeDrivers.size() > 0) {
			fakeDrivers.clear();
			resetted = true;
		}
		ClassLoader classLoaderTmp = classLoader;
		while (classLoaderTmp != null) {
			if (classLoaderTmp instanceof ConsoleConfigClassLoader) {
				((ConsoleConfigClassLoader)classLoaderTmp).close();
				resetted = true;
			}
			classLoaderTmp = classLoaderTmp.getParent();
		}
		if (classLoader != null) {
			classLoader = null;
			resetted = true;
		}
		return resetted;
	}

	public boolean hasConfiguration() {
		return configuration != null;
	}

	/**
	 * @return
	 */
	public IConfiguration getConfiguration() {
		return configuration;
	}
	
	public ISessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public boolean isSessionFactoryCreated() {
		return sessionFactory != null;
	}

	public URL findResource(String name) {
		return classLoader != null ? classLoader.findResource(name) : null;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}
	
	public void buildMappings() {
		execute(() -> {
			getConfiguration().buildMappings();
			return null;
		});
	}

	public String getConsoleConfigurationName() {
		return prefs.getName();
	}

	public IService getHibernateService() {
		return RuntimeServiceManager.getInstance().findService(getHibernateVersion());
	}

}
