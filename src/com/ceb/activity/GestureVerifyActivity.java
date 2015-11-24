package com.ceb.activity;

import com.caifuline.R;
import com.ceb.base.BaseActivity;
import com.wujay.fund.widget.GestureContentView;
import com.wujay.fund.widget.GestureDrawline.GestureCallBack;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 手势绘制/校验界面
 *
 */
public class GestureVerifyActivity extends BaseActivity implements android.view.View.OnClickListener {
	/** 手机号码*/
	public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
	/** 意图 */
	public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
	private RelativeLayout mTopLayout;
	private TextView mTextTitle;
	private TextView mTextCancel;
	private ImageView mImgUserLogo;
	private TextView mTextPhoneNumber;
	private TextView mTextTip;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	private TextView mTextForget;
	private TextView mTextOther;
	private String mParamPhoneNumber;
	private long mExitTime = 0;
	private int mParamIntentCode;
	private SharedPreferences sp;
	private String loginname;
	private String flag;
	private String enter;
	SharedPreferences isLogin ;
	SharedPreferences.Editor editor2 ;
	private int count = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_verify);
		ObtainExtraData();
		setUpViews();
		setUpListeners();
	}
	
	private void ObtainExtraData() {
		mParamPhoneNumber = getIntent().getStringExtra(PARAM_PHONE_NUMBER);
		mParamIntentCode = getIntent().getIntExtra(PARAM_INTENT_CODE, 0);
		
		Intent in = getIntent();
		flag = in.getStringExtra("flag");
		enter = in.getStringExtra("in");
		
		isLogin = getSharedPreferences("isLogin", 0);
		editor2 = isLogin.edit();
		 count = 0;
		sp = getSharedPreferences("USERID", 0);
		loginname = sp.getString("loginname", "null");
	}
	
	private void setUpViews() {
		mTopLayout = (RelativeLayout) findViewById(R.id.top_layout);
		mTextTitle = (TextView) findViewById(R.id.text_title);
		mTextCancel = (TextView) findViewById(R.id.text_cancel);
		mImgUserLogo = (ImageView) findViewById(R.id.user_logo);
		mTextPhoneNumber = (TextView) findViewById(R.id.text_phone_number);
		mTextTip = (TextView) findViewById(R.id.text_tip);
		mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
		mTextForget = (TextView) findViewById(R.id.text_forget_gesture);
		mTextOther = (TextView) findViewById(R.id.text_other_account);
		
		mTextPhoneNumber.setText("您好，"+loginname);
		// 初始化一个显示各个点的viewGroup
		SharedPreferences isF = getSharedPreferences("gesturepasswd", 0);
		String str= isF.getString("inputCode", "");
		Log.d("gesturepasswd", "gesturepasswd"+str);
		mGestureContentView = new GestureContentView(this, true, str,//检验手势密码 与设置的手势密码比较
				new GestureCallBack() {

					@Override
					public void onGestureCodeInput(String inputCode) {

					}

					@Override
					public void checkedSuccess() {
						mGestureContentView.clearDrawlineState(0L);
						if(flag != null && flag.equals("close")){
//							Intent in2 = new Intent(GestureVerifyActivity.this,GestureEditActivity.class);
//		        		//	in2.putExtra("flag", "open");
//							GestureVerifyActivity.this.startActivity(in2);
//		        			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
							GestureVerifyActivity.this.finish();
							
						}else{
							Intent bob = new Intent(GestureVerifyActivity.this,
									MainActivity.class);
							
							startActivity(bob);
							GestureVerifyActivity.this.finish();
							overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
						}
					}

					@Override
					public void checkedFail() {
						count++;
						mGestureContentView.clearDrawlineState(1300L);
						mTextTip.setVisibility(View.VISIBLE);
						if(count < 5){
							mTextTip.setText(Html
									.fromHtml("<font color='#ffffff'>密码错误</font>")+" , 你还有"+(5-count)+"次机会");
						}else{
							SharedPreferences isLogin = getSharedPreferences("isLogin", 0);
							SharedPreferences.Editor editor2 = isLogin.edit();
							editor2.clear();
							editor2.commit();
							editor2.putBoolean("islogin", false);//退出登录
							editor2.commit();
							finish();
							Intent in = new Intent(GestureVerifyActivity.this,ActUserYanzheng.class);
							in.putExtra("flag", "forget");
							in.putExtra("in", enter);
							startActivity(in);
							overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
//							SharedPreferences sp_opengesture = getSharedPreferences("OPENGESTURE", 0);
//							SharedPreferences.Editor editor = sp_opengesture.edit();
//							editor.clear();
//							editor.commit();
//							editor.putBoolean("isopengesture", true);//退出登录
//							editor.commit();
						}
						
						// 左右移动动画
						Animation shakeAnimation = AnimationUtils.loadAnimation(GestureVerifyActivity.this, R.anim.shake);
						mTextTip.startAnimation(shakeAnimation);
					}
				});
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(mGestureContainer);
	}
	
	private void setUpListeners() {
		mTextCancel.setOnClickListener(this);
		mTextForget.setOnClickListener(this);
		mTextOther.setOnClickListener(this);
		
	}
	
	private String getProtectedMobile(String phoneNumber) {
		if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 11) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		builder.append(phoneNumber.subSequence(0,3));
		builder.append("****");
		builder.append(phoneNumber.subSequence(7,11));
		return builder.toString();
	}
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_cancel:
			this.finish();
			break;
		case R.id.text_forget_gesture:
			
			SharedPreferences sp_opengesture = getSharedPreferences("OPENGESTURE", 0);
			SharedPreferences.Editor editor = sp_opengesture.edit();
			editor.clear();
			editor.commit();
			editor.putBoolean("isopengesture", false);
		
			editor.commit();
		
			editor2.clear();
			editor2.commit();
			editor2.putBoolean("islogin", false);//退出登录
			editor2.commit();
			Intent in = new Intent(GestureVerifyActivity.this,ActUserYanzheng.class);
			in.putExtra("flag", "forget");
			in.putExtra("in", enter);
			startActivity(in);
			finish();
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.text_other_account:
			
			SharedPreferences sp_openges = getSharedPreferences("OPENGESTURE", 0);
			SharedPreferences.Editor editor2 = sp_openges.edit();
			editor2.clear();
			editor2.commit();
			editor2.putBoolean("isopengesture", false);
		
			editor2.commit();
			
			editor2.clear();
			editor2.commit();
			editor2.putBoolean("islogin", false);//退出登录
			editor2.commit();
			Intent ii = new Intent(GestureVerifyActivity.this,ActUserYanzheng.class);
			ii.putExtra("in", enter);
			ii.putExtra("flag", "other");
			startActivity(ii);
			finish();
			break;
		default:
			break;
		}
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
