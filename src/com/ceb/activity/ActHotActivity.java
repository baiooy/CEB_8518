package com.ceb.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caifuline.R;
import com.ceb.base.BaseActivity;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
import com.model.ActivityList;
import com.model.Activity_item;
import com.model.UserInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.service.UserInfoService;
import com.spp.SppaConstant;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout.OnLoadListener;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout.OnRefreshListener;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;

@SuppressLint("ResourceAsColor")
public class ActHotActivity extends BaseActivity implements IXListViewListener{
	DisplayImageOptions options;
	public ImageLoader imageLoader = ImageLoader.getInstance();
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	private Handler mHandler;
	XListView lv_activity;
	static int _pageIndex = 1;
	int _pageCount = 1;
	int oldCount;
	LoadingDialog dialog;
	int count = 0;
	private LinearLayout mEmpty_ll;
	private LinearLayout mNetless_ll;
	private TextView tv_re1,tv_reload;
	@Override
	public void setView() {
		setContentView(R.layout.act_hotactivity);

		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		
		
		_pageIndex = 1;
	    _pageCount = 1;
		/**
		 * 初始化
		 */
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));		
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(null)
		.showImageForEmptyUri(null)
		.showImageOnFail(null)
		.cacheInMemory(true)//设置内存缓存
		.cacheOnDisk(true)//设置磁盘缓存
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(1))//设置图片的半径，可通过这个参数设置圆角图片
		.build();
		
		
		
		_userInfoService = new UserInfoService(ActHotActivity.this);
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					break;

				default:
					break;
				}
				if(dialog != null && dialog.isShowing())
					dialog.dismiss();
			}
		};
		
		
	}

	@Override
	public void initView() {
		lv_activity = (XListView) findViewById(R.id.lv_activity);
		lv_activity.setPullLoadEnable(false);
		lv_activity.setPullRefreshEnable(false);
		lv_activity.setXListViewListener(this);
		mEmpty_ll = (LinearLayout)findViewById(R.id.empty_hot);//显示没有数据
		mNetless_ll = (LinearLayout) findViewById(R.id.netless_hot);//显示没有数据
		tv_reload = (TextView) findViewById(R.id.tv_emp_hot);
		tv_re1 = (TextView) findViewById(R.id.tv_net_hot);
		
		try {
			getHotActivity();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		tv_reload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					count = 0;
					getHotActivity();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		tv_re1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					count = 0;
					getHotActivity();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	
	public void onback(View v){
		finish();
	}

	void getHotActivity(){
		if (_pageCount > 0 && _pageIndex > _pageCount) {
			return;
		}
		if (_pageIndex < 1)
			_pageIndex = 1;

		if (_pageIndex > _pageCount)
			_pageIndex = _pageCount;
		
		String  signature = "activitylist.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.page = _pageIndex;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(this);
		if(!SppaConstant.isNetworkAvailable(this)){
			lv_activity.setEmptyView(mNetless_ll);
		}else{
			new HotActivityTask().execute(userInfo);
		}
	}
    
	class HotActivityTask extends AsyncTask<UserInfo, Void, ActivityList>{
		@Override
		protected void onPreExecute() {
			if(count == 0){//首次进入 显示dialog，刷新或加载更多是不显示
				dialog = new LoadingDialog(ActHotActivity.this);
				dialog.show();
				dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
			}
			count++;
			
		}
		@Override
		protected ActivityList doInBackground(UserInfo... params) {
			ActivityList info = null;
			
			info = _userInfoService.getHotActivity(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActivityList result) {
			if(result == null || result.equals(null)){
				lv_activity.setEmptyView(mEmpty_ll);
				mHandler.sendEmptyMessage(-1);
			}else if(result.ret == 0){
				if(result.item.size() > 0 ){
					
					if (result != null && result.item.size() > 0) {

						for (int i = 0; i < result.item.size(); i++) {
							//mData.add(result.item.get(i));
						}
					}
					Message msg = new Message();

					msg.what = 0;

					mHandler.sendMessage(msg);
					lv_activity.setAdapter(new ImageAdapter(result.item));
				}else{
					lv_activity.setEmptyView(mEmpty_ll);
					mHandler.sendEmptyMessage(0);
				}
				//_handler.sendEmptyMessage(0);
			}else{
				Toast.makeText(ActHotActivity.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	

	class ImageAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		private List<Activity_item>  mList;
	    private String userID;
		ImageAdapter(List<Activity_item> list) {
			inflater = LayoutInflater.from(ActHotActivity.this);
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = inflater.inflate(R.layout.hotactivity_item, parent, false);
				holder = new ViewHolder();
				holder.iv_image = (ImageView) view.findViewById(R.id.iv_image);
				holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
				holder.tv_date = (TextView) view.findViewById(R.id.tv_date);
				holder.tv_title =  (TextView) view.findViewById(R.id.tv_title);
				holder.rl_hot = (RelativeLayout) view.findViewById(R.id.rl_hot);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.tv_content.setText(mList.get(position).date);
			holder.tv_date.setText(mList.get(position).date);
			holder.tv_title.setText(mList.get(position).title);
			final String jumpUrl = mList.get(position).jumpUrl;
			
	        imageLoader.displayImage(mList.get(position).img, holder.iv_image, options, animateFirstListener);
	        
	       
	        holder.rl_hot.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent in = new Intent(ActHotActivity.this,ActHotDetail.class);
					in.putExtra("jumpUrl", jumpUrl);
					startActivity(in);
					overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
				}
			});
	        
			
			return view;
		}
	}
	
	private static class ViewHolder {
		TextView tv_content;
		TextView tv_date;
		TextView tv_title;
		ImageView iv_image;
		RelativeLayout rl_hot;
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
	@Override
	protected void onDestroy() {
		AnimateFirstDisplayListener.displayedImages.clear();
		super.onDestroy();
	}



	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}
	
}
