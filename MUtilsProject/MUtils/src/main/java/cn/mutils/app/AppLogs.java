package cn.mutils.app;

import android.util.Log;

import cn.mutils.core.log.Logs;
import cn.mutils.core.runtime.StackTraceUtil;
import cn.mutils.core.text.StringUtil;

/**
 * Logs for Android runtime
 */
public class AppLogs extends Logs {

    @Override
    public int verbose(String tag, String msg) {
        if (!mEnabled) {
            return 0;
        }
        tag = tag != null ? tag : "";
        msg = msg != null ? msg : "";
        return Log.v(tag, msg);
    }

    @Override
    public int verbose(String tag, String msg, Throwable tr) {
        if (!mEnabled) {
            return 0;
        }
        tag = tag != null ? tag : "";
        msg = msg != null ? msg : "";
        return Log.v(tag, msg, tr);
    }

    @Override
    public int debug(String tag, String msg) {
        if (!mEnabled) {
            return 0;
        }
        tag = tag != null ? tag : "";
        msg = msg != null ? msg : "";
        return Log.d(tag, msg);
    }

    @Override
    public int debug(String tag, String msg, Throwable tr) {
        if (!mEnabled) {
            return 0;
        }
        tag = tag != null ? tag : "";
        msg = msg != null ? msg : "";
        return Log.d(tag, msg, tr);
    }

    @Override
    public int info(String tag, String msg) {
        if (!mEnabled) {
            return 0;
        }
        tag = tag != null ? tag : "";
        msg = msg != null ? msg : "";
        return Log.i(tag, msg);
    }

    @Override
    public int info(String tag, String msg, Throwable tr) {
        if (!mEnabled) {
            return 0;
        }
        tag = tag != null ? tag : "";
        msg = msg != null ? msg : "";
        return Log.i(tag, msg, tr);
    }

    @Override
    public int warn(String tag, String msg) {
        if (!mEnabled) {
            return 0;
        }
        tag = tag != null ? tag : "";
        msg = msg != null ? msg : "";
        return Log.w(tag, msg);
    }

    @Override
    public int warn(String tag, String msg, Throwable tr) {
        if (!mEnabled) {
            return 0;
        }
        tag = tag != null ? tag : "";
        msg = msg != null ? msg : "";
        return Log.w(tag, msg, tr);
    }

    @Override
    public int error(String tag, String msg) {
        if (!mEnabled) {
            return 0;
        }
        tag = tag != null ? tag : "";
        msg = msg != null ? msg : "";
        return Log.e(tag, msg);
    }

    @Override
    public int error(String tag, String msg, Throwable tr) {
        if (!mEnabled) {
            return 0;
        }
        tag = tag != null ? tag : "";
        msg = msg != null ? msg : "";
        return Log.e(tag, msg, tr);
    }

    @Override
    public int verbose(String msg) {
        if (!mEnabled) {
            return 0;
        }
        msg = msg != null ? msg : "";
        return Log.v(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg);
    }

    @Override
    public int verbose(String msg, Throwable tr) {
        if (!mEnabled) {
            return 0;
        }
        msg = msg != null ? msg : "";
        return Log.v(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
    }

    @Override
    public int debug(String msg) {
        if (!mEnabled) {
            return 0;
        }
        msg = msg != null ? msg : "";
        return Log.d(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg);
    }

    @Override
    public int debug(String msg, Throwable tr) {
        if (!mEnabled) {
            return 0;
        }
        msg = msg != null ? msg : "";
        return Log.d(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
    }

    @Override
    public int info(String msg) {
        if (!mEnabled) {
            return 0;
        }
        msg = msg != null ? msg : "";
        return Log.i(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg);
    }

    @Override
    public int info(String msg, Throwable tr) {
        if (!mEnabled) {
            return 0;
        }
        msg = msg != null ? msg : "";
        return Log.i(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
    }

    @Override
    public int warn(String msg) {
        if (!mEnabled) {
            return 0;
        }
        msg = msg != null ? msg : "";
        return Log.w(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg);
    }

    @Override
    public int warn(String msg, Throwable tr) {
        if (!mEnabled) {
            return 0;
        }
        msg = msg != null ? msg : "";
        return Log.w(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
    }

    @Override
    public int error(String msg) {
        if (!mEnabled) {
            return 0;
        }
        msg = msg != null ? msg : "";
        return Log.e(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg);
    }

    @Override
    public int error(String msg, Throwable tr) {
        if (!mEnabled) {
            return 0;
        }
        msg = msg != null ? msg : "";
        return Log.e(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
    }

}
