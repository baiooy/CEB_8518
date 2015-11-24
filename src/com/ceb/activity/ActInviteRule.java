package com.ceb.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.caifuline.R;
import com.ceb.base.BaseActivity;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.loadingdialog.LoadingDialog;
import com.model.ActionResultInfo;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;

@SuppressLint("SetJavaScriptEnabled")
public class ActInviteRule extends BaseActivity {
	private WebView wv_invite;
	private String requestUrl = null;
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	private Handler _handler;
	LoadingDialog dialog;
	private TextView tv_title;
	@Override
	public void setView() {
		setContentView(R.layout.act_inviterule);

		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		_userInfoService = new UserInfoService(this);

		_handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(ActInviteRule.this, "网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
					wv_invite.loadUrl(requestUrl);

					if (dialog != null & dialog.isShowing())
						dialog.dismiss();
					wv_invite.setWebChromeClient(new WebChromeClient() {
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
			about();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void initView() {
		wv_invite = (WebView) findViewById(R.id.wv_invite);
		tv_title = (TextView) findViewById(R.id.tv_title);
		
		wv_invite.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				wv_invite.requestFocus();
				return false;
			}

		});

		wv_invite.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url); //

				return true;
			}
		});
		
		WebSettings webSettings = wv_invite.getSettings();
		webSettings.setBuiltInZoomControls(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setBuiltInZoomControls(true);
		wv_invite.requestFocus();
		webSettings.setLoadWithOverviewMode(true);

		

	}
	
	@Override
	public boolean onKeyDown(int keyCoder,KeyEvent event){
        if(wv_invite.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK){
        	wv_invite.goBack();   

                return true;
        }else if(keyCoder == KeyEvent.KEYCODE_BACK){
        	ActInviteRule.this.finish();//按了返回键，但已经不能返回(mWebView.canGoBack()=false)，则执行退出确认
        	return true; 
        }  
        return false;
}

	void about(){
		String  signature = "inviterule.php:"+System.currentTimeMillis()/1000+":hxpay";
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
		
		
		new AboutTask().execute(userInfo);
	}
	
	class AboutTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{

		@Override
		protected void onPreExecute() {
			dialog = new LoadingDialog(ActInviteRule.this);
			dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
			dialog.show();
		}
		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.getInviteRule(params[0]);
			
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
				Toast.makeText(ActInviteRule.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	
	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
