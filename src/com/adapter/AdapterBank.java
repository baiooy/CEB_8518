package com.adapter;



import com.caifuline.R;
import com.ceb.activity.ActContract;
import com.ceb.activity.ActRepaymentSchedule;
import com.model.BankCardList;
import com.view.CircularProgressDrawable;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class AdapterBank extends BaseAdapter {
	private Context mContext;
	BankCardList mData;
	RadioButton rb;
	Animator currentAnimation;
	@SuppressWarnings("deprecation")
	Gallery mGallery;
	static public ImageView iv_prev,iv_next;
    static public String bankID,withdrawbank;
	public AdapterBank(Context c,BankCardList b,Gallery gg,RadioButton radiobutton){
		mContext = c;
		mData = b;
		mGallery = gg;
		rb = radiobutton;
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
		final String bankname = mData.item.get(position).bankname;
		final String bankCard = mData.item.get(position).bankCard;
		final String bankIcon = mData.item.get(position).bankIcon;
		final String bankState = mData.item.get(position).bankState;
//		final String bankID = mData.item.get(position).bankID;
		bankID = mData.item.get(position).bankID;
		withdrawbank = mData.item.get(position).withdrawbank;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.tixianbankcard_item, null);
		}
		
		ImageView iv_bankicon = (ImageView) convertView.findViewById(R.id.iv_bankicon);
		TextView tv_bankname = (TextView) convertView.findViewById(R.id.tv_bankname);
		TextView tv_bankcard = (TextView) convertView.findViewById(R.id.tv_bankcard);
		
		tv_bankname.setText(bankname);
		tv_bankcard.setText(bankCard.substring(0, 4)+" **** **** "+bankCard.substring(bankCard.length()-4));
		
		if(bankID.equalsIgnoreCase("CIB")){
			iv_bankicon.setImageResource(R.drawable.xingye);
		}else if(bankID.equalsIgnoreCase("ABC")){
			iv_bankicon.setImageResource(R.drawable.nongye);
		}else if(bankID.equalsIgnoreCase("CCB")){
			iv_bankicon.setImageResource(R.drawable.jianshe);
		}else if(bankID.equalsIgnoreCase("CMB")){
			iv_bankicon.setImageResource(R.drawable.zhaoshang);
		}else if(bankID.equalsIgnoreCase("BOB")){
			iv_bankicon.setImageResource(R.drawable.beijing);
		}else if(bankID.equalsIgnoreCase("BRCB")){
			iv_bankicon.setImageResource(R.drawable.beijingnongcun);
		}else if(bankID.equalsIgnoreCase("CBHB")){
			iv_bankicon.setImageResource(R.drawable.bohai);
		}else if(bankID.equalsIgnoreCase("BEA")){
			iv_bankicon.setImageResource(R.drawable.dongya);
		}else if(bankID.equalsIgnoreCase("CEB")){
			iv_bankicon.setImageResource(R.drawable.guangda);
		}else if(bankID.equalsIgnoreCase("GDB")){
			iv_bankicon.setImageResource(R.drawable.guangfa);
		}else if(bankID.equalsIgnoreCase("HZCB")){
			iv_bankicon.setImageResource(R.drawable.hangzhou);
		}else if(bankID.equalsIgnoreCase("HXBJ")){
			iv_bankicon.setImageResource(R.drawable.huaxia);
		}else if(bankID.equalsIgnoreCase("BCM")){
			iv_bankicon.setImageResource(R.drawable.jiaotong);
		}else if(bankID.equalsIgnoreCase("CMBC")){
			iv_bankicon.setImageResource(R.drawable.minsheng);
		}else if(bankID.equalsIgnoreCase("NJCB")){
			iv_bankicon.setImageResource(R.drawable.nanjing);
		}else if(bankID.equalsIgnoreCase("PAB")){
			iv_bankicon.setImageResource(R.drawable.pingan);
		}else if(bankID.equalsIgnoreCase("SPDB")){
			iv_bankicon.setImageResource(R.drawable.pufa);
		}else if(bankID.equalsIgnoreCase("SHRCB")){
			iv_bankicon.setImageResource(R.drawable.shanghainongcun);
		}else if(bankID.equalsIgnoreCase("BKSH")){
			iv_bankicon.setImageResource(R.drawable.shanghai);
		}else if(bankID.equalsIgnoreCase("DESZ")){
			iv_bankicon.setImageResource(R.drawable.shenzhen);
		}else if(bankID.equalsIgnoreCase("PSBC")){
			iv_bankicon.setImageResource(R.drawable.youzheng);
		}else if(bankID.equalsIgnoreCase("CZSB")){
			iv_bankicon.setImageResource(R.drawable.zheshang);
		}else if(bankID.equalsIgnoreCase("BOC")){
			iv_bankicon.setImageResource(R.drawable.zhongguo);
		}else if(bankID.equalsIgnoreCase("CTIB")){
			iv_bankicon.setImageResource(R.drawable.zhongxin);
		}else if(bankID.equalsIgnoreCase("ICBC")){
			iv_bankicon.setImageResource(R.drawable.gongshang);
		}else{
			iv_bankicon.setImageResource(R.drawable.zanwu);
		}
		Log.i("getView bankID--withdrawbank",bankID+"---"+ withdrawbank);
		LinearLayout ll_ll = (LinearLayout) convertView.findViewById(R.id.ll_ll);
		
		
		WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		LayoutParams params = ll_ll.getLayoutParams();
		params.width = dm.widthPixels;
		ll_ll.setLayoutParams(params);
		
		iv_prev = (ImageView) convertView.findViewById(R.id.iv_prev);
		iv_next = (ImageView) convertView.findViewById(R.id.iv_next);
		
		if(mGallery.getCount() == 1){
			iv_prev.setVisibility(View.INVISIBLE);
			iv_next.setVisibility(View.INVISIBLE);
		}else{
			iv_prev.setVisibility(View.INVISIBLE);
			iv_next.setVisibility(View.VISIBLE);
			iv_next.setOnClickListener(NextLister);
		}
		
		
		Log.i("mGallery.getCount()", "mGallery.getCount()="+mGallery.getCount());
		mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				rb.setChecked(true);
				if(mGallery.getCount() == 1){
					iv_prev.setVisibility(View.INVISIBLE);
					iv_next.setVisibility(View.INVISIBLE);
				}else if(position == 0){
					iv_prev.setVisibility(View.INVISIBLE);
					iv_next.setVisibility(View.VISIBLE);
					iv_next.setOnClickListener(NextLister);
				}else if(position != 0 && position != mGallery.getCount()-1){
					iv_prev.setVisibility(View.VISIBLE);
					iv_next.setVisibility(View.VISIBLE);
					iv_prev.setOnClickListener(PrevLister);
					iv_next.setOnClickListener(NextLister);
				}else if(position == mGallery.getCount()-1){
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
	
	public OnClickListener PrevLister = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
//			int index = mGallery.getSelectedItemPosition();
//			mGallery.setSelection(--index);
			mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
			
		}
	};
	
	public OnClickListener NextLister = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
//			int index = mGallery.getSelectedItemPosition();
//			mGallery.setSelection(++index);
			mGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
			
		}
	};

}
