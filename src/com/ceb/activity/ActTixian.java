package com.ceb.activity;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.adapter.AdapterBank;
import com.adapter.AdapterMainPage;
import com.adapter.AdapterTrade;
import com.caifuline.R;
import com.ceb.activity.ActTixianHuifu.BankCardTask;
import com.ceb.base.BaseActivity;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
import com.model.ActionResultInfo;
import com.model.BankCardList;
import com.model.BankCard_item;
import com.model.TradeDetail_item;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;
import com.view.UGallery;

import android.widget.RadioGroup.OnCheckedChangeListener;

@SuppressLint("HandlerLeak")
@SuppressWarnings("deprecation")
public class ActTixian extends BaseActivity {
	private RadioGroup radiogroup;
	private  RadioButton  rb_putong,rb_kuaisu,rb_jishi;
	private TextView tv_tixianrule,tv_balance,tv_tixian;
	private UGallery listView_bankcard;
	private SharedPreferences sp;
	private String userID,loginname;
	UserInfoService _userInfoService;
	AdapterBank mAdapter;
	private Handler mHandler;
	private String mentionType = "0";
	private EditText et_tixian;
	private String bankID;
	private String withdrawbank;
	private LinearLayout loading_tixian;
	private ImageView leftbtn=null;
	private ImageView rightbtn=null;
	private WindowManager wm=null;
	private WindowManager.LayoutParams wmParams=null;
	private LoadingDialog dialog;
	String requestUrl;
	@Override
	public void setView() {
		setContentView(R.layout.act_tixian);

		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		loginname = sp.getString("loginname", "null");
		
		_userInfoService = new UserInfoService(ActTixian.this);
		
		
		
		mHandler = new Handler(){
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					requestUrl = SppaConstant.getIPmobilev11()+"/app/ceb/withdraw.php?" +
							"platformID="+SppaConstant.ANDROID+"&userID="+userID+
							"&loginname="+loginname+"&money="+"1"+"&bankID="+AdapterBank.bankID
							+"&mentionType="+mentionType+"&withdrawbank="+AdapterBank.withdrawbank;
					
					break;
				case 1:
					Intent in = new Intent(ActTixian.this,ActTixianHuifu.class);
					in.putExtra("bankID", AdapterBank.bankID);
					in.putExtra("withdrawbank", AdapterBank.withdrawbank);
					in.putExtra("money", et_tixian.getText().toString());
					in.putExtra("mentionType", mentionType);
					startActivity(in);
					finish();
					overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
					break;
				case -2:
					
					break;
				default:
					break;
				}
				if(dialog != null && dialog.isShowing())
					dialog.dismiss();
			}
		};
		
		Log.i("版本号", android.os.Build.VERSION.SDK + "," + android.os.Build.VERSION.RELEASE);
		
//		initFloatView();
	}

	@Override
	public void initView() {
		radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		loading_tixian = (LinearLayout) findViewById(R.id.loading_tixian);
		
		
		rb_putong = (RadioButton) findViewById(R.id.rb_putong);
		rb_kuaisu = (RadioButton) findViewById(R.id.rb_kuaisu);
		rb_jishi = (RadioButton) findViewById(R.id.rb_jishi);
		
		tv_tixianrule = (TextView) findViewById(R.id.tv_tixianrule);
		tv_balance = (TextView) findViewById(R.id.tv_balance);
		tv_tixian = (TextView) findViewById(R.id.tv_tixian);
		et_tixian = (EditText) findViewById(R.id.et_tixian);
		
		listView_bankcard = (UGallery) findViewById(R.id.listView_bankcard);
		
		try {
			getBankCardManage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		radiogroup.setOnCheckedChangeListener(listen);
		listView_bankcard.setSelection(0); 
		
	}

		private OnCheckedChangeListener  listen=new OnCheckedChangeListener() {
		        @Override
		        public void onCheckedChanged(RadioGroup group, int checkedId) {
		        int id= group.getCheckedRadioButtonId();
		            switch (id) {
		            case R.id.rb_putong:
		            	mentionType = "0";
		                break;
		            case R.id.rb_kuaisu:
		            	mentionType = "1";
		                break;
		            case R.id.rb_jishi:
		            	if(!AdapterBank.bankID.equalsIgnoreCase("CMB") && !AdapterBank.bankID.equalsIgnoreCase("ICBC")){
		            		rb_jishi.setChecked(false);
		            		if(mentionType.equals("0")){
		            			rb_putong.setChecked(true);
		            		}else{
		            			rb_kuaisu.setChecked(true);
		            		}
		            		new SweetAlertDialog(ActTixian.this, SweetAlertDialog.ERROR_TYPE)
		                    .setContentText("即时提现仅支持工商银行，招商银行!")
		                    .show();
		            	}else{
		            		mentionType = "2";
		            		rb_jishi.setChecked(true);
		            	}
		            	
		                break;
		            default:
		            	mentionType = "0";
		                break;
		
		            }
		        }
		    };
	
	@Override
	public void setListener() {
		tv_tixianrule.setOnClickListener(this);
		tv_tixian.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_tixianrule:
			startActivity(new Intent(ActTixian.this,ActTixianRule.class));
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.tv_tixian:
			if(et_tixian.getText().toString().equals("")){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setContentText("请输入提现金额!")
                .show();
			}else if(et_tixian.getText().toString().startsWith("0")){
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setContentText("请输入正确的金额!")
                .show();
			}else if(mentionType.equals("-1")){
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setContentText("请选择提现方式!")
                .show();
			}else if(Double.parseDouble(et_tixian.getText().toString()) > Double.parseDouble(tv_balance.getText().toString())){
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setContentText("您的提现金额大于可用余额，请重新输入!")
                .show();
			}else{
				Log.i("onclick bankID--withdrawbank",bankID+"---"+ withdrawbank);
//				Intent in = new Intent(ActTixian.this,ActTixianHuifu.class);
//				in.putExtra("bankID", AdapterBank.bankID);
//				in.putExtra("withdrawbank", AdapterBank.withdrawbank);
//				in.putExtra("money", et_tixian.getText().toString());
//				in.putExtra("mentionType", mentionType);
//				startActivity(in);
//				finish();
//				overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
	//			Toast.makeText(ActTixian.this, "mentionType==>"+mentionType, 1000).show();
				try {
					gettixian();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			Log.i("bankID--withdrawbank",AdapterBank.bankID+"---"+ AdapterBank.withdrawbank);
			break;
		default:
			break;
		}

	}
	
	
	
	
	public void onback(View v){
		finish();
	}

	void getBankCardManage(){
		String  signature = "mention.php:"+System.currentTimeMillis()/1000+":hxpay";
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

		new BankCardTask().execute(userInfo);
	}
    
	class BankCardTask extends AsyncTask<UserInfo, Void, BankCardList>{
		@Override
		protected void onPreExecute() {
			dialog = new LoadingDialog(ActTixian.this);
			dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
			dialog.show();
			listView_bankcard.setEmptyView(loading_tixian);
		}
		
		@Override
		protected BankCardList doInBackground(UserInfo... params) {
			BankCardList info = null;
			
			info = _userInfoService.getBankCard(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(BankCardList result) {
			Log.i("result.ret", result.ret+"");
			if(result == null || result.equals(null)){
				mHandler.sendEmptyMessage(-1);
			}else if(result.ret == 0){
				if(result.item.size() > 0 ){
				//	bankID = result.item.get(0).bankID;
				//	withdrawbank = result.item.get(0).withdrawbank;
					tv_balance.setText(new DecimalFormat("0.00").format(Double.parseDouble(result.balance)));
			//		tv_balance.setText(result.balance);
					mAdapter = new AdapterBank(ActTixian.this, result,listView_bankcard,rb_putong);
					listView_bankcard.setAdapter(mAdapter);
					
					Message msg = new Message();
					msg.what = 0;
					msg.obj = result.item;
					mHandler.sendMessage(msg);
				}else{
					Toast.makeText(ActTixian.this,result.msg , Toast.LENGTH_SHORT).show();
				}
				//_handler.sendEmptyMessage(0);
			}else{
				Toast.makeText(ActTixian.this,result.msg , Toast.LENGTH_SHORT).show();
				mHandler.sendEmptyMessage(-2);
			}
		}
		
	}
	
	void gettixian(){

		new TixianTask().execute();
	}
    
	class TixianTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{
		@Override
		protected void onPreExecute() {
		}
		
		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.gettixianhuifu(requestUrl);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result != null &&result.ret != null && !result.ret.equals("")){
				Toast.makeText(ActTixian.this,result.msg , Toast.LENGTH_SHORT).show();
			}else{
		//		Toast.makeText(ActTixianHuifu.this,result.msg , Toast.LENGTH_SHORT).show();
				mHandler.sendEmptyMessage(1);
			}
		}
		
	}
	
	
	@Override
	protected void onPause() {
		if(dialog != null && dialog.isShowing()){
			dialog.dismiss();
		}
		super.onPause();
	}
	
}
