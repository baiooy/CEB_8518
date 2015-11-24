package com.adapter;



import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.TreeMap;

import com.caifuline.R;
import com.ceb.activity.ActCebDetail;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.model.ActionResultInfo;
import com.model.HuikuanList;
import com.model.Huikuan_item;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;
import com.view.CircularProgressDrawable;
import com.view.EmptyViewHelper;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterToubiao extends BaseAdapter {
	private Context mContext;
	private ArrayList<Huikuan_item> mData;
	Animator currentAnimation;
	private String userID;
	UserInfoService _userInfoService;
	private Handler mHandler;
	public AdapterToubiao(Context c ,ArrayList<Huikuan_item> h ,String id,Handler handler){
		mContext = c;
		mData = h;
		userID = id;
		mHandler = handler;
		_userInfoService = new UserInfoService(mContext);
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
		final String subtitle = mData.get(position).subtitle;
		final String productId = mData.get(position).productId;
		final String investId = mData.get(position).investId;
		final String date = mData.get(position).date;
		final String investmentAmount = mData.get(position).investmentAmount;
		final String amountRevenue = mData.get(position).amountRevenue;
		final String planType = mData.get(position).planType;
		final String state = mData.get(position).state;
		final String contractsType = (String) mData.get(position).contractsType;
		final String awardState = (String) mData.get(position).awardState;
		final String awardStr = (String) mData.get(position).awardStr;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.toubiao_item, null);
		}
		
		TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
		TextView tv_subtitle = (TextView) convertView.findViewById(R.id.tv_subtitle);
		TextView tv_investmentAmount = (TextView) convertView.findViewById(R.id.tv_investmentAmount);
		TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);
		TextView tv_amountRevenue = (TextView) convertView.findViewById(R.id.tv_amountRevenue);
		Button btn_thaw = (Button) convertView.findViewById(R.id.btn_thaw);
//		Button btn_repaymentschedule = (Button) convertView.findViewById(R.id.btn_repaymentschedule);
//		Button btn_contract = (Button) convertView.findViewById(R.id.btn_contract);
		ImageView iv_jiang = (ImageView) convertView.findViewById(R.id.iv_jiang);
		tv_title.setText(title);
//		tv_subtitle.setText("("+subtitle+")");
		tv_investmentAmount.setText(investmentAmount.split("\\.")[0]);
		tv_date.setText(date.substring(0, 10).concat("\\n").concat(date.substring(11)).replace("\\n", "\n"));
		tv_amountRevenue.setText(amountRevenue);
		
		if(awardState != null && awardState.equals("1")){
			tv_subtitle.setVisibility(View.VISIBLE);
			tv_subtitle.setText("+"+awardStr+"%奖励");
//			iv_jiang.setVisibility(View.VISIBLE);
		}else{
			tv_subtitle.setVisibility(View.INVISIBLE);
//			iv_jiang.setVisibility(View.INVISIBLE);
		}
		
		if(state.equals("0")){
			btn_thaw.setVisibility(View.INVISIBLE);
		}else{
			btn_thaw.setVisibility(View.VISIBLE);
			btn_thaw.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					getThaw(investId);
					
				}
			});
		}
		
		RelativeLayout ll_title = (RelativeLayout) convertView.findViewById(R.id.ll_title);
		ll_title.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent in = new Intent(mContext,ActCebDetail.class);
				in.putExtra("productID", productId);
				in.putExtra("borrowSatus", "22");
				in.putExtra("awardState", awardState);
				mContext.startActivity(in);
				((Activity) mContext).overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
				
			}
		});
		
		return convertView;
	}
	
	void getThaw(String productId){
		String  signature = "thaw.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.productID = productId;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(mContext);

		new ToubiaoTask().execute(userInfo);
	}
    
	class ToubiaoTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{

		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.getThaw(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			
			if(result == null || result.equals(null)){
	//			mHandler.sendEmptyMessage(-1);
			}else if(result.ret.equals("null")){
			//	mHandler.sendEmptyMessage(3);
			}else if(result.ret.equals("0")){
				mHandler.sendEmptyMessage(11);
				Toast.makeText(mContext,result.msg , Toast.LENGTH_SHORT).show();
				
			}else{
				Toast.makeText(mContext,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}


}
