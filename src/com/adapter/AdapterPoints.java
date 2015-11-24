package com.adapter;
import java.util.List;

import com.caifuline.R;
import com.model.NoPartner;
import com.model.Points;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterPoints extends BaseAdapter {
	private Context mContext;
	Animator currentAnimation;
	List<Points> mData;
	public AdapterPoints(Context c,List<Points> no){
		mContext = c;
		mData = no;
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
	//	final String pointsAccount = mData.get(position).pointsAccount;
		final String pointsRegisterType = mData.get(position).pointsRegisterType;
		final String pointsDepositType = mData.get(position).pointsDepositType;
		final String pointsInvestType = mData.get(position).pointsInvestType;
		final String pointsRewardPoints = mData.get(position).pointsRewardPoints;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.invest_item, null);
		}
		
		TextView tv_noPartnerAccount = (TextView) convertView.findViewById(R.id.tv_noPartnerAccount);
		TextView tv_noPartnerRegisterType = (TextView) convertView.findViewById(R.id.tv_noPartnerRegisterType);
		TextView tv_noPartnerDepositType = (TextView) convertView.findViewById(R.id.tv_noPartnerDepositType);
		TextView tv_noPartnerInvestType = (TextView) convertView.findViewById(R.id.tv_noPartnerInvestType);
		TextView tv_noPartnerRewardPoints = (TextView) convertView.findViewById(R.id.tv_noPartnerRewardPoints);
		
	//	tv_noPartnerAccount.setText(pointsAccount);
		if(pointsRegisterType.equals("0")){
			tv_noPartnerRegisterType.setText("否");
		}else{
			tv_noPartnerRegisterType.setText("是");
		}
		
		if(pointsDepositType.equals("0")){
			tv_noPartnerDepositType.setText("否");
		}else{
			tv_noPartnerDepositType.setText("是");
		}
		
		if(pointsInvestType.equals("0")){
			tv_noPartnerInvestType.setText("否");
		}else{
			tv_noPartnerInvestType.setText("是");
		}
		
		tv_noPartnerRewardPoints.setText(pointsRewardPoints);
	
		return convertView;
	}
	
	

}
