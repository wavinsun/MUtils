package cn.o.app.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.o.app.R;

/**
 * PullLayout refer to
 * http://blog.csdn.net/zhongkejingwang/article/details/38868463
 */
@SuppressLint({ "HandlerLeak", "InflateParams" })
@SuppressWarnings("deprecation")
public class PullLayout extends RelativeLayout {

	public interface OnRefreshListener {

		void onRefresh(PullLayout pullLayout);

		void onLoadMore(PullLayout pullLayout);

	}

	public static final int STATAE_INIT = 0;
	public static final int STATE_RELEASE_TO_REFRESH = 1;
	public static final int STATE_REFRESHING = 2;
	public static final int STATE_RELEASE_TO_LOAD = 3;
	public static final int STATE_LOADING = 4;
	public static final int STATE_DONE = 5;

	private int state = STATAE_INIT;

	private OnRefreshListener mListener;

	private float downY, lastY;

	/** Pull down distance.pullDownY and pullUpY can not be same as zero */
	public float pullDownY = 0;
	/** Pull up distance */
	private float pullUpY = 0;

	/** Refresh distance */
	private float refreshDist = 200;
	/** Load more distance */
	private float loadMoreDist = 200;

	private MyTimer timer;

	public float MOVE_SPEED = 8;
	/** First layout */
	private boolean isLayout = false;
	/** Touch on refreshing */
	private boolean isTouch = false;
	/** 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化 */
	private float radio = 2;

	/** Arrow animation */
	private RotateAnimation reverseAnimation;

	/** Pull down head view */
	private View refreshHeadView;
	/** Pull down arrow view */
	private View pullDownView;
	/** Refreshing icon */
	private View refreshingView;
	/** Refresh state TextView */
	private TextView refreshStateTextView;

	/** Pull up foot view */
	private View loadmoreFootView;
	/** Pull up arrow view */
	private View pullUpView;
	/** Loading icon */
	private View loadingView;
	/** Load state TextView */
	private TextView loadStateTextView;

	/** Pull View who is real view for content */
	private View pullView;
	/** Filter one more touch point */
	private int mEvents;

	/** Whether it can pull down while touching */
	private boolean canPullDown = true;

	/** Whether it can pull up while touching */
	private boolean canPullUp = true;

	/** Whether pull down enabled */
	protected boolean mPullDownEnabled = true;
	/** Whether pull up enabled */
	protected boolean mPullUpEnabled = true;

	/** Policy for pull down and pull up */
	protected PullPolicy mPolicy = new PullPolicy();

	/** Hander for back-rolling */
	Handler updateHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// 回弹速度随下拉距离moveDeltaY增大而增大
			MOVE_SPEED = (float) (8
					+ 5 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
			if (!isTouch) {
				// 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
				if (state == STATE_REFRESHING && pullDownY <= refreshDist) {
					pullDownY = refreshDist + MOVE_SPEED;
					timer.cancel();
				} else if (state == STATE_LOADING && -pullUpY <= loadMoreDist) {
					pullUpY = -loadMoreDist - MOVE_SPEED;
					timer.cancel();
				}
			}
			if (pullDownY > 0)
				pullDownY -= MOVE_SPEED;
			else if (pullUpY < 0)
				pullUpY += MOVE_SPEED;
			if (pullDownY < 0) {
				// 已完成回弹
				pullDownY = 0;
				pullDownView.clearAnimation();
				// 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != STATE_REFRESHING && state != STATE_LOADING)
					changeState(STATAE_INIT);
				timer.cancel();
				requestLayout();
			}
			if (pullUpY > 0) {
				// 已完成回弹
				pullUpY = 0;
				pullUpView.clearAnimation();
				// 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != STATE_REFRESHING && state != STATE_LOADING)
					changeState(STATAE_INIT);
				timer.cancel();
			}
			// 刷新布局,会自动调用onLayout
			requestLayout();
		}
	};

	public PullLayout(Context context) {
		super(context);
		init(context, null);
	}

	public PullLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public PullLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		timer = new MyTimer(updateHandler);
		reverseAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.pull_reverse);

		LayoutInflater inflater = LayoutInflater.from(context);
		refreshHeadView = inflater.inflate(R.layout.pull_refresh_head, null);
		loadmoreFootView = inflater.inflate(R.layout.pull_load_more, null);

		// 初始化下拉布局
		pullDownView = refreshHeadView.findViewById(R.id.pull_icon);
		refreshStateTextView = (TextView) refreshHeadView.findViewById(R.id.state_tv);
		refreshingView = refreshHeadView.findViewById(R.id.refreshing_icon);
		// 初始化上拉布局
		pullUpView = loadmoreFootView.findViewById(R.id.pullup_icon);
		loadStateTextView = (TextView) loadmoreFootView.findViewById(R.id.loadstate_tv);
		loadingView = loadmoreFootView.findViewById(R.id.loading_icon);

		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(R.styleable.PullLayout);
			int textSize = typedArray.getDimensionPixelSize(R.styleable.PullLayout_android_textSize, 0);
			if (textSize != 0) {
				refreshStateTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
				loadStateTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			}
			ColorStateList textColor = typedArray.getColorStateList(R.styleable.PullLayout_android_textColor);
			if (textColor != null) {
				refreshStateTextView.setTextColor(textColor);
				loadStateTextView.setTextColor(textColor);
			}
			typedArray.recycle();
		}

		refreshHeadView.setBackgroundDrawable(this.getBackground());
		loadmoreFootView.setBackgroundDrawable(this.getBackground());
	}

	public void setTextColor(int textColor) {
		refreshStateTextView.setTextColor(textColor);
		loadStateTextView.setTextColor(textColor);
	}

	public void setTextSize(float size) {
		refreshStateTextView.setTextSize(size);
		loadStateTextView.setTextSize(size);
	}

	public PullPolicy getPolicy() {
		return mPolicy;
	}

	public void setPolicy(PullPolicy policy) {
		mPolicy = policy;
	}

	public boolean isPullDownEnabled() {
		return mPullDownEnabled;
	}

	public boolean isPullUpEnabled() {
		return mPullUpEnabled;
	}

	public void setPullDownEnabled(boolean enabled) {
		if (mPullDownEnabled == enabled) {
			return;
		}
		mPullDownEnabled = enabled;
		changeState(STATE_DONE);
		hide();
	}

	public void setPullUpEnabled(boolean enabled) {
		if (mPullUpEnabled == enabled) {
			return;
		}
		mPullUpEnabled = enabled;
		changeState(STATE_DONE);
		hide();
	}

	public View getPullView() {
		return pullView;
	}

	public void ensureHeadFoot() {
		if (this.getChildCount() == 3) {
			if (this.getChildAt(0) != refreshHeadView) {
				throw new UnsupportedOperationException();
			}
			if (this.getChildAt(1) != pullView) {
				throw new UnsupportedOperationException();
			}
			if (this.getChildAt(2) != loadmoreFootView) {
				throw new UnsupportedOperationException();
			}
			return;
		}
		if (this.getChildCount() != 1) {
			throw new UnsupportedOperationException("PullLayout must has only one child view");
		}
		View v = this.getChildAt(0);
		if (v.getLayoutParams().height != LayoutParams.MATCH_PARENT) {
			throw new UnsupportedOperationException();
		}
		pullView = v;
		this.addView(refreshHeadView, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.addView(loadmoreFootView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		ensureHeadFoot();
	}

	private void hide() {
		timer.schedule(5);
	}

	public void onRefreshComplete() {
		changeState(STATE_DONE);
		hide();
	}

	public void onLoadMoreComplete() {
		changeState(STATE_DONE);
		hide();
	}

	private void changeState(int to) {
		state = to;
		switch (state) {
		case STATAE_INIT:
			// 下拉布局初始状态
			refreshingView.setVisibility(View.GONE);
			refreshStateTextView.setText(R.string.pull_to_refresh);
			pullDownView.clearAnimation();
			pullDownView.setVisibility(View.VISIBLE);
			// 上拉布局初始状态
			loadingView.setVisibility(View.GONE);
			loadStateTextView.setText(R.string.pull_up_to_load);
			pullUpView.clearAnimation();
			pullUpView.setVisibility(View.VISIBLE);
			break;
		case STATE_RELEASE_TO_REFRESH:
			// 释放刷新状态
			refreshStateTextView.setText(R.string.pull_release_to_refresh);
			pullDownView.startAnimation(reverseAnimation);
			break;
		case STATE_REFRESHING:
			// 正在刷新状态
			pullDownView.clearAnimation();
			refreshingView.setVisibility(View.VISIBLE);
			pullDownView.setVisibility(View.GONE);
			refreshStateTextView.setText(R.string.pull_refreshing);
			break;
		case STATE_RELEASE_TO_LOAD:
			// 释放加载状态
			loadStateTextView.setText(R.string.pull_release_to_load);
			pullUpView.startAnimation(reverseAnimation);
			break;
		case STATE_LOADING:
			// 正在加载状态
			pullUpView.clearAnimation();
			loadingView.setVisibility(View.VISIBLE);
			pullUpView.setVisibility(View.GONE);
			loadStateTextView.setText(R.string.pull_loading);
			break;
		case STATE_DONE:
			// 刷新或加载完毕，啥都不做
			break;
		}
	}

	/**
	 * 不限制上拉或下拉
	 */
	private void releasePull() {
		canPullDown = true;
		canPullUp = true;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			downY = ev.getY();
			lastY = downY;
			timer.cancel();
			mEvents = 0;
			releasePull();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_POINTER_UP:
			// 过滤多点触碰
			mEvents = -1;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mEvents == 0) {
				if (mPullDownEnabled && (pullDownY > 0
						|| (mPolicy.canPullDown(pullView) && canPullDown && state != STATE_LOADING))) {
					// 可以下拉，正在加载时不能下拉
					// 对实际滑动距离做缩小，造成用力拉的感觉
					pullDownY = pullDownY + (ev.getY() - lastY) / radio;
					if (pullDownY < 0) {
						pullDownY = 0;
						canPullDown = false;
						canPullUp = true;
					}
					if (pullDownY > getMeasuredHeight())
						pullDownY = getMeasuredHeight();
					if (state == STATE_REFRESHING) {
						// 正在刷新的时候触摸移动
						isTouch = true;
					}
				} else if (mPullUpEnabled
						&& (pullUpY < 0 || (mPolicy.canPullUp(pullView) && canPullUp && state != STATE_REFRESHING))) {
					// 可以上拉，正在刷新时不能上拉
					pullUpY = pullUpY + (ev.getY() - lastY) / radio;
					if (pullUpY > 0) {
						pullUpY = 0;
						canPullDown = true;
						canPullUp = false;
					}
					if (pullUpY < -getMeasuredHeight())
						pullUpY = -getMeasuredHeight();
					if (state == STATE_LOADING) {
						// 正在加载的时候触摸移动
						isTouch = true;
					}
				} else
					releasePull();
			} else
				mEvents = 0;
			lastY = ev.getY();
			// 根据下拉距离改变比例
			radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
			requestLayout();
			if (pullDownY > 0) {
				if (pullDownY <= refreshDist && (state == STATE_RELEASE_TO_REFRESH || state == STATE_DONE)) {
					// 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
					changeState(STATAE_INIT);
				}
				if (pullDownY >= refreshDist && state == STATAE_INIT) {
					// 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
					changeState(STATE_RELEASE_TO_REFRESH);
				}
			} else if (pullUpY < 0) {
				// 下面是判断上拉加载的，同上，注意pullUpY是负值
				if (-pullUpY <= loadMoreDist && (state == STATE_RELEASE_TO_LOAD || state == STATE_DONE)) {
					changeState(STATAE_INIT);
				}
				// 上拉操作
				if (-pullUpY >= loadMoreDist && state == STATAE_INIT) {
					changeState(STATE_RELEASE_TO_LOAD);
				}

			}
			// 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
			// Math.abs(pullUpY))就可以不对当前状态作区分了
			if ((pullDownY + Math.abs(pullUpY)) > 8) {
				// 防止下拉过程中误触发长按事件和点击事件
				ev.setAction(MotionEvent.ACTION_CANCEL);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (pullDownY > refreshDist || -pullUpY > loadMoreDist)
				// 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
				isTouch = false;
			if (state == STATE_RELEASE_TO_REFRESH) {
				changeState(STATE_REFRESHING);
				// 刷新操作
				if (mListener != null)
					mListener.onRefresh(this);
			} else if (state == STATE_RELEASE_TO_LOAD) {
				changeState(STATE_LOADING);
				// 加载操作
				if (mListener != null)
					mListener.onLoadMore(this);
			}
			hide();
		default:
			break;
		}
		// 事件分发交给父类
		super.dispatchTouchEvent(ev);
		return true;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (!isLayout) {
			// 这里是第一次进来的时候做一些初始化
			ensureHeadFoot();
			isLayout = true;
			refreshDist = ((ViewGroup) refreshHeadView).getChildAt(0).getMeasuredHeight();
			loadMoreDist = ((ViewGroup) loadmoreFootView).getChildAt(0).getMeasuredHeight();
		}
		// 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
		refreshHeadView.layout(0, (int) (pullDownY + pullUpY) - refreshHeadView.getMeasuredHeight(),
				refreshHeadView.getMeasuredWidth(), (int) (pullDownY + pullUpY));
		pullView.layout(0, (int) (pullDownY + pullUpY), pullView.getMeasuredWidth(),
				(int) (pullDownY + pullUpY) + pullView.getMeasuredHeight());
		loadmoreFootView.layout(0, (int) (pullDownY + pullUpY) + pullView.getMeasuredHeight(),
				loadmoreFootView.getMeasuredWidth(),
				(int) (pullDownY + pullUpY) + pullView.getMeasuredHeight() + loadmoreFootView.getMeasuredHeight());
	}

	public static class PullPolicy {

		public boolean canPullDown(View v) {
			if (v == null) {
				return false;
			}
			if (v instanceof AdapterView<?>) {
				return canPullDown((AdapterView<?>) v);
			}
			if (v instanceof ScrollView) {
				return canPullDown((ScrollView) v);
			}
			if (v instanceof WebView) {
				return canPullDown((WebView) v);
			}
			return true;
		}

		public boolean canPullUp(View v) {
			if (v == null) {
				return false;
			}
			if (v instanceof AdapterView<?>) {
				return canPullUp((AdapterView<?>) v);
			}
			if (v instanceof ScrollView) {
				return canPullUp((ScrollView) v);
			}
			if (v instanceof WebView) {
				return canPullUp((WebView) v);
			}
			return true;
		}

		protected boolean canPullDown(AdapterView<?> v) {
			if (v.getCount() == 0) {
				// 没有item的时候也可以下拉刷新
				return true;
			} else if (v.getFirstVisiblePosition() == 0 && v.getChildAt(0).getTop() >= 0) {
				// 滑到顶部了
				return true;
			} else
				return false;
		}

		protected boolean canPullUp(AdapterView<?> v) {
			if (v.getCount() == 0) {
				// 没有item的时候也可以上拉加载
				return true;
			} else if (v.getLastVisiblePosition() == (v.getCount() - 1)) {
				// 滑到底部了
				if (v.getChildAt(v.getLastVisiblePosition() - v.getFirstVisiblePosition()) != null
						&& v.getChildAt(v.getLastVisiblePosition() - v.getFirstVisiblePosition()).getBottom() <= v
								.getMeasuredHeight())
					return true;
			}
			return false;
		}

		protected boolean canPullDown(ScrollView v) {
			if (v.getScrollY() == 0)
				return true;
			else
				return false;
		}

		protected boolean canPullUp(ScrollView v) {
			if (v.getScrollY() >= (v.getChildAt(0).getHeight() - v.getMeasuredHeight()))
				return true;
			else
				return false;
		}

		protected boolean canPullDown(WebView v) {
			if (v.getScrollY() == 0)
				return true;
			else
				return false;
		}

		protected boolean canPullUp(WebView v) {
			if (v.getScrollY() >= Math.floor(v.getContentHeight() * v.getScale() - v.getMeasuredHeight()))
				return true;
			else
				return false;
		}

	}

	class MyTimer {
		private Handler handler;
		private Timer timer;
		private MyTask mTask;

		public MyTimer(Handler handler) {
			this.handler = handler;
			timer = new Timer();
		}

		public void schedule(long period) {
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
			mTask = new MyTask(handler);
			timer.schedule(mTask, 0, period);
		}

		public void cancel() {
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
		}

		class MyTask extends TimerTask {
			private Handler handler;

			public MyTask(Handler handler) {
				this.handler = handler;
			}

			@Override
			public void run() {
				handler.obtainMessage().sendToTarget();
			}

		}
	}

}
