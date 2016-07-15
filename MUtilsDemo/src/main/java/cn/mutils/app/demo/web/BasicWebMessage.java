package cn.mutils.app.demo.web;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

import cn.mutils.app.demo.web.BasicWebMessage.BasicWebMessageData;
import cn.mutils.app.demo.web.BasicWebMessage.BasicWebMessageResult;
import cn.mutils.app.ui.web.WebMessageState;

@SuppressWarnings("serial")
public class BasicWebMessage<DATA extends BasicWebMessageData, RESULT extends BasicWebMessageResult> {

    @Keep
    @KeepClassMembers
    public static class BasicWebMessageData {

    }

    @Keep
    @KeepClassMembers
    public static class BasicWebMessageResult {

        public WebMessageState state = WebMessageState.invalid;

    }

    public Integer id;

    public String name;

    public DATA data;

    public RESULT result;

    public String callbacker;

}
