package org.hibernate.tool.eclipse.orm.base.core.test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.tool.eclipse.orm.console.core.ConsoleConfiguration;
import org.hibernate.tool.eclipse.orm.console.core.KnownConfigurations;
import org.hibernate.tool.eclipse.orm.console.core.KnownConfigurationsAdapter;
import org.hibernate.tool.eclipse.orm.console.core.preferences.ConsoleConfigurationPreferences;
import org.hibernate.tool.eclipse.orm.base.core.test.nls.Messages;
import org.hibernate.tool.eclipse.orm.runtime.spi.ISessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;

public class KnownConfigurationsTest {

	static class CCListener extends KnownConfigurationsAdapter {
		List<ConsoleConfiguration> added = new ArrayList<ConsoleConfiguration>();
		public void configurationAdded(ConsoleConfiguration root) {
			added.add(root);
		}
		public void sessionFactoryBuilt(ConsoleConfiguration ccfg, ISessionFactory builtFactory) {
			Assert.fail(Messages.KnownConfigurationsTest_no_sf_should_be_build);
		}
		public void sessionFactoryClosing(ConsoleConfiguration configuration, ISessionFactory closingFactory) {
			Assert.fail(Messages.KnownConfigurationsTest_no_sf_should_be_closed);
		}
		public void configurationRemoved(ConsoleConfiguration root, boolean forUpdate) {
			if(!added.remove(root)) {
				Assert.fail(Messages.KnownConfigurationsTest_trying_remove_non_existing_console);
			}
		}
	}

	@Test
	public void testKnownConfigurations() {

		KnownConfigurations knownConfigurations = KnownConfigurations.getInstance();
		ConsoleConfiguration[] configurations = knownConfigurations.getConfigurations();

		Assert.assertEquals(0, configurations.length);


		CCListener listener = new CCListener();
		try {
		knownConfigurations.addConsoleConfigurationListener(listener);

		Assert.assertEquals(0, listener.added.size());

		ConsoleConfigurationPreferences preferences = new ConsoleConfigurationPreferences() {
			public void setName(String name) {}
			public void readStateFrom(Element element) {}
			public void writeStateTo(Element node) {}
			public File getPropertyFile() {return null;}
			public File getConfigXMLFile() {return null;}
			public Properties getProperties() {return null;}
			public File[] getMappingFiles() {return null;}
			public URL[] getCustomClassPathURLS() {return null;}
			public String getName() {
				return Messages.KnownConfigurationsTest_fake_prefs;
			}
			public String getEntityResolverName() {return null;}
			public ConfigurationMode getConfigurationMode() {return null;}
			public String getNamingStrategy() {return null;}
			public String getPersistenceUnitName() {return null;}
			public String getConnectionProfileName() {return null;}
			public String getDialectName() {return null;}
			public String getHibernateVersion() {return null;}
		};

		ConsoleConfigurationPreferences preferences2 = new ConsoleConfigurationPreferences() {
			String name = Messages.KnownConfigurationsTest_new_test;
			public void setName(String name) {this.name = name;}
			public void readStateFrom(Element element) {}
			public void writeStateTo(Element node) {}
			public File getPropertyFile() {return null;}
			public File getConfigXMLFile() {return null;}
			public Properties getProperties() {return null;}
			public File[] getMappingFiles() {return null;}
			public URL[] getCustomClassPathURLS() {return null;}
			public String getName() {return name;}
			public String getEntityResolverName() {return null;}
			public ConfigurationMode getConfigurationMode() {return null;}
			public String getNamingStrategy() {return null;}
			public String getPersistenceUnitName() {return null;}
			public String getConnectionProfileName() {return null;}
			public String getDialectName() {return null;}
			public String getHibernateVersion() {return null;}
		};

		ConsoleConfiguration configuration = new ConsoleConfiguration(preferences);
		ConsoleConfiguration configuration2 = new ConsoleConfiguration(preferences2);

		knownConfigurations.addConfiguration(configuration, false);
		knownConfigurations.addConfiguration(configuration2, false);

		configurations = knownConfigurations.getConfigurations();
		Assert.assertEquals(2,configurations.length);
		Assert.assertEquals(listener.added.size(), 0);

		knownConfigurations.addConfiguration(configuration, true);
		knownConfigurations.addConfiguration(configuration2, true);

		configurations = knownConfigurations.getConfigurations();
		Assert.assertEquals(2,configurations.length);
		Assert.assertEquals(listener.added.size(), 2);

		knownConfigurations.removeConfiguration(configuration, false);
		knownConfigurations.removeConfiguration(configuration2, false);

		configurations = knownConfigurations.getConfigurations();
		Assert.assertEquals(0,configurations.length);
		Assert.assertEquals(listener.added.size(), 0);
		} finally {
			KnownConfigurations.getInstance().removeConfigurationListener(listener);
		}
	}

}
