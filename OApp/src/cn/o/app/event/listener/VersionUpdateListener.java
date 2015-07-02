package cn.o.app.event.listener;

public interface VersionUpdateListener {

	/**
	 * 
	 * @param version
	 * @return Return true to intercept
	 */
	public boolean onYes(String version);

	public void onNo();

	public void onUpdate(String version);

	public void onUpdateCancel(String version);

}