package cn.mutils.app.core.net;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.protocol.HttpContext;

@SuppressWarnings("deprecation")
public class NetRedirectHandler extends DefaultRedirectHandler {

    @Override
    public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
        boolean isRedirect = super.isRedirectRequested(response, context);
        if (!isRedirect) {
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == 301 || responseCode == 302) {
                return true;
            }
        }
        return isRedirect;
    }

}
