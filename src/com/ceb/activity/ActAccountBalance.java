package com.ceb.activity;

import java.net.URLEncoder;
import java.util.TreeMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.caifuline.R;
import com.ceb.activity.ActAlterPasswd.AlterPasswdTask;
import com.ceb.base.BaseActivity;
import com.ceb.widge.MD5Util;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.model.ActionResultInfo;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;

public class ActAccountBalance extends BaseActivity {
	private TextView tv_recharge, tv_tixian;
	private TextView tv_withdrawalMoney, tv_frozenSum, tv_totalAssets;

	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	private Handler _handler;
	private SharedPreferences sp_kaihu, sp_kaika;
	private boolean isopen, iskaika;

	@Override
	public void setView() {
		setContentView(R.layout.act_accountbalance);

		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");

		sp_kaihu = getSharedPreferences("huifukaihu", 0);
		isopen = sp_kaihu.getBoolean("kaihu", false);
		sp_kaika = getSharedPreferences("kaika", 0);
		iskaika = sp_kaika.getBoolean("kaika", false);

		_userInfoService = new UserInfoService(ActAccountBalance.this);

		_handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(ActAccountBalance.this,
							"网络不给力，麻烦重试~o(>_<)o", Toast.LENGTH_SHORT).show();
					break;
				case 0:
					break;
				default:
					break;
				}
			};
		};

//		try {
//			getAccountBalance();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void initView() {
		tv_recharge = (TextView) findViewById(R.id.tv_recharge);
		tv_tixian = (TextView) findViewById(R.id.tv_tixian);

		tv_withdrawalMoney = (TextView) findViewById(R.id.tv_withdrawalMoney);
		tv_frozenSum = (TextView) findViewById(R.id.tv_frozenSum);
		tv_totalAssets = (TextView) findViewById(R.id.tv_totalAssets);
	}

	@Override
	public void setListener() {
		tv_recharge.setOnClickListener(this);
		tv_tixian.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_recharge:
			if (!isopen) {
//				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//						.setContentText("您还未开通汇付天下账户，快去开通吧!").show();
				 new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
	             .setContentText("您还未开通汇付天下账户，快去开通吧!")
	             .setCancelText("取消")
	             .setConfirmText("去开通")
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
	                	 Intent in = new Intent(ActAccountBalance.this, ActHuifu.class);
	         			startActivity(in);
	         			overridePendingTransition(android.R.anim.fade_in,
	         					android.R.anim.fade_out);
	                 	sDialog.dismiss();
	                 }
	             })
	             .show();
			} /*else if (!iskaika) {
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
						.setTitleText("Oops...").setContentText("您还未绑定银行卡!")
						.show();
			}*/ else {
				Intent in = new Intent(ActAccountBalance.this,
						ActRecharge.class);
				in.putExtra("money", tv_withdrawalMoney.getText().toString());
				startActivity(in);
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
			}
			break;
		case R.id.tv_tixian:
			if (!isopen) {
//				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//						.setContentText("您还未开通汇付天下账户，快去开通吧!").show();
				
				 new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
	             .setContentText("您还未开通汇付天下账户，快去开通吧!")
	             .setCancelText("取消")
	             .setConfirmText("去开通")
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
	                	 Intent in = new Intent(ActAccountBalance.this, ActHuifu.class);
	         			startActivity(in);
	         			overridePendingTransition(android.R.anim.fade_in,
	         					android.R.anim.fade_out);
	                 	sDialog.dismiss();
	                 }
	             })
	             .show();
			} else if (!iskaika) {
//				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//						.setContentText("您还未绑定银行卡!")
//						.show();
				
				 new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
	             .setContentText("您还未绑定银行卡!")
	             .setCancelText("取消")
	             .setConfirmText("去绑卡")
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
	                	 Intent in = new Intent(ActAccountBalance.this, ActAddBankCard.class);
	         			startActivity(in);
	         			overridePendingTransition(android.R.anim.fade_in,
	         					android.R.anim.fade_out);
	                 	sDialog.dismiss();
	                 }
	             })
	             .show();
			} else {
				startActivity(new Intent(ActAccountBalance.this,
						ActTixian.class));
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
			}
			break;
		default:
			break;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			getAccountBalance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onback(View v) {
		finish();
	}

	void getAccountBalance() {
		String  signature = "account.php:"+System.currentTimeMillis()/1000+":hxpay";
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

		new AccountBalanceTask().execute(userInfo);
	}

	class AccountBalanceTask extends
			AsyncTask<UserInfo, Void, ActionResultInfo> {

		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;

			info = _userInfoService.getAccountBalance(params[0]);

			return info;
		}

		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if (result == null || result.equals(null)) {
				_handler.sendEmptyMessage(-1);
			} else if (result.ret.equals("0")) {
				if (result.withdrawalMoney.equals("null")||result.withdrawalMoney.equals("")) {
					tv_withdrawalMoney.setText("0.00");
				} else {
					tv_withdrawalMoney.setText(result.withdrawalMoney + "");
				}
				if (result.frozenSum.equals("null")||result.frozenSum.equals("")) {
					tv_frozenSum.setText("0.00");
				} else {
					tv_frozenSum.setText(result.frozenSum + "");
				}
				tv_totalAssets.setText(result.totalAssets);

				// Toast.makeText(ActAccountBalance.this,result.msg ,
				// Toast.LENGTH_SHORT).show();
				_handler.sendEmptyMessage(0);
			} else {
				Toast.makeText(ActAccountBalance.this, result.msg,
						Toast.LENGTH_SHORT).show();
			}
		}

	}

}
