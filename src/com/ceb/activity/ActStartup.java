package com.ceb.activity;


import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.TreeMap;

import com.baidu.push.example.MyPushMessageReceiver;
import com.caifuline.R;
import com.ceb.base.BaseActivity;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.model.BannerArray;
import com.model.BannerInfo;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;
import com.view.BgView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;


@SuppressLint("HandlerLeak")
public class ActStartup extends BaseActivity {
	private ImageView iv_banner;
	private SharedPreferences baiduID;
	private String userId,channelId;
	private String userID;
	private String role;
	UserInfoService _userInfoService;
	private Handler _handler;
	ArrayList<BannerInfo> banner;
	private String img;
	private String dur;
	private SharedPreferences sp_opengesture = null;
	private Boolean isopengesture = false; //默认开启
	private SharedPreferences isNewUser;
	private Boolean isNew = true; //默认开启
	@Override
	public void setView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_startup);
		
		
		isNewUser = getSharedPreferences("isNewUser", 0);
		isNew = isNewUser.getBoolean("isNewUser", false);
		sp_opengesture = getSharedPreferences("OPENGESTURE", 0);
		isopengesture = sp_opengesture.getBoolean("isopengesture", false);
		
		Log.i("isopengesture", ""+isopengesture);
		
		_userInfoService = new UserInfoService(this);
		
//		userId = MyPushMessageReceiver.getUserId();
//		channelId = MyPushMessageReceiver.getChannelId();
//		
//		SharedPreferences baiduID = getSharedPreferences("baiduID", 0);
//		SharedPreferences.Editor editor = baiduID.edit();
//		editor.clear();
//		editor.commit();
//		editor.putString("userId", userId);
//		editor.putString("channelId", channelId);
//		editor.commit();
		
		
		baiduID = getSharedPreferences("baiduID", 0);
		
		userId = baiduID.getString("userId", "null");
		channelId = baiduID.getString("channelId", "null"); 
		
		Log.i("ActStartup ==baiduID", "userId ==>"+userId+"  //channelId ====>"+channelId);
		
		userID = getIntent().getStringExtra("userID");
		role = getIntent().getStringExtra("role");
		startup();
		_handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
//					Toast.makeText(ActStartup.this,
//							"网络不给力，麻烦重试~o(>_<)o",
//							Toast.LENGTH_SHORT).show();
					//保证在没有网络的情况下，顺利通过
					iv_banner.setImageResource(R.drawable.startup);
					new Thread(){
						public void run() {
							try {
								Thread.sleep(2000);
								if(isopengesture && !isNew){
									_handler.sendEmptyMessage(4);
								}else{
									_handler.sendEmptyMessage(2);
								}
								
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						};
					}.start();
					
					break;
				case 0:
					Log.i("img", "img"+img);
					new BgView(ActStartup.this, img, iv_banner ,"");
					
					_handler.sendEmptyMessage(3);
					
					break;
				case 3:
					new Thread(){
						public void run() {
							try {
								Thread.sleep(Integer.parseInt(dur)*1000);
								if(isopengesture){
									_handler.sendEmptyMessage(4);
								}else{
									_handler.sendEmptyMessage(2);
								}
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						};
					}.start();
					break;
				case 2:
					Intent bob = new Intent(ActStartup.this,
							MainActivity.class);
					
//					bob.putExtra("userID", userID);
//					bob.putExtra("role", role);
					startActivity(bob);
					ActStartup.this.finish();
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					break;
				case 4:
					Intent bo = new Intent(ActStartup.this,
							GestureVerifyActivity.class);
					bo.putExtra("in", "in");
					startActivity(bo);
					ActStartup.this.finish();
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					break;
				default:
					break;
				}
			};
		};
		
	//	_handler.sendEmptyMessage(-1);
	}

	@Override
	public void initView() {
		iv_banner= (ImageView) findViewById(R.id.iv_banner);
		
	}

	@Override
	public void setListener() {

	}
	
	
	void startup(){
		String  signature = "startup.php:"+System.currentTimeMillis()/1000+":hxpay";
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
		userInfo.user_id = userId;
		userInfo.channel_id = channelId;
		
		new BannerTask().execute(userInfo);
	}
	
	class BannerTask extends AsyncTask<UserInfo, Void, BannerArray>{

		@Override
		protected BannerArray doInBackground(UserInfo... params) {
			BannerArray info = null;
			
			info = _userInfoService.startup(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(BannerArray result) {
			Log.i("result == null", (result == null)+"");
			if(result == null){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0")){
				banner = result.item;
				for(int i=0 ;i< banner.size();i++){
					img = banner.get(i).img;
					dur = banner.get(i).dur;
				}
				_handler.sendEmptyMessage(0);
			}else{
				;//Toast.makeText(ActStartup.this, , Toast.LENGTH_SHORT).show();
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
