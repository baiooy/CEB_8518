package com.ceb.activity;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.caifuline.R;
import com.ceb.base.BaseActivity;
import com.ceb.base.ExitApplication;
import com.ceb.widge.UISwitchButton;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.view.ActionSheet;
import com.view.ActionSheet.ActionSheetListener;

public class ActSetting extends  FragmentActivity implements
ActionSheetListener,OnClickListener {
	private UISwitchButton switch1;
	Context context = ActSetting.this;
	private RelativeLayout rl_alterpasswd,rl_help,rl_bank,rl_contact,rl_openhuifu,rl_update,rl_fb;
	private SharedPreferences sp_opengesture = null;
	private Boolean isopengesture = true; //默认开启
	private TextView tv_version, tv_v,tv_username,tv_openhuifu,tv_bangka;
	private String userAccountType,cardType;
	private String loginname;
	private Handler mHandler;
	private Button btn_logout;
	private ActionSheet dial = new ActionSheet();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_shezhi);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置始终为竖屏
		ExitApplication.getInstance().addActivity(this);
		
		setTheme(R.style.ActionSheetStyleIOS7);
		sp_opengesture = getSharedPreferences("OPENGESTURE", 0);
		
		Intent in = getIntent();
		userAccountType = in.getStringExtra("userAccountType");
		cardType = in.getStringExtra("cardType");
		loginname = in.getStringExtra("username");
		
		initView();
		setListener();
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case -1:
					break;
				case 0:
					new SweetAlertDialog(ActSetting.this, SweetAlertDialog.SUCCESS_TYPE)
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
			}


	
	public void initView() {
		rl_alterpasswd = (RelativeLayout) findViewById(R.id.rl_alterpasswd);
		rl_openhuifu = (RelativeLayout) findViewById(R.id.rl_openhuifu);
		rl_update = (RelativeLayout) findViewById(R.id.rl_update);
		rl_fb = (RelativeLayout) findViewById(R.id.rl_fb);
		rl_bank = (RelativeLayout) findViewById(R.id.rl_bank);
		rl_contact = (RelativeLayout) findViewById(R.id.rl_contact);
		switch1 = (UISwitchButton) findViewById(R.id.switch_1);
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_openhuifu = (TextView) findViewById(R.id.tv_openhuifu);
		tv_bangka = (TextView) findViewById(R.id.tv_bangka);
		tv_username.setText("欢迎您，"+loginname);
		tv_v = (TextView) findViewById(R.id.tv_v);
		btn_logout = (Button) findViewById(R.id.btn_logout);
		
		if(userAccountType != null && userAccountType.equals("0")){
			rl_openhuifu.setClickable(true);
			rl_openhuifu.setOnClickListener(this);
			tv_openhuifu.setText("马上开通");
			tv_openhuifu.setTextColor(Color.RED);
		}else{
			rl_openhuifu.setClickable(false);
			tv_openhuifu.setText("已开通");
			tv_openhuifu.setTextColor(Color.GRAY);
		}
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
        			Intent in1 = new Intent(ActSetting.this,GestureVerifyActivity.class);
        			in1.putExtra("flag", "close");
        			ActSetting.this.startActivity(in1);
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
        			Intent in2 = new Intent(ActSetting.this,GestureEditActivity.class);
        			in2.putExtra("flag", "open");
        			ActSetting.this.startActivityForResult(in2, 0x01);
        			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
            	}
            }
        });
		
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					tv_v.setVisibility(View.INVISIBLE);
					tv_version.setText("发现新版本！");
					tv_version.setTextColor(Color.rgb(255, 000, 000));
					break;
				case UpdateStatus.No: // has no update
					try {
						PackageInfo info = getPackageManager().getPackageInfo(
								getPackageName(), 0);
						String version = info.versionName;
						tv_v.setVisibility(View.VISIBLE);
						tv_version.setText(version + "");
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
//				 case UpdateStatus.NoneWifi: // none wifi
//				 Toast.makeText(context, "没有wifi连接， 只在wifi下更新",
//				 Toast.LENGTH_SHORT).show();
//				 break;
//				 case UpdateStatus.Timeout: // time out
//				 Toast.makeText(context, "超时", Toast.LENGTH_SHORT).show();
//				 break;
				}
			}

		});
		UmengUpdateAgent.update(context);
		
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////		Bundle extras = data.getExtras();//& extras.getString("flag").equals("opensuccess")
//		if(requestCode == 0x01 & resultCode == RESULT_OK ){
//			Log.i("Oops", "Oops");
//			new SweetAlertDialog(ActSetting.this, SweetAlertDialog.SUCCESS_TYPE)
//			.setTitleText("Oops...")
//            .setContentText("手势密码设置成功!")
//            .show();
//		}else if(requestCode == 0x02 & resultCode == RESULT_OK){
//		}else if(requestCode == 0x03 & resultCode == RESULT_OK){
//		}else if(requestCode == 0x04 & resultCode == RESULT_OK){
//		}
//
//		super.onActivityResult(requestCode, resultCode, data);
//	}
	
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
	
	public void setListener() {
		rl_alterpasswd.setOnClickListener(this);
		rl_help.setOnClickListener(this);
		rl_bank.setOnClickListener(this);
		rl_contact.setOnClickListener(this);
		btn_logout.setOnClickListener(this);
		rl_update.setOnClickListener(this);
		rl_fb.setOnClickListener(this);
	}

//	public static Intent getPdfFileIntent( String param )
//	  {
//	    Intent intent = new Intent("android.intent.action.VIEW");
//	   intent.addCategory("android.intent.category.DEFAULT");
//	   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	    Uri uri = Uri.fromFile(new File(param ));
//	    intent.setDataAndType(uri,"application/pdf");
//	    return intent;
//	  }


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_alterpasswd:
			Intent in = new Intent(ActSetting.this,ActAlterPasswd.class);
			startActivity(in);
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
//		case R.id.rl_help:
//			startActivity(new Intent(ActSetting.this,ActAboutUs.class));
//			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
//			break;
		case R.id.rl_bank:
			Intent ins = new Intent(ActSetting.this,ActBankCardManage.class);
			ins.putExtra("userAccountType", userAccountType);
			startActivity(ins);
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.rl_contact:
//			String url = "tel:" + "4008608518";
//			Intent inse = new Intent(Intent.ACTION_CALL, Uri.parse(url));
//			startActivity(inse);
//			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			dial.createBuilder(this, getSupportFragmentManager())
			.setCancelButtonTitle("取消")
			.setOtherButtonTitles("400-860-8518")
			.setCancelableOnTouchOutside(true).setListener(this).show();
			
			break;
		case R.id.rl_openhuifu:
			Intent insi = new Intent(ActSetting.this,ActHuifu.class);
			startActivity(insi);
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.rl_update:
			UmengUpdateAgent.setUpdateAutoPopup(false);
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
				@Override
				public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
					switch (updateStatus) {
					case UpdateStatus.Yes: // has update
						UmengUpdateAgent.showUpdateDialog(context, updateInfo);
						break;
					case UpdateStatus.No: // has no update
						Toast.makeText(context, "您当前已经是最新版本！",
								Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.NoneWifi: // none wifi
						Toast.makeText(context, "没有wifi连接， 只在wifi下更新",
								Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.Timeout: // time out
						Toast.makeText(context, "超时", Toast.LENGTH_SHORT)
								.show();
						break;
					}
					
				}

			});

			UmengUpdateAgent.update(context);
			break;
		case R.id.btn_logout:
			 new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
             .setContentText("确定退出?")
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
						editor.putString("loginname",loginname);
						editor.commit();
						
						SharedPreferences sp_opengesture = getSharedPreferences("OPENGESTURE", 0);
						SharedPreferences.Editor edit = sp_opengesture.edit();
						edit.clear();
						edit.commit();
						edit.putBoolean("isopengesture", false);
	        			
						edit.commit();
						
						startActivity(new Intent(ActSetting.this,ActUserYanzheng.class));
						finish();
						overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out); 
                 	sDialog.dismiss();
                 }
             })
             .show();

			
			
			break;
		case R.id.rl_fb:
			startActivity(new Intent(ActSetting.this, ActFeedBack.class));
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		default:
			break;
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
	
	public void onback(View v){
		finish();
	}

	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
		switch (index) {
		case 0:
			String url = "tel:" + "4008608518";
			Intent inse = new Intent(Intent.ACTION_CALL, Uri.parse(url));
			startActivity(inse);
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		}
		
	}
	
}
