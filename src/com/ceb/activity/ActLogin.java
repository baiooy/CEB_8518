package com.ceb.activity;

import java.net.URLEncoder;
import java.util.TreeMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.caifuline.R;
import com.ceb.activity.ActRegister.RegisterTask;
import com.ceb.base.BaseActivity;
import com.ceb.fragment.FragmentMy;
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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ActLogin extends BaseActivity {
	Button btn_login;
	TextView tv_forget;
	private EditText et_phone,et_passwd;
	UserInfoService _userInfoService;
	private Handler _handler;
	private String userID;
	private String userNickName;
	private String loginname,pid;
	private TextView tv_show;
	private SharedPreferences sp;
	private SharedPreferences sp_ges;
	private String phone;
	String flag,enter;
	private boolean isShowPass = false;
	private LoadingDialog dialog;
	@Override
	public void setView() {
		setContentView(R.layout.act_login);

		
		Intent in = getIntent();
		phone = in.getStringExtra("phone");
		_userInfoService = new UserInfoService(this);
		flag = in.getStringExtra("flag");
		Log.i("ActLogin  flag", "flag"+flag);
		enter = in.getStringExtra("in"); 
		Log.i("MD5Util", MD5Util.MD5("123456"));
		_handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(ActLogin.this,
							"网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
//					Intent in = new Intent(ActRegister.this,ActRegisterSucceed.class);
//					in.putExtra("userID", userID);
//					in.putExtra("loginname", loginname);
//					startActivity(in);
					if(flag != null && flag.equals("forget")){
//						Intent in2 = new Intent(ActLogin.this,GestureEditActivity.class);
//						in2.putExtra("flag", "open");
//	        			ActLogin.this.startActivity(in2);
//	        			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
						SharedPreferences sp_opengesture = getSharedPreferences("OPENGESTURE", 0);
						SharedPreferences.Editor edit = sp_opengesture.edit();
						edit.clear();
						edit.commit();
						edit.putBoolean("isopengesture", true);
	        			
						edit.commit();
						
					}
					if(enter!= null && enter.equals("in")){
						Intent ii =new Intent(ActLogin.this,MainActivity.class);
						if(MainActivity.instance != null)
							MainActivity.instance.finish();
						ii.putExtra("in", "in");
						startActivity(ii);
						overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
					}
					
//					if(sp_ges.getBoolean("isopengesture", true)){
//						Intent ii =new Intent(ActLogin.this,GestureEditActivity.class);
//						startActivity(ii);
//						overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
//					}
					
					
					finish();
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
		btn_login = (Button) findViewById(R.id.btn_login);
		tv_forget = (TextView) findViewById(R.id.tv_forget);
//		tv_forget.setText(Html.fromHtml("<u>" + "忘记密码？" + "</u>"));
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_passwd = (EditText) findViewById(R.id.et_passwd);
		tv_show = (TextView) findViewById(R.id.tv_show);
		sp = getSharedPreferences("USERID", 0);
		sp_ges = getSharedPreferences("OPENGESTURE", 0);
		
		userID = sp.getString("userID", "0");
		
		et_phone.setText(phone);
	}

	@Override
	public void setListener() {
		tv_forget.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		tv_show.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
		case R.id.btn_register:
			Intent ins = new Intent(ActLogin.this,ActRegister.class);
			startActivity(ins);
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.tv_forget:
			Intent in = new Intent(this,ActFindBackPasswd.class);
			in.putExtra("phone", phone);
			startActivity(in);
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.btn_login:
//			Intent aintent = new Intent(ActLogin.this,
//					FragmentMy.class);
//			aintent.putExtra("flag", "login");
//			setResult(RESULT_OK, aintent);
			if(et_passwd.getText().toString().equals("")){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
				.setContentText("请输入密码!")
				.show();
			}else{
				try {
					login();
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
		finish();
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
		protected void onPreExecute() {
			dialog = new LoadingDialog(ActLogin.this);
			dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
			dialog.show();
			super.onPreExecute();
		}
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
			}else if(result.ret != null & result.ret.equals("0")){
				userID = result.userID;
				userNickName = result.userNickName;
				loginname = result.loginname;
				pid = result.pid;
				
				Log.i("userID--loginname", userID+"--"+loginname);
				
				SharedPreferences USERID = getSharedPreferences("USERID", 0);
				SharedPreferences.Editor editor = USERID.edit();
				editor.clear();
				editor.commit();
				editor.putString("userID",userID);
				editor.putString("loginname",loginname);
				editor.commit();
				
				SharedPreferences PID = getSharedPreferences("PID", 0);
				SharedPreferences.Editor editor_pid = PID.edit();
				editor_pid.clear();
				editor_pid.commit();
				editor_pid.putString("pid",pid);
				editor_pid.commit();
				Log.i("pid", pid+"--");
				
				SharedPreferences isLogin = getSharedPreferences("isLogin", 0);
				SharedPreferences.Editor editor2 = isLogin.edit();
				editor2.clear();
				editor2.commit();
				editor2.putBoolean("islogin", true);//登录成功后，记录登陆状态
				editor2.commit();
				
				
				_handler.sendEmptyMessage(0);
			}else{
				_handler.sendEmptyMessage(1);
				Toast.makeText(ActLogin.this,result.msg , Toast.LENGTH_SHORT).show();
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
	
	
	
	
	
	
	
	
	

}
