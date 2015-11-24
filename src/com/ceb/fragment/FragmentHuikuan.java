package com.ceb.fragment;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.TreeMap;

import com.adapter.AdapterDuifu;
import com.adapter.AdapterHuikuan;
import com.caifuline.R;
import com.ceb.fragment.FragmentMainPage.QualityTask;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
import com.model.ActionResultInfo;
import com.model.HuikuanList;
import com.model.Huikuan_item;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;
import com.view.EmptyViewHelper;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class FragmentHuikuan extends Fragment  implements IXListViewListener{
	private XListView listView_huikuan;
	private Handler mHandler;
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	private Handler _handler;
	static int _pageIndex = 1;
	int _pageCount = 1;
	int oldCount;
	AdapterHuikuan mAdapter;
	private LinearLayout mEmpty_ll;
	private LinearLayout mNetless_ll;
	private TextView tv_re1,tv_reload;
	private ArrayList<Huikuan_item> mData;
	LoadingDialog dialog;
	int count = 0;
	private LinearLayout loading_hui;
    public FragmentHuikuan() {
        super();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	sp = getActivity().getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		
		_pageIndex = 1;
	    _pageCount = 1;
	    count = 0;
		_userInfoService = new UserInfoService(getActivity());
		mData = new ArrayList<Huikuan_item>();
		_handler = new Handler(){
			@SuppressWarnings("unchecked")
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					break;
				case 0:
					mData = (ArrayList<Huikuan_item>) msg.obj;
					
					mAdapter = new AdapterHuikuan(getActivity(),mData);
					mAdapter.notifyDataSetChanged();
					listView_huikuan.setAdapter(mAdapter);

					if (_pageIndex > 1 && mData.size() > oldCount + 1) {
						listView_huikuan.smoothScrollBy(oldCount, 1000);
						listView_huikuan.setSelectionFromTop(oldCount, 100);
					} else if (mData.size() > oldCount) {
						listView_huikuan.smoothScrollBy(oldCount, 1000);
						listView_huikuan.setSelectionFromTop(oldCount, 100);
					}
					break;
				case 3:
					Toast.makeText(getActivity(),
							"暂无数据",
							Toast.LENGTH_SHORT).show();
					break;
				case 2:
					break;
				default:
					break;
				}
				if(dialog!=null && dialog.isShowing())
					dialog.dismiss();
			};
		};
    	
		
		
        return inflater.inflate(R.layout.fragment_huikuan, container, false);
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		
		mEmpty_ll = (LinearLayout) getActivity().findViewById(R.id.empty_hui);//显示没有数据
		mNetless_ll = (LinearLayout) getActivity().findViewById(R.id.netless_hui);//显示没有数据
		loading_hui = (LinearLayout) getActivity().findViewById(R.id.loading_hui);
		tv_reload = (TextView) getActivity().findViewById(R.id.tv_emp_hui);
		tv_re1 = (TextView) getActivity().findViewById(R.id.tv_net_hui);
		listView_huikuan = (XListView) getActivity().findViewById(R.id.listView_huikuan);
		listView_huikuan.setPullLoadEnable(true);
		listView_huikuan.setPullRefreshEnable(false);
		listView_huikuan.setXListViewListener(this);
//		listView_huikuan.setAdapter(new AdapterHuikuan(getActivity()));
		
		try {
			getHuikuanData();
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
					getHuikuanData();
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
					getHuikuanData();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
		super.onActivityCreated(savedInstanceState);
	}
	
	void getHuikuanData(){
		if (_pageCount > 0 && _pageIndex > _pageCount) {
			listView_huikuan.stopLoadMore();
			return;
		}
		if (_pageIndex < 1)
			_pageIndex = 1;

		if (_pageIndex > _pageCount)
			_pageIndex = _pageCount;
		
		String  signature = "investlist.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.productType = "1";//非转让
		userInfo.type = "40";
		userInfo.page = _pageIndex;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(getActivity());
		if(!SppaConstant.isNetworkAvailable(getActivity())){
			listView_huikuan.setEmptyView(mNetless_ll);
		}else{
			new HuikuanTask().execute(userInfo);
		}
	}
    
	class HuikuanTask extends AsyncTask<UserInfo, Void, HuikuanList>{
		@Override
		protected void onPreExecute() {
			if(count == 0){//首次进入 显示dialog，刷新或加载更多是不显示
			dialog = new LoadingDialog(getActivity());
			dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
			dialog.show();
	//		listView_huikuan.setEmptyView(loading_hui);
			}
			count++;
		}
		@Override
		protected HuikuanList doInBackground(UserInfo... params) {
			HuikuanList info = null;
			
			info = _userInfoService.getHuikuanData(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(HuikuanList result) {
			if(listView_huikuan != null){
				listView_huikuan.stopRefresh();
				listView_huikuan.stopLoadMore();
			}
			
			Log.i("result == null", (result==null)+"");
			if(result == null || result.equals(null)){
			//	new EmptyViewHelper(listView_huikuan, R.layout.empty_view, fr);
				listView_huikuan.setEmptyView(mEmpty_ll);
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
						listView_huikuan.setPullLoadEnable(false);
					}else{
						listView_huikuan.setPullLoadEnable(true);
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
					listView_huikuan.setEmptyView(mEmpty_ll);
				}
			}else{
		//		new EmptyViewHelper(listView_huikuan, R.layout.empty_view, fr);
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
				getHuikuanData();
			}
		}, 1000);
	}
	
	@Override
	public void onLoadMore() {
		
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				_pageIndex++;
				getHuikuanData();
			}
		}, 1000);
	}
	
	
	

}
