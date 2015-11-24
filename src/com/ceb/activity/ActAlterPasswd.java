package com.ceb.activity;

import java.net.URLEncoder;
import java.util.TreeMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.adapter.AdapterRepaymentschedule;
import com.caifuline.R;
import com.ceb.activity.ActRegister.VercodeTask;
import com.ceb.base.BaseActivity;
import com.ceb.widge.MD5Util;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.model.ActionResultInfo;
import com.model.InvestplanList;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;

public class ActAlterPasswd extends BaseActivity {
	private EditText et_oldpasswd,et_newpasswd;
	private TextView tv_show;
	private Button btn_confirm;
	private boolean isShowPass = false;
	private String oldpasswd,newpasswd;
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	private Handler _handler;
	@Override
	public void setView() {
		setContentView(R.layout.act_alterpasswd);

		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		_userInfoService = new UserInfoService(ActAlterPasswd.this);
		
		_handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(ActAlterPasswd.this,
							"网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
					finish();
					break;
				default:
					break;
				}
			};
		};
		
		
	}

	@Override
	public void initView() {
		et_oldpasswd = (EditText) findViewById(R.id.et_oldpasswd);
		et_newpasswd = (EditText) findViewById(R.id.et_newpasswd);
		tv_show = (TextView) findViewById(R.id.tv_show);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		
		
	}

	@Override
	public void setListener() {
		tv_show.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);

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
			Editable etext = et_newpasswd.getText();
			Selection.setSelection(etext, etext.length());
			break;
		case R.id.btn_confirm:
			oldpasswd = et_oldpasswd.getText().toString();
			newpasswd = et_newpasswd.getText().toString();
			
			if(oldpasswd.equals("")||newpasswd.equals("")){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setContentText("请输入完整!")
                .show();
			}else if(newpasswd.length() < 6 || newpasswd.length() > 20){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setContentText("密码长度不符!")
                .show();
			}else{
				
				try {
					alterPasswd();
				} catch (Exception e) {
					e.printStackTrace();
				}
				hideInput();
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 密码显示
	 */
	public void show() {
		et_newpasswd.setTransformationMethod(HideReturnsTransformationMethod
				.getInstance());
		tv_show.setText("隐藏");
		isShowPass = true;
	}

	/**
	 * 密码隐藏
	 */
	public void hide() {
		et_newpasswd.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		
		tv_show.setText("显示");
		isShowPass = false;
	}
	
	public void onback(View v){
		finish();
	}

	void alterPasswd(){
		String  signature = "settingpassword.php:"+System.currentTimeMillis()/1000+":hxpay";
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
		userInfo.oldpassword =MD5Util.MD5(oldpasswd); 
		userInfo.password = MD5Util.MD5(newpasswd);
		
		new AlterPasswdTask().execute(userInfo);
	}

	class AlterPasswdTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{

		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.alterPasswd(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result == null || result.equals(null)){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0")){
					//Toast.makeText(ActAlterPasswd.this,result.msg , Toast.LENGTH_SHORT).show();
				_handler.sendEmptyMessage(0);
			}else{
				Toast.makeText(ActAlterPasswd.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	
	
	void hideInput(){
		//点击按钮时  先隐藏软键盘，再执行响应操作
		InputMethodManager inputMethodManager =(InputMethodManager)this.getApplicationContext().  
        getSystemService(Context.INPUT_METHOD_SERVICE);   
		inputMethodManager.hideSoftInputFromWindow(et_newpasswd.getWindowToken(), 0);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
