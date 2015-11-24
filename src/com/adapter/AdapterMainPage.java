package com.adapter;

import com.caifuline.R;
import com.caifuline.R.color;
import com.ceb.activity.ActCebDetail;
import com.ceb.activity.ActContract;
import com.ceb.activity.ActRepaymentSchedule;
import com.ceb.activity.ActUserYanzheng;
import com.model.BankCardList;
import com.model.MainArray;
import com.view.CircularProgressDrawable;
import com.view.UGallery;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class AdapterMainPage extends BaseAdapter {
	private Context mContext;
	MainArray mData;
	Animator currentAnimation;
	@SuppressWarnings("deprecation")
	UGallery mGallery;
	private ImageView iv_prev, iv_next;
	private SharedPreferences isLogin;
	private Boolean islogin = false;
	CircularProgressDrawable drawable;

	public AdapterMainPage(Context c, MainArray b, UGallery gg) {
		mContext = c;
		mData = b;
		mGallery = gg;

		isLogin = mContext.getSharedPreferences("isLogin", 0);
		islogin = isLogin.getBoolean("islogin", false);
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
		return mData.item.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final String title = mData.item.get(position).title;
		final String productID = mData.item.get(position).productID;
		final String subtitle = mData.item.get(position).subtitle;
		final String minInvestment = mData.item.get(position).minInvestment;
		final String progress = mData.item.get(position).progress;
		final String extraIncome = mData.item.get(position).extraIncome;
		final String deadline = mData.item.get(position).deadline;
		final String type = mData.item.get(position).type;
		final String borrowStatus = mData.item.get(position).borrowStatus;
		final String awardState = mData.item.get(position).awardState;
		final String awardStr = mData.item.get(position).awardStr;
		int mProgress;
		mProgress = Integer.parseInt(progress);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.main_item, null);
		}

		TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);//tv_jiang
		TextView tv_jiang = (TextView) convertView.findViewById(R.id.tv_jiang);
		TextView tv_extraIncome = (TextView) convertView
				.findViewById(R.id.tv_extraIncome);
		TextView tv_progress = (TextView) convertView
				.findViewById(R.id.tv_progress);
		TextView tv_deadline = (TextView) convertView
				.findViewById(R.id.tv_deadline);
		TextView tv_minInvestment = (TextView) convertView
				.findViewById(R.id.tv_minInvestment);
		Button btn_lijitouzi = (Button) convertView
				.findViewById(R.id.btn_lijitouzi);
		RelativeLayout num1 = (RelativeLayout) convertView
				.findViewById(R.id.num1);
		ImageView iv_xinshou = (ImageView) convertView
				.findViewById(R.id.iv_xinshou);

		if (type.equals("1")) {// 新手标
			iv_xinshou.setVisibility(View.VISIBLE);//
			iv_xinshou.setImageResource(R.drawable.xinshou);
			tv_jiang.setVisibility(View.INVISIBLE);
			
		} else if (type.equals("2")) {// 体验标
			iv_xinshou.setVisibility(View.VISIBLE);
			iv_xinshou.setImageResource(R.drawable.yiyanbiao);
			tv_jiang.setVisibility(View.INVISIBLE);
		} else {
			if (awardState.equals("1")) {
				tv_jiang.setText("+"+awardStr+"%奖励");
				tv_jiang.setVisibility(View.VISIBLE);
			} else {
				tv_jiang.setVisibility(View.INVISIBLE);
			}
			iv_xinshou.setVisibility(View.INVISIBLE);
		}

		tv_title.setText(title);
		tv_extraIncome.setText(extraIncome);
		tv_minInvestment.setText(minInvestment.split("\\.")[0]);
		tv_progress.setText(progress);
		tv_deadline.setText(deadline);

		btn_lijitouzi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (islogin) {// 已登录
					Intent ins = new Intent(mContext, ActCebDetail.class);
					ins.putExtra("productID", productID);
					ins.putExtra("borrowSatus", borrowStatus);
					ins.putExtra("awardState", awardState);
					ins.putExtra("type", type);
					mContext.startActivity(ins);
					((Activity) mContext).overridePendingTransition(
							android.R.anim.fade_in, android.R.anim.fade_out);
				} else {
					Intent in = new Intent(mContext, ActUserYanzheng.class);
					mContext.startActivity(in);
					((Activity) mContext).overridePendingTransition(
							android.R.anim.fade_in, android.R.anim.fade_out);
				}

			}
		});

		tv_title.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (islogin) {// 已登录
					Intent ins = new Intent(mContext, ActCebDetail.class);
					ins.putExtra("productID", productID);
					ins.putExtra("borrowSatus", borrowStatus);
					ins.putExtra("awardState", awardState);
					ins.putExtra("type", type);
					mContext.startActivity(ins);
					((Activity) mContext).overridePendingTransition(
							android.R.anim.fade_in, android.R.anim.fade_out);
				} else {
					Intent in = new Intent(mContext, ActUserYanzheng.class);
					mContext.startActivity(in);
					((Activity) mContext).overridePendingTransition(
							android.R.anim.fade_in, android.R.anim.fade_out);
				}

			}
		});

		Animator currentAnimation;
		LinearLayout ivDrawable;

		ivDrawable = (LinearLayout) convertView.findViewById(R.id.iv_drawable);
		drawable = new CircularProgressDrawable.Builder()
				.setRingWidth(
						mContext.getResources().getDimensionPixelSize(
								R.dimen.drawable_ring_size))
								
				.setOutlineColor(
						mContext.getResources().getColor(R.color.transparent))
				.setRingColor(
						mContext.getResources().getColor(
								android.R.color.holo_red_light))
				.setCenterColor(Color.rgb(245, 233, 233)).create();
		ivDrawable.setBackgroundDrawable(drawable);
		// ivDrawable.setOnClickListener(this);
		currentAnimation = prepareStyle2Animation(Integer.parseInt(progress));

		currentAnimation.start();

		LinearLayout ll_ll = (LinearLayout) convertView
				.findViewById(R.id.ll_ll);

		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		LayoutParams params = ll_ll.getLayoutParams();
		Log.i("", "params==null" + (params == null) + "dm==null" + (dm == null));
		params.width = dm.widthPixels;
		ll_ll.setLayoutParams(params);

		iv_prev = (ImageView) convertView.findViewById(R.id.iv_prev);
		iv_next = (ImageView) convertView.findViewById(R.id.iv_next);

		iv_prev.setOnClickListener(PrevLister);
		iv_next.setOnClickListener(NextLister);

		

		mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				if (mGallery.getCount() == 1) {
			//		Toast.makeText(mContext, "adapter----1", 1000).show();
					iv_prev.setVisibility(View.INVISIBLE);
					iv_next.setVisibility(View.INVISIBLE);
				} else if (mGallery.getSelectedItemPosition() == 0) {
			//		Toast.makeText(mContext, "adapter----2", 1000).show();
					iv_prev.setVisibility(View.INVISIBLE);
					iv_next.setVisibility(View.VISIBLE);
					iv_next.setOnClickListener(NextLister);
				} else if (mGallery.getSelectedItemPosition() != 0 && mGallery.getSelectedItemPosition() != mGallery.getCount() - 1) {
			//		Toast.makeText(mContext, "adapter----3", 1000).show();
					iv_prev.setVisibility(View.VISIBLE);
					iv_next.setVisibility(View.VISIBLE);
					iv_next.setOnClickListener(NextLister);
					iv_prev.setOnClickListener(PrevLister);
				} else if (mGallery.getSelectedItemPosition() == mGallery.getCount() - 1) {
			//		Toast.makeText(mContext, "adapter----4", 1000).show();
					iv_next.setVisibility(View.INVISIBLE);
					iv_prev.setVisibility(View.VISIBLE);
					iv_prev.setOnClickListener(PrevLister);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		

		return convertView;
	}

	/**
	 * Style 2 animation will fill the outer ring while applying a color effect
	 * from red to green
	 * 
	 * @return Animation
	 */
	private Animator prepareStyle2Animation(int progress) {
		AnimatorSet animation = new AnimatorSet();

		ObjectAnimator progressAnimation = ObjectAnimator.ofFloat(drawable,
				CircularProgressDrawable.PROGRESS_PROPERTY, 0f,
				progress * 0.01f);
		progressAnimation.setDuration(1200);
		progressAnimation
				.setInterpolator(new AccelerateDecelerateInterpolator());

		ObjectAnimator colorAnimator = ObjectAnimator.ofInt(drawable,
				CircularProgressDrawable.RING_COLOR_PROPERTY, mContext
						.getResources()
						.getColor(android.R.color.holo_red_light));
		colorAnimator.setEvaluator(new ArgbEvaluator());
		colorAnimator.setDuration(1600);
		// tv_progress.setText((int)(drawable.getCircleScale()*100)+"");
		// tv_progress.setText((int)(drawable.getProgress())+"");
		animation.playTogether(progressAnimation, colorAnimator);

		return animation;
	}

	public OnClickListener PrevLister = new View.OnClickListener() {

		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			// int index = mGallery.getSelectedItemPosition();
			// mGallery.setSelection(--index);
			mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);

		}
	};

	public OnClickListener NextLister = new View.OnClickListener() {

		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			// int index = mGallery.getSelectedItemPosition();
			// mGallery.setSelection(++index);
			mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);

		}
	};

}
