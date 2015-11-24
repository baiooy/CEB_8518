package com.ceb.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.AdapterTrade;
import com.caifuline.R;
import com.ceb.base.BaseActivity;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
import com.model.TradeDetailList;
import com.model.TradeDetail_item;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;
import com.view.EmptyViewHelper;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout.OnRefreshListener;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;

@SuppressLint({ "HandlerLeak", "ResourceAsColor" })
public class ActTradeDetail extends BaseActivity implements IXListViewListener{
//	private SwipeRefreshLayout swipe_trade;
	private XListView listView_trade;
	private Handler mHandler;
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	static int _pageIndex = 1;
	int _pageCount = 1;
	int oldCount;
	AdapterTrade mAdapter;
	ArrayList<TradeDetail_item> mData;
	LoadingDialog dialog;
	int count = 0;
	private LinearLayout mEmpty_ll;
	private LinearLayout mNetless_ll;
	private LinearLayout loading_trade;
	private TextView tv_re1,tv_reload;
	@Override
	public void setView() {
		setContentView(R.layout.act_tradedetail);
		
		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		
		_pageIndex = 1;
	    _pageCount = 1;
	    count = 0;
	    mData = new ArrayList<TradeDetail_item>();
		
		_userInfoService = new UserInfoService(ActTradeDetail.this);

	}

	@SuppressWarnings("deprecation")
	@Override
	public void initView() {
		listView_trade = (XListView) findViewById(R.id.listView_trade);
		listView_trade.setPullLoadEnable(true);
		listView_trade.setPullRefreshEnable(false);
		listView_trade.setXListViewListener(this);
		mEmpty_ll = (LinearLayout)findViewById(R.id.empty_trade);//显示没有数据
		mNetless_ll = (LinearLayout) findViewById(R.id.netless_trade);//显示没有数据
		loading_trade = (LinearLayout) findViewById(R.id.loading_trade);
		tv_reload = (TextView) findViewById(R.id.tv_emp_trade);
		tv_re1 = (TextView) findViewById(R.id.tv_net_trade);
		mHandler = new Handler(){
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case -1:
//					Toast.makeText(ActTradeDetail.this,
//							"网络不给力，麻烦重试~o(>_<)o",
//							Toast.LENGTH_SHORT).show();
					break;
				case 0:
					mData = (ArrayList<TradeDetail_item>) msg.obj;
					
					mAdapter = new AdapterTrade(ActTradeDetail.this,mData);
					mAdapter.notifyDataSetChanged();
					listView_trade.setAdapter(mAdapter);

					if (_pageIndex > 1 && mData.size() > oldCount + 1) {
						listView_trade.smoothScrollBy(oldCount, 1000);
						listView_trade.setSelectionFromTop(oldCount, 100);
					} else if (mData.size() > oldCount) {
						listView_trade.smoothScrollBy(oldCount, 1000);
						listView_trade.setSelectionFromTop(oldCount, 100);
					}
					break;

				default:
					break;
				}
				if(dialog != null && dialog.isShowing())
					dialog.dismiss();
			}
		};
		
		tv_reload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					count = 0;
					getTradeDetail();
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
					getTradeDetail();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		

		try {
			getTradeDetail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	@Override
	public void setListener() {

	}
	
	@Override
	public void onClick(View v) {

	}
	public void onback(View v){
		finish();
	}
	
	
	void getTradeDetail(){
		if (_pageCount > 0 && _pageIndex > _pageCount) {
			listView_trade.stopLoadMore();
			return;
		}
		if (_pageIndex < 1)
			_pageIndex = 1;

		if (_pageIndex > _pageCount)
			_pageIndex = _pageCount;
		
		String  signature = "deal.php:"+System.currentTimeMillis()/1000+":hxpay";
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
			listView_trade.setEmptyView(mNetless_ll);
		}else{
			new TradeDetailTask().execute(userInfo);
		}
	}
    
	class TradeDetailTask extends AsyncTask<UserInfo, Void, TradeDetailList>{
		@Override
		protected void onPreExecute() {
			if(count == 0){//首次进入 显示dialog，刷新或加载更多是不显示
				dialog = new LoadingDialog(ActTradeDetail.this);
				dialog.show();
				dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
	//			listView_trade.setEmptyView(loading_trade);
			}
			count++;
			
		}
		
		@Override
		protected TradeDetailList doInBackground(UserInfo... params) {
//			listView_trade.setXListViewListener(null);
			TradeDetailList info = null;
			
			info = _userInfoService.getTradeDetail(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(TradeDetailList result) {
//			listView_trade.setXListViewListener(ActTradeDetail.this);
			if(listView_trade != null){
				listView_trade.stopRefresh();
				listView_trade.stopLoadMore();
			}
			Log.i("result", "result =="+result);
			if(result == null || result.equals(null)){
				listView_trade.setEmptyView(mEmpty_ll);
				mHandler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0")){
				if(result.item.size() > 0 ){
					int pages = Integer.parseInt(result.count);
					int page = Integer.parseInt(result.page);
					if(pages%10 == 0){
						_pageCount = pages/10;
					}else{
						_pageCount = (pages+10)/10;
					}
					
					if(page == _pageCount){//显示到最后一页，底部加载更多隐藏
						listView_trade.setPullLoadEnable(false);
					}else{
						listView_trade.setPullLoadEnable(true);
					}
					
		//			_pageCount = pages/10+pages%10;;
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
					listView_trade.setEmptyView(mEmpty_ll);
				}
				//_handler.sendEmptyMessage(0);
			}else{
				Toast.makeText(ActTradeDetail.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}

//	@Override
//	public void onLoad() {
//		_pageIndex++;
//		try {
//			getTradeDetail();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	@Override
	public void onRefresh() {
		_pageIndex=1;
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(mData != null){
					mData.clear();
				}
				getTradeDetail();
			}
		}, 500);
		try {
			_pageIndex = 1;
			mData.clear();
			getTradeDetail();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onLoadMore() {
		
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				_pageIndex++;
				getTradeDetail();
			}
		}, 1000);
		
		
	}
	
	

}
