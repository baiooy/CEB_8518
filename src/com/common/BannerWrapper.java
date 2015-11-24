package com.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.caifuline.R;
import com.ceb.activity.ActBannerDetail;
import com.google.gson.Gson;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//广告栏封装类，支持自动换页和本地缓存
public class BannerWrapper {
	
	final static int AUTO_SLIDE_INTERVAL = 5000;
	final static String CACHE_NAME = "CACHE_NAME_0298E226-BF79-4BD3-9255-DA8CF3760ED5";
	final static String CACHE_CONTENT = "CACHE_CONTENT";

	ViewPager _bannerViewPager;
	TextView _bannerTitle;
	ViewGroup _dotsViewGroup;
	ArrayList<View> _pageViews;
	ArrayList<ImageView> _dotImageViews;
	Handler _bannerAutoSlideHandler;
	Runnable _bannerAutoSlideRunnable;
	IBannerService _bannerService;
	Context _context;
	List<BannerItem> _data;
	int _viewPagerLayoutID;
	SwipeRefreshLayout mSwipeRefreshLayout;
	
	public BannerWrapper(Context c, IBannerService bannerService, 
			TextView bannerTitle, ViewGroup dotsViewGroup, 
			ViewPager bannerViewPager, int viewPagerLayoutID ,SwipeRefreshLayout swipe) {
		
		_bannerViewPager = bannerViewPager;
		_bannerTitle = bannerTitle;
		_dotsViewGroup = dotsViewGroup;
		_bannerService = bannerService;
		_context = c;
		_viewPagerLayoutID = viewPagerLayoutID;
		mSwipeRefreshLayout = swipe;
		
		init();
	}
	
	// 开始自动换页
	public void start() {
		
		if (_bannerAutoSlideHandler != null && _data.size() > 0) {
	    	_bannerAutoSlideHandler.removeCallbacks(_bannerAutoSlideRunnable);

	    	_bannerAutoSlideHandler.postDelayed(_bannerAutoSlideRunnable, AUTO_SLIDE_INTERVAL);
	    }
	}
	
	// 停止自动换页
	public void stop() {
		
	    if (_bannerAutoSlideHandler != null) {
	    	_bannerAutoSlideHandler.removeCallbacks(_bannerAutoSlideRunnable);
	    }
	}
	
	void init() {
		
		// 页面集合
		_pageViews = new ArrayList<View>();
		_dotImageViews = new ArrayList<ImageView>();

		// 自动换页
		_bannerAutoSlideHandler = new Handler();
		_bannerAutoSlideRunnable = new Runnable() {
			public void run() {
				int pos = _bannerViewPager.getCurrentItem();
				if (pos + 1 >= _data.size()) {
					pos = 0;
				} else {
					pos = pos + 1;
				}
				_bannerViewPager.setCurrentItem(pos, true);
				_bannerAutoSlideHandler.postDelayed(_bannerAutoSlideRunnable, AUTO_SLIDE_INTERVAL);
			}
		};

		// 页面数据
		_data = new ArrayList<BannerItem>();
		
		_bannerViewPager.setAdapter(new GuidePageAdapter());
		_bannerViewPager.setOnPageChangeListener(new GuidePageChangeListener());
		
		// 默认欢迎页
		BannerItem welcomePage = new BannerItem();
		_data.add(welcomePage);
		RefreshBanners();

		// 先读本地缓存，再访问网络
		new ReadCacheTask().execute();
	}
	
	// 本地缓存读取类
	class CacheService implements IBannerService {

		@Override
		public List<BannerItem> retrieveItems() {
			
			SharedPreferences sp = _context.getSharedPreferences(CACHE_NAME,
					Context.MODE_WORLD_READABLE);
			
			if(sp != null && sp.contains(CACHE_CONTENT)) {
				String content = sp.getString(CACHE_CONTENT, "");
				try {
					if(content != null && content.length() > 0) {
						
						Log.i("Banners Read Cache", content);

						BannerItem[] banners = (BannerItem[]) new Gson().fromJson(content, BannerItem[].class);
						
						//new TypeToken<List<Video>>(){}.getType()
						return Arrays.asList(banners);
					}
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			return null;
		}
	}
	
	// 本地缓存的读取任务
	class ReadCacheTask extends AsyncTask<Void, Void, List<BannerItem>> {

		@Override
		protected List<BannerItem> doInBackground(Void... arg0) {
			
			try {
				CacheService cacheService = new CacheService();
				
				return cacheService.retrieveItems();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(List<BannerItem> result) {
			
			if(result != null && result.size() > 0) {
				
				_data.clear();
				_data.addAll(result);
				
				RefreshBanners();
			}
			
			new DownloadBannersTask().execute();
		}
	}

	// 网络数据的读取任务
	class DownloadBannersTask extends AsyncTask<Void, Void, List<BannerItem>> {
		
		@Override
		protected List<BannerItem> doInBackground(Void... arg0) {

			try {
				List<BannerItem> bannerItems = _bannerService.retrieveItems();
				
				return bannerItems;
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			return null;
		}

		protected void onPostExecute(List<BannerItem> result) {
			
			if(result != null && result.size() > 0) {
				
				_data.clear();
				_data.addAll(result);

				// write cache
				SharedPreferences sp = _context.getSharedPreferences(CACHE_NAME,
						Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE);
				
				if(sp != null) {
					try {
						String content = new Gson().toJson(_data);
						sp.edit().putString(CACHE_CONTENT, content).commit();
						
						Log.i("Banners Write Cache", content);
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
				
				RefreshBanners();
			}
		}
	}
	
	void RefreshBanners() {
		
		// 停止换页，防止并发
		stop();
		
		final int CNT = _data.size();
		
		LayoutInflater inflater = (LayoutInflater) _context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

		for(int i = _pageViews.size() - 1; i >= CNT; --i) {
			_pageViews.remove(i);
			_dotImageViews.remove(i);
		}
		//_pageViews.clear();

		ImageView imageView;

		for (int i = _pageViews.size(); i < CNT; ++i) {

			// 广告页
			View v1 = inflater.inflate(_viewPagerLayoutID, null);

			_pageViews.add(v1);
			// 小圆点
			imageView = new ImageView(_context);
			imageView.setLayoutParams(new LayoutParams(15, 15));
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 0, 10, 0);
			imageView.setLayoutParams(lp);

			_dotImageViews.add(i, imageView);

			if (i == 0) {
				// 默认选中第一张图片
				_dotImageViews.get(i).setBackgroundResource(R.drawable.page);
			} else {
				_dotImageViews.get(i).setBackgroundResource(R.drawable.page_now);
			}
		}

		_dotsViewGroup.removeAllViews();
		for (int i = 0; i < CNT; ++i) {
			_dotsViewGroup.addView(_dotImageViews.get(i));
		}
		
		_bannerViewPager.getAdapter().notifyDataSetChanged();
		//_bannerViewPager.setAdapter(new GuidePageAdapter());
		
		int currentPageIndex = _bannerViewPager.getCurrentItem();
		View currentPage = _pageViews.get(currentPageIndex);
		
		_bannerTitle.setText(_data.get(currentPageIndex).title);
		
		if(_data.get(currentPageIndex).imgURL != null && _data.get(currentPageIndex).imgURL.length() > 0) {
			new com.view.BgView(_context, _data.get(0).imgURL, (ImageView) currentPage);
		}
		
		
		start();//开启自动换页
	}

	// 指引页面数据适配器
	private class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return _pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(_pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, final int arg1) {

			View vTmp = _pageViews.get(arg1);

			if(_data.get(arg1).imgURL != null && _data.get(arg1).imgURL.length() > 0) {
				Log.i("instantiateItem", "index="+arg1+"/"+_pageViews.size()+", URL="+_data.get(arg1).imgURL);
				new com.view.BgView(_context, _data.get(arg1).imgURL, (RelativeLayout) vTmp);
			}
			

			((ViewPager) arg0).addView(vTmp);
			
			vTmp.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					Toast.makeText(_context, "璺宠浆鏆傛湭瀹炵幇"+arg1, 1000).show();
					Log.i("imgURL", _data.get(arg1).title+"/"+_data.get(arg1).content+"/"+_data.get(arg1).imgURL);
					
//					 Uri uri = Uri.parse(_data.get(arg1).content);    
//					 Intent it = new Intent(Intent.ACTION_VIEW, uri);    
//					 _context.startActivity(it);  
					
					 Intent it = new Intent(_context, ActBannerDetail.class);
					 it.putExtra("url", _data.get(arg1).content);
					 it.putExtra("title", _data.get(arg1).title);
					 _context.startActivity(it);
					
				}
			});
			
			return _pageViews.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			;
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			;
		}

		@Override
		public void finishUpdate(View arg0) {
			;
		}
	}

	// 页面更改事件监听器
	private class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {
			switch (state) {

			case ViewPager.SCROLL_STATE_DRAGGING:// 1 —— PRESS
				mSwipeRefreshLayout.setEnabled(false);
				break;
			case ViewPager.SCROLL_STATE_SETTLING:// 2 —— UP
				break;
			case ViewPager.SCROLL_STATE_IDLE:// 0 —— END
				
				mSwipeRefreshLayout.setEnabled(true);
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		//	Log.i("onPageScrolled--bannerwrapper", "onPageScrolled  position=" + arg0);
		}

		@Override
		public void onPageSelected(int arg0) {

			_bannerTitle.setText(_data.get(arg0).title);
			Log.i("广告title", _data.get(arg0).title);

			for (int i = 0; i < _dotImageViews.size(); i++) {
				_dotImageViews.get(arg0).setBackgroundResource(R.drawable.page);

				if (arg0 != i) {
					_dotImageViews.get(i).setBackgroundResource(R.drawable.page_now);
				}
			}
		}
	}

	public interface IBannerService {
		
		public List<BannerItem> retrieveItems();
	}

	public static class BannerItem {		
		public String imgURL;
		public String title;
		public String content;
		
	}
}
