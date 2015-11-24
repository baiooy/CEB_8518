package com.ceb.base;


import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseActivity extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置始终为竖屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setView();
		initView();
		setListener();
//		initData();
//		Log.i("deviceinfo", SppaConstant.getDeviceInfo(this));
		ExitApplication.getInstance().addActivity(this);
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		//友盟统计
		MobclickAgent.onPause(this);
		
		
	}
	public void onback(View v) {
		finish();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//友盟统计
		MobclickAgent.onResume(this);
	}
	
	/**
	 * ���ò����ļ�
	 */
	public abstract void setView();

	/**
	 * ��ʼ�������ļ��еĿؼ�
	 */
	public abstract void initView();

	/**
	 * ���ÿؼ��ļ���
	 */
	public abstract void setListener();
	
	
	
}
