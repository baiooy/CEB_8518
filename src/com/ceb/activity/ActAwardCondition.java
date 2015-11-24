package com.ceb.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.caifuline.R;
import com.ceb.activity.ActChongzhiHuifu.WebTOANDROIDInterface;
import com.ceb.activity.ActInviteRule.AboutTask;
import com.ceb.base.BaseActivity;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.loadingdialog.LoadingDialog;
import com.model.ActionResultInfo;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;

public class ActAwardCondition extends BaseActivity {
	private WebView webView_award;
	private String requestUrl;
	private SharedPreferences sp;
	private String loginname,userID,productID;
	private TextView tv_title;
	UserInfoService _userInfoService;
	private Handler _handler;
	LoadingDialog dialog;
	@Override
	public void setView() {
		setContentView(R.layout.act_awardcondition);

		sp = getSharedPreferences("USERID", 0);
		loginname = sp.getString("loginname", "null");
		userID = sp.getString("userID", "0");
		productID = getIntent().getStringExtra("productID");
		requestUrl = SppaConstant.getIPmobilev11()+"productawardinfo.php?" +
				"platformID="+SppaConstant.ANDROID+"&userID="+userID+"&productID="+productID;
		Log.i("Aeard_requestUrl", requestUrl);
		
		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		_userInfoService = new UserInfoService(this);

		_handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(ActAwardCondition.this, "网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
					webView_award.loadUrl(requestUrl);

					if (dialog != null & dialog.isShowing())
						dialog.dismiss();
					webView_award.setWebChromeClient(new WebChromeClient() {
//						@Override
//						public void onProgressChanged(WebView view, int newProgress) 
//						{
//							if(newProgress > 70){
//								if(dialog != null & dialog.isShowing())
//									dialog.dismiss();
//							}
//							super.onProgressChanged(view, newProgress);
//						}
						
						@Override
						public void onReceivedTitle(WebView view, String title) 
						{
							super.onReceivedTitle(view, title);
							tv_title.setText(title);
						}
					});
					break;
				default:
					break;
				}
			};
		};
		
		try {
			about();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initView() {
		webView_award = (WebView) findViewById(R.id.webView_award);
		tv_title = (TextView) findViewById(R.id.tv_title);
		webView_award.setOnTouchListener(new View.OnTouchListener() {  
		    @Override  
		    public boolean onTouch(View v, MotionEvent event) {  
		    	webView_award.requestFocus();  
		         return false;  
		    }
 
		});  
		
		
		webView_award.setWebViewClient(new WebViewClient()
	    {
	      @Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url)
	      {

	    	view.loadUrl(url); 
	        return true;
	      }
	      
	      
	    });
		
		
		
		WebSettings webSettings = webView_award.getSettings();
		webSettings.setBuiltInZoomControls(true);//支持缩放
		webSettings.setJavaScriptEnabled(true);
//		webView_award.loadUrl(requestUrl);
		webView_award.setWebChromeClient(new WebChromeClient(){
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
	
	void about(){
		String  signature = "productawardinfo.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.productID = productID;
		userInfo.udid = SppaConstant.getDeviceInfo(this);
		
		
		new AboutTask().execute(userInfo);
	}
	
	class AboutTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{

		@Override
		protected void onPreExecute() {
			dialog = new LoadingDialog(ActAwardCondition.this);
			dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
			dialog.show();
		}
		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.getAwardRule(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result == null){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0")){
				
				requestUrl = result.url;
				
				_handler.sendEmptyMessage(0);
			}else{
				Toast.makeText(ActAwardCondition.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}


}
