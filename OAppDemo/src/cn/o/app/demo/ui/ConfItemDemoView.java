package cn.o.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import cn.o.app.OUtil;
import cn.o.app.conf.ConfItem;
import cn.o.app.core.annotation.Format;
import cn.o.app.core.annotation.Name;
import cn.o.app.core.annotation.Primitive;
import cn.o.app.core.annotation.Primitive.PrimitiveType;
import cn.o.app.core.annotation.event.OnClick;
import cn.o.app.core.annotation.res.SetContentView;
import cn.o.app.core.io.ODate;
import cn.o.app.demo.R;
import cn.o.app.ui.Alert;
import cn.o.app.ui.StateView;

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
		alert.setMessage(OUtil.toStringXML(confItem));
		alert.show();
	}

	@OnClick(R.id.conf_from_pref)
	protected void onClickConfFromPref() {
		MyConfItem confItem = new MyConfItem();
		confItem.getFromPref(getContext());
		Alert alert = new Alert(getContext());
		alert.setMessageGravity(Gravity.NO_GRAVITY);
		alert.setMessage(OUtil.toStringXML(confItem));
		alert.show();
	}

	@OnClick(R.id.conf_to_pref)
	protected void onClikcConfToPref() {
		ODate now = new ODate();
		MyConfItem confItem = new MyConfItem();
		confItem.setId((int) now.getTime());
		confItem.setName("lounien");
		confItem.setCreateTime(now);
		confItem.putToPref(getContext());
		Alert alert = new Alert(getContext());
		alert.setMessageGravity(Gravity.NO_GRAVITY);
		alert.setMessage(OUtil.toStringXML(confItem));
		alert.show();
	}

	@SuppressWarnings("serial")
	static class MyConfItem extends ConfItem {

		protected int mId;

		protected String mName;

		protected ODate mCreateTime;

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
		public ODate getCreateTime() {
			return mCreateTime;
		}

		public void setCreateTime(ODate createTime) {
			mCreateTime = createTime;
		}

	}

}
