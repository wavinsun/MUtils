package cn.mutils.app.zxing;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import com.google.zxing.Result;

import cn.mutils.app.ui.StateView;
import cn.mutils.app.zxing.camera.CameraManager;
import cn.mutils.app.zxing.decode.CaptureHandler;

/**
 * Capture view of framework<br>
 * It is used to capture data from QRCode image.
 */
@SuppressWarnings({"UnnecessaryInterfaceModifier", "deprecation"})
public class CaptureView extends StateView implements Callback {

    public static interface CaptureListener {
        void onCapture(String data);
    }

    protected CaptureHandler mHandler;
    protected SurfaceView mSurfaceView;
    protected ViewfinderView mViewfinderView;
    protected boolean mHasSurface;
    protected CaptureListener mListener;
    protected boolean mLightsOn;
    protected Drawable mLaserLine;
    protected ColorStateList mLaserColor;
    protected ColorStateList mFrameColor;
    protected ColorStateList mMaskColor;

    public CaptureView(Context context) {
        super(context);
        init(context, null);
    }

    public CaptureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CaptureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CaptureView);
        mLaserLine = typedArray.getDrawable(R.styleable.CaptureView_laserLine);
        mLaserColor = typedArray.getColorStateList(R.styleable.CaptureView_laserColor);
        mFrameColor = typedArray.getColorStateList(R.styleable.CaptureView_frameColor);
        mMaskColor = typedArray.getColorStateList(R.styleable.CaptureView_maskColor);
        typedArray.recycle();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = this.getContext();
        mSurfaceView = new SurfaceView(context);
        mSurfaceView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        this.addView(mSurfaceView);
        mViewfinderView = new ViewfinderView(context);
        mViewfinderView.setLaserLine(mLaserLine);
        mViewfinderView.setLaserColor(mLaserColor);
        mViewfinderView.setFrameColor(mFrameColor);
        mViewfinderView.setMaskColor(mMaskColor);
        mViewfinderView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        this.addView(mViewfinderView);
        mHasSurface = false;

        CameraManager.init(getContext().getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        if (mHasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    @Override
    public void onPause() {
        if (mHandler != null) {
            mHandler.quitSynchronously();
            mHandler = null;
        }
        CameraManager.get().closeDriver();
        super.onPause();
    }

    protected void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (Exception e) {
            return;
        }
        if (mHandler == null) {
            mHandler = new CaptureHandler(this, "UTF-8");
        }
        if (mLightsOn) {
            this.turnOnLights();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!mHasSurface) {
            mHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHasSurface = false;
    }

    public void handleDecode(Result obj, Bitmap barcode) {
        if (mListener != null) {
            mListener.onCapture(obj.getText());
        }
    }

    public void drawViewfinder() {
        mViewfinderView.invalidate();
    }

    public ViewfinderView getViewfinderView() {
        return mViewfinderView;
    }

    public CaptureHandler getCaptureHandler() {
        return mHandler;
    }

    public boolean isLightsOn() {
        return mLightsOn;
    }

    public boolean turnOnLights() {
        CameraManager cameraManager = CameraManager.get();
        if (cameraManager == null) {
            return false;
        }
        Camera camera = cameraManager.getCamera();
        if (camera == null) {
            return false;
        }
        Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        mLightsOn = Parameters.FLASH_MODE_TORCH.equals(camera.getParameters().getFlashMode());
        return mLightsOn;
    }

    public boolean turnOffLights() {
        CameraManager cameraManager = CameraManager.get();
        if (cameraManager == null) {
            return false;
        }
        Camera camera = cameraManager.getCamera();
        if (camera == null) {
            return false;
        }
        Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
        mLightsOn = Parameters.FLASH_MODE_TORCH.equals(camera.getParameters().getFlashMode());
        return !mLightsOn;
    }

    public void setChromeColor(int chromeColor) {
        this.mViewfinderView.setFrameColor(chromeColor);
        this.mViewfinderView.setLaserColor(chromeColor);
    }

    public void setChromeColor(String chromeColor) {
        try {
            this.setChromeColor(Color.parseColor(chromeColor));
        } catch (Exception e) {
            // IllegalArgumentException
        }
    }

    public void setListener(CaptureListener listener) {
        mListener = listener;
    }

}
