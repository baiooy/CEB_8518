package com.ceb.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.TreeMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import com.adapter.AdapterInvest;
import com.adapter.AdapterTrade;
import com.caifuline.R;
import com.caifuline.R.color;
import com.ceb.base.BaseActivity;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.loadingdialog.LoadingDialog;
import com.model.ActionResultInfo;
import com.model.InviteList;
import com.model.NoPartner;
import com.model.TradeDetail_item;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;
import com.xlistview.XListView;
import com.xlistview.XListView.IXListViewListener;

public class ActInviteList extends BaseActivity implements IXListViewListener{
	XListView listView_nopa;
	private Boolean ispartner = false;
	private Button btn_join,btn_investrule;
	private ImageButton btn_share;
	private SharedPreferences sp;
	private String userID;
	private UserInfoService _userInfoService;
	private TextView tv_pid,tv_invitenum,tv_number,tv_content;
	private LinearLayout mEmpty_ll;
	private LinearLayout mNetless_ll;
	private LinearLayout loading_nopa;
	private TextView tv_re1,tv_reload;
	private Handler _handler;
	LoadingDialog dialog;
	private AdapterInvest mAdapter;
	private String testImage;
	private String pid,title ,content,outerContent, imgurl,htmlurl;
	private static final String FILE_NAME = "/pic_lovely_cats.jpg";
	private String flag;
	static int _pageIndex = 1;
	int _pageCount = 1;
	int oldCount;
	ArrayList<NoPartner> mData;
	@Override
	public void setView() {
		setContentView(R.layout.act_invite_list);
		_userInfoService = new UserInfoService(ActInviteList.this);
		
		
		_pageIndex = 1;
	    _pageCount = 1;
	//    count = 0;
	    mData = new ArrayList<NoPartner>();
		
		Intent intent = getIntent();
		ispartner = intent.getBooleanExtra("ispartner", false);
		flag = intent.getStringExtra("flag");
//		new Thread() {
//			public void run() {
//				initImagePath();
//			}
//		}.start();
		_handler = new Handler(){
			@SuppressWarnings("unchecked")
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0://是合伙人
					Intent ii = new Intent(ActInviteList.this, ActInviteListNoPartner.class);
					startActivity(ii);
					ActInviteList.this.finish();
					overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
					break;
				case 1://不是合伙人
					
					break;
				case 2://正在审核中。。。
					btn_join.setText("申请中");
					btn_join.setClickable(false);
					break;
				case 3:
					mData = (ArrayList<NoPartner>) msg.obj;
					
					mAdapter = new AdapterInvest(ActInviteList.this,mData);
					mAdapter.notifyDataSetChanged();
					listView_nopa.setAdapter(mAdapter);

					if (_pageIndex > 1 && mData.size() > oldCount + 1) {
						listView_nopa.smoothScrollBy(oldCount, 1000);
						listView_nopa.setSelectionFromTop(oldCount, 100);
					} else if (mData.size() > oldCount) {
						listView_nopa.smoothScrollBy(oldCount, 1000);
						listView_nopa.setSelectionFromTop(oldCount, 100);
					}
					
					
//					mAdapter = new AdapterInvest(ActInviteList.this,result.noPartner_item);
//					mAdapter.notifyDataSetChanged();
//					listView_nopa.setAdapter(mAdapter);
					break;
				default:
					break;
				}
				if(dialog != null & dialog.isShowing())
					dialog.dismiss();
			};
		};
		
		try {
			getInviteList();
			share();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initImagePath() {
		try {
			String cachePath = com.mob.tools.utils.R.getCachePath(this, null);
			testImage = cachePath + FILE_NAME;
			File file = new File(testImage);
			if (!file.exists()) {
				file.createNewFile();
				Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.ceb_icon);
				FileOutputStream fos = new FileOutputStream(file);
				pic.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch(Throwable t) {
			t.printStackTrace();
			testImage = null;
		}
	}
	
	private void showShare(String title,String con,String imageurl,String htmlurl) {
		Log.i("showShare", title+"///"+con+"///"+imageurl+"///"+htmlurl);
		 htmlurl = htmlurl+"?pid="+getSharedPreferences("PID", 0).getString("pid", "4209");
		 con = con+"\n注册邀请码："+getSharedPreferences("PID", 0).getString("pid", "4209");
		 OnekeyShare oks = new OnekeyShare();
			oks.setTitle(title);
			oks.setTitleUrl(htmlurl);
			oks.setText(con);
			oks.setImageUrl(imageurl);
			oks.setUrl(htmlurl);
			oks.setComment(getString(R.string.share));
			oks.disableSSOWhenAuthorize();
			oks.setComment(getString(R.string.share));
			oks.setSite(getString(R.string.app_name));
			oks.setSiteUrl("http://bao.8518.com");
			oks.setVenueName("ShareSDK");
//			// text是分享文本，所有平台都需要这个字段
//					oks.setText("ceb8518");
			oks.setVenueDescription("This is a beautiful place!");
			oks.setLatitude(23.056081f);
			oks.setLongitude(113.385708f);
			oks.disableSSOWhenAuthorize();
			oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
				public void onShare(Platform platform, ShareParams paramsToShare) {
					// 改写twitter分享内容中的text字段，否则会超长，
					// 因为twitter会将图片地址当作文本的一部分去计算长度
					if ("Twitter".equals(platform.getName())) {
					}
				}
			});
//		 //关闭sso授权
//		 oks.disableSSOWhenAuthorize(); 

		// 启动分享GUI
		 oks.show(this);
		 }
	
	@Override
	public void initView() {
		btn_join = (Button) findViewById(R.id.btn_join);
		btn_share = (ImageButton) findViewById(R.id.btn_share);
		btn_investrule = (Button) findViewById(R.id.btn_investrule);
		tv_pid = (TextView) findViewById(R.id.tv_pid); 
		tv_invitenum = (TextView) findViewById(R.id.tv_invitenum);
		tv_number = (TextView) findViewById(R.id.tv_number);
		tv_content = (TextView) findViewById(R.id.tv_content);
		mEmpty_ll = (LinearLayout)findViewById(R.id.empty_nopa);//显示没有数据
		mNetless_ll = (LinearLayout) findViewById(R.id.netless_nopa);//显示没有数据
		loading_nopa = (LinearLayout) findViewById(R.id.loading_nopa);
		tv_reload = (TextView) findViewById(R.id.tv_emp_nopa);
		tv_re1 = (TextView) findViewById(R.id.tv_net_nopa);
		listView_nopa = (XListView) findViewById(R.id.listView_nopa);
		listView_nopa.setPullLoadEnable(true);
		listView_nopa.setPullRefreshEnable(false);
//		listView_nopa.setAdapter(new AdapterInvest(this));
		
		if(flag != null & flag.equals("99")){
			btn_join.setText("申请中");
			btn_join.setTextColor(color.grey);
			btn_join.setClickable(false);
		}else if(flag != null & flag.equals("0")){
			btn_join.setText("立即入伙合伙人");
			btn_join.setTextColor(getResources().getColor(R.drawable.pinglun_text));
			btn_join.setClickable(true);
			btn_join.setOnClickListener(this);
		}
		
		tv_reload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					getInviteList();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		tv_re1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					getInviteList();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
	}

	@Override
	public void setListener() {
		
		btn_investrule.setOnClickListener(this);
		btn_share.setOnClickListener(this);
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_join:
			try {
				join();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.btn_investrule:
			Intent in1 = new Intent(ActInviteList.this,ActInviteRule.class);
			startActivity(in1);
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.btn_share:
			showShare(title,outerContent,imgurl,htmlurl);
			break;
		default:
			break;
		}

	}
	
	public void onback(View v) {
		finish();
	}
	
	void share(){
		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		String  signature = "sharefriend.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.type = "5";//邀请记录分享
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(this);
		
		if(SppaConstant.isNetworkAvailable(this)){
			new ShareTask().execute(userInfo);
		}
	}
	
	class ShareTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{


		@Override
		protected void onPreExecute() {
//			dialog = new LoadingDialog(ActInviteList.this);
//			dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
//			dialog.show();
		}
		
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
//				pid = result.pid;
				title = result.title;
				content = result.content;
				outerContent = result.outerContent;
				imgurl = result.imgurl;
				htmlurl = result.htmlurl;
				
				Log.i("onPostExecute", title+"///"+content+"///"+imgurl+"///"+htmlurl);
				_handler.sendEmptyMessage(1);
			}else{
				Toast.makeText(ActInviteList.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	
	
	
	void join(){
		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		String  signature = "inviteapply.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(this);
		
		if(SppaConstant.isNetworkAvailable(this)){
			new JoinTask().execute(userInfo);
		}
		
	}
	
	class JoinTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{


		@Override
		protected void onPreExecute() {
			dialog = new LoadingDialog(ActInviteList.this);
			dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
			dialog.show();
		}
		
		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.getJoin(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result == null){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret != null & result.ret.equals("0")){
				try {
					isPartner();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				Toast.makeText(ActInviteList.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	
	
	void isPartner(){
		sp = getSharedPreferences("USERID", 0);
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
		userInfo.udid = SppaConstant.getDeviceInfo(this);
		
		if(SppaConstant.isNetworkAvailable(this)){
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
					_handler.sendEmptyMessage(0);
				}else if(result.partnerType.equals("0")){
					ispartner = false;
					_handler.sendEmptyMessage(1);
				}else{
					ispartner = false;
					_handler.sendEmptyMessage(2);
				}
			}else{
				Toast.makeText(ActInviteList.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	void getInviteList(){
		if (_pageCount > 0 && _pageIndex > _pageCount) {
			listView_nopa.stopLoadMore();
			return;
		}
		if (_pageIndex < 1)
			_pageIndex = 1;

		if (_pageIndex > _pageCount)
			_pageIndex = _pageCount;
		
		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		String  signature = "invitelist.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.type = "0";//不是合伙人
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.page = _pageIndex;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(this);
		
		if(!SppaConstant.isNetworkAvailable(this)){
			listView_nopa.setEmptyView(mNetless_ll);
		}else{
			new InviteTask().execute(userInfo);
		}
	}
	
	class InviteTask extends AsyncTask<UserInfo, Void, InviteList>{
		
		@Override
		protected void onPreExecute() {
			dialog = new LoadingDialog(ActInviteList.this);
			dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
			dialog.show();
		}

		@Override
		protected InviteList doInBackground(UserInfo... params) {
			InviteList info = null;
			
			info = _userInfoService.getInviteList(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(InviteList result) {
			if(listView_nopa != null){
				listView_nopa.stopRefresh();
				listView_nopa.stopLoadMore();
			}
			
			if(result == null){
				listView_nopa.setEmptyView(mEmpty_ll);
				_handler.sendEmptyMessage(-1);
			}else if(result != null && result.noPartner_item.size() < 1 ){
				tv_pid.setText(result.pid);
				tv_invitenum.setText(result.invitenum);
				tv_number.setText(result.number);
				tv_content.setText(result.content);
				listView_nopa.setEmptyView(mEmpty_ll);
				_handler.sendEmptyMessage(-1);
			}else if(result.ret != null & result.ret.equals("0")){
				tv_pid.setText(result.pid);
				tv_invitenum.setText(result.invitenum);
				tv_number.setText(result.number);
				
				int pages = Integer.parseInt(result.count);
				int page = Integer.parseInt(result.page);
				if(pages%10 == 0){
					_pageCount = pages/10;
				}else{
					_pageCount = (pages+10)/10;
				}
				
				if(page == _pageCount){//显示到最后一页，底部加载更多隐藏
					listView_nopa.setPullLoadEnable(false);
				}else{
					listView_nopa.setPullLoadEnable(true);
				}
				
	//			_pageCount = pages/10+pages%10;;
				oldCount = mData.size();
				
				if (result != null && result.noPartner_item.size() > 0) {

					for (int i = 0; i < result.noPartner_item.size(); i++) {
						mData.add(result.noPartner_item.get(i));
					}
				}
				Message msg = new Message();
				if (mData.size() == 0) {
					msg.obj = result;
				} else {
					msg.obj = mData;
				}

				msg.what = 3;

				_handler.sendMessage(msg);
				
				
				
				
//				
//				mAdapter = new AdapterInvest(ActInviteList.this,result.noPartner_item);
//				mAdapter.notifyDataSetChanged();
//				listView_nopa.setAdapter(mAdapter);
//				_handler.sendEmptyMessage(2);
			}else{
				Toast.makeText(ActInviteList.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}




	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
				_pageIndex++;
				getInviteList();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
