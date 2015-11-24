package com.ceb.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.TreeMap;

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

import com.adapter.AdapterMessage;
import com.adapter.AdapterNotice;
import com.caifuline.R;
import com.ceb.activity.ActMyMessage.MessageTask;
import com.ceb.base.BaseActivity;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
import com.model.MessageArray;
import com.model.Message_item;
import com.model.NoticeArray;
import com.model.Notice_item;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;
import com.view.EmptyViewHelper;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;

public class ActSyatemMsg extends BaseActivity  implements IXListViewListener{
	XListView listView_smessage;
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	static int _pageIndex = 1;
	int _pageCount = 1;
	int oldCount;
	private Handler mHandler;
	private ArrayList<Notice_item> mData;
	AdapterNotice mAdapter;
	LoadingDialog dialog;
	int count = 0;
	private LinearLayout mEmpty_ll;
	private LinearLayout mNetless_ll;
	private TextView tv_re1,tv_reload;
	@Override
	public void setView() {
		setContentView(R.layout.act_systemmsg);
		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		count = 0;
		_pageIndex = 1;
	    _pageCount = 1;
		
	    mData = new ArrayList<Notice_item>();
		
		_userInfoService = new UserInfoService(this);
		
		mHandler = new Handler(){
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					
					mData = (ArrayList<Notice_item>) msg.obj;
					
					mAdapter = new AdapterNotice(ActSyatemMsg.this,mData);
					mAdapter.notifyDataSetChanged();
					listView_smessage.setAdapter(mAdapter);

					
//					tv_tips1.setVisibility(View.GONE);
//					tv_tips2.setVisibility(View.GONE);
					if (_pageIndex > 1 && mData.size() > oldCount + 1) {
						listView_smessage.smoothScrollBy(oldCount, 1000);
						listView_smessage.setSelectionFromTop(oldCount, 100);
					} else if (mData.size() > oldCount) {
						listView_smessage.smoothScrollBy(oldCount, 1000);
						listView_smessage.setSelectionFromTop(oldCount, 100);
					}
					
					
					break;
				case -1:
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
		listView_smessage = (XListView) findViewById(R.id.listView_smessage);
		listView_smessage.setPullLoadEnable(true);
		listView_smessage.setXListViewListener(this);
		mEmpty_ll = (LinearLayout)findViewById(R.id.empty_smsg);//显示没有数据
		mNetless_ll = (LinearLayout) findViewById(R.id.netless_smsg);//显示没有数据
		tv_reload = (TextView) findViewById(R.id.tv_emp_smsg);
		tv_re1 = (TextView) findViewById(R.id.tv_net_smsg);
		
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
//	@Override
//	public void setView() {
//		setContentView(R.layout.act_systemmsg);
//
//		sp = getSharedPreferences("USERID", 0);
//		userID = sp.getString("userID", "0");
//		count = 0;
//		_pageIndex = 1;
//	    _pageCount = 1;
//		
//	    mData = new ArrayList<Notice_item>();
//		
//		_userInfoService = new UserInfoService(ActSyatemMsg.this);
//		
//		mHandler = new Handler(){
//			@SuppressWarnings("unchecked")
//			@Override
//			public void handleMessage(Message msg) {
//				switch (msg.what) {
//				case 0:
//					
//					mData = (ArrayList<Notice_item>) msg.obj;
//					
//					mAdapter = new AdapterNotice(ActSyatemMsg.this,mData);
//					mAdapter.notifyDataSetChanged();
//					listView_smessage.setAdapter(mAdapter);
//
//					
////					tv_tips1.setVisibility(View.GONE);
////					tv_tips2.setVisibility(View.GONE);
//					if (_pageIndex > 1 && mData.size() > oldCount + 1) {
//						listView_smessage.smoothScrollBy(oldCount, 1000);
//						listView_smessage.setSelectionFromTop(oldCount, 100);
//					} else if (mData.size() > oldCount) {
//						listView_smessage.smoothScrollBy(oldCount, 1000);
//						listView_smessage.setSelectionFromTop(oldCount, 100);
//					}
//					
//					
//					break;
//				case -1:
//					new EmptyViewHelper(listView_smessage, R.layout.netless_view, fr);
//					break;
//				case 2:
//					
//				break;
//				default:
//					break;
//				}
//				dialog.dismiss();
//			}
//		};
//		
//		try {
//			getMessage();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void initView() {
//		fr = (FrameLayout)findViewById(R.id.parent);
//		listView_smessage = (XListView) findViewById(R.id.listView_smessage);
//		listView_smessage.setPullLoadEnable(true);
//		listView_smessage.setXListViewListener(this);
//
//	}
//
//	@Override
//	public void setListener() {
//		// TODO Auto-generated method stub
//
//	}
//
//	
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//
//	}

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
	
	void getMessage(){
		if (_pageCount > 0 && _pageIndex > _pageCount) {
			listView_smessage.stopLoadMore();
			return;
		}
		if (_pageIndex < 1)
			_pageIndex = 1;

		if (_pageIndex > _pageCount)
			_pageIndex = _pageCount;
		
		String  signature = "notice.php:"+System.currentTimeMillis()/1000+":hxpay";
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
			listView_smessage.setEmptyView(mNetless_ll);
		}else{
			new MessageTask().execute(userInfo);
		}
	}
	
	class MessageTask extends AsyncTask<UserInfo, Void, NoticeArray>{

		@Override
		protected void onPreExecute() {
			if(count == 0){//首次进入 显示dialog，刷新或加载更多是不显示
			dialog = new LoadingDialog(ActSyatemMsg.this);
			dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
			dialog.show();
			}
			count++;
		}
		@Override
		protected NoticeArray doInBackground(UserInfo... params) {
			NoticeArray info = null;
			
			info = _userInfoService.getOsMessage(params[0]);
			
			Log.i("info", "info =="+info);
			return info;
		}
		
		@Override
		protected void onPostExecute(NoticeArray result) {
			if(listView_smessage != null){
				listView_smessage.stopRefresh();
				listView_smessage.stopLoadMore();
			}
			Log.i("result", "result =="+result);
			if(result == null || result.equals(null)){
				listView_smessage.setEmptyView(mEmpty_ll);
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
					listView_smessage.setPullLoadEnable(false);
				}else{
					listView_smessage.setPullLoadEnable(true);
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
				listView_smessage.setEmptyView(mEmpty_ll);
				Toast.makeText(ActSyatemMsg.this, result.msg , Toast.LENGTH_SHORT).show();
				mHandler.sendEmptyMessage(2);
			}
		}
		
	}
//	public void onback(View v){
//		finish();
//	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		
	}

}
