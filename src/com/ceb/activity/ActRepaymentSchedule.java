package com.ceb.activity;

import java.net.URLEncoder;
import java.util.TreeMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.adapter.AdapterRepaymentschedule;
import com.caifuline.R;
import com.ceb.base.BaseActivity;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.model.InvestplanList;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;

public class ActRepaymentSchedule extends BaseActivity {
	private SwipeRefreshLayout swipe_repaymentschedule;
	private TextView tv_tips_repaymentschedule;
	private ListView listView_repaymentschedule;
	private Handler mHandler;
	private SharedPreferences sp;
	private String userID;
	private String productId;
	UserInfoService _userInfoService;
	private Handler _handler;
	AdapterRepaymentschedule mAdapter;
	@Override
	public void setView() {
		setContentView(R.layout.act_repaymentschedule);

		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		
		Intent in = getIntent();
		productId = in.getStringExtra("productId");
		
		_userInfoService = new UserInfoService(ActRepaymentSchedule.this);
		
		_handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(ActRepaymentSchedule.this,
							"网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
//					Intent in = new Intent(ActRegister.this,ActRegisterSucceed.class);
//					in.putExtra("userID", userID);
//					in.putExtra("loginname", loginname);
//					startActivity(in);
//					finish();
					break;
				case 3:
					break;
				case 2:
					break;
				default:
					break;
				}
			};
		};
		
		try {
			getInvestPlan();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
	}

	@Override
	public void initView() {
		swipe_repaymentschedule = (SwipeRefreshLayout) findViewById(R.id.swipe_repaymentschedule);
		tv_tips_repaymentschedule = (TextView) findViewById(R.id.tv_tips_repaymentschedule);
		listView_repaymentschedule = (ListView) findViewById(R.id.listView_repaymentschedule);
		
	//	listView_repaymentschedule.setAdapter(new AdapterRepaymentschedule(ActRepaymentSchedule.this));
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					tv_tips_repaymentschedule.setVisibility(View.GONE);
					break;

				default:
					break;
				}
			}
		};
		
		swipe_repaymentschedule.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		/**
		 *  解决listview与SwipeRefreshLayout滑动冲突问题
		 */
		listView_repaymentschedule.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
			        int visibleItemCount, int totalItemCount) {
			    boolean enable = false;
			    if(listView_repaymentschedule != null && listView_repaymentschedule.getChildCount() > 0){
			        // check if the first item of the list is visible
			        boolean firstItemVisible = listView_repaymentschedule.getFirstVisiblePosition() == 0;
			        // check if the top of the first item is visible
			        boolean topOfFirstItemVisible = listView_repaymentschedule.getChildAt(0).getTop() == 0;
			        // enabling or disabling the refresh layout
			        enable = firstItemVisible && topOfFirstItemVisible;
			    }
			    swipe_repaymentschedule.setEnabled(enable);
			}});
		
		swipe_repaymentschedule
		.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				tv_tips_repaymentschedule.setVisibility(View.VISIBLE);
				tv_tips_repaymentschedule.setText("正在刷新...");
				
				
				processOnRefresh();
			}
		});

	}
	
	public void processOnRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				tv_tips_repaymentschedule.setText("刷新成功！");
				swipe_repaymentschedule.setRefreshing(false);
				Message msg = new Message();
				msg.what = 0;
				mHandler.sendMessageDelayed(msg, 1000);
				
			}
		}, 3000);
		
	}

	@Override
	public void setListener() {}
	
	@Override
	public void onClick(View v) {}
	
	public void onback(View v){
		finish();
	}
	
	void getInvestPlan(){
		String  signature = "investplan.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;//productID
		userInfo.productID = productId;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(ActRepaymentSchedule.this);

		new InvestPlanTask().execute(userInfo);
	}
    
	class InvestPlanTask extends AsyncTask<UserInfo, Void, InvestplanList>{

		@Override
		protected InvestplanList doInBackground(UserInfo... params) {
			InvestplanList info = null;
			
			info = _userInfoService.getInvestplanData(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(InvestplanList result) {
			if(result == null || result.equals(null)){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0")){
				if(result.item.size() > 0 ){
					mAdapter = new AdapterRepaymentschedule(ActRepaymentSchedule.this, result);
					listView_repaymentschedule.setAdapter(mAdapter);
				}else{
					Toast.makeText(ActRepaymentSchedule.this,result.msg , Toast.LENGTH_SHORT).show();
				}
				_handler.sendEmptyMessage(0);
			}else{
				Toast.makeText(ActRepaymentSchedule.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	


}
