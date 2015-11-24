package com.ceb.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import com.caifuline.R;
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

@SuppressLint("HandlerLeak")
public class ActYaoqing extends BaseActivity {
	private RelativeLayout rl_lijiyaoqing,rl_chakanyaoqingjilu;
	private Boolean ispartner = false;//判断是否为合伙人
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	private Handler _handler;
	private String testImage;
	private static final String FILE_NAME = "/pic_lovely_cats.jpg";
	LoadingDialog dialog;
	private String pid,title ,content,outerContent, imgurl,htmlurl;
	private SharedPreferences isLogin;
	private Boolean islogin = false;
	private String flag = "";
	@Override
	public void setView() {
		setContentView(R.layout.act_yaoqing);

		_userInfoService = new UserInfoService(ActYaoqing.this);
//		new Thread() {
//			public void run() {
//				initImagePath();
//			}
//		}.start();
		_handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					if(!ispartner){
						Intent ii = new Intent(ActYaoqing.this, ActInviteList.class);
						ii.putExtra("flag", flag);
						startActivity(ii);
						overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
					}else{
						Intent ii = new Intent(ActYaoqing.this, ActInviteListNoPartner.class);
						startActivity(ii);
						overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
					}
					break;
				case 1:
					showShare(title,outerContent,imgurl,htmlurl);
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
		rl_lijiyaoqing = (RelativeLayout) findViewById(R.id.rl_lijiyaoqing);
		rl_chakanyaoqingjilu = (RelativeLayout) findViewById(R.id.rl_chakanyaoqingjilu);

		
	}

	private void initImagePath() {
		try {
			String cachePath = com.mob.tools.utils.R.getCachePath(this, null);
			testImage = cachePath + FILE_NAME;
			File file = new File(testImage);
			if (!file.exists()) {
				file.createNewFile();
				Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.ceb_icon);
				FileOutputStream fos = new FileOutputStream(file);
				pic.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch(Throwable t) {
			t.printStackTrace();
			testImage = null;
		}
	}
	
	@Override
	public void setListener() {
		rl_lijiyaoqing.setOnClickListener(this);
		rl_chakanyaoqingjilu.setOnClickListener(this);

	}
	

//	@Override
//	protected void onResume() {
//		try {
//			isPartner();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		super.onResume();
//	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_lijiyaoqing:
			isLogin = getSharedPreferences("isLogin", 0);
			islogin = isLogin.getBoolean("islogin", false);
			if (islogin) {// 已登录
				try {
					share();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else {
				Intent in = new Intent(ActYaoqing.this, ActUserYanzheng.class);
				startActivity(in);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
			
			
		
			break;
		case R.id.rl_chakanyaoqingjilu:
			isLogin = getSharedPreferences("isLogin", 0);
			islogin = isLogin.getBoolean("islogin", false);
			if (islogin) {// 已登录
				try {
					isPartner();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Intent in = new Intent(ActYaoqing.this, ActUserYanzheng.class);
				startActivity(in);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
			
			break;

		default:
			break;
		}

	}
	
	private void showShare(String title,String con,String imageurl,String htmlurl) {
		Log.i("showShare", title+"///"+con+"///"+imageurl+"///"+htmlurl);
		SharedPreferences sp = getSharedPreferences("PID", 0);
		 htmlurl = htmlurl+"?pid="+sp.getString("pid", "4209");
		 con = con+"\n注册邀请码："+sp.getString("pid", "4209");
		 OnekeyShare oks = new OnekeyShare();
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
//		 //关闭sso授权
//		 oks.disableSSOWhenAuthorize(); 

		// 启动分享GUI
		 oks.show(this);
		 }
	
	public void onback(View v) {
		finish();
	}
	
	
	void isPartner(){
		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		String  signature = "invitefriend.php:"+System.currentTimeMillis()/1000+":hxpay";
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
		
		if(SppaConstant.isNetworkAvailable(this)){
			new PartnerTask().execute(userInfo);
		}
		
	}
	
	class PartnerTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{


		@Override
		protected void onPreExecute() {
			dialog = new LoadingDialog(ActYaoqing.this);
			dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
			dialog.show();
		}
		
		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.getIsartner(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result == null){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret != null & result.ret.equals("0")){
				if(result.partnerType.equals("1")){
					ispartner = true;
					flag = "1";
				}else if(result.partnerType.equals("0")){
					ispartner = false;
					flag = "0";
				}else{
					ispartner = false;
					flag = "99";
				}
				_handler.sendEmptyMessage(0);
			}else{
				Toast.makeText(ActYaoqing.this,result.msg , Toast.LENGTH_SHORT).show();
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
		userInfo.type = "4";//立即邀请分享
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
			dialog = new LoadingDialog(ActYaoqing.this);
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
				pid = result.pid;
				title = result.title;
				content = result.content;
				outerContent = result.outerContent;
				imgurl = result.imgurl;
				htmlurl = result.htmlurl;
				Log.i("onPostExecute", title+"///"+content+"///"+imgurl+"///"+htmlurl);
				_handler.sendEmptyMessage(1);
			}else{
				Toast.makeText(ActYaoqing.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	

}
