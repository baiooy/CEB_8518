package com.ceb.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.TreeMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;

import com.caifuline.R;
import com.ceb.activity.ActYaoqing.ShareTask;
import com.ceb.base.BaseActivity;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
import com.mob.tools.utils.UIHandler;
import com.model.ActionResultInfo;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;
import com.view.BgView;

public class ActShareToFriends extends BaseActivity {
	private Button btn_share;
	private String testImage;
	private static String FILE_NAME = null;
	private TextView tv_content,tv_title;
	private ImageView iv_img;
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	private Handler _handler;
	LoadingDialog dialog;
	private String pid,title ,content,outerContent, imgurl,htmlurl;
	@Override
	public void setView() {
		setContentView(R.layout.act_sharetofriends);
		_userInfoService = new UserInfoService(ActShareToFriends.this);
		//Log.i("onPostExecute", title+"///"+content+"///"+imgurl+"///"+htmlurl);
		
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		content = intent.getStringExtra("content");
		imgurl = intent.getStringExtra("imgurl");
		htmlurl = intent.getStringExtra("htmlurl");
		outerContent = intent.getStringExtra("outerContent");
//		try {
//			share();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
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
		btn_share = (Button) findViewById(R.id.btn_share);
		tv_content = (TextView) findViewById(R.id.tv_content);
		iv_img = (ImageView) findViewById(R.id.iv_img);
		tv_title = (TextView) findViewById(R.id.tv_title);
		
		tv_title.setText(title);
		tv_content.setText(content);
		new BgView(ActShareToFriends.this, imgurl, iv_img, "");
		
	}

	@Override
	protected void onStop() {
		FILE_NAME = null;
		testImage = null;
		super.onStop();
	}
	
	@Override
	public void setListener() {
		btn_share.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_share:
			showShare(title,outerContent,imgurl,htmlurl);
			break;

		default:
			break;
		}

	}

	private void initImagePath(int id) {
		try {
			String cachePath = com.mob.tools.utils.R.getCachePath(this, null);
			testImage = cachePath + FILE_NAME;
			File file = new File(testImage);
			if (!file.exists()) {
				file.createNewFile();
				Bitmap pic = BitmapFactory.decodeResource(getResources(),
						id);
				FileOutputStream fos = new FileOutputStream(file);
				pic.compress(CompressFormat.JPEG, 1000, fos);
				fos.flush();
				fos.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			testImage = null;
		}
		Log.i("initImagePath", "testImage=="+testImage);
	}

	private void showShare(String title,String con,String imageurl,String htmlurl) {
		Log.i("showShare", title+"///"+con+"///"+imageurl+"///"+htmlurl);
		 htmlurl = htmlurl+"?pid="+getSharedPreferences("PID", 0).getString("pid", "4209");
		 con = con+"\n注册邀请码："+getSharedPreferences("PID", 0).getString("pid", "4209");
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
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 
//		 Platform weibo = ShareSDK.getPlatform(ActShareToFriends.this, SinaWeibo.NAME);
//	//	 weibo.setPlatformActionListener(paListener);
//		 weibo.authorize();
		 //移除授权
		 //weibo.removeAccount();
		 
//		 Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
//		 weibo.SSOSetting(false);  //设置false表示使用SSO授权方式
//	//	 weibo.setPlatformActionListener(this); // 设置分享事件回调
//		 weibo.authorize();
		 
		// 启动分享GUI
		 oks.show(this);
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
		userInfo.type = "1";
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
			dialog = new LoadingDialog(ActShareToFriends.this);
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
				tv_title.setText(title);
				tv_content.setText(content);
				new BgView(ActShareToFriends.this, imgurl, iv_img, "");
				
				Log.i("onPostExecute", title+"///"+content+"///"+imgurl+"///"+htmlurl);
				_handler.sendEmptyMessage(1);
			}else{
				Toast.makeText(ActShareToFriends.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	

}
