package cn.o.app.ui.pattern;

public interface IPatternOwner {

	// 在一段时间内取消手势
	public void disablePattern(long duration);

	// 立即启用手势
	public void enablePattern();

	// 是否需要检测手势密码
	public boolean checkPattern();

	// 手势密码逻辑实现
	public void doCheckPattern();

	// 创建手势密码视图
	public IPatternView newPattern();

	// 显示手势密码视图
	public void showPattern();

	// 隐藏手势密码视图
	public void hidePattern();
}
