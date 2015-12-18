package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;
import cn.mutils.app.core.annotation.event.OnClick;
import cn.mutils.app.core.annotation.res.FindViewById;
import cn.mutils.app.core.annotation.res.SetContentView;
import cn.mutils.app.demo.R;
import cn.mutils.app.ui.ImageIcon;
import cn.mutils.app.ui.StateView;
import cn.mutils.app.util.AppUtil;

@SetContentView(R.layout.view_path_button)
public class DemoPathButtonView extends StateView {

	protected boolean mOpened;

	@FindViewById(R.id.go)
	protected TextView mGoButton;

	@FindViewById(R.id.path_left)
	protected ImageIcon mLeftPathButton;

	@FindViewById(R.id.path_bottom)
	protected ImageIcon mBottomPathButton;

	@FindViewById(R.id.path_right)
	protected ImageIcon mRightPathButton;

	public DemoPathButtonView(Context context) {
		super(context);
	}

	public DemoPathButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DemoPathButtonView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@OnClick(R.id.go)
	protected void onClickGo() {
		switchPathButtons();
	}

	@OnClick({ R.id.path_left, R.id.path_bottom, R.id.path_right })
	protected void onClickPathButton() {
		switchPathButtons();
	}

	protected void switchPathButtons() {
		if (mOpened) {
			mOpened = false;
			ImageIcon[] pathButtons = new ImageIcon[] { mLeftPathButton, mRightPathButton, mBottomPathButton };
			for (ImageIcon pathButton : pathButtons) {
				Animation anim = AppUtil.animOfPathButton(mOpened, pathButton, mGoButton);
				anim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation anim) {

					}

					@Override
					public void onAnimationRepeat(Animation anim) {

					}

					@Override
					public void onAnimationEnd(Animation anim) {
						mLeftPathButton.setVisibility(View.INVISIBLE);
						mRightPathButton.setVisibility(View.INVISIBLE);
						mBottomPathButton.setVisibility(View.INVISIBLE);
					}
				});
				pathButton.startAnimation(anim);
			}
		} else {
			mOpened = true;
			ImageIcon[] pathButtons = new ImageIcon[] { mLeftPathButton, mRightPathButton, mBottomPathButton };
			for (ImageIcon pathButton : pathButtons) {
				Animation anim = AppUtil.animOfPathButton(mOpened, pathButton, mGoButton);
				pathButton.setVisibility(View.VISIBLE);
				pathButton.startAnimation(anim);
			}
		}
	}

}
