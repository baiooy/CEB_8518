package com.view;

import com.caifuline.R;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class EmptyViewHelper {
	 private ListView mListView;  
	 private View emptyView;  
	 private Context mContext;  
	 private String mEmptyText;  
	 private int resID;
	 private TextView mTextView;  
	 private FrameLayout parent;  
	  
	    public EmptyViewHelper(ListView listView, int resID, String text) {  
	        mListView = listView;  
	        mContext = listView.getContext();  
	        mEmptyText = text;  
	        this.resID = resID;
	        initEmptyView(resID);  
	    }  
	  
	    public EmptyViewHelper(ListView listView,int resId, FrameLayout parent) {  
	        mListView = listView;  
	        mContext = listView.getContext();  
	 //       mEmptyText = text;  
	        resID = resId;
	        this.parent = parent;  
	        initEmptyView(resId); 
	        
	    }  
	  
	    private void initEmptyView(int resId) {  
	        emptyView = View.inflate(mContext, resId, null);  
	          
	        LayoutParams lp = new LayoutParams(700, 700, Gravity.CENTER);  
	  
	        if(parent != null){
	        	parent.removeAllViews();
//	        	parent.removeView(mListView);
//	        	parent.removeView(emptyView);
	        	parent.addView(mListView);
	        }
	        parent.addView(emptyView, lp);  
	  
	//        mListView.setEmptyView(emptyView);  
//	        if (!TextUtils.isEmpty(mEmptyText)) {  
//	            ((TextView) emptyView.findViewById(R.id.empty_textview))  
//	                    .setText(mEmptyText);  
//	        }  
	    }  
}
