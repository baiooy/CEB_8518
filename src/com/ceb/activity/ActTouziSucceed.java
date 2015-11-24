package com.ceb.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.caifuline.R;
import com.ceb.base.BaseActivity;

public class ActTouziSucceed extends BaseActivity {

	private Button btn_touzijilu;

	@Override
	public void setView() {
		setContentView(R.layout.act_touzisuccess);
	}

	@Override
	public void initView() {
		btn_touzijilu = (Button) findViewById(R.id.btn_touzijilu);
	}

	@Override
	public void setListener() {
		btn_touzijilu.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_touzijilu:
			Intent in = new Intent(ActTouziSucceed.this,ActTouzi.class);
			startActivity(in);
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;

		default:
			break;
		}

	}
	
	public void onback(View v){
		finish();
	}

}
