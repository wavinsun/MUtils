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

}
