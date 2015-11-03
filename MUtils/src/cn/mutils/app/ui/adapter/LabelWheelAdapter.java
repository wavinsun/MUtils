package cn.mutils.app.ui.adapter;

import java.util.List;

import cn.mutils.app.core.ILabelItem;
import kankan.wheel.widget.WheelAdapter;

/**
 * {@link WheelAdapter} Whose data item is {@link ILabelItem}
 */
public class LabelWheelAdapter implements WheelAdapter {

	protected List<? extends ILabelItem> mDataProvider;

	protected int mMaxDesiredLength;

	public List<? extends ILabelItem> getDataProvider() {
		return mDataProvider;
	}

	public void setDataProvider(List<? extends ILabelItem> dataProvider) {
		mDataProvider = dataProvider;
	}

	public ILabelItem getLabelItem(int index) {
		if (mDataProvider == null) {
			return null;
		}
		if (index < 0 || index >= mDataProvider.size()) {
			return null;
		}
		return mDataProvider.get(index);
	}

	@Override
	public int getItemsCount() {
		if (mDataProvider == null) {
			return 0;
		}
		return mDataProvider.size();
	}

	@Override
	public String getItem(int index) {
		if (mDataProvider == null) {
			return null;
		}
		if (index < 0 || index >= mDataProvider.size()) {
			return null;
		}
		String str = mDataProvider.get(index).getLabel();
		if (str != null && mMaxDesiredLength > 1) {
			if (str.length() > mMaxDesiredLength) {
				str = str.substring(0, mMaxDesiredLength - 1) + "...";
			}
		}
		return str;
	}

	@Override
	public int getMaximumLength() {
		int max = 0;
		for (int i = getItemsCount() - 1; i >= 0; i--) {
			String label = mDataProvider.get(i).getLabel();
			if (label != null) {
				int labelLength = label.length();
				if (labelLength > max) {
					max = labelLength;
				}
			}
		}
		return max;
	}

	public void setMaxDesiredLength(int maxDesiredLength) {
		mMaxDesiredLength = maxDesiredLength;
	}
}
