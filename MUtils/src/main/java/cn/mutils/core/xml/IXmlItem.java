package cn.mutils.core.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;
import proguard.annotation.KeepImplementations;

import cn.mutils.core.beans.BeanField;

@Keep
@KeepClassMembers
@KeepImplementations
public interface IXmlItem {

    IXmlItem fromXml(Node xml, BeanField itemField);

    Node toXml(Document doc, BeanField itemField);

}
