package com.common;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;

public class AsyncImageLoader3 {
	private static final String TAG = "AsyncImageLoader3";
	// 为了加快速度，在内存中开启缓存（主要应用于重复图片较多时，或者同一个图片要多次被访问，比如在ListView时来回滚动）
	//图片缓存对象，键是URL，值是SoftReference对象，该对象指向一个Drawable对象
	public Map<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
	private ExecutorService executorService = Executors.newFixedThreadPool(5); // 固定五个线程来执行任务
	private final Handler handler = new Handler();

	/**
	 * 
	 * @param imageUrl
	 *            图像url地址
	 * @param callback
	 *            回调接口
	 * @return 返回内存中缓存的图像，第一次加载返回null
	 */
	public Drawable loadDrawable(final String imageUrl,
			final ImageCallback callback) { 
		//查询缓存，查看当前要下载的图片是否已经在缓存中  如果缓存过就从缓存中取出数据
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			//查询缓存的内容是否被GC回收
			if (softReference.get() != null) {
				return softReference.get();//还没被回收
			}
		}

		//缓存中没有图像，则从网络上取出数据，并将取出的数据缓存到内存中
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				try {
					String imgUrl = imageUrl;
//					if (!imageUrl.startsWith(SppaConstant.getIPmobile())) {
//						imgUrl = SppaConstant.getIPmobile()
//								+ (imageUrl.startsWith("/") ? "" : "/")
//								+ imageUrl;
//					}
					 //根据图片的URL，下载图片，并生成一个Drawable对象
					final Drawable drawable = Drawable.createFromStream(
							new URL(imgUrl).openStream(), "image.png");
					imageCache.put(imageUrl, new SoftReference<Drawable>(
							drawable));
					handler.post(new Runnable() {//将Runnable直接添加入队列
						@Override
						public void run() {
							callback.imageLoaded(drawable);
						}
					});
				} catch (Exception e) {
					Log.e(TAG, e.toString() + "图片下载及保存时出现异常！");
				}
			}
		});

		return null;
	}

	// 从网络上取数据方法
	protected Drawable loadImageFromUrl(String imageUrl) {
		try {
			return Drawable.createFromStream(new URL(imageUrl).openStream(),
					"image.png");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 对外界开放的回调接口
	public interface ImageCallback {
		// 注意 此方法是用来设置目标对象的图像资源
		public void imageLoaded(Drawable imageDrawable);
	}

	// 对外界开放的回调接口，图像加载前
	public interface ImagePreloadCallback {
		// 注意 此方法是用来设置目标对象的图像资源
		public void imagePreload();
	}
}
