package cn.o.app.ui.photo;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import cn.o.app.event.listener.OnSelectedChangeListener;
import cn.o.app.ui.Activitier;

public class PhotoActivity extends Activitier {

	protected PhotoContainer mPhotoContainer;

	protected PhotoExtra mExtra;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mPhotoContainer = new PhotoContainer(this);
		mPhotoContainer.setLayoutParams(
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		mPhotoContainer.setOnSelectedChangeListener(new OnSelectedChangeListener() {

			@Override
			public void onChanged(View v, int index) {
				finish();
			}
		});
		this.setContentView(mPhotoContainer);

		mExtra = new PhotoExtra();
		if (mExtra.getFrom(getIntent())) {
			mPhotoContainer.setDataProvider(mExtra.getPhotos(), mExtra.getDisplayedIndex());
		}
	}
}
