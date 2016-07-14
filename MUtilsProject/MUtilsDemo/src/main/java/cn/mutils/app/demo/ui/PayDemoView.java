package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

import cn.mutils.app.demo.R;
import cn.mutils.app.net.INetTask;
import cn.mutils.app.net.NetExceptionUtil;
import cn.mutils.app.net.NetTask;
import cn.mutils.app.net.NetTaskListener;
import cn.mutils.app.pay.AlipayTask;
import cn.mutils.app.pay.AppPayListener;
import cn.mutils.app.pay.AppPayTask;
import cn.mutils.app.pay.UPPayTask;
import cn.mutils.app.pay.WXPayTask;
import cn.mutils.app.ui.Alert;
import cn.mutils.app.ui.StateView;
import cn.mutils.core.annotation.Name;
import cn.mutils.core.annotation.event.Click;
import cn.mutils.core.annotation.res.FindViewById;
import cn.mutils.core.annotation.res.SetContentView;

@SetContentView(R.layout.view_pay)
public class PayDemoView extends StateView {

    @FindViewById(R.id.log)
    protected TextView mLog;

    public PayDemoView(Context context) {
        super(context);
    }

    public PayDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PayDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        StringBuilder sb = new StringBuilder();
        sb.append("Union Pay Account:\n	NO.	6226 4401 2345 6785\n	PIN	111101\n");
        mLog.setText(sb);
    }

    @Click(R.id.union_pay)
    protected void onClickUnionPay() {
        UPPayTNTask task = new UPPayTNTask();
        task.addListener(new NetTaskListener<String, String>() {

            @Override
            public void onException(INetTask<String, String> task, Exception e) {
                if (NetExceptionUtil.handle(e, getToastOwner()) != null) {
                    toast("Get TN failed");
                }
            }

            @Override
            public void onComplete(INetTask<String, String> task, String response) {
                UPPayTask payTask = new UPPayTask();
                payTask.setDebug(true);
                payTask.setTradeNo(response);
                payTask.setContext(getContext());
                payTask.addListener(new AppPayListener() {

                    @Override
                    public void onError(AppPayTask task, Exception e) {
                        Alert alert = new Alert(getContext());
                        alert.setTitle("Pay Error");
                        alert.show();
                    }

                    @Override
                    public void onComplete(AppPayTask task) {
                        Alert alert = new Alert(getContext());
                        alert.setTitle("Pay Complete");
                        alert.show();
                    }
                });
                payTask.start();
            }
        });
        add(task);
    }

    @Click(R.id.ali_pay)
    protected void onClickAliPay() {
        AlipayTask task = new AlipayTask();
        task.setBody("Body");
        task.setContext(getContext());
        task.setNotifyUrl("http://www.***.com/notify_url.jsp");
        task.setOutTradeNo(System.currentTimeMillis() + "");
        task.setPartner("2088411*********");
        task.setRsaPrivate(
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAP7Gq5UJJoNQuuRgLIECVt8JGpbc8D70m1KD3mGfeg0lB4b3U7apVd8Qu+Z1Z+rMz6T4+dPdR/oujfbNUhyJZM+3NAavv1/HPkJcE2OK5Kwf/WldPm7mDT2XWPGfU+oSVDtg4l0WizJ/kc92SK3Fnhh8NQZedzL9g6SJLXqRpR65AgMBAAECgYEAj6XGYl5pZ4qiBVewYADIqDJC2qKxll1mIc0WGmbHcxfYuaFDgu7Q6tdNii/SKpHV1YdHnMvb5FRRWMEFNe/FdTQyJguBjHyIw5u4pX549/ttQoFtD7vpFMxHs11rcAY9CWooUsFazDpFa4uwHGB46qvv+EGuDTIl0F0UpEl8Bu0CQQD/rf/+I/wiBfVi0M3DBcz2olHRrcjN2JhwA4qj364NdD7+D2XGvzPEpBqcZ7JqjLPnDfXAKiBKTbeyZp9vT4KvAkEA/xhhZhYuzeLXdoEhY1Aiz8fkJ53N3Kx/CimyBQQkSR+IYzpMl4M9NQ82RGMFkE7tXzaDYnmPJ6o3csbRnTHvFwJAXV+wQ6HkrJA8g2/6FSUDK3cwJkEvOm3FjfLdKOfBasxvTN1Wr/SX9VJxHXmJYPwtn04r15gK3tfvJEnyI/aHpQJATakufdXjCes1jW5iq8mJz5gbbbQtKDGbu4xWFhDcuyZOb2cgSkA6Mh0feFJtLzHYMlR2S1SWsIyxtM+yoRjCoQJBAIlCuqlOgakOlT6DWKZXakMFm1qZy5zRecXOoK6xcrjrNLf+dMj9o8Qf4Dj4PoxDUwSaEK0bVMrpdi76K+Z6twk=");
        task.setSeller("***@***.com");
        task.setSubject("Subject");
        task.setTotalFee("0.01");
        task.addListener(new AppPayListener() {

            @Override
            public void onError(AppPayTask task, Exception e) {
                Alert alert = new Alert(getContext());
                alert.setTitle("Pay Error");
                alert.show();
            }

            @Override
            public void onComplete(AppPayTask task) {
                Alert alert = new Alert(getContext());
                alert.setTitle("Pay Complete");
                alert.show();
            }
        });
        task.start();
    }

    @Click(R.id.wx_pay)
    protected void onClickWXPay() {
        WXPayInfoTask task = new WXPayInfoTask();
        task.setRequest(new WXPayInfoTask.WXPayInfoReq());
        task.addListener(new NetTaskListener<WXPayInfoTask.WXPayInfoReq, WXPayInfoTask.WXPayInfoRes>() {

            @Override
            public void onComplete(INetTask<WXPayInfoTask.WXPayInfoReq, WXPayInfoTask.WXPayInfoRes> task, WXPayInfoTask.WXPayInfoRes wxPayInfoRes) {
                WXPayTask payTask = new WXPayTask();
                payTask.setContext(getContext());
                payTask.setAppId(wxPayInfoRes.appid);
                payTask.setNonceStr(wxPayInfoRes.noncestr);
                payTask.setPackageValue(wxPayInfoRes.packageValue);
                payTask.setPartnerId(wxPayInfoRes.partnerid);
                payTask.setPrepayId(wxPayInfoRes.prepayid);
                payTask.setSign(wxPayInfoRes.sign);
                payTask.setTimeStamp(wxPayInfoRes.timestamp);
                payTask.addListener(new AppPayListener() {
                    @Override
                    public void onComplete(AppPayTask task) {
                        Alert alert = new Alert(getContext());
                        alert.setTitle("Pay Complete");
                        alert.show();
                    }

                    @Override
                    public void onError(AppPayTask task, Exception e) {
                        Alert alert = new Alert(getContext());
                        alert.setTitle("Pay Error");
                        alert.show();
                    }
                });
                payTask.start();
            }

            @Override
            public void onException(INetTask<WXPayInfoTask.WXPayInfoReq, WXPayInfoTask.WXPayInfoRes> task, Exception e) {
                if (NetExceptionUtil.handle(e, getToastOwner()) != null) {
                    toast("Get prepayid failed");
                }
            }

        });
        add(task);
    }

    public static class WXPayInfoTask extends NetTask<WXPayInfoTask.WXPayInfoReq, WXPayInfoTask.WXPayInfoRes> {

        @Keep
        @KeepClassMembers
        public static class WXPayInfoReq {
            public String plat = "android";
        }

        @Keep
        @KeepClassMembers
        public static class WXPayInfoRes {
            public String appid;
            public String noncestr;

            @Name("package")
            public String packageValue;

            public String partnerid;
            public String prepayid;
            public String timestamp;
            public String sign;
        }

        @Override
        public void setContext(Context context) {
            super.setContext(context);
            setUrl("http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php");
        }

        @Override
        protected void debugging(String event, String message) {
            super.debugging(event, message);
        }
    }

    static class UPPayTNTask extends NetTask<String, String> {

        @Override
        public void setContext(Context context) {
            super.setContext(context);
            setUrl("http://202.101.25.178:8080/sim/gettn");
        }

        @Override
        protected void debugging(String event, String message) {
            super.debugging(event, message);
        }

    }

}
