package com.ceb.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ceb.base.BaseActivity;
import com.ceb.fragment.FragmentDuifu;
import com.ceb.fragment.FragmentHuikuan;
import com.ceb.fragment.FragmentQita;
import com.ceb.fragment.FragmentToubiao;
import com.ceb.fragment.FragmentYongjin;
import com.ryg.fragment.ui.IndicatorFragmentActivity;
import com.ryg.fragment.ui.IndicatorFragmentActivity.TabInfo;

public class ActMyReward extends IndicatorFragmentActivity {
	private Boolean ispartner = false;
	@Override
	protected int supplyTabs(List<TabInfo> tabs) {
		ispartner = getIntent().getBooleanExtra("ispartner", false);
		Log.i("supplyTabs_ispartner", ""+ispartner);
		
		if(ispartner){
			
			tabs.add(new TabInfo(0, "佣金",
	                FragmentYongjin.class,"我的奖励",""));
	        tabs.add(new TabInfo(1, "其他",
	                FragmentQita.class,"我的奖励",""));
	        
		}else{
			 tabs.add(new TabInfo(0, "",
		                FragmentQita.class,"我的奖励",""));
		}
		
		return 0;
	}
	
	public void onback(View v){
		startActivity(new Intent(ActMyReward.this,MainActivity.class));
		overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
		finish();
	}

}
