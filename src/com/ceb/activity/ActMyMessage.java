package com.ceb.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.TreeMap;

import com.adapter.AdapterMessage;
import com.caifuline.R;
import com.ceb.base.BaseActivity;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
import com.model.MessageArray;
import com.model.Message_item;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;
import com.view.EmptyViewHelper;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("ResourceAsColor")
public class ActMyMessage extends BaseActivity implements IXListViewListener{
	XListView listView_mymessage;
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	static int _pageIndex = 1;
	int _pageCount = 1;
	int oldCount;
	private Handler mHandler;
	private ArrayList<Message_item> mData;
	AdapterMessage mAdapter;
	LoadingDialog dialog;
	private LinearLayout mEmpty_ll;
	private LinearLayout mNetless_ll;
	private TextView tv_re1,tv_reload;
	int count = 0;
//	@Override
//	public void setView() {
//		setContentView(R.layout.act_mymessage);
//		sp = getSharedPreferences("USERID", 0);
//		userID = sp.getString("userID", "0");
//		count = 0;
//		_pageIndex = 1;
//	    _pageCount = 1;
//		
//	    mData = new ArrayList<Message_item>();
//		
//		_userInfoService = new UserInfoService(this);
//		
//		mHandler = new Handler(){
//			@SuppressWarnings("unchecked")
//			@Override
//			public void handleMessage(Message msg) {
//				switch (msg.what) {
//				case 0:
//					
//					mData = (ArrayList<Message_item>) msg.obj;
//					
//					mAdapter = new AdapterMessage(ActMyMessage.this,mData);
//					mAdapter.notifyDataSetChanged();
//					listView_mymessage.setAdapter(mAdapter);
//
//					
////					tv_tips1.setVisibility(View.GONE);
////					tv_tips2.setVisibility(View.GONE);
//					if (_pageIndex > 1 && mData.size() > oldCount + 1) {
//						listView_mymessage.smoothScrollBy(oldCount, 1000);
//						listView_mymessage.setSelectionFromTop(oldCount, 100);
//					} else if (mData.size() > oldCount) {
//						listView_mymessage.smoothScrollBy(oldCount, 1000);
//						listView_mymessage.setSelectionFromTop(oldCount, 100);
//					}
//					
//					
//					break;
//				case -1:
//					break;
//				case 2:
//					
//				break;
//				default:
//					break;
//				}
//				if(dialog!=null && dialog.isShowing())
//					dialog.dismiss();
//			}
//		};
//		
//		
//	}
	
	@Override
	public void setView() {
		setContentView(R.layout.act_mymessage);

		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		count = 0;
		_pageIndex = 1;
	    _pageCount = 1;
		
	    mData = new ArrayList<Message_item>();
		
		_userInfoService = new UserInfoService(ActMyMessage.this);
		
		mHandler = new Handler(){
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					
					mData = (ArrayList<Message_item>) msg.obj;
					
					mAdapter = new AdapterMessage(ActMyMessage.this,mData);
					mAdapter.notifyDataSetChanged();
					listView_mymessage.setAdapter(mAdapter);

					
//					tv_tips1.setVisibility(View.GONE);
//					tv_tips2.setVisibility(View.GONE);
					if (_pageIndex > 1 && mData.size() > oldCount + 1) {
						listView_mymessage.smoothScrollBy(oldCount, 1000);
						listView_mymessage.setSelectionFromTop(oldCount, 100);
					} else if (mData.size() > oldCount) {
						listView_mymessage.smoothScrollBy(oldCount, 1000);
						listView_mymessage.setSelectionFromTop(oldCount, 100);
					}
					
					
					break;
				case -1:
//					new EmptyViewHelper(listView_mymessage, R.layout.netless_view, fr);
					break;
				case 2:
					
				break;
				default:
					break;
				}
				dialog.dismiss();
			}
		};
		
		
	}
	
	@Override
	public void initView() {
		listView_mymessage = (XListView) findViewById(R.id.listView_mymessage);
		listView_mymessage.setPullLoadEnable(true);
		listView_mymessage.setXListViewListener(this);
		mEmpty_ll = (LinearLayout)findViewById(R.id.empty_msg);//显示没有数据
		mNetless_ll = (LinearLayout) findViewById(R.id.netless_msg);//显示没有数据
		tv_reload = (TextView) findViewById(R.id.tv_emp_msg);
		tv_re1 = (TextView) findViewById(R.id.tv_net_msg);
		
		try {
			getMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		tv_reload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					count = 0;
					getMessage();
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
					getMessage();
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

//	@Override
//	public void onLoad() {
//		_pageIndex++;
//		try {
//			getMessage();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				_pageIndex = 1;
				if(mData != null){
					mData.clear();
				}
				getMessage();
			}
		}, 1000);
	}
	
	void getMessage(){
		if (_pageCount > 0 && _pageIndex > _pageCount) {
			listView_mymessage.stopLoadMore();
			return;
		}
		if (_pageIndex < 1)
			_pageIndex = 1;

		if (_pageIndex > _pageCount)
			_pageIndex = _pageCount;
		
		String  signature = "message.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.productStatus = "22";
		userInfo.page = _pageIndex;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(this);
		
		if(!SppaConstant.isNetworkAvailable(this)){
			listView_mymessage.setEmptyView(mNetless_ll);
		}else{
			new MessageTask().execute(userInfo);
		}
	}
	
	class MessageTask extends AsyncTask<UserInfo, Void, MessageArray>{

		@Override
		protected void onPreExecute() {
			if(count == 0){//首次进入 显示dialog，刷新或加载更多是不显示
			dialog = new LoadingDialog(ActMyMessage.this);
			dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
			dialog.show();
			}
			count++;
		}
		@Override
		protected MessageArray doInBackground(UserInfo... params) {
			MessageArray info = null;
			
			info = _userInfoService.getMessage(params[0]);
			
			Log.i("info", "info =="+info);
			return info;
		}
		
		@Override
		protected void onPostExecute(MessageArray result) {
			if(listView_mymessage != null){
				listView_mymessage.stopRefresh();
				listView_mymessage.stopLoadMore();
			}
			Log.i("result", "result =="+result);
			if(result == null || result.toString().equals("null")){
				listView_mymessage.setEmptyView(mEmpty_ll);
				mHandler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0") && result.item.size() > 0){
				int pages = Integer.parseInt(result.count);
				int page = Integer.parseInt(result.page);
				if(pages%10 == 0){
					_pageCount = pages/10;
				}else{
					_pageCount = (pages+10)/10;
				}
				oldCount = mData.size();
				
				if(page == _pageCount){//显示到最后一页，底部加载更多隐藏
					listView_mymessage.setPullLoadEnable(false);
				}else{
					listView_mymessage.setPullLoadEnable(true);
				}
				
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
				listView_mymessage.setEmptyView(mEmpty_ll);
				Toast.makeText(ActMyMessage.this, result.msg , Toast.LENGTH_SHORT).show();
				mHandler.sendEmptyMessage(2);
			}
		}
		
	}

	@Override
	public void onLoadMore() {
		
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				_pageIndex++;
				getMessage();
			}
		}, 1000);
	}
	
}
