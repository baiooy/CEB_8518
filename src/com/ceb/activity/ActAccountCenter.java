package com.ceb.activity;

import java.net.URLEncoder;
import java.util.TreeMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.caifuline.R;
import com.ceb.activity.ActAccountBalance.AccountBalanceTask;
import com.ceb.base.BaseActivity;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UISwitchButton;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
import com.model.ActionResultInfo;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;
import com.view.BgView;
import com.view.RoundImageView;

public class ActAccountCenter extends BaseActivity {
	private Button btn_logout;
	private RoundImageView riv_account;
	private UISwitchButton switch1;
	private RelativeLayout rl_alterpasswd,rl_help,rl_bank,rl_contact,rl_openhuifu,rl_update,rl_fb,rl_message;
	private SharedPreferences sp_opengesture = null;
	private Boolean isopengesture = true; //默认开启
	private TextView tv_version, tv_v,tv_username,tv_openhuifu,tv_bangka,tv_loggingname,tv_phone,tv_renzheng;
	private String userAccountType,cardType,loginname;
	private Handler mHandler;
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	private Handler _handler;
	private LoadingDialog dialog;
	ImageView iv_redround;
	int megTotal;
	@Override
	public void setView() {
		setContentView(R.layout.act_accountcenter);

		sp_opengesture = getSharedPreferences("OPENGESTURE", 0);
		Intent in = getIntent();
		userAccountType = in.getStringExtra("userAccountType");
		cardType = in.getStringExtra("cardType");
		loginname = in.getStringExtra("username");
		megTotal = in.getIntExtra("megTotal", 0);
		_userInfoService = new UserInfoService(ActAccountCenter.this);
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case -1:
					break;
				case 0:
					new SweetAlertDialog(ActAccountCenter.this, SweetAlertDialog.SUCCESS_TYPE)
		            .setContentText("手势密码设置成功!")
		            .show();
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
		
		_handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case -1:
					break;
				case 0:
				
					break;
				case 1:
					break;
				default:
					break;
				}
				if(dialog != null && dialog.isShowing()){
					dialog.dismiss();
				}
			};
		};
		
		try {
			getAccountCenter();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void initView() {
		riv_account = (RoundImageView) findViewById(R.id.riv_account);
		btn_logout = (Button) findViewById(R.id.btn_logout);
		rl_alterpasswd = (RelativeLayout) findViewById(R.id.rl_alterpasswd);
		rl_message = (RelativeLayout) findViewById(R.id.rl_message);
		rl_openhuifu = (RelativeLayout) findViewById(R.id.rl_openhuifu);
		rl_update = (RelativeLayout) findViewById(R.id.rl_update);
		rl_fb = (RelativeLayout) findViewById(R.id.rl_fb);
		rl_bank = (RelativeLayout) findViewById(R.id.rl_bank);
		rl_contact = (RelativeLayout) findViewById(R.id.rl_contact);
		switch1 = (UISwitchButton) findViewById(R.id.switch_1);
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_openhuifu = (TextView) findViewById(R.id.tv_openhuifu);
		tv_bangka = (TextView) findViewById(R.id.tv_bangka);
		tv_renzheng =  (TextView) findViewById(R.id.tv_renzheng);
		iv_redround = (ImageView) findViewById(R.id.iv_redround);
		
		if(megTotal > 0){
			iv_redround.setVisibility(View.VISIBLE);
		}else{
			iv_redround.setVisibility(View.INVISIBLE);
		}
//		tv_username.setText("欢迎您，"+loginname);
		tv_v = (TextView) findViewById(R.id.tv_v);
		tv_loggingname = (TextView) findViewById(R.id.tv_loggingname);
		tv_loggingname.setText(loginname);
		
//		if(userAccountType != null && userAccountType.equals("0")){
//			rl_openhuifu.setClickable(true);
//			rl_openhuifu.setOnClickListener(this);
//			tv_openhuifu.setText("马上开通");
//			tv_openhuifu.setTextColor(Color.RED);
//		}else{
//			rl_openhuifu.setClickable(false);
//			tv_openhuifu.setText("已开通");
//			tv_openhuifu.setTextColor(Color.GRAY);
//		}
		if(cardType != null && cardType.equals("0")){
			tv_bangka.setText("未绑定");
		}else{
			tv_bangka.setText("已绑定");
		}
		
		isopengesture = sp_opengesture.getBoolean("isopengesture", false);
		if(isopengesture){
			switch1.setChecked(true);
		}else{
			switch1.setChecked(false);
		}
		
		
		switch1.setOnCheckedChangeListener(new OnCheckedChangeListener() {           
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
            	if(!isChecked)//第一个开关关闭
            	{
            		isopengesture = false;
        			SharedPreferences.Editor editor = sp_opengesture.edit();
        			editor.clear();
        			editor.commit();
        			editor.putBoolean("isopengesture", isopengesture);
        			
        			editor.commit();
        			Log.i("isopengesture", ""+isopengesture);
        			Intent in1 = new Intent(ActAccountCenter.this,GestureVerifyActivity.class);
        			in1.putExtra("flag", "close");
        			ActAccountCenter.this.startActivity(in1);
        			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
            	}
            	else//第一个开关打开
            	{
            		isopengesture = true;
        			SharedPreferences.Editor editor = sp_opengesture.edit();
        			editor.clear();
        			editor.commit();
        			editor.putBoolean("isopengesture", isopengesture);
        			editor.commit();
        			Log.i("isopengesture", ""+isopengesture);
        			Intent in2 = new Intent(ActAccountCenter.this,GestureEditActivity.class);
        			in2.putExtra("flag", "open");
        			ActAccountCenter.this.startActivityForResult(in2, 0x01);
        			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
            	}
            }
        });

	}

	@Override
	public void setListener() {
		btn_logout.setOnClickListener(this);
		rl_alterpasswd.setOnClickListener(this);
//		rl_openhuifu.setOnClickListener(this);
		rl_bank.setOnClickListener(this);
//		rl_contact.setOnClickListener(this);
//		rl_update.setOnClickListener(this);
//		rl_fb.setOnClickListener(this);
		rl_message.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_logout:
			 new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
             .setContentText("是否要退出该账户?")
             .setCancelText("取消")
             .setConfirmText("确定")
             .showCancelButton(true)
             .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                 @Override
                 public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismiss();
                 }
             })
             .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                 @Override
                 public void onClick(SweetAlertDialog sDialog) {
                	 SharedPreferences isLogin = getSharedPreferences("isLogin", 0);
						SharedPreferences.Editor editor2 = isLogin.edit();
						editor2.clear();
						editor2.commit();
						editor2.putBoolean("islogin", false);//退出登录
						editor2.commit();
						
						SharedPreferences USERID = getSharedPreferences("USERID", 0);
						SharedPreferences.Editor editor = USERID.edit();
						editor.clear();
						editor.commit();
						editor.putString("userID","0");
			//			editor.putString("loginname",loginname);
						editor.commit();
						
						SharedPreferences sp_opengesture = getSharedPreferences("OPENGESTURE", 0);
						SharedPreferences.Editor edit = sp_opengesture.edit();
						edit.clear();
						edit.commit();
						edit.putBoolean("isopengesture", false);
	        			
						edit.commit();
						
						startActivity(new Intent(ActAccountCenter.this,ActUserYanzheng.class));
						finish();
						overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out); 
                 	sDialog.dismiss();
                 }
             })
             .show();
			break;
		case R.id.rl_alterpasswd:
			Intent in = new Intent(ActAccountCenter.this,ActAlterPasswd.class);
			startActivity(in);
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.rl_bank:
			Intent ins = new Intent(ActAccountCenter.this,ActBankCardManage.class);
			ins.putExtra("userAccountType", userAccountType);
			startActivity(ins);
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		
		case R.id.rl_openhuifu:
			Intent insi = new Intent(ActAccountCenter.this,ActHuifu.class);
			startActivity(insi);
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.rl_message:
			startActivity(new Intent(ActAccountCenter.this, ActMyMessage.class));
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		default:
			break;
		}

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 0x01 && resultCode == RESULT_OK){
			Log.i("Oops", "Oops");
			new Thread(){
				public void run() {
					try {
						sleep(400);
						mHandler.sendEmptyMessage(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				};
			}.start();
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	void getAccountCenter() {
		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		
		String  signature = "accountcenter.php:"+System.currentTimeMillis()/1000+":hxpay";
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

		new AccountCenterTask().execute(userInfo);
	}

	class AccountCenterTask extends
			AsyncTask<UserInfo, Void, ActionResultInfo> {

		@Override
		protected void onPreExecute() {
			dialog = new LoadingDialog(ActAccountCenter.this);
			dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
			dialog.show();
			super.onPreExecute();
		}
		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;

			info = _userInfoService.getAccountCenter(params[0]);

			return info;
		}

		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if (result == null || result.equals(null)) {
				_handler.sendEmptyMessage(-1);
			} else if (result.ret.equals("0")) {
				tv_loggingname.setText(result.username);
				tv_phone.setText(result.phone.substring(0, 3)+" **** "+result.phone.substring(result.phone.length()-4));
				if(result.payAccount.equals("0") /*|| userAccountType != null && userAccountType.equals("0")*/){
					rl_openhuifu.setClickable(true);
					rl_openhuifu.setOnClickListener(ActAccountCenter.this);
					tv_openhuifu.setText("马上开通");
					tv_openhuifu.setTextColor(Color.RED);
				}else{
					rl_openhuifu.setClickable(false);
					tv_openhuifu.setText(result.payAccount.substring(0, result.payAccount.length()/4+1)+"****"+result.payAccount.substring(result.payAccount.length()*3/4));
					tv_openhuifu.setTextColor(Color.GRAY);
				}
				if(result.userAccountType != null && result.userAccountType.equals("0")){
					tv_renzheng.setText("未认证");
				}else{
					tv_renzheng.setText("已认证");
				}
				new BgView(ActAccountCenter.this, result.iconurl,riv_account);
				
				_handler.sendEmptyMessage(0);
			} else {
				_handler.sendEmptyMessage(1);
				Toast.makeText(ActAccountCenter.this, result.msg,
						Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		isopengesture = sp_opengesture.getBoolean("isopengesture", false);
		if(isopengesture){
			switch1.setChecked(true);
		}else{
			switch1.setChecked(false);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void onback(View v) {
		finish();
	}

	
}
