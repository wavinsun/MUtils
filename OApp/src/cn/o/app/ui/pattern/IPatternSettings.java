package cn.o.app.ui.pattern;

import android.content.Context;
import cn.o.app.io.INoProguard;

public interface IPatternSettings extends INoProguard {
	// 用户标识
	public String getUserId();

	public void setUserId(String userId);

	// 手势密码
	public String getPassword();

	public void setPassword(String password);

	// 是否启用
	public boolean isEnabled();

	public void setEnabled(boolean enabled);

	// 检测周期
	public int getPeriod();

	public void setPeriod(int period);

	// 加载配置
	public boolean load(Context context, String userId);

	// 保存配置
	public boolean save(Context context);
}
