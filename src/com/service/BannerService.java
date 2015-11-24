package com.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.common.BannerWrapper;
import com.common.HttpUtils;
import com.common.BannerWrapper.BannerItem;
import com.common.BaseAuthenicationHttpClient;
import com.json.JSONArray;
import com.json.JSONObject;
import com.model.UserInfo;
import com.spp.SppaConstant;

public class BannerService implements com.common.BannerWrapper.IBannerService {
	static final String SUB_URL = "banner/index";
	Context _context = null;
	ArrayList<BannerItem> _banners = new ArrayList<BannerItem>();
	UserInfo userInfo;
	public BannerService(Context context ,UserInfo u) {
		_context = context;
		userInfo = u;
	}

	public Boolean retrieveBannersInfo() {
		Boolean ret = false;
		try {
			String url = SppaConstant.getIPmobile()+"/app/ceb/qualityproductlist.php?"
					+"&userID="+userInfo.userID
					+"&platformID="+userInfo.platformID
					+"&version="+userInfo.version
					+"&signature="+userInfo.signature
					+"&udid="+userInfo.udid;;//SppaConstant.getIPmobile() + "/" + SUB_URL;

	//		c jsStr = HttpUtils.doGet(url);
					final String jsStr = BaseAuthenicationHttpClient._doRequest(
					_context, url, null);

			
			Log.i("jsStr bannar", "jsStr =="+jsStr);
			JSONObject json = new JSONObject(jsStr);
			JSONObject return_data = null;
			JSONArray banners = null;
			
			return_data = json.getJSONObject("return_data");
			banners = return_data.getJSONArray("banner");
			
			String errno = json.getString("ret");
			ret = errno.equals("0");

			if (ret) {
		//		JSONArray banners = json.getJSONArray("banner");
				for (int i = 0; i < banners.length(); i++) {
					JSONObject obj = banners.getJSONObject(i);

					String img = obj.getString("bannerImg");
					String title = obj.getString("bannerTitle");
					String content = obj.getString("bannerUrl");

					BannerItem b = new BannerItem();
					b.imgURL = img;
					b.title = title;
					b.content = content;

					_banners.add(b);
				}
			}

			return ret;

		} catch (final Exception ex) {
			ex.printStackTrace();
		}

		return ret;
	}

	public int getBannerCount() {
		return _banners.size();
	}

	public String getBannerImageURL(int index) {
		if (index >= _banners.size())
			return "";

		return  _banners.get(index).imgURL;
	}

	public String getBannerImageTitle(int index) {
		if (index >= _banners.size())
			return "";

		return _banners.get(index).title;
	}
	
	public String getBannerImageContent(int index) {
		if (index >= _banners.size())
			return "";

		return _banners.get(index).content;
	}

	
	@Override
	public List<BannerItem> retrieveItems() {
		retrieveBannersInfo();
		return _banners;
	}
}
