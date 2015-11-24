package com.adapter;



import java.util.ArrayList;

import com.caifuline.R;
import com.ceb.activity.ActContract;
import com.ceb.activity.ActRepaymentSchedule;
import com.model.BankCardList;
import com.model.BankCard_item;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdapterBankCard extends BaseAdapter {
	private Context mContext;
	ArrayList<BankCard_item> mData;
	Animator currentAnimation;
	public AdapterBankCard(Context c,ArrayList<BankCard_item> b){
		mContext = c;
		mData = b;
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
		final String bankname = mData.get(position).bankname;
		final String bankCard = mData.get(position).bankCard;
		final String bankID = mData.get(position).bankID;
		final String bankIcon = mData.get(position).bankIcon;
		final String isDefault = mData.get(position).isDefault;
		final String expressFlag = mData.get(position).expressFlag;
		
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.bankcard_item, null);
		}
		
		ImageView iv_bankicon = (ImageView) convertView.findViewById(R.id.iv_bankicon);
		TextView tv_bankname = (TextView) convertView.findViewById(R.id.tv_bankname);
		TextView tv_bankcard = (TextView) convertView.findViewById(R.id.tv_bankcard);
//		CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkBox_default);
		
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
		
//		if(bankState.equals("Y")){
//			checkbox.setChecked(true);
//			checkbox.setTextColor(Color.RED);
//		}else{
//			checkbox.setChecked(false);
//			checkbox.setClickable(false);
//			checkbox.setTextColor(Color.GRAY);
//		}
		
		return convertView;
	}
	
	

}
