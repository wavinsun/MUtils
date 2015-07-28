package cn.o.app.media;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import cn.o.app.event.Listener;
import cn.o.app.ui.core.IActivityExecutor;

public class PickContactTask extends MediaTask {

	public static interface PickContactListener extends Listener {

		public void onComplete(String name, List<String> phones);

	}

	public PickContactTask(IActivityExecutor executor, int requestCode) {
		super(executor, requestCode);
	}

	public boolean pickContact() {
		if (mLocked) {
			return false;
		}
		Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		try {
			mExecutor.startActivityForResult(intent, mRequestCode);
		} catch (Exception e) {
			return false;
		}
		mExecutor.addOnActivityResultListener(mOnActivityResultListener);
		mLocked = true;
		return true;
	}

	public void setListener(PickContactListener listener) {
		super.setListener(listener);
	}

	@Override
	protected void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		ContentResolver resolver = mExecutor.getContext().getContentResolver();
		Cursor c = resolver.query(data.getData(), null, null, null, null);
		if (!c.moveToFirst()) {
			if (!c.isClosed()) {
				c.close();
			}
			return;
		}
		String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		String hasPhone = c.getString(c.getColumnIndex(Contacts.HAS_PHONE_NUMBER));
		if (!hasPhone.equals("1")) {
			if (!c.isClosed()) {
				c.close();
			}
			return;
		}
		String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
		Cursor pc = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
		ArrayList<String> phones = new ArrayList<String>();
		if (pc.moveToNext()) {
			for (; !pc.isAfterLast(); pc.moveToNext()) {
				int index = pc.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
				String phone = pc.getString(index);
				phones.add(phone);
			}
		}
		if (!pc.isClosed()) {
			pc.close();
		}
		if (!c.isClosed()) {
			c.close();
		}
		if (phones.size() == 0) {
			return;
		}
		PickContactListener listener = getListener(PickContactListener.class);
		if (listener != null) {
			listener.onComplete(name, phones);
		}
	}

}
