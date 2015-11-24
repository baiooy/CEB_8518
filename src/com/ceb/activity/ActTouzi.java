package com.ceb.activity;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import android.R.array;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.caifuline.R;
import com.ceb.base.BaseActivity;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.model.ActionResultInfo;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;

public class ActTouzi extends BaseActivity {
	public static ActTouzi instance;
	private Button btn_touzi;
	private Button btn_add, btn_subtract, btn_agreement, btn_chongzhi,
			btn_quantou;
	private TextView tv_touzie, tv_title, tv_subtitle, tv_annualIncome,
			tv_period, tv_totalMoney, tv_accountBalance, tv_shouyi,
			tv_maxInvestment,tv_alreadyInvest;
	private String productID, title, subtitle, annualIncome, period,
			totalMoney, accountBalance, minInvestment,alreadyInvest;
	private double shouyi;
	private double mMin, borrowMoney;
	private String maxInvestment;
	private double yujishouyi;
	private SharedPreferences isLogin;
	private Boolean islogin = false;
	int touzie;
	String userID;
	private SharedPreferences sp;
	private Boolean isopen = false;
	private SharedPreferences spcard;
	private Boolean opencard = false;
	private String increaseMoney, borrowPass;
	UserInfoService _userInfoService;
	String type,freeMoney;
	private LinearLayout ll_maxInvestment,ll_totalMoney;
	private TextView tv1;
	@Override
	public void setView() {
		setContentView(R.layout.act_touzi);

		isLogin = getSharedPreferences("isLogin", 0);
		islogin = isLogin.getBoolean("islogin", false);
		instance = this;
		sp = getSharedPreferences("huifukaihu", 0);
		isopen = sp.getBoolean("kaihu", false);

		spcard = getSharedPreferences("kaika", 0);
		opencard = sp.getBoolean("kaika", false);
		_userInfoService = new UserInfoService(this);
		Log.i("isopen--opencard", isopen + "--" + (opencard) + "");

		Intent in = getIntent();
		productID = in.getStringExtra("productID");
		// title,subtitle,annualIncome,period,totalMoney,accountBalance,minInvestment;
		title = in.getStringExtra("title");
		subtitle = in.getStringExtra("subtitle");
		annualIncome = in.getStringExtra("annualIncome");
		period = in.getStringExtra("period");
		totalMoney = in.getStringExtra("totalMoney");
		accountBalance = in.getStringExtra("accountBalance");
		borrowMoney = in.getDoubleExtra("borrowMoney", 0.00);
		increaseMoney = in.getStringExtra("increaseMoney");
		borrowPass = in.getStringExtra("borrowPass");
		type = in.getStringExtra("type");
		freeMoney = in.getStringExtra("freeMoney");
		
		alreadyInvest = in.getStringExtra("alreadyInvest");
		
		maxInvestment = in.getStringExtra("maxInvestment");
		if (accountBalance.equals("null")) {
			accountBalance = "0.00 ";
		}
		minInvestment = in.getStringExtra("minInvestment");
		shouyi = in.getDoubleExtra("shouyi", 0.00);
		mMin = in.getDoubleExtra("mMin", 0.00);

		Log.i("shouyi--mMin", shouyi + "--" + mMin);
		Log.i("检测网络连接", SppaConstant.isNetworkAvailable(this)+"");

	}

	@Override
	public void initView() {
		btn_touzi = (Button) findViewById(R.id.btn_touzi);
		btn_add = (Button) findViewById(R.id.btn_add);
		btn_subtract = (Button) findViewById(R.id.btn_subtract);
		btn_quantou = (Button) findViewById(R.id.btn_quantou);
		tv_touzie = (TextView) findViewById(R.id.tv_touzie);
		btn_agreement = (Button) findViewById(R.id.btn_agreement);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
		tv_annualIncome = (TextView) findViewById(R.id.tv_annualIncome);
		tv_period = (TextView) findViewById(R.id.tv_period);
		tv_totalMoney = (TextView) findViewById(R.id.tv_totalMoney);
		tv_accountBalance = (TextView) findViewById(R.id.tv_accountBalance);
		tv_shouyi = (TextView) findViewById(R.id.tv_shouyi);
		tv_alreadyInvest = (TextView) findViewById(R.id.tv_alreadyInvest);
		tv_maxInvestment = (TextView) findViewById(R.id.tv_maxInvestment);
		btn_chongzhi = (Button) findViewById(R.id.btn_chongzhi);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv_alreadyInvest.setText(alreadyInvest);
		ll_maxInvestment = (LinearLayout) findViewById(R.id.ll_maxInvestment);
		ll_totalMoney = (LinearLayout) findViewById(R.id.ll_totalMoney);
		
		if(type != null && type.equals("2")){//体验标
			ll_maxInvestment.setVisibility(View.GONE);
			ll_totalMoney.setVisibility(View.GONE);
			btn_chongzhi.setVisibility(View.GONE);
			btn_quantou.setVisibility(View.GONE);
			tv1.setText("体验本金:");
			try {
				tv_touzie.setText(freeMoney.split("\\.")[0]);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				tv_accountBalance.setText(freeMoney);
			} catch (Exception e) {
				e.printStackTrace();
			}
			btn_add.setEnabled(false);
			btn_subtract.setEnabled(false);
			
			
		}else{
			ll_maxInvestment.setVisibility(View.VISIBLE);
			ll_totalMoney.setVisibility(View.VISIBLE);
			btn_chongzhi.setVisibility(View.VISIBLE);
			btn_quantou.setVisibility(View.VISIBLE);
			tv1.setText("账户余额:");
			try {
				tv_touzie.setText(minInvestment.split("\\.")[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			tv_accountBalance.setText(accountBalance);
			btn_add.setEnabled(true);
			btn_subtract.setEnabled(true);
		}
		
		
		
		
		
		
		
		
		
		
		// Integer.parseInt("12".split("\\.")[0])
		if (Integer.parseInt(maxInvestment.split("\\.")[0]) == 0) {
//			maxInvestment = totalMoney;
			tv_maxInvestment.setText("无投资金额限制");
		} else {
			tv_maxInvestment.setText(maxInvestment + "元");
		}

		tv_title.setText(title);
		if (!subtitle.equals("")) {
			tv_subtitle.setText("(" + subtitle + ")");
		}

		tv_annualIncome.setText(annualIncome + "%");
		tv_period.setText(period + "天");
		tv_totalMoney.setText(totalMoney + "元");
//		tv_accountBalance.setText(accountBalance);

		// tv_shouyi.setText("" + "元");
//		tv_touzie.setText(minInvestment.split("\\.")[0]);

		// if(Integer.parseInt(tv_touzie.getText().toString().split("\\.")[0])
		// <= Integer.parseInt(minInvestment.split(".")[0])){
		btn_subtract.setEnabled(false);
		btn_subtract.setBackgroundResource(R.drawable.jianhaodan);
		// }

		yujishouyi = Integer.parseInt(tv_touzie.getText().toString()
				.split("\\.")[0])
				* shouyi / borrowMoney;

		Log.i("yujishouyi", yujishouyi + "--");
		tv_shouyi.setText(new DecimalFormat("0.00").format(yujishouyi) + "元");
	}

	@Override
	public void setListener() {
		btn_touzi.setOnClickListener(this);
		btn_add.setOnClickListener(this);
		btn_subtract.setOnClickListener(this);
		btn_agreement.setOnClickListener(this);
		btn_chongzhi.setOnClickListener(this);
		btn_quantou.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		isLogin = getSharedPreferences("isLogin", 0);
		islogin = isLogin.getBoolean("islogin", false);

		sp = getSharedPreferences("huifukaihu", 0);
		isopen = sp.getBoolean("kaihu", false);

		spcard = getSharedPreferences("kaika", 0);
		opencard = sp.getBoolean("kaika", false);
		if(type != null && !type.equals("2")){
			tv_touzie.setText(minInvestment.split("\\.")[0]);
			yujishouyi = Integer.parseInt(tv_touzie.getText().toString()
					.split("\\.")[0])
					* shouyi / borrowMoney;
			tv_shouyi.setText(new DecimalFormat("0.00").format(yujishouyi)
					+ "元");
		}
		
		
		try {
			personalcenter();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_touzi:

			 String[] arrtouzi = touzijisuan(Integer.parseInt(totalMoney.split("\\.")[0]),
						Integer.parseInt(maxInvestment.split("\\.")[0]),
						Integer.parseInt(tv_accountBalance.getText().toString().split("\\.")[0]),
						Integer.parseInt(alreadyInvest.split("\\.")[0]),
						Integer.parseInt(increaseMoney.split("\\.")[0]),
						Integer.parseInt(tv_touzie.getText().toString().split("\\.")[0]),
						Integer.parseInt(minInvestment.split("\\.")[0]));
			 
			 
			switch (Integer.parseInt(arrtouzi[0])) {
			
			case 10:
			
				 new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
	             .setContentText(arrtouzi[1])
	             .setCancelText("取消")
	             .setConfirmText("去充值")
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
	                	 Intent in = new Intent(ActTouzi.this, ActRecharge.class);
	         			in.putExtra("money", tv_accountBalance.getText().toString());// productID
	         			startActivity(in);
	         			overridePendingTransition(android.R.anim.fade_in,
	         					android.R.anim.fade_out);
	                 	sDialog.dismiss();
	                 }
	             })
	             .show();
				
				break;
			case 11:
						
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
				.setTitleText("Oops...").setContentText(arrtouzi[1])
				.show();
				break;
			case 12:
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
				.setContentText(arrtouzi[1])
				.show();
				break;
			case 14:
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
				.setContentText(arrtouzi[1])
				.show();
				break;
			case 13:
				 if (!borrowPass.equals("")) {
					LayoutInflater inflater = getLayoutInflater();
					View layout = inflater.inflate(R.layout.qianggouma,
							(ViewGroup) findViewById(R.id.dialog));
					final EditText etname = (EditText) layout
							.findViewById(R.id.etname);
					new AlertDialog.Builder(this)
							.setView(layout)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											if (etname.getText().toString()
													.equals(borrowPass)) {
											
												Intent ini = new Intent(
														ActTouzi.this,
														ActTouziHuifu.class);
												ini.putExtra("money",
														tv_touzie.getText()
																.toString());// productID
												ini.putExtra("productID",
														productID);
												ini.putExtra("type",
														type);
												ini.putExtra("title",
														title);
												startActivity(ini);
												overridePendingTransition(
														android.R.anim.fade_in,
														android.R.anim.fade_out);	
														


											} else {
												Toast.makeText(ActTouzi.this,
														"抢购码不正确", 1000).show();
											}

										}
									})

							.setNegativeButton("取消", null).show();
				}else{
					
					Intent ini = new Intent(
							ActTouzi.this,
							ActTouziHuifu.class);
					ini.putExtra("money",
							tv_touzie.getText()
									.toString());// productID
					ini.putExtra("productID",
							productID);
					ini.putExtra("productID",
							productID);
					ini.putExtra("type",
							type);
					ini.putExtra("title",
							title);
					startActivity(ini);
					overridePendingTransition(
							android.R.anim.fade_in,
							android.R.anim.fade_out);	
					
					
				}
				
				
				
				
				break;
			}
	
			break;
		case R.id.btn_add:
			Log.i("Integer.parseInt(maxInvestment", Integer.parseInt(maxInvestment.split("\\.")[0])+"");
			String arradd[] = addjisuan(Integer.parseInt(totalMoney.split("\\.")[0]),
					Integer.parseInt(maxInvestment.split("\\.")[0]),
					Integer.parseInt(tv_accountBalance.getText().toString().split("\\.")[0]),
					Integer.parseInt(alreadyInvest.split("\\.")[0]),
					Integer.parseInt(increaseMoney.split("\\.")[0]),
					Integer.parseInt(tv_touzie.getText().toString().split("\\.")[0]),
					Integer.parseInt(minInvestment.split("\\.")[0]));
			
			switch (Integer.parseInt(arradd[0])) {
			case 0:
//				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//				.setTitleText("Oops...").setContentText(arradd[1])
//				.show();
				 new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
	             .setContentText(arradd[1])
	             .setCancelText("取消")
	             .setConfirmText("去充值")
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
	                	 Intent in = new Intent(ActTouzi.this, ActRecharge.class);
	         			in.putExtra("money", tv_accountBalance.getText().toString());// productID
	         			startActivity(in);
	         			overridePendingTransition(android.R.anim.fade_in,
	         					android.R.anim.fade_out);
	                 	sDialog.dismiss();
	                 }
	             })
	             .show();
				break;
			case 1:
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
				.setContentText(arradd[1])
				.show();
				break;
			case 2:
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
				.setContentText(arradd[1])
				.show();
				break;
			case 4:
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
				.setContentText(arradd[1])
				.show();
				break;

			default:
				break;
			}
			
			 yujishouyi = Integer.parseInt(tv_touzie.getText().toString()
						.split("\\.")[0])
						* shouyi / borrowMoney;
				tv_shouyi.setText(new DecimalFormat("0.00").format(yujishouyi)
						+ "元");
			
			
			btn_subtract.setEnabled(true);
			break;
		case R.id.btn_subtract:
			int touzi = Integer.parseInt(tv_touzie.getText().toString()
					.split("\\.")[0]);
			Log.i("touzi", "" + touzi);
			Log.i("increaseMoney", "" + increaseMoney);
			if (touzi > Integer.parseInt(minInvestment.split("\\.")[0])) {
				if (!increaseMoney.equals("")) {
					touzi = touzi
							- Integer.parseInt(increaseMoney.split("\\.")[0]);
				} else {
					touzi = touzi
							- Integer.parseInt(minInvestment.split("\\.")[0]);
				}

				tv_touzie.setText(touzi + "");
			} else {
				btn_subtract.setEnabled(false);
				btn_subtract.setBackgroundResource(R.drawable.jianhaodan);
			}

			yujishouyi = Integer.parseInt(tv_touzie.getText().toString()
					.split("\\.")[0])
					* shouyi / borrowMoney;
			// new DecimalFormat("#.00").format(yujishouyi);
			tv_shouyi.setText(new DecimalFormat("0.00").format(yujishouyi)
					+ "元");
			break;
		case R.id.btn_agreement:
			Intent ii = new Intent(ActTouzi.this, ActAgreement.class);
//			if(type.equals("2")){
//				type = "0";
//			}
			ii.putExtra("type", type);
			startActivity(ii);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			break;
		case R.id.btn_chongzhi:
			Intent in = new Intent(ActTouzi.this, ActRecharge.class);
			in.putExtra("money", tv_accountBalance.getText().toString());// productID
			startActivity(in);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			break;
		case R.id.btn_quantou:

			/**
			 * 
			 * @param a  可投
			 * @param b  总额上限
			 * @param c  账户余额
			 * @param d  已投金额
			 * @param e  递增金额
			 * @param f  投标金额
			 * @param g  起投金额
			 * @return
			 */
			
			
		
			String[] arrquantoujisuan = quantoujisuan(Integer.parseInt(totalMoney.split("\\.")[0]),
									Integer.parseInt(maxInvestment.split("\\.")[0]),
									Integer.parseInt(tv_accountBalance.getText().toString().split("\\.")[0]),
									Integer.parseInt(alreadyInvest.split("\\.")[0]),
									Integer.parseInt(increaseMoney.split("\\.")[0]),
									Integer.parseInt(tv_touzie.getText().toString().split("\\.")[0]),
									Integer.parseInt(minInvestment.split("\\.")[0]));
			switch (Integer.parseInt(arrquantoujisuan[0])) {
			
			case 0:
				
//				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//				.setTitleText("Oops...").setContentText(arrquantoujisuan[1])
//				.show();
				 new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
	             .setContentText(arrquantoujisuan[1])
	             .setCancelText("取消")
	             .setConfirmText("去充值")
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
	                	 Intent in = new Intent(ActTouzi.this, ActRecharge.class);
	         			in.putExtra("money", tv_accountBalance.getText().toString());// productID
	         			startActivity(in);
	         			overridePendingTransition(android.R.anim.fade_in,
	         					android.R.anim.fade_out);
	                 	sDialog.dismiss();
	                 }
	             })
	             .show();
				break;
			case 1:
						
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
				.setContentText(arrquantoujisuan[1])
				.show();
				break;

			case 4:
				if(!arrquantoujisuan[1].equals("")){
//					new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//					.setTitleText("Oops...").setContentText(arrquantoujisuan[1])
//					.show();
					 new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
		             .setContentText(arrquantoujisuan[1])
		             .setCancelText("取消")
		             .setConfirmText("去充值")
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
		                	 Intent in = new Intent(ActTouzi.this, ActRecharge.class);
		         			in.putExtra("money", tv_accountBalance.getText().toString());// productID
		         			startActivity(in);
		         			overridePendingTransition(android.R.anim.fade_in,
		         					android.R.anim.fade_out);
		                 	sDialog.dismiss();
		                 }
		             })
		             .show();
				}
				
				
				tv_touzie.setText(arrquantoujisuan[2]);
				
				 yujishouyi = Integer.parseInt(tv_touzie.getText().toString()
							.split("\\.")[0])
							* shouyi / borrowMoney;
					tv_shouyi.setText(new DecimalFormat("0.00").format(yujishouyi)
							+ "元");
					btn_subtract.setEnabled(true);
				
				break;

				
			
			}
		 
			
			
		}	
		}


	void personalcenter() {
		sp = getSharedPreferences("USERID", 0);
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
		userInfo.udid = SppaConstant.getDeviceInfo(this);

		new PersonalcenterTask().execute(userInfo);
	}

	class PersonalcenterTask extends
			AsyncTask<UserInfo, Void, ActionResultInfo> {

		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;

			info = _userInfoService.getPersoncenter(params[0]);

			return info;
		}

		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if (result == null) {
				// _handler.sendEmptyMessage(-1);
			} else if (result.ret.equals("0")) {
				Log.i("SppaConstant.isNumeric",
						SppaConstant.isNumeric(result.cumulativeRevenue) + "");

				if(type!=null && !type.equals("2")){
					if (!result.availableBalance.equals("") ) {
						tv_accountBalance.setText(result.availableBalance);
					} else {
						tv_accountBalance.setText("0.00");
					}
				}
				

				// _handler.sendEmptyMessage(0);
			} else {
				// Toast.makeText(this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void onback(View v) {
		finish();
	}
	
	
	/**
	 * 
	 * @param a  可投
	 * @param b  总额上限
	 * @param c  账户余额
	 * @param d  已投金额
	 * @param e  递增金额
	 * @param f  投标金额
	 * @param g  起投金额
	 * @return
	 */
	
	public Map<String, String> mininvest(int a,int b,int c,int d,int e,int f,int g){
		
		Map<String, String> map ;
		
		if(c == 0){
			 map = new HashMap<String, String>();
			 map.put("0", "您的余额不足，请去充值赚钱吧");
			 map.put("0", "");
			return map;
		}else if(b == 0){
			 map = new HashMap<String, String>();
			 map.put("1", "无投资上限");
			 map.put("0", "");
			
			return map;
		}else if(d == 0){
			 map = new HashMap<String, String>();
			 map.put("2", "此标还未投资");
			 map.put("0", "");
			
			 
			 
			 
			return map;
		}else if(e == 0){
			 e = g;//起投金额付给递增金额
			 map = new HashMap<String, String>();
			 map.put("3", "");
			 map.put("0", "");
			
			return map;
		}else {
			
//			/**
//			 * c = 954  e= 50
//			 * 
//			 */
//			if(c%e != 0){
//				c-c%e;
//			}
			
			
			
			
			
			
			
			
			 map = new HashMap<String, String>();
			 map.put("3", "");
			 map.put(""+Math.min(Math.min(a, b), Math.min(c, d)), "");
			 return map;
		}
		
//		return Math.min(Math.min(a, b), Math.min(c, d));
	}
	
	
	
	
	/**
	 * 
	 * @param a  可投
	 * @param b  总额上限
	 * @param c  账户余额
	 * @param d  已投金额
	 * @param e  递增金额
	 * @param f  投标金额
	 * @param g  起投金额
	 * @return
	 */
	
	public String[] quantoujisuan(int a,int b,int c,int d,int e,int f,int g){
		   String arr[] ;
		
		if(c == 0 || c < g){
			arr = new String[3] ; 
			   arr[0] = "0" ;  
		       arr[1] = "您的余额不足，请去充值赚钱吧" ; 
		       arr[2] = "" ;  

			return arr;
		}else if(a == 0){
			arr = new String[3] ; 
			arr[0] = "1" ;  
		    arr[1] = "该标已满额，请换其它标投哦！" ; 
		    arr[2] = "" ;  

			return arr;
		}else {
			/**
			 * c = 954  e= 50
			 * 
			 */
			if(b == 0){
				b=Integer.MAX_VALUE;
			}
			 int minint = Math.min(Math.min(a, b), c);
			 int touzie = minint-minint%e;
			 if(touzie < g){
				 touzie = g;
				 arr = new String[3] ; 
					arr[0] = "4" ;  //leixing
				    arr[1] = "老板，余额不足啦，去充值赚钱吧！" ;   //meg
				    arr[2] = ""+touzie ;   //mun
				 
			 }else{
				 arr = new String[3] ; 
					arr[0] = "4" ;  //leixing
				    arr[1] = "" ;   //meg
				    arr[2] = ""+touzie ;   //mun
			 }
			 
			
			 
			 
			 return arr;
		}
		
	}
	
	/**
	 * 
	 * @param a  可投
	 * @param b  总额上限
	 * @param c  账户余额
	 * @param d  已投金额
	 * @param e  递增金额
	 * @param f  投标金额
	 * @param g  起投金额
	 * @return
	 */
	
	public String[] touzijisuan(int a,int b,int c,int d,int e,int f,int g){
		   String arr[] = null ; 
		if(c == 0 || c < f){
			arr = new String[3] ; 
			   arr[0] = "10" ;  
		       arr[1] = "老板，余额不足啦，去充值赚钱吧！" ; 
		       arr[2] = "" ;  

			return arr;
		}else if(a == 0){
			arr = new String[3] ; 
			arr[0] = "14" ;  
		    arr[1] = "该标已满额，请换其它标投哦！" ; 
		    arr[2] = "" ;  

			return arr;
		}else if(b != 0 && b - d < f){
				arr = new String[3] ; 
				
				   arr[0] = "11" ;  
				   if(d == 0){
					   arr[1] = "此标您最高还可以投资"+new DecimalFormat("0.00").format((b - d))+"元" ; 
				   }else{
					   arr[1] = "此标您已累计投资了"+d+"元，您最高还可以投资"+new DecimalFormat("0.00").format((b - d))+"元" ; 
				   }
			      
			       arr[2] = "" ;  
			       return arr;
				
			}else if(a != f && a - f < g){
				arr = new String[3] ; 
				
				   arr[0] = "12" ;  
				   arr[1] = "您此次的投资金额是："+f+ " 元，将使项目剩余投标金额："+ (a - f)+ "元,小于投资起点金额：" + g + " 元！请全部投资或者减小投资金额。" ; 
			      
			       arr[2] = "" ;  
			       return arr;
			}else{
				arr = new String[3] ; 
				
				   arr[0] = "13" ;  //chenggong
			       arr[1] = "" ; 
			       arr[2] = "" ;  
			       return arr;
			}
			
		
	}
	
	
	/**
	 * 
	 * @param a  可投金额
	 * @param b  总额上限
	 * @param c  账户余额
	 * @param d  已投金额
	 * @param e  递增金额
	 * @param f  投标金额
	 * @param g  起投金额
	 */
	
	public String[] addjisuan(int a,int b,int c,int d,int e,int f,int g){
		   String arr[] = null ; 
		   /**
			 * @param a  可投金额
			 * @param b  总额上限
			 * @param c  账户余额
			 * @param d  已投金额
			 * @param e  递增金额
			 * @param f  投标金额
			 * @param g  起投金额
			 */
		if(c == 0 || c < f || f + e  >  c){
			
			arr = new String[3] ; 
			
			   arr[0] = "0" ;  
		       arr[1] = "老板，余额不足啦，去充值赚钱吧！" ; 
		       arr[2] = "" ;  

			return arr;
		}else if(a == 0){
			arr = new String[3] ; 
			
			   arr[0] = "4" ;  
		       arr[1] = "此标已满额，请换其它标投哦！" ; 
		       arr[2] = "" ;  

			return arr;
		}else if(b != 0 && (f  >  b|| b-d <= f)){
				arr = new String[3] ; 
				
				   arr[0] = "1" ;  
				   if(d == 0){
					   arr[1] = "此标您最多可投资"+new DecimalFormat("0.00").format(b-d)+"元" ; 
				   }else{
					   arr[1] = "您已累计投资了"+d+"元，此标最多可投资"+new DecimalFormat("0.00").format(b-d)+"元" ; 
				   }
			      
			       arr[2] = "" ;  
			       return arr;
				
			}else if(f+e>a){
				arr = new String[3] ; 
				  arr[0] = "2" ;  
				  arr[1] = "此标您最多可投资"+new DecimalFormat("0.00").format(a)+"元" ; 
			      
			       arr[2] = "" ;  
			       return arr;
				
				
			}else{
				arr = new String[3] ; 
				
				   arr[0] = "3" ;  //chenggong
			       arr[1] = "" ; 
			       arr[2] = "" ; 
			       
			       if(String.valueOf(e) != null){
			    	   f = f+e;
			       }else{
			    	   f= f+g;
			       }
			       
			       tv_touzie.setText(f + "");
			       
			       
			       return arr;
			}
			
		
	}
	
	
}

