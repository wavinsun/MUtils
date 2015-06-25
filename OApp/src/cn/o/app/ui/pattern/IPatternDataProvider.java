package cn.o.app.ui.pattern;

public interface IPatternDataProvider {

	// 是否登陆
	public boolean isLogined();

	// 忘记手势密码
	public void forgetPattern();

	// 手势密码检测周期
	public long getPatternPeriod();

	// 是否启用手势密码
	public boolean isPatternEnabled();

	// 手势密码是否有效
	public boolean isPatternRight(String patternText);

	// 获取昵称
	public String getNickName();

	// 是否有手势密码
	public boolean hasPattern();
}
