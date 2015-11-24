package com.adapter;



import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.adapter.AdapterDuifu.ContractTask;
import com.caifuline.R;
import com.caifuline.R.color;
import com.ceb.activity.ActCebDetail;
import com.ceb.activity.ActChongzhiHuifu;
import com.ceb.activity.ActContract;
import com.ceb.activity.ActRepaymentSchedule;
import com.ceb.activity.MainActivity;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
import com.model.ActionResultInfo;
import com.model.HuikuanList;
import com.model.Huikuan_item;
import com.model.UserInfo;
import com.model.Yongjin_item;
import com.service.UserInfoService;
import com.spp.SppaConstant;
import com.view.CircularProgressDrawable;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterYongjin extends BaseAdapter {
	private Context mContext;
	Animator currentAnimation;
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	private Handler _handler;
	private String requestUrl;
	List<Yongjin_item> mData;
	private Handler mHandler;
	LoadingDialog dialog;
	OnClickListener lingqu,shiyong;
	public AdapterYongjin(Context c ,List<Yongjin_item> data,Handler handler){
		mContext = c;
//		mData = h;
		mData = data;
		sp = mContext.getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "null");
		_userInfoService = new UserInfoService(mContext);
		mHandler = handler;
		
		_handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(mContext,
							"网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
//					Intent ins = new Intent(Intent.ACTION_VIEW, Uri.parse(requestUrl));
//					mContext.startActivity(ins);
//					((Activity) mContext).overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
					break;
				case 3:
					break;
				case 1:
					break;
				case 2:
					break;
				default:
					break;
				}
				if(dialog!=null && dialog.isShowing())
					dialog.dismiss();
			};
		};
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
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final String id = mData.get(position).id;
		final String title = mData.get(position).title;
		final String awardType = mData.get(position).awardType;
		final String source = mData.get(position).source;
		final String validDate = (String) mData.get(position).validDate;
		final String orderID = (String) mData.get(position).orderID;
		final String state = mData.get(position).state;
		final String money = mData.get(position).money;
		
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.yongjin_item, null);
		}
		FrameLayout fr_state = (FrameLayout) convertView.findViewById(R.id.fr_state);
		LinearLayout ll_state = (LinearLayout) convertView.findViewById(R.id.ll_state);
		ImageView iv_state = (ImageView) convertView.findViewById(R.id.iv_state);
		Button btn_lingqu = (Button) convertView.findViewById(R.id.btn_lingqu);
		TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
		TextView tv_source = (TextView) convertView.findViewById(R.id.tv_source);
		TextView tv_validDate = (TextView) convertView.findViewById(R.id.tv_validDate);
		TextView tv_money = (TextView) convertView.findViewById(R.id.tv_money);
		
//		Button btn_repaymentschedule = (Button) convertView.findViewById(R.id.btn_repaymentschedule);
//		Button btn_contract = (Button) convertView.findViewById(R.id.btn_contract);
		ll_state.setClickable(false);
		if(awardType.equals("1")){
			btn_lingqu.setText("立即使用");
			btn_lingqu.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent ii = new Intent(mContext,MainActivity.class);
					MainActivity.instance.finish();
					ii.putExtra("hot", "hot");
					mContext.startActivity(ii);
					((Activity) mContext).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					
				}
			});
		}else{
			btn_lingqu.setText("立即领取");
			ll_state.setClickable(true);
			ll_state.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(mContext, "fr_state", 1000).show();
					try {
						getLingqu(id);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			});
			btn_lingqu.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						getLingqu(id);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			});
			
		}
		ll_state.setClickable(false);
		tv_title.setText(title);
		tv_source.setText(source);
		tv_validDate.setText(validDate);
		try {
			tv_money.setText(money+" 元");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(state!=null){
			if(state.equals("0")){//0-未领取
				btn_lingqu.setVisibility(View.VISIBLE);
				ll_state.setBackgroundColor(mContext.getResources().getColor(color.zhusediao));
				iv_state.setImageResource(0);
			}else if(state.equals("1")){//1-已领取 
				btn_lingqu.setVisibility(View.GONE);
				ll_state.setBackgroundColor(mContext.getResources().getColor(color.transparent));
				iv_state.setImageResource(R.drawable.yinglingqu);
			}else if(state.equals("2")){//2-已使用
				btn_lingqu.setVisibility(View.GONE);
				ll_state.setBackgroundColor(mContext.getResources().getColor(color.transparent));
				iv_state.setImageResource(R.drawable.yishiyong);
			}else if(state.equals("3")){//3-无效
				btn_lingqu.setVisibility(View.GONE);
				ll_state.setBackgroundColor(mContext.getResources().getColor(color.transparent));
				iv_state.setImageResource(R.drawable.wuxiao);
			}else if(state.equals("4")){//4-已过期
				btn_lingqu.setVisibility(View.GONE);
				ll_state.setBackgroundColor(mContext.getResources().getColor(color.transparent));
				iv_state.setImageResource(R.drawable.yiguoqi);
			}
		}
		
//		
//		LinearLayout ll_title = (LinearLayout) convertView.findViewById(R.id.ll_title);
		lingqu = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					getLingqu(id);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		shiyong = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					Intent ii = new Intent(mContext,MainActivity.class);
					MainActivity.instance.finish();
					ii.putExtra("hot", "hot");
					mContext.startActivity(ii);
			//		(Activity)mContext.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		
		return convertView;
	}
	
	void getLingqu(String awardID){
		String  signature = "awardreceive.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.awardID = awardID;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(mContext);

		new LingquTask().execute(userInfo);
	}
    
	class LingquTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{
		@Override
		protected void onPreExecute() {
			dialog = new LoadingDialog(mContext);
			dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
			dialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.getLingqu(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result == null || result.equals(null)){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0")){
			//	requestUrl = result.url;
				mHandler.sendEmptyMessage(11);
		//		Toast.makeText(mContext,result.msg , Toast.LENGTH_SHORT).show();
				_handler.sendEmptyMessage(0);
			}else{
				_handler.sendEmptyMessage(1);
				Toast.makeText(mContext,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}


}
