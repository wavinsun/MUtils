package cn.mutils.app.ui.pattern;

/**
 * Data provider for pattern of gestures password
 */
public interface IPatternDataProvider {

	public boolean isLogined();

	public void forgetPattern();

	public long getPatternPeriod();

	public boolean isPatternEnabled();

	public boolean isPatternRight(String patternText);

	public String getNickName();

	public boolean hasPattern();

}
