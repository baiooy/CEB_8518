package com.ceb.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.adapter.AdapterHuankuanjihua;
import com.adapter.AdapterTouzijilu;
import com.caifuline.R;
import com.caifuline.R.color;
import com.ceb.base.BaseActivity;
import com.ceb.base.ExitApplication;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.TBLayout;
import com.ceb.widge.UtiEncrypt;
import com.ceb.widge.TBLayout.OnPageChangedListener;
import com.ceb.widge.TBLayout.OnPullListener;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
import com.model.CebDetail;
import com.model.InvestmentList_item;
import com.model.RepaymentList_item;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class ActCebDetail extends BaseActivity implements OnPullListener,
		OnPageChangedListener {
	public static ActCebDetail instance;
	private TBLayout mLayout;
	private RelativeLayout mHeader, mFooter;
	private LinearLayout mFooterContent,ll_yaoqing;
	private RelativeLayout mHeaderContent;
	private ViewPager vp;
	private ArrayList<View> views;
	private View product_detail, touzijilu, huankuanjihua, fengxiankongzhi;
	private ListView lv_touzijilu, lv_huankuanjihua;
	private TextView tv_touzi;
	private ScrollView scrollview1, scrollview2, scrollview3;
	private Button btn_chanpingxiangqing, btn_touzijilu, btn_huankuanjihua,
			btn_fengxiankongzhi;
	private WebView webview_productdetail;
	private SharedPreferences sp;
	private String userID;
	private String productID;
	private String borrowSatus;
	UserInfoService _userInfoService;
	private Handler mHandler;
	private TextView tv_title, tv_annualIncome, tv_progress, tv_minInvestment,
			tv_totalMoney, tv_period, tv_repayment, tv_info, tv_yiyou, tv_jilu;
	TextView tv_webview, tv_webview_fengkong;
	TextView tv_numInvestors, tv_aveAmount, tv_lastAmount;
	AdapterTouzijilu adapterTouzijilu;
	AdapterHuankuanjihua adapterHuankuanjihua;
	ArrayList<InvestmentList_item> investmentData;
	ArrayList<RepaymentList_item> repaymentData;
	private String title, subtitle, annualIncome, period, totalMoney,
			accountBalance, minInvestment, increaseMoney, borrowPass,
			alreadyInvest;
	LoadingDialog dialog;
	static double shouyi = 0.00;
	private double mMin;
	double borrowMoney;
	String maxInvestment;
	String type,freeMoney,awardStr,awardState;
	private SharedPreferences isLogin;
	private Boolean islogin = false;
	private TextView tv_state,tv_yiwancheng,tv_qitou,tv_ketou,tv_baifenhao,tv_qi,tv_yuan;
	private SharedPreferences sp_kaihu, sp_kaika;
	private boolean isopen, iskaika;
	@Override
	public void setView() {
		setContentView(R.layout.activity_scrollview);

		
		sp_kaihu = getSharedPreferences("huifukaihu", 0);
//		isopen = sp_kaihu.getBoolean("kaihu", false);
		sp_kaika = getSharedPreferences("kaika", 0);
//		iskaika = sp_kaika.getBoolean("kaika", false);
		instance = this;
		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		shouyi = 0.00;
		Intent in = getIntent();
		productID = in.getStringExtra("productID");
		borrowSatus = in.getStringExtra("borrowSatus");
		type = in.getStringExtra("type");
		awardState = in.getStringExtra("awardState");
		_userInfoService = new UserInfoService(this);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(ActCebDetail.this, "网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;

				case 0:
					dialog.dismiss();
					break;

				default:
					break;
				}
			}
		};
		isLogin = getSharedPreferences("isLogin", 0);
		islogin = isLogin.getBoolean("islogin", false);

		try {
			getDetail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initView() {
		mLayout = (TBLayout) findViewById(R.id.tblayout);
		mLayout.setOnPullListener(this);
		mLayout.setOnContentChangeListener(this);
		mHeader = (RelativeLayout) findViewById(R.id.header);
		mFooter = (RelativeLayout) findViewById(R.id.footer);
		mHeaderContent = (RelativeLayout) mHeader.getChildAt(0);
		mFooterContent = (LinearLayout) mFooter.getChildAt(0);
		ll_yaoqing = (LinearLayout) findViewById(R.id.ll_yaoqing);
		tv_touzi = (TextView) findViewById(R.id.tv_touzi);
		tv_state = (TextView) findViewById(R.id.tv_state);
		tv_yiwancheng = (TextView) findViewById(R.id.tv_yiwancheng);
		tv_qitou = (TextView) findViewById(R.id.tv_qitou);
		tv_ketou = (TextView) findViewById(R.id.tv_ketou);
		tv_baifenhao = (TextView) findViewById(R.id.tv_baifenhao);
		tv_qi = (TextView) findViewById(R.id.tv_qi);
		tv_yuan = (TextView) findViewById(R.id.tv_yuan);

		Log.i("borrowSatus", "borrowSatus=" + borrowSatus);
		if (borrowSatus != null && !borrowSatus.equals("22")) {
			tv_touzi.setBackgroundColor(Color.rgb(220, 220, 220));
			tv_touzi.setClickable(false);
		} else {
			tv_touzi.setBackgroundColor(getResources()
					.getColor(color.zhusediao));
			tv_touzi.setClickable(true);
			tv_touzi.setOnClickListener(this);
		}

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_annualIncome = (TextView) findViewById(R.id.tv_annualIncome);
		tv_progress = (TextView) findViewById(R.id.tv_progress);
		tv_minInvestment = (TextView) findViewById(R.id.tv_minInvestment);
		tv_totalMoney = (TextView) findViewById(R.id.tv_totalMoney);
		tv_period = (TextView) findViewById(R.id.tv_period);
		tv_repayment = (TextView) findViewById(R.id.tv_repayment);
		tv_info = (TextView) findViewById(R.id.tv_info);

		btn_chanpingxiangqing = (Button) findViewById(R.id.btn_chanpingxiangqing);
		btn_touzijilu = (Button) findViewById(R.id.btn_touzijilu);
		btn_huankuanjihua = (Button) findViewById(R.id.btn_huankuanjihua);
		btn_fengxiankongzhi = (Button) findViewById(R.id.btn_fengxiankongzhi);

		scrollview1 = (ScrollView) findViewById(R.id.scrollview1);
		scrollview1.setOnTouchListener(new TouchListenerImpl());
		vp = (ViewPager) findViewById(R.id.vp_cebdetail);

		LayoutInflater inflater = LayoutInflater.from(ActCebDetail.this);
		product_detail = inflater.inflate(R.layout.product_detail, null);
		touzijilu = inflater.inflate(R.layout.touzijilu, null);
		huankuanjihua = inflater.inflate(R.layout.huankuanjihua, null);
		fengxiankongzhi = inflater.inflate(R.layout.fengxiankongzhi, null);
		tv_webview_fengkong = (TextView) fengxiankongzhi
				.findViewById(R.id.tv_webview_fengkong);
		scrollview2 = (ScrollView) fengxiankongzhi
				.findViewById(R.id.scrollview2);
		scrollview2.setOnTouchListener(new TouchListenerImpl2());

		scrollview3 = (ScrollView) product_detail
				.findViewById(R.id.scrollview3);
		scrollview3.setOnTouchListener(new TouchListenerImpl3());

		tv_numInvestors = (TextView) touzijilu
				.findViewById(R.id.tv_numInvestors);
		tv_aveAmount = (TextView) touzijilu.findViewById(R.id.tv_aveAmount);
		tv_lastAmount = (TextView) touzijilu.findViewById(R.id.tv_lastAmount);

		tv_yiyou = (TextView) touzijilu.findViewById(R.id.tv_yiyou);
		tv_jilu = (TextView) touzijilu.findViewById(R.id.tv_jilu);

		tv_webview = (TextView) product_detail.findViewById(R.id.tv_webview);
		webview_productdetail = (WebView) product_detail
				.findViewById(R.id.webview_productdetail);
		lv_touzijilu = (ListView) touzijilu.findViewById(R.id.lv_touzijilu);
		lv_huankuanjihua = (ListView) huankuanjihua
				.findViewById(R.id.lv_huankuanjihua);
		// lv_touzijilu.setAdapter(new AdapterTouzijilu(ActCebDetail.this));
		// lv_huankuanjihua.setAdapter(new
		// AdapterHuankuanjihua(ActCebDetail.this));

		lv_touzijilu.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				boolean enable = false;
				TBLayout.isScroll = false;
				if (lv_touzijilu != null && lv_touzijilu.getChildCount() > 0) {
					// check if the first item of the list is visible
					boolean firstItemVisible = lv_touzijilu
							.getFirstVisiblePosition() == 0;
					// check if the top of the first item is visible
					boolean topOfFirstItemVisible = lv_touzijilu.getChildAt(0)
							.getTop() == 0;
					// enabling or disabling the refresh layout
					enable = firstItemVisible && topOfFirstItemVisible;

				}
				if (lv_touzijilu.getChildCount() == 0) {
					enable = true;
				}
				// swipe_huikuan.setEnabled(enable);
				if (enable) {
					TBLayout.isScroll = true;
					// Toast.makeText(ActCebDetail.this, "顶部1", 1000).show();
				}
			}
		});

		lv_huankuanjihua.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				boolean enable = false;
				TBLayout.isScroll = false;
				if (lv_huankuanjihua != null
						&& lv_huankuanjihua.getChildCount() > 0) {
					// check if the first item of the list is visible
					boolean firstItemVisible = lv_huankuanjihua
							.getFirstVisiblePosition() == 0;
					// check if the top of the first item is visible
					boolean topOfFirstItemVisible = lv_huankuanjihua
							.getChildAt(0).getTop() == 0;
					// enabling or disabling the refresh layout
					enable = firstItemVisible && topOfFirstItemVisible;

				}
				if (enable) {
					TBLayout.isScroll = true;
					// Toast.makeText(ActCebDetail.this, "顶部2", 1000).show();
				}
				// swipe_huikuan.setEnabled(enable);
			}
		});

		initViewpage();
	}

	private class TouchListenerImpl implements OnTouchListener {
		@SuppressWarnings("static-access")
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_MOVE:
				TBLayout.isScroll2 = false;
				TBLayout.isScroll = false;
				int scrollY = view.getScrollY();
				int height = view.getHeight();
				int scrollViewMeasuredHeight = scrollview1.getChildAt(0)
						.getMeasuredHeight();

				if (scrollY <= 0
						&& (scrollY + height) >= scrollViewMeasuredHeight) {
					TBLayout.isScroll2 = true;
					scrollview1.setFocusable(false);
					// Toast.makeText(ActCebDetail.this, "scrollview1全显示",
					// 1000).show();
				} else if ((scrollY + height) >= scrollViewMeasuredHeight) {// 底部
					TBLayout.isScroll2 = true;
					scrollview1.setFocusable(false);
					// Toast.makeText(ActCebDetail.this, "scrollview1底部显示",
					// 1000).show();
				} else if (scrollY <= 0) {// 顶部
				// TBLayout.isScroll2 = true;
				// scrollview1.setFocusable(false);
				// Toast.makeText(ActCebDetail.this, "scrollview1顶部显示",
				// 1000).show();
				}
				break;
			default:
				break;
			}
			return false;
		}

	};

	private class TouchListenerImpl2 implements OnTouchListener {
		@SuppressWarnings("static-access")
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_MOVE:
				TBLayout.isScroll2 = false;
				TBLayout.isScroll = false;
				int scrollY = view.getScrollY();
				int height = view.getHeight();
				int scrollViewMeasuredHeight = scrollview2.getChildAt(0)
						.getMeasuredHeight();

				if (scrollY == 0
						&& (scrollY + height) == scrollViewMeasuredHeight) {
					TBLayout.isScroll = true;
					// Toast.makeText(ActCebDetail.this, "scrollview2全显示",
					// 1000).show();
				} else if (scrollY == 0
						&& (scrollY + height) != scrollViewMeasuredHeight) {// 顶部
					TBLayout.isScroll = true;
					scrollview2.setFocusable(false);
					// Toast.makeText(ActCebDetail.this, "scrollview2顶部显示",
					// 1000).show();
				} else if (scrollY != 0
						&& (scrollY + height) == scrollViewMeasuredHeight) {// 底部
				// TBLayout.isScroll = true;
				// scrollview1.setFocusable(false);
				// Toast.makeText(ActCebDetail.this, "scrollview2底部显示",
				// 1000).show();
				}
				break;
			default:
				break;
			}
			return false;
		}

	};

	private class TouchListenerImpl3 implements OnTouchListener {
		@SuppressWarnings("static-access")
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_MOVE:
				TBLayout.isScroll2 = false;
				TBLayout.isScroll = false;
				int scrollY = view.getScrollY();
				int height = view.getHeight();
				int scrollViewMeasuredHeight = scrollview3.getChildAt(0)
						.getMeasuredHeight();

				if (scrollY == 0
						&& (scrollY + height) == scrollViewMeasuredHeight) {
					TBLayout.isScroll = true;
					// Toast.makeText(ActCebDetail.this, "scrollview2全显示",
					// 1000).show();
				} else if (scrollY == 0
						&& (scrollY + height) != scrollViewMeasuredHeight) {// 顶部
					TBLayout.isScroll = true;
					scrollview3.setFocusable(false);
					// Toast.makeText(ActCebDetail.this, "scrollview2顶部显示",
					// 1000).show();
				} else if (scrollY != 0
						&& (scrollY + height) == scrollViewMeasuredHeight) {// 底部
				// TBLayout.isScroll = true;
				// scrollview1.setFocusable(false);
				// Toast.makeText(ActCebDetail.this, "scrollview2底部显示",
				// 1000).show();
				}
				break;
			default:
				break;
			}
			return false;
		}

	};

	@Override
	public void setListener() {

		btn_chanpingxiangqing.setOnClickListener(this);
		btn_touzijilu.setOnClickListener(this);
		btn_huankuanjihua.setOnClickListener(this);
		btn_fengxiankongzhi.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_touzi:
			isopen = sp_kaihu.getBoolean("kaihu", false);
			iskaika = sp_kaika.getBoolean("kaika", false);
			/**
			 * 此时判断是否处于登陆状态，若已登录，跳入立即投资界面，若未登录，跳入输入手机号界面，然后走 用户验证接口判断是否为注册用户。
			 */
			if (!islogin) {// 未登录
				Intent ins = new Intent(ActCebDetail.this,
						ActUserYanzheng.class);
				ins.putExtra("productID", productID);
				startActivity(ins);
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
			}else if(type != null && !type.equals("2")){//不是体验标 判断是否开户
				if (!isopen) {//未开户
					 new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
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
		                	 Intent in = new Intent(ActCebDetail.this, ActHuifu.class);
		         			startActivity(in);
		         			overridePendingTransition(android.R.anim.fade_in,
		         					android.R.anim.fade_out);
		                 	sDialog.dismiss();
		                 }
		             })
		             .show();
					
				}else{//已开户
					Intent ii = new Intent(ActCebDetail.this, ActTouzi.class);
					// title,subtitle,annualIncome,period,totalMoney,accountBalance,minInvestment;
					ii.putExtra("title", title);
					ii.putExtra("subtitle", subtitle);
					ii.putExtra("annualIncome", annualIncome);
					ii.putExtra("period", period);
					ii.putExtra("totalMoney", totalMoney);
					ii.putExtra("accountBalance", accountBalance);
					ii.putExtra("minInvestment", minInvestment);
					ii.putExtra("productID", productID);
					ii.putExtra("shouyi", shouyi);
					ii.putExtra("mMin", mMin);
					ii.putExtra("borrowMoney", borrowMoney);
					ii.putExtra("maxInvestment", maxInvestment);
					ii.putExtra("increaseMoney", increaseMoney);
					ii.putExtra("borrowPass", borrowPass);
					ii.putExtra("type", type);
					ii.putExtra("alreadyInvest", alreadyInvest);//freeMoney
					ii.putExtra("freeMoney", freeMoney);
					startActivity(ii);
					overridePendingTransition(android.R.anim.fade_in,
							android.R.anim.fade_out);
				}
			}else {//是体验标
				Intent ii = new Intent(ActCebDetail.this, ActTouzi.class);
				// title,subtitle,annualIncome,period,totalMoney,accountBalance,minInvestment;
				ii.putExtra("title", title);
				ii.putExtra("subtitle", subtitle);
				ii.putExtra("annualIncome", annualIncome);
				ii.putExtra("period", period);
				ii.putExtra("totalMoney", totalMoney);
				ii.putExtra("accountBalance", accountBalance);
				ii.putExtra("minInvestment", minInvestment);
				ii.putExtra("productID", productID);
				ii.putExtra("shouyi", shouyi);
				ii.putExtra("mMin", mMin);
				ii.putExtra("borrowMoney", borrowMoney);
				ii.putExtra("maxInvestment", maxInvestment);
				ii.putExtra("increaseMoney", increaseMoney);
				ii.putExtra("borrowPass", borrowPass);
				ii.putExtra("type", type);
				ii.putExtra("alreadyInvest", alreadyInvest);//freeMoney
				ii.putExtra("freeMoney", freeMoney);
				startActivity(ii);
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
			}
			break;
		case R.id.btn_chanpingxiangqing:
			btn_chanpingxiangqing.setTextColor(getResources().getColor(
					R.color.white));
			btn_chanpingxiangqing.setBackgroundResource(R.drawable.left_check);

			btn_touzijilu.setTextColor(getResources().getColor(
					R.color.zhusediao));
			btn_touzijilu.setBackgroundResource(R.drawable.right_uncheck);

			btn_huankuanjihua.setTextColor(getResources().getColor(
					R.color.zhusediao));
			btn_huankuanjihua.setBackgroundResource(R.drawable.middle_uncheck);

			btn_fengxiankongzhi.setTextColor(getResources().getColor(
					R.color.zhusediao));
			btn_fengxiankongzhi
					.setBackgroundResource(R.drawable.middle_uncheck);

			vp.setCurrentItem(0);
			break;
		case R.id.btn_touzijilu:
			btn_chanpingxiangqing.setTextColor(getResources().getColor(
					R.color.zhusediao));
			btn_chanpingxiangqing
					.setBackgroundResource(R.drawable.left_uncheck);

			btn_touzijilu.setTextColor(getResources().getColor(R.color.white));
			btn_touzijilu.setBackgroundResource(R.drawable.right_check);

			btn_huankuanjihua.setTextColor(getResources().getColor(
					R.color.zhusediao));
			btn_huankuanjihua.setBackgroundResource(R.drawable.middle_uncheck);

			btn_fengxiankongzhi.setTextColor(getResources().getColor(
					R.color.zhusediao));
			btn_fengxiankongzhi
					.setBackgroundResource(R.drawable.middle_uncheck);

			vp.setCurrentItem(3);
			break;
		case R.id.btn_huankuanjihua:
			btn_chanpingxiangqing.setTextColor(getResources().getColor(
					R.color.zhusediao));
			btn_chanpingxiangqing
					.setBackgroundResource(R.drawable.left_uncheck);

			btn_touzijilu.setTextColor(getResources().getColor(
					R.color.zhusediao));
			btn_touzijilu.setBackgroundResource(R.drawable.right_uncheck);

			btn_huankuanjihua.setTextColor(getResources().getColor(
					R.color.white));
			btn_huankuanjihua.setBackgroundResource(R.drawable.middle_check);

			btn_fengxiankongzhi.setTextColor(getResources().getColor(
					R.color.zhusediao));
			btn_fengxiankongzhi
					.setBackgroundResource(R.drawable.middle_uncheck);

			vp.setCurrentItem(2);
			break;
		case R.id.btn_fengxiankongzhi:
			btn_chanpingxiangqing.setTextColor(getResources().getColor(
					R.color.zhusediao));
			btn_chanpingxiangqing
					.setBackgroundResource(R.drawable.left_uncheck);

			btn_touzijilu.setTextColor(getResources().getColor(
					R.color.zhusediao));
			btn_touzijilu.setBackgroundResource(R.drawable.right_uncheck);

			btn_huankuanjihua.setTextColor(getResources().getColor(
					R.color.zhusediao));
			btn_huankuanjihua.setBackgroundResource(R.drawable.middle_uncheck);

			btn_fengxiankongzhi.setTextColor(getResources().getColor(
					R.color.white));
			btn_fengxiankongzhi.setBackgroundResource(R.drawable.middle_check);

			vp.setCurrentItem(1);
			break;
		case R.id.tv_state://productID
			Intent in = new Intent(this,ActAwardCondition.class);
			in.putExtra("productID", productID);
			startActivity(in);
			overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		default:
			break;
		}

	}

	
	@Override
	public void onResume() {
		super.onResume();
		Log.i("onResume", "onResume");
		isLogin = getSharedPreferences("isLogin", 0);
		islogin = isLogin.getBoolean("islogin", false);
		super.onResume();
		
	}
	
	private void initViewpage() {
		views = new ArrayList<View>();
		
		if(type != null && type.equals("2")){//体验标
			views.add(product_detail);
			views.add(huankuanjihua);
			
			
			vp.setAdapter(new MyViewPagerAdapter(views));
			vp.setCurrentItem(0);
			vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

				@Override
				public void onPageSelected(int position) {
					if (position == 0) {
						btn_chanpingxiangqing.setTextColor(getResources().getColor(
								R.color.white));
						btn_chanpingxiangqing
								.setBackgroundResource(R.drawable.left_check);

//						btn_fengxiankongzhi.setTextColor(getResources().getColor(
//								R.color.zhusediao));
//						btn_fengxiankongzhi
//								.setBackgroundResource(R.drawable.middle_uncheck);

						btn_huankuanjihua.setTextColor(getResources().getColor(
								R.color.zhusediao));
						btn_huankuanjihua
								.setBackgroundResource(R.drawable.right_uncheck);

//						btn_touzijilu.setTextColor(getResources().getColor(
//								R.color.zhusediao));
//						btn_touzijilu
//								.setBackgroundResource(R.drawable.right_uncheck);

					} else if (position == 1) {
						btn_chanpingxiangqing.setTextColor(getResources().getColor(
								R.color.zhusediao));
						btn_chanpingxiangqing
								.setBackgroundResource(R.drawable.left_uncheck);

//						btn_fengxiankongzhi.setTextColor(getResources().getColor(
//								R.color.white));
//						btn_fengxiankongzhi
//								.setBackgroundResource(R.drawable.middle_check);

						btn_huankuanjihua.setTextColor(getResources().getColor(
								R.color.white));
						btn_huankuanjihua
								.setBackgroundResource(R.drawable.right_check);

//						btn_touzijilu.setTextColor(getResources().getColor(
//								R.color.zhusediao));
//						btn_touzijilu
//								.setBackgroundResource(R.drawable.right_uncheck);
					} 
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}
			});
			
			
		}else{
			views.add(product_detail);
			views.add(fengxiankongzhi);
			views.add(huankuanjihua);
			views.add(touzijilu);
			vp.setAdapter(new MyViewPagerAdapter(views));
			vp.setCurrentItem(0);
			vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

				@Override
				public void onPageSelected(int position) {
					if (position == 0) {
						btn_chanpingxiangqing.setTextColor(getResources().getColor(
								R.color.white));
						btn_chanpingxiangqing
								.setBackgroundResource(R.drawable.left_check);

						btn_fengxiankongzhi.setTextColor(getResources().getColor(
								R.color.zhusediao));
						btn_fengxiankongzhi
								.setBackgroundResource(R.drawable.middle_uncheck);

						btn_huankuanjihua.setTextColor(getResources().getColor(
								R.color.zhusediao));
						btn_huankuanjihua
								.setBackgroundResource(R.drawable.middle_uncheck);

						btn_touzijilu.setTextColor(getResources().getColor(
								R.color.zhusediao));
						btn_touzijilu
								.setBackgroundResource(R.drawable.right_uncheck);

					} else if (position == 1) {
						btn_chanpingxiangqing.setTextColor(getResources().getColor(
								R.color.zhusediao));
						btn_chanpingxiangqing
								.setBackgroundResource(R.drawable.left_uncheck);

						btn_fengxiankongzhi.setTextColor(getResources().getColor(
								R.color.white));
						btn_fengxiankongzhi
								.setBackgroundResource(R.drawable.middle_check);

						btn_huankuanjihua.setTextColor(getResources().getColor(
								R.color.zhusediao));
						btn_huankuanjihua
								.setBackgroundResource(R.drawable.middle_uncheck);

						btn_touzijilu.setTextColor(getResources().getColor(
								R.color.zhusediao));
						btn_touzijilu
								.setBackgroundResource(R.drawable.right_uncheck);
					} else if (position == 2) {
						btn_chanpingxiangqing.setTextColor(getResources().getColor(
								R.color.zhusediao));
						btn_chanpingxiangqing
								.setBackgroundResource(R.drawable.left_uncheck);

						btn_fengxiankongzhi.setTextColor(getResources().getColor(
								R.color.zhusediao));
						btn_fengxiankongzhi
								.setBackgroundResource(R.drawable.middle_uncheck);

						btn_huankuanjihua.setTextColor(getResources().getColor(
								R.color.white));
						btn_huankuanjihua
								.setBackgroundResource(R.drawable.middle_check);

						btn_touzijilu.setTextColor(getResources().getColor(
								R.color.zhusediao));
						btn_touzijilu
								.setBackgroundResource(R.drawable.right_uncheck);
					} else {
						btn_chanpingxiangqing.setTextColor(getResources().getColor(
								R.color.zhusediao));
						btn_chanpingxiangqing
								.setBackgroundResource(R.drawable.left_uncheck);

						btn_fengxiankongzhi.setTextColor(getResources().getColor(
								R.color.zhusediao));
						btn_fengxiankongzhi
								.setBackgroundResource(R.drawable.middle_uncheck);

						btn_huankuanjihua.setTextColor(getResources().getColor(
								R.color.zhusediao));
						btn_huankuanjihua
								.setBackgroundResource(R.drawable.middle_uncheck);

						btn_touzijilu.setTextColor(getResources().getColor(
								R.color.white));
						btn_touzijilu.setBackgroundResource(R.drawable.right_check);
					}
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}
			});
		}
		
	}

	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public int getCount() {

			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mListViews.get(position), 0);

			return mListViews.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));
		}

	}

	@Override
	public void onPageChanged(int stub) {
		switch (stub) {
		case TBLayout.SCREEN_HEADER:
			Log.d("tag", "SCREEN_HEADER");
			break;
		case TBLayout.SCREEN_FOOTER:
			Log.d("tag", "SCREEN_FOOTER");
			break;
		}

	}

	@Override
	public boolean headerFootReached(MotionEvent event) {
		if (mHeader.getScrollY() + mHeader.getHeight() >= mHeaderContent
				.getHeight()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean footerHeadReached(MotionEvent event) {
		if (mFooter.getScrollY() == 0) {
			return true;
		}
		return false;
	}

	public void onback(View v) {
		SharedPreferences Back = getSharedPreferences("back", 0);
		SharedPreferences.Editor editor2 = Back.edit();
		editor2.clear();
		editor2.commit();
		editor2.putBoolean("back", true);
		editor2.commit();
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			SharedPreferences Back = getSharedPreferences("back", 0);
			SharedPreferences.Editor editor2 = Back.edit();
			editor2.clear();
			editor2.commit();
			editor2.putBoolean("back", true);
			editor2.commit();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	void getDetail() {
		String  signature = "detail.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.productID = productID;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(ActCebDetail.this);

		new DetailTask().execute(userInfo);
	}

	class DetailTask extends AsyncTask<UserInfo, Void, CebDetail> {

		@Override
		protected void onPreExecute() {
			dialog = new LoadingDialog(ActCebDetail.this);
			dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
			dialog.show();
		}

		@Override
		protected CebDetail doInBackground(UserInfo... params) {
			CebDetail info = null;

			info = _userInfoService.getCebDetail(params[0]);

			Log.i("info", "info ==" + info);
			return info;
		}

		@Override
		protected void onPostExecute(CebDetail result) {
			// mSwipeRefreshLayout.setRefreshing(false);
			if (result == null || result.toString().equals("null")) {
				mHandler.sendEmptyMessage(-1);
			} else if (result.ret.equals("0")) {
				// tv_annualIncome,tv_progress,tv_minInvestment,
				// tv_totalMoney,tv_period,tv_repayment,tv_info

				// title,subtitle,annualIncome,period,totalMoney,accountBalance,minInvestment;

				title = result.title;
				subtitle = result.subtitle;
				annualIncome = result.annualIncome;
				period = result.period;
				totalMoney = result.totalMoney;
				accountBalance = result.accountBalance;
				minInvestment = result.minInvestment;
				increaseMoney = result.increaseMoney;
				borrowPass = result.borrowPass;
				alreadyInvest = result.alreadyInvest;

				tv_title.setText(result.title);
				tv_annualIncome.setText(result.annualIncome);
				
				tv_period.setText(result.period);
				tv_repayment.setText(result.repayment);
				tv_info.setText(result.info);
				type = result.type;
				awardStr = result.awardStr;
				freeMoney = result.freeMoney;
				if(result.type.equals("2")){//体验标
					tv_state.setVisibility(View.VISIBLE);
					tv_state.setBackgroundDrawable(null);
					tv_state.setText("免本体验标");
					tv_state.setTextColor(Color.rgb(204, 153, 000));
					tv_state.setClickable(false);
					tv_yiwancheng.setText("计息日期");
					tv_qitou.setText("体验本金");
					tv_ketou.setText("还款日期");
					tv_progress.setTextSize(18);
					tv_progress.setText(result.startDate);
					try {
						tv_minInvestment.setText(result.freeMoney.split("\\.")[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					tv_totalMoney.setTextSize(18);
					RelativeLayout.LayoutParams layoutParams= 
				            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT); 
				            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT); 
				            layoutParams.setMargins(0, 0, 13, 0);
				            tv_totalMoney.setLayoutParams(layoutParams); 
				            tv_progress.setLayoutParams(layoutParams); 
					tv_totalMoney.setText(result.endDate);
					tv_baifenhao.setVisibility(View.INVISIBLE);
					tv_qi.setVisibility(View.GONE);
					tv_yuan.setVisibility(View.INVISIBLE);
					
					btn_chanpingxiangqing.setText("体验说明");
					btn_touzijilu.setVisibility(View.GONE);
		//			btn_huankuanjihua
					btn_fengxiankongzhi.setVisibility(View.GONE);
					btn_huankuanjihua
					.setBackgroundResource(R.drawable.right_uncheck);
					
					LinearLayout.LayoutParams Params= 
				            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT); 
		//			Params.width = 1000;
					Params.setMargins(200, 0, 200, 0);
					ll_yaoqing.setLayoutParams(Params); 
					
				}else{
					if(result.type.equals("0") && awardState != null && awardState.equals("1")){//奖励标
						tv_state.setVisibility(View.VISIBLE);
						tv_state.setTextColor(getResources().getColor(color.zhusediao));
						tv_state.setText(awardStr);
						tv_state.setClickable(true);
						tv_state.setOnClickListener(ActCebDetail.this);
						tv_state.setBackgroundResource(R.drawable.jiang3);
						
//						Drawable drawable = getResources().getDrawable(R.drawable.jianglibiao);
//						drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
//						tv_state.setCompoundDrawables(drawable, null, null, null);
					}else{
						tv_state.setVisibility(View.INVISIBLE);
					}
			//		tv_state.setVisibility(View.INVISIBLE);
					tv_yiwancheng.setText("已完成");
					tv_qitou.setText("起投金额");
					tv_ketou.setText("可投金额");
					tv_progress.setTextSize(24);
					tv_progress.setText(result.progress);
					try {
						tv_minInvestment.setText(result.minInvestment.split("\\.")[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					tv_totalMoney.setTextSize(24);
					tv_totalMoney.setText(result.totalMoney);
					tv_baifenhao.setVisibility(View.VISIBLE);
					tv_qi.setVisibility(View.VISIBLE);
					tv_yuan.setVisibility(View.VISIBLE);
					
					btn_huankuanjihua
					.setBackgroundResource(R.drawable.middle_uncheck);
				}
				
				if (result.pdHtml.equals("null")) {
					result.pdHtml = "";
				} else {
					tv_webview.setText(Html.fromHtml(result.pdHtml));
				}

				if (result.rcdHtml == null) {
					result.rcdHtml = "";
				}
				tv_webview_fengkong.setText(Html.fromHtml(result.rcdHtml));// result.rcdHtml
				// webview_productdetail.getSettings().setDefaultTextEncodingName("UTF-8")
				// ;
				// webview_productdetail.loadData(result.pdHtml, "text/html",
				// "UTF-8");
				if (result.numInvestors.equals("0")) {
					tv_yiyou.setVisibility(View.GONE);
					tv_jilu.setVisibility(View.GONE);
					tv_numInvestors.setText("暂无投资记录");
					tv_numInvestors.setTextColor(Color.GRAY);
				} else {
					tv_yiyou.setVisibility(View.VISIBLE);
					tv_jilu.setVisibility(View.VISIBLE);
					tv_numInvestors.setText(result.numInvestors);
					tv_numInvestors.setTextColor(Color.RED);
				}

				// tv_numInvestors.setText(result.numInvestors);
				tv_aveAmount.setText(result.aveAmount);
				tv_lastAmount.setText(result.lastAmount);

				investmentData = result.investmentList_item;
				repaymentData = result.repaymentList_item;

				for (int i = 0; i < repaymentData.size(); i++) {
					double ii = Double
							.parseDouble(repaymentData.get(i).maturityYield);
					shouyi += ii;
				}

				borrowMoney = Double.parseDouble(result.borrowMoney);// 6
				maxInvestment = result.maxInvestment;// 8
				double totalMoney = Double.parseDouble(result.totalMoney);// 9
				double dd = Double.parseDouble(maxInvestment);
				if (dd == 0.00) {
					mMin = (borrowMoney > totalMoney) ? totalMoney
							: borrowMoney;
				} else {
					mMin = dd;
				}

				adapterTouzijilu = new AdapterTouzijilu(ActCebDetail.this,
						investmentData);
				lv_touzijilu.setAdapter(adapterTouzijilu);

				adapterHuankuanjihua = new AdapterHuankuanjihua(
						ActCebDetail.this, repaymentData);
				lv_huankuanjihua.setAdapter(adapterHuankuanjihua);

				mHandler.sendEmptyMessage(0);
			} else {
				Toast.makeText(ActCebDetail.this, result.msg,
						Toast.LENGTH_SHORT).show();
				// }
			}

		}

	}
}
