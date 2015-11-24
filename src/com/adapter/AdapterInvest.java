package com.adapter;
import java.util.List;

import com.caifuline.R;
import com.model.NoPartner;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterInvest extends BaseAdapter {
	private Context mContext;
	Animator currentAnimation;
	List<NoPartner> mData;
	public AdapterInvest(Context c,List<NoPartner> no){
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
		final String noPartnerAccount = mData.get(position).noPartnerAccount;
		final String noPartnerRegisterType = mData.get(position).noPartnerRegisterType;
		final String noPartnerDepositType = mData.get(position).noPartnerDepositType;
		final String noPartnerInvestType = mData.get(position).noPartnerInvestType;
		final String noPartnerRewardPoints = mData.get(position).noPartnerRewardPoints;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.invest_item, null);
		}
		
		TextView tv_noPartnerAccount = (TextView) convertView.findViewById(R.id.tv_noPartnerAccount);
		TextView tv_noPartnerRegisterType = (TextView) convertView.findViewById(R.id.tv_noPartnerRegisterType);
		TextView tv_noPartnerDepositType = (TextView) convertView.findViewById(R.id.tv_noPartnerDepositType);
		TextView tv_noPartnerInvestType = (TextView) convertView.findViewById(R.id.tv_noPartnerInvestType);
		TextView tv_noPartnerRewardPoints = (TextView) convertView.findViewById(R.id.tv_noPartnerRewardPoints);
		
		tv_noPartnerAccount.setText(noPartnerAccount);
		if(noPartnerRegisterType.equals("1")){
			tv_noPartnerRegisterType.setText("是");
		}else{
			tv_noPartnerRegisterType.setText("否");
		}
		
		if(noPartnerDepositType.equals("1")){
			tv_noPartnerDepositType.setText("是");
		}else{
			tv_noPartnerDepositType.setText("否");
		}
		
		if(noPartnerInvestType.equals("1")){
			tv_noPartnerInvestType.setText("是");
		}else{
			tv_noPartnerInvestType.setText("否");
		}
		
		tv_noPartnerRewardPoints.setText(noPartnerRewardPoints);
	
		return convertView;
	}
	
	

}
