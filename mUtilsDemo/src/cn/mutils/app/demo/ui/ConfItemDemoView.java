package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import cn.mutils.app.AppUtil;
import cn.mutils.app.core.annotation.Format;
import cn.mutils.app.core.annotation.Name;
import cn.mutils.app.core.annotation.Primitive;
import cn.mutils.app.core.annotation.Primitive.PrimitiveType;
import cn.mutils.app.core.annotation.event.OnClick;
import cn.mutils.app.core.annotation.res.SetContentView;
import cn.mutils.app.core.time.DateTime;
import cn.mutils.app.demo.R;
import cn.mutils.app.settings.ConfItem;
import cn.mutils.app.ui.Alert;
import cn.mutils.app.ui.StateView;

@SetContentView(R.layout.view_conf_item)
public class ConfItemDemoView extends StateView {

	public ConfItemDemoView(Context context) {
		super(context);
	}

	public ConfItemDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ConfItemDemoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@OnClick(R.id.conf_from_asset)
	protected void onClickConfFromAsset() {
		MyConfItem confItem = new MyConfItem();
		confItem.getFromAsset(getContext());
		Alert alert = new Alert(getContext());
		alert.setMessageGravity(Gravity.NO_GRAVITY);
		alert.setMessage(AppUtil.toStringXML(confItem));
		alert.show();
	}

	@OnClick(R.id.conf_from_pref)
	protected void onClickConfFromPref() {
		MyConfItem confItem = new MyConfItem();
		confItem.getFromPref(getContext());
		Alert alert = new Alert(getContext());
		alert.setMessageGravity(Gravity.NO_GRAVITY);
		alert.setMessage(AppUtil.toStringXML(confItem));
		alert.show();
	}

	@OnClick(R.id.conf_to_pref)
	protected void onClikcConfToPref() {
		DateTime now = new DateTime();
		MyConfItem confItem = new MyConfItem();
		confItem.setId((int) now.getTime());
		confItem.setName("wavinsun");
		confItem.setCreateTime(now);
		confItem.putToPref(getContext());
		Alert alert = new Alert(getContext());
		alert.setMessageGravity(Gravity.NO_GRAVITY);
		alert.setMessage(AppUtil.toStringXML(confItem));
		alert.show();
	}

	@SuppressWarnings("serial")
	static class MyConfItem extends ConfItem {

		protected int mId;

		protected String mName;

		protected DateTime mCreateTime;

		public MyConfItem() {
			mAssetFileName = "MyConfItem.xml";
			mPrefFileName = "MyConfItem.xml";
		}

		public int getId() {
			return mId;
		}

		public void setId(int id) {
			mId = id;
		}

		public String getName() {
			return mName;
		}

		public void setName(String name) {
			mName = name;
		}

		@Name("createAt")
		@Primitive(PrimitiveType.STRING)
		@Format("yyyy-MM-dd HH:mm:ss")
		public DateTime getCreateTime() {
			return mCreateTime;
		}

		public void setCreateTime(DateTime createTime) {
			mCreateTime = createTime;
		}

	}

}
