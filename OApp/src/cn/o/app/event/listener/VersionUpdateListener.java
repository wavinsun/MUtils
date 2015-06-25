package cn.o.app.event.listener;

public interface VersionUpdateListener {

	// return true to intercept update dialog
	public boolean onYes(String version);

	public void onNo();

	public void onUpdate(String version);

	public void onUpdateCancel(String version);
}