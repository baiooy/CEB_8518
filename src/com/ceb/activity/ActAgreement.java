package com.ceb.activity;

import java.net.URLEncoder;
import java.util.TreeMap;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import com.loadingdialog.LoadingDialog;
import com.model.ActionResultInfo;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;

public class ActAgreement extends BaseActivity {
	private WebView wv_agreement;
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	private Handler _handler;
	private String requestUrl;
	String type;
	LoadingDialog dialog;
	@Override
	public void setView() {
		setContentView(R.layout.act_agreement);

		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		
		type = getIntent().getStringExtra("type");
		
		_userInfoService = new UserInfoService(ActAgreement.this);
		
		_handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(ActAgreement.this,
							"网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
					wv_agreement.loadUrl(requestUrl);
					
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
			getContract();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initView() {
		wv_agreement = (WebView) findViewById(R.id.wv_agreement);
		
		wv_agreement.setOnTouchListener(new View.OnTouchListener() {  
		    @Override  
		    public boolean onTouch(View v, MotionEvent event) {  
		    	wv_agreement.requestFocus();  
		         return false;  
		    }
 
		});  
		
		
		wv_agreement.setWebViewClient(new WebViewClient()
	    {
	      @Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url)
	      {

	        view.loadUrl(url); // �ڵ�ǰ��webview����ת���µ�url

	        return true;
	      }
	      
	      @Override
	    	public void onPageStarted(WebView view, String url, Bitmap favicon) {
	    	  if (dialog == null) {
	    		  dialog=new LoadingDialog(ActAgreement.this);
	    		  dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
	    		  dialog.show();
	    		  wv_agreement.setEnabled(false);// 当加载网页的时候将网页进行隐藏
	    		  }
	    		super.onPageStarted(view, url, favicon);
	    	}
	      
	      @Override
	    	public void onPageFinished(WebView view, String url) {
	    	  if (dialog != null && dialog.isShowing()) {
	    		  dialog.dismiss();
	    		  dialog = null;
	    		  wv_agreement.setEnabled(true);
	    		  }
	    //		super.onPageFinished(view, url);
	    	}
	    });
		
		WebSettings webSettings = wv_agreement.getSettings();
		webSettings.setBuiltInZoomControls(true);//����
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		wv_agreement.clearCache(true);  
	}

	@Override
	public void setListener() {

	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public void onback(View v){
		finish();
	}
	
	void getContract(){
		String  signature = "agreement.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.type = type;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(ActAgreement.this);

		new ContractTask().execute(userInfo);
	}
    
	class ContractTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{

		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.getAgreement(params[0]);
			
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
				Toast.makeText(ActAgreement.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
}
