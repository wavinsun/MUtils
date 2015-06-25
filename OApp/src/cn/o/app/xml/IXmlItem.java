package cn.o.app.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cn.o.app.io.INoProguard;
import cn.o.app.runtime.OField;

public interface IXmlItem extends INoProguard {

	public IXmlItem fromXml(Node xml, OField itemField);

	public Node toXml(Document doc, OField itemField);

}
