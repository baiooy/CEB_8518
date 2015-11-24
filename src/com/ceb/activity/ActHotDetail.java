package com.ceb.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import com.caifuline.R;
import com.ceb.base.BaseActivity;
import com.json.JSONException;
import com.json.JSONObject;
import com.loadingdialog.LoadingDialog;
import com.umeng.analytics.MobclickAgent;
import com.view.BgView;

public class ActHotDetail extends BaseActivity {
	private WebView mWebView;
	private String requestUrl;
	private TextView tv_title;
	private LoadingDialog dialog;
	private SharedPreferences sp;
	private String userID;
	private String testImage;
	private static final String FILE_NAME = "/pic_lovely_cats.jpg";
	@Override
	public void setView() {
		setContentView(R.layout.act_hotdetail);

		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		MobclickAgent.setDebugMode(true);
		Intent in = getIntent();
		requestUrl = in.getStringExtra("jumpUrl");
		Log.i("requestUrl", "requestUrl="+requestUrl);
		
		new Thread() {
			public void run() {
				initImagePath();
			}
		}.start();
	}

	@Override
	public void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		mWebView = (WebView) findViewById(R.id.wv_hot);
		
		dialog = new LoadingDialog(ActHotDetail.this);
		dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
		dialog.show();
		
		mWebView.setOnTouchListener(new View.OnTouchListener() {  
		    @Override  
		    public boolean onTouch(View v, MotionEvent event) {  
		    	mWebView.requestFocus();  
		         return false;  
		    }
 
		});  
		
		
		mWebView.setWebViewClient(new WebViewClient()
	    {
	      @Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url)
	      {

	        view.loadUrl(url); // �ڵ�ǰ��webview����ת���µ�url

	        return true;
	      }
	    });
		
		
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setBuiltInZoomControls(true);//����
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		//允许执行javascript语句
		webSettings.setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new WebTOANDROIDInterface(), "android");
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		mWebView.clearCache(true);  
		if(requestUrl != null)
			mWebView.loadUrl(requestUrl);
		//	mWebView.loadUrl(" file:///android_asset/json.html");
		
		mWebView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) 
			{
				if(newProgress > 70){
					if(dialog != null & dialog.isShowing())
						dialog.dismiss();
				}
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

	public class WebTOANDROIDInterface{
		@JavascriptInterface
		public void share(String json) { 
			showShare( json);
		}
		@JavascriptInterface
		public void close() { 
			ActHotDetail.this.finish();
		}
		
	}
		private void initImagePath() {
			try {
				String cachePath = com.mob.tools.utils.R.getCachePath(this, null);
				testImage = cachePath + FILE_NAME;
				File file = new File(testImage);
				if (!file.exists()) {
					file.createNewFile();
				//	new BgView(url, imageView)
					Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.ceb_icon);
					FileOutputStream fos = new FileOutputStream(file);
					pic.compress(CompressFormat.JPEG, 100, fos);
					fos.flush();
					fos.close();
				}
			} catch(Throwable t) {
				t.printStackTrace();
				testImage = null;
			}
		}
		
	
	@Override
	public boolean onKeyDown(int keyCoder,KeyEvent event){
        if(mWebView.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK){
        	mWebView.goBack();   

                return true;
        }else if(keyCoder == KeyEvent.KEYCODE_BACK){
        	ActHotDetail.this.finish();//按了返回键，但已经不能返回(mWebView.canGoBack()=false)，则执行退出确认
        	return true; 
        }  
        return false;
}
	
	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}
	
	private void showShare(String jso) {
		OnekeyShare oks = new OnekeyShare();
		try {
			JSONObject json = new JSONObject(jso);
			String title = json.getString("title");
			String content = json.getString("content");
			String imgurl = json.getString("imgurl");
			String htmlurl = json.getString("htmlurl");
			oks.setText(content);
			oks.setTitle(title);
			oks.setTitleUrl(htmlurl);
			oks.setImageUrl(imgurl);
	//		oks.setImagePath(imgurl);
			oks.setUrl(htmlurl);
			Log.i("showShare", jso+title+content+imgurl);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		
		// oks.setText(getString(R.string.share_content));
//		oks.setImagePath(testImage);
		
		
//		oks.setFilePath(testImage);
		oks.setComment(getString(R.string.share));
		oks.setSite(getString(R.string.app_name));
		oks.setSiteUrl("http://bao.8518.com");
		oks.setVenueName("ShareSDK");
//		// text是分享文本，所有平台都需要这个字段
//				oks.setText("ceb8518");
		oks.setVenueDescription("This is a beautiful place!");
		oks.setLatitude(23.056081f);
		oks.setLongitude(113.385708f);
		oks.disableSSOWhenAuthorize();
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
			public void onShare(Platform platform, ShareParams paramsToShare) {
				// 改写twitter分享内容中的text字段，否则会超长，
				// 因为twitter会将图片地址当作文本的一部分去计算长度
				if ("Twitter".equals(platform.getName())) {
				}
			}
		});
		// //关闭sso授权
		// oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		// oks.setTitleUrl("http://sharesdk.cn");
		
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		// oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用

		// 启动分享GUI
		oks.show(this);
	}

	@Override
	protected void onResume() {
		/**
		 * arg0  -- Context 
		 * arg1  -- id 为事件ID 
		 * arg2  -- map 为当前事件的属性和取值 
		 * arg3  -- du 为当前事件的数值，取值范围是-2,147,483,648 到 +2,147,483,647 之间的有符号整数，
		 * 			即int 32整型，如果du数据值超过该范围，会造成数据丢包，影响数据统计的准确性。 
		 */
//		 Map<String, String> map_value = new HashMap<String, String>();
//	//	   map_value.put("count", "BannerDetail");
//		   map_value.put("userID", userID);
//		   MobclickAgent.onEventValue(this, "Banner", map_value, 12000);
////		   MobclickAgent.onEvent(this, "banner");
		   HashMap<String,String> map = new HashMap<String,String>();
		   map.put("userID",userID);
		   MobclickAgent.onEvent(this, "HotActivity", map);  
		super.onResume();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	public void onback(View v){
		finish();
	}

}
