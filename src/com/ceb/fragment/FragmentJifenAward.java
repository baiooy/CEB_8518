package com.ceb.fragment;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.TreeMap;

import com.adapter.AdapterInvest;
import com.caifuline.R;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
import com.model.InviteList;
import com.model.NoPartner;
import com.model.UserInfo;
import com.model.YongjinList;
import com.model.Yongjin_item;
import com.service.UserInfoService;
import com.spp.SppaConstant;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentJifenAward extends Fragment implements IXListViewListener{
	private XListView listView_jifen;
	private LinearLayout mEmpty_ll;
	private LinearLayout mNetless_ll;
	private TextView tv_re1,tv_reload;
	LoadingDialog dialog;
	private Handler mHandler;
	int count = 0;
//	private LinearLayout loading_yongjin;
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	private Handler _handler;
	static int _pageIndex = 1;
	int _pageCount = 1;
	int oldCount;
	private ArrayList<NoPartner> mData;
	AdapterInvest mAdapter;
	private TextView tv_number,tv_invitenum;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		sp = getActivity().getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		
		_pageIndex = 1;
	    _pageCount = 1;
	    count = 0;
		_userInfoService = new UserInfoService(getActivity());
		mData = new ArrayList<NoPartner>();
		
		_handler = new Handler(){
			@SuppressWarnings("unchecked")
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					break;
				case 0:
					mData = (ArrayList<NoPartner>) msg.obj;

					mAdapter = new AdapterInvest(getActivity(),mData);
					mAdapter.notifyDataSetChanged();
					listView_jifen.setAdapter(mAdapter);

					if (_pageIndex > 1 && mData.size() > oldCount + 1) {
						listView_jifen.smoothScrollBy(oldCount, 1000);
						listView_jifen.setSelectionFromTop(oldCount, 100);
					} else if (mData.size() > oldCount) {
						listView_jifen.smoothScrollBy(oldCount, 1000);
						listView_jifen.setSelectionFromTop(oldCount, 100);
					}
					break;
				case 3:
					Toast.makeText(getActivity(),
							"暂无数据",
							Toast.LENGTH_SHORT).show();
					break;
				case 11:
				
					break;
				default:
					break;
				}
				if(dialog!=null && dialog.isShowing())
					dialog.dismiss();
			};
		};
		
		 return inflater.inflate(R.layout.fragment_jifen, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mEmpty_ll = (LinearLayout) getActivity().findViewById(R.id.empty_jifen);//显示没有数据
		mNetless_ll = (LinearLayout) getActivity().findViewById(R.id.netless_jifen);//显示没有数据
//		loading_yongjin = (LinearLayout) getActivity().findViewById(R.id.loading_yongjin);
		tv_reload = (TextView) getActivity().findViewById(R.id.tv_emp_jifen);
		tv_re1 = (TextView) getActivity().findViewById(R.id.tv_net_jifen);
		listView_jifen = (XListView) getActivity().findViewById(R.id.listView_jifen);
		listView_jifen.setPullLoadEnable(false);
		listView_jifen.setPullRefreshEnable(true);
		listView_jifen.setXListViewListener(this);
		tv_number = (TextView) getActivity().findViewById(R.id.tv_number2);
		tv_invitenum = (TextView) getActivity().findViewById(R.id.tv_invitenum2);
//		listView_jifen.setAdapter(new AdapterInvest(getActivity()));
		try {
			getInviteList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					break;

				default:
					break;
				}
			}
		};
		
		tv_reload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					count = 0;
					getInviteList();
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
					getInviteList();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
		
		
		
		super.onActivityCreated(savedInstanceState);
	}

	
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				_pageIndex = 1;
				if(mData != null){
					mData.clear();
				}
				getInviteList();
			}
		}, 1000);
	}
	
	@Override
	public void onLoadMore() {
		
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				_pageIndex++;
				getInviteList();
			}
		}, 1000);
	}
	
	void getInviteList(){
		if (_pageCount > 0 && _pageIndex > _pageCount) {
			listView_jifen.stopLoadMore();
			return;
		}
		if (_pageIndex < 1)
			_pageIndex = 1;

		if (_pageIndex > _pageCount)
			_pageIndex = _pageCount;
		
		String  signature = "invitelist.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.type = "1";//积分
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.page = _pageIndex;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(getActivity());
		
		if(!SppaConstant.isNetworkAvailable(getActivity())){
			listView_jifen.setEmptyView(mNetless_ll);
		}else{
			new InviteTask().execute(userInfo);
		}
	}
	
	class InviteTask extends AsyncTask<UserInfo, Void, InviteList>{
		
		@Override
		protected void onPreExecute() {
			dialog = new LoadingDialog(getActivity());
			dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
			dialog.show();
		}

		@Override
		protected InviteList doInBackground(UserInfo... params) {
			InviteList info = null;
			
			info = _userInfoService.getInviteList(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(InviteList result) {
			if(listView_jifen != null){
				listView_jifen.stopRefresh();
				listView_jifen.stopLoadMore();
			}
			
			if(result == null){
				listView_jifen.setEmptyView(mEmpty_ll);
				_handler.sendEmptyMessage(-1);
			}else if(result != null && result.points_item.size() < 1 ){
				tv_invitenum.setText(result.invitenum+" 人");
				tv_number.setText(result.number+" 分");
				listView_jifen.setEmptyView(mEmpty_ll);
				_handler.sendEmptyMessage(-1);
			}else if(result.ret != null & result.ret.equals("0")){
				tv_invitenum.setText(result.invitenum+" 人");
				tv_number.setText(result.number+" 分");
				
				int pages = Integer.parseInt(result.count);
				int page = Integer.parseInt(result.page);
				if(pages%10 == 0){
					_pageCount = pages/10;
				}else{
					_pageCount = (pages+10)/10;
				}
				oldCount = mData.size();
				
				if(page == _pageCount){//显示到最后一页，底部加载更多隐藏
					listView_jifen.setPullLoadEnable(false);
				}else{
					listView_jifen.setPullLoadEnable(true);
				}
				
				if (result != null && result.points_item.size() > 0) {

					for (int i = 0; i < result.points_item.size(); i++) {
						mData.add(result.points_item.get(i));
					}
				}
				Message msg = new Message();
				if (mData.size() == 0) {
					msg.obj = result;
				} else {
					msg.obj = mData;
				}

				msg.what = 0;

				_handler.sendMessage(msg);
				
				
				
//				
//				mAdapter = new AdapterInvest(getActivity(),result.noPartner_item);
//				mAdapter.notifyDataSetChanged();
//				listView_jifen.setAdapter(mAdapter);
//				_handler.sendEmptyMessage(2);
			}else{
				Toast.makeText(getActivity(),result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	
	

}
