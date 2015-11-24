package com.ceb.activity;

import java.net.URLEncoder;
import java.util.TreeMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import com.caifuline.R;
import com.ceb.activity.ActShareToFriends.ShareTask;
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
import com.view.BgView;

public class ActTouBiaoResult extends BaseActivity {
	private String RespCode;
	private TextView tv_title,tv1,tv_biaoti  ,tv_backhot,tv_backtouzi;
	ImageButton btn_share;
	private String mType,title,type;
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	private Handler _handler;
	LoadingDialog dialog;
	private String pid,ptitle ,content,outerContent, imgurl,htmlurl;
	@Override
	public void setView() {
		setContentView(R.layout.act_toubiaoresult);
		_userInfoService = new UserInfoService(ActTouBiaoResult.this);
		Intent in = getIntent();
		mType = in.getStringExtra("type");
		title = in.getStringExtra("title");
		RespCode = in.getStringExtra("RespCode");
		
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
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv_biaoti  = (TextView) findViewById(R.id.tv_biaoti);
		tv_backhot = (TextView) findViewById(R.id.tv_backhot);
		tv_backtouzi = (TextView) findViewById(R.id.tv_backtouzi);
		btn_share = (ImageButton) findViewById(R.id.btn_share);
		tv_biaoti.setText("超额宝"+title+"期");
		if(RespCode != null && RespCode.equals("000")){
			tv_title.setText("投标成功");
			btn_share.setVisibility(View.VISIBLE);
			tv1.setText("恭喜您，投标成功！");
		}else{
			tv_title.setText("投标失败");
			btn_share.setVisibility(View.INVISIBLE);
			tv1.setText("很遗憾，投标失败！");
		}
		if(mType!=null & mType.equals("2")){
			type = "2";
		}else{
			type = "3";
		}
		
		try {
			share();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setListener() {
		btn_share.setOnClickListener(this);
		tv_backhot.setOnClickListener(this);
		tv_backtouzi.setOnClickListener(this);
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_share:
			showShare(ptitle,outerContent,imgurl,htmlurl);
			break;
		case R.id.tv_backhot:
			Intent ii = new Intent(ActTouBiaoResult.this,MainActivity.class);
			MainActivity.instance.finish();
			ii.putExtra("hot", "hot");
			startActivity(ii);
			ActTouBiaoResult.this.finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			
			break;
		case R.id.tv_backtouzi:
			Intent iin = new Intent(ActTouBiaoResult.this,ActInvestHistory.class);
			startActivity(iin);
			ActTouBiaoResult.this.finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			
			break;
		default:
			break;
		}

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
//		 //关闭sso授权
//		 oks.disableSSOWhenAuthorize(); 

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
		userInfo.type = type;
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
			dialog = new LoadingDialog(ActTouBiaoResult.this);
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
				ptitle = result.title;
				content = result.content;
				outerContent = result.outerContent;
				imgurl = result.imgurl;
				htmlurl = result.htmlurl;
				
				Log.i("onPostExecute", title+"///"+content+"///"+imgurl+"///"+htmlurl);
				_handler.sendEmptyMessage(1);
			}else{
				Toast.makeText(ActTouBiaoResult.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	

}
