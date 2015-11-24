package com.adapter;



import java.util.ArrayList;

import com.caifuline.R;
import com.ceb.activity.ActContract;
import com.ceb.activity.ActRepaymentSchedule;
import com.model.InvestmentList_item;
import com.view.CircularProgressDrawable;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdapterTouzijilu extends BaseAdapter {
	private Context mContext;
	ArrayList<InvestmentList_item> mData;
	public AdapterTouzijilu(Context c,ArrayList<InvestmentList_item> i){
		mContext = c;
		mData = i;
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
		final String investUsername = mData.get(position).investUsername;
		final String totalAmount = mData.get(position).totalAmount;
		final String investmentDate = mData.get(position).investmentDate;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.touzijilu_item, null);
		}
		
		TextView tv_investUsername = (TextView) convertView.findViewById(R.id.tv_investUsername);
		TextView tv_totalAmount = (TextView) convertView.findViewById(R.id.tv_totalAmount);
		TextView tv_investmentDate = (TextView) convertView.findViewById(R.id.tv_investmentDate);
		
		tv_investUsername.setText(investUsername);
		tv_totalAmount.setText(totalAmount);
		tv_investmentDate.setText(investmentDate);
		
		return convertView;
	}
	
	

}
