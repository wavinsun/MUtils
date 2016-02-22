package cn.mutils.core.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cn.mutils.core.INoProguard;
import cn.mutils.core.beans.BeanField;

public interface IXmlItem extends INoProguard {

    IXmlItem fromXml(Node xml, BeanField itemField);

    Node toXml(Document doc, BeanField itemField);

}
