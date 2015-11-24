package com.ceb.activity;

import java.net.URLEncoder;
import java.util.TreeMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.caifuline.R;
import com.ceb.activity.ActLogin.LoginTask;
import com.ceb.base.BaseActivity;
import com.ceb.widge.MD5Util;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
import com.model.ActionResultInfo;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("HandlerLeak")
public class ActRegister extends BaseActivity {
	private ImageView gouhao;
	private TextView tv_show,tv_fengxian,tv_yonghu,tv_hege;
	private int flag = -1;
	private TextView tv_getyzm;
	private Button btn_loginn,btn_register;
	private EditText et_passwd,et_yanzhengma,et_yaoqingma;//et_phone,
	UserInfoService _userInfoService;
	private Handler _handler;
	private TimeCount time;
	private String userID,loginname,str;
	private String phone,pid;
	private boolean isShowPass = false;
	private LoadingDialog dialog;
	@Override
	public void setView() {
		setContentView(R.layout.act_register);

		_userInfoService = new UserInfoService(this);
		
		Intent in = getIntent();
		phone = in.getStringExtra("phone");
		
		_handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(ActRegister.this,
							"网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
					Intent in = new Intent(ActRegister.this,ActRegisterSucceed.class);
					in.putExtra("userID", userID);
					in.putExtra("loginname", loginname);//str
					in.putExtra("str", str);
					startActivity(in);
					finish();
					overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
					break;
				case 3:
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
		gouhao = (ImageView) findViewById(R.id.gouhao);
		tv_getyzm = (TextView) findViewById(R.id.tv_getyzm);
		btn_loginn = (Button) findViewById(R.id.btn_loginn);
		btn_register = (Button) findViewById(R.id.btn_register);
		tv_getyzm.setText(Html.fromHtml("<u>" + "获取验证码" + "</u>"));
		tv_show = (TextView) findViewById(R.id.tv_show);
//		et_phone = (EditText) findViewById(R.id.et_phone);
		et_passwd = (EditText) findViewById(R.id.et_passwd);
		et_yanzhengma = (EditText) findViewById(R.id.et_yanzhengma);
		et_yaoqingma = (EditText) findViewById(R.id.et_yaoqingma);
		tv_fengxian = (TextView) findViewById(R.id.tv_fengxian);
		tv_yonghu = (TextView) findViewById(R.id.tv_yonghu);
		tv_hege = (TextView) findViewById(R.id.tv_hege);
//		et_phone.setText(phone);
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

	@Override
	public void setListener() {
		gouhao.setOnClickListener(this);
		btn_loginn.setOnClickListener(this);
		tv_getyzm.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		tv_show.setOnClickListener(this);
		tv_fengxian.setOnClickListener(this);
		tv_yonghu.setOnClickListener(this);
		tv_hege.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gouhao:
			if(flag == -1){//勾上
				gouhao.setImageResource(R.drawable.xuankuangs);//去√
				flag = 0;
			}else if(flag == 0){//未勾上
				gouhao.setImageResource(R.drawable.xuankuangs1);//打√
				flag = -1;
			}
			
			break;
		case R.id.btn_loginn:
			finish();
			break;
		case R.id.tv_getyzm:
			hideInput();
//				if(et_phone.getText().toString().equals("")){
//					new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
//					.setCustomImage(R.drawable.ceb_icon)
//                    .setTitleText("Oops...")
//                    .setContentText("您还未填手机号!")
//                    .show();
//				}else if(et_phone.getText().toString().length() < 11){
//					new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
//                    .setTitleText("Oops...")
//                    .setContentText("您填的手机号长度不正确!")
//                    .show();
//				}else{
					getvercode();
					time.start();
			//	}
			break;
		case R.id.btn_register:
			hideInput();
			/*if(et_phone.getText().toString().equals("")){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
				.setCustomImage(R.drawable.ceb_icon)
                .setTitleText("Oops...")
                .setContentText("您还未填手机号!")
                .show();
			}else if(et_phone.getText().toString().length() < 11){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("您填的手机号长度不正确!")
                .show();
			}else*/ if(et_passwd.getText().toString().equals("")){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setContentText("请输入登录密码!")
                .show();
			}else if(et_passwd.getText().toString().length() < 6){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setContentText("您设置的密码过短!")
                .show();
				
			}else if(et_yanzhengma.getText().toString().equals("")){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setContentText("请输入验证码!")
                .show();
			}else if(et_yanzhengma.getText().toString().length() < 6){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setContentText("验证码输入有误!")
                .show();
			}else if(flag != -1){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setContentText("不同意8518理财网用户协议不可以注册哦!")
                .show();
			}else{
				
				register();
				
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
		case R.id.tv_fengxian:
			startActivity(new Intent(ActRegister.this,ActFengxiantishi.class));
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.tv_yonghu:
			startActivity(new Intent(ActRegister.this,ActYonghuxieyi.class));
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.tv_hege:
			startActivity(new Intent(ActRegister.this,ActHegeTouziren.class));
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		default:
			break;
		}

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
	
	public void onback(View v){
		finish();
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
		userInfo.mobilePhone = phone;//et_phone.getText().toString();
		
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
			}else if(result.equals("0")){
				
				
				
				_handler.sendEmptyMessage(0);
			}else{
			//	Toast.makeText(ActRegister.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	void register(){
		String  signature = "register.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.mobilePhone = phone;//et_phone.getText().toString();
		userInfo.passwd = et_passwd.getText().toString();
		userInfo.vercode = et_yanzhengma.getText().toString();
		userInfo.recommendcode = et_yaoqingma.getText().toString();
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(this);
		
		
		new RegisterTask().execute(userInfo);
	}
	
	class RegisterTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{

		@Override
		protected void onPreExecute() {
			dialog = new LoadingDialog(ActRegister.this);
			dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
			dialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.register(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result == null){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0")){
				userID = result.userID;
				loginname = result.loginname;
				str = result.str;
				
				SharedPreferences USERID = getSharedPreferences("USERID", 0);
				SharedPreferences.Editor editor = USERID.edit();
				editor.clear();
				editor.commit();
				editor.putString("userID", userID);
				editor.putString("loginname", loginname);
				editor.commit();
				
				login();
				_handler.sendEmptyMessage(0);
			}else{
				_handler.sendEmptyMessage(2);
				Toast.makeText(ActRegister.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	
	void login(){
		String  signature = "login.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.mobilePhone = phone;//et_phone.getText().toString();
		userInfo.passwd = MD5Util.MD5(et_passwd.getText().toString());
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(this);
		
		
		new LoginTask().execute(userInfo);
	}
	
	class LoginTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{

		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.login(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result == null){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0")){
				userID = result.userID;
				pid = result.pid;
		//		userNickName = result.userNickName;
				
//				SharedPreferences USERID = getSharedPreferences("USERID", 0);
//				SharedPreferences.Editor editor = USERID.edit();
//				editor.clear();
//				editor.commit();
//				editor.putString("userID", userID);
//				editor.commit();
				
				SharedPreferences PID = getSharedPreferences("PID", 0);
				SharedPreferences.Editor editor_pid = PID.edit();
				editor_pid.clear();
				editor_pid.commit();
				editor_pid.putString("pid",pid);
				editor_pid.commit();
				
				SharedPreferences isLogin = getSharedPreferences("isLogin", 0);
				SharedPreferences.Editor editor2 = isLogin.edit();
				editor2.clear();
				editor2.commit();
				editor2.putBoolean("islogin", true);//登录成功后，记录登陆状态
				editor2.commit();
				
				
				_handler.sendEmptyMessage(0);
			}else{
				Toast.makeText(ActRegister.this,result.msg , Toast.LENGTH_SHORT).show();
			}
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

	/**
	 * 密码隐藏
	 */
	public void hide() {
		et_passwd.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		
		tv_show.setText("显示");
		isShowPass = false;
	}
	
	void hideInput(){
		//点击按钮时  先隐藏软键盘，再执行响应操作
		InputMethodManager inputMethodManager =(InputMethodManager)this.getApplicationContext().  
        getSystemService(Context.INPUT_METHOD_SERVICE);   
		inputMethodManager.hideSoftInputFromWindow(et_yanzhengma.getWindowToken(), 0);
	}


}
