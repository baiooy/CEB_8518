package com.ceb.activity;

import java.net.URLEncoder;
import java.util.TreeMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.caifuline.R;
import com.ceb.base.BaseActivity;
import com.ceb.widge.ParamSign;
import com.common.Constants;
import com.model.ActionResultInfo;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;

public class ActHuiFuSucceed extends BaseActivity {

	private String userID,loginname;
	private Button btn_backceb;
	UserInfoService _userInfoService;
	private Handler _handler;
	private TextView tv_loginname;
	private SharedPreferences sp;
	@Override
	public void setView() {
		setContentView(R.layout.act_huifukaihusucceed);
		
		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		loginname = sp.getString("loginname", "null");
//		Intent in = getIntent();
//		userID = in.getStringExtra("userID");
//		loginname = in.getStringExtra("loginname");
		_userInfoService = new UserInfoService(this);

	}

	@Override
	public void initView() {
		btn_backceb = (Button) findViewById(R.id.btn_backceb);
		tv_loginname = (TextView) findViewById(R.id.tv_loginname);
		
		tv_loginname.setText(loginname);

	}

	@Override
	public void setListener() {
		btn_backceb.setOnClickListener(this);

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_backceb:
			Intent in = new Intent(ActHuiFuSucceed.this,GestureEditActivity.class);
			in.putExtra("flag", "open");
			startActivity(in);
			finish();
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
		//	Toast.makeText(ActHuiFuSucceed.this, "跳回超额宝", 1000).show();
			
			break;

		default:
			break;
		}
	}
	
	private String openhuifu(){
		String appSecret = Constants.APP_SERECT;
		TreeMap<String, String> paramsMap = new TreeMap<String, String>();
		paramsMap.put("userID", userID);
		@SuppressWarnings("deprecation")
		String signature = URLEncoder.encode(ParamSign.value(paramsMap,
				appSecret));

		UserInfo userInfo = new UserInfo();
		userInfo.signature = signature;
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.loginname = loginname;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(ActHuiFuSucceed.this);
		
		String url = "http://testinterface.8518.com/app/ceb/payaccounts.php?"
				+"&userID="+userInfo.userID
				+"&platformID="+userInfo.platformID
				+"&version="+userInfo.version
				+"&signature="+userInfo.signature
				+"&loginname="+userInfo.loginname
				+"&udid="+userInfo.udid;
		
		return url;
	}
	
	class OpenHuifuTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{

		@Override
		protected void onPreExecute() {
	//		mSwipeRefreshLayout.setRefreshing(true);//一进来就开始刷新
		}
		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
//			info = _userInfoService.openhuifu(params[0]);
			
			Log.i("info", "info =="+info);
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
	//		mSwipeRefreshLayout.setRefreshing(false);
			Log.i("result", "result =="+result);
			if(result == null || result.toString().equals("null")){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0")){
//				String title = result.title;
//				String productID = result.productID;
//				String minInvestment = result.minInvestment;
//				String progress = result.progress;
//				String extraIncome = result.extraIncome;
//				String deadline = result.deadline;
//				
//				mProgress = Integer.parseInt(progress);
//				tv_title.setText(title);
//				tv_extraIncome.setText(extraIncome);
//				tv_progress.setText(progress);
//				tv_deadline.setText(deadline);
//				tv_minInvestment.setText(minInvestment);
//				
				_handler.sendEmptyMessage(0);
			}else{
				Toast.makeText(ActHuiFuSucceed.this, result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	
	public void onback(View v){
		finish();
	}

}
