package com.sothree.slidinguppanel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.mutils.app.R;

public class SlidingUpPanelLayout extends ViewGroup {

    /**
     * Default peeking out panel height
     */
    private static final int DEFAULT_PANEL_HEIGHT = 48; // dp;

    /**
     * Default anchor point height
     */
    private static final float DEFAULT_ANCHOR_POINT = 1.0f; // In relative %

    /**
     * Default initial state for the component
     */
    private static PanelState DEFAULT_SLIDE_STATE = PanelState.COLLAPSED;

    /**
     * Default Minimum velocity that will be detected as a fling
     */
    private static final int DEFAULT_MIN_FLING_VELOCITY = 600; // dips per second

    /**
     * Minimum velocity that will be detected as a fling
     */
    private int mMinFlingVelocity = DEFAULT_MIN_FLING_VELOCITY;

    /**
     * The size of the overhang in pixels.
     */
    private int mPanelHeight = -1;

    /**
     * If provided, the panel can be dragged by only this view. Otherwise, the entire panel can be
     * used for dragging.
     */
    private View mDragView;

    /**
     * If provided, the panel can be dragged by only this view. Otherwise, the entire panel can be
     * used for dragging.
     */
    private int mDragViewResId = -1;

    /**
     * The child view that can slide, if any.
     */
    private View mSlideableView;

    /**
     * The main view
     */
    private View mMainView;

    /**
     * Current state of the slideable view.
     */
    public enum PanelState {
        EXPANDED,
        COLLAPSED,
        ANCHORED,
        HIDDEN,
        DRAGGING
    }

    private PanelState mSlideState = DEFAULT_SLIDE_STATE;

    /**
     * If the current slide state is DRAGGING, this will store the last non dragging state
     */
    private PanelState mLastNotDraggingSlideState = DEFAULT_SLIDE_STATE;

    /**
     * 上一次不包含影藏的状态
     */
    private PanelState mLastNotHiddenSlideState = DEFAULT_SLIDE_STATE;

    /**
     * How far the panel is offset from its expanded position. range [0, 1] where 0 = collapsed, 1 =
     * expanded.
     */
    private float mSlideOffset;

    /**
     * How far in pixels the slideable panel may move.
     */
    private int mSlideRange;

    /**
     * An anchor point where the panel can stop during sliding
     */
    private float mAnchorPoint = 1.f;

    /**
     * Flag indicating that sliding feature is enabled\disabled
     */
    private boolean mDragEnabled = true;

    private float mInitialMotionX;
    private float mInitialMotionY;
    private boolean mActionDownOnSlideableView;

    private List<PanelSlideListener> mPanelSlideListeners = new ArrayList<>();

    private final ViewDragHelper mDragHelper;

    /**
     * Stores whether or not the pane was expanded the last time it was slideable. If
     * expand/collapse operations are invoked this state is modified. Used by instance state
     * save/restore.
     */
    private boolean mFirstLayout = true;

    /**
     * 可滑动区域补白，可以为负数
     */
    private int mSlideRangePadding;

    /**
     * 悬挂高度，该高度相对于控件本身
     */
    private int mAnchorHeight = -1;

    /**
     * 悬挂位置，相对于父控件的百分比
     */
    private float mAnchorPointInParent = 1.0f;

    /**
     * 展开点高度
     */
    private int mExpandHeight = -1;

    /**
     * 展开位置，滑动到相对于父控件本身的该比例值时自动执行EXPANDED状态切换
     */
    private float mExpandPointInParent = 1.0f;

    /**
     * 展开位置，滑动到相对于SlideRange区域的该百分比自动执行EXPANDED状态切换
     *
     * @see #mAnchorPoint
     */
    private float mExpandPoint = 1.0f;

    /**
     * 拦截横向拖拽事件
     *
     * 设置为true即可拦截SlideableView中的HorizontalScrollView、ViewPager等组件的横向滑动事件
     */
    private boolean mDragHorizontalIntercept;

    /**
     * 拦截拖拽事件的滑动阀值
     */
    private int mInterceptTouchSlop;

    /**
     * 设置是否在DRAGGING状态下捕捉UI事件
     *
     * 是否在代码调用设置状态之后响应触摸事件
     */
    private boolean mCaptureViewOnDraggingState = true;

    /**
     * SlideOffset误差
     *
     * 由于SlideOffset转换成Top值存在float与int换算的强制转换存在误差
     */
    private float mSlideOffsetSlop;

    /**
     * 额外的悬挂高度
     */
    private ArrayList<Integer> mAnchorHeightExtras = new ArrayList<>();

    /**
     * 正在作用的额外悬挂高度索引
     */
    private int mAnchorHeightExtraIndex = -1;

    /**
     * 额外的悬挂点
     */
    private ArrayList<Float> mAnchorPointExtras = new ArrayList<>();

    /**
     * ScrollView滚动到顶部
     */
    private boolean mScrollAtTop;

    /**
     * ScrollView引用
     */
    private View mScrollableView;

    /**
     * Listener for monitoring events about sliding panes.
     */
    public interface PanelSlideListener {
        /**
         * Called when a sliding pane's position changes.
         *
         * @param panel       The child view that was moved
         * @param slideOffset The new offset of this sliding pane within its range, from 0-1
         */
        public void onPanelSlide(View panel, float slideOffset);

        /**
         * Called when a sliding panel state changes
         *
         * @param panel The child view that was slid to an collapsed position
         */
        public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState);
    }

    /**
     * No-op stubs for {@link PanelSlideListener}. If you only want to implement a subset of the
     * listener methods you can extend this instead of implement the full interface.
     */
    public static class SimplePanelSlideListener implements PanelSlideListener {

        @Override
        public void onPanelSlide(View panel, float slideOffset) {

        }

        @Override
        public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
            switch (newState) {
                case COLLAPSED:
                    onPanelCollapsed(panel);
                    break;
                case ANCHORED:
                    onPanelAnchored(panel);
                    break;
                case EXPANDED:
                    onPanelExpanded(panel);
                    break;
                case HIDDEN:
                    onPanelHidden(panel);
                    break;
            }
        }

        public void onPanelCollapsed(View panel) {

        }

        public void onPanelAnchored(View panel) {

        }

        public void onPanelExpanded(View panel) {

        }

        public void onPanelHidden(View panel) {

        }

    }

    public SlidingUpPanelLayout(Context context) {
        this(context, null);
    }

    public SlidingUpPanelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingUpPanelLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (isInEditMode()) {
            mDragHelper = null;
            return;
        }

        float sensitivity = 5f;
        Interpolator scrollerInterpolator = null;
        if (attrs != null) {

            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlidingUpPanelLayout);

            if (ta != null) {
                mPanelHeight = ta.getDimensionPixelSize(R.styleable.SlidingUpPanelLayout_panelHeight, -1);

                mMinFlingVelocity = ta.getInt(R.styleable.SlidingUpPanelLayout_flingVelocity, DEFAULT_MIN_FLING_VELOCITY);

                mDragViewResId = ta.getResourceId(R.styleable.SlidingUpPanelLayout_dragView, -1);

                mAnchorPoint = ta.getFloat(R.styleable.SlidingUpPanelLayout_anchorPoint, DEFAULT_ANCHOR_POINT);

                mSlideState = PanelState.values()[ta.getInt(R.styleable.SlidingUpPanelLayout_initialState, DEFAULT_SLIDE_STATE.ordinal())];

                int interpolatorResId = ta.getResourceId(R.styleable.SlidingUpPanelLayout_scrollInterpolator, -1);
                if (interpolatorResId != -1) {
                    scrollerInterpolator = AnimationUtils.loadInterpolator(context, interpolatorResId);
                }

                mSlideRangePadding = ta.getDimensionPixelOffset(R.styleable.SlidingUpPanelLayout_slideRangePadding, 0);
                mAnchorHeight = ta.getDimensionPixelSize(R.styleable.SlidingUpPanelLayout_anchorHeight, -1);
                mExpandHeight = ta.getDimensionPixelOffset(R.styleable.SlidingUpPanelLayout_expandHeight, -1);
                mExpandPointInParent = ta.getFloat(R.styleable.SlidingUpPanelLayout_expandPointInParent, 1.0f);
                mExpandPoint = ta.getFloat(R.styleable.SlidingUpPanelLayout_expandPoint, 1.0f);
                mAnchorPointInParent = ta.getFloat(R.styleable.SlidingUpPanelLayout_anchorPointInParent, 1.0f);
                sensitivity = ta.getFloat(R.styleable.SlidingUpPanelLayout_sensitivity, sensitivity);
            }

            ta.recycle();
        }

        final float density = context.getResources().getDisplayMetrics().density;
        if (mPanelHeight == -1) {
            mPanelHeight = (int) (DEFAULT_PANEL_HEIGHT * density + 0.5f);
        }

        mDragHelper = ViewDragHelper.create(this, sensitivity, scrollerInterpolator, new DragHelperCallback());
        mDragHelper.setMinVelocity(mMinFlingVelocity * density);

        if (mAnchorPoint != 1.0f) {
            mAnchorPointInParent = 1.0f;
        }
        if (mAnchorHeight != -1 && mAnchorHeight > mPanelHeight) {
            mAnchorPoint = mAnchorPointInParent = 1;
        }
        if (mExpandPoint != 1.0f) {
            mExpandPointInParent = 1.0f;
        }
        if (mExpandHeight != -1 && mExpandHeight > mPanelHeight) {
            mExpandPoint = mExpandPointInParent = 1;
        }

        mInterceptTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    /**
     * Set the Drag View after the view is inflated
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mDragViewResId != -1) {
            setDragView(findViewById(mDragViewResId));
        }
        mSlideableView = getChildCount() == 2 ? getChildAt(1) : null;
    }

    public boolean isScrollAtTop() {
        return mScrollAtTop;
    }

    public void setScrollAtTop(boolean scrollAtTop) {
        setScrollAtTop(scrollAtTop, null);
    }

    public void setScrollAtTop(boolean scrollAtTop, View scrollableView) {
        mScrollAtTop = scrollAtTop;
        mScrollableView = scrollableView;
    }

    public boolean isCaptureViewOnDraggingState() {
        return mCaptureViewOnDraggingState;
    }

    public void setCaptureViewOnDraggingState(boolean captureViewOnDraggingState) {
        mCaptureViewOnDraggingState = captureViewOnDraggingState;
    }

    public boolean isDragHorizontalIntercept() {
        return mDragHorizontalIntercept;
    }

    public void setDragHorizontalIntercept(boolean intercept) {
        mDragHorizontalIntercept = intercept;
    }

    /**
     * 获取滑动区域补白
     */
    public int getSlideRangePadding() {
        return mSlideRangePadding;
    }

    /**
     * 设置滑动区域补白
     */
    public void setSlideRangePadding(int value) {
        if (mSlideRangePadding == value) {
            return;
        }
        mSlideRangePadding = value;
        requestLayout();
    }

    /**
     * 获取当前滑动高度，即SlideableView露出的高度
     */
    public int getSlideOffsetHeight() {
        return (int) (mSlideOffset * mSlideRange + mPanelHeight);
    }

    /**
     * Set sliding enabled flag
     *
     * @param enabled flag value
     */
    public void setDragEnabled(boolean enabled) {
        mDragEnabled = enabled;
    }

    public boolean isDragEnabled() {
        return mDragEnabled && mSlideableView != null && mSlideState != PanelState.HIDDEN;
    }

    /**
     * Set the collapsed panel height in pixels
     *
     * @param val A height in pixels
     */
    public void setPanelHeight(int val) {
        if (getPanelHeight() == val) {
            return;
        }

        mPanelHeight = val;
        requestLayout();
    }

    /**
     * @return The current collapsed panel height
     */
    public int getPanelHeight() {
        return mPanelHeight;
    }

    /**
     * @return The current minimin fling velocity
     */
    public int getMinFlingVelocity() {
        return mMinFlingVelocity;
    }

    /**
     * Sets the minimum fling velocity for the panel
     *
     * @param val the new value
     */
    public void setMinFlingVelocity(int val) {
        mMinFlingVelocity = val;
    }

    /**
     * Adds a panel slide listener
     */
    public void addPanelSlideListener(PanelSlideListener listener) {
        mPanelSlideListeners.add(listener);
    }

    /**
     * Removes a panel slide listener
     */
    public void removePanelSlideListener(PanelSlideListener listener) {
        mPanelSlideListeners.remove(listener);
    }

    public int getViewDragState() {
        return mDragHelper.getViewDragState();
    }

    public View getSlideableView() {
        return mSlideableView;
    }

    public View getDragView() {
        return mDragView;
    }

    /**
     * Set the draggable view portion. Use to null, to allow the whole panel to be draggable
     *
     * @param dragView A view that will be used to drag the panel.
     */
    public void setDragView(View dragView) {
        mDragView = dragView;
    }

    /**
     * Set the draggable view portion. Use to null, to allow the whole panel to be draggable
     *
     * @param dragViewResId The resource ID of the new drag view
     */
    public void setDragView(int dragViewResId) {
        mDragViewResId = dragViewResId;
        setDragView(findViewById(dragViewResId));
    }

    public List<Integer> getAnchorHeightExtras() {
        return new ArrayList<>(mAnchorHeightExtras);
    }

    public void setAnchorHeightExtras(List<Integer> anchorHeightExtras) {
        if (anchorHeightExtras == null || anchorHeightExtras.size() == 0) {
            if (mAnchorHeightExtras.size() == 0) {
                return;
            }
        }
        //将传入的悬挂高度配置信息进行排序
        ArrayList<Integer> sortedExtras = new ArrayList<>(anchorHeightExtras);
        if (sortedExtras.size() != 0) {
            for (Integer extra : sortedExtras) {
                if (extra == null) {
                    throw new IllegalArgumentException("element can not be null");
                }
            }
            Collections.sort(sortedExtras, new Comparator<Integer>() {
                @Override
                public int compare(Integer lhs, Integer rhs) {
                    return lhs - rhs;
                }
            });
            mAnchorHeightExtras.clear();
            mAnchorHeightExtras.addAll(sortedExtras);
        } else {
            mAnchorHeightExtras.clear();
        }
        requestLayout();
        return;
    }

    public float getExpandPoint() {
        return mExpandPoint;
    }

    public void setExpandPoint(float expandPoint) {
        setExpandPoint(expandPoint, true);
    }

    private void setExpandPoint(float expandPoint, boolean resetMode) {
        if (expandPoint > 0 && expandPoint <= 1) {
            if (resetMode) {
                mExpandHeight = -1;
                mExpandPointInParent = 1;
            }
            mExpandPoint = expandPoint;
        }
    }

    public int getExpandHeight() {
        if (mExpandHeight == -1) {
            return (int) (mExpandPoint * mSlideRange + mPanelHeight + getPaddingBottom());
        }
        return mExpandHeight;
    }

    public void setExpandHeight(int expandHeight) {
        if (mExpandHeight == expandHeight) {
            return;
        }
        mExpandHeight = expandHeight;
        mExpandPoint = mExpandPointInParent = 1;
        requestLayout();
    }

    /**
     * 获取当前正在作用的额外悬挂高度
     */
    public int getAnchorHeightExtraNow() {
        if (mSlideState == PanelState.ANCHORED) {
            if (mAnchorHeightExtraIndex >= 0 && mAnchorHeightExtraIndex < mAnchorHeightExtras.size()) {
                return mAnchorHeightExtras.get(mAnchorHeightExtraIndex);
            }
        }
        return -1;
    }

    public int getAnchorHeight() {
        if (mAnchorHeight == -1) {
            return (int) (mAnchorPoint * mSlideRange + mPanelHeight + getPaddingBottom());
        }
        return mAnchorHeight;
    }

    public void setAnchorHeight(int anchorHeight) {
        if (mAnchorHeight == anchorHeight) {
            return;
        }
        mAnchorHeight = anchorHeight;
        mAnchorPoint = mAnchorPointInParent = 1;
        requestLayout();
    }

    private void setAnchorPoint(float anchorPoint, boolean resetMode) {
        if (anchorPoint > 0 && anchorPoint <= 1) {
            if (resetMode) {
                mAnchorHeight = -1;
                mAnchorPointInParent = 1;
            }
            mAnchorPoint = anchorPoint;
            if (resetMode) {
                mFirstLayout = true;
                requestLayout();
            }
        }
    }

    /**
     * Set an anchor point where the panel can stop during sliding
     *
     * @param anchorPoint A value between 0 and 1, determining the position of the anchor point
     *                    starting from the top of the layout.
     */
    public void setAnchorPoint(float anchorPoint) {
        setAnchorPoint(anchorPoint, true);
    }

    /**
     * Gets the currently set anchor point
     *
     * @return the currently set anchor point
     */
    public float getAnchorPoint() {
        return mAnchorPoint;
    }

    void dispatchOnPanelSlide(View panel) {
        for (PanelSlideListener l : mPanelSlideListeners) {
            l.onPanelSlide(panel, mSlideOffset);
        }
    }

    void dispatchOnPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
        for (PanelSlideListener l : mPanelSlideListeners) {
            l.onPanelStateChanged(panel, previousState, newState);
        }
        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
    }

    void updateObscuredViewVisibility() {
        if (getChildCount() == 0) {
            return;
        }
        final int leftBound = getPaddingLeft();
        final int rightBound = getWidth() - getPaddingRight();
        final int topBound = getPaddingTop();
        final int bottomBound = getHeight() - getPaddingBottom();
        final int left;
        final int right;
        final int top;
        final int bottom;
        if (mSlideableView != null && hasOpaqueBackground(mSlideableView)) {
            left = mSlideableView.getLeft();
            right = mSlideableView.getRight();
            top = mSlideableView.getTop();
            bottom = mSlideableView.getBottom();
        } else {
            left = right = top = bottom = 0;
        }
        View child = getChildAt(0);
        final int clampedChildLeft = Math.max(leftBound, child.getLeft());
        final int clampedChildTop = Math.max(topBound, child.getTop());
        final int clampedChildRight = Math.min(rightBound, child.getRight());
        final int clampedChildBottom = Math.min(bottomBound, child.getBottom());
        final int vis;
        if (clampedChildLeft >= left && clampedChildTop >= top &&
                clampedChildRight <= right && clampedChildBottom <= bottom) {
            vis = INVISIBLE;
        } else {
            vis = VISIBLE;
        }
        child.setVisibility(vis);
    }

    void setAllChildrenVisible() {
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == INVISIBLE) {
                child.setVisibility(VISIBLE);
            }
        }
    }

    private static boolean hasOpaqueBackground(View v) {
        final Drawable bg = v.getBackground();
        return bg != null && bg.getOpacity() == PixelFormat.OPAQUE;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mFirstLayout = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFirstLayout = true;
    }

    /**
     * 计算额外悬挂点百分比，相对于SlideRange区域
     *
     * @param heightSize 测量高度
     */
    private void computeAnchorPointExtras(int heightSize) {
        int sizeOfHeights = mAnchorHeightExtras.size();
        if (mAnchorPointExtras.size() != sizeOfHeights) {
            mAnchorPointExtras.clear();
        }
        float slideRange = heightSize - mPanelHeight - getPaddingTop() - getPaddingBottom() - mSlideRangePadding;
        for (int i = 0; i < sizeOfHeights; i++) {
            float point = 1.0f;
            if (slideRange != 0) {
                point = (mAnchorHeightExtras.get(i) - mPanelHeight) / slideRange;
                point = (point > 0 && point <= 1) ? point : 1.0f;
            }
            if (i == mAnchorPointExtras.size()) {
                mAnchorPointExtras.add(point);
            } else {
                mAnchorPointExtras.set(i, point);
            }
        }
    }

    /**
     * 计算悬挂点百分比，相对于SlideRange区域
     *
     * @param heightSize 测量高度
     */
    private void computeAnchorPoint(int heightSize) {
        float slideRange = heightSize - mPanelHeight - getPaddingTop() - getPaddingBottom() - mSlideRangePadding;
        if (slideRange != 0) {
            float anchorPoint = (mAnchorHeight - mPanelHeight) / slideRange;
            if (anchorPoint != mAnchorPoint) {
                setAnchorPoint(anchorPoint, false);
            }
        }
    }

    /**
     * 根据父控件悬挂百分比计算当前的悬挂百分比
     *
     * @param anchorPointInParent 该百分比包含panelHeight大小
     */
    private void computeAnchorPoint(float anchorPointInParent) {
        if (mSlideRange == 0) {
            return;
        }
        int heightSize = getMeasuredHeight();
        if (heightSize == 0) {
            return;
        }
        float anchorPoint = pointInParentToSlideRange(anchorPointInParent, heightSize);
        if (anchorPoint != mAnchorPoint) {
            setAnchorPoint(anchorPoint, false);
        }
    }

    private void computeExpandPoint(int heightSize) {
        float slideRange = heightSize - mPanelHeight - getPaddingTop() - getPaddingBottom() - mSlideRangePadding;
        if (slideRange != 0) {
            float expandPoint = (mExpandHeight - mPanelHeight) / slideRange;
            if (expandPoint != mExpandPoint) {
                setExpandPoint(expandPoint, false);
            }
        }
    }

    /**
     * 计算展开点百分比
     */
    private void computeExpandPoint(float expandPointInParent) {
        if (mSlideRange == 0) {
            return;
        }
        int heightSize = getMeasuredHeight();
        if (heightSize == 0) {
            return;
        }
        float expandPoint = pointInParentToSlideRange(expandPointInParent, heightSize);
        if (expandPoint != mExpandPoint) {
            mExpandPoint = expandPoint;
        }
    }

    /**
     * 将父组件中Point转换成滑动区域中的Point
     *
     * @param pointInParent 父组件中的Point
     * @param heightSize    当前组件的测量高度
     */
    private float pointInParentToSlideRange(float pointInParent, int heightSize) {
        int topMarginInParent = 0;
        int bottomMarginInParent = 0;
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params != null && params.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            if (params instanceof MarginLayoutParams) {
                MarginLayoutParams marginParams = (MarginLayoutParams) params;
                topMarginInParent = marginParams.topMargin;
                bottomMarginInParent = marginParams.bottomMargin;
            }
        }
        float anchorPointWidthPanel = (pointInParent * (heightSize + topMarginInParent + bottomMarginInParent)
                - bottomMarginInParent) / heightSize; //加上PanelHeight的悬挂点
        float point = (anchorPointWidthPanel * heightSize - mPanelHeight - getPaddingTop() - getPaddingBottom() - mSlideRangePadding) / mSlideRange;
        return point;
    }

    /**
     * 计算滑动区域
     */
    private void computeSlideRange() {
        mSlideRange = getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - mSlideRangePadding;
    }

    private void onMeasureComplete(int heightSize) {
        if (mAnchorPointInParent != 1) {
            computeAnchorPoint(mAnchorPointInParent);
        } else {
            if (mAnchorHeight != -1) {
                computeAnchorPoint(heightSize);
            }
        }
        if (mExpandPointInParent != 1) {
            computeExpandPoint(mExpandPointInParent);
        } else {
            if (mExpandHeight != -1) {
                computeExpandPoint(heightSize);
            }
        }
        mSlideOffsetSlop = mSlideRange != 0 ? 1.0f / mSlideRange : 0.001f;
        if (mAnchorHeightExtras.size() != 0) {
            computeAnchorPointExtras(heightSize);
        }
    }

    @SuppressWarnings("Range")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
        } else if (heightMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("Height must have an exact value or MATCH_PARENT");
        }

        final int childCount = getChildCount();

        if (childCount != 2) {
            throw new IllegalStateException("Sliding up panel layout must have exactly 2 children!");
        }

        mMainView = getChildAt(0);
        mSlideableView = getChildAt(1);
        if (mDragView == null) {
            setDragView(mSlideableView);
        }

        // If the sliding panel is not visible, then put the whole view in the hidden state
        if (mSlideableView.getVisibility() != VISIBLE) {
            mSlideState = PanelState.HIDDEN;
        }

        int layoutHeight = heightSize - getPaddingTop() - getPaddingBottom();
        int layoutWidth = widthSize - getPaddingLeft() - getPaddingRight();

        // First pass. Measure based on child LayoutParams width/height.
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            // We always measure the sliding panel in order to know it's height (needed for show panel)
            if (child.getVisibility() == GONE && i == 0) {
                continue;
            }

            int height = layoutHeight;
            int width = layoutWidth;
            if (child == mMainView) {
                if (mSlideState != PanelState.HIDDEN) {
                    height -= mPanelHeight;
                }

                width -= lp.leftMargin + lp.rightMargin;
            } else if (child == mSlideableView) {
                // The slideable view should be aware of its top margin.
                // See https://github.com/umano/AndroidSlidingUpPanel/issues/412.
                height -= lp.topMargin;
            }

            int childWidthSpec;
            if (lp.width == LayoutParams.WRAP_CONTENT) {
                childWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);
            } else if (lp.width == LayoutParams.MATCH_PARENT) {
                childWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            } else {
                childWidthSpec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
            }

            int childHeightSpec;
            if (lp.height == LayoutParams.WRAP_CONTENT) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
            } else {
                // Modify the height based on the weight.
                if (lp.weight > 0 && lp.weight < 1) {
                    height = (int) (height * lp.weight);
                } else if (lp.height != LayoutParams.MATCH_PARENT) {
                    height = lp.height;
                }
                childHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            }

            child.measure(childWidthSpec, childHeightSpec);

            if (child == mSlideableView) {
                mSlideRange = mSlideableView.getMeasuredHeight() - mPanelHeight - mSlideRangePadding;
                mSlideRange = mSlideRange > 0 ? mSlideRange : 1;
            }
        }

        setMeasuredDimension(widthSize, heightSize);
        onMeasureComplete(heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();

        final int childCount = getChildCount();

        if (mFirstLayout) {
            switch (mSlideState) {
                case EXPANDED:
                    mSlideOffset = 1.0f;
                    break;
                case ANCHORED:
                    if (mAnchorHeightExtraIndex >= 0 && mAnchorHeightExtraIndex < mAnchorPointExtras.size()) {
                        mSlideOffset = mAnchorPointExtras.get(mAnchorHeightExtraIndex);
                    } else {
                        mSlideOffset = mAnchorPoint;
                    }
                    break;
                case HIDDEN:
                    int newTop = computePanelTopPosition(0.0f) + mPanelHeight;
                    mSlideOffset = computeSlideOffset(newTop);
                    break;
                default:
                    mSlideOffset = 0.f;
                    break;
            }
        }

        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            // Always layout the sliding view on the first layout
            if (child.getVisibility() == GONE && (i == 0 || mFirstLayout)) {
                continue;
            }

            final int childHeight = child.getMeasuredHeight();
            int childTop = paddingTop;

            if (child == mSlideableView) {
                childTop = computePanelTopPosition(mSlideOffset);
            }

            final int childBottom = childTop + childHeight;
            final int childLeft = paddingLeft + lp.leftMargin;
            final int childRight = childLeft + child.getMeasuredWidth();

            child.layout(childLeft, childTop, childRight, childBottom);
        }

        if (mFirstLayout) {
            updateObscuredViewVisibility();
        }

        mFirstLayout = false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Recalculate sliding panes and their details
        if (h != oldh) {
            mFirstLayout = true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnabled() || !isDragEnabled()) {
            mDragHelper.cancel();
            return onInterceptTouchEventHorizontal(ev);
        }

        if (!mCaptureViewOnDraggingState) {
            if (mDragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE) {
                return onInterceptTouchEventHorizontal(ev);
            }
        }

        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }

        final float x = ev.getX();
        final float y = ev.getY();
        final int dragSlop = mDragHelper.getTouchSlop();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mInitialMotionX = x;
                mInitialMotionY = y;
                mActionDownOnSlideableView = y > mSlideableView.getTop();
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Intercept Scrollable view events
                if (mScrollAtTop) {
                    float dy = y - mInitialMotionY;
                    if (dy >= dragSlop) {
                        mDragHelper.shouldInterceptTouchEvent(ev);
                        return true;
                    } else if (dy < -dragSlop) {
                        if (!isViewUnder(mDragView, (int) mInitialMotionX, (int) mInitialMotionY)) {
                            if (mScrollableView == null || (mScrollableView != null && mScrollableView.getScrollY() != 0)) {
                                mScrollAtTop = false;
                                return onInterceptTouchEventHorizontal(ev);
                            }
                        }
                    }
                }

                if (!isViewUnder(mDragView, (int) mInitialMotionX, (int) mInitialMotionY)) {
                    mDragHelper.cancel();
                    return onInterceptTouchEventHorizontal(ev);
                }
                break;
            }
        }
        return mDragHelper.shouldInterceptTouchEvent(ev) || onInterceptTouchEventHorizontal(ev);
    }

    /**
     * 拦截横向滑动
     */
    private boolean onInterceptTouchEventHorizontal(MotionEvent ev) {
        if (mActionDownOnSlideableView && mDragHorizontalIntercept) {
            if (ev.getActionMasked() == MotionEvent.ACTION_MOVE) {
                return Math.abs(ev.getX() - mInitialMotionX) > mInterceptTouchSlop;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isEnabled() || !isDragEnabled()) {
            return super.onTouchEvent(ev);
        }
        try {
            mDragHelper.processTouchEvent(ev);
            return mActionDownOnSlideableView;
        } catch (Exception ex) {
            return mActionDownOnSlideableView;
        }
    }

    private boolean isViewUnder(View view, int x, int y) {
        if (view == null) return false;
        int[] viewLocation = new int[2];
        view.getLocationOnScreen(viewLocation);
        int[] parentLocation = new int[2];
        this.getLocationOnScreen(parentLocation);
        int screenX = parentLocation[0] + x;
        int screenY = parentLocation[1] + y;
        return screenX >= viewLocation[0] && screenX < viewLocation[0] + view.getWidth() &&
                screenY >= viewLocation[1] && screenY < viewLocation[1] + view.getHeight();
    }

    /*
     * Computes the top position of the panel based on the slide offset.
     */
    private int computePanelTopPosition(float slideOffset) {
        int slidingViewHeight = mSlideableView != null ? mSlideableView.getMeasuredHeight() : 0;
        int slidePixelOffset = (int) (slideOffset * mSlideRange);
        // Compute the top of the panel if its collapsed
        return getMeasuredHeight() - getPaddingBottom() - mPanelHeight - slidePixelOffset;
    }

    /*
     * Computes the slide offset based on the top position of the panel
     */
    private float computeSlideOffset(int topPosition) {
        // Compute the panel top position if the panel is collapsed (offset 0)
        final int topBoundCollapsed = computePanelTopPosition(0);

        // Determine the new slide offset based on the collapsed top position and the new required
        // top position
        return (float) (topBoundCollapsed - topPosition) / mSlideRange;
    }

    public PanelState getLastPanelState() {
        return mLastNotDraggingSlideState;
    }

    /**
     * Returns the current state of the panel as an enum.
     *
     * @return the current panel state
     */
    public PanelState getPanelState() {
        return mSlideState;
    }

    /**
     * Change panel state to the given state with
     *
     * @param state - new panel state
     */
    public void setPanelState(PanelState state) {
        setPanelState(state, -1);
    }

    private void setPanelStateInternal(PanelState state) {
        if (mSlideState == state && mSlideState != PanelState.ANCHORED) return;
        PanelState oldState = mSlideState;
        mSlideState = state;
        beforePanelStateChangedDispatched();
        if (mFirstLayout) {
            return;
        }
        dispatchOnPanelStateChanged(this, oldState, state);
    }

    private void beforePanelStateChangedDispatched() {
        mLastNotHiddenSlideState = mSlideState != PanelState.HIDDEN ? mSlideState : mLastNotHiddenSlideState;
        if (mSlideState != PanelState.ANCHORED) {
            return;
        }
        if (mAnchorHeightExtraIndex >= 0 && mAnchorHeightExtraIndex < mAnchorHeightExtras.size()) {
            if (mAnchorPointExtras.get(mAnchorHeightExtraIndex) != getSlideOffsetHeight()) {
                mAnchorHeightExtraIndex = -1;
            }
        } else {
            mAnchorHeightExtraIndex = -1;
        }
    }

    /**
     * 扩展设置状态
     *
     * @param state             状态
     * @param anchorHeightExtra 默认值为-1，该值需要提前预设
     */
    public void setPanelState(PanelState state, int anchorHeightExtra) {
        if (state == null || state == PanelState.DRAGGING) {
            return;
        }
        if (!isEnabled()
                || (!mFirstLayout && mSlideableView == null)
                || (state == mSlideState && state != PanelState.ANCHORED && anchorHeightExtra != -1)
                || mSlideState == PanelState.DRAGGING) return;

        if (state == PanelState.ANCHORED) {
            if (mSlideState == PanelState.ANCHORED) {
                if (anchorHeightExtra != -1 && anchorHeightExtra == getAnchorHeightExtraNow()) {
                    return;
                }
            }
            mAnchorHeightExtraIndex = mAnchorHeightExtras.indexOf(anchorHeightExtra);
        }

        if (mFirstLayout) {
            setPanelStateInternal(state);
        } else {
            if (mSlideState == PanelState.HIDDEN) {
                mSlideableView.setVisibility(View.VISIBLE);
                requestLayout();
            }
            switch (state) {
                case ANCHORED:
                    float point = 1.0f;
                    if (mAnchorHeightExtraIndex >= 0 && mAnchorHeightExtraIndex < mAnchorPointExtras.size()) {
                        point = mAnchorPointExtras.get(mAnchorHeightExtraIndex);
                    } else {
                        point = mAnchorPoint;
                    }
                    smoothSlideTo(point, 0);
                    break;
                case COLLAPSED:
                    smoothSlideTo(0, 0);
                    break;
                case EXPANDED:
                    smoothSlideTo(1.0f, 0);
                    break;
                case HIDDEN:
                    int newTop = computePanelTopPosition(0.0f) + mPanelHeight;
                    smoothSlideTo(computeSlideOffset(newTop), 0);
                    break;
            }
        }
    }

    public void showPanel() {
        setPanelState(mLastNotHiddenSlideState);
    }

    public void hidePanel() {
        setPanelState(PanelState.HIDDEN);
    }

    private void onPanelDragged(int newTop) {
        mLastNotDraggingSlideState = mSlideState;
        setPanelStateInternal(PanelState.DRAGGING);
        // Recompute the slide offset based on the new top position
        mSlideOffset = computeSlideOffset(newTop);
        // Dispatch the slide event
        dispatchOnPanelSlide(mSlideableView);
    }

    /**
     * Smoothly animate mDraggingPane to the target X position within its range.
     *
     * @param slideOffset position to animate to
     * @param velocity    initial velocity in case of fling, or 0.
     */
    boolean smoothSlideTo(float slideOffset, int velocity) {
        if (!isEnabled() || mSlideableView == null) {
            // Nothing to do.
            return false;
        }

        int panelTop = computePanelTopPosition(slideOffset);
        if (mDragHelper.smoothSlideViewTo(mSlideableView, mSlideableView.getLeft(), panelTop)) {
            setAllChildrenVisible();
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper != null && mDragHelper.continueSettling(true)) {
            if (!isEnabled()) {
                mDragHelper.abort();
                return;
            }

            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof MarginLayoutParams
                ? new LayoutParams((MarginLayoutParams) p)
                : new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams && super.checkLayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        if (mSlideState != PanelState.DRAGGING) {
            ss.mSlideState = mSlideState;
        } else {
            ss.mSlideState = mLastNotDraggingSlideState;
        }
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mSlideState = ss.mSlideState != null ? ss.mSlideState : DEFAULT_SLIDE_STATE;
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            if (!mCaptureViewOnDraggingState && mSlideState == PanelState.DRAGGING) {
                return false;
            }

            return child == mSlideableView;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            if (mDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE) {
                mSlideOffset = computeSlideOffset(mSlideableView.getTop());

                if (mSlideOffset == 1) {
                    updateObscuredViewVisibility();
                    setPanelStateInternal(PanelState.EXPANDED);
                } else if (mSlideOffset == 0) {
                    setPanelStateInternal(PanelState.COLLAPSED);
                } else if (mSlideOffset < 0) {
                    setPanelStateInternal(PanelState.HIDDEN);
                    mSlideableView.setVisibility(View.INVISIBLE);
                } else {
                    if (mAnchorPoint != 1 && Math.abs(mSlideOffset - mAnchorPoint) <= mSlideOffsetSlop) {
                        mAnchorHeightExtraIndex = -1;
                        mSlideOffset = mAnchorPoint;
                        updateObscuredViewVisibility();
                        setPanelStateInternal(PanelState.ANCHORED);
                    } else {
                        for (int i = 0, size = mAnchorPointExtras.size(); i < size; i++) {
                            float anchorPointExtra = mAnchorPointExtras.get(i);
                            if (Math.abs(anchorPointExtra - mSlideOffset) <= mSlideOffsetSlop) {
                                mAnchorHeightExtraIndex = i;
                                mSlideOffset = anchorPointExtra;
                                updateObscuredViewVisibility();
                                setPanelStateInternal(PanelState.ANCHORED);
                                break;
                            }
                        }
                    }
                }
                if (mSlideState == PanelState.DRAGGING) { //没有完成滑动判断，出错处理
                    float anchorPointExtraClosest = 1.0f;
                    if ((anchorPointExtraClosest = computeAnchorPointExtraClosest(mSlideOffset, 0)) != 1.0f) {
                        smoothSlideTo(anchorPointExtraClosest, 0);
                    } else {
                        float deltaExpand = Math.abs(mSlideOffset - 1); //距离EXPAND状态的增量值
                        float deltaAnchor = mAnchorPoint != 1 ? Math.abs(mSlideOffset - mAnchorPoint) : 1; //距离ANCHOR状态的增量值
                        float deltaCollapse = Math.abs(mSlideOffset); //距离COLLAPSED状态的增量值
                        if (deltaExpand < deltaAnchor && deltaExpand < deltaCollapse) {
                            smoothSlideTo(1, 0);
                        } else if (mAnchorPoint != 1 && deltaAnchor < deltaExpand && deltaAnchor < deltaCollapse) {
                            smoothSlideTo(mAnchorPoint, 0);
                        } else if (deltaCollapse < deltaExpand && deltaCollapse < deltaAnchor) {
                            smoothSlideTo(0, 0);
                        } else {
                            smoothSlideTo(0, 0);
                        }
                    }
                }
            }
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            setAllChildrenVisible();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            onPanelDragged(top);
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int target = 0;

            // direction is always positive if we are sliding in the expanded direction
            float direction = -yvel;

            float anchorPointExtraClosest = computeAnchorPointExtraClosest(mSlideOffset, direction);
            if (anchorPointExtraClosest != 1.0f) {
                target = computePanelTopPosition(anchorPointExtraClosest);
            } else if (direction > 0 && mSlideOffset <= mAnchorPoint) {
                // swipe up -> expand and stop at anchor point
                target = computePanelTopPosition(mAnchorPoint);
            } else if (direction > 0 && mSlideOffset > mAnchorPoint) {
                // swipe up past anchor -> expand
                target = computePanelTopPosition(1.0f);
            } else if (direction < 0 && mSlideOffset >= mAnchorPoint) {
                // swipe down -> collapse and stop at anchor point
                target = computePanelTopPosition(mAnchorPoint);
            } else if (direction < 0 && mSlideOffset < mAnchorPoint) {
                // swipe down past anchor -> collapse
                target = computePanelTopPosition(0.0f);
            } else if (mSlideOffset >= mExpandPoint) {
                target = computePanelTopPosition(1.0f);
            } else if (mExpandPoint == 1 && mSlideOffset >= (1.f + mAnchorPoint) / 2) {
                // zero velocity, and far enough from anchor point => expand to the top
                target = computePanelTopPosition(1.0f);
            } else if (mSlideOffset >= mAnchorPoint / 2) {
                // zero velocity, and close enough to anchor point => go to anchor
                target = computePanelTopPosition(mAnchorPoint);
            } else {
                // settle at the bottom
                target = computePanelTopPosition(0.0f);
            }

            mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), target);
            invalidate();
        }

        private float computeAnchorPointExtraClosest(float slideOffset, float direction) {
            float anchorPointExtra = 1.0f;
            int pointExtraSize = mAnchorPointExtras.size();
            if (pointExtraSize == 0) {
                return anchorPointExtra;
            }
            if (direction > 0) { //swipe up
                for (int i = 0; i < pointExtraSize; i++) {
                    float anchorPoint = mAnchorPointExtras.get(i);
                    if (anchorPoint > slideOffset) {
                        anchorPointExtra = anchorPoint;
                        break;
                    }
                }
            } else if (direction < 0) { //swipe down
                for (int i = 0; i < pointExtraSize; i++) {
                    float anchorPoint = mAnchorPointExtras.get(i);
                    if (anchorPoint < slideOffset) {
                        anchorPointExtra = anchorPoint;
                        break;
                    }
                }
            } else {
                ArrayList<Float> deltaExtras = new ArrayList<>();
                for (int i = 0; i < pointExtraSize; i++) {
                    deltaExtras.add(Math.abs(mAnchorPointExtras.get(i) - slideOffset));
                }
                deltaExtras.add(Math.abs(slideOffset)); //COLLAPSED
                deltaExtras.add(Math.abs(1 - slideOffset)); //EXPANDED
                deltaExtras.add(Math.abs(mExpandPoint - slideOffset)); //展开点排除
                deltaExtras.add(Math.abs(mAnchorPoint - slideOffset)); //ANCHORED 没有设置则该点默认为1
                int minIndex = -1;
                float minValue = 1.0f;
                for (int i = 0, size = deltaExtras.size(); i < size; i++) {
                    float delta = deltaExtras.get(i);
                    if (minValue > delta) {
                        minValue = delta;
                        minIndex = i;
                    }
                }
                if (minIndex >= 0 && minIndex < pointExtraSize) {
                    anchorPointExtra = mAnchorPointExtras.get(minIndex);
                }
            }
            return anchorPointExtra;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mSlideRange;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int collapsedTop = computePanelTopPosition(0.f);
            final int expandedTop = computePanelTopPosition(1.0f);
            return Math.min(Math.max(top, expandedTop), collapsedTop);
        }
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        private static final int[] ATTRS = new int[]{
                android.R.attr.layout_weight
        };

        public float weight = 0;

        public LayoutParams() {
            super(MATCH_PARENT, MATCH_PARENT);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, float weight) {
            super(width, height);
            this.weight = weight;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super(source);
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            final TypedArray ta = c.obtainStyledAttributes(attrs, ATTRS);
            if (ta != null) {
                this.weight = ta.getFloat(0, 0);
            }

            ta.recycle();
        }
    }

    static class SavedState extends BaseSavedState {
        PanelState mSlideState;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            String panelStateString = in.readString();
            try {
                mSlideState = panelStateString != null ? Enum.valueOf(PanelState.class, panelStateString)
                        : PanelState.COLLAPSED;
            } catch (IllegalArgumentException e) {
                mSlideState = PanelState.COLLAPSED;
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(mSlideState == null ? null : mSlideState.toString());
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    @Override
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
