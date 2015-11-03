package cn.mutils.app.ui.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import cn.mutils.app.core.ILockable;
import cn.mutils.app.core.annotation.event.OnClick;
import cn.mutils.app.core.annotation.res.FindViewById;
import cn.mutils.app.core.annotation.res.GetColor;
import cn.mutils.app.core.annotation.res.GetColorStateList;
import cn.mutils.app.core.annotation.res.GetDimension;
import cn.mutils.app.core.annotation.res.GetDimensionPixelSize;
import cn.mutils.app.core.annotation.res.GetDrawable;
import cn.mutils.app.core.annotation.res.GetString;
import cn.mutils.app.core.annotation.res.GetStringArray;
import cn.mutils.app.core.annotation.res.LoadAnimation;
import cn.mutils.app.core.annotation.res.SetContentView;
import cn.mutils.app.core.reflect.ReflectUtil;
import cn.mutils.app.core.task.IStopable;
import cn.mutils.app.core.task.IStopableManager;
import cn.mutils.app.event.listener.OnActivityResultListener;
import cn.mutils.app.event.listener.OnClickListener;
import cn.mutils.app.os.IContextProvider;
import cn.mutils.app.ui.InfoToast;

/**
 * UI core implementation of framework
 */
@SuppressWarnings({ "deprecation", "unchecked" })
public class UICore {

	/**
	 * Hide keyboard when touch view who is not EditText and not focused
	 * 
	 * @param ev
	 * @param dispatcher
	 */
	public static void dispatchTouchEvent(MotionEvent ev, IWindowProvider dispatcher) {
		if (ev.getAction() != MotionEvent.ACTION_DOWN) {
			return;
		}
		boolean isSoftInputValid = false;
		Window w = dispatcher.getWindow();
		View focus = w.getCurrentFocus();
		if (focus != null && (focus instanceof EditText)) {
			EditText edit = (EditText) focus;
			int[] l = new int[2];
			edit.getLocationInWindow(l);
			boolean touchEdit = (ev.getX() >= l[0]) && (ev.getX() <= l[0] + edit.getWidth()) && (ev.getY() >= l[1])
					&& (ev.getY() <= l[1] + edit.getHeight());
			isSoftInputValid = touchEdit;
		}
		if (!isSoftInputValid) {
			InputMethodManager imm = (InputMethodManager) dispatcher.getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(focus != null ? focus.getWindowToken() : w.getDecorView().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	/**
	 * Get content view
	 * 
	 * @param finder
	 * @return
	 */
	public static View getContentView(IViewFinder finder) {
		View v = null;
		if (finder instanceof Activity) {
			v = ((Activity) finder).findViewById(android.R.id.content);
		} else if (finder instanceof Dialog) {
			v = ((Dialog) finder).findViewById(android.R.id.content);
		} else {
			if (finder instanceof View) {
				return (View) finder;
			}
		}
		if (v == null) {
			return null;
		}
		if (v instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) v;
			if (vg.getChildCount() > 0) {
				v = vg.getChildAt(0);
			}
		}
		return v;
	}

	/**
	 * IOC for content view
	 * 
	 * @param owner
	 */
	public static void injectContentView(IContentViewOwner owner) {
		Class<?> c = owner.getClass();
		while (IContentViewOwner.class.isAssignableFrom(c)) {
			SetContentView s = c.getAnnotation(SetContentView.class);
			if (s != null) {
				owner.setContentView(s.value());
				return;
			}
			c = c.getSuperclass();
		}
	}

	/**
	 * IOC for events
	 * 
	 * @param finder
	 */
	public static void injectEvents(IViewFinder finder) {
		Class<?> c = finder.getClass();
		while (IViewFinder.class.isAssignableFrom(c)) {
			for (Method m : c.getDeclaredMethods()) {
				OnClick oc = m.getAnnotation(OnClick.class);
				if (oc == null) {
					continue;
				}
				if (oc.value().length == 0 && (finder instanceof View)) {
					OnClickListener ocl = new OnClickListener();
					ocl.setCallBack(m);
					ocl.setProxy(finder);
					((View) finder).setOnClickListener(ocl);
					continue;
				}
				for (int id : oc.value()) {
					View v = findViewById(finder, id, View.class);
					if (v == null) {
						continue;
					}
					OnClickListener ocl = new OnClickListener();
					ocl.setCallBack(m);
					ocl.setProxy(finder);
					v.setOnClickListener(ocl);
				}
			}
			c = c.getSuperclass();
		}
	}

	/**
	 * IOC for resources
	 * 
	 * @param finder
	 */
	public static void injectResources(IViewFinder finder) {
		Context context = (finder instanceof IContextProvider) ? ((IContextProvider) finder).getContext() : null;
		Resources res = context == null ? null : context.getResources();
		Class<?> c = finder.getClass();
		while (IViewFinder.class.isAssignableFrom(c)) {
			for (Field f : c.getDeclaredFields()) {
				FindViewById fvbi = f.getAnnotation(FindViewById.class);
				if (fvbi != null) {
					View v = findViewById(finder, fvbi.value(), (Class<View>) f.getType());
					if (v != null) {
						ReflectUtil.set(finder, f, v);
					}
				}
				if (context == null) {
					continue;
				}
				LoadAnimation la = f.getAnnotation(LoadAnimation.class);
				if (la != null) {
					ReflectUtil.set(finder, f, AnimationUtils.loadAnimation(context, la.value()));
				}
				if (res == null) {
					continue;
				}
				GetColor gc = f.getAnnotation(GetColor.class);
				if (gc != null) {
					ReflectUtil.set(finder, f, res.getColor(gc.value()));
				}
				GetColorStateList gcsl = f.getAnnotation(GetColorStateList.class);
				if (gcsl != null) {
					ReflectUtil.set(finder, f, res.getColorStateList(gcsl.value()));
				}
				GetDimension gd = f.getAnnotation(GetDimension.class);
				if (gd != null) {
					ReflectUtil.set(finder, f, res.getDimension(gd.value()));
				}
				GetDimensionPixelSize gdps = f.getAnnotation(GetDimensionPixelSize.class);
				if (gdps != null) {
					ReflectUtil.set(finder, f, res.getDimensionPixelSize(gdps.value()));
				}
				GetDrawable d = f.getAnnotation(GetDrawable.class);
				if (d != null) {
					ReflectUtil.set(finder, f, res.getDrawable(d.value()));
				}
				GetString gs = f.getAnnotation(GetString.class);
				if (gs != null) {
					ReflectUtil.set(finder, f, res.getString(gs.value()));
				}
				GetStringArray gsa = f.getAnnotation(GetStringArray.class);
				if (gsa != null) {
					ReflectUtil.set(finder, f, res.getStringArray(gsa.value()));
				}
			}
			c = c.getSuperclass();
		}
	}

	public static <T extends View> T findViewById(Object root, int id, Class<T> viewClass) {
		if (root == null) {
			return null;
		}
		View view = null;
		if (root instanceof View) {
			view = (View) root;
			if (view.getId() == id) {
				if (viewClass != null) {
					if (viewClass.isInstance(view)) {
						return (T) view;
					} else {
						return null;
					}
				}
				return (T) view;
			}
			view = ((View) root).findViewById(id);
		} else if (root instanceof Activity) {
			view = ((Activity) root).findViewById(id);
		} else if (root instanceof Dialog) {
			view = ((Dialog) root).findViewById(id);
		} else if (root instanceof IView) {
			view = ((IView) root).toView().findViewById(id);
		}
		if (view != null) {
			if (viewClass != null) {
				if (viewClass.isInstance(view)) {
					return (T) view;
				} else {
					return null;
				}
			}
			return (T) view;
		}
		List<IStateView> bindViews = null;
		if (root instanceof IStateViewManager) {
			bindViews = ((IStateViewManager) root).getBindStateViews();
		}
		if (bindViews != null) {
			for (IStateView stateView : bindViews) {
				T v = stateView.findViewById(id, viewClass);
				if (v != null) {
					return v;
				}
			}
		}
		List<View> cachedViews = null;
		if (root instanceof ICachedViewManager) {
			cachedViews = ((ICachedViewManager) root).getCachedViews();
		}
		if (cachedViews != null) {
			for (View cacheView : cachedViews) {
				if (cacheView instanceof IStateView) {
					T v = ((IStateView) cacheView).findViewById(id, viewClass);
					if (v != null) {
						return v;
					}
				} else {
					View v = cacheView.findViewById(id);
					if (v != null) {
						if (viewClass != null) {
							if (viewClass.isInstance(v)) {
								return (T) v;
							} else {
								return null;
							}
						}
						return (T) v;
					}
				}
			}
		}
		return null;
	}

	public static void bindStateViews(IStateViewManager manager, View v) {
		bindStateViews(manager, v, 0, Integer.MAX_VALUE);
	}

	public static void bindStateViews(IStateViewManager manager, View v, int depth, int maxDepth) {
		if (depth >= maxDepth) {
			return;
		}
		if (v instanceof IStateView) {
			if (v != manager) {
				manager.bind((IStateView) v);
				return;
			}
		}
		if (v instanceof ViewGroup) {
			ViewGroup g = (ViewGroup) v;
			for (int i = 0, size = g.getChildCount(); i < size; i++) {
				bindStateViews(manager, g.getChildAt(i), depth + 1, maxDepth);
			}
		}
	}

	public static void bind(IStateViewManager manager, IStateView v) {
		List<IStateView> views = manager.getBindStateViews();
		if (views.contains(v)) {
			return;
		}
		v.setManager(manager);
		views.add(v);
	}

	public static void bind(IStopableManager manager, IStopable s) {
		List<IStopable> stopables = manager.getBindStopables();
		List<IStopable> stopedList = null;
		for (IStopable stopable : stopables) {
			if (stopable.equals(s)) {
				if (stopedList != null) {
					stopables.removeAll(stopedList);
					stopedList.clear();
				}
				return;
			}
			if (stopable.isStoped()) {
				if (stopedList == null) {
					stopedList = new ArrayList<IStopable>();
				}
				stopedList.add(stopable);
			}
		}
		if (stopedList != null) {
			stopables.removeAll(stopedList);
			stopedList.clear();
		}
		stopables.add(s);
	}

	public static void dispatchCreate(IStateView v) {
		if (v.isCreateDispatched()) {
			return;
		}
		v.onCreate();
		v.setCreateDispatched(true);
	}

	public static void dispatchStart(IStateViewManager manager) {
		if (manager instanceof IStateView) {
			IStateView v = (IStateView) manager;
			if (!v.isCreateDispatched()) {
				dispatchCreate(v);
			}
		}
		if (manager.redirectToSelectedView()) {
			IStateView v = manager.getSelectedView();
			if (v != null) {
				v.onStart();
			}
		} else {
			List<IStateView> views = manager.getBindStateViews();
			for (IStateView stateView : views) {
				stateView.onStart();
			}
		}
	}

	public static void dispatchResume(IStateViewManager manager) {
		if (manager instanceof IStateView) {
			IStateView v = (IStateView) manager;
			if (!v.isCreateDispatched()) {
				v.onStart();
			}
		}
		if (manager.redirectToSelectedView()) {
			IStateView v = manager.getSelectedView();
			if (v != null) {
				v.onResume();
			}
		} else {
			List<IStateView> views = manager.getBindStateViews();
			for (IStateView stateView : views) {
				stateView.onResume();
			}
		}
	}

	public static void dispatchPause(IStateViewManager manager) {
		if (manager instanceof IStateView) {
			IStateView v = (IStateView) manager;
			if (!v.isCreateDispatched()) {
				dispatchCreate(v);
			}
		}
		if (manager instanceof IStopableManager) {
			((IStopableManager) manager).stopAll();
		}
		if (manager.redirectToSelectedView()) {
			IStateView v = manager.getSelectedView();
			if (v != null) {
				v.onPause();
			}
		} else {
			List<IStateView> views = manager.getBindStateViews();
			for (IStateView stateView : views) {
				stateView.onPause();
			}
		}
		if (manager instanceof IToastOwner) {
			InfoToast toast = ((IToastOwner) manager).getInfoToast();
			if (toast != null) {
				toast.hideNow();
			}
		}
	}

	public static void dispatchStop(IStateViewManager manager) {
		if (manager instanceof IStateView) {
			IStateView v = (IStateView) manager;
			if (!v.isCreateDispatched()) {
				v.onPause();
			}
		}
		if (manager.redirectToSelectedView()) {
			IStateView v = manager.getSelectedView();
			if (v != null) {
				v.onStop();
			}
		} else {
			List<IStateView> views = manager.getBindStateViews();
			for (IStateView stateView : views) {
				stateView.onStop();
			}
		}
	}

	public static void dispatchDestroy(IStateViewManager manager) {
		if (manager instanceof IStateView) {
			IStateView v = (IStateView) manager;
			if (!v.isCreateDispatched()) {
				return;
			}
		}
		if (manager instanceof IStopableManager) {
			((IStopableManager) manager).stopAll(true);
		}
		List<IStateView> views = manager.getBindStateViews();
		for (IStateView stateView : views) {
			stateView.onDestroy();
		}
		views.clear();
	}

	public static void stopAll(IStopableManager manager) {
		stopAll(manager, false);
	}

	public static void stopAll(IStopableManager manager, boolean includeLockable) {
		List<IStopable> stopables = manager.getBindStopables();
		List<IStopable> stopedList = null;
		for (IStopable stopable : stopables) {
			if (includeLockable) {
				if (stopable instanceof ILockable) {
					continue;
				}
			}
			stopable.stop();
			if (stopedList == null) {
				stopedList = new ArrayList<IStopable>();
			}
			stopedList.add(stopable);
		}
		if (stopedList != null) {
			stopables.removeAll(stopedList);
			stopedList.clear();
		}
	}

	public static boolean interceptBackPressed(IStateViewManager manager) {
		if (manager instanceof IStopableManager) {
			List<IStopable> stopables = ((IStopableManager) manager).getBindStopables();
			boolean catchStopableed = false;
			List<IStopable> stopedList = null;
			for (IStopable stopable : stopables) {
				if (stopable instanceof ILockable) {
					continue;
				}
				if (stopable.isStoped()) {
					if (stopedList == null) {
						stopedList = new ArrayList<IStopable>();
					}
					stopedList.add(stopable);
					continue;
				}
				if (!stopable.isRunInBackground() && !stopable.isStoped()) {
					catchStopableed = true;
					stopable.stop();
					if (stopedList == null) {
						stopedList = new ArrayList<IStopable>();
					}
					stopedList.add(stopable);
				}
			}
			if (stopedList != null) {
				stopables.removeAll(stopedList);
				stopedList.clear();
			}
			if (catchStopableed) {
				return true;
			}
		}
		if (manager.redirectToSelectedView()) {
			IStateView v = manager.getSelectedView();
			if (v != null) {
				if (v.onInterceptBackPressed()) {
					return true;
				}
			}
		} else {
			List<IStateView> views = manager.getBindStateViews();
			for (IStateView stateView : views) {
				if (stateView.onInterceptBackPressed()) {
					return true;
				}
			}
		}
		return false;
	}

	public static void startActivity(IContextProvider provider, Intent intent) {
		provider.getContext().startActivity(intent);
	}

	public static void startActivityForResult(IActivityExecutor executor, Intent intent, int requestCode) {
		Context context = executor.getContext();
		if (context instanceof Activity) {
			((Activity) context).startActivityForResult(intent, requestCode);
		}
	}

	public static void onActivityResult(IActivityExecutor executor, int requestCode, int resultCode, Intent data) {
		Context context = executor.getContext();
		List<OnActivityResultListener> listeners = executor.getOnActivityResultListeners();
		if (listeners != null) {
			for (OnActivityResultListener listener : listeners) {
				listener.onActivityResult(context, requestCode, resultCode, data);
			}
		}
		if (!(executor instanceof IStateViewManager)) {
			return;
		}
		IStateViewManager manager = (IStateViewManager) executor;
		if (manager.redirectToSelectedView()) {
			IStateView selectedView = manager.getSelectedView();
			if (selectedView == null) {
				return;
			}
			selectedView.onActivityResult(context, requestCode, resultCode, data);
		} else {
			List<IStateView> bindViews = manager.getBindStateViews();
			if (bindViews == null) {
				return;
			}
			for (IStateView stateView : bindViews) {
				stateView.onActivityResult(context, requestCode, resultCode, data);
			}
		}
	}

	protected static boolean isEnabled(IToastOwner owner) {
		if (owner instanceof IActivity) {
			return ((IActivity) owner).isRunning();
		}
		Context context = owner.getContext();
		if (context instanceof IActivity) {
			return ((IActivity) context).isRunning();
		}
		if (context instanceof Activity) {
			return !((Activity) context).isFinishing();
		}
		return true;
	}

	public static void toast(IToastOwner owner, CharSequence s) {
		if (!isEnabled(owner)) {
			return;
		}
		Toast t = owner.getToast();
		if (s == null) {
			t.cancel();
		} else {
			if (s.equals("")) {
				t.cancel();
			} else {
				t.setText(s);
				t.show();
			}
		}
	}

	public static void toast(IToastOwner owner, int resId, Object... args) {
		if (!isEnabled(owner)) {
			return;
		}
		Toast t = owner.getToast();
		if (resId == 0) {
			t.cancel();
		} else {
			if (args != null & args.length != 0) {
				Context context = owner.getContext();
				t.setText(context.getString(resId, args));
			} else {
				t.setText(resId);
			}
			t.show();
		}
	}
}
