package com.adapter;

import java.util.ArrayList;
import java.util.List;

import com.caifuline.R;
import com.model.Brokerage;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterXianjin extends BaseAdapter {
	private Context mContext;
	Animator currentAnimation;
	private List<Brokerage> mData;
	public AdapterXianjin(Context c,ArrayList<Brokerage> br){
		mContext = c;
		mData = br;
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
		final String brokerageAccount = mData.get(position).brokerageAccount;
		final String investMoney = mData.get(position).investMoney;
		final String investData = mData.get(position).investData;
		final String brokerage = mData.get(position).brokerage;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.xianjin_item, null);
		}
		
		TextView tv_brokerageAccount = (TextView) convertView.findViewById(R.id.tv_brokerageAccount);
		TextView tv_investMoney = (TextView) convertView.findViewById(R.id.tv_investMoney);
		TextView tv_investData = (TextView) convertView.findViewById(R.id.tv_investData);
		TextView tv_brokerage = (TextView) convertView.findViewById(R.id.tv_brokerage);
		
		tv_brokerageAccount.setText(brokerageAccount);
		tv_investMoney.setText(investMoney);
		tv_investData.setText(investData);
		tv_brokerage.setText(brokerage);
		
	
		return convertView;
	}
	
	

}
