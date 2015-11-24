package com.ceb.activity;

import java.net.URLEncoder;
import java.util.TreeMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.caifuline.R;
import com.ceb.activity.ActRegister.RegisterTask;
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

public class ActUserYanzheng extends BaseActivity {
	private EditText et_phone;
	private Button btn_yanzheng;
	private String userID = "0";
	UserInfoService _userInfoService;
	private Handler _handler;
	SharedPreferences isNewUser;
	String flag,enter,other;
	private LoadingDialog dialog;
	@Override
	public void setView() {
		setContentView(R.layout.act_useryanzheng);

		Intent in = getIntent();
		flag = in.getStringExtra("flag");
		enter = in.getStringExtra("in");
		other = in.getStringExtra("flag");
		Log.i("ActUserYanzheng  enter", "enter"+enter);
		Log.i("ActUserYanzheng  flag", "flag"+flag);
		_userInfoService = new UserInfoService(this);
		isNewUser = getSharedPreferences("isNewUser", 0);
		_handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(ActUserYanzheng.this,
							"网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
//					Intent in = new Intent(ActUserYanzheng.this,ActRegisterSucceed.class);
//					in.putExtra("userID", userID);
//					in.putExtra("loginname", loginname);
//					startActivity(in);
					
					break;
				case 1:
					break;
				case 2:
					break;
				default:
					break;
				}
				if(dialog != null && dialog.isShowing()){
					dialog.dismiss();
				}
			};
		};
	}

	@Override
	public void initView() {
		et_phone = (EditText) findViewById(R.id.et_phone);
		btn_yanzheng = (Button) findViewById(R.id.btn_yanzheng);
	}

	@Override
	public void setListener() {
		btn_yanzheng.setOnClickListener(this);

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_yanzheng:
			hideInput();
			if(et_phone.getText().toString().equals("")){
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setContentText("您还未填手机号/账号!")
                .show();
			}else if(SppaConstant.isCN(et_phone.getText().toString())){
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setContentText("输入格式不正确!")
                .show();
			}else if(et_phone.getText().toString().length()<6 ||et_phone.getText().toString().length()>20){
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setContentText("您输入的长度不正确!")
                .show();
			}else if(SppaConstant.isNumeric(et_phone.getText().toString()) && et_phone.getText().toString().length() != 11){
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setContentText("您输入的手机号长度不正确!")
                .show();

			}else{
				try {
					validate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;

		default:
			break;
		}

	}
	
	public void onback(View v){
		if(flag != null && flag.equals("forget")){
			Intent in1 = new Intent(ActUserYanzheng.this,GestureVerifyActivity.class);
		//	in1.putExtra("flag", "close");
			startActivity(in1);
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
		}else if(other != null && other.equals("other")){
			Intent in1 = new Intent(ActUserYanzheng.this,GestureVerifyActivity.class);
			//	in1.putExtra("flag", "close");
				startActivity(in1);
				overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
		}
		finish();
	}
	
	void hideInput(){
		//点击按钮时  先隐藏软键盘，再执行响应操作
		InputMethodManager inputMethodManager =(InputMethodManager)this.getApplicationContext().  
        getSystemService(Context.INPUT_METHOD_SERVICE);   
		inputMethodManager.hideSoftInputFromWindow(et_phone.getWindowToken(), 0);
	}
	
	void validate(){
		String  signature = "validate.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.mobilePhone = et_phone.getText().toString();
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(this);
		
		
		new ValidateTask().execute(userInfo);
	}
	
	class ValidateTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{
		@Override
		protected void onPreExecute() {
			dialog = new LoadingDialog(ActUserYanzheng.this);
			dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
			dialog.show();
			super.onPreExecute();
		}
		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.validate(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result == null){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0")){
				if(result.state.equals("0")){//未注册
					SharedPreferences.Editor editor = isNewUser.edit();
					editor.clear();
					editor.commit();
					editor.putBoolean("isNewUser", true);
					editor.commit();
					if(!SppaConstant.isNumeric(et_phone.getText().toString())){
						new SweetAlertDialog(ActUserYanzheng.this, SweetAlertDialog.WARNING_TYPE)
		                .setContentText("该账号并没有注册我站会员，请使用手机号码注册！")
		                .show();
					}else{
						Intent in = new Intent(ActUserYanzheng.this,ActRegister.class);
						in.putExtra("phone", et_phone.getText().toString());
						startActivity(new Intent(in));
						finish();
						overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
					}
					
				}else{//已注册
					SharedPreferences.Editor editor = isNewUser.edit();
					editor.clear();
					editor.commit();
					editor.putBoolean("isNewUser", false);
					editor.commit();
					Intent in = new Intent(ActUserYanzheng.this,ActLogin.class);
					in.putExtra("phone", et_phone.getText().toString());
					in.putExtra("flag", flag);
					in.putExtra("in", enter);
					startActivity(in);
					finish();
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				}
				_handler.sendEmptyMessage(0);
			}else{
				_handler.sendEmptyMessage(1);
				Toast.makeText(ActUserYanzheng.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	
	}
	

}
