package com.ceb.activity;


import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.push.example.MyPushMessageReceiver;
import com.baidu.push.example.Utils;
import com.caifuline.R;
import com.ceb.activity.ActStartup.BannerTask;
import com.ceb.base.BaseActivity;
import com.ceb.widge.ActivitySplitAnimationUtil;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.model.BannerArray;
import com.model.BannerInfo;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;


import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ActAppStart extends BaseActivity {
	private ImageView[] imgviews;
	private List<ImageView> imageViewList;
	private ImageView splash;
	private ViewPager vp;
	private ArrayList<View> views;
	private Drawable[] drawables;
	long exittime = 0;
	private GestureDetector gestureDetector; 
	private int flaggingWidth;
	private boolean misScrolled;
	ImageView iv_bg;
	ImageView iv_line;
	Animation animFadein, animationRight;
//	RelativeLayout ll1, ll2, ll3;//ll4;
	private SharedPreferences isNewUser;
	private Boolean isNew = true; //默认开启
	private SharedPreferences baiduID;
	private static boolean _isFirst = true;
	static String userID, role;
	UserInfoService _userInfoService;
	private Handler _handler;
	ArrayList<BannerInfo> banner;
	private String userId,channelId;
	private String img;
	private String dur;
	private Boolean ispass = false;
	Runnable sendable ;
	private SharedPreferences sp_opengesture = null;
	private Boolean isopengesture = false; //默认开启
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0x01:
				splash.setVisibility(View.GONE);
				break;
			case 0x02:
				//android中如果要在线程中实现页面跳转，其动画效果overridePendingTransition会失效。
				Intent bob = new Intent(ActAppStart.this,
						ActStartup.class);
				startActivity(bob);
				ActAppStart.this.finish();
				overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
			case 0x03:
				Intent bo = new Intent(ActAppStart.this,
						MainActivity.class);
				startActivity(bo);
				ActAppStart.this.finish();
				overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
				break;
			case 0x04:
				Intent b = new Intent(ActAppStart.this,
						GestureVerifyActivity.class);
				b.putExtra("in", "in");
				startActivity(b);
				ActAppStart.this.finish();
				overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void setView() {
		setContentView(R.layout.act_appstart);
		animationRight = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.tutorail_right);
		views = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();
		_userInfoService = new UserInfoService(this);
		
		
		sp_opengesture = getSharedPreferences("OPENGESTURE", 0);
		isopengesture = sp_opengesture.getBoolean("isopengesture", false);
		
		Log.i("isopengesture", ""+isopengesture);
		
		/**
		 * 百度云推送开始绑定操作
		 */
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY,
				Utils.getMetaValue(ActAppStart.this, "api_key"));
		
		
		userId = MyPushMessageReceiver.getUserId();
		channelId = MyPushMessageReceiver.getChannelId();
		
		SharedPreferences baiduID = getSharedPreferences("baiduID", 0);
		SharedPreferences.Editor editor = baiduID.edit();
		editor.clear();
		editor.commit();
		editor.putString("userId", userId);
		editor.putString("channelId", channelId);
		editor.commit();
		
		baiduID = getSharedPreferences("baiduID", 0);
		String a = baiduID.getString("userId", "null");
		String b = baiduID.getString("channelId", "null"); 
		
		
		Log.i("baiduID", "userId ==>"+a+"  //channelId ====>"+b);
		
		isNewUser = getSharedPreferences("isNewUser", 0);
		isNew = isNewUser.getBoolean("isNewUser", true);
		imageViewList = new ArrayList<ImageView>();
		int[] imageResIDs = getImageResIDs();
		ImageView iv;
		for (int i = 0; i < imageResIDs.length; i++) {
			iv = new ImageView(this);
			iv.setBackgroundResource(imageResIDs[i]);
			imageViewList.add(iv);
		}
		
		_handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					new Thread(sendable).start();
					break;
				case -1:
					new Thread(sendable).start();
					break;
				default:
					break;
				}
			}
		};
		
		try {
			startup();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	 private int[] getImageResIDs() {
	    	return new int[]{
	    			R.drawable.u1,
	    			R.drawable.u2,
	    			R.drawable.u3,
	    	};
	    }
	
	@SuppressWarnings("deprecation")
	@Override
	public void initView() {
		
		
		 sendable = new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					if (isNewUser != null & !isNew ) {
						if(ispass){//跳过
							if(isopengesture){
								handler.sendEmptyMessage(0x04);
							}else{
								handler.sendEmptyMessage(0x03);
							}
							
						}else{
							handler.sendEmptyMessage(0x02);
						}
						
						
					}
					Thread.sleep(2000);
					handler.sendEmptyMessage(0x01);
				} catch (InterruptedException e) {
				}

			}
		};

		iv_bg = (ImageView) this.findViewById(R.id.iv_bg);
		iv_line = (ImageView) this.findViewById(R.id.iv_off);
		// ���ض���
		animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.tv_off);

		animFadein.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Log.i("onAnimationEnd", "onAnimationEnd");
				iv_line.setBackgroundColor(Color.BLACK);
				iv_line.setVisibility(View.GONE);

				ActAppStart.this.finish();
			}
		});
		gestureDetector = new GestureDetector(new GuideViewTouch());
		splash = (ImageView) findViewById(R.id.splash);
		vp = (ViewPager) findViewById(R.id.view);
		imgviews = new ImageView[3];
		imgviews[0] = (ImageView) findViewById(R.id.walker1);
		imgviews[1] = (ImageView) findViewById(R.id.walker2);
		imgviews[2] = (ImageView) findViewById(R.id.walker3);
//		imgviews[3] = (ImageView) findViewById(R.id.walker4);
		drawables = new Drawable[3];
		drawables[0] = getResources().getDrawable(
				R.drawable.wb_walker1_bottom_notloggedin);
		drawables[1] = getResources().getDrawable(
				R.drawable.wb_walker2_bottom_notloggedin);
		drawables[2] = getResources().getDrawable(
				R.drawable.wb_walker3_bottom_notloggedin);
//		drawables[3] = getResources().getDrawable(
//				R.drawable.wb_walker4_bottom_notloggedin);
		initViewpage();
		// ��ȡ�ֱ���
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		flaggingWidth = dm.widthPixels / 3;
//		new Thread(sendable).start();
	}

	@Override
	public void setListener() {
//		Log.i("ActAppstart  aaaaaa",
//				"UserId ==>" + MyPushMessageReceiver.getUserId());
//		Log.i("ActAppstart  bbbb",
//				"ChannelId ==>" + MyPushMessageReceiver.getChannelId());
	}

	private void initViewpage() {
//		views.clear();
//		views.add(ll1);
//		views.add(ll2);
//		views.add(ll3);
//		views.add(ll4);
//		vp.setTransitionEffect(TransitionEffect.Accordion);
//		vp.setAdapter(new MainAdapter(views));
//		vp.setAdapter(new MyViewPagerAdapter(views));
		
		ViewPagerAdapter adapter = new ViewPagerAdapter();
		vp.setAdapter(adapter);
		
		vp.setCurrentItem(0);

		vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				for (int i = 0; i < 3; i++) {
					if (i == position) {// ѡ��
						imgviews[i].setImageDrawable(drawables[i]);
					} else {
						imgviews[i]
								.setImageDrawable(getResources()
										.getDrawable(
												R.drawable.wb_walker_bottom_notloggedin));
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				switch (state) {
				case ViewPager.SCROLL_STATE_DRAGGING:
					misScrolled = false;
					break;
				case ViewPager.SCROLL_STATE_SETTLING:
					misScrolled = true;
					break;
				case ViewPager.SCROLL_STATE_IDLE:
					if (vp.getCurrentItem() == vp.getAdapter().getCount() - 1
							&& !misScrolled) {
					}
					misScrolled = true;
					break;
				}
			}
		});
	}

	class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return 3;
		}

		/**
		 * 判断出去的view是否等于进来的view 如果为true直接复用
		 */
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		/**
		 * 销毁预加载以外的view对象, 会把需要销毁的对象的索引位置传进来就是position
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(imageViewList.get(position % imageViewList.size()));
		}

		/**
		 * 创建一个view
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(imageViewList.get(position % imageViewList.size()));
			return imageViewList.get(position % imageViewList.size());
		}
    }
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exittime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exittime = System.currentTimeMillis();
			} else {// �˳�����
				vp.setVisibility(View.GONE);
				((LinearLayout) findViewById(R.id.num))
						.setVisibility(View.GONE);
				// ��ʼ����
				iv_line.setVisibility(View.VISIBLE);
				iv_line.startAnimation(animFadein);
				Log.i("startAnimation", "startAnimation");
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private class GuideViewTouch extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (vp.getCurrentItem() == vp.getAdapter().getCount() - 1) {
				if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY()
						- e2.getY())
						&& (e1.getX() - e2.getX() <= (-flaggingWidth) || e1
								.getX() - e2.getX() >= flaggingWidth)) {
					if (e1.getX() - e2.getX() >= flaggingWidth) {
//						Intent ins = new Intent(ActAppStart.this,
//								ActStartup.class);/
//						ins.putExtra("flag", "11");
//						ActivitySplitAnimationUtil.startActivity(
//								ActAppStart.this, ins);
						if(ispass){
							handler.sendEmptyMessage(0x03);
						}else{
							handler.sendEmptyMessage(0x02);
						}
						finish();
						overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
						return true;
					}
				}
			}
			return false;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) {
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		return super.dispatchTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	void startup(){
		String  signature = "startup.php:"+System.currentTimeMillis()/1000+":hxpay";
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
		userInfo.user_id = userId;
		userInfo.channel_id = channelId;
		
		new BannerTask().execute(userInfo);
	}
	
	class BannerTask extends AsyncTask<UserInfo, Void, BannerArray>{

		@Override
		protected BannerArray doInBackground(UserInfo... params) {
			BannerArray info = null;
			
			info = _userInfoService.startup(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(BannerArray result) {
			Log.i("appstart Banner result == null", (result == null)+"");
			if(result == null || result.item.size() == 0 ){
				ispass = true;
				_handler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0")){
				banner = result.item;
				ispass = false;
				for(int i=0 ;i< banner.size();i++){
					img = banner.get(i).img;
					dur = banner.get(i).dur;
				}
				_handler.sendEmptyMessage(0);
			}else{
				;//Toast.makeText(ActStartup.this, , Toast.LENGTH_SHORT).show();
			}
		}
		
	}

}

