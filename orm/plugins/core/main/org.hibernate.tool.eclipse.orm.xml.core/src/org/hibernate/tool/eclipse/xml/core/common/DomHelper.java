package org.hibernate.tool.eclipse.xml.core.common;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomHelper {

	public static List<Element> getChildElements(Element parent, String tagName) {
		List<Element> result = new ArrayList<>();
		if (parent == null) return result;
		NodeList children = parent.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element && tagName.equals(child.getNodeName())) {
				result.add((Element) child);
			}
		}
		return result;
	}

	public static Element getFirstChildElement(Element parent, String tagName) {
		List<Element> children = getChildElements(parent, tagName);
		return children.isEmpty() ? null : children.get(0);
	}

	public static Element getOrCreateElement(Element parent, String tagName) {
		Element child = getFirstChildElement(parent, tagName);
		if (child == null) {
			child = parent.getOwnerDocument().createElement(tagName);
			parent.appendChild(child);
		}
		return child;
	}

	public static String getAttributeValue(Element element, String attr) {
		if (element == null) return "";
		String value = element.getAttribute(attr);
		return value != null ? value : "";
	}

	public static void setAttributeValue(Element element, String attr, String value) {
		if (value == null || value.isEmpty()) {
			element.removeAttribute(attr);
		} else {
			element.setAttribute(attr, value);
		}
	}

	public static Element addElement(Element parent, String tagName) {
		Element child = parent.getOwnerDocument().createElement(tagName);
		parent.appendChild(child);
		return child;
	}

	public static void removeElement(Element element) {
		if (element != null && element.getParentNode() != null) {
			element.getParentNode().removeChild(element);
		}
	}

	public static String getTextContent(Element element) {
		if (element == null) return "";
		// Read text from child nodes (WTP's DOM may not support Element.getTextContent)
		StringBuilder sb = new StringBuilder();
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == Node.CDATA_SECTION_NODE) {
				sb.append(child.getNodeValue());
			}
		}
		return sb.toString().trim();
	}

	public static void setTextContent(Element element, String text) {
		// Remove existing child nodes (WTP's DOM does not support Element.setTextContent)
		while (element.getFirstChild() != null) {
			element.removeChild(element.getFirstChild());
		}
		if (text != null && !text.isEmpty()) {
			element.appendChild(element.getOwnerDocument().createTextNode(text));
		}
	}

	public static Element findPropertyElement(Element sessionFactory, String propertyName) {
		for (Element prop : getChildElements(sessionFactory, "property")) {
			if (propertyName.equals(prop.getAttribute("name"))) {
				return prop;
			}
		}
		return null;
	}

	public static String getPropertyValue(Element sessionFactory, String propertyName) {
		Element prop = findPropertyElement(sessionFactory, propertyName);
		return prop != null ? getTextContent(prop) : "";
	}

	public static void setPropertyValue(Element sessionFactory, String propertyName, String value) {
		Element prop = findPropertyElement(sessionFactory, propertyName);
		if (value == null || value.isEmpty()) {
			if (prop != null) {
				removeElement(prop);
			}
		} else {
			if (prop == null) {
				prop = addElement(sessionFactory, "property");
				prop.setAttribute("name", propertyName);
			}
			setTextContent(prop, value);
		}
	}
}
