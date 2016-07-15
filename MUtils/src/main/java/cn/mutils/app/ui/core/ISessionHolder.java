package cn.mutils.app.ui.core;

/**
 * Server Session holder of framework for UI
 */
@SuppressWarnings("unused")
public interface ISessionHolder {

    /**
     * Whether it is a session holder
     */
    boolean isSessionHolder();

    /**
     * Validate session now<br>
     * If the session is invalid,we should do login logic.
     */
    void validateSession();

    /**
     * Whether has session
     */
    boolean hasSession();

    /**
     * Whether session is changed
     */
    boolean isSessionChanged();

    /**
     * Happens on session changed
     */
    void onSessionChanged();

}
