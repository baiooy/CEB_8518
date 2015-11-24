package com.ceb.fragment;



import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;



import com.adapter.AdapterHot;
import com.adapter.AdapterHotZhuan;
import com.caifuline.R;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
import com.model.ProductArray;
import com.model.ProductList_item;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;
import com.view.CircularProgressDrawable;
import com.view.EmptyViewHelper;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "ResourceAsColor", "HandlerLeak" })
public class FragmentHot extends Fragment implements OnClickListener,IXListViewListener {
	private Handler mHandler;
	LinearLayout ivDrawable;
	CircularProgressDrawable drawable;
	Animator currentAnimation;
	TextView tv_progress;
	private ListView listView_zhuan;
	XListView listView_hot;
	private View list_hot,list_hot_zhuan;
	private ViewPager vp;
	private ArrayList<View> views;
	UserInfoService _userInfoService;
	String userID;
	static int _pageIndex = 1;
	int _pageCount = 1;
	int oldCount;
	AdapterHot mAdapterHot;
	private SharedPreferences sp;
	private SharedPreferences ba;
	private Boolean isback;
	private List<ProductList_item> mData;
	private LinearLayout mEmpty_ll;
	private LinearLayout mNetless_ll;
	private TextView tv_reload,tv_reload_hot1;
	LoadingDialog dialog;
	int count = 0;
	private Boolean canRefresh = true;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		_userInfoService = new UserInfoService(getActivity());
		
		_pageIndex = 1;
	    _pageCount = 1;
	    count = 0;
	    mData = new ArrayList<ProductList_item>();
	    
		return inflater.inflate(R.layout.fragment_hot, container, false);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		LayoutInflater inflater =  LayoutInflater.from(getActivity()); 
		list_hot = inflater.inflate(R.layout.list_hot, null);
		list_hot_zhuan = inflater.inflate(R.layout.list_hot_zhuan, null);
		
		mEmpty_ll = (LinearLayout) list_hot.findViewById(R.id.empty_ll);//显示没有数据
		mNetless_ll = (LinearLayout) list_hot.findViewById(R.id.netless_ll);//显示没有数据
		tv_reload = (TextView) list_hot.findViewById(R.id.tv_reload_hot);
		tv_reload_hot1 = (TextView) list_hot.findViewById(R.id.tv_reload_hot1);
//		fr = (FrameLayout)list_hot.findViewById(R.id.parent_);
		listView_hot = (XListView) list_hot.findViewById(R.id.listView_hot);
	//	listView_hot.setAdapter(new AdapterHot(getActivity()));
		listView_hot.setPullLoadEnable(true);
		listView_hot.setXListViewListener(this);
//		try {
//			if(mData != null){
//				mData.clear();
//			}
//			getProductList();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		listView_zhuan = (ListView) list_hot_zhuan.findViewById(R.id.listView_zhuan);
		listView_zhuan.setAdapter(new AdapterHotZhuan(getActivity()));
		
		
		
		vp = (ViewPager) getActivity().findViewById(R.id.vp_hot);
		initViewpage();
		
		mHandler = new Handler(){
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					mData = (List<ProductList_item>) msg.obj;
					mAdapterHot = new AdapterHot(getActivity(),mData);
					mAdapterHot.notifyDataSetChanged();
					listView_hot.setAdapter(mAdapterHot);
					
					
					if (_pageIndex > 1 && mData.size() > oldCount + 1) {
						listView_hot.smoothScrollBy(oldCount, 1000);
						listView_hot.setSelectionFromTop(oldCount, 100);
					} else if (mData.size() > oldCount) {
//						listView_hot.smoothScrollBy(oldCount, 1000);
//						listView_hot.setSelectionFromTop(oldCount, 100);
					}
					break;
				
				default:
					break;
				}
				if(dialog!=null && dialog.isShowing())
					dialog.dismiss();
			}
		};
		
		tv_reload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					 count = 0;
					getProductList();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		tv_reload_hot1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					 count = 0;
					getProductList();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
		super.onActivityCreated(savedInstanceState);
	}
	

	private void initViewpage(){
		views=new ArrayList<View>();  
		
		views.add(list_hot);
//		views.add(list_hot_zhuan);
		vp.setAdapter(new MyViewPagerAdapter(views));
		vp.setCurrentItem(0);
		
	}
    
	public class MyViewPagerAdapter extends PagerAdapter{
		private List<View> mListViews;  
        
        public MyViewPagerAdapter(List<View> mListViews) {  
            this.mListViews = mListViews;  
        } 
		@Override
		public int getCount() {
			
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mListViews.get(position), 0); 
			
            return mListViews.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));
		}
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.btn_list:
//			btn_list.setTextColor(getResources().getColor(R.color.white));
//			btn_zhuan.setTextColor(getResources().getColor(R.color.zhusediao));
//			btn_list.setBackgroundResource(R.drawable.button_11_2x);
//			btn_zhuan.setBackgroundResource(R.drawable.button_12_2x);
//			vp.setCurrentItem(0);
//			
//			break;
//		case R.id.btn_zhuan:
//			
//			btn_list.setTextColor(getResources().getColor(R.color.zhusediao));
//			btn_zhuan.setTextColor(getResources().getColor(R.color.white));
//			btn_list.setBackgroundResource(R.drawable.button_10_2x);
//			btn_zhuan.setBackgroundResource(R.drawable.button_13_2x);
//			vp.setCurrentItem(1);
//			
//			break;
		default:
			break;
		}
		
	}


	
	
	@Override
		public void onResume() {
		ba = getActivity().getSharedPreferences("back", 0);
		isback = ba.getBoolean("back", false);
		Log.i("back", "hot"+isback);
		if(isback){
			;
		}else{
			_pageIndex = 1;
			if(mData != null){
				mData.clear();
			}
			getProductList();
		}
		SharedPreferences Back = getActivity().getSharedPreferences("back", 0);
		SharedPreferences.Editor editor2 = Back.edit();
		editor2.clear();
		editor2.commit();
		
		
			super.onResume();
		}
	
	
	void getProductList(){
		
		 sp = getActivity().getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		
		if (_pageCount > 0 && _pageIndex > _pageCount) {
			listView_hot.stopLoadMore();
			
			return;
		}
		if (_pageIndex < 1)
			_pageIndex = 1;

		if (_pageIndex > _pageCount)
			_pageIndex = _pageCount;
		
		String  signature = "list.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.productStatus = "0";
		userInfo.page = _pageIndex;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(getActivity());
		
		if(!SppaConstant.isNetworkAvailable(getActivity())){
			listView_hot.setEmptyView(mNetless_ll);
		}else{
			new ProductListTask().execute(userInfo);
		}
	}
	
	class ProductListTask extends AsyncTask<UserInfo, Void, ProductArray>{

		@Override
		protected void onPreExecute() {
			if(count == 0){//首次进入 显示dialog，刷新或加载更多是不显示
				dialog = new LoadingDialog(getActivity());
				dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
				dialog.show();
				}
				count++;
		}
		@Override
		protected ProductArray doInBackground(UserInfo... params) {
			ProductArray info = null;
			
			info = _userInfoService.getProductList(params[0]);
			
			Log.i("info", "info =="+info);
			return info;
		}
		
		@Override
		protected void onPostExecute(ProductArray result) {
			if(listView_hot != null){
				listView_hot.stopRefresh();
				listView_hot.stopLoadMore();
			}
			canRefresh = true;
			Log.i("result", "result =="+result);
			if(result == null || result.toString().equals("null")){
				listView_hot.setEmptyView(mEmpty_ll);
				mHandler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0")){
				if( result.item.size() > 0){
					
				int pages = Integer.parseInt(result.count);
				int page = Integer.parseInt(result.page);
				if(pages%10 == 0){
					_pageCount = pages/10;
				}else{
					_pageCount = (pages+10)/10;
				}
				
				if(page == _pageCount){//显示到最后一页，底部加载更多隐藏
					listView_hot.setPullLoadEnable(false);
				}else{
					listView_hot.setPullLoadEnable(true);
				}
				
				oldCount = mData.size();
				
				if (result != null && result.item.size() > 0) {

					for (int i = 0; i < result.item.size(); i++) {
						mData.add(result.item.get(i));
					}
				}
				
				Message msg = new Message();
				if (mData.size() == 0) {
					msg.obj = result;
				} else {
					msg.obj = mData;
				}

				msg.what = 0;

				mHandler.sendMessage(msg);
				
				}else{
					listView_hot.setEmptyView(mEmpty_ll);
				}
			}else{
				
				Toast.makeText(getActivity(), result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}

	@Override
	public void onLoadMore() {
		_pageIndex++;
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				getProductList();
			}
		}, 1000);
		
	}
	@Override
	public void onRefresh() {
		try {
			if(canRefresh){
				canRefresh = false;
				_pageIndex = 1;
				
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						if(mData != null){
							mData.clear();
						}
						getProductList();
					}
				}, 1000);
				
			}else{
				if(listView_hot != null){
					listView_hot.stopRefresh();
					listView_hot.stopLoadMore();
				}
				return;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
