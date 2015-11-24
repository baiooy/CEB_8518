package com.ceb.activity;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.socialization.Socialization;

import com.baidu.push.example.MyPushMessageReceiver;
import com.caifuline.R;
import com.ceb.base.BaseActivity;
import com.ceb.base.ExitApplication;
import com.ceb.fragment.FragmentHot;
import com.ceb.fragment.FragmentMainPage;
import com.ceb.fragment.FragmentMore;
import com.ceb.fragment.FragmentMy;
import com.ceb.widge.SelectPicPopupWindow;
import com.umeng.update.UmengUpdateAgent;
import com.view.BgView;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	long exittime = 0;
	private FrameLayout id_content;
	SelectPicPopupWindow menuWindow; // 弹出框
	private FragmentMainPage mMain ;//= new FragmentMainPage();  
	private FragmentHot mHot;// = new FragmentHot();  
	private FragmentMy mMy ;//= new FragmentMy();  
	private FragmentMore mMore;// = new FragmentMore(); 
	private ImageView iv_main,iv_hot,iv_my,iv_more;
	private TextView tv_main,tv_hot,tv_my,tv_more;
	FragmentManager fm = getFragmentManager();  
	FragmentTransaction transaction ;
	public static int tuisong = 0;
	String notification;
	static public Handler _handler;
	private Handler mHandler;
	public static MainActivity instance;
	String enter;
	String hot = "1";
	@Override
	public void setView() {
		setContentView(R.layout.act_main);
		
		ShareSDK.initSDK(this);
		ShareSDK.registerService(Socialization.class);
		Socialization service = ShareSDK.getService(Socialization.class);
//		service.setCustomPlatform(new MyPlatform(this));
		instance = this;
		Log.i("MainActivity setView", "setView");
		Intent ii = getIntent();
		enter = ii.getStringExtra("in");
		hot = ii.getStringExtra("hot");
		Log.i("main_enter","enter=="+enter );
		if(enter!=null&&enter.equals("in")){
			Intent i =new Intent(MainActivity.this,GestureEditActivity.class);
			i.putExtra("flag", "open2");
			startActivityForResult(i, 0x02);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case -1:
					break;
				case 0:
					new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
		            .setContentText("手势密码设置成功!")
		            .show();
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
		mMain = new FragmentMainPage();
		mHot = new FragmentHot();
		mMy = new FragmentMy();
		mMore = new FragmentMore();
		FragmentTransaction transaction  = fm.beginTransaction();
		
		transaction.add(R.id.id_content, mMain)
		.add(R.id.id_content, mMy)
		.add(R.id.id_content, mHot)
		.add(R.id.id_content, mMore).commit();
		
//		if(fragid != null & fragid.equals("chongzhi")){
//			Toast.makeText(this, "chongzhi返回", 1000).show();
//			FragmentTransaction transaction2  = fm.beginTransaction();
//			transaction2
//	        .show(mHot)
//	        .hide(mMy)
//	        .hide(mMain)
//	        .hide(mMore)
//	        .commit();
//		}
//		
		setDefaultFragment();
		
		String userId = MyPushMessageReceiver.getUserId();
		String channelId = MyPushMessageReceiver.getChannelId();
		
		SharedPreferences baiduID = getSharedPreferences("baiduID", 0);
		SharedPreferences.Editor editor = baiduID.edit();
		editor.clear();
		editor.commit();
		editor.putString("userId", userId);
		editor.putString("channelId", channelId);
		editor.commit();
		
		/**
		 * 友盟自动更新语句
		 */
		UmengUpdateAgent.update(this);
		
//		mHandler = new Handler(){
//			@Override
//			public void handleMessage(Message msg) {
//				switch (msg.what) {
//				case 0:
//					tv_tips.setVisibility(View.GONE);
//					break;
//
//				default:
//					break;
//				}
//			}
//		};
		
		
		_handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case 1122:
					tuisong = 0;
					String strs = (String) msg.obj;
					if(strs == null || strs.length() == 0){
						strs = MyPushMessageReceiver.descrip;
					}
					uploadImage(MainActivity.this, strs);
					Log.i("通知===_handler", "notification " + notification);
					break;
				case -1:
					Toast.makeText(MainActivity.this, "网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					// babyImage.setClickable(false);
					break;
				case 0:
					break;
				
			
				
				default:
					break;
				}

			}
		};
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 0x02 && resultCode == RESULT_OK){
			Log.i("Oops", "Oops");
			new Thread(){
				public void run() {
					try {
						sleep(400);
						mHandler.sendEmptyMessage(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				};
			}.start();
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void uploadImage(final Activity context, String mes) {

		if (menuWindow != null && menuWindow.isShowing()) {
			;
		} else {
			menuWindow = new SelectPicPopupWindow(context, mes);
			// 显示窗口
			menuWindow.showAtLocation(
					MainActivity.this.findViewById(R.id.id_content),
					Gravity.TOP | Gravity.RIGHT, 0, 35); // 设置layout在PopupWindow中显示的位置
		}

	}
	//iv_main,iv_hot;tv_main,tv_hot;
	public void homepage(View v){
		iv_main.setImageResource(R.drawable.shouye);
		tv_main.setTextColor(getResources().getColor(R.color.zhusediao));
		iv_hot.setImageResource(R.drawable.rexiao);
		tv_hot.setTextColor(Color.rgb(153, 153, 153));
		iv_my.setImageResource(R.drawable.wode);
		tv_my.setTextColor(Color.rgb(153, 153, 153));
		iv_more.setImageResource(R.drawable.gengduo);
		tv_more.setTextColor(Color.rgb(153, 153, 153));
		
//		FragmentManager fm = getFragmentManager();  
//	    FragmentTransaction transaction = fm.beginTransaction();
//	   
		 if (mMain == null)  
         {  
			 mMain = new FragmentMainPage();  
         }    
//		 
//         transaction.replace(R.id.id_content, mMain); 
//         transaction.addToBackStack(null);
//         transaction.commit();
		
//		 switchContent2( (mHot == null?mMy:mHot),mMain);
		 FragmentTransaction transaction  = fm.beginTransaction();
		Log.d("homepage", "homepage");
		
		transaction
        .show(mMain)
        .hide(mMy)
        .hide(mHot)
        .hide(mMore)
        .commit();
	}
	public void hotsale(View v){
		iv_main.setImageResource(R.drawable.shouye1);
		tv_main.setTextColor(Color.rgb(153, 153, 153));
		iv_hot.setImageResource(R.drawable.rexiao1);
		tv_hot.setTextColor(getResources().getColor(R.color.zhusediao));
		iv_my.setImageResource(R.drawable.wode);
		tv_my.setTextColor(Color.rgb(153, 153, 153));
		iv_more.setImageResource(R.drawable.gengduo);
		tv_more.setTextColor(Color.rgb(153, 153, 153));
//		FragmentManager fm = getFragmentManager();  
//	    FragmentTransaction transaction = fm.beginTransaction();
		if (mHot == null)  
        {  
			mHot = new FragmentHot();  
        }    
//        transaction.replace(R.id.id_content, mHot); 
//        transaction.addToBackStack(null);
//        transaction.commit();
		
		//switchContent2( (mMain == null?mMy:mMain),mHot);
		Log.d("hotsale", "hotsale");
		FragmentTransaction transaction  = fm.beginTransaction();
		transaction
        .show(mHot)
        .hide(mMy)
        .hide(mMain)
        .hide(mMore)
        .commit();
	}
	
	public void myaccount(View v){
		iv_main.setImageResource(R.drawable.shouye1);
		tv_main.setTextColor(Color.rgb(153, 153, 153));
		iv_hot.setImageResource(R.drawable.rexiao);
		tv_hot.setTextColor(Color.rgb(153, 153, 153));
		iv_my.setImageResource(R.drawable.wode1);
		tv_my.setTextColor(getResources().getColor(R.color.zhusediao));
		iv_more.setImageResource(R.drawable.gengduo);
		tv_more.setTextColor(Color.rgb(153, 153, 153));
//		FragmentManager fm = getFragmentManager();  
//	    FragmentTransaction transaction = fm.beginTransaction();
		if (mMy == null)  
        {  
			mMy = new FragmentMy();  
        }    
//        transaction.replace(R.id.id_content, mMy); 
//        transaction.addToBackStack(null);
//        transaction.commit();
		Log.d("myaccount", "myaccount");
		FragmentTransaction transaction  = fm.beginTransaction();
		transaction
        .show(mMy)
        .hide(mHot)
        .hide(mMain)
        .hide(mMore)
        .commit();
//		switchContent2( (mMain == null?mHot:mMain),mMy);
	}
	
	public void more(View v){
		iv_more.setImageResource(R.drawable.gengduo1);
		tv_more.setTextColor(getResources().getColor(R.color.zhusediao));
		iv_main.setImageResource(R.drawable.shouye1);
		tv_main.setTextColor(Color.rgb(153, 153, 153));
		iv_hot.setImageResource(R.drawable.rexiao);
		tv_hot.setTextColor(Color.rgb(153, 153, 153));
		iv_my.setImageResource(R.drawable.wode);
		tv_my.setTextColor(Color.rgb(153, 153, 153));
//		iv_my.setImageResource(R.drawable.wode1);
//		tv_my.setTextColor(getResources().getColor(R.color.zhusediao));
//		FragmentManager fm = getFragmentManager();  
//	    FragmentTransaction transaction = fm.beginTransaction();
		if (mMore == null)  
        {  
			mMore = new FragmentMore();  
        }    
//        transaction.replace(R.id.id_content, mMy); 
//        transaction.addToBackStack(null);
//        transaction.commit();
		Log.d("myaccount", "myaccount");
		FragmentTransaction transaction  = fm.beginTransaction();
		transaction
        .show(mMore)
        .hide(mHot)
        .hide(mMain)
        .hide(mMy)
        .commit();
//		switchContent2( (mMain == null?mHot:mMain),mMy);
	}
	

	
	private void setDefaultFragment()  
    {  
		FragmentTransaction transaction  = fm.beginTransaction();
		transaction
        .show(mMain)
        .hide(mMy)
        .hide(mHot)
        .hide(mMore)
        .commit();
        /**
         * commitAllowingStateLoss():
         * Like commit() but allows the commit to be executed after an activity's state 
         * is saved. This is dangerous 	because the commit can be lost if the activity
         *  needs to later be restored from its state, so this should only be used 
         *  for cases where it is okay for the UI state to change unexpectedly on the user.
         */
//          transaction.commitAllowingStateLoss();
    }

	@Override
	protected void onResume() {
		Log.i("MainActivity onResume", "onResume");
		Log.i(" MainActivity onResume", "tuisong =  "+tuisong);
		Intent ii = getIntent();
		Log.i("MainActivity onResume", "notification =  "+ii.getStringExtra("notification"));
		if (tuisong == 10) {
				notification = ii.getStringExtra("notification");
				Log.i("通知===onCreate", "notification " + notification);
				Message msg = new Message();
				msg.obj = notification;
				msg.what = 1122;
				_handler.sendMessageDelayed(msg, 1000);
		}
		
//		Intent i = getIntent();
		
		Log.i("hot", "hot="+hot);
		if(hot == null){
		}else if(hot.equals("hot")){
			iv_main.setImageResource(R.drawable.shouye1);
			tv_main.setTextColor(Color.rgb(153, 153, 153));
			iv_hot.setImageResource(R.drawable.rexiao1);
			tv_hot.setTextColor(getResources().getColor(R.color.zhusediao));
			iv_my.setImageResource(R.drawable.wode);
			tv_my.setTextColor(Color.rgb(153, 153, 153));
			iv_more.setImageResource(R.drawable.gengduo);
			tv_more.setTextColor(Color.rgb(153, 153, 153));
			FragmentTransaction transaction1  = fm.beginTransaction();
			transaction1
	        .show(mHot)
	        .hide(mMy)
	        .hide(mMain)
	        .hide(mMore)
	        .commit();
		}
		hot = "1";
		super.onResume();
	}

	
	@Override
	public void initView() {
		Log.i("MainActivity initView", "initView");
		id_content = (FrameLayout) findViewById(R.id.id_content);
		
		iv_main = (ImageView) findViewById(R.id.iv_main);
		iv_hot = (ImageView) findViewById(R.id.iv_hot);
		iv_my = (ImageView) findViewById(R.id.iv_my);
		iv_more = (ImageView) findViewById(R.id.iv_more);
		tv_main = (TextView) findViewById(R.id.tv_main);
		tv_hot = (TextView) findViewById(R.id.tv_hot);
		tv_my = (TextView) findViewById(R.id.tv_my);
		tv_more = (TextView) findViewById(R.id.tv_more);
		
		
	}

	@Override
	public void setListener() {
		
	}
	
	@Override
	public void onClick(View v) {
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exittime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exittime = System.currentTimeMillis();
			} else {
				ExitApplication.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


}
