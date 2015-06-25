package cn.o.app.ui.pattern;

public interface IPatternView {

	// 忘记手势密码
	public void forget();

	// 完成逻辑
	public void finish();

	// 手势是否正确
	public boolean isPatternRight();

	// 刷新
	public void refresh();
}
