package com.ceb.activity;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.TreeMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class ActTouji extends BaseActivity {
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
	String type;

	@Override
	public void setView() {
		setContentView(R.layout.act_touzi);

		isLogin = getSharedPreferences("isLogin", 0);
		islogin = isLogin.getBoolean("islogin", false);

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
		alreadyInvest = in.getStringExtra("alreadyInvest");
		
		maxInvestment = in.getStringExtra("maxInvestment");
		if (accountBalance.equals("null")) {
			accountBalance = "0.00 ";
		}
		minInvestment = in.getStringExtra("minInvestment");
		shouyi = in.getDoubleExtra("shouyi", 0.00);
		mMin = in.getDoubleExtra("mMin", 0.00);

		Log.i("shouyi--mMin", shouyi + "--" + mMin);

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
		
		tv_alreadyInvest.setText(alreadyInvest);
		// Integer.parseInt("12".split("\\.")[0])
		if (Integer.parseInt(maxInvestment.split("\\.")[0]) == 0) {
			maxInvestment = totalMoney;
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
		tv_accountBalance.setText(accountBalance);

		// tv_shouyi.setText("" + "元");
		tv_touzie.setText(minInvestment.split("\\.")[0]);

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
		tv_touzie.setText(minInvestment.split("\\.")[0]);
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
			Log.i("alreadyInvest", tv_maxInvestment.getText().toString()
					+ alreadyInvest.split("\\.")[0]);
			if (tv_touzie.getText().toString().split("\\.")[0].equals("")) {
				Log.i("tv_touzie", yujishouyi
						+ "--tv_touzie");
				new SweetAlertDialog(
						ActTouji.this,
						SweetAlertDialog.ERROR_TYPE)
						.setContentText(
								"请输入投资额!")
						.show();
			} else if (Integer.parseInt(tv_touzie.getText().toString().split("\\.")[0]) > Integer
					.parseInt(tv_accountBalance.getText().toString().split("\\.")[0])){
				new SweetAlertDialog(
						ActTouji.this,
						SweetAlertDialog.ERROR_TYPE)
						.setContentText(
								"账户余额不足，请先充值!")
						.show();
			}else if(touzie > Integer
							.parseInt(maxInvestment
									.split("\\.")[0])
					|| touzie > Integer
							.parseInt(totalMoney
									.split("\\.")[0])) {
				Log.i("tv_touzie", yujishouyi
						+ "--tv_touzie2");
				new SweetAlertDialog(
						ActTouji.this,
						SweetAlertDialog.ERROR_TYPE)
						.setContentText(
								"超出投资上限!")
						.show();
				}else if (Integer.parseInt(totalMoney.split("\\.")[0]) != Integer
					.parseInt(tv_touzie.getText().toString().split("\\.")[0])
					&& Integer.parseInt(totalMoney.split("\\.")[0])
							- Integer.parseInt(tv_touzie.getText().toString()
									.split("\\.")[0]) < Integer
								.parseInt(minInvestment.split("\\.")[0])) {// 投完这个标剩余可投金额小于起投金额
				new SweetAlertDialog(ActTouji.this,
						SweetAlertDialog.WARNING_TYPE)
						.setContentText(
								"您此次的投资金额是："
										+ tv_touzie.getText().toString()
										+ " 元，将使项目剩余金额："
										+ (Integer.parseInt(totalMoney
												.split("\\.")[0])
												- Integer.parseInt(tv_touzie
														.getText().toString()
														.split("\\.")[0])
												+ "元,小于投资起点金额：" + minInvestment + " 元！请全部投资或者减小投资金额。"))
						.show();
			} else if(SppaConstant.isNumeric(tv_maxInvestment.getText().toString()) &&
					Integer.parseInt(tv_maxInvestment.getText().toString().split("\\.")[0]) - Integer.parseInt(alreadyInvest.split("\\.")[0]) <= 0
					|| SppaConstant.isNumeric(tv_maxInvestment.getText().toString()) && Integer.parseInt(tv_touzie.getText().toString().split("\\.")[0]) > (Integer.parseInt(tv_maxInvestment.getText().toString().split("\\.")[0]) - Integer.parseInt(alreadyInvest.split("\\.")[0]))){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
				.setContentText("此标您已累计投资了"+alreadyInvest+"元，您最高还可投资"+(Integer.parseInt(tv_maxInvestment.getText().toString().split("\\.")[0])-Integer.parseInt(alreadyInvest.split("\\.")[0]))+"元")
				.show();
			}else if (!borrowPass.equals("")) {
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
											touzie = Integer.parseInt(tv_touzie
													.getText().toString()
													.split("\\.")[0]);
											if (!islogin) {// 判断是否登陆
												Log.i("islogin", yujishouyi
														+ "--islogin");
												Intent in = new Intent(
														ActTouji.this,
														ActUserYanzheng.class);
												startActivity(in);
												overridePendingTransition(
														android.R.anim.fade_in,
														android.R.anim.fade_out);
											} else if (!isopen) {// 判断是否汇付开户
												Log.i("isopen", yujishouyi
														+ "--isopen");
												Intent in = new Intent(
														ActTouji.this,
														ActHuifu.class);
												startActivity(in);
												overridePendingTransition(
														android.R.anim.fade_in,
														android.R.anim.fade_out);
											
											} else {
												// Log.i("else", yujishouyi +
												// "--else");
												// Intent in = new
												// Intent(ActTouzi.this,
												// ActTouziHuifu.class);
												// in.putExtra("money",
												// tv_touzie.getText().toString());//
												// productID
												// in.putExtra("productID",
												// productID);
												// startActivity(in);
												// overridePendingTransition(android.R.anim.fade_in,
												// android.R.anim.fade_out);

												touzie = Integer
														.parseInt(tv_touzie
																.getText()
																.toString()
																.split("\\.")[0]);
												Log.i("aasas",
														""
																+ Integer
																		.parseInt(totalMoney
																				.split("\\.")[0])
																+ Integer
																		.parseInt(tv_touzie
																				.getText()
																				.toString()
																				.split("\\.")[0])
																+ Integer
																		.parseInt(minInvestment
																				.split("\\.")[0]));
												if (!islogin) {// 判断是否登陆
													Log.i("islogin", yujishouyi
															+ "--islogin");
													Intent in = new Intent(
															ActTouji.this,
															ActUserYanzheng.class);
													startActivity(in);
													overridePendingTransition(
															android.R.anim.fade_in,
															android.R.anim.fade_out);
												} else if (!isopen) {// 判断是否汇付开户
													Log.i("isopen", yujishouyi
															+ "--isopen");
													Intent in = new Intent(
															ActTouji.this,
															ActHuifu.class);
													startActivity(in);
													overridePendingTransition(
															android.R.anim.fade_in,
															android.R.anim.fade_out);
												}/*
												 * else
												 * if(!opencard){//判断是否绑定银行卡
												 * Log.i("opencard", yujishouyi
												 * + "--opencard"); Intent in =
												 * new Intent(ActTouzi.this,
												 * ActAddBankCard.class);
												 * startActivity(in);
												 * overridePendingTransition
												 * (android.R.anim.fade_in,
												 * android.R.anim.fade_out); }
												 */else if (tv_touzie.getText()
														.toString().equals("")) {
													Log.i("tv_touzie",
															yujishouyi
																	+ "--tv_touzie");
													new SweetAlertDialog(
															ActTouji.this,
															SweetAlertDialog.ERROR_TYPE)
															.setTitleText(
																	"Oops...")
															.setContentText(
																	"请输入投资额!")
															.show();
												} else if (touzie > Integer
														.parseInt(tv_accountBalance
																.getText()
																.toString()
																.split("\\.")[0])
														|| touzie > Integer
																.parseInt(maxInvestment
																		.split("\\.")[0])
														|| touzie > Integer
																.parseInt(totalMoney
																		.split("\\.")[0])) {
													Log.i("tv_touzie",
															yujishouyi
																	+ "--tv_touzie2");
													new SweetAlertDialog(
															ActTouji.this,
															SweetAlertDialog.ERROR_TYPE)
															.setTitleText(
																	"Oops...")
															.setContentText(
																	"超出投资上限!")
															.show();
												} else {
													Log.i("else", yujishouyi
															+ "--else");
													Intent in = new Intent(
															ActTouji.this,
															ActTouziHuifu.class);
													in.putExtra("money",
															tv_touzie.getText()
																	.toString());// productID
													in.putExtra("productID",
															productID);
													startActivity(in);
													overridePendingTransition(
															android.R.anim.fade_in,
															android.R.anim.fade_out);
												}

											}

										} else {
											Toast.makeText(ActTouji.this,
													"抢购码不正确", 1000).show();
										}

									}
								})

						.setNegativeButton("取消", null).show();
			} else {
				touzie = Integer.parseInt(tv_touzie
						.getText().toString()
						.split("\\.")[0]);
				if (!islogin) {// 判断是否登陆
					Log.i("islogin", yujishouyi
							+ "--islogin");
					Intent in = new Intent(
							ActTouji.this,
							ActUserYanzheng.class);
					startActivity(in);
					overridePendingTransition(
							android.R.anim.fade_in,
							android.R.anim.fade_out);
				} else if (!isopen) {// 判断是否汇付开户
					Log.i("isopen", yujishouyi
							+ "--isopen");
					Intent in = new Intent(
							ActTouji.this,
							ActHuifu.class);
					startActivity(in);
					overridePendingTransition(
							android.R.anim.fade_in,
							android.R.anim.fade_out);
				}else if (tv_touzie.getText()
						.toString().equals("")) {
					Log.i("tv_touzie", yujishouyi
							+ "--tv_touzie");
					new SweetAlertDialog(
							ActTouji.this,
							SweetAlertDialog.ERROR_TYPE)
							.setTitleText("Oops...")
							.setContentText(
									"请输入投资额!")
							.show();
				} else if (touzie > Integer
						.parseInt(tv_accountBalance
								.getText()
								.toString()
								.split("\\.")[0])
						|| touzie > Integer
								.parseInt(maxInvestment
										.split("\\.")[0])
						|| touzie > Integer
								.parseInt(totalMoney
										.split("\\.")[0])) {
					Log.i("tv_touzie", yujishouyi
							+ "--tv_touzie2");
					new SweetAlertDialog(
							ActTouji.this,
							SweetAlertDialog.ERROR_TYPE)
							.setTitleText("Oops...")
							.setContentText(
									"超出投资上限!")
							.show();
				} else {
					// Log.i("else", yujishouyi +
					// "--else");
					// Intent in = new
					// Intent(ActTouzi.this,
					// ActTouziHuifu.class);
					// in.putExtra("money",
					// tv_touzie.getText().toString());//
					// productID
					// in.putExtra("productID",
					// productID);
					// startActivity(in);
					// overridePendingTransition(android.R.anim.fade_in,
					// android.R.anim.fade_out);

					touzie = Integer
							.parseInt(tv_touzie
									.getText()
									.toString()
									.split("\\.")[0]);
					Log.i("aasas",
							""
									+ Integer
											.parseInt(totalMoney
													.split("\\.")[0])
									+ Integer
											.parseInt(tv_touzie
													.getText()
													.toString()
													.split("\\.")[0])
									+ Integer
											.parseInt(minInvestment
													.split("\\.")[0]));
					if (!islogin) {// 判断是否登陆
						Log.i("islogin", yujishouyi
								+ "--islogin");
						Intent in = new Intent(
								ActTouji.this,
								ActUserYanzheng.class);
						startActivity(in);
						overridePendingTransition(
								android.R.anim.fade_in,
								android.R.anim.fade_out);
					} else if (!isopen) {// 判断是否汇付开户
						Log.i("isopen", yujishouyi
								+ "--isopen");
						Intent in = new Intent(
								ActTouji.this,
								ActHuifu.class);
						startActivity(in);
						overridePendingTransition(
								android.R.anim.fade_in,
								android.R.anim.fade_out);
					}/*
					 * else
					 * if(!opencard){//判断是否绑定银行卡
					 * Log.i("opencard", yujishouyi
					 * + "--opencard"); Intent in =
					 * new Intent(ActTouzi.this,
					 * ActAddBankCard.class);
					 * startActivity(in);
					 * overridePendingTransition
					 * (android.R.anim.fade_in,
					 * android.R.anim.fade_out); }
					 */else if (tv_touzie.getText()
							.toString().equals("")) {
						Log.i("tv_touzie",
								yujishouyi
										+ "--tv_touzie");
						new SweetAlertDialog(
								ActTouji.this,
								SweetAlertDialog.ERROR_TYPE)
								.setTitleText(
										"Oops...")
								.setContentText(
										"请输入投资额!")
								.show();
					} else if (touzie > Integer
							.parseInt(tv_accountBalance
									.getText()
									.toString()
									.split("\\.")[0])
							|| touzie > Integer
									.parseInt(maxInvestment
											.split("\\.")[0])
							|| touzie > Integer
									.parseInt(totalMoney
											.split("\\.")[0])) {
						Log.i("tv_touzie",
								yujishouyi
										+ "--tv_touzie2");
						new SweetAlertDialog(
								ActTouji.this,
								SweetAlertDialog.ERROR_TYPE)
								.setTitleText(
										"Oops...")
								.setContentText(
										"超出投资上限!")
								.show();
					} else {
						Log.i("else", yujishouyi
								+ "--else");
						Intent in = new Intent(
								ActTouji.this,
								ActTouziHuifu.class);
						in.putExtra("money",
								tv_touzie.getText()
										.toString());// productID
						in.putExtra("productID",
								productID);
						startActivity(in);
						overridePendingTransition(
								android.R.anim.fade_in,
								android.R.anim.fade_out);
					}

				}
			}

			break;
		case R.id.btn_add:
			Log.i("tv_touzie.getText().toString()", tv_touzie.getText()
					.toString());

			touzie = Integer.parseInt(tv_touzie.getText().toString()
					.split("\\.")[0]);

			if (Integer.parseInt(tv_touzie.getText().toString()
					.split("\\.")[0])+Integer.parseInt(increaseMoney.split("\\.")[0]) > Integer.parseInt(tv_accountBalance.getText()
					.toString().split("\\.")[0])) {
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
						.setTitleText("Oops...").setContentText("账户余额不足，请先充值!")
						.show();
			} else if (Integer.parseInt(tv_touzie.getText().toString()
					.split("\\.")[0]) >= Integer
					.parseInt(maxInvestment.split("\\.")[0])
					|| touzie >= Integer.parseInt(totalMoney.split("\\.")[0])) {
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
						.setTitleText("Oops...").setContentText("此标最多可投资"+tv_maxInvestment.getText().toString().split("\\.")[0]+"元。")
						.show();
			}else if(SppaConstant.isNumeric(tv_maxInvestment.getText().toString()) &&
					Integer.parseInt(tv_maxInvestment.getText().toString().split("\\.")[0]) - Integer.parseInt(alreadyInvest.split("\\.")[0]) <= 0){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
				.setTitleText("Oops...").setContentText("此标您已累计投资了"+alreadyInvest+"元，您最高还可投资"+(Integer.parseInt(tv_maxInvestment.getText().toString().split("\\.")[0])-Integer.parseInt(alreadyInvest.split("\\.")[0]))+"元")
				.show();
			} else {
				if (increaseMoney != null) {
					touzie = touzie
							+ Integer.parseInt(increaseMoney.split("\\.")[0]);
				} else {
					touzie = touzie
							+ Integer.parseInt(minInvestment.split("\\.")[0]);
				}

				tv_touzie.setText(touzie + "");
				btn_subtract.setEnabled(true);
				btn_subtract.setBackgroundResource(R.drawable.jian);

				yujishouyi = Integer.parseInt(tv_touzie.getText().toString()
						.split("\\.")[0])
						* shouyi / borrowMoney;
				tv_shouyi.setText(new DecimalFormat("0.00").format(yujishouyi)
						+ "元");
			}
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
			Intent ii = new Intent(ActTouji.this, ActAgreement.class);
			ii.putExtra("type", type);
			startActivity(ii);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			break;
		case R.id.btn_chongzhi:
			Intent in = new Intent(ActTouji.this, ActRecharge.class);
			in.putExtra("money", tv_accountBalance.getText().toString());// productID
			startActivity(in);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			break;
		case R.id.btn_quantou:
		   if(SppaConstant.isNumeric(tv_maxInvestment.getText().toString()) &&
					Integer.parseInt(tv_maxInvestment.getText().toString().split("\\.")[0]) - Integer.parseInt(alreadyInvest.split("\\.")[0]) <= 0){
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
				.setTitleText("Oops...").setContentText("此标您已累计投资了"+alreadyInvest+"元，您最高还可投资"+(Integer.parseInt(tv_maxInvestment.getText().toString().split("\\.")[0])-Integer.parseInt(alreadyInvest.split("\\.")[0]))+"元")
				.show();
			}else if (Double.parseDouble(totalMoney) < Double.parseDouble(tv_touzie
					.getText().toString())
					|| Integer.parseInt(maxInvestment.split("\\.")[0]) < Double
							.parseDouble(tv_touzie.getText().toString())/*
																		 * ||Double
																		 * .
																		 * parseDouble
																		 * (
																		 * totalMoney
																		 * ) <
																		 * Double
																		 * .
																		 * parseDouble
																		 * (
																		 * accountBalance
																		 * )
																		 */) {
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
						.setTitleText("Oops...").setContentText("超出投资上限!")
						.show();
			}else if (Double.parseDouble(tv_accountBalance.getText()
					.toString()) < Double.parseDouble(tv_touzie.getText()
					.toString())
					|| Double.parseDouble(totalMoney) < Double
							.parseDouble(tv_touzie.getText().toString())
					|| Integer.parseInt(maxInvestment.split("\\.")[0]) < Double
							.parseDouble(tv_touzie.getText().toString())) {
				new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
						.setTitleText("Oops...").setContentText("账户余额不足，请去充值!")
						.show();
			} else if(SppaConstant.isNumeric(tv_maxInvestment.getText().toString()) && Integer.parseInt(tv_accountBalance.getText().toString().split("\\.")[0])%Integer.parseInt(increaseMoney.split("\\.")[0]) != 0){
				Log.i("区域", Integer.parseInt(tv_accountBalance.getText().toString().split("\\.")[0])+"////"+Integer.parseInt(tv_accountBalance.getText().toString().split("\\.")[0])%Integer.parseInt(increaseMoney.split("\\.")[0])+"");
				int ab = Integer.parseInt(tv_accountBalance.getText().toString().split("\\.")[0]) - Integer.parseInt(tv_accountBalance.getText().toString().split("\\.")[0])%Integer.parseInt(increaseMoney.split("\\.")[0]);
				
				double aa = Math.min(Double.parseDouble(totalMoney.split("\\.")[0]),
						Double.parseDouble(tv_maxInvestment.getText().toString().split("\\.")[0]));
				if (ab < aa) {
					tv_touzie.setText(ab+"");
				} else {
					tv_touzie.setText(String.valueOf((int) aa));
				}
				
//				if(ab>Integer.parseInt(totalMoney.split("\\.")[0])){
//					tv_touzie.setText(String.valueOf(Integer.parseInt(totalMoney.split("\\.")[0])));
//				}else{
//					tv_touzie.setText(String.valueOf((int) ab));
//				}
				
				
				  yujishouyi = Integer.parseInt(tv_touzie.getText().toString()
							.split("\\.")[0])
							* shouyi / borrowMoney;
					tv_shouyi.setText(new DecimalFormat("0.00").format(yujishouyi)
							+ "元");
					btn_subtract.setEnabled(true);
			}else if(SppaConstant.isNumeric(tv_maxInvestment.getText().toString())){
				double aa = Math.min(Double.parseDouble(totalMoney.split("\\.")[0]),
						Double.parseDouble(tv_maxInvestment.getText().toString().split("\\.")[0]));
				if (Double.parseDouble(tv_accountBalance.getText().toString()) < aa) {
					tv_touzie.setText(tv_accountBalance.getText().toString()
							.split("\\.")[0]);
				} else {
					tv_touzie.setText(String.valueOf((int) aa));
				}
				// tv_touzie.setText(accountBalance);
				yujishouyi = Integer.parseInt(tv_touzie.getText().toString()
						.split("\\.")[0])
						* shouyi /borrowMoney;// mMin;
				tv_shouyi.setText(new DecimalFormat("0.00").format(yujishouyi)
						+ "元");
				btn_subtract.setEnabled(true);
			}else{
				
				double aa = Math.min(Double.parseDouble(totalMoney.split("\\.")[0]),
						Double.parseDouble(tv_accountBalance.getText().toString()));
				
				tv_touzie.setText(String.valueOf((int) aa));
				// tv_touzie.setText(accountBalance);
				yujishouyi = Integer.parseInt(tv_touzie.getText().toString()
						.split("\\.")[0])
						* shouyi /borrowMoney;// mMin;
				tv_shouyi.setText(new DecimalFormat("0.00").format(yujishouyi)
						+ "元");
				btn_subtract.setEnabled(true);
				
			}
		   
		 
			break;
		default:
			break;
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

				if (!result.availableBalance.equals("null")) {
					tv_accountBalance.setText(result.availableBalance);
				} else {
					tv_accountBalance.setText("0.00");
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

}
