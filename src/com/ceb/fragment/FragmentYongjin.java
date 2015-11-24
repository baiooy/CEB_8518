package com.ceb.fragment;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.TreeMap;

import com.adapter.AdapterYongjin;
import com.caifuline.R;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
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

public class FragmentYongjin extends Fragment implements IXListViewListener{
	private XListView listView_yongjin;
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
	private ArrayList<Yongjin_item> mData;
	AdapterYongjin mAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		sp = getActivity().getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		
		_pageIndex = 1;
	    _pageCount = 1;
	    count = 0;
		_userInfoService = new UserInfoService(getActivity());
		mData = new ArrayList<Yongjin_item>();
		
		_handler = new Handler(){
			@SuppressWarnings("unchecked")
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					break;
				case 0:
					mData = (ArrayList<Yongjin_item>) msg.obj;
					
					mAdapter = new AdapterYongjin(getActivity(),mData,_handler);
					mAdapter.notifyDataSetChanged();
					listView_yongjin.setAdapter(mAdapter);

					if (_pageIndex > 1 && mData.size() > oldCount + 1) {
						listView_yongjin.smoothScrollBy(oldCount, 1000);
						listView_yongjin.setSelectionFromTop(oldCount, 100);
					} else if (mData.size() > oldCount) {
						listView_yongjin.smoothScrollBy(oldCount, 1000);
						listView_yongjin.setSelectionFromTop(oldCount, 100);
					}
					break;
				case 3:
					Toast.makeText(getActivity(),
							"暂无数据",
							Toast.LENGTH_SHORT).show();
					break;
				case 11:
					try {
						if(mData != null){
							mData.clear();
						}
						count = 0;
						getYongjinData();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
				if(dialog!=null && dialog.isShowing())
					dialog.dismiss();
			};
		};
		
		 return inflater.inflate(R.layout.fragment_yongjin, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mEmpty_ll = (LinearLayout) getActivity().findViewById(R.id.empty_yongjin);//显示没有数据
		mNetless_ll = (LinearLayout) getActivity().findViewById(R.id.netless_yongjin);//显示没有数据
//		loading_yongjin = (LinearLayout) getActivity().findViewById(R.id.loading_yongjin);
		tv_reload = (TextView) getActivity().findViewById(R.id.tv_emp_yongjin);
		tv_re1 = (TextView) getActivity().findViewById(R.id.tv_net_yongjin);
		listView_yongjin = (XListView) getActivity().findViewById(R.id.listView_yongjin);
		listView_yongjin.setPullLoadEnable(false);
		listView_yongjin.setPullRefreshEnable(true);
		listView_yongjin.setXListViewListener(this);
		
		try {
			getYongjinData();
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
					getYongjinData();
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
					getYongjinData();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
		
		
		
		super.onActivityCreated(savedInstanceState);
	}

	void getYongjinData(){
		if (_pageCount > 0 && _pageIndex > _pageCount) {
			listView_yongjin.stopLoadMore();
			return;
		}
		if (_pageIndex < 1)
			_pageIndex = 1;

		if (_pageIndex > _pageCount)
			_pageIndex = _pageCount;
		
		String  signature = "awardlist.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.type = "4";
		userInfo.page = _pageIndex;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(getActivity());
		if(!SppaConstant.isNetworkAvailable(getActivity())){
			listView_yongjin.setEmptyView(mNetless_ll);
		}else{
			new YongjinTask().execute(userInfo);
		}
	}
    
	class YongjinTask extends AsyncTask<UserInfo, Void, YongjinList>{
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
		protected YongjinList doInBackground(UserInfo... params) {
			YongjinList info = null;
			
			info = _userInfoService.getYongjinData(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(YongjinList result) {
			if(listView_yongjin != null){
				listView_yongjin.stopRefresh();
				listView_yongjin.stopLoadMore();
			}
			
			Log.i("result == null", (result==null)+"");
			if(result == null || result.equals(null)){
				listView_yongjin.setEmptyView(mEmpty_ll);
				_handler.sendEmptyMessage(-1);
				
			}else if(result.ret.equals("null")){
				_handler.sendEmptyMessage(3);
			}else if(result.ret.equals("0") ){
				if(result.item.size() > 0 ){
					int pages = Integer.parseInt(result.count);
					int page = Integer.parseInt(result.page);
					if(pages%10 == 0){
						_pageCount = pages/10;
					}else{
						_pageCount = (pages+10)/10;
					}
					oldCount = mData.size();
					
					if(page == _pageCount){//显示到最后一页，底部加载更多隐藏
						listView_yongjin.setPullLoadEnable(false);
					}else{
						listView_yongjin.setPullLoadEnable(true);
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

					_handler.sendMessage(msg);
					
					
				}else{
				//	Toast.makeText(getActivity(),result.msg , Toast.LENGTH_SHORT).show();
					listView_yongjin.setEmptyView(mEmpty_ll);
				}
			}else{
		//		new EmptyViewHelper(listView_yongjin, R.layout.empty_view, fr);
				Toast.makeText(getActivity(),result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
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
				getYongjinData();
			}
		}, 1000);
	}
	
	@Override
	public void onLoadMore() {
		
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				_pageIndex++;
				getYongjinData();
			}
		}, 1000);
	}
	
	
	
	

}
