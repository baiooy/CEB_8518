package com.ceb.activity;

import java.net.URLEncoder;
import java.util.TreeMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.caifuline.R;
import com.ceb.activity.ActRegister.TimeCount;
import com.ceb.activity.ActRegister.VercodeTask;
import com.ceb.base.BaseActivity;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.model.ActionResultInfo;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;

public class ActFindBackPasswd extends BaseActivity {
	private TextView tv_getyzm;
	private Button btn_next;
	private EditText et_phone,et_yanzhengma;
	UserInfoService _userInfoService;
	private String userID;
	private Handler _handler;
	private TimeCount time;
	private String phone;
	@Override
	public void setView() {
		setContentView(R.layout.act_finabackpasswd);

		_userInfoService = new UserInfoService(this);
		Intent in = getIntent();
		phone = in.getStringExtra("phone");
		
		_handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(ActFindBackPasswd.this,
							"网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
					Intent in = new Intent(ActFindBackPasswd.this,ActResetPasswd.class);
					startActivity(in);
					overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
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
		tv_getyzm = (TextView) findViewById(R.id.tv_getyzm);
		btn_next = (Button) findViewById(R.id.btn_next);
		tv_getyzm.setText(Html.fromHtml("<u>" + "获取验证码" + "</u>"));
		
		et_phone = (EditText) findViewById(R.id.et_phone);
		if(SppaConstant.isNumeric(phone)){
			et_phone.setText(phone);
		}
		
		et_yanzhengma = (EditText) findViewById(R.id.et_yanzhengma);
		
		time = new TimeCount(60000, 1000);// 构造CountDownTimer对象,参数依次为总时长,和计时的时间间隔
		
		et_yanzhengma.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String code = et_yanzhengma.getText().toString();
				if (code.length() == 6) {
						time.cancel();
						hideInput();
					} 
				}

		});

	}

	/* 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			tv_getyzm.setText(Html.fromHtml("<u>" + "重新获取" + "</u>"));
			tv_getyzm.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			tv_getyzm.setClickable(false);
			tv_getyzm.setText("获取中" + millisUntilFinished / 1000 + "秒");
		}
	}
	
	@Override
	public void setListener() {
		btn_next.setOnClickListener(this);
		tv_getyzm.setOnClickListener(this);
		et_phone.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_getyzm:
			hideInput();
			if(et_phone.getText().toString().equals("")){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setContentText("您还未填手机号!")
                .show();
			}else if(et_phone.getText().toString().length() < 11){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setContentText("您填的手机号长度不正确!")
                .show();
			}else{
				getvercode();
				time.start();
			}
			break;
		case R.id.btn_next:
			hideInput();
			if(et_phone.getText().toString().equals("")){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setContentText("您还未填手机号!")
                .show();
			}else if(et_phone.getText().toString().length() < 11){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setContentText("您填的手机号长度不正确!")
                .show();
			}else if(et_yanzhengma.getText().toString().equals("")){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setContentText("请输入验证码!")
                .show();
			}else if(et_yanzhengma.getText().toString().length() < 6){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setContentText("验证码输入有误!")
                .show();
			}else{
				try {
					forgetpassword();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			break;
		case R.id.et_phone:
			et_phone.setCursorVisible(true);
			break;
		default:
			break;
		}

	}
	
	void getvercode(){
		String  signature = "getvercode.php:"+System.currentTimeMillis()/1000+":hxpay";
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
		userInfo.mobilePhone = et_phone.getText().toString();
		
		new VercodeTask().execute(userInfo);
	}
	
	class VercodeTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{

		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.getvercode(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result == null){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0")){
		//		Toast.makeText(ActFindBackPasswd.this,result.msg , Toast.LENGTH_SHORT).show();
		//		_handler.sendEmptyMessage(0);
			}else{
				Toast.makeText(ActFindBackPasswd.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	void forgetpassword(){
		String  signature = "forgetpassword.php:"+System.currentTimeMillis()/1000+":hxpay";
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
		userInfo.mobilePhone = et_phone.getText().toString();
		userInfo.vercode = et_yanzhengma.getText().toString();
		
		new ForgetpasswordTask().execute(userInfo);
	}
	
	class ForgetpasswordTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{

		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.forgetpassword(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result == null){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0")){
				userID = result.userID;
				
				if(userID != null && !userID.equals("")){
					SharedPreferences USERID = getSharedPreferences("USERID", 0);
					SharedPreferences.Editor editor = USERID.edit();
					editor.clear();
					editor.commit();
					editor.putString("userID",userID);
					editor.commit();
				}
				_handler.sendEmptyMessage(0);
			}else{
				Toast.makeText(ActFindBackPasswd.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	
	void hideInput(){
		//点击按钮时  先隐藏软键盘，再执行响应操作
		InputMethodManager inputMethodManager =(InputMethodManager)this.getApplicationContext().  
        getSystemService(Context.INPUT_METHOD_SERVICE);   
		inputMethodManager.hideSoftInputFromWindow(et_yanzhengma.getWindowToken(), 0);
	}
	
	public void onback(View v){
		finish();
	}

}
