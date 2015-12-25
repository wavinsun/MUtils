/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.mutils.app.qrcode;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import cn.mutils.app.qrcode.camera.CameraManager;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

	private Paint paint;
	private int maskColor;
	private int frameColor;
	private int laserColor;
	private Drawable laserLine;

	protected int lastFrameWidth;
	protected int lastFrameHeight;
	protected int laserPosition;
	protected Rect frame;
	protected Matrix matrix;

	public ViewfinderView(Context context) {
		super(context);
		init();
	}

	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	protected void init() {
		paint = new Paint();
		paint.setAntiAlias(true);
		maskColor = 0x60000000;
		frameColor = 0xFF13B0F7;
		laserColor = 0xFF10B5F1;
		frame = new Rect();
		matrix = new Matrix();
	}

	public void setLaserLine(Drawable laserLine) {
		this.laserLine = laserLine;
		this.invalidate();
	}

	public void setLaserColor(ColorStateList laserColor) {
		if (laserColor == null) {
			return;
		}
		this.laserColor = laserColor.getDefaultColor();
		this.invalidate();
	}

	public void setFrameColor(ColorStateList frameColor) {
		if (frameColor == null) {
			return;
		}
		this.frameColor = frameColor.getDefaultColor();
		this.invalidate();
	}

	public void setMaskColor(ColorStateList maskColor) {
		if (maskColor == null) {
			return;
		}
		this.maskColor = maskColor.getDefaultColor();
		this.invalidate();
	}

	public void setFrameColor(int frameColor) {
		this.frameColor = frameColor;
		this.invalidate();
	}

	public void setFrameColor(String frameColor) {
		try {
			this.setFrameColor(Color.parseColor(frameColor));
		} catch (Exception e) {

		}
	}

	public void setLaserColor(int laserColor) {
		this.laserColor = laserColor;
		this.invalidate();
	}

	public void setLaserColor(String laserColor) {
		try {
			this.setLaserColor(Color.parseColor(laserColor));
		} catch (Exception e) {

		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		int[] location = new int[2];
		this.getLocationInWindow(location);
		frame.setEmpty();
		CameraManager.get().setFramingRect(null);
		Rect framingRect = CameraManager.get().getFramingRect();
		if (framingRect == null) {
			return;
		}
		frame.set(framingRect);
		frame.offset(-frame.left, -frame.top);
		frame.offset(location[0] + (this.getWidth() - frame.width()) / 2,
				location[1] + (this.getHeight() - frame.height()) / 2);
		CameraManager.get().setFramingRect(frame);
		frame.offset(-location[0], -location[1]);
		this.invalidate();
	}

	@Override
	public void onDraw(Canvas canvas) {
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		// Draw the exterior (i.e. outside the framing rect) darkened
		paint.reset();
		paint.setColor(maskColor);
		if (frame.isEmpty()) {
			canvas.drawRect(0, 0, width, height, paint);
			return;
		}
		float laserW = laserLine == null ? 0 : laserLine.getIntrinsicWidth();
		float laserH = laserLine == null ? 0 : laserLine.getIntrinsicHeight();
		int laserStep = (int) (3 + laserH / 2 + 0.5);
		if (this.lastFrameWidth == frame.width()
				&& this.lastFrameHeight == frame.height()) {
			if (this.laserPosition + laserStep > frame.height()) {
				this.laserPosition = laserStep;
			}
		} else {
			this.lastFrameWidth = frame.width();
			this.lastFrameHeight = frame.height();
			this.laserPosition = laserStep;
		}
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
				paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);

		if (laserLine == null) {
			paint.setColor(laserColor);
			paint.setStrokeWidth(3);
			canvas.drawLine(frame.left, frame.top + this.laserPosition,
					frame.right, frame.top + this.laserPosition, paint);
			paint.setStrokeWidth(1);
		} else {
			float laserNowW = frame.width() - 4;
			float laserNowH = laserNowW * laserH / laserW;
			int halfLaserNowH = (int) (laserNowH / 2 + 0.5);
			laserLine.setBounds(frame.left + 2, frame.top + this.laserPosition
					- halfLaserNowH, frame.right - 2, frame.top
					+ this.laserPosition + halfLaserNowH);
			laserLine.draw(canvas);
		}
		this.laserPosition += 20;

		// Draw a two pixel solid black border inside the framing rect
		paint.setColor(frameColor);
		paint.setStrokeWidth(5);
		drawFrameRect(frame.left, frame.top, frame.right, frame.bottom, canvas,
				paint);
		paint.setStrokeWidth(1);

		// Request another update at the animation interval, but only
		// repaint the laser line,
		// not the entire viewfinder mask.
		postInvalidateDelayed(100, frame.left, frame.top, frame.right,
				frame.bottom);

	}

	protected void drawFrameRect(float left, float top, float right,
			float bottom, Canvas canvas, Paint paint) {
		int offset = (int) (paint.getStrokeWidth() / 2);
		float width = right - left;
		float height = bottom - top;
		float hSize = width / 10;
		float vSize = height / 10;
		canvas.drawLine(left - offset, top, left + hSize, top, paint);
		canvas.drawLine(right - hSize, top, right + offset, top, paint);
		canvas.drawLine(right, top - offset, right, top + vSize, paint);
		canvas.drawLine(right, bottom - vSize, right, bottom + offset, paint);
		canvas.drawLine(right + offset, bottom, right - hSize, bottom, paint);
		canvas.drawLine(left + hSize, bottom, left - offset, bottom, paint);
		canvas.drawLine(left, bottom + offset, left, bottom - vSize, paint);
		canvas.drawLine(left, top + vSize, left, top - offset, paint);
	}

}
