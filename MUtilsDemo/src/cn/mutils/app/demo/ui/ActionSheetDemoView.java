package cn.mutils.app.demo.ui;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import cn.mutils.app.core.annotation.event.OnClick;
import cn.mutils.app.core.annotation.res.SetContentView;
import cn.mutils.app.demo.R;
import cn.mutils.app.ui.ActionSheet;
import cn.mutils.app.ui.ActionSheet.ActionItem;
import cn.mutils.app.ui.ActionSheet.OnActionItemClickListener;
import cn.mutils.app.ui.StateView;

@SetContentView(R.layout.view_action_sheet)
public class ActionSheetDemoView extends StateView {

	public ActionSheetDemoView(Context context) {
		super(context);
	}

	public ActionSheetDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ActionSheetDemoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@OnClick(R.id.go)
	protected void onClickGo() {
		ActionSheet<ActionItem> actionSheet = new ActionSheet<ActionItem>(getContext());
		ArrayList<ActionItem> dataProvider = new ArrayList<ActionSheet.ActionItem>();
		ActionItem item;
		item = new ActionItem();
		item.setText("New Versions");
		dataProvider.add(item);
		item = new ActionItem();
		item.setText("Help");
		dataProvider.add(item);
		item = new ActionItem();
		item.setText("Exit");
		dataProvider.add(item);
		actionSheet.setDataProvider(dataProvider);
		actionSheet.show();
		actionSheet.setOnActionItemClickListener(new OnActionItemClickListener<ActionSheet.ActionItem>() {

			@Override
			public void onItemClick(ActionSheet<ActionItem> sheet, View v, int position, ActionItem dataItem) {
				toast(dataItem.getText());
			}

		});
	}

}
