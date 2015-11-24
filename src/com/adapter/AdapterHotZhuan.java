package com.adapter;



import com.caifuline.R;
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
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AdapterHotZhuan extends BaseAdapter {
	private Context mContext;
	
	Animator currentAnimation;
	public AdapterHotZhuan(Context c){
		mContext = c;
		
		 
		 
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 6;
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
					.inflate(R.layout.hot_zhuanrang_item, null);
		}
		final CircularProgressDrawable drawable;
		drawable = new CircularProgressDrawable.Builder()
        .setRingWidth(mContext.getResources().getDimensionPixelSize(R.dimen.drawable_ring_size))
        .setOutlineColor(mContext.getResources().getColor(android.R.color.darker_gray))
        .setRingColor(mContext.getResources().getColor(android.R.color.holo_green_light))
        .setCenterColor(mContext.getResources().getColor(android.R.color.transparent))
        .create();
		
		
		
		currentAnimation = prepareStyle2Animation(drawable);
		 
		 currentAnimation.start();
		
		
		return convertView;
	}
	
	 /**
     * Style 2 animation will fill the outer ring while applying a color effect from red to green
     *
     * @return Animation
     */
    private Animator prepareStyle2Animation(Object c) {
        AnimatorSet animation = new AnimatorSet();

        ObjectAnimator progressAnimation = ObjectAnimator.ofFloat(c, CircularProgressDrawable.PROGRESS_PROPERTY,
                0f, 0.75f);
        progressAnimation.setDuration(1200);
        progressAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator colorAnimator = ObjectAnimator.ofInt(c, CircularProgressDrawable.RING_COLOR_PROPERTY,
        		mContext.getResources().getColor(android.R.color.holo_red_dark),
        		mContext.getResources().getColor(android.R.color.holo_green_light));
        colorAnimator.setEvaluator(new ArgbEvaluator());
        colorAnimator.setDuration(1600);
        animation.playTogether(progressAnimation, colorAnimator);
        return animation;
    }

}
