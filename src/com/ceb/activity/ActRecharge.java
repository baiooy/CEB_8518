package com.ceb.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.caifuline.R;
import com.ceb.base.BaseActivity;

public class ActRecharge extends BaseActivity {
	private EditText et_money;
	private TextView tv_chongzhi,tv_balance,tv_chongzhirule;
	private String money;
	private SharedPreferences isLogin;
	private Boolean islogin = false;
	@Override
	public void setView() {
		setContentView(R.layout.act_recharge);

		Intent in = getIntent();
		money = in.getStringExtra("money");
		
		isLogin = getSharedPreferences("isLogin", 0);
		islogin = isLogin.getBoolean("islogin", false);
	}

	@Override
	public void initView() {
		et_money = (EditText) findViewById(R.id.et_money);
		tv_chongzhi = (TextView) findViewById(R.id.tv_chongzhi);
		tv_balance = (TextView) findViewById(R.id.tv_balance);
		tv_chongzhirule = (TextView) findViewById(R.id.tv_chongzhirule);
		if(!money.equals("null")){
			tv_balance.setText(money+"元");
		}else{
			tv_balance.setText("0.00 元");
		}
	}

	@Override
	public void setListener() {
		tv_chongzhi.setOnClickListener(this);
		tv_chongzhirule.setOnClickListener(this);
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_chongzhi:
			if(!islogin){
				Intent in = new Intent(ActRecharge.this, ActUserYanzheng.class);
				startActivity(in);
				overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			}else if(et_money.getText().toString().equals("")){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setContentText("请输入充值金额!")
                .show();
			}else if(et_money.getText().toString().startsWith("0")){
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setContentText("请输入正确的金额!")
                .show();
			}else if(Integer.parseInt(et_money.getText().toString()) < 50){
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setContentText("充值金额不能小于50元!")
                .show();
				//需做判断，不然输入过大易导致崩溃
			}else if(Integer.parseInt(et_money.getText().toString()) > 5000000){
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setContentText("单次充值金额不得高于500万元!")
                .show();
			}else{
				Intent in = new Intent(ActRecharge.this,ActChongzhiHuifu.class);
				in.putExtra("money", et_money.getText().toString());
				startActivity(in);
				finish();
				overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			}
			break;
		case R.id.tv_chongzhirule:
			startActivity(new Intent(ActRecharge.this,ActChongzhiRule.class));
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
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
	}
	
	public void onback(View v){
		finish();
	}

}
