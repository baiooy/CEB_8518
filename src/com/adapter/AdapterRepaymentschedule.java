package com.adapter;

import java.text.DecimalFormat;

import com.caifuline.R;
import com.caifuline.R.color;
import com.model.InvestplanList;
import com.view.CircularProgressDrawable;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdapterRepaymentschedule extends BaseAdapter {
	private Context mContext;
	private InvestplanList mData;
	Animator currentAnimation;
	public AdapterRepaymentschedule(Context c,InvestplanList i){
		mContext = c;
		
		mData = i;
	}
	@Override
	public int getCount() {
		if (mData != null) {
			return mData.item.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.item.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final String title = mData.item.get(position).title;
		final String money = mData.item.get(position).money;
		final String date = mData.item.get(position).date;
		final String interestReceive = mData.item.get(position).interestReceive;
		final String principalReceive = mData.item.get(position).principalReceive;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.repaymentschedule_item, null);
		}
		
		TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
		TextView tv_money = (TextView) convertView.findViewById(R.id.tv_money);
		TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);
		
		tv_title.setText(title);
		if(money.equals("0")){
			tv_money.setTextColor(mContext.getResources().getColor(color.zhusediao));
			tv_money.setText("￥"+new DecimalFormat("0.00").format(Double.parseDouble(interestReceive)+Double.parseDouble(principalReceive))+"(待收)");
		}else{
			tv_money.setTextColor(Color.rgb(000, 204, 153));
			tv_money.setText("￥"+money);
		}
		
		tv_date.setText(date);
		
		return convertView;
	}
	
	

}
