package com.adapter;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.TreeMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.caifuline.R;
import com.ceb.activity.ActCebDetail;
import com.ceb.activity.ActRepaymentSchedule;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.model.ActionResultInfo;
import com.model.Huikuan_item;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterDuifu extends BaseAdapter {
	private Context mContext;
	private ArrayList<Huikuan_item> mData;
	Animator currentAnimation;
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	private Handler _handler;
	private String requestUrl;
//	private String productId,investId;
	public AdapterDuifu(Context c ,ArrayList<Huikuan_item> h){
		mContext = c;
		mData = h;
		sp = mContext.getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "null");
		_userInfoService = new UserInfoService(mContext);
		_handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(mContext,
							"网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
					Intent ins = new Intent(Intent.ACTION_VIEW, Uri.parse(requestUrl));
					mContext.startActivity(ins);
					((Activity) mContext).overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
					break;
				case 3:
					break;
				case 2:
					break;
				default:
					break;
				}
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
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
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
		final String contractsType = (String) mData.get(position).contractsType;
		final String awardState = (String) mData.get(position).awardState;
		final String awardStr = (String) mData.get(position).awardStr;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.duifu_item, null);
		}
		Button btn_huankuan = (Button) convertView.findViewById(R.id.btn_huankuan);
		Button btn_contractt = (Button) convertView.findViewById(R.id.btn_contractt);
		
		TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
		TextView tv_subtitle = (TextView) convertView.findViewById(R.id.tv_subtitle);
		TextView tv_investmentAmount = (TextView) convertView.findViewById(R.id.tv_investmentAmount);
		TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);
		TextView tv_amountRevenue = (TextView) convertView.findViewById(R.id.tv_amountRevenue);
		ImageView iv_jiang = (ImageView) convertView.findViewById(R.id.iv_jiang);
		
		if(awardState != null && awardState.equals("1")){
			iv_jiang.setVisibility(View.INVISIBLE);
			tv_subtitle.setVisibility(View.VISIBLE);
			tv_subtitle.setText("+"+awardStr+"%奖励");
		}else{
			iv_jiang.setVisibility(View.INVISIBLE);
			tv_subtitle.setVisibility(View.INVISIBLE);
		}
		tv_title.setText(title);
//		tv_subtitle.setText("("+subtitle+")");
		tv_investmentAmount.setText(investmentAmount.split("\\.")[0]);
		tv_date.setText(date.substring(0, 10).concat("\\n").concat(date.substring(11)).replace("\\n", "\n"));
		tv_amountRevenue.setText(amountRevenue);
		
		if(planType.equals("0")){
			btn_huankuan.setVisibility(View.GONE);
		}else{
			btn_huankuan.setVisibility(View.VISIBLE);
		}
		
		if(contractsType.equals("0")){
			btn_contractt.setVisibility(View.GONE);
		}else{
			btn_contractt.setVisibility(View.VISIBLE);
		}
		
		btn_huankuan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent in = new Intent(mContext,ActRepaymentSchedule.class);
				in.putExtra("productId", investId);
				mContext.startActivity(in);
				((Activity) mContext).overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			}
		});
		
		btn_contractt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent in = new Intent(mContext,ActContract.class);
//				in.putExtra("productId", productId);
//				in.putExtra("investId", investId);
//				mContext.startActivity(in);
//				((Activity) mContext).overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
				new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setContentText("查看合同需要先下载!是否确定？")
                .setCancelText("取消")
                .setConfirmText("确定")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance, keep widget user state, reset them if you need
//                        sDialog.setTitleText("取消成功!")
//                                .setContentText("您成功取消了本次操作 ！")
//                                .setConfirmText("OK")
//                                .showCancelButton(false)
//                                .setCancelClickListener(null)
//                                .setConfirmClickListener(null)
//                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                        // or you can new a SweetAlertDialog to show
                       sDialog.dismiss();
//                        new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
//                                .setContentText("您成功取消了本次操作 ！")
//                                .setConfirmText("OK")
//                                .show();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.setTitleText("Deleted!")
//                                .setContentText("Your imaginary file has been deleted!")
//                                .setConfirmText("OK")
//                                .showCancelButton(false)
//                                .setCancelClickListener(null)
//                                .setConfirmClickListener(null)
//                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    	sDialog.dismiss();
                    	getContract(investId,productId);
                    }
                })
                .show();
				
				
			}
		});
		
		
		LinearLayout ll_title = (LinearLayout) convertView.findViewById(R.id.ll_title);
		ll_title.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent in = new Intent(mContext,ActCebDetail.class);
				in.putExtra("borrowSatus", "");
				in.putExtra("productID", productId);
				in.putExtra("awardState", awardState);
				mContext.startActivity(in);
				((Activity) mContext).overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
				
			}
		});
		
		return convertView;
	}
	
	void getContract(String i,String p){
		String  signature = "investcontact.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.investId = i;
		userInfo.productID = p;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(mContext);

		new ContractTask().execute(userInfo);
	}
    
	class ContractTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{

		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.getContract(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result == null || result.equals(null)){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0")){
				requestUrl = result.url;
					
				_handler.sendEmptyMessage(0);
			}else{
				Toast.makeText(mContext,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}

}
