package com.ceb.fragment;

import com.adapter.AdapterZhuanrangzhong;
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

public class FragmentZhuanrangzhong extends Fragment {
	private SwipeRefreshLayout swipe_zhuanrangzhong;
	private TextView tv_tips_zhuanrangzhong;
	private ListView listView_zhuanrangzhong;
	private Handler mHandler;
    public FragmentZhuanrangzhong() {
        super();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
        return inflater.inflate(R.layout.fragment_zhuanrangzhong, container, false);
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		
		
		swipe_zhuanrangzhong = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_zhuanrangzhong);
		tv_tips_zhuanrangzhong = (TextView) getActivity().findViewById(R.id.tv_tips_zhuanrangzhong);
		listView_zhuanrangzhong = (ListView) getActivity().findViewById(R.id.listView_zhuanrangzhong);
		
		listView_zhuanrangzhong.setAdapter(new AdapterZhuanrangzhong(getActivity()));
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					tv_tips_zhuanrangzhong.setVisibility(View.GONE);
					break;

				default:
					break;
				}
			}
		};
		
		swipe_zhuanrangzhong.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		/**
		 *  解决listview与SwipeRefreshLayout滑动冲突问题
		 */
		listView_zhuanrangzhong.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
			        int visibleItemCount, int totalItemCount) {
			    boolean enable = false;
			    if(listView_zhuanrangzhong != null && listView_zhuanrangzhong.getChildCount() > 0){
			        // check if the first item of the list is visible
			        boolean firstItemVisible = listView_zhuanrangzhong.getFirstVisiblePosition() == 0;
			        // check if the top of the first item is visible
			        boolean topOfFirstItemVisible = listView_zhuanrangzhong.getChildAt(0).getTop() == 0;
			        // enabling or disabling the refresh layout
			        enable = firstItemVisible && topOfFirstItemVisible;
			    }
			    swipe_zhuanrangzhong.setEnabled(enable);
			}});
		
		swipe_zhuanrangzhong
		.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				tv_tips_zhuanrangzhong.setVisibility(View.VISIBLE);
				tv_tips_zhuanrangzhong.setText("正在刷新...");
				
				
				processOnRefresh();
			}
		});
		
		super.onActivityCreated(savedInstanceState);
	}
	public void processOnRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				tv_tips_zhuanrangzhong.setText("刷新成功！");
				swipe_zhuanrangzhong.setRefreshing(false);
				Message msg = new Message();
				msg.what = 0;
				mHandler.sendMessageDelayed(msg, 1000);
				
			}
		}, 3000);
		
	}
    

}
