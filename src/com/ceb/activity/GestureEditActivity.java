package com.ceb.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.caifuline.R;
import com.ceb.base.BaseActivity;
import com.wujay.fund.common.Constants;
import com.wujay.fund.widget.GestureContentView;
import com.wujay.fund.widget.GestureDrawline.GestureCallBack;
import com.wujay.fund.widget.LockIndicator;

/**
 * 
 * 手势密码设置界面
 *
 */
public class GestureEditActivity extends BaseActivity implements OnClickListener {
	/** 手机号码*/
	public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
	/** 意图 */
	public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
	/** 首次提示绘制手势密码，可以选择跳过 */
	public static final String PARAM_IS_FIRST_ADVICE = "PARAM_IS_FIRST_ADVICE";
	private TextView mTextTitle;
	private TextView mTextCancel;
	private LockIndicator mLockIndicator;
	private TextView mTextTip;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	private TextView mTextReset;
	private String mParamSetUpcode = null;
	private String mParamPhoneNumber;
	private boolean mIsFirstInput = true;
	private String mFirstPassword = null;
	private String mConfirmPassword = null;
	private int mParamIntentCode;
	private String flag;
	private SharedPreferences sp = null;
	private SharedPreferences sp_opengesture = null;
	private Boolean isopengesture = true; //默认开启
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_edit);
		setUpViews();
		setUpListeners();
		
		Intent in = getIntent();
		flag = in.getStringExtra("flag");
		
		sp = getSharedPreferences("gesturepasswd", 0);
		sp_opengesture = getSharedPreferences("OPENGESTURE", 0);
	}
	
	private void setUpViews() {
		mTextTitle = (TextView) findViewById(R.id.text_title);
		mTextCancel = (TextView) findViewById(R.id.text_cancel);
		mTextReset = (TextView) findViewById(R.id.text_reset);
		mTextReset.setClickable(false);
		mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
		mTextTip = (TextView) findViewById(R.id.text_tip);
		mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
		// 初始化一个显示各个点的viewGroup
		mGestureContentView = new GestureContentView(this, false, "", new GestureCallBack() {
			@Override
			public void onGestureCodeInput(String inputCode) {
				if (!isInputPassValidate(inputCode)) {
					mTextTip.setText(Html.fromHtml("<font color='#ffffff'>最少链接4个点, 请重新输入</font>"));
					mGestureContentView.clearDrawlineState(0L);
					return;
				}
				if (mIsFirstInput) {
					mFirstPassword = inputCode;
					updateCodeList(inputCode);
					mGestureContentView.clearDrawlineState(0L);
					mTextReset.setClickable(true);
					mTextReset.setText(getString(R.string.reset_gesture_code));
	//				Toast.makeText(GestureEditActivity.this, inputCode, Toast.LENGTH_SHORT).show();
				} else {
					if (inputCode.equals(mFirstPassword)) {
						if(flag != null && flag.equals("open")){
							Log.i("Ges--flag", flag);
							Intent aintent = new Intent(GestureEditActivity.this,
									ActAccountCenter.class);
				//			aintent.putExtra("flag", "opensuccess");
							/* 将数据打包到aintent Bundle 的过程略 */
							setResult(RESULT_OK, aintent); // 这理有2个参数(int resultCode,
															// Intent intent)
						}
						if(flag != null && flag.equals("open2")){
							Intent aintent = new Intent(GestureEditActivity.this,
									MainActivity.class);
				//			aintent.putExtra("flag", "opensuccess");
							/* 将数据打包到aintent Bundle 的过程略 */
							setResult(RESULT_OK, aintent); // 这理
						}
						mGestureContentView.clearDrawlineState(0L);
						GestureEditActivity.this.finish();
						
						SharedPreferences.Editor editor = sp.edit();
						editor.clear();
						editor.commit();
						editor.putString("inputCode", inputCode);
						
						editor.commit();
						
						SharedPreferences sp_opengesture = getSharedPreferences("OPENGESTURE", 0);
						SharedPreferences.Editor edit = sp_opengesture.edit();
						edit.clear();
						edit.commit();
						edit.putBoolean("isopengesture", true);
	        			
						edit.commit();
					} else {
						mTextTip.setText(Html.fromHtml("<font color='#ffffff'>与上一次绘制不一致，请重新绘制</font>"));
						// 左右移动动画
						Animation shakeAnimation = AnimationUtils.loadAnimation(GestureEditActivity.this, R.anim.shake);
						mTextTip.startAnimation(shakeAnimation);
						// 保持绘制的线，1.5秒后清除
						mGestureContentView.clearDrawlineState(1000L);
					}
				}
				mIsFirstInput = false;
			}

			@Override
			public void checkedSuccess() {
				
			}

			@Override
			public void checkedFail() {
				
			}
		});
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(mGestureContainer);
		updateCodeList("");
	}
	
	private void setUpListeners() {
		mTextCancel.setOnClickListener(this);
		mTextReset.setOnClickListener(this);
	}
	
	private void updateCodeList(String inputCode) {
		// 更新选择的图案
		mLockIndicator.setPath(inputCode);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_cancel:
			isopengesture = false;
			SharedPreferences.Editor editor = sp_opengesture.edit();
			editor.clear();
			editor.commit();
			editor.putBoolean("isopengesture", isopengesture);
			
			editor.commit();
			
			this.finish();
			
			break;
		case R.id.text_reset:
			mIsFirstInput = true;
			updateCodeList("");
			mTextTip.setText(getString(R.string.set_gesture_pattern));
			break;
		default:
			break;
		}
	}
	
	private boolean isInputPassValidate(String inputPassword) {
		if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
			return false;
		}
		return true;
	}

	@Override
	public void setView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		
	}
	
}
