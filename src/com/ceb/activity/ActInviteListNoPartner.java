package com.ceb.activity;

import java.util.List;

import com.caifuline.R;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ceb.fragment.FragmentJifenAward;
import com.ceb.fragment.FragmentQita;
import com.ceb.fragment.FragmentXianjinAward;
import com.ceb.fragment.FragmentYongjin;
import com.ryg.fragment.ui.IndicatorFragmentActivity;

public class ActInviteListNoPartner extends IndicatorFragmentActivity{
	private TextView tv_yaoqingrule;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	protected int supplyTabs(List<TabInfo> tabs) {
		tabs.add(new TabInfo(0, "现金奖励",
				FragmentXianjinAward.class,"邀请记录","详细规则"));
        tabs.add(new TabInfo(1, "积分奖励",
        		FragmentJifenAward.class,"邀请记录","详细规则"));
		return 0;
	}
	
	public void onback(View v){
		finish();
	}

}
