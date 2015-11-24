package com.ceb.activity;

import java.net.URLEncoder;
import java.util.TreeMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.caifuline.R;
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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;


@SuppressLint("HandlerLeak")
public class ActFeedBack extends BaseActivity {
	private EditText et_content,et_mail;
	private TextView mTextView = null;
	private static final int MAX_COUNT = 500;
	private RadioGroup rp;
	private RadioButton rb1,rb2,rb3;
	private String type = "-1";
	private Button btnSEND;
	private SharedPreferences isLogin;
	private Boolean islogin = false;
	private SharedPreferences sp;
	private String userID;
	private Handler _handler;
	UserInfoService _userInfoService;
	@Override
	public void setView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_feedback);

		isLogin = getSharedPreferences("isLogin", 0);
		islogin = isLogin.getBoolean("islogin", false);
		
		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		
		_userInfoService = new UserInfoService(ActFeedBack.this);
		
		_handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(ActFeedBack.this, "网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
					Toast.makeText(ActFeedBack.this, "反馈成功！",
							Toast.LENGTH_SHORT).show();
					finish();
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
		et_content = (EditText) findViewById(R.id.et_content);
		et_mail = (EditText) findViewById(R.id.et_mail);
		et_content.addTextChangedListener(mTextWatcher);
		et_content.setSelection(et_content.length()); // 将光标移动最后一个字符后面
		
		mTextView = (TextView) findViewById(R.id.count);
		setLeftCount();
		
		btnSEND = (Button) findViewById(R.id.btnSEND);
		
		rp = (RadioGroup) findViewById(R.id.rp);
		rb1 = (RadioButton) findViewById(R.id.rb1);
		rb2 = (RadioButton) findViewById(R.id.rb2);
		rb3 = (RadioButton) findViewById(R.id.rb3);
		
		rp.setOnCheckedChangeListener(listen);
	}

	private OnCheckedChangeListener  listen=new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
        int id= group.getCheckedRadioButtonId();
            switch (id) {
            case R.id.rb1:
            	type = "1";
                break;
            case R.id.rb2:
            	type = "2";
                break;
            case R.id.rb3:
            	type = "3";
                break;
            default:
            	type = "-1";
                break;

            }
        }
    };
	
	@Override
	public void setListener() {
		btnSEND.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSEND:
			/**
			 * 此时判断是否处于登陆状态，若已登录，跳入立即投资界面，若未登录，跳入输入手机号界面，然后走 用户验证接口判断是否为注册用户。
			 */
			if (!islogin) {// 已登录
				Intent in = new Intent(ActFeedBack.this, ActUserYanzheng.class);
				startActivity(in);
				overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			} else if(type.equals("-1")){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setContentText("请选择反馈类型!")
                .show();
			}else if(et_content.getText().toString().equals("")){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setContentText("请输入反馈内容!")
                .show();
			}else{
				try {
					feedback();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		isLogin = getSharedPreferences("isLogin", 0);
		islogin = isLogin.getBoolean("islogin", false);
		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
	}
	
	void feedback(){
		String  signature = "feedback.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.type = type;
		userInfo.content = et_content.getText().toString();
		userInfo.mail = et_mail.getText().toString();
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(this);
		
		new FeedBackTask().execute(userInfo);
	}
	
	class FeedBackTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{

		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.getFeedback(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result == null){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0")){
				_handler.sendEmptyMessage(0);
			}else{
				Toast.makeText(ActFeedBack.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	
	public void onback(View v){
		finish();
	}
	
	/**
	 * 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
	 * 
	 * @param c
	 * @return
	 */
	private long calculateLength(CharSequence c) {
		double len = 0;
		for (int i = 0; i < c.length(); i++) {
			int tmp = (int) c.charAt(i);
			if (tmp > 0 && tmp < 127) {
				len += 0.5;
			} else {
				len++;
			}
		}
		return Math.round(len);
	}

	
	/**
	 * 刷新剩余输入字数
	 */
	private void setLeftCount() {
		mTextView.setText(String.valueOf((MAX_COUNT - getInputCount())));
	}
	
	


	/**
	 * 获取用户输入的分享内容字数
	 * 
	 * @return
	 */
	private long getInputCount() {
		return calculateLength(et_content.getText().toString());
	}
	
	private TextWatcher mTextWatcher = new TextWatcher() {

		private int editStart;

		private int editEnd;

		public void afterTextChanged(Editable s) {
			editStart = et_content.getSelectionStart();
			editEnd = et_content.getSelectionEnd();

			// 先去掉监听器，否则会出现栈溢出
			et_content.removeTextChangedListener(mTextWatcher);

			// 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
			// 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
			while (calculateLength(s.toString()) > MAX_COUNT) { // 当输入字符个数超过限制的大小时，进行截断操作
				s.delete(editStart - 1, editEnd);
				editStart--;
				editEnd--;
			}
			et_content.setText(s);
			et_content.setSelection(editStart);

			// 恢复监听器
			et_content.addTextChangedListener(mTextWatcher);

			setLeftCount();
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

	};
	

}
