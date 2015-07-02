package cn.o.app.ui.pattern;

public interface IPatternOwner {

	public void disablePattern(long duration);

	public void enablePattern();

	public boolean checkPattern();

	public void doCheckPattern();

	public IPatternView newPattern();

	public void showPattern();

	public void hidePattern();

}
