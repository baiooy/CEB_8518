package com.ceb.activity;

import java.util.List;

import android.os.Bundle;
import android.view.View;

import com.caifuline.R;
import com.ceb.fragment.FragmentHuikuan;
import com.ceb.fragment.FragmentDuifu;
import com.ceb.fragment.FragmentToubiao;
import com.ryg.fragment.ui.IndicatorFragmentActivity;


public class ActInvestHistory  extends IndicatorFragmentActivity{

	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	    }
	
	@Override
	protected int supplyTabs(List<TabInfo> tabs) {
		tabs.add(new TabInfo(0, "回款中",
                FragmentHuikuan.class,"投资记录",""));
        tabs.add(new TabInfo(1, "投标中",
                FragmentToubiao.class,"投资记录",""));
        tabs.add(new TabInfo(2, "已兑付",
                FragmentDuifu.class,"投资记录",""));

		return 0;
	}
	
	public void onback(View v){
		finish();
	}

}
