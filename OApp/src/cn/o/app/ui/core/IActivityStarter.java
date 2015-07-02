package cn.o.app.ui.core;

import android.content.Intent;

public interface IActivityStarter extends IActivityResultCatcher {

	public void startActivity(Intent intent);

	public void startActivityForResult(Intent intent, int requestCode);

	public void onActivityResult(int requestCode, int resultCode, Intent data);

}
