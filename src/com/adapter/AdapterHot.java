package com.adapter;

import java.util.ArrayList;
import java.util.List;

import com.caifuline.R;
import com.caifuline.R.color;
import com.ceb.activity.ActCebDetail;
import com.model.ProductList_item;
import com.view.CircularProgressDrawable;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdapterHot extends BaseAdapter {
	private Context mContext;
	
	int count;
	Animator currentAnimation;
	private Handler mHandler;
	List<ProductList_item> mData;
//	TextView tv_progress;
	public AdapterHot(Context c ,List<ProductList_item> data){
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
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final String title = mData.get(position).title;
		final String yields = mData.get(position).yields;
		final String borrowSatus = mData.get(position).borrowSatus;
		final String productID = mData.get(position).productID;
		final String daynum = mData.get(position).daynum;
		final String minInvestment = (String) mData.get(position).minInvestment;
		final String progress = (String) mData.get(position).progress;
		final String insurancesState = mData.get(position).insurancesState;
		final String mortgageState = mData.get(position).mortgageState;
		final String explain = (String) mData.get(position).explain;
		final String description = (String) mData.get(position).description;
		final String type = (String) mData.get(position).type;
		final String awardState = (String) mData.get(position).awardState;
		final String freeMoney = (String) mData.get(position).freeMoney;
		final String awardStr = (String) mData.get(position).awardStr;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.hot_item, null);
		}
		
		
		final TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
		final TextView tv_yields = (TextView) convertView.findViewById(R.id.tv_yields);
		final TextView tv_daynum = (TextView) convertView.findViewById(R.id.tv_daynum);
		final TextView tv_minInvestment = (TextView) convertView.findViewById(R.id.tv_minInvestment);
		final TextView tv_progress = (TextView) convertView.findViewById(R.id.tv_progres);
		final TextView tv_bai = (TextView) convertView.findViewById(R.id.tv_bai);
		final TextView tv_explain = (TextView) convertView.findViewById(R.id.tv_explain);
		final TextView tv_description = (TextView) convertView.findViewById(R.id.tv_description);
		ImageView iv_new = (ImageView) convertView.findViewById(R.id.iv_new);
		ImageView iv_insurancesState = (ImageView) convertView.findViewById(R.id.iv_insurancesState);
		ImageView iv_jiahao = (ImageView) convertView.findViewById(R.id.iv_jiahao);
		ImageView iv_mortgageState = (ImageView) convertView.findViewById(R.id.iv_mortgageState);
		TextView iv_jiang = (TextView) convertView.findViewById(R.id.iv_jiang);
		
		tv_minInvestment.setText(minInvestment.split("\\.")[0]+"元起");
//		tv_minInvestment.setText(minInvestment+"元起");
		
		if(insurancesState.equals("0")){
			iv_insurancesState.setVisibility(View.VISIBLE);
		}else{
			iv_insurancesState.setVisibility(View.GONE);
		}
		
		if(mortgageState.equals("0")){
			iv_mortgageState.setVisibility(View.VISIBLE);
		}else{
			iv_mortgageState.setVisibility(View.GONE);
		}
		if(insurancesState.equals("0") && mortgageState.equals("0")){
			iv_insurancesState.setVisibility(View.VISIBLE);
			iv_mortgageState.setVisibility(View.VISIBLE);
			iv_jiahao.setVisibility(View.VISIBLE);
		}else{
			iv_jiahao.setVisibility(View.GONE);
		}
		
		if(type.equals("1")){//新手标
			iv_new.setVisibility(View.VISIBLE);
			iv_new.setImageResource(R.drawable.xinshou);
			iv_jiang.setVisibility(View.INVISIBLE);
			tv_daynum.setText(daynum+"天");
		}else  if(type.equals("2")){//体验标
			iv_new.setVisibility(View.VISIBLE);
			iv_new.setImageResource(R.drawable.yiyanbiao);
			iv_jiang.setVisibility(View.INVISIBLE);
			try {
				tv_minInvestment.setText(freeMoney+"元");
				tv_daynum.setText(daynum+"天");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(type.equals("0")){//正常标
			tv_daynum.setText(Integer.parseInt(daynum)/30+"个月");
			if(awardState.equals("1")){//奖励标
				iv_new.setVisibility(View.INVISIBLE);
			//	iv_new.setImageResource(R.drawable.jiang);
				iv_jiang.setText("+"+awardStr+"%奖励");
				iv_jiang.setVisibility(View.VISIBLE);
				
			}else{
		//		iv_new.setVisibility(View.INVISIBLE);
				iv_new.setVisibility(View.INVISIBLE);
				iv_jiang.setVisibility(View.INVISIBLE);
			}
		}
		
		final CircularProgressDrawable drawable,drawablegray;
		drawable = new CircularProgressDrawable.Builder()
        .setRingWidth(mContext.getResources().getDimensionPixelSize(R.dimen.drawable_ring_size_small))
        .setOutlineColor(mContext.getResources().getColor(R.color.lightgrey))
        .setRingColor(mContext.getResources().getColor(android.R.color.holo_red_light))
        .setCenterColor(mContext.getResources().getColor(android.R.color.transparent))
        .create();
		LinearLayout ivDrawable = (LinearLayout) convertView.findViewById(R.id.iv_drawable);;
		
		drawablegray = new CircularProgressDrawable.Builder()
        .setRingWidth(mContext.getResources().getDimensionPixelSize(R.dimen.drawable_ring_size_small))
        .setOutlineColor(mContext.getResources().getColor(R.color.lightgrey))
        .setRingColor(mContext.getResources().getColor(R.color.lightgrey))
        .setCenterColor(mContext.getResources().getColor(android.R.color.transparent))
        .create();
		
		
		
		
		currentAnimation = prepareStyle2Animation(drawable ,Integer.parseInt(progress));
		 
		 currentAnimation.start();
		 
		ivDrawable.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 if (currentAnimation != null) {
		                currentAnimation.cancel();
		            }
				 currentAnimation = prepareStyle2Animation(drawable,Integer.parseInt(progress));
				 currentAnimation.start();
			}
		});
		
		
		tv_title.setText(title);
		tv_yields.setText(yields+"%");
//		tv_daynum.setText(daynum+"天");
//		tv_minInvestment.setText(minInvestment.split("\\.")[0]+"元起");
		if(borrowSatus.equals("21")){//筹备 不可投资
			tv_bai.setVisibility(View.GONE);
			tv_progress.setTextColor(Color.GRAY);
			tv_progress.setTextSize(8);
			tv_progress.setText("筹备中");
			ivDrawable.setBackgroundDrawable(drawablegray);
		}else if(borrowSatus.equals("30")){
			tv_bai.setVisibility(View.GONE);
			tv_progress.setTextColor(Color.GRAY);
			tv_progress.setTextSize(8);
			tv_progress.setText("满额复审中");
			ivDrawable.setBackgroundDrawable(drawablegray);
		}else if(borrowSatus.equals("32")){
			tv_bai.setVisibility(View.GONE);
			tv_progress.setTextColor(Color.GRAY);
			tv_progress.setTextSize(8);
			tv_progress.setText("还款中");
			ivDrawable.setBackgroundDrawable(drawablegray);
		}else if(borrowSatus.equals("41")){
			tv_bai.setVisibility(View.GONE);
			tv_progress.setTextColor(Color.GRAY);
			tv_progress.setTextSize(8);
			tv_progress.setText("还款完成");
			ivDrawable.setBackgroundDrawable(drawablegray);
		}else{
			tv_progress.setTextColor(mContext.getResources().getColor(color.zhusediao));
			tv_bai.setVisibility(View.VISIBLE);
			tv_progress.setTextSize(14);
			tv_progress.setText(progress);
			ivDrawable.setBackgroundDrawable(drawable);
		}
		
		
		
		
		
//		tv_explain.setText(explain);
		tv_description.setText(description);
		
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					break;
				case 0x01:
//					tv_progress.setText(count+"");
					break;
				default:
					break;
				}
			}
		};
		
		
		LinearLayout ll_hot = (LinearLayout) convertView.findViewById(R.id.ll_hot);
		ll_hot.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent in = new Intent(mContext,ActCebDetail.class);
				in.putExtra("productID", productID);
				in.putExtra("borrowSatus", borrowSatus);
				in.putExtra("type", type);///awardState
				in.putExtra("awardState", awardState);
				mContext.startActivity(in);
				((Activity) mContext).overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			}
		});
		
		return convertView;
	}
	
	 /**
     * Style 2 animation will fill the outer ring while applying a color effect from red to green
     *
     * @return Animation
     */
    private Animator prepareStyle2Animation(Object c , int progress) {
        AnimatorSet animation = new AnimatorSet();

        ObjectAnimator progressAnimation = ObjectAnimator.ofFloat(c, CircularProgressDrawable.PROGRESS_PROPERTY,
                0f, progress*0.01f);
        progressAnimation.setDuration(1200);
        progressAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator colorAnimator = ObjectAnimator.ofInt(c, CircularProgressDrawable.RING_COLOR_PROPERTY,
        		mContext.getResources().getColor(android.R.color.holo_red_light));
        colorAnimator.setEvaluator(new ArgbEvaluator());
        colorAnimator.setDuration(1600);
        animation.playTogether(progressAnimation, colorAnimator);
        return animation;
    }

}
