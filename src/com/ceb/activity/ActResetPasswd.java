package com.ceb.activity;

import java.net.URLEncoder;
import java.util.TreeMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.caifuline.R;
import com.ceb.activity.ActFindBackPasswd.VercodeTask;
import com.ceb.base.BaseActivity;
import com.ceb.widge.MD5Util;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.model.ActionResultInfo;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;

public class ActResetPasswd extends BaseActivity {
	private Button btn_ok;
	private TextView tv_show;
	private EditText et_passwd;
	private boolean isShowPass = false;
	private Handler _handler;
	private String userID,mPasswd;
	UserInfoService _userInfoService;
	private SharedPreferences user;
	@Override
	public void setView() {
		setContentView(R.layout.act_resetpasswd);

		_userInfoService = new UserInfoService(this);
		
		user = getSharedPreferences("USERID", 0);
		userID = user.getString("userID","0");
		
		Log.i("userID", userID);
		_handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(ActResetPasswd.this,
							"网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
					Intent in = new Intent(ActResetPasswd.this,MainActivity.class);
					startActivity(in);
					overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
					finish();
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
	}

	@Override
	public void initView() {
		btn_ok = (Button) findViewById(R.id.btn_ok);
		tv_show = (TextView) findViewById(R.id.tv_show);
		et_passwd = (EditText) findViewById(R.id.et_passwd);
	}

	@Override
	public void setListener() {
		btn_ok.setOnClickListener(this);
		tv_show.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			if(et_passwd.getText().toString().equals("")){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setContentText("请输入新密码!")
                .show();
			}else{
				
				resetPasswd();
				
			}
			break;
		case R.id.tv_show:
			if (!isShowPass) {
				show();
			} else {
				hide();
			}
			/**
			 * 移动edittext的光标到文字的末尾处
			 */
			Editable etext = et_passwd.getText();
			Selection.setSelection(etext, etext.length());
			break;

		default:
			break;
		}
	}
	
	/**
	 * 密码显示
	 */
	public void show() {
		et_passwd.setTransformationMethod(HideReturnsTransformationMethod
				.getInstance());
		tv_show.setText("隐藏");
		isShowPass = true;
	}

	@SuppressWarnings("deprecation")
	void resetPasswd(){
		String  signature = "resetpassword.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.passwd = MD5Util.MD5(et_passwd.getText().toString());
//		Log.i("mPasswd--md5", mPasswd);
//		Log.i("mPasswd--MD5Util", userInfo.passwd);
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(this);
		
		new ResetPasswdTask().execute(userInfo);
	}
	
	class ResetPasswdTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{

		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.resetPasswd(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result == null){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0")){
	//			Toast.makeText(ActResetPasswd.this,result.msg , Toast.LENGTH_SHORT).show();
				_handler.sendEmptyMessage(0);
			}else{
				Toast.makeText(ActResetPasswd.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	
	
	
	
	
	/**
	 * 密码隐藏
	 */
	public void hide() {
		et_passwd.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		
		tv_show.setText("显示");
		isShowPass = false;
	}
	
	public void onback(View v){
		finish();
	}

}
