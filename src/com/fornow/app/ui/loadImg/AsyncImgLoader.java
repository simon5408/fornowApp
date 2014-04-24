/*****************************************************************************
 *
 *                      FORNOW PROPRIETARY INFORMATION
 *
 *          The information contained herein is proprietary to ForNow
 *           and shall not be reproduced or disclosed in whole or in part
 *                    or used for any design or manufacture
 *              without direct written authorization from ForNow.
 *
 *            Copyright (c) 2014 by ForNow.  All rights reserved.
 *
 *****************************************************************************/
package com.fornow.app.ui.loadImg;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.fornow.app.ui.AppClass;

/**
 * @author Jiafa Lv
 * @date Apr 24, 2014 10:52:20 AM
 * @email simon-jiafa@126.com
 * 
 */
public class AsyncImgLoader {
//	private static final String imgCacheDir = "/mnt/sdcard/.yimicache/";
	private static final String imgCacheDir = Environment
			.getExternalStorageDirectory().getPath() + ".yimicache/";
	private HashMap<String, SoftReference<Drawable>> imageCache = null;
	private String imgName;

	public AsyncImgLoader() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}

	public Drawable loadDrawable(final String imageUrl, final String Tag,
			final ImageCallback imageCallback) {
		imgName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		Drawable drawable = null;
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> reference = imageCache.get(imageUrl);
			Drawable draw = reference.get();
			if (draw != null) {
				drawable = draw;
			}
		} else {
			File cacheDir = new File(imgCacheDir);
			File[] cacheFiles = cacheDir.listFiles();
			int i = 0;
			if (null != cacheFiles) {
				for (; i < cacheFiles.length; i++) {
					if (imgName.equals(cacheFiles[i].getName())) {
						break;
					}
				}
				if (i < cacheFiles.length) {
					Bitmap bitmap = BitmapFactory.decodeFile(imgCacheDir
							+ imgName);
					drawable = new BitmapDrawable(bitmap);
				}
			}
		}

		if (drawable == null) {
			new Thread() {
				@SuppressLint("NewApi")
				@Override
				public void run() {
					try {
						Drawable draw = Drawable.createFromResourceStream(
								AppClass.getContext().getResources(), null,
								new URL(imageUrl).openStream(), "src", null);
						imageCache.put(imageUrl, new SoftReference<Drawable>(
								draw));
						cacheImg2Sdcard(draw);
						imageCallback.imageLoaded(draw, Tag);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						imageCallback.imageLoaded(null, Tag);
						e.printStackTrace();
					}

				}
			}.start();
		} else {
			imageCallback.imageLoaded(drawable, Tag);
		}
		return null;

	}

	@SuppressLint("NewApi")
	public void cacheImg2Sdcard(Drawable drawable) {
		BitmapDrawable bd = (BitmapDrawable) drawable;

		Bitmap bitmap = bd.getBitmap();

		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inJustDecodeBounds = true;
		options.inSampleSize = ImageSize.computeSampleSize(options, -1,
				bitmap.getWidth() * bitmap.getHeight());
		options.inJustDecodeBounds = false;

		byte[] byBitmap = Bitmap2Bytes(bitmap);

		bitmap = BitmapFactory.decodeByteArray(byBitmap, 0, byBitmap.length,
				options);

		File dir = new File(imgCacheDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File bitmapFile = new File(imgCacheDir + imgName);
		if (!bitmapFile.exists()) {
			try {
				bitmapFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(bitmapFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public byte[] Bitmap2Bytes(Bitmap bm) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String Tag);

	}
}
