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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import com.caifuline.R;
import com.ceb.activity.ActTouBiaoResult.ShareTask;
import com.ceb.base.BaseActivity;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
import com.model.ActionResultInfo;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;

public class ActRegisterSucceed extends BaseActivity {

	private String userID,loginname,str;
	private Button btn_huifutianxia;
	UserInfoService _userInfoService;
	private Handler _handler;
	private TextView tv_loginname,tv_str;
	ImageButton btn_shareregis;
	LoadingDialog dialog;
	private SharedPreferences sp;
	private String pid,title ,content,outerContent, imgurl,htmlurl;
	@Override
	public void setView() {
		setContentView(R.layout.act_registersucceed);
		
		Intent in = getIntent();
		userID = in.getStringExtra("userID");
		loginname = in.getStringExtra("loginname");
		str = in.getStringExtra("str");
		_userInfoService = new UserInfoService(this);

		_handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					break;
				case 1:
//					showShare(title,outerContent,imgurl,htmlurl);
					break;
				default:
					break;
				}
				if(dialog != null & dialog.isShowing())
					dialog.dismiss();
			};
		};
	}

	@Override
	public void initView() {
		btn_huifutianxia = (Button) findViewById(R.id.btn_huifutianxia);
		btn_shareregis = (ImageButton) findViewById(R.id.btn_shareregis);
//		tv_loginname = (TextView) findViewById(R.id.tv_loginname);
		tv_str = (TextView) findViewById(R.id.tv_str);
		tv_str.setText(str);
		
		try {
			share();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void setListener() {
		btn_huifutianxia.setOnClickListener(this);
		btn_shareregis.setOnClickListener(this);

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_huifutianxia:
			Intent in = new Intent(ActRegisterSucceed.this,ActHuifu.class);
			in.putExtra("url", openhuifu());
			startActivity(in);
			finish();
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.btn_shareregis:
			showShare(title,outerContent,imgurl,htmlurl);
			break;
		default:
			break;
		}
	}
	
	private void showShare(String title,String con,String imageurl,String htmlurl) {
		Log.i("showShare", title+"///"+con+"///"+imageurl+"///"+htmlurl);
		 OnekeyShare oks = new OnekeyShare();
		 htmlurl = htmlurl+"?pid="+getSharedPreferences("PID", 0).getString("pid", "4209");
		 con = con+"\n注册邀请码："+getSharedPreferences("PID", 0).getString("pid", "4209");
			oks.setTitle(title);
			oks.setTitleUrl(htmlurl);
			oks.setText(con);
			oks.setImageUrl(imageurl);
			oks.setUrl(htmlurl);
			oks.setComment(getString(R.string.share));
			oks.disableSSOWhenAuthorize();
			oks.setComment(getString(R.string.share));
			oks.setSite(getString(R.string.app_name));
			oks.setSiteUrl("http://bao.8518.com");
			oks.setVenueName("ShareSDK");
//			// text是分享文本，所有平台都需要这个字段
//					oks.setText("ceb8518");
			oks.setVenueDescription("This is a beautiful place!");
			oks.setLatitude(23.056081f);
			oks.setLongitude(113.385708f);
			oks.disableSSOWhenAuthorize();
			oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
				public void onShare(Platform platform, ShareParams paramsToShare) {
					// 改写twitter分享内容中的text字段，否则会超长，
					// 因为twitter会将图片地址当作文本的一部分去计算长度
					if ("Twitter".equals(platform.getName())) {
					}
				}
			});

		// 启动分享GUI
		 oks.show(this);
		 }
	
	
	private String openhuifu(){
	
		String  signature = "payaccounts.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.loginname = loginname;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(ActRegisterSucceed.this);
		
		
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
				Toast.makeText(ActRegisterSucceed.this, result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	void share(){
		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		String  signature = "sharefriend.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.type = "0";//注册分享
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(this);
		
		if(SppaConstant.isNetworkAvailable(this)){
			new ShareTask().execute(userInfo);
		}
	}
	
	class ShareTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{


		@Override
		protected void onPreExecute() {
			dialog = new LoadingDialog(ActRegisterSucceed.this);
			dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
			dialog.show();
		}
		
		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.getShare(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result == null){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret != null & result.ret.equals("0")){
//				pid = result.pid;
				title = result.title;
				content = result.content;
				outerContent = result.outerContent;
				imgurl = result.imgurl;
				htmlurl = result.htmlurl;
				
				Log.i("onPostExecute", title+"///"+content+"///"+imgurl+"///"+htmlurl);
				_handler.sendEmptyMessage(1);
			}else{
				Toast.makeText(ActRegisterSucceed.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	
	public void onback(View v){
		finish();
	}

}
