package com.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.caifuline.R;
import com.common.AsyncImageLoader;
import com.common.AsyncImageLoader.ImageCallback;
import com.common.AsyncImageLoader3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * User: Rock Author: Rock.liu@meehelath.com Date: Oct 15, 2012
 */
public class BgView {

	private AsyncImageLoader3 asyncImageLoader3 = new AsyncImageLoader3();
	private static AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
	static Drawable draw = null;
	// 缓存
	public BgView(Context context, final String url, final ImageView imageView) {
		// 延遲加載圖片 ： imageUrl 是 圖片的http鏈接地址，後面是回调函數
		// Log.d("Rock", url+":pic_path");
		Drawable cachedImage = asyncImageLoader.loadDrawable(context, url,
				new ImageCallback() {
					public void imageLoaded(Drawable imageDrawable,
							String imageUrl) {
						// 防止图片url获取不到图片时，占位图片不见了的情况
						if (imageDrawable != null) {
							imageView.setImageDrawable(imageDrawable);
						} else {
						//	imageView.setImageResource(R.drawable.loading);
						}
					}
				});
		if (cachedImage != null) {
			imageView.setImageDrawable(cachedImage);
		} else if(imageView.getDrawable() == null) {
		//	imageView.setImageResource(R.drawable.loading);
		}
		
	}
	
	// 缓存
	public BgView(Context context, final String url, final ImageView imageView,String str) {
		// 延遲加載圖片 ： imageUrl 是 圖片的http鏈接地址，後面是回调函數
		// Log.d("Rock", url+":pic_path");
		Drawable cachedImage = asyncImageLoader._loadDrawable(context, url,
				new ImageCallback() {
					public void imageLoaded(Drawable imageDrawable,
							String imageUrl) {
						// 防止图片url获取不到图片时，占位图片不见了的情况
						if (imageDrawable != null) {
							imageView.setImageDrawable(imageDrawable);
						} else {
							//imageView.setImageResource(R.drawable.user_default_ico);
						}
					}
				});
		if (cachedImage != null) {
			imageView.setImageDrawable(cachedImage);
		} else if(imageView.getDrawable() == null) {
			//imageView.setImageResource(R.drawable.user_default_ico);
		}
		
	}

	
	// 缓存
	public static Drawable loadImage(Context context, final String url) {
		// 延遲加載圖片 ： imageUrl 是 圖片的http鏈接地址，後面是回调函數
		// Log.d("Rock", url+":pic_path");
		
		Drawable cachedImage = asyncImageLoader.loadDrawable(context, url,
				new ImageCallback() {
					public void imageLoaded(Drawable imageDrawable,
							String imageUrl) {
						// 防止图片url获取不到图片时，占位图片不见了的情况
						if (imageDrawable != null) {
							draw = imageDrawable;
						} 
					}
				});
		if (cachedImage != null) {
			draw = cachedImage;
		} 
//		if (cachedImage != null) {
//		BitmapDrawable bd = (BitmapDrawable) cachedImage;
//		Bitmap bm = bd.getBitmap();
//		if(!bm.isRecycled()){//先判断图片是否已释放了   
//			   
//			bm.recycle();   
//			   
//			} 
//		}
		return draw;
		
	}
	
	
	
	// 不缓存
	public BgView(final String url, final ImageView imageView) {
		// Log.d("Rock", url+":pic_path3");
		// //如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
		Drawable cacheImage = asyncImageLoader3.loadDrawable(url,
				new AsyncImageLoader3.ImageCallback() {
					// 请参见实现：如果第一次加载url时下面方法会执行
					@Override
					public void imageLoaded(Drawable imageDrawable) {
						if (imageDrawable != null) {
							imageView.setImageDrawable(imageDrawable);
						} else {
						//	imageView.setImageResource(R.drawable.loading);
						}
					}
				});
		if (cacheImage != null) {
			imageView.setImageDrawable(cacheImage);
		} else {
		//	imageView.setImageResource(R.drawable.loading);
		}
	}

	// 不缓存
	public BgView(final String url, final ImageView imageView,
			AsyncImageLoader3.ImagePreloadCallback imagePreloadCallback,
			AsyncImageLoader3.ImageCallback imageLoadedCallback) {
		// Log.d("Rock", url+":pic_path3");
		// //如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
		Drawable cacheImage = asyncImageLoader3.loadDrawable(url,
				imageLoadedCallback);
		
		if (cacheImage != null) {
			imageLoadedCallback.imageLoaded(cacheImage);
		} else {
			imagePreloadCallback.imagePreload();
		}
	}

	// 不缓存
	public BgView(final String url, final RoundImageView imageView, 
			final RoundImageView.ScaleType preloadScaleType, 
			final RoundImageView.ScaleType loadedScaleType) {

		AsyncImageLoader3.ImageCallback imageLoadedCallback = new AsyncImageLoader3.ImageCallback() {
			@Override
			public void imageLoaded(Drawable imageDrawable) {
				if (imageDrawable != null) {
					imageView.setScaleType(loadedScaleType);
					imageView.setImageDrawable(imageDrawable);
				}
			}
		};
		
		AsyncImageLoader3.ImagePreloadCallback imagePreloadCallback = new AsyncImageLoader3.ImagePreloadCallback() {
			@Override
			public void imagePreload() {
				imageView.setScaleType(preloadScaleType);
		//		imageView.setImageResource(R.drawable.loading);
			}
		};
		
		// //如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
		Drawable cacheImage = asyncImageLoader3.loadDrawable(url,
				imageLoadedCallback);
		
		if (cacheImage != null) {
			imageLoadedCallback.imageLoaded(cacheImage);
		} else {
			imagePreloadCallback.imagePreload();
		}
	}

	// 缓存 for the background image of RelativeLayout
	public BgView(final Context context, final String url,
			final RelativeLayout bgimageView) {
		// 如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
		// Log.d("Rock", url+":pic_path");
		Drawable cacheImage = asyncImageLoader.loadDrawable(context, url,
				new ImageCallback() {
					// 请参见实现：如果第一次加载url时下面方法会执行
					@Override
					public void imageLoaded(Drawable imageDrawable,
							String imageUrl) {
						if (imageDrawable != null) {
							bgimageView.setBackgroundDrawable(imageDrawable);
						} else {
							Drawable drawable_bg = context.getResources()
									.getDrawable(R.drawable.loading);
					//		bgimageView.setBackgroundDrawable(drawable_bg);
						}
					}
				});
		if (cacheImage != null) {
			bgimageView.setBackgroundDrawable(cacheImage);
		} else if(bgimageView.getBackground() == null) {
			Drawable drawable_bg = context.getResources().getDrawable(
					R.drawable.loading);
	//		bgimageView.setBackgroundDrawable(drawable_bg);
		}
	}

	// 不缓存
	public BgView(final String url, final RelativeLayout bgimageView) {
		// Log.d("Rock", url+":pic_path3");
		// 如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
		Drawable cacheImage = asyncImageLoader3.loadDrawable(url,
				new AsyncImageLoader3.ImageCallback() {
					// 请参见实现：如果第一次加载url时下面方法会执行
					@Override
					public void imageLoaded(Drawable imageDrawable) {
						if (imageDrawable != null) {
							bgimageView.setBackgroundDrawable(imageDrawable);
						} else {
							// Drawable drawable_bg =
							// getResources().getDrawable(R.drawable.user_default_ico);
							// bgimageView.setBackgroundDrawable(drawable_bg);
						}
					}
				});
		if (cacheImage != null) {
			bgimageView.setBackgroundDrawable(cacheImage);
		} else {
			// Drawable drawable_bg =
			// getResources().getDrawable(R.drawable.user_default_ico);
			// bgimageView.setBackgroundDrawable(drawable_bg);
		}
	}

	
	public Drawable BgView_buhuancun(final String url) { //不缓存
		//Log.d("Rock", url+":pic_path3");
//      //如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
		Drawable cacheImage = asyncImageLoader3.loadDrawable(url,new AsyncImageLoader3.ImageCallback() {
         //请参见实现：如果第一次加载url时下面方法会执行
         @Override
		public void imageLoaded(Drawable imageDrawable) {
        	 if (imageDrawable != null ) { 
//        	 imageView.setImageDrawable(imageDrawable);
        	 }else{
// 		 imageView.setImageResource(R.drawable.user_null);
        	 }
         }
     });
//    if(cacheImage!=null){
//    	imageView.setImageDrawable(cacheImage);
//    }else{
//		 imageView.setImageResource(R.drawable.user_null);
//	 }
	return cacheImage;
}
	
	public static Bitmap getImage(String imageUrl) {
        byte[] data = null;
        Bitmap bitmap = null;
        try {
            // 建立URL
            URL url = new URL(imageUrl);
 
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
 
            InputStream input = conn.getInputStream();// 到这可以直接BitmapFactory.decodeFile也行。 返回bitmap
 
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = input.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
 
            input.close();
            data = output.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(
                    data, 0, data.length);

     //       System.out.println("下载完毕！");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
	
	
	
	
	
	
	
	
	
	
}
