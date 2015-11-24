package com.ceb.activity;

import java.util.List;

import android.os.Bundle;
import android.view.View;

import com.ceb.fragment.FragmentKezhuanrang;
import com.ceb.fragment.FragmentYijieshu;
import com.ceb.fragment.FragmentZhuanrangzhong;
import com.ryg.fragment.ui.IndicatorFragmentActivity;


public class ActTransferProduct  extends IndicatorFragmentActivity{

	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	    }
	
	@Override
	protected int supplyTabs(List<TabInfo> tabs) {
		tabs.add(new TabInfo(0, "可转让",
                FragmentKezhuanrang.class,"转让产品",""));
        tabs.add(new TabInfo(1, "转让中",
                FragmentZhuanrangzhong.class,"转让产品",""));
        tabs.add(new TabInfo(2, "已结束",
                FragmentYijieshu.class,"转让产品",""));

		return 0;
	}
	public void onback(View v){
		finish();
	}

}
