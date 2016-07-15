package cn.mutils.app.push;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;
import proguard.annotation.KeepImplementations;

import cn.mutils.app.os.IContextProvider;

/**
 * Push dispatcher of framework
 */
@Keep
@KeepClassMembers
@KeepImplementations
public interface IPushDispatcher<MESSAGE> extends IContextProvider {

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
