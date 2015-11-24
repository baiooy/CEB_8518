package com.adapter;



import java.util.List;

import com.caifuline.R;
import com.ceb.activity.ActNoticeDetail;
import com.model.Message_item;
import com.model.Notice_item;
import com.model.ProductList_item;
import com.spp.SppaConstant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdapterNotice extends BaseAdapter {
	private Context mContext;
	List<Notice_item> mData;
	public AdapterNotice(Context c,List<Notice_item> data){
		mContext = c;
		mData = data;
	}
	@Override
	public int getCount() {
		if (mData != null) {
			return mData.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final String title = mData.get(position).title;
		final String date = mData.get(position).date;
		final String noticeID = mData.get(position).noticeID;
		final String content = mData.get(position).content;
		final String htmlurl = mData.get(position).htmlurl;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext.getApplicationContext())
					.inflate(R.layout.notice_item, null);
		}
		
		TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
		TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);
		
		tv_title.setText(title);
		tv_date.setText(date);
		
		LinearLayout ll_osmsg = (LinearLayout) convertView.findViewById(R.id.ll_osmsg);
		ll_osmsg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent in = new Intent(mContext,ActNoticeDetail.class);
				in.putExtra("htmlurl", htmlurl);
				mContext.startActivity(in);
				((Activity) mContext).overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			}
		});
		
		return convertView;
	}

}
