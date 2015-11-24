package com.ceb.activity;

import java.net.URLEncoder;
import java.util.TreeMap;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.caifuline.R;
import com.ceb.activity.ActContract.ContractTask;
import com.ceb.base.BaseActivity;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.model.ActionResultInfo;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;

public class ActTixianRule extends BaseActivity {
	private WebView wv_tixianrule;
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	private Handler _handler;
	private String requestUrl;

	@Override
	public void setView() {
		setContentView(R.layout.act_tixianrule);

		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		
		_userInfoService = new UserInfoService(ActTixianRule.this);
		
		_handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(ActTixianRule.this,
							"网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
					wv_tixianrule.loadUrl(requestUrl);
					
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
		
		try {
			getTixianrule();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initView() {
		wv_tixianrule = (WebView) findViewById(R.id.wv_tixianrule);

		wv_tixianrule.setOnTouchListener(new View.OnTouchListener() {  
		    @Override  
		    public boolean onTouch(View v, MotionEvent event) {  
		    	wv_tixianrule.requestFocus();  
		         return false;  
		    }
 
		});  
		
		
		wv_tixianrule.setWebViewClient(new WebViewClient()
	    {
	      @Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url)
	      {

	        view.loadUrl(url); // �ڵ�ǰ��webview����ת���µ�url

	        return true;
	      }
	    });
		
		WebSettings webSettings = wv_tixianrule.getSettings();
		webSettings.setBuiltInZoomControls(true);//����
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		wv_tixianrule.clearCache(true);  
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	
	public void onback(View v){
		finish();
	}
	
	void getTixianrule(){
		String  signature = "drawmoneyrule.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(ActTixianRule.this);

		new TixianRuleTask().execute(userInfo);
	}
    
	class TixianRuleTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{

		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.getTixianRule(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result == null || result.equals(null)){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0")){
				requestUrl = result.url;
					
				_handler.sendEmptyMessage(0);
			}else{
				Toast.makeText(ActTixianRule.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
}
