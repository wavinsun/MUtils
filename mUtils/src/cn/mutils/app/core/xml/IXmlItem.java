package cn.mutils.app.core.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cn.mutils.app.core.INoProguard;
import cn.mutils.app.core.beans.BeanField;

public interface IXmlItem extends INoProguard {

	public IXmlItem fromXml(Node xml, BeanField itemField);

	public Node toXml(Document doc, BeanField itemField);

}
