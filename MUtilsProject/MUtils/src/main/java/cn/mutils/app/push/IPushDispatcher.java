package cn.mutils.app.push;

import cn.mutils.app.core.INoProguard;
import cn.mutils.app.os.IContextProvider;

/**
 * Push dispatcher of framework
 */
public interface IPushDispatcher<MESSAGE> extends IContextProvider, INoProguard {

    /**
     * Whether to intercept translate message
     *
     * @return Return true to intercept dispatching
     */
    boolean preTranslateMessage();

    /**
     * Translate dispatcher to MESSAGE
     */
    MESSAGE translateMessage();

    /**
     * Dispatch message
     */
    void onMessage(MESSAGE message);

    /**
     * Get push manager
     */
    IPushManager getManager();

    /**
     * Set push manager
     */
    void setManager(IPushManager manager);

}
