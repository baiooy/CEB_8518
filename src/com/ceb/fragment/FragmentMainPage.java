package com.ceb.fragment;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import com.adapter.AdapterMainPage;
import com.caifuline.R;
import com.ceb.activity.ActBannerDetail;
import com.ceb.activity.ActCebDetail;
import com.ceb.activity.ActUserYanzheng;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.loadingdialog.LoadingDialog;
import com.model.Banner_item;
import com.model.MainArray;
import com.model.UserInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.service.BannerService;
import com.service.UserInfoService;
import com.spp.SppaConstant;
import com.view.CircularProgressDrawable;
import com.view.UGallery;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class FragmentMainPage extends Fragment implements OnClickListener  {
	SharedPreferences _sharedPreferences;
	String _userType = "-1";
	String userID;
	ViewGroup _dotViewGroup;
	Button btn_location;
	private ViewPager vp;
	private ArrayList<View> views;
	long exittime = 0;
//	private SwipeRefreshLayout mSwipeRefreshLayout;
//	private TextView tv_tips;
	private Handler mHandler;
	LinearLayout ivDrawable;
	CircularProgressDrawable drawable;
	Animator currentAnimation;
	TextView tv_title, tv_extraIncome, tv_progress, tv_deadline,
			tv_minInvestment, tv_rebate;
	int index = 0;
	Runnable sendable;
	private int mProgress;
	private String type;
	UserInfoService _userInfoService;
	private Handler _handler;
	BannerService _service = null;
	Button btn_lijitouzi;
	private SharedPreferences isLogin;
	private Boolean islogin = false;
	String productID;
	private ImageView iv_xinshou;
	LoadingDialog dialog;
	LinearLayout num4;
	SharedPreferences isNewUser;
	private SharedPreferences sp;
	private UGallery  gallery;
	 GestureDetector mGestureDetector;  
	 
	 private boolean isalive = true; //线程执行条件
	 private int count_drawable = 0;
	private int cur_index = 0;
	 @SuppressWarnings("deprecation")
	 UGallery gallery_banner;
	 int count = 0;
	 DisplayImageOptions options;
	 ArrayList<ImageView> _dotImageViews;
	public ImageLoader imageLoader = ImageLoader.getInstance();
	PullToRefreshScrollView mPullRefreshScrollView;
	ScrollView mScrollView;
	private AdapterMainPage mAdapter;
	private Boolean canRefresh = true;
	private LinearLayout num1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("onCreateView", "onCreateView");
		count = 0;
		
		
		
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		

		mPullRefreshScrollView = (PullToRefreshScrollView) getActivity().findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if(canRefresh){
					canRefresh = false;
					try {
						qualityproducts();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					if(mPullRefreshScrollView != null){
						mPullRefreshScrollView.onRefreshComplete();
					}
					return;
				}
				
			}
		});

		mScrollView = mPullRefreshScrollView.getRefreshableView();
		
		
		/**
		 * 初始化
		 */
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));		
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(null)
		.showImageForEmptyUri(null)
		.showImageOnFail(getResources().getDrawable(R.drawable.banner))
		.cacheInMemory(true)//设置内存缓存
		.cacheOnDisk(true)//设置磁盘缓存
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(0))//设置图片的半径，可通过这个参数设置圆角图片
		.build();
		_dotImageViews = new ArrayList<ImageView>();
//		mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(
//				R.id.swipe_container);
		gallery_banner = (UGallery) getActivity().findViewById(
				R.id.gallery_banner);
		_dotViewGroup = (ViewGroup) getActivity().findViewById(
				R.id.viewGroup);
//		mSwipeRefreshLayout.setEnabled(false);
		gallery = (UGallery) getActivity().findViewById(
				R.id.gallery);
		num1 = (LinearLayout) getActivity().findViewById(
				R.id.num1);
		num1.setOnClickListener(this);
		Log.i("onActivityCreated", "onActivityCreated");
		views = new ArrayList<View>();
//		mGestureDetector = new GestureDetector(this);  
		isNewUser = getActivity().getSharedPreferences("isNewUser", 0);
		SharedPreferences.Editor editer = isNewUser.edit();
		editer.clear();
		editer.commit();
		editer.putBoolean("isNewUser", false);
		editer.commit();
		
		_handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(getActivity(), "网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
					currentAnimation = prepareStyle2Animation(mProgress);

					currentAnimation.start();
					new Thread(sendable).start();
					
					break;
				case 3:
					break;
				case 2:
					break;
				default:
					break;
				}
			};
			
//			mhandler = new Handler(){
//				public void handleMessage(Message msg){
//					if(msg.what == 1){
//						gallery_banner.setSelection(msg.arg1);
//					}
//				}
//			}; 
		};
//		gallery.setOnTouchListener(this);
//		gallery_banner.setOnTouchListener(this);
		_userInfoService = new UserInfoService(getActivity());
		
		
		isLogin = getActivity().getSharedPreferences("isLogin", 0);
		islogin = isLogin.getBoolean("islogin", false);
		
		try {
			qualityproducts();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
				//	tv_tips.setVisibility(View.GONE);
					break;
				case 0x01:
					tv_progress.setText(index + "");
					break;
				case 11:
		//			gallery_banner.setSelection(msg.arg1);
		//			Toast.makeText(getActivity(), msg.arg1+"", 1000).show();
					if(msg.arg1 < count_drawable){
						cur_index++;
						if(cur_index == count_drawable){
							cur_index = 0;
							gallery_banner.setSelection(0);
						}
						gallery_banner.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
					}/*else if(msg.arg1 == count_drawable){
						cur_index--;
						gallery_banner.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
					}*/
					
					break;
				default:
					break;
				}
			}
		};
		tv_progress = (TextView) getActivity().findViewById(R.id.tv_progress);
		iv_xinshou = (ImageView) getActivity().findViewById(R.id.iv_xinshou);
		tv_title = (TextView) getActivity().findViewById(R.id.tv_title);
		tv_rebate = (TextView) getActivity().findViewById(R.id.tv_rebate);
		num4 = (LinearLayout) getActivity().findViewById(R.id.num4);
		tv_extraIncome = (TextView) getActivity().findViewById(
				R.id.tv_extraIncome);
		tv_deadline = (TextView) getActivity().findViewById(R.id.tv_deadline);
		tv_minInvestment = (TextView) getActivity().findViewById(
				R.id.tv_minInvestment);
		btn_lijitouzi = (Button) getActivity().findViewById(R.id.btn_lijitouzi);
		btn_lijitouzi.setOnClickListener(this);
		sendable = new Runnable() {

			@Override
			public void run() {
				index = 0;
				for (int i = 0; i < mProgress; i++) {
					index++;
					try {
						Thread.sleep(1200 / mProgress);
						mHandler.sendEmptyMessage(0x01);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		};

		new Thread(sendable).start();

		ivDrawable = (LinearLayout) getActivity()
				.findViewById(R.id.iv_drawable);
		drawable = new CircularProgressDrawable.Builder()
				.setRingWidth(
						getResources().getDimensionPixelSize(
								R.dimen.drawable_ring_size))
				.setOutlineColor(getResources().getColor(R.color.lightgrey))
				.setRingColor(
						getResources().getColor(android.R.color.holo_red_light))
				.setCenterColor(
						getResources().getColor(android.R.color.transparent))
				.create();
		ivDrawable.setBackgroundDrawable(drawable);
		ivDrawable.setOnClickListener(this);

//		mSwipeRefreshLayout.setEnabled(true);

//	

//		tv_tips = (TextView) getActivity().findViewById(R.id.tv_tips);

//		mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
//				android.R.color.holo_green_light,
//				android.R.color.holo_orange_light,
//				android.R.color.holo_red_light);
//
//		mSwipeRefreshLayout
//				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//					@Override
//					public void onRefresh() {
//					//	tv_tips.setVisibility(View.VISIBLE);
//						mSwipeRefreshLayout.setRefreshing(true);
//						tv_tips.setText("正在刷新...");
//						try {
//							mHandler.postDelayed(new Runnable() {
//								@Override
//								public void run() {
//									qualityproducts();
//								}
//							}, 1400);
//							
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//		//				processOnRefresh();
//					}
//				});
		super.onActivityCreated(savedInstanceState);
	}

//	public void processOnRefresh() {
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				tv_tips.setText("刷新成功！");
//				mSwipeRefreshLayout.setRefreshing(false);
//
//				Message msg = new Message();
//				msg.what = 0;
//				mHandler.sendMessageDelayed(msg, 1000);
//
//			}
//		}, 3000);
//
//	}

	/**
	 * Style 2 animation will fill the outer ring while applying a color effect
	 * from red to green
	 * 
	 * @return Animation
	 */
	private Animator prepareStyle2Animation(int progress) {
		AnimatorSet animation = new AnimatorSet();

		ObjectAnimator progressAnimation = ObjectAnimator.ofFloat(drawable,
				CircularProgressDrawable.PROGRESS_PROPERTY, 0f,
				progress * 0.01f);
		progressAnimation.setDuration(1200);
		progressAnimation
				.setInterpolator(new AccelerateDecelerateInterpolator());

		ObjectAnimator colorAnimator = ObjectAnimator.ofInt(drawable,
				CircularProgressDrawable.RING_COLOR_PROPERTY, getResources()
						.getColor(android.R.color.holo_red_light));
		colorAnimator.setEvaluator(new ArgbEvaluator());
		colorAnimator.setDuration(1600);
		// tv_progress.setText((int)(drawable.getCircleScale()*100)+"");
		// tv_progress.setText((int)(drawable.getProgress())+"");
		animation.playTogether(progressAnimation, colorAnimator);

		return animation;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i("onResume", "onResume");
		isLogin = getActivity().getSharedPreferences("isLogin", 0);
		islogin = isLogin.getBoolean("islogin", false);
		try {
			qualityproducts();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_drawable:
			if (currentAnimation != null) {
				currentAnimation.cancel();
			}
			currentAnimation = prepareStyle2Animation(mProgress);
			currentAnimation.start();
			new Thread(sendable).start();
			break;
		case R.id.btn_lijitouzi:
			/**
			 * 此时判断是否处于登陆状态，若已登录，跳入立即投资界面，若未登录，跳入输入手机号界面，然后走 用户验证接口判断是否为注册用户。
			 */
			if (islogin) {// 已登录
				Intent ins = new Intent(getActivity(), ActCebDetail.class);
				ins.putExtra("productID", productID);
				startActivity(ins);
				getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			} else {
				Intent in = new Intent(getActivity(), ActUserYanzheng.class);
				startActivity(in);
				getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}

			break;
		case R.id.num1:
			Intent in = new Intent(getActivity(),ActBannerDetail.class);
			in.putExtra("url", "http://testinterface.8518.com/app/ceb/statics/security.html");
			in.putExtra("title","安全保障");
			startActivity(in);
			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		default:
			break;
		}

	}

	// @Override
	// public void onResume() {
	// super.onResume();
	//
	// _bannerWrapper.start();
	// }

	@Override
	public void onPause() {
		super.onPause();
		Log.i("onPause", "onPause");
//		if (_bannerWrapper != null)
//			_bannerWrapper.stop();
	}

	void fitScreen() {

		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

		final int screenHeight = dm.heightPixels;
		final int screenWidth = dm.widthPixels;

		// Banner bar
		{
			// 广告图片规格720*400
			ViewGroup bannerBar = (ViewGroup) getActivity().findViewById(
					R.id.banner_bar);
			LayoutParams params = (LayoutParams) bannerBar.getLayoutParams();
			params.height = (int) (400 * screenWidth * 1.0f / 720);
			bannerBar.setLayoutParams(params);
		}
	}

	void qualityproducts() {
		sp = getActivity().getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		
		String  signature = "qualityproductlist.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(getActivity());

		new QualityTask().execute(userInfo);
	}

	class QualityTask extends AsyncTask<UserInfo, Void, MainArray> {

		@Override
		protected void onPreExecute() {
	//		mSwipeRefreshLayout.setRefreshing(true);// 一进来就开始刷新
			mPullRefreshScrollView.onRefreshComplete();
			dialog = new LoadingDialog(getActivity());
			// dialog.show();
		}

		@Override
		protected MainArray doInBackground(UserInfo... params) {
			MainArray info = null;

			info = _userInfoService.qualityproducts(params[0]);
			_service = new BannerService(getActivity(), params[0]);
			Log.i("info", "info ==" + info);
			return info;
		}

		@Override
		protected void onPostExecute(final MainArray result) {
//			mSwipeRefreshLayout.setRefreshing(false);
//			tv_tips.setText("刷新成功！");
			canRefresh = true;
			mHandler.sendEmptyMessage(0);
			Log.i("result", "result ==" + result);
			if (result == null || result.toString().equals("null")) {
				_handler.sendEmptyMessage(-1);
			} else if (result.ret.equals("0")) {
				mAdapter = new AdapterMainPage(getActivity(),result,gallery);
				gallery.setAdapter(mAdapter);
				ImageAdapter adapter = new ImageAdapter(result.item_banner);
				gallery_banner.setAdapter(adapter);
				count_drawable = adapter.getCount();
				
				ImageView imageView;
				for(int i=0;i<result.item_banner.size();i++){
					// 小圆点
					imageView = new ImageView(getActivity());
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
				_dotViewGroup.removeAllViews();
				for (int i = 0; i < result.item_banner.size(); ++i) {
					_dotViewGroup.addView(_dotImageViews.get(i));
				}
				
				gallery_banner.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						for(int i=0;i<result.item_banner.size();i++){
							_dotImageViews.get(position).setBackgroundResource(R.drawable.page);
							if(position != i){
								_dotImageViews.get(i).setBackgroundResource(R.drawable.page_now);
							}
						}
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
						
					}
					
				});
				
				if(count == 0){
					//线程控制部分 --- 广告自动滚动
					new Thread(new Runnable(){
						int flag = 1;
						@Override
						public void run() {
							while(isalive){
								
								if((cur_index+1) == count_drawable){
									flag = -1;
								}else if(cur_index == 0){
									flag = 1;
								}
//								cur_index = cur_index % count_drawable;
					//			Log.i("", "cur_index == "+ cur_index +" , count_drawble == "+ count_drawable);
								Message msg = mHandler.obtainMessage(11, cur_index, 0);
								mHandler.sendMessage(msg);
							//	cur_index++;
								try{
									Thread.sleep(3000);
								}catch(InterruptedException e){
									e.printStackTrace();
								}
//								switch(flag){
//								case 1:cur_index++;
//								break;
//								case -1:cur_index--;
//								break;
//								}
								
							}
							
						}			
					}).start();
				}
				count++;
				
				gallery_banner.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent in = new Intent(getActivity(),ActBannerDetail.class);
						in.putExtra("url", result.item_banner.get(position).bannerUrl);
						in.putExtra("title", result.item_banner.get(position).bannerTitle);
						startActivity(in);
						getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
					}
				});
				
				_handler.sendEmptyMessage(0);

			} else {
				Toast.makeText(getActivity(), result.msg, Toast.LENGTH_SHORT)
						.show();
			}
		}

	}
	
//	 	@Override   
//	    public boolean onTouch(View v, MotionEvent event) {   
//	        // TODO Auto-generated method stub   
//	  //      Log.i("touch","touch"); 
//	 //			mSwipeRefreshLayout.setEnabled(false);
//	         return mGestureDetector.onTouchEvent(event);    
//	    }   
//
//	@Override
//	public boolean onDown(MotionEvent e) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//	@Override
//	public void onShowPress(MotionEvent e) {
//		// TODO Auto-generated method stub
//		
//	}
//	@Override
//	public boolean onSingleTapUp(MotionEvent e) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//	@Override
//	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
//			float distanceY) {
////		if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY()
////				- e2.getY()) /*||Math.abs(e1.getX() - e2.getX())>10*/) {
////			Log.i("onScroll", "左右滑动");
////	//		mSwipeRefreshLayout.setEnabled(false);
////		}else{
////			Log.i("onScroll", "上下滑动");
//////			mSwipeRefreshLayout.setEnabled(true);
//////			mSwipeRefreshLayout.setRefreshing(true);
////			try {
////				mHandler.postDelayed(new Runnable() {
////					@Override
////					public void run() {
////						qualityproducts();
////					}
////				}, 1400);
////				
////			} catch (Exception e) {
////				e.printStackTrace();
////			}
////		}
//		return false;
//	}
//	@Override
//	public void onLongPress(MotionEvent e) {
//		
//	}
//	@Override
//	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//			float velocityY) {
////		if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY()
////				- e2.getY()) /*||Math.abs(e1.getX() - e2.getX())>10*/) {
////			Log.i("onFling", "左右滑动");
////	//		mSwipeRefreshLayout.setEnabled(false);
////		}else{
////			if(e1.getY()- e2.getY() < 0){
////		
////			Log.i("onFling", "上下滑动");
//////			mSwipeRefreshLayout.setEnabled(true);
//////			mSwipeRefreshLayout.setRefreshing(true);
////			try {
////				mHandler.postDelayed(new Runnable() {
////					@Override
////					public void run() {
////						qualityproducts();
////					}
////				}, 400);
////				
////			} catch (Exception e) {
////				e.printStackTrace();
////			}
////			}
////		}
////		mSwipeRefreshLayout.setEnabled(false);
//		return false;
//	}
//	
	
	
	class ImageAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		private List<Banner_item>  mList;
	    private String userID;
		ImageAdapter(List<Banner_item> list) {
			inflater = LayoutInflater.from(getActivity());
			mList = list;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = inflater.inflate(R.layout.banner_item, parent, false);
				holder = new ViewHolder();
				holder.iv_banner = (ImageView) view.findViewById(R.id.iv_banner);
				holder.ll_banner = (LinearLayout) view.findViewById(R.id.ll_banner);
				holder.ll_ll = (LinearLayout) view.findViewById(R.id.ll_ll);
				
				WindowManager wm = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
				DisplayMetrics dm = new DisplayMetrics();
				wm.getDefaultDisplay().getMetrics(dm);
				LayoutParams params = holder.ll_ll.getLayoutParams();
				params.width = dm.widthPixels;
				holder.ll_ll.setLayoutParams(params);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			if(position >= mList.size()){
				position = 0;
			}
	        imageLoader.displayImage(mList.get(position).bannerImg, holder.iv_banner, options, animateFirstListener);
	        
	        
			
			return view;
		}
	}
	
	private static class ViewHolder {
		LinearLayout ll_ll;
		ImageView iv_banner;
		LinearLayout ll_banner;
	}
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
