package com.ceb.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.TreeMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.adapter.AdapterBankCard;
import com.adapter.AdapterTrade;
import com.caifuline.R;
import com.caifuline.R.color;
import com.ceb.activity.ActTradeDetail.TradeDetailTask;
import com.ceb.base.BaseActivity;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
import com.model.BankCardList;
import com.model.BankCard_item;
import com.model.TradeDetailList;
import com.model.TradeDetail_item;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;
import com.view.EmptyViewHelper;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;

public class ActBankCardManage extends BaseActivity implements IXListViewListener{
	private TextView tv_addbankcard;
	private XListView listView_bankcard;
//	private SwipeRefreshLayout swipe_bankcard;
//	private TextView tv_tips_bankcard;
	private Handler mHandler;
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	AdapterBankCard mAdapter;
	String userAccountType;
	int count = 0;
	private LinearLayout mEmpty_ll;
	private LinearLayout mNetless_ll,loading_bank;
	private TextView tv_re1,tv_reload;
	LoadingDialog dialog;
	Boolean kuaijie = false;
	ArrayList<BankCard_item> mData;
	@Override
	public void setView() {
		setContentView(R.layout.act_bankcardmanage);

		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		count = 0;
		Intent in = getIntent();
		userAccountType = in.getStringExtra("userAccountType");
		mData = new ArrayList<BankCard_item>();
		_userInfoService = new UserInfoService(ActBankCardManage.this);
		
		
	}

	@Override
	public void initView() {
		tv_addbankcard = (TextView) findViewById(R.id.tv_addbankcard);
		listView_bankcard = (XListView) findViewById(R.id.listView_bankcard);
		listView_bankcard.setPullLoadEnable(false);
		listView_bankcard.setXListViewListener(this);
		mEmpty_ll = (LinearLayout)findViewById(R.id.empty_bank);//显示没有数据
		mNetless_ll = (LinearLayout) findViewById(R.id.netless_bank);//显示没有数据
		loading_bank = (LinearLayout) findViewById(R.id.loading_bank);
		tv_reload = (TextView) findViewById(R.id.tv_emp_bank);
		tv_re1 = (TextView) findViewById(R.id.tv_net_bank);
		
		try {
			getBankCardManage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					break;
				case 1:
					break;
				case -1:
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
					getBankCardManage();
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
					getBankCardManage();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
	
	}
	
	@Override
	public void setListener() {

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_addbankcard://跳入汇付天下
			/*if(kuaijie){
				new SweetAlertDialog(ActBankCardManage.this, SweetAlertDialog.WARNING_TYPE)
				.setTitleText("Oops...").setContentText("您已经开通了快捷卡，不可以换绑!")
				.show();
			}else */if(userAccountType.equals("1")){
				startActivity(new Intent(ActBankCardManage.this,ActAddBankCard.class));
				overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			}else{
//					new SweetAlertDialog(ActBankCardManage.this, SweetAlertDialog.WARNING_TYPE)
//					.setContentText("先开通汇付天下账户，才可以绑定银行卡!")
//					.show();
					
					 new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
		             .setContentText("先开通汇付天下账户，才可以绑定银行卡!")
		             .setCancelText("取消")
		             .setConfirmText("去开通")
		             .showCancelButton(true)
		             .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
		                 @Override
		                 public void onClick(SweetAlertDialog sDialog) {
		                    sDialog.dismiss();
		                 }
		             })
		             .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
		                 @Override
		                 public void onClick(SweetAlertDialog sDialog) {
		                	 Intent in = new Intent(ActBankCardManage.this, ActHuifu.class);
		         			startActivity(in);
		         			overridePendingTransition(android.R.anim.fade_in,
		         					android.R.anim.fade_out);
		                 	sDialog.dismiss();
		                 }
		             })
		             .show();
			}
			
			break;

		default:
			break;
		}

	}
	
	public void onback(View v){
		finish();
	}
	
	void getBankCardManage(){
		String  signature = "bank.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(this);
		if(!SppaConstant.isNetworkAvailable(this)){
			listView_bankcard.setEmptyView(mNetless_ll);
		}else{
			new BankCardTask().execute(userInfo);
		}
	}
    
	class BankCardTask extends AsyncTask<UserInfo, Void, BankCardList>{
		@Override
		protected void onPreExecute() {
			if(count == 0){//首次进入 显示dialog，刷新或加载更多是不显示
				dialog = new LoadingDialog(ActBankCardManage.this);
				dialog.show();
				dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
//				listView_bankcard.setEmptyView(loading_bank);
			}
			count++;
			
		}
		
		@Override
		protected BankCardList doInBackground(UserInfo... params) {
			BankCardList info = null;
			
			info = _userInfoService.getBankCardList(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(BankCardList result) {
			if(listView_bankcard != null){
				listView_bankcard.stopRefresh();
				listView_bankcard.stopLoadMore();
			}
			if(result == null || result.equals(null)){
				mHandler.sendEmptyMessage(-1);
				listView_bankcard.setEmptyView(mEmpty_ll);
				tv_addbankcard.setOnClickListener(ActBankCardManage.this);
			}else if(result.ret == 0){
				if(result.item.size() > 0 ){
					mData = (ArrayList<BankCard_item>) result.item;
					mHandler.sendEmptyMessage(0);
					mAdapter = new AdapterBankCard(ActBankCardManage.this, mData);
					mAdapter.notifyDataSetChanged();
					listView_bankcard.setAdapter(mAdapter);
				}else{
					listView_bankcard.setEmptyView(mEmpty_ll);
					mHandler.sendEmptyMessage(0);
				//	Toast.makeText(ActBankCardManage.this,result.msg , Toast.LENGTH_SHORT).show();
				}
				
				/**
				 * 判断是否已绑定快捷卡
				 */
				
				for(int i=0;i<result.item.size();i++){
					kuaijie = kuaijie || (result.item.get(i).expressFlag.equals("Y"));
				}
				Log.i("kuaijie", "kuaijie = "+kuaijie);
				if(kuaijie){//已绑定
					tv_addbankcard.setBackgroundColor(Color.rgb(220, 220, 220));
				}else{
					tv_addbankcard.setBackgroundResource(R.drawable.btn_text);
					tv_addbankcard.setOnClickListener(ActBankCardManage.this);
				}
				
				
				
				//_handler.sendEmptyMessage(0);
			}else{
				mHandler.sendEmptyMessage(1);
			//	Toast.makeText(ActBankCardManage.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		try {
			getBankCardManage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(mData != null){
					mData.clear();
				}
				getBankCardManage();
			}
		}, 1000);
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}
	

}
