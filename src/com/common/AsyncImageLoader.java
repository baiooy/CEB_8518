package com.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AsyncImageLoader {
	private static final String TAG = "AsyncImageLoader";

	private HashMap<String, SoftReference<Drawable>> imageCache;
	private BlockingQueue queue;
	private ThreadPoolExecutor executor;

	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();

		// 绾跨▼姹狅細鏈�ぇ50鏉★紝姣忔鎵ц锛�鏉★紝绌洪棽绾跨▼缁撴潫鐨勮秴鏃舵椂闂达細180绉�
		queue = new LinkedBlockingQueue();
		executor = new ThreadPoolExecutor(1, 50, 180, TimeUnit.SECONDS, queue);
	}

	public Drawable loadDrawable(final Context context, final String imageUrl,
			final ImageCallback imageCallback) {
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
		};

		// 鐢ㄧ嚎绋嬫睜鏉ュ仛涓嬭浇鍥剧墖鐨勪换鍔�
		executor.execute(new Runnable() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(context, imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
				
			}
		});
		
		return null;
	}
	
	public Drawable _loadDrawable(final Context context, final String imageUrl,
			final ImageCallback imageCallback) {
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
		};

		// 鐢ㄧ嚎绋嬫睜鏉ュ仛涓嬭浇鍥剧墖鐨勪换鍔�
		executor.execute(new Runnable() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl_(context, imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
				
			}
		});
		
		return null;
	}

	// 缃戠粶鍥剧墖鍏堜笅杞藉埌鏈湴cache鐩綍淇濆瓨锛屼互imagUrl鐨勫浘鐗囨枃浠跺悕淇濆瓨銆傚鏋滄湁鍚屽悕鏂囦欢鍦╟ache鐩綍灏变粠鏈湴鍔犺浇
	public static Drawable _loadImageFromUrl(Context context, String imageUrl) {
		Drawable drawable = null;
		if (imageUrl == null)
			return null;
		String imagePath = "";
		String fileName = "";

//		if (!imageUrl.startsWith(SppaConstant.getIPmobile())) {
//			imageUrl = SppaConstant.getIPmobile()
//					+ (imageUrl.startsWith("/") ? "" : "/") + imageUrl;
//		}
		fileName = imageUrl.replace("http://", "http--").replace("//", "/")
				.replace("/", "-");

		// 鍥剧墖鍦ㄦ墜鏈烘湰鍦扮殑瀛樻斁璺緞,娉ㄦ剰锛歠ileName涓虹┖鐨勬儏鍐�
		imagePath = context.getCacheDir() + "/" + fileName;

		File file = new File(context.getCacheDir(), fileName);// 淇濆瓨鏂囦欢
		// 鍙互鍦ㄨ繖閲岄�杩囨枃浠跺悕鏉ュ垽鏂紝鏄惁鏈湴鏈夋鍥剧墖
		if (!file.exists() && !file.isDirectory()) {
			try {
				FileOutputStream fos = new FileOutputStream(file);
				//浠庣綉缁滆鍙栧浘鐗囷紝鍐欏叆锛堜繚瀛樺埌锛夋枃浠�
				InputStream is = new URL(imageUrl).openStream();
				int data = is.read();
				while (data != -1) {
					fos.write(data);
					data = is.read();
					;
				}
				fos.close();
				is.close();
				drawable = Drawable.createFromPath(file.toString());
				Log.i(TAG, "Image downloaded = " + imageUrl);
			} catch (IOException e) {
				Log.e(TAG, e.toString() + "鍥剧墖涓嬭浇鍙婁繚瀛樻椂鍑虹幇寮傚父锛");
			}
		} else {
			drawable = Drawable.createFromPath(file.toString());
			Log.i(TAG, "Image from local = " + file.toString());
		}
		return drawable;
	}

	
	public static Drawable loadImageFromUrl(Context context, String imageUrl) {
		Drawable drawable = null;
		BitmapDrawable bad=  null;
		
	
		
		if (imageUrl == null)
			return null;
		String imagePath = "";
		String fileName = "";

//		if (!imageUrl.startsWith(SppaConstant.getIPmobile())) {
//			imageUrl = SppaConstant.getIPmobile()
//					+ (imageUrl.startsWith("/") ? "" : "/") + imageUrl;
//		}
		fileName = imageUrl.replace("http://", "http--").replace("//", "/")
				.replace("/", "-");

		// 鍥剧墖鍦ㄦ墜鏈烘湰鍦扮殑瀛樻斁璺緞,娉ㄦ剰锛歠ileName涓虹┖鐨勬儏鍐�
		imagePath = context.getCacheDir() + "/" + fileName;

		File file = new File(context.getCacheDir(), fileName);// 淇濆瓨鏂囦欢
		// 鍙互鍦ㄨ繖閲岄�杩囨枃浠跺悕鏉ュ垽鏂紝鏄惁鏈湴鏈夋鍥剧墖
		if (!file.exists() && !file.isDirectory()) {
			try {
				FileOutputStream fos = new FileOutputStream(file);
				//浠庣綉缁滆鍙栧浘鐗囷紝鍐欏叆锛堜繚瀛樺埌锛夋枃浠�
				InputStream is = new URL(imageUrl).openStream();
				int data = is.read();
				while (data != -1) {
					fos.write(data);
					data = is.read();
					;
				}
				fos.close();
				is.close();
				drawable = Drawable.createFromPath(file.toString());		
				Log.i(TAG, "Image downloaded = " + imageUrl);
			} catch (IOException e) {
				Log.e(TAG, e.toString() + "鍥剧墖涓嬭浇鍙婁繚瀛樻椂鍑虹幇寮傚父锛");
			}
		} else {
			drawable = Drawable.createFromPath(file.toString());
			Log.i(TAG, "Image from local = " + file.toString());
		}
		Log.i("drawable == null" ,(drawable == null)+"" );
		if(drawable != null){
			BitmapDrawable bd = (BitmapDrawable) drawable;
			Bitmap bm = bd.getBitmap();
			Bitmap bi = FileUtil.comp(bm);
			bad=  new BitmapDrawable(context.getResources() ,bi );
			
			return bad;
		}
		
		return drawable;
	}
	
	// 缃戠粶鍥剧墖鍏堜笅杞藉埌鏈湴cache鐩綍淇濆瓨锛屼互imagUrl鐨勫浘鐗囨枃浠跺悕淇濆瓨銆傚鏋滄湁鍚屽悕鏂囦欢鍦╟ache鐩綍灏变粠鏈湴鍔犺浇
		public static Drawable loadImageFromUrl_(Context context, String imageUrl) {
			Drawable drawable = null;
			if (imageUrl == null)
				return null;
			String imagePath = "";
			String fileName = "";

//			if (!imageUrl.startsWith(SppaConstant.getIPmobile())) {
//				imageUrl = SppaConstant.getIPmobile()
//						+ (imageUrl.startsWith("/") ? "" : "/") + imageUrl;
//			}
			fileName = imageUrl.replace("http://", "http--").replace("//", "/")
					.replace("/", "-");

			// 鍥剧墖鍦ㄦ墜鏈烘湰鍦扮殑瀛樻斁璺緞,娉ㄦ剰锛歠ileName涓虹┖鐨勬儏鍐�
			imagePath = context.getCacheDir() + "/" + fileName;

			File file = new File(context.getCacheDir(), fileName);// 淇濆瓨鏂囦欢
			// 鍙互鍦ㄨ繖閲岄�杩囨枃浠跺悕鏉ュ垽鏂紝鏄惁鏈湴鏈夋鍥剧墖
			if (!file.exists() && !file.isDirectory()) {
				try {
					FileOutputStream fos = new FileOutputStream(file);
					//浠庣綉缁滆鍙栧浘鐗囷紝鍐欏叆锛堜繚瀛樺埌锛夋枃浠�
					InputStream is = new URL(imageUrl).openStream();
					int data = is.read();
					while (data != -1) {
						fos.write(data);
						data = is.read();
						;
					}
					fos.close();
					is.close();
					drawable = Drawable.createFromPath(file.toString());
					Log.i(TAG, "Image downloaded = " + imageUrl);
				} catch (IOException e) {
					Log.e(TAG, e.toString() + "鍥剧墖涓嬭浇鍙婁繚瀛樻椂鍑虹幇寮傚父锛");
				}
			} else {
				drawable = Drawable.createFromPath(file.toString());
				Log.i(TAG, "Image from local = " + file.toString());
			}
			return drawable;
		}
	
	
	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}

	public Drawable loadDrawableWithoutCache(final Context context, final String imageUrl,
			final ImageCallback imageCallback) {

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
		};

		// 鐢ㄧ嚎绋嬫睜鏉ュ仛涓嬭浇鍥剧墖鐨勪换鍔�
		executor.execute(new Runnable() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(context, imageUrl);

				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		});

		return null;
	}
}
