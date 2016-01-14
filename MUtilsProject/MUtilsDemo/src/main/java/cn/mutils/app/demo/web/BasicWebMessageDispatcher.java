package cn.mutils.app.demo.web;

import org.json.JSONObject;

import cn.mutils.app.core.json.JsonUtil;
import cn.mutils.app.core.reflect.ReflectUtil;
import cn.mutils.app.demo.web.BasicWebMessage.BasicWebMessageData;
import cn.mutils.app.demo.web.BasicWebMessage.BasicWebMessageResult;
import cn.mutils.app.ui.web.WebMessageDispatcher;

@SuppressWarnings({"serial", "unchecked"})
public abstract class BasicWebMessageDispatcher<DATA extends BasicWebMessageData, RESULT extends BasicWebMessageResult>
        extends WebMessageDispatcher<BasicWebMessage<DATA, RESULT>> {

    public Integer id;

    public String name;

    public String callbacker;

    public JSONObject data;

    public abstract int messageId();

    public abstract String messageName();

    @Override
    public boolean preTranslateMessage() {
        if (id != null) {
            if (id.equals(this.messageId())) {
                return false;
            }
        }
        if (name != null) {
            if (name.equals(this.messageName())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public BasicWebMessage<DATA, RESULT> translateMessage() {
        BasicWebMessage<DATA, RESULT> msg = new BasicWebMessage<DATA, RESULT>();
        msg.id = id;
        msg.name = name;
        msg.callbacker = callbacker;
        try {
            msg.data = (DATA) ReflectUtil.getParamRawType(this.getClass(), 0).newInstance();
            JsonUtil.convertFromJson(data, msg.data);
            return msg;
        } catch (Exception e) {
            return null;
        }
    }

}
