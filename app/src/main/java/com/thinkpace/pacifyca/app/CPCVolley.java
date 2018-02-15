
package com.thinkpace.pacifyca.app;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.thinkpace.pacifyca.net.OkHttpStack;
import com.thinkpace.pacifyca.utils.ImageCacheManager;


public class CPCVolley {

	private static RequestQueue mRequestQueue;
	private static RequestQueue mImageRequestQueue;

	
	private CPCVolley() {}
	
	/**
	 * initialize Volley
	 */
	public static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context, new OkHttpStack(),true);
		mImageRequestQueue = Volley.newRequestQueue(context, true);
		ImageCacheManager.INSTANCE.initImageCache();
	}
	
	public static RequestQueue getRequestQueue(Context context) {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			return mRequestQueue = Volley.newRequestQueue(context, new OkHttpStack(),true);
		}
	}
	
	public static RequestQueue getImageRequestQueue() {

		if (mImageRequestQueue != null) {
			return mImageRequestQueue;
		} else {
			throw new IllegalStateException("Image RequestQueue not initialized");
		}
	}
}
