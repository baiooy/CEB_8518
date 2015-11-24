package com.ceb.fragment;

import java.net.URLEncoder;
import java.util.TreeMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.caifuline.R;
import com.ceb.activity.ActAboutUs;
import com.ceb.activity.ActAccountBalance;
import com.ceb.activity.ActAccountCenter;
import com.ceb.activity.ActCebDetail;
import com.ceb.activity.ActFeedBack;
import com.ceb.activity.ActHotActivity;
import com.ceb.activity.ActHuifu;
import com.ceb.activity.ActInvestHistory;
import com.ceb.activity.ActMyReward;
import com.ceb.activity.ActRecharge;
import com.ceb.activity.ActSetting;
import com.ceb.activity.ActShareToFriends;
import com.ceb.activity.ActSyatemMsg;
import com.ceb.activity.ActTixian;
import com.ceb.activity.ActTradeDetail;
import com.ceb.activity.ActUserYanzheng;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.Tools;
import com.ceb.widge.UISwitchButton;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.loadingdialog.LoadingDialog;
import com.model.ActionResultInfo;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;
import com.view.BadgeView;
import com.view.BgView;
import com.view.RoundImageView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

@SuppressLint("HandlerLeak")
public class FragmentMy extends Fragment implements OnClickListener{
	String mFlag = "";
	LinearLayout ll_login, ll_unlogin, ll_yue,ll_center;
	RelativeLayout rl_touzijilu, rl_tradedetail, rl_myreward;
	RelativeLayout rl_feedback, rl_about, rl_contactus;
	
	Button btn_chongzhi, btn_tixian;
	TextView tv_register, tv_login,tv_title,tv_msg,tv_content,tv_totalAssets;
	private SharedPreferences islogin;
	private boolean login = false;
	private TextView tv_cumulativeRevenue, tv_availableBalance,
			tv_collectRevenue,tv_msgg;
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	private Handler _handler;
	private String userAccountType;
	private String cardType;
	private String loginname;
	private boolean isopen,iskaika;
	BadgeView badge;
	LinearLayout ll_share;
	RoundImageView account_center;
	PullToRefreshScrollView mPullRefreshScrollView;
	private Boolean ispartner = false;
	ImageView iv_next;
	private String pid,title ,content,outerContent, imgurl,htmlurl;
	UISwitchButton switch_guide2;
	private Boolean canRefresh = true;
	public static Handler mHandler;
	private SharedPreferences isNewUser;
	private Boolean isNew = true; //默认开启
	private int megTotal;
	public static ImageView iv_guide_my;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		sp = getActivity().getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		
		isNewUser = getActivity().getSharedPreferences("isNewUser", 0);
		isNew = isNewUser.getBoolean("isNewUser", true);
		
		_userInfoService = new UserInfoService(getActivity());
		
		_handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(getActivity(),
							"网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
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
		
		return inflater.inflate(R.layout.act_my, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initView();

		setListener();
		if (isNewUser != null & isNew ) {
			Tools.setGuidImage(getActivity(), R.id.ll_my, R.drawable.guide_zichan,
					"FirstLogin","");
		}
		
		mPullRefreshScrollView = (PullToRefreshScrollView) getActivity().findViewById(R.id.refresh_scrollview);
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if(canRefresh){
					canRefresh = false;
				try {
					personalcenter();
					isPartner();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				if(mPullRefreshScrollView != null){
					mPullRefreshScrollView.onRefreshComplete();
				}
			}
			}
		});
		super.onActivityCreated(savedInstanceState);
	}

	private void initView() {
		
		iv_next = (ImageView) getActivity().findViewById(R.id.iv_next);
		rl_touzijilu = (RelativeLayout) getActivity().findViewById(
				R.id.rl_touzijilu);
		rl_myreward = (RelativeLayout) getActivity().findViewById(
				R.id.rl_myreward);
		tv_msg = (TextView) getActivity().findViewById(
				R.id.tv_msg);
		ll_center = (LinearLayout) getActivity().findViewById(
				R.id.ll_center);
		tv_msgg = (TextView) getActivity().findViewById(
				R.id.tv_msgg);
		rl_tradedetail = (RelativeLayout) getActivity().findViewById(
				R.id.rl_tradedetail);
		tv_content = (TextView) getActivity().findViewById(
				R.id.tv_content);
		tv_totalAssets = (TextView) getActivity().findViewById(
				R.id.tv_totalAssets);
		ll_share =  (LinearLayout) getActivity().findViewById(
				R.id.ll_share);
//		rl_zhuanrang = (RelativeLayout) getActivity().findViewById(
//				R.id.rl_zhuanrang);
		rl_feedback = (RelativeLayout) getActivity().findViewById(
				R.id.rl_feedback);
		rl_about = (RelativeLayout) getActivity().findViewById(R.id.rl_about);
		rl_contactus = (RelativeLayout) getActivity().findViewById(
				R.id.rl_contactus);
//		rl_message = (RelativeLayout) getActivity().findViewById(
//				R.id.rl_message);
		badge = new BadgeView(getActivity(), ll_center);
//		rl_hotactivity = (RelativeLayout) getActivity().findViewById(
//				R.id.rl_hotactivity);
		
		iv_guide_my = (ImageView) getActivity().findViewById(R.id.iv_guide_my);
		
		if(getActivity().getSharedPreferences("isN", 0).getBoolean("isN", true)){
			iv_guide_my.setVisibility(View.VISIBLE);
		}else{
			iv_guide_my.setVisibility(View.GONE);
		}
		ll_login = (LinearLayout) getActivity().findViewById(R.id.ll_login);
		ll_unlogin = (LinearLayout) getActivity().findViewById(R.id.ll_unlogin);
		ll_yue = (LinearLayout) getActivity().findViewById(R.id.ll_yue);

		tv_cumulativeRevenue = (TextView) getActivity().findViewById(
				R.id.tv_cumulativeRevenue);
		tv_availableBalance = (TextView) getActivity().findViewById(
				R.id.tv_availableBalance);
		tv_collectRevenue = (TextView) getActivity().findViewById(
				R.id.tv_collectRevenue);

		btn_chongzhi = (Button) getActivity().findViewById(R.id.btn_chongzhi);
		btn_tixian = (Button) getActivity().findViewById(R.id.btn_tixian);
		account_center = (RoundImageView) getActivity().findViewById(R.id.account_center);
		badge = new BadgeView(getActivity(), ll_center);
		badge.setText("26");
		islogin = getActivity().getSharedPreferences("isLogin", 0);
		login = islogin.getBoolean("islogin", false);
		Log.i("initView() login?", login + "");
//		
		tv_register = (TextView) getActivity().findViewById(R.id.tv_register);
		tv_login = (TextView) getActivity().findViewById(R.id.tv_login);
		tv_title = (TextView) getActivity().findViewById(R.id.tv_title);
		
//		rl_set = (RelativeLayout) getActivity().findViewById(R.id.rl_set);
		switch_guide2 = (UISwitchButton) getActivity().findViewById(R.id.switch_guide2);
		mHandler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 8518:
					if(iv_guide_my.getVisibility() != View.VISIBLE){
						iv_guide_my.setVisibility(View.VISIBLE);
					}
					break;
				case 85188:
					if(iv_guide_my.getVisibility() == View.VISIBLE){
							iv_guide_my.setVisibility(View.GONE);
						}
					break;
				default:
					break;
				}
			};
		};
		switch_guide2.setOnCheckedChangeListener(new OnCheckedChangeListener() {           
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
            	if(!isChecked)//第一个开关关闭
            	{
//            		Tools.setGuidImage(getActivity(), R.id.ll_my, R.drawable.guide_zichan,
//            				"FirstLogin");
            		
            	}
            	else//第一个开关打开
            	{
//            		Tools.setGuidImage(getActivity(), R.id.ll_my, R.drawable.guide_zichan,
//            				"FirstLogin");
            		
            	}
            }
        });
		
	}

	
	
	private void setListener() {
		tv_register.setOnClickListener(this);
		tv_login.setOnClickListener(this);
		ll_yue.setOnClickListener(this);
		btn_chongzhi.setOnClickListener(this);
		btn_tixian.setOnClickListener(this);
		rl_touzijilu.setOnClickListener(this);
		rl_tradedetail.setOnClickListener(this);
//		ll_share.setOnClickListener(this);
//		rl_zhuanrang.setOnClickListener(this);
//		rl_set.setOnClickListener(this);
		rl_feedback.setOnClickListener(this);
		rl_about.setOnClickListener(this);
		rl_contactus.setOnClickListener(this);
//		rl_message.setOnClickListener(this);
//		rl_hotactivity.setOnClickListener(this);
		account_center.setOnClickListener(this);
		rl_myreward.setOnClickListener(this);
		iv_guide_my.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		sp = getActivity().getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		
		islogin = getActivity().getSharedPreferences("isLogin", 0);
		login = islogin.getBoolean("islogin", false);
		Log.i("FragmentMy _onResume() login?", login + "");

		if (!login) {
			ll_unlogin.setVisibility(View.VISIBLE);
			ll_login.setVisibility(View.GONE);
//			ll_unlogin.setVisibility(View.GONE);
//			ll_login.setVisibility(View.GONE);
//			Intent intent = new Intent(getActivity(), ActUserYanzheng.class);
//			startActivity(intent);
//			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
		} else {
			ll_login.setVisibility(View.VISIBLE);
			ll_unlogin.setVisibility(View.GONE);
			try {
				personalcenter();
				isPartner();
				share();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_login:
			// Intent intent = new Intent(getActivity(),ActLogin.class);
			// startActivityForResult(intent, 0x01);
//		showShare();
			Intent intent = new Intent(getActivity(), ActUserYanzheng.class);
			startActivity(intent);
			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.tv_register:
			// startActivity(new Intent(getActivity(),ActRegister.class));
			Intent ins = new Intent(getActivity(), ActUserYanzheng.class);
			startActivity(ins);
			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.ll_yue:
			startActivity(new Intent(getActivity(), ActAccountBalance.class));
			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.btn_chongzhi:
			if(!isopen){
//				new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
//				.setContentText("您还未开通汇付天下账户，快去开通吧!")
//				.show();
				 new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
	             .setContentText("您还未开通汇付天下账户，快去开通吧!")
	             .setCancelText("取消")
	             .setConfirmText("去开通")
	             .showCancelButton(true)
	             .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
	                 @Override
	                 public void onClick(SweetAlertDialog sDialog) {
	                    sDialog.dismiss();
	                 }
	             })
	             .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
	                 @Override
	                 public void onClick(SweetAlertDialog sDialog) {
	                	 Intent in = new Intent(getActivity(), ActHuifu.class);
	         			startActivity(in);
	         			getActivity().overridePendingTransition(android.R.anim.fade_in,
	         					android.R.anim.fade_out);
	                 	sDialog.dismiss();
	                 }
	             })
	             .show();
			}/*else if(!iskaika){
				new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
				.setTitleText("Oops...").setContentText("您还未绑定银行卡!")
				.show();
			}*/else{
				Intent insi = new Intent(getActivity(), ActRecharge.class);
				insi.putExtra("money", tv_availableBalance.getText().toString());
				startActivity(insi);
				getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			}
			break;
		case R.id.btn_tixian:
			if(!isopen){
				new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
				.setContentText("您还未开通汇付天下账户，快去开通吧!")
				.show();
			}else if(!iskaika){
				new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
				.setContentText("您还未绑定银行卡!")
				.show();
			}else{
				startActivity(new Intent(getActivity(), ActTixian.class));
				getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			}
			break;
		case R.id.rl_touzijilu:
			startActivity(new Intent(getActivity(), ActInvestHistory.class));
			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.rl_tradedetail:
			startActivity(new Intent(getActivity(), ActTradeDetail.class));
			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
//		case R.id.rl_zhuanrang:
//			startActivity(new Intent(getActivity(), ActTransferProduct.class));
//			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
//			break;
//		case R.id.rl_set:
//			Intent ini = new Intent(getActivity(), ActSetting.class);
//			ini.putExtra("username", loginname);
//			ini.putExtra("userAccountType", userAccountType);
//			ini.putExtra("cardType", cardType);
//			startActivity(ini);
//			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
//			break;
		case R.id.rl_feedback:
			startActivity(new Intent(getActivity(), ActFeedBack.class));
			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.rl_about:
			startActivity(new Intent(getActivity(), ActAboutUs.class));
			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.rl_contactus:
			 new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
			 .setTitleText("400-860-8518")
             .setContentText("服务时间：工作日9:00-21:00")
             .setCustomImage(R.drawable.ceb_icon)
             .setCancelText("取消")
             .setConfirmText("呼叫")
             .showCancelButton(true)
             .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                 @Override
                 public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismiss();
                 }
             })
             .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                 @Override
                 public void onClick(SweetAlertDialog sDialog) {
                	 String url = "tel:" + "4008608518";
						Intent in = new Intent(Intent.ACTION_CALL, Uri.parse(url));
						startActivity(in);
						getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                 	sDialog.dismiss();
                 }
             })
             .show();
			
			break;
//		case R.id.rl_message:
//			startActivity(new Intent(getActivity(), ActMessage.class));
//			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
//			break;
//		case R.id.rl_hotactivity:
//			startActivity(new Intent(getActivity(), ActHotActivity.class));
//			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
//			break;
//		
		case R.id.rl_myreward:
			Intent tt = new Intent(getActivity(), ActMyReward.class);
			tt.putExtra("ispartner", ispartner);
			startActivity(tt);
			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.account_center:
			Intent ini = new Intent(getActivity(), ActAccountCenter.class);
			ini.putExtra("username", loginname);
			ini.putExtra("userAccountType", userAccountType);
			ini.putExtra("cardType", cardType);//megTotal
			ini.putExtra("megTotal", megTotal);
			startActivity(ini);
			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.ll_share:
			Intent ii = new Intent(getActivity(), ActShareToFriends.class);
			
			ii.putExtra("title", title);
			ii.putExtra("content", content);
			ii.putExtra("imgurl", imgurl);
			ii.putExtra("htmlurl", htmlurl);
			ii.putExtra("outerContent", outerContent);
			
			
			startActivity(ii);
			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			
			break;
		case R.id.iv_guide_my:
			if(iv_guide_my.getVisibility() == View.VISIBLE){
				iv_guide_my.setVisibility(View.GONE);
				FragmentMore._handler.sendEmptyMessage(1122);
			}
		}
	}
	
	
	void share(){
		sp = getActivity().getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		String  signature = "sharefriend.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.type = "1";
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(getActivity());
		
		if(SppaConstant.isNetworkAvailable(getActivity())){
			new ShareTask().execute(userInfo);
		}
	}
	
	class ShareTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{


		
		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.getShare(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result == null){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret != null & result.ret.equals("0")){
				pid = result.pid;
				title = result.title;
				content = result.content;
				outerContent = result.outerContent;
				imgurl = result.imgurl;
				htmlurl = result.htmlurl;
//				tv_title.setText(title);
//				tv_content.setText(content);
//				new BgView(ActShareToFriends.this, imgurl, iv_img, "");
				
				Log.i("onPostExecute", title+"///"+content+"///"+imgurl+"///"+htmlurl);
				_handler.sendEmptyMessage(1);
			}else{
				Toast.makeText(getActivity(),result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	
	
	
	void personalcenter(){
		sp = getActivity().getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		String  signature = "center.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(getActivity());
		
		if(SppaConstant.isNetworkAvailable(getActivity())){
			new PersonalcenterTask().execute(userInfo);
		}
		
	}
	
	class PersonalcenterTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{

		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.getPersoncenter(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			canRefresh = true;
			mPullRefreshScrollView.onRefreshComplete();
			if(result == null){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret != null & result.ret.equals("0")){
				Log.i("SppaConstant.isNumeric", SppaConstant.isNumeric(result.cumulativeRevenue)+"");
				if(SppaConstant.isNumeric(result.cumulativeRevenue)){
					tv_cumulativeRevenue.setTextSize(37);
					iv_next.setVisibility(View.VISIBLE);
					ll_share.setClickable(true);
					ll_share.setOnClickListener(FragmentMy.this);
				}else{
					tv_cumulativeRevenue.setTextSize(15);
					iv_next.setVisibility(View.GONE);
					ll_share.setClickable(false);
				}
				tv_cumulativeRevenue.setText(result.cumulativeRevenue);
				
				if(result.content != null){
					tv_content.setVisibility(View.VISIBLE);
					tv_content.setText(result.content);
				}else{
					tv_content.setVisibility(View.GONE);
				}
				tv_totalAssets.setText(result.totalAssets);
				
				if( !result.collectRevenue.equals("")){
					tv_collectRevenue.setText(result.collectRevenue);
				}else{
					tv_collectRevenue.setText("0.00");
				}
				
				if(!result.availableBalance.equals("")){
					tv_availableBalance.setText(result.availableBalance);
				}else{
					tv_availableBalance.setText("0.00");
				}
				
				userAccountType = result.userAccountType;
				cardType = result.cardType;
				loginname = result.username;
		//		int answerNum = Integer.parseInt(result.megTotal);
				Log.i("result.megTotal", "msg"+result.megTotal);
				megTotal = Integer.parseInt(result.megTotal);
				Log.i(".megTotal", "msg"+result.megTotal);
				
				if(!result.megTotal.equals("null") && Integer.parseInt(result.megTotal) > 0){//有新消息，显示消息提示
					if(Integer.parseInt(result.megTotal) > 99){
						badge.setText("99+");
					}else{
						badge.setText(result.megTotal);
					}
					badge.show();
					Log.i(".megTotal", "show"+result.megTotal);
				}else{
					Log.i(".megTotal", "toggle"+result.megTotal);
					if(badge.isShown())
						badge.toggle();
				}
				
//				badge.clearComposingText();
//				badge.clearFocus();
//				tv_title.setText(result.username);
				
				SharedPreferences sp = getActivity().getSharedPreferences("huifukaihu", 0);
				SharedPreferences.Editor editor = sp.edit();
				editor.clear();
				editor.commit();
				if(userAccountType.equals("1")){
					editor.putBoolean("kaihu", true);
					isopen = true;
				}else{
					editor.putBoolean("kaihu", false);
					isopen = false;
				}
				editor.commit();
				
				SharedPreferences kaika = getActivity().getSharedPreferences("kaika", 0);
				SharedPreferences.Editor editor2 = kaika.edit();
				editor2.clear();
				editor2.commit();
				if(cardType.equals("1")){
					editor2.putBoolean("kaika", true);
					iskaika = true;
				}else{
					editor2.putBoolean("kaika", false);
					iskaika = false;
				}
				editor2.commit();
				
				_handler.sendEmptyMessage(0);
			}else{
				Toast.makeText(getActivity(),result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}


	
	
	private void showShare() {
		 ShareSDK.initSDK(getActivity());
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 

		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle(getString(R.string.share));
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl("http://sharesdk.cn");
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("我是分享文本");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl("http://sharesdk.cn");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		 oks.show(getActivity());
		 }
	
	
	void isPartner(){
		sp = getActivity().getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		String  signature = "invitefriend.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(getActivity());
		
		if(SppaConstant.isNetworkAvailable(getActivity())){
			new PartnerTask().execute(userInfo);
		}
		
	}
	
	class PartnerTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{

		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.getIsartner(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result == null){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret != null & result.ret.equals("0")){
				if(result.partnerType.equals("1")){
					ispartner = true;
				}else{
					ispartner = false;
				}
			}else{
				Toast.makeText(getActivity(),result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	


}
