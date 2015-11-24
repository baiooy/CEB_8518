package com.ceb.fragment;

import com.adapter.AdapterKezhuanrang;
import com.caifuline.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class FragmentKezhuanrang extends Fragment {
	private SwipeRefreshLayout swipe_kezhuanrang;
	private TextView tv_tips_kezhuanrang;
	private ListView listView_kezhuanrang;
	private Handler mHandler;
    public FragmentKezhuanrang() {
        super();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
        return inflater.inflate(R.layout.fragment_kezhuanrang, container, false);
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		
		
		swipe_kezhuanrang = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_kezhuanrang);
		tv_tips_kezhuanrang = (TextView) getActivity().findViewById(R.id.tv_tips_kezhuanrang);
		listView_kezhuanrang = (ListView) getActivity().findViewById(R.id.listView_kezhuanrang);
		
		listView_kezhuanrang.setAdapter(new AdapterKezhuanrang(getActivity()));
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					tv_tips_kezhuanrang.setVisibility(View.GONE);
					break;

				default:
					break;
				}
			}
		};
		
		swipe_kezhuanrang.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		/**
		 *  解决listview与SwipeRefreshLayout滑动冲突问题
		 */
		listView_kezhuanrang.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
			        int visibleItemCount, int totalItemCount) {
			    boolean enable = false;
			    if(listView_kezhuanrang != null && listView_kezhuanrang.getChildCount() > 0){
			        // check if the first item of the list is visible
			        boolean firstItemVisible = listView_kezhuanrang.getFirstVisiblePosition() == 0;
			        // check if the top of the first item is visible
			        boolean topOfFirstItemVisible = listView_kezhuanrang.getChildAt(0).getTop() == 0;
			        // enabling or disabling the refresh layout
			        enable = firstItemVisible && topOfFirstItemVisible;
			    }
			    swipe_kezhuanrang.setEnabled(enable);
			}});
		
		swipe_kezhuanrang
		.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				tv_tips_kezhuanrang.setVisibility(View.VISIBLE);
				tv_tips_kezhuanrang.setText("正在刷新...");
				
				
				processOnRefresh();
			}
		});
		
		super.onActivityCreated(savedInstanceState);
	}
	public void processOnRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				tv_tips_kezhuanrang.setText("刷新成功！");
				swipe_kezhuanrang.setRefreshing(false);
				Message msg = new Message();
				msg.what = 0;
				mHandler.sendMessageDelayed(msg, 1000);
				
			}
		}, 3000);
		
	}
    

}
