package org.hibernate.tool.eclipse.xml.core.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

@SuppressWarnings("restriction")
public class DomHelperTest {

	private static final String CFG_XML =
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
		"<hibernate-configuration>\n" +
		"  <session-factory name=\"test\">\n" +
		"    <property name=\"hibernate.dialect\">org.hibernate.dialect.H2Dialect</property>\n" +
		"    <property name=\"hibernate.connection.url\">jdbc:h2:mem:test</property>\n" +
		"    <mapping resource=\"com/example/Foo.hbm.xml\"/>\n" +
		"  </session-factory>\n" +
		"</hibernate-configuration>";

	private IDOMModel model;
	private IDOMDocument document;

	@Before
	public void setUp() throws Exception {
		model = (IDOMModel) StructuredModelManager.getModelManager()
			.createUnManagedStructuredModelFor("org.eclipse.wst.xml.core.xmlsource");
		model.getStructuredDocument().set(CFG_XML);
		document = model.getDocument();
	}

	@After
	public void tearDown() {
		if (model != null) {
			model.releaseFromEdit();
		}
	}

	@Test
	public void testGetChildElements() {
		Element root = document.getDocumentElement();
		List<Element> factories = DomHelper.getChildElements(root, "session-factory");
		assertEquals(1, factories.size());
		assertEquals("session-factory", factories.get(0).getNodeName());
	}

	@Test
	public void testGetChildElementsReturnsEmptyForNull() {
		List<Element> result = DomHelper.getChildElements(null, "anything");
		assertTrue(result.isEmpty());
	}

	@Test
	public void testGetFirstChildElement() {
		Element root = document.getDocumentElement();
		Element sf = DomHelper.getFirstChildElement(root, "session-factory");
		assertNotNull(sf);
		assertEquals("test", sf.getAttribute("name"));
	}

	@Test
	public void testGetFirstChildElementReturnsNullWhenMissing() {
		Element root = document.getDocumentElement();
		assertNull(DomHelper.getFirstChildElement(root, "nonexistent"));
	}

	@Test
	public void testGetOrCreateElementExisting() {
		Element root = document.getDocumentElement();
		Element sf = DomHelper.getOrCreateElement(root, "session-factory");
		assertEquals("test", sf.getAttribute("name"));
	}

	@Test
	public void testGetOrCreateElementNew() {
		Element root = document.getDocumentElement();
		Element sf = DomHelper.getFirstChildElement(root, "session-factory");
		Element security = DomHelper.getOrCreateElement(sf, "security");
		assertNotNull(security);
		assertEquals("security", security.getNodeName());
		// calling again should return the same element
		Element same = DomHelper.getOrCreateElement(sf, "security");
		assertEquals(security, same);
	}

	@Test
	public void testGetAttributeValue() {
		Element root = document.getDocumentElement();
		Element sf = DomHelper.getFirstChildElement(root, "session-factory");
		assertEquals("test", DomHelper.getAttributeValue(sf, "name"));
		assertEquals("", DomHelper.getAttributeValue(sf, "nonexistent"));
		assertEquals("", DomHelper.getAttributeValue(null, "name"));
	}

	@Test
	public void testSetAttributeValue() {
		Element root = document.getDocumentElement();
		Element sf = DomHelper.getFirstChildElement(root, "session-factory");
		DomHelper.setAttributeValue(sf, "name", "changed");
		assertEquals("changed", sf.getAttribute("name"));
		DomHelper.setAttributeValue(sf, "name", "");
		assertEquals("", sf.getAttribute("name"));
	}

	@Test
	public void testAddAndRemoveElement() {
		Element root = document.getDocumentElement();
		Element sf = DomHelper.getFirstChildElement(root, "session-factory");
		Element added = DomHelper.addElement(sf, "listener");
		assertNotNull(added);
		assertEquals("listener", added.getNodeName());
		assertEquals(sf, added.getParentNode());
		DomHelper.removeElement(added);
		assertNull(added.getParentNode());
	}

	@Test
	public void testGetTextContent() {
		Element root = document.getDocumentElement();
		Element sf = DomHelper.getFirstChildElement(root, "session-factory");
		Element prop = DomHelper.findPropertyElement(sf, "hibernate.dialect");
		assertEquals("org.hibernate.dialect.H2Dialect", DomHelper.getTextContent(prop));
	}

	@Test
	public void testGetTextContentReturnsEmptyForNull() {
		assertEquals("", DomHelper.getTextContent(null));
	}

	@Test
	public void testSetTextContent() {
		Element root = document.getDocumentElement();
		Element sf = DomHelper.getFirstChildElement(root, "session-factory");
		Element prop = DomHelper.findPropertyElement(sf, "hibernate.dialect");
		DomHelper.setTextContent(prop, "org.hibernate.dialect.MySQLDialect");
		assertEquals("org.hibernate.dialect.MySQLDialect", DomHelper.getTextContent(prop));
	}

	@Test
	public void testSetTextContentEmpty() {
		Element root = document.getDocumentElement();
		Element sf = DomHelper.getFirstChildElement(root, "session-factory");
		Element prop = DomHelper.findPropertyElement(sf, "hibernate.dialect");
		DomHelper.setTextContent(prop, "");
		assertEquals("", DomHelper.getTextContent(prop));
	}

	@Test
	public void testFindPropertyElement() {
		Element root = document.getDocumentElement();
		Element sf = DomHelper.getFirstChildElement(root, "session-factory");
		assertNotNull(DomHelper.findPropertyElement(sf, "hibernate.dialect"));
		assertNotNull(DomHelper.findPropertyElement(sf, "hibernate.connection.url"));
		assertNull(DomHelper.findPropertyElement(sf, "nonexistent.property"));
	}

	@Test
	public void testGetPropertyValue() {
		Element root = document.getDocumentElement();
		Element sf = DomHelper.getFirstChildElement(root, "session-factory");
		assertEquals("org.hibernate.dialect.H2Dialect", DomHelper.getPropertyValue(sf, "hibernate.dialect"));
		assertEquals("jdbc:h2:mem:test", DomHelper.getPropertyValue(sf, "hibernate.connection.url"));
		assertEquals("", DomHelper.getPropertyValue(sf, "nonexistent"));
	}

	@Test
	public void testSetPropertyValueNew() {
		Element root = document.getDocumentElement();
		Element sf = DomHelper.getFirstChildElement(root, "session-factory");
		DomHelper.setPropertyValue(sf, "hibernate.show_sql", "true");
		assertEquals("true", DomHelper.getPropertyValue(sf, "hibernate.show_sql"));
	}

	@Test
	public void testSetPropertyValueUpdate() {
		Element root = document.getDocumentElement();
		Element sf = DomHelper.getFirstChildElement(root, "session-factory");
		DomHelper.setPropertyValue(sf, "hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		assertEquals("org.hibernate.dialect.PostgreSQLDialect", DomHelper.getPropertyValue(sf, "hibernate.dialect"));
	}

	@Test
	public void testSetPropertyValueRemove() {
		Element root = document.getDocumentElement();
		Element sf = DomHelper.getFirstChildElement(root, "session-factory");
		DomHelper.setPropertyValue(sf, "hibernate.dialect", "");
		assertEquals("", DomHelper.getPropertyValue(sf, "hibernate.dialect"));
		assertNull(DomHelper.findPropertyElement(sf, "hibernate.dialect"));
	}
}
