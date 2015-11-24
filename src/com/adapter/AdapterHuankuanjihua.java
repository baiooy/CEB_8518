package com.adapter;



import java.util.ArrayList;

import com.caifuline.R;
import com.ceb.activity.ActContract;
import com.ceb.activity.ActRepaymentSchedule;
import com.model.InvestmentList_item;
import com.model.RepaymentList_item;
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

public class AdapterHuankuanjihua extends BaseAdapter {
	private Context mContext;
	ArrayList<RepaymentList_item> mData;
	public AdapterHuankuanjihua(Context c,ArrayList<RepaymentList_item> i){
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
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final String repaymentDate = mData.get(position).repaymentDate;
		final String maturityYield = mData.get(position).maturityYield;
		final String principal = mData.get(position).principal;
		
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.huankuanjihua_item, null);
		}
		
		final TextView tv_repaymentDate = (TextView) convertView.findViewById(R.id.tv_repaymentDate);
		final TextView tv_maturityYield = (TextView) convertView.findViewById(R.id.tv_maturityYield);
		final TextView tv_principal = (TextView) convertView.findViewById(R.id.tv_principal);
		
		tv_repaymentDate.setText(repaymentDate);
		tv_maturityYield.setText(maturityYield);
		tv_principal.setText(principal);
		
		
		return convertView;
	}
	
	

}
