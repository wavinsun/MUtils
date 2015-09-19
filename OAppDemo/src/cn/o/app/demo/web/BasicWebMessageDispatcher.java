package cn.o.app.demo.web;

import org.json.JSONObject;

import cn.o.app.ui.web.WebMessageDispatcher;

@SuppressWarnings("serial")
public abstract class BasicWebMessageDispatcher<MESSAGE> extends WebMessageDispatcher<MESSAGE> {

	public Integer id;

	public String name;

	public String callbacker;

	public JSONObject data;

}
