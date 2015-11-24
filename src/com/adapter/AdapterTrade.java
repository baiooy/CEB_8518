package com.adapter;

import java.util.ArrayList;

import com.caifuline.R;
import com.caifuline.R.color;
import com.model.TradeDetailList;
import com.model.TradeDetail_item;
import com.spp.SppaConstant;
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

public class AdapterTrade extends BaseAdapter {
	private Context mContext;
	ArrayList<TradeDetail_item> mData;
	Animator currentAnimation;
	public AdapterTrade(Context c,ArrayList<TradeDetail_item> t){
		mContext = c;
		
		mData = t; 
		 
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
		final String type = mData.get(position).type;
		final String money = mData.get(position).money;
		final String date = mData.get(position).date;
		final String typeinfo = mData.get(position).typeinfo;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.trade_item, null);
		}
		
		TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
	//	TextView tv_subtitle = (TextView) convertView.findViewById(R.id.tv_subtitle);
		TextView tv_money = (TextView) convertView.findViewById(R.id.tv_money);
		TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);
		ImageView iv_type = (ImageView) convertView.findViewById(R.id.iv_type);
		
		tv_title.setText(typeinfo);
	//	tv_subtitle.setText(subtitle);
		if(money.substring(0, 1).equals("-")){
			tv_money.setTextColor(mContext.getResources().getColor(color.zhusediao));
			tv_money.setText(money);
		}else{
			tv_money.setTextColor(Color.rgb(000, 204, 102));
			tv_money.setText("+"+money);
		}
		
		tv_date.setText(SppaConstant.getStrTime(date));
		
		if(type.equals("10")){
			iv_type.setImageResource(R.drawable.chong);
		}else if(type.equals("20")){
			iv_type.setImageResource(R.drawable.xian);
		}else if(type.equals("30")){
			iv_type.setImageResource(R.drawable.gou);
		}else if(type.equals("31")){
			iv_type.setImageResource(R.drawable.shu);
		}else if(type.equals("40")||type.equals("73")){
			iv_type.setImageResource(R.drawable.dong);
		}else if(type.equals("41")||type.equals("72")){
			iv_type.setImageResource(R.drawable.jie);
		}else if(type.equals("42")||type.equals("60")||type.equals("61")||type.equals("63")||type.equals("70")){
			iv_type.setImageResource(R.drawable.zhi);
		}else if(type.equals("43")||type.equals("44")||type.equals("45")||type.equals("62")||type.equals("71")||type.equals("102")||type.equals("103")||type.equals("104")){
			iv_type.setImageResource(R.drawable.shou);
		}/*else if(type.equals("104")){
			iv_type.setImageResource(R.drawable.jiang);
		}*/else{
			iv_type.setImageResource(R.drawable.wu);
		}
		
		return convertView;
	}
	
	

}
