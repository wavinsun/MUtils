package cn.mutils.app.core.event.listener;

/**
 * Version update listener
 */
public interface VersionUpdateListener {

    /**
     * @param version Version
     * @return Return true to intercept
     */
    boolean onYes(String version);

    void onNo();

    void onUpdate(String version);

    void onUpdateCancel(String version);

}