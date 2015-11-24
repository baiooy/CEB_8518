package com.ceb.fragment;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.caifuline.R;
import com.ceb.activity.ActAboutUs;
import com.ceb.activity.ActAccountCenter;
import com.ceb.activity.ActBannerDetail;
import com.ceb.activity.ActCommonProblem;
import com.ceb.activity.ActFeedBack;
import com.ceb.activity.ActHotActivity;
import com.ceb.activity.ActSyatemMsg;
import com.ceb.activity.ActYaoqing;
import com.ceb.activity.GestureEditActivity;
import com.ceb.activity.GestureVerifyActivity;
import com.ceb.activity.MainnActivity;
import com.ceb.widge.DataCleanManager;
import com.ceb.widge.Tools;
import com.ceb.widge.UISwitchButton;
import com.model.MainArray;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FragmentMore extends Fragment implements OnClickListener{
	RelativeLayout rl_feedback, rl_about, rl_contactus, rl_message, rl_hotactivity ,rl_update
						,rl_yaoqing ,rl_osmsg,rl_question,rl_cache;
	TextView tv_version,tv_v,tv_cache;
	UISwitchButton switch_guide;
	private SharedPreferences isNewUser;
	private Boolean isNew = true; //默认开启
	public static  ImageView iv_guide_more;
	public static Handler _handler;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_more, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		initView();
		
		setListener();
		
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					tv_v.setVisibility(View.INVISIBLE);
					tv_version.setText("发现新版本！");
					tv_version.setTextColor(Color.rgb(255, 000, 000));
					break;
				case UpdateStatus.No: // has no update
					try {
						PackageInfo info = getActivity().getPackageManager().getPackageInfo(
								getActivity().getPackageName(), 0);
						String version = info.versionName;
						tv_v.setVisibility(View.VISIBLE);
						tv_version.setTextColor(Color.rgb(153, 153, 153));
						tv_version.setText("当前版本："+version + "");
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}

		});
		UmengUpdateAgent.update(getActivity());
		
		
		super.onActivityCreated(savedInstanceState);
	}
	
	
	private void initView() {
		
		rl_feedback = (RelativeLayout) getActivity().findViewById(
				R.id.rl_fb);
		rl_about = (RelativeLayout) getActivity().findViewById(
				R.id.rl_aboutus);
		rl_contactus = (RelativeLayout) getActivity().findViewById(
				R.id.rl_contact);
		
		rl_hotactivity = (RelativeLayout) getActivity().findViewById(
				R.id.rl_hotactivity);
		rl_update = (RelativeLayout) getActivity().findViewById(
				R.id.rl_update);
		rl_yaoqing =  (RelativeLayout) getActivity().findViewById(
				R.id.rl_yaoqing);
		rl_osmsg = (RelativeLayout) getActivity().findViewById(
				R.id.rl_osmsg);
		
		rl_question = (RelativeLayout) getActivity().findViewById(
				R.id.rl_question);
		rl_cache = (RelativeLayout) getActivity().findViewById(
				R.id.rl_cache);
		
		tv_version = (TextView) getActivity().findViewById(
				R.id.tv_version);
		tv_v = (TextView) getActivity().findViewById(
				R.id.tv_v);
		tv_cache = (TextView) getActivity().findViewById(
				R.id.tv_cache);
		iv_guide_more = (ImageView) getActivity().findViewById(R.id.iv_guide_more);
		switch_guide = (UISwitchButton) getActivity().findViewById(R.id.switch_guide);
		if(getActivity().getSharedPreferences("isN", 0).getBoolean("isN", true)){
			iv_guide_more.setVisibility(View.VISIBLE);
			switch_guide.setChecked(true);
		}else{
			iv_guide_more.setVisibility(View.GONE);
			switch_guide.setChecked(false);
		}
		
		
		try {
			tv_cache.setText(DataCleanManager.getTotalCacheSize(getActivity()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		switch_guide.setOnCheckedChangeListener(new OnCheckedChangeListener() {           
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
            	if(!isChecked)//关闭
            	{
            		
            	if(iv_guide_more.getVisibility() == View.VISIBLE){
    				iv_guide_more.setVisibility(View.GONE);
    			}
            		FragmentMy.mHandler.sendEmptyMessage(85188);
            	}
            	else//打开
            	{
            		if(iv_guide_more.getVisibility() != View.VISIBLE){
        				iv_guide_more.setVisibility(View.VISIBLE);
        			}
            		FragmentMy.mHandler.sendEmptyMessage(8518);
            		
            	}
            }
        });
		
		_handler =  new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 1122:
					if(iv_guide_more.getVisibility() != View.VISIBLE & FragmentMy.iv_guide_my.getVisibility() != View.VISIBLE){
						switch_guide.setChecked(false);
					}/*else{
						switch_guide.setChecked(true);
					}*/
					break;
				
				default:
					break;
				}
			};
		};
		
	}
	
	private void setListener() {
		rl_feedback.setOnClickListener(this);
		rl_about.setOnClickListener(this);
		rl_contactus.setOnClickListener(this);
//		rl_message.setOnClickListener(this);
		rl_hotactivity.setOnClickListener(this);
		rl_yaoqing.setOnClickListener(this); 
		rl_osmsg.setOnClickListener(this); 
		rl_question.setOnClickListener(this); 
		rl_update.setOnClickListener(this); 
		rl_cache.setOnClickListener(this); 
		iv_guide_more.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_fb:
			startActivity(new Intent(getActivity(), ActFeedBack.class));
			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.rl_aboutus:
			startActivity(new Intent(getActivity(), ActAboutUs.class));
			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.rl_contact:
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
		case R.id.rl_hotactivity:
			startActivity(new Intent(getActivity(), ActHotActivity.class));
			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.rl_yaoqing:
			startActivity(new Intent(getActivity(), ActYaoqing.class));
			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.rl_osmsg:
			startActivity(new Intent(getActivity(), ActSyatemMsg.class));
			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.rl_question:
			startActivity(new Intent(getActivity(), ActCommonProblem.class));
			getActivity().overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.rl_update:
			UmengUpdateAgent.setUpdateAutoPopup(false);
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
				@Override
				public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
					switch (updateStatus) {
					case UpdateStatus.Yes: // has update
						UmengUpdateAgent.showUpdateDialog(getActivity(), updateInfo);
						break;
					case UpdateStatus.No: // has no update
						Toast.makeText(getActivity(), "您当前已经是最新版本！",
								Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.NoneWifi: // none wifi
						Toast.makeText(getActivity(), "没有wifi连接， 只在wifi下更新",
								Toast.LENGTH_SHORT).show();
						break;
					case UpdateStatus.Timeout: // time out
						Toast.makeText(getActivity(), "超时", Toast.LENGTH_SHORT)
								.show();
						break;
					}
					
				}

			});

			UmengUpdateAgent.update(getActivity());
			break;
		case R.id.rl_cache:
			try {
				DataCleanManager.clearAllCache(getActivity());
				tv_cache.setText(DataCleanManager.getTotalCacheSize(getActivity()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.iv_guide_more:
			if(iv_guide_more.getVisibility() == View.VISIBLE){
				iv_guide_more.setVisibility(View.GONE);
				_handler.sendEmptyMessage(1122);
				
				
				SharedPreferences isN = getActivity().getSharedPreferences("isN", 0);
				SharedPreferences.Editor editer = isN.edit();
				editer.clear();
				editer.commit();
				editer.putBoolean("isN", false);
				editer.commit();
			}
			break;
		default:
			break;
		}
		
	}
	
	
	@Override
		public void onResume() {
		
		try {
			if(tv_cache != null ){
				tv_cache.setText(DataCleanManager.getTotalCacheSize(getActivity()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
			super.onResume();
		}
}
