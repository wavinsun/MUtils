package cn.o.app.ui.pattern;

import android.content.Context;
import cn.o.app.core.io.INoProguard;

public interface IPatternSettings extends INoProguard {

	public String getUserId();

	public void setUserId(String userId);

	public String getPassword();

	public void setPassword(String password);

	public boolean isEnabled();

	public void setEnabled(boolean enabled);

	public int getPeriod();

	public void setPeriod(int period);

	public boolean load(Context context, String userId);

	public boolean save(Context context);

}
