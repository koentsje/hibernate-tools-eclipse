package org.hibernate.tool.eclipse.orm.base.core.test;

import java.io.File;
import java.io.FileWriter;
import java.util.Observable;
import java.util.Observer;

import org.hibernate.tool.eclipse.orm.console.core.ConsoleConfiguration;
import org.hibernate.tool.eclipse.orm.console.core.KnownConfigurations;
import org.hibernate.tool.eclipse.orm.query.QueryParameter;
import org.hibernate.tool.eclipse.orm.query.QueryInputModel;
import org.hibernate.tool.eclipse.orm.console.core.HibernateExtension;
import org.hibernate.tool.eclipse.orm.base.core.test.utils.TestConsoleConfigurationPreferences;
import org.hibernate.tool.eclipse.orm.runtime.spi.IService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class QueryParametersTest {
	
	private static final String HIBERNATE_CFG_XML = 
		"<!DOCTYPE hibernate-configuration PUBLIC                               " +
		"	'-//Hibernate/Hibernate Configuration DTD 3.0//EN'                  " +
		"	'http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd'> " +
		"                                                                       " +
		"<hibernate-configuration>                                              " +
		"	<session-factory/>                                                  " + 
		"</hibernate-configuration>                                             " ;		
	
	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();
	
	private File cfgXmlFile = null;
	private ConsoleConfiguration consoleCfg;
	private IService service;

	@Before
	public void setUp() throws Exception {
		ConsoleConfiguration.setRuntimeManagerFactory(
			prefs -> new HibernateExtension(prefs));
		cfgXmlFile = new File(temporaryFolder.getRoot(), "hibernate.cfg.xml");
		FileWriter fw = new FileWriter(cfgXmlFile);
		fw.write(HIBERNATE_CFG_XML);
		fw.close();
		TestConsoleConfigurationPreferences cfgprefs = 
				new TestConsoleConfigurationPreferences(cfgXmlFile);
		consoleCfg = new ConsoleConfiguration(cfgprefs);
		KnownConfigurations.getInstance().addConfiguration(consoleCfg, true);
		service = consoleCfg.getRuntimeManager().getHibernateService();
	}
	
	@After
	public void tearDown() throws Exception {
		KnownConfigurations.getInstance().removeAllConfigurations();
		consoleCfg = null;
		cfgXmlFile = null;
	}

	@Test
	public void testQueryParameter() {
		QueryInputModel model = new QueryInputModel(service);
		
		QueryParameter[] cqps = model.getQueryParameters();
		Assert.assertNotNull(cqps);
		
		QueryInputModel qpmodel = model;
		Assert.assertNotNull(qpmodel);
		
		class TestObserver implements Observer {
			int cnt = 0;
			public void update(Observable o, Object arg) {
				cnt++;			
			}			
		};
		
		TestObserver testObserver = new TestObserver();
		qpmodel.addObserver(testObserver);
		QueryParameter consoleQueryParameter = new QueryParameter(service);
		qpmodel.addParameter(consoleQueryParameter);
		Assert.assertEquals(1,testObserver.cnt);
		
		qpmodel.removeParameter(consoleQueryParameter);
		Assert.assertEquals(2,testObserver.cnt);
	}
	
	@Test
	public void testCreateUnique() {
		
		QueryInputModel model = new QueryInputModel(service);
		
		QueryParameter parameter = model.createUniqueParameter("param"); //$NON-NLS-1$
		model.addParameter(parameter);
		
		Assert.assertFalse(model.createUniqueParameter("param").getName().equals(parameter.getName())); //$NON-NLS-1$
	}

}
