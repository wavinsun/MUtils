package cn.o.app.core.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cn.o.app.core.io.INoProguard;
import cn.o.app.core.runtime.OField;

public interface IXmlItem extends INoProguard {

	public IXmlItem fromXml(Node xml, OField itemField);

	public Node toXml(Document doc, OField itemField);

}
