package cn.mutils.app.demo.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import org.json.JSONObject;

import cn.mutils.app.demo.PushNotifyActivity;
import cn.mutils.app.demo.PushNotifyActivity.DemoPushNotifyExtra;
import cn.mutils.app.demo.R;
import cn.mutils.app.demo.push.HelloWorldPushDispatcher.HelloWorldMessage;
import cn.mutils.app.push.PushDispathcer;
import cn.mutils.core.INoProguard;
import cn.mutils.core.json.JsonUtil;

@SuppressWarnings("serial")
public class HelloWorldPushDispatcher extends PushDispathcer<HelloWorldMessage> {

    public static final int CODE_HELLO_WORLD = 1000;

    public static class HelloWorldMessage implements INoProguard {

        protected String mName;

        protected String mMessage;

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getMessage() {
            return mMessage;
        }

        public void setMessage(String message) {
            mMessage = message;
        }

    }

    protected int mCode;

    protected JSONObject mData;

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        mCode = code;
    }

    public JSONObject getData() {
        return mData;
    }

    public void setData(JSONObject data) {
        mData = data;
    }

    @Override
    public boolean preTranslateMessage() {
        if (mCode != CODE_HELLO_WORLD) {
            return true;
        }
        return false;
    }

    @Override
    public HelloWorldMessage translateMessage() {
        try {
            return JsonUtil.convertFromJson(mData, HelloWorldMessage.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onMessage(HelloWorldMessage message) {
        Context context = getContext();
        Intent intent = new Intent(context, PushNotifyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        DemoPushNotifyExtra extra = new DemoPushNotifyExtra();
        extra.setMessage(message);
        extra.putTo(intent);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, CODE_HELLO_WORLD, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
        builder.setContentIntent(pendingIntent);
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        builder.setOngoing(false);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setTicker("Hello world!");
        builder.setContentTitle(message.mName);
        builder.setContentText(message.mMessage);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(CODE_HELLO_WORLD, builder.build());
    }

}
