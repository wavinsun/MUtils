package cn.mutils.app.ui.pattern;

public interface IPatternOwner {

	public void disablePattern(long duration);

	public void enablePattern();

	public boolean checkPattern();

	public void doCheckPattern();

	public PatternDialog newPatternDialog();

	public void showPattern();

	public void hidePattern();

}
