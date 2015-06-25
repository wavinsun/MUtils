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

package cn.o.app.qrcode.decode;

import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import android.os.Handler;
import android.os.Looper;
import cn.o.app.qrcode.CaptureView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;

/**
 * This thread does all the heavy lifting of decoding the images.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
final class DecodeThread extends Thread {
	public static final String BARCODE_BITMAP = "barcode_bitmap";

	private final CaptureView mView;
	private final Hashtable<DecodeHintType, Object> mHints;
	private Handler mHandler;
	private final CountDownLatch mHandlerInitLatch;

	DecodeThread(CaptureView view, String characterSet) {

		this.mView = view;
		mHandlerInitLatch = new CountDownLatch(1);

		mHints = new Hashtable<DecodeHintType, Object>(3);

		Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
		decodeFormats.add(BarcodeFormat.QR_CODE);
		decodeFormats.add(BarcodeFormat.CODE_128);
		decodeFormats.add(BarcodeFormat.CODE_93);
		decodeFormats.add(BarcodeFormat.CODE_39);
		decodeFormats.add(BarcodeFormat.UPC_A);
		decodeFormats.add(BarcodeFormat.UPC_E);
		decodeFormats.add(BarcodeFormat.EAN_13);
		decodeFormats.add(BarcodeFormat.EAN_8);
		decodeFormats.add(BarcodeFormat.ITF);
		decodeFormats.add(BarcodeFormat.DATA_MATRIX);

		mHints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

		if (characterSet != null) {
			mHints.put(DecodeHintType.CHARACTER_SET, characterSet);
		}
	}

	Handler getHandler() {
		try {
			mHandlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return mHandler;
	}

	@Override
	public void run() {
		Looper.prepare();
		mHandler = new DecodeHandler(mView, mHints);
		mHandlerInitLatch.countDown();
		Looper.loop();
	}

}
