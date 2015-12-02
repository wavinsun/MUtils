package cn.mutils.app.demo.ui;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import cn.mutils.app.core.annotation.event.OnClick;
import cn.mutils.app.core.annotation.res.FindViewById;
import cn.mutils.app.core.annotation.res.SetContentView;
import cn.mutils.app.core.log.Logs;
import cn.mutils.app.demo.R;
import cn.mutils.app.os.IntentExecutor;
import cn.mutils.app.ui.PopMenu;
import cn.mutils.app.ui.StateView;
import cn.mutils.app.ui.adapter.IItemView;
import cn.mutils.app.ui.adapter.ItemView;
import cn.mutils.app.ui.adapter.PopMenuAdapter;
import cn.mutils.app.util.AppUtil;

@SetContentView(R.layout.view_intent_executor)
public class IntentExecutorDemoView extends StateView {

	public IntentExecutorDemoView(Context context) {
		super(context);
	}

	public IntentExecutorDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public IntentExecutorDemoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@OnClick(R.id.go)
	protected void onClickGoBtn(View v) {
		Context context = getContext();
		String path = AppUtil.getDiskDataRoot(context) + "IntentExecutor.png";
		Logs.i(path);
		Bitmap bitmap = AppUtil.toBitmap((View) this.getParent());
		if (!AppUtil.save(bitmap, path)) {
			toast("Save failed");
			return;
		}
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
		List<IntentExecutor> executors = IntentExecutor.queryActivities(context, intent);
		IntentExecutorAdapter adapter = new IntentExecutorAdapter();
		adapter.setDataProvider(executors);
		PopMenu<IntentExecutor> popMenu = new PopMenu<IntentExecutor>(v);
		popMenu.setAdapter(adapter);
		popMenu.show();
	}

	class IntentExecutorAdapter extends PopMenuAdapter<IntentExecutor> {

		@Override
		public IItemView<IntentExecutor> getItemView() {
			return new IntentExecutorItemView(getContext());
		}

	}

	@SetContentView(R.layout.item_intent_executor)
	class IntentExecutorItemView extends ItemView<IntentExecutor> {

		@FindViewById(R.id.line)
		protected View mLine;

		@FindViewById(R.id.text)
		protected TextView mText;

		public IntentExecutorItemView(Context context) {
			super(context);
		}

		@Override
		public void onResume() {
			mLine.setVisibility(mPosition == 0 ? View.INVISIBLE : View.VISIBLE);
			int size = (int) AppUtil.dp2px(getContext(), 40);
			Drawable drawable = mDataProvider.getIcon();
			drawable.setBounds(0, 0, size, size);
			mText.setCompoundDrawables(drawable, null, null, null);
			mText.setText(mDataProvider.getLabel());
		}

		@OnClick
		protected void onClick() {
			((IntentExecutorAdapter) getAdapter()).getPopMenue().dismiss();
			getContext().startActivity(mDataProvider.getIntent());
		}

	}

}
