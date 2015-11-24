package com.adapter;



import com.caifuline.R;
import com.ceb.activity.ActContract;
import com.ceb.activity.ActRepaymentSchedule;
import com.view.CircularProgressDrawable;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
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

public class AdapterCebdetail extends BaseAdapter {
	private Context mContext;
	private Activity activity;
	Animator currentAnimation;
	public AdapterCebdetail(Context c){
		mContext = c;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.duifu_item, null);
		}
		Button btn_huankuan = (Button) convertView.findViewById(R.id.btn_huankuan);
		Button btn_contractt = (Button) convertView.findViewById(R.id.btn_contractt);
		btn_huankuan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent in = new Intent(mContext,ActRepaymentSchedule.class);
				mContext.startActivity(in);
			}
		});
		
		btn_contractt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent in = new Intent(mContext,ActContract.class);
				mContext.startActivity(in);
			}
		});
		
		return convertView;
	}
	
	

}
