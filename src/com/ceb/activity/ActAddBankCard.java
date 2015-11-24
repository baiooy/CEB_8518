package com.ceb.activity;

import java.net.URLEncoder;
import java.util.TreeMap;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.caifuline.R;
import com.ceb.activity.ActContract.ContractTask;
import com.ceb.base.BaseActivity;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
import com.model.ActionResultInfo;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;

public class ActAddBankCard extends BaseActivity {
	private WebView wv_addbankcard;
	private SharedPreferences sp;
	private String userID;
	private String loginname;
	UserInfoService _userInfoService;
	private Handler _handler;
	private String requestUrl;
	LoadingDialog dialog;
	private TextView tv_title;

	@Override
	public void setView() {
		setContentView(R.layout.act_addbankcard);

		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		loginname = sp.getString("loginname", "null");
		tv_title = (TextView) findViewById(R.id.tv_title);
		String  signature = "bindcard.php:"+System.currentTimeMillis()/1000+":hxpay";
		
		try {
			requestUrl = SppaConstant.getIPmobilev11()+"bindcard.php?platformID="
					+ SppaConstant.ANDROID
					+"&signature="
					+RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes())
					+ "&loginname="
					+ loginname
					+ "&userID="
					+ userID;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		_userInfoService = new UserInfoService(ActAddBankCard.this);
		// dialog = new LoadingDialog(this);
		// dialog.show();
		_handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(ActAddBankCard.this, "网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
					// wv_contract.loadUrl(requestUrl);

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
	public void initView() {
		wv_addbankcard = (WebView) findViewById(R.id.wv_addbankcard);

		wv_addbankcard.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				wv_addbankcard.requestFocus();
				return false;
			}

		});

		wv_addbankcard.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url); // �ڵ�ǰ��webview����ת���µ�url

				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if (dialog == null) {
					dialog = new LoadingDialog(ActAddBankCard.this);
					dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
//					dialog.show();
//					wv_addbankcard.setEnabled(false);// 当加载网页的时候将网页进行隐藏
				}
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
					wv_addbankcard.setEnabled(true);
				}
				// super.onPageFinished(view, url);
			}

		});

		wv_addbankcard.loadUrl(requestUrl);

		WebSettings webSettings = wv_addbankcard.getSettings();
		webSettings.setBuiltInZoomControls(true);// ����
		webSettings.setJavaScriptEnabled(true);
//		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//		wv_addbankcard.clearCache(true);
		wv_addbankcard.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) 
			{
				super.onProgressChanged(view, newProgress);
			}
			
			@Override
			public void onReceivedTitle(WebView view, String title) 
			{
				super.onReceivedTitle(view, title);
				tv_title.setText(title);
			}
		});
		
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public void onback(View v) {
		finish();
	}

}
