package cn.mutils.app.ui.photo;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.mutils.app.event.listener.OnSelectedChangeListener;
import cn.mutils.app.ui.Dialoger;

@SuppressWarnings("unused")
public class PhotoDialog {

    protected Dialoger mDialog;

    protected PhotoContainer mPhotoContainer;

    public PhotoDialog(Context context) {
        mPhotoContainer = new PhotoContainer(context);
        mPhotoContainer.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mPhotoContainer.setOnSelectedChangeListener(new OnSelectedChangeListener() {

            @Override
            public void onChanged(View v, int index) {
                dismiss();
            }
        });
        mDialog = new Dialoger(context);
        mDialog.setCancelable(true);
        mDialog.setContentView(mPhotoContainer);
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                mDialog = null;
                mPhotoContainer = null;
            }
        });
    }

    public void setDataProvider(List<String> dataProvider, int displayIndex) {
        if (mPhotoContainer == null) {
            return;
        }
        mPhotoContainer.setDataProvider(dataProvider, displayIndex);
    }

    public void setDataProvider(List<String> dataProvider) {
        setDataProvider(dataProvider, 0);
    }

    public void dismiss() {
        if (mDialog == null) {
            return;
        }
        mDialog.dismiss();
        mDialog = null;
    }

    public void show() {
        if (mDialog == null) {
            return;
        }
        mDialog.requestHFill();
        mDialog.show();
    }

}