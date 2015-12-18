package cn.mutils.app.ui.core;

/**
 * Server Session holder of framework for UI
 */
public interface ISessionHolder {

	/**
	 * Whether it is a session holder
	 * 
	 * @return
	 */
	public boolean isSessionHolder();

	/**
	 * Validate session now<br>
	 * If the session is invalid,we should do login logic.
	 */
	public void validateSession();

	/**
	 * Whether has session
	 */
	public boolean hasSession();

	/**
	 * Whether session is changed
	 * 
	 * @return
	 */
	public boolean isSessionChanged();

	/**
	 * Happens on session changed
	 */
	public void onSessionChanged();

}
