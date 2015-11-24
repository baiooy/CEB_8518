package com.ceb.activity;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.adapter.ExpandAdapter;
import com.caifuline.R;
import com.ceb.base.BaseActivity;
import com.common.Item;

public class ActCommonProblem extends BaseActivity {
	private ExpandableListView mListView;
	private ExpandAdapter mAdapter = null;
	private List<List<Item>> mData = new ArrayList<List<Item>>();
	private int lastClick = -1;//上一次点击的group的position  
	private int[] mGroupArrays = new int[] { R.array.zhucedenglu,
											 R.array.xinshourumen, 
											 R.array.yinhangka ,
											 R.array.xiangguanyouhui,
											 R.array.anquanbaozhang};

	private int[] mDetailIds = new int[] { R.array.zhucedenglu_detail,
										   R.array.xinshourumen_detail, 
										   R.array.yinhangka_detail,
										   R.array.xiangguanyouhui_detail,
										   R.array.anquanbaozhang_detail };
	
	 private int[][] mImageIds = new int[][] {
	            { 0, 0,0},{0,0,0,0,0,0,0,0,0,0,0},{R.drawable.combank,0,0},{0,0,0,0},{0,0,0,0,0}};
	            

	@Override
	public void setView() {
		setContentView(R.layout.act_commonproblem);

	}

	@Override
	public void initView() {
		mListView = (ExpandableListView) findViewById(R.id.mListView);

		initData();

		mListView.setGroupIndicator(null);
		
//		mListView.setGroupIndicator(getResources().getDrawable(
//				R.drawable.expander_floder));//系统自带的图标
		mAdapter = new ExpandAdapter(this, mData);
		mListView.setAdapter(mAdapter);
		mListView
				.setDescendantFocusability(ExpandableListView.FOCUS_AFTER_DESCENDANTS);
		// mListView.setOnChildClickListener(this);
		
//		mListView.setOnGroupClickListener(new OnGroupClickListener() {  
//            public boolean onGroupClick(ExpandableListView parent, View v,  
//                    int groupPosition, long id) {  
//                  
//                if(lastClick == -1)  
//                {  
//                	mListView.expandGroup(groupPosition);  
//                }  
//                  
//                if(lastClick != -1 && lastClick != groupPosition)  
//                {  
//                	mListView.collapseGroup(lastClick);  
//                	mListView.expandGroup(groupPosition);  
//                }  
//                else if(lastClick == groupPosition)   
//                {  
//                    if(mListView.isGroupExpanded(groupPosition))  
//                    	mListView.collapseGroup(groupPosition);  
////                    else if(!mListView.isGroupExpanded(groupPosition))  
////                    	mListView.expandGroup(groupPosition);  
//                }  
//                  
//                lastClick = groupPosition;  
//                return true;  
//            }  
//        });  
		

	}

	private void initData() {
		
		for (int i = 0; i < mGroupArrays.length; i++) {//3
			List<Item> list = new ArrayList<Item>();
			String[] childs = getStringArray(mGroupArrays[i]);
			String[] details = getStringArray(mDetailIds[i]);
			Log.i("mGroupArrays//childs", mGroupArrays.length+"||"+childs.length);
			for (int j = 0; j < childs.length; j++) {
				Item item = new Item(mImageIds[i][j],childs[j], details[j]);
				list.add(item);
			}
			mData.add(list);
		}
	}

	private String[] getStringArray(int resId) {
		return getResources().getStringArray(resId);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
