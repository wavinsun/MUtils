package cn.o.app.demo.ui;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import cn.o.app.OUtil;
import cn.o.app.annotation.event.OnClick;
import cn.o.app.annotation.res.SetContentView;
import cn.o.app.demo.R;
import cn.o.app.media.CropPhotoTask;
import cn.o.app.media.CropPhotoTask.CropPhotoListener;
import cn.o.app.media.MakeVideoTask;
import cn.o.app.media.MakeVideoTask.MakeVideoListener;
import cn.o.app.media.PickContactTask;
import cn.o.app.media.PickContactTask.PickContactListener;
import cn.o.app.media.PickPhotoTask;
import cn.o.app.media.PickPhotoTask.PickPhotoListener;
import cn.o.app.media.TakePhotoTask;
import cn.o.app.media.TakePhotoTask.TakePhotoListener;
import cn.o.app.ui.Alert;
import cn.o.app.ui.StateView;
import cn.o.app.ui.photo.PhotoActivity;
import cn.o.app.ui.photo.PhotoExtra;
import cn.o.app.ui.video.VideoActivity;
import cn.o.app.ui.video.VideoActivity.VideoExtra;

@SetContentView(R.layout.view_media)
public class MediaDemoView extends StateView {

	public static final int REQUEST_CODE_PICK_CONTACT = 1000;
	public static final int REQUEST_CODE_TAKE_PHOTO = 1001;
	public static final int REQUEST_CODE_PICK_PHOTO = 1002;
	public static final int REQUEST_CODE_CROP_PHOTO = 1003;
	public static final int REQUEST_CODE_MAKE_VIDEO = 1004;

	protected Uri mCropPhotoUri;

	public MediaDemoView(Context context) {
		super(context);
	}

	public MediaDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MediaDemoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@OnClick(R.id.pick_contact)
	protected void onClickPickContact() {
		PickContactTask task = new PickContactTask(this, REQUEST_CODE_PICK_CONTACT);
		task.setListener(new PickContactListener() {

			@Override
			public void onComplete(String name, List<String> phones) {
				Alert alert = new Alert(getContext());
				alert.setTitle(name);
				StringBuilder sb = new StringBuilder();
				for (String phone : phones) {
					if (sb.length() != 0) {
						sb.append("\n");
					}
					sb.append(phone);
				}
				alert.setMessage(sb);
				alert.show();
			}
		});
		task.pickContact();
	}

	@OnClick(R.id.take_photo)
	protected void onClickTakePhoto() {
		TakePhotoTask task = new TakePhotoTask(this, REQUEST_CODE_TAKE_PHOTO);
		task.setListener(new TakePhotoListener() {

			@Override
			public void onComplete(Uri uri) {
				mCropPhotoUri = uri;
				Intent intent = new Intent(getContext(), PhotoActivity.class);
				PhotoExtra extra = new PhotoExtra();
				extra.setPhotos(OUtil.asArrayList(new String[] { uri.getPath() }));
				extra.putTo(intent);
				startActivity(intent);
			}
		});
		task.takePhoto();
	}

	@OnClick(R.id.pick_photo)
	protected void onClickPickPhoto() {
		PickPhotoTask task = new PickPhotoTask(this, REQUEST_CODE_PICK_PHOTO);
		task.setListener(new PickPhotoListener() {

			@Override
			public void onComplete(Uri uri) {
				mCropPhotoUri = uri;
				Intent intent = new Intent(getContext(), PhotoActivity.class);
				PhotoExtra extra = new PhotoExtra();
				extra.setPhotos(OUtil.asArrayList(new String[] { uri.getPath() }));
				extra.putTo(intent);
				startActivity(intent);
			}
		});
		task.pickPhoto();
	}

	@OnClick(R.id.crop_photo)
	protected void onClickCropPhoto() {
		if (mCropPhotoUri == null) {
			toast("Please take or pick one photo before click me");
			return;
		}
		CropPhotoTask task = new CropPhotoTask(this, REQUEST_CODE_CROP_PHOTO);
		task.setListener(new CropPhotoListener() {

			@Override
			public void onComplete(Uri uri) {
				Intent intent = new Intent(getContext(), PhotoActivity.class);
				PhotoExtra extra = new PhotoExtra();
				extra.setPhotos(OUtil.asArrayList(new String[] { uri.getPath() }));
				extra.putTo(intent);
				startActivity(intent);
			}
		});
		task.cropPhoto(mCropPhotoUri, 150, 150);
	}

	@OnClick(R.id.make_video)
	protected void onClickMakeVideo() {
		MakeVideoTask task = new MakeVideoTask(this, REQUEST_CODE_MAKE_VIDEO);
		task.setListener(new MakeVideoListener() {

			@Override
			public void onComplete(Uri uri) {
				Intent intent = new Intent(getContext(), VideoActivity.class);
				VideoExtra extra = new VideoExtra();
				extra.setUrl(uri.getPath());
				extra.putTo(intent);
				startActivity(intent);
			}
		});
		task.makeVideo();
	}

}
