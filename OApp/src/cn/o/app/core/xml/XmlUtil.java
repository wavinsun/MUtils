package cn.o.app.core.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cn.o.app.core.runtime.BeanField;
import cn.o.app.core.runtime.ReflectUtil;

/**
 * XML serializer of framework
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class XmlUtil {

	/** XML node attribute tag for entity property name */
	public static final String TAG_NAME = "name";
	/** XML node attribute tag for entity property of string */
	public static final String TAG_STRING = "string";
	/** XML node attribute tag for entity property of integer */
	public static final String TAG_INT = "int";
	/** XML node attribute tag for entity property of long */
	public static final String TAG_LONG = "long";
	/** XML node attribute tag for entity property of double */
	public static final String TAG_DOUBLE = "double";
	/** XML node attribute tag for entity property of boolean */
	public static final String TAG_BOOL = "bool";
	/** XML node attribute tag for entity property of enumeration */
	public static final String TAG_ENUM = "enum";
	/** XML node tag for entity property null */
	public static final String TAG_NULL = "null";

	public static Document parse(String file) throws Exception {
		return parse(new File(file));
	}

	public static Document parse(File file) throws Exception {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
	}

	public static Document parse(InputStream is) throws Exception {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
	}

	public static void save(Document doc, OutputStream os) throws Exception {
		TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(os));
	}

	public static void save(Document doc, String file) throws Exception {
		save(doc, new File(file));
	}

	public static void save(Document doc, File file) throws Exception {
		TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(file));
	}

	protected static Node getChildNode(Node node, String childAttrName) {
		NodeList nodes = node.getChildNodes();
		if (nodes == null) {
			return null;
		}
		for (int i = nodes.getLength() - 1; i >= 0; i--) {
			Node sub = nodes.item(i);
			if (sub.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			String attr = getAttr(sub, TAG_NAME);
			if (attr == null) {
				continue;
			}
			if (attr.equals(childAttrName)) {
				return sub;
			}
		}
		return null;
	}

	public static String getAttr(Node node, String name) {
		Node attr = node.getAttributes().getNamedItem(name);
		return attr == null ? null : attr.getNodeValue();
	}

	public static void setAttr(Node node, String name, String value) {
		NamedNodeMap attrs = node.getAttributes();
		Node attr = attrs.getNamedItem(name);
		if (attr == null) {
			Document doc = node.getOwnerDocument();
			attr = doc.createAttribute(name);
			attrs.setNamedItem(attr);
		}
		attr.setNodeValue(value);
	}

	public static NodeList selectNodes(Node node, String xPath) throws Exception {
		return (NodeList) XPathFactory.newInstance().newXPath().compile(xPath).evaluate(node, XPathConstants.NODESET);
	}

	public static Node newNode(Document doc, BeanField field) {
		Node node = doc.createElement(field.getRawType().getSimpleName());
		XmlUtil.setAttr(node, XmlUtil.TAG_NAME, field.getName());
		return node;
	}

	public static Document newDoc() throws Exception {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
	}

	public static Document newDoc(String xml) throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		try {
			return parse(bis);
		} catch (Exception e) {
			throw e;
		} finally {
			bis.close();
		}
	}

	public static String toString(Node node) throws Exception {
		return toString(node, true);
	}

	public static String toString(Node node, boolean indent) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			if (indent) {
				t.setOutputProperty(OutputKeys.INDENT, "yes");
				t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			}
			t.transform(new DOMSource(node), new StreamResult(bos));
			return bos.toString("UTF-8");
		} catch (Exception e) {
			throw e;
		} finally {
			bos.close();
		}
	}

	public static <T> T convertFromNode(Node node, Class<T> targetClass) throws Exception {
		return convertFromNode(node, targetClass, null);
	}

	protected static <T> T convertFromNode(Node node, Class<T> targetClass, Type genericType) throws Exception {
		if (TAG_NULL.equals(node.getNodeName())) {
			return null;
		}
		if (targetClass == String.class) {
			return (T) node.getTextContent();
		} else if (targetClass == Integer.TYPE || targetClass == Integer.class) {
			return (T) Integer.valueOf(node.getTextContent());
		} else if (targetClass == Long.TYPE || targetClass == Long.class) {
			return (T) Long.valueOf(node.getTextContent());
		} else if (targetClass == Double.TYPE || targetClass == Double.class) {
			return (T) Double.valueOf(node.getTextContent());
		} else if (targetClass == Boolean.TYPE || targetClass == Boolean.class) {
			return (T) Boolean.valueOf(node.getTextContent());
		} else if (Enum.class.isAssignableFrom(targetClass)) {
			return (T) ReflectUtil.valueOfEnum(targetClass, node.getTextContent());
		}
		return convertFromNode(node, targetClass.newInstance(), genericType);
	}

	protected static <T> T convertFromNode(Node node, T target, Type genericType) throws Exception {
		if (TAG_NULL.equals(node.getNodeName())) {
			return null;
		}
		if (target instanceof String) {
			return (T) node.getTextContent();
		} else if (target instanceof Integer) {
			return (T) Integer.valueOf(node.getTextContent());
		} else if (target instanceof Long) {
			return (T) Long.valueOf(node.getTextContent());
		} else if (target instanceof Double) {
			return (T) Double.valueOf(node.getTextContent());
		} else if (target instanceof Boolean) {
			return (T) Boolean.valueOf(node.getTextContent());
		} else if (target instanceof Enum) {
			return (T) ReflectUtil.valueOfEnum(target.getClass(), node.getTextContent());
		} else if (target instanceof Collection) {
			return (T) convertCollectionFromNode(node, (Collection<?>) target, genericType);
		} else if (target instanceof Map) {
			return (T) convertMapFromNode(node, (Map<?, ?>) target, genericType);
		} else if (target instanceof IXmlItem) {
			return (T) ((IXmlItem) target).fromXml(node, null);
		}
		for (BeanField f : BeanField.getFields(target.getClass())) {
			Node sub = getChildNode(node, f.getName());
			if (sub == null) {
				continue;
			}
			Class<?> fClass = f.getRawType(genericType);
			f.set(target, IXmlItem.class.isAssignableFrom(fClass) ? (((IXmlItem) fClass.newInstance()).fromXml(sub, f))
					: convertFromNode(sub, fClass, f.getGenericType(genericType)));
		}
		return target;
	}

	protected static <T extends Collection> T convertCollectionFromNode(Node node, T target, Type genericType)
			throws Exception {
		Class<?> targetClass = target.getClass();
		Class<?> eClass = ReflectUtil.getCollectionElementRawType(targetClass, genericType);
		Type eType = ReflectUtil.getCollectionElementGenericType(targetClass, genericType);
		NodeList childNodes = node.getChildNodes();
		for (int i = 0, size = childNodes.getLength(); i < size; i++) {
			Node sub = childNodes.item(i);
			if (sub.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			target.add(convertFromNode(sub, eClass, eType));
		}
		return target;
	}

	protected static <T extends Map> T convertMapFromNode(Node node, T target, Type genericType) throws Exception {
		Class<?> targetClass = target.getClass();
		Class<?> keyClass = ReflectUtil.getMapKeyRawType(targetClass, genericType);
		if (!String.class.isAssignableFrom(keyClass)) {
			throw new Exception();
		}
		Class<?> vClass = ReflectUtil.getMapValueRawType(targetClass, genericType);
		Type vType = ReflectUtil.getMapValueGenericType(targetClass, genericType);
		NodeList childNodes = node.getChildNodes();
		for (int i = childNodes.getLength() - 1; i >= 0; i--) {
			Node sub = childNodes.item(i);
			if (sub.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			target.put(getAttr(sub, TAG_NAME), convertFromNode(sub, vClass, vType));
		}
		return target;
	}

	public static <T> T convertFromDoc(Document doc, Class<T> targetClass) throws Exception {
		return convertFromNode(doc.getDocumentElement(), targetClass, null);
	}

	public static <T> T convert(String str, Class<T> targetClass) throws Exception {
		if (str == null) {
			return null;
		}
		return convertFromNode(newDoc(str).getDocumentElement(), targetClass, null);
	}

	public static <T> T convert(String str, T target) throws Exception {
		return convertFromNode(newDoc(str).getDocumentElement(), target, null);
	}

	public static <T> String convert(T target) throws Exception {
		return toString(convertToDoc(target), false);
	}

	public static <T> Document convertToDoc(T target) throws Exception {
		Document doc = newDoc();
		doc.appendChild(convertToNode(target, doc));
		return doc;
	}

	protected static <T> Node convertToNode(T target, Document doc) throws Exception {
		if (target == null) {
			return doc.createElement(TAG_NULL);
		} else if (target instanceof String) {
			Node node = doc.createElement(TAG_STRING);
			node.setTextContent((String) target);
			return node;
		} else if (target instanceof Integer) {
			Node node = doc.createElement(TAG_INT);
			node.setTextContent(target.toString());
			return node;
		} else if (target instanceof Long) {
			Node node = doc.createElement(TAG_LONG);
			node.setTextContent(target.toString());
			return node;
		} else if (target instanceof Double) {
			Node node = doc.createElement(TAG_DOUBLE);
			node.setTextContent(target.toString());
			return node;
		} else if (target instanceof Boolean) {
			Node node = doc.createElement(TAG_BOOL);
			node.setTextContent(target.toString());
			return node;
		} else if (target instanceof Enum) {
			Node node = doc.createElement(TAG_ENUM);
			node.setTextContent(target.toString());
			return node;
		} else if (target instanceof Collection) {
			return convertCollectionToNode(target, doc);
		} else if (target instanceof Map) {
			return convertMapToNode(target, doc);
		} else if (target instanceof IXmlItem) {
			return ((IXmlItem) target).toXml(doc, null);
		}
		Class<?> targetClass = target.getClass();
		Node node = doc.createElement(targetClass.getSimpleName());
		for (BeanField f : BeanField.getFields(targetClass)) {
			Object v = f.get(target);
			if (v == null) {
				continue;
			}
			Node subNode = (v instanceof IXmlItem) ? (((IXmlItem) v).toXml(doc, f)) : convertToNode(v, doc);
			setAttr(subNode, TAG_NAME, f.getName());
			node.appendChild(subNode);
		}
		return node;
	}

	protected static <T> Node convertCollectionToNode(T target, Document doc) throws Exception {
		Node node = doc.createElement(target.getClass().getSimpleName());
		for (Object element : (Collection<?>) target) {
			node.appendChild(convertToNode(element, doc));
		}
		return node;
	}

	protected static <T> Node convertMapToNode(T target, Document doc) throws Exception {
		Node node = doc.createElement(target.getClass().getSimpleName());
		for (Entry<?, ?> entry : ((Map<?, ?>) target).entrySet()) {
			Object k = entry.getKey();
			if (!(k instanceof String)) {
				break;
			}
			Object v = entry.getValue();
			if (v == null) {
				continue;
			}
			Node subNode = convertToNode(v, doc);
			setAttr(subNode, TAG_NAME, (String) k);
			node.appendChild(subNode);
		}
		return node;
	}
}