package cn.o.app.demo.web;

import cn.o.app.core.INoProguard;
import cn.o.app.demo.web.BasicWebMessage.BasicWebMessageData;
import cn.o.app.demo.web.BasicWebMessage.BasicWebMessageResult;
import cn.o.app.ui.web.WebMessageState;

@SuppressWarnings("serial")
public class BasicWebMessage<DATA extends BasicWebMessageData, RESULT extends BasicWebMessageResult>
		implements INoProguard {

	public static class BasicWebMessageData implements INoProguard {

	}

	public static class BasicWebMessageResult implements INoProguard {

		public WebMessageState state = WebMessageState.invalid;

	}

	public Integer id;

	public String name;

	public DATA data;

	public RESULT result;

	public String callbacker;

}
