package com.ceb.activity;


import com.caifuline.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class MainnActivity extends Activity {
	WebView webview;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		
		
	//	String html = "<button onClick=\"javascript:window.android.ii()\">哈哈</button>";
	//	String html = "<button id=\"jumpshare\" class=\"btn\" onclick=\"javascript:window.android.ii();\">jumpshare</button>";
		String html = "http://schedule.8518.com/share/share.html";
		webview.loadUrl(html);
	//	webview.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
		webview.addJavascriptInterface(new JsObject(), "android");
		
		
		
	}
	
	class JsObject{
		@JavascriptInterface
		public void share(){
			Log.e("---", "-----");
			Toast.makeText(MainnActivity.this, "share", 1000).show();
	//		MainnActivity.this.finish();
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
