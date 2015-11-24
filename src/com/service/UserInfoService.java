package com.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.common.BaseAuthenicationHttpClient;
import com.common.FileUtil;
import com.common.FormFile;
import com.common.HttpUtils;
import com.google.gson.JsonArray;
import com.json.JSONArray;
import com.json.JSONException;
import com.json.JSONObject;
import com.model.ActionResultInfo;
import com.model.ActivityList;
import com.model.Activity_item;
import com.model.BankCardList;
import com.model.BankCard_item;
import com.model.BannerArray;
import com.model.BannerInfo;
import com.model.Banner_item;
import com.model.Brokerage;
import com.model.CebDetail;
import com.model.HuikuanList;
import com.model.Huikuan_item;
import com.model.InvestmentList_item;
import com.model.InvestplanList;
import com.model.Investplan_item;
import com.model.InviteList;
import com.model.List_item;
import com.model.MainArray;
import com.model.MessageArray;
import com.model.Message_item;
import com.model.NoPartner;
import com.model.NoticeArray;
import com.model.Notice_item;
import com.model.Points;
import com.model.ProductArray;
import com.model.ProductList_item;
import com.model.RepaymentList_item;
import com.model.TradeDetailList;
import com.model.TradeDetail_item;
import com.model.UserInfo;
import com.model.YongjinList;
import com.model.Yongjin_item;
import com.spp.SppaConstant;


import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

public class UserInfoService {

	static final String SUB_URL_UPDATE = "/user/update";
	public static final String META_MESSAGE = "message";
	public static final String META_CODE = "ret";
	private Context _context;
	private String _udid;
//	ActionResultInfo detail = null;
	public UserInfoService(Context c) {
		_context = c;
		TelephonyManager tm = (TelephonyManager) _context
				.getSystemService(Context.TELEPHONY_SERVICE);
		_udid = tm.getDeviceId();
	}
	
	public BannerArray startup(UserInfo userInfo) {

		BannerArray detail = null;

		try {
			String url = SppaConstant.getIPmobilev11()+"startup.php?";
			String uu = "channel_id="+userInfo.channel_id+"&user_id="+userInfo.user_id+"&userID="+userInfo.userID+"&platformID="+userInfo.platformID+"&version="+userInfo.version+"&signature="+userInfo.signature+"&udid="+userInfo.udid;
			Log.i("百度推送", url+uu);
			
			Map<String, String> params = new HashMap<String, String>();

			params.put("signature", userInfo.signature);
			params.put("version", userInfo.version);
			params.put("userID", userInfo.userID);
			params.put("platformID", userInfo.platformID);
			params.put("udid", userInfo.udid);
			params.put("user_id", userInfo.user_id);
			params.put("channel_id", userInfo.channel_id);
			
			Log.i("BannerArray params", params+"");

			String jsStr = "";
			jsStr = HttpUtils.doPost(url+uu, uu);
//			jsStr = BaseAuthenicationHttpClient.doRequest(_context, url+uu,null);
//			jsStr = HttpUtils.doGet(uu);
			if(jsStr == null){
				return null;
			}

			Log.i("startup  jsStr", jsStr);
			
			JSONObject json = null ,return_data = null,json_item = null;
			JSONArray banner = null;
			json = new JSONObject(jsStr);
			return_data = json.getJSONObject("return_data");
			banner = return_data.getJSONArray("banner");
			
			detail = new BannerArray();
			detail.ret = json.getString(META_CODE);
			BannerInfo info = null;
			detail.item = new ArrayList<BannerInfo>();
			for(int i = 0;i < banner.length(); i++){
				info = new BannerInfo();
				json_item = banner.getJSONObject(i);
				info.img = json_item.getString("img");
				info.dur = json_item.getString("dur");
				
				detail.item.add(info);
			}
//			detail.img = return_data.getString("userID");
//			detail.dur = return_data.getString("userType");
			Log.d("BannerArray", "catch之前" + ":e");
			return detail;
		} catch (Exception e) {
			Log.d("BannerArray", e + "##catch之后:e");
			return null;
		}

	}
	
	
	public ActionResultInfo getvercode(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"getvercode.php?"
												+"phonenum="+userInfo.mobilePhone
												+"&userID="+userInfo.userID
												+"&platformID="+userInfo.platformID
												+"&version="+userInfo.version
												+"&signature="+userInfo.signature
												+"&udid="+userInfo.udid;
		Log.i("vercode", "url == "+url);
		
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		Log.i("vercode", "jsStr == "+jsStr);
		if(jsStr == null){
			return null;
		}
		
		JSONObject json = null;
		
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
		
	}
	
	public ActionResultInfo forgetpassword(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"forgetpassword.php?"
												+"phonenum="+userInfo.mobilePhone
												+"&vercode="+userInfo.vercode
												+"&userID="+userInfo.userID
												+"&platformID="+userInfo.platformID
												+"&version="+userInfo.version
												+"&signature="+userInfo.signature
												+"&udid="+userInfo.udid;
		Log.i("找回密码", "url == "+url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		Log.i("找回密码", "jsStr == "+jsStr);
		if(jsStr == null){
			return null;
		}
		
		JSONObject json = null;
		JSONObject return_data = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			
			return_data = json.getJSONObject("return_data");
			detail.userID = return_data.getString("userID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
		
	}
	
	//http://testinterface.8518.com/app/ceb/register.php?phonenum=12345678900&password=123456&vercode=142864&productID=3&platformID=1
	public ActionResultInfo register(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"register.php";
//										+"phonenum="+userInfo.mobilePhone
//										+"&password="+userInfo.passwd
//										+"&vercode="+userInfo.vercode
//										+"&userID="+userInfo.userID
//										+"&invcode="+userInfo.recommendcode
//										+"&platformID="+userInfo.platformID
//										+"&version="+userInfo.version
//										+"&signature="+userInfo.signature
//										+"&udid="+userInfo.udid;
		
		Map<String, String> params = new HashMap<String, String>();

		params.put("phonenum", userInfo.mobilePhone);
		params.put("password", userInfo.passwd);
		params.put("vercode", userInfo.vercode);
		params.put("userID", userInfo.userID);
		params.put("invcode", userInfo.recommendcode);
		params.put("platformID", userInfo.platformID);
		params.put("version", userInfo.version);
		params.put("signature", userInfo.signature);
		params.put("udid", userInfo.udid);
		Log.i("url", url);
		String jsStr = "";
//		jsStr = HttpUtils.doGet(url);
		
		try {
			jsStr = BaseAuthenicationHttpClient._doRequest(_context, url,
					params);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr register", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONObject return_data = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			
			
			return_data = json.getJSONObject("return_data");
			
			
			
			detail.userID = return_data.getString("userID");
			detail.loginname = return_data.getString("loginname");
			detail.str = return_data.getString("str");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	
	public ActionResultInfo login(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"login.php";
										String param = "phonenum="+userInfo.mobilePhone
										+"&password="+userInfo.passwd
										+"&vercode="+userInfo.vercode
										+"&userID="+userInfo.userID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
										
		
		
										
		Log.i("url", url);
		Map<String, String> params = new HashMap<String, String>();

		params.put("phonenum", userInfo.mobilePhone);
		params.put("password", userInfo.passwd);
//		params.put("vercode", userInfo.vercode);
		params.put("userID", userInfo.userID);
		params.put("platformID", userInfo.platformID);
		params.put("version", userInfo.version);
		params.put("signature", userInfo.signature);
		params.put("udid", userInfo.udid);
		String jsStr = "";
//		jsStr = HttpUtils.doGet(url);
//		jsStr = HttpUtils.doPost(url, param);
		
		try {
			jsStr = BaseAuthenicationHttpClient._doRequest(_context, url,
					params);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		Log.i("jsStr login", "jsStr =="+jsStr);
		
		if(jsStr == null){
			return null;
		}
		
		JSONObject json = null;
		JSONObject return_data = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			
			
			return_data = json.getJSONObject("return_data");
			detail.userID = return_data.getString("userID");
			detail.userNickName = return_data.getString("userNickName");
			detail.loginname = return_data.getString("loginname");
			detail.mobilePhone = return_data.getString("mobilePhone");
			detail.userIcon = return_data.getString("userIco");
			detail.userSex = return_data.getString("userSex");
			detail.userCity = return_data.getString("userCity");
			detail.userMail = return_data.getString("userMail");
			detail.userAddress = return_data.getString("userAddress");
			detail.pid = return_data.getString("pid");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	public ActionResultInfo about(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"settingour.php?"
										+"&userID="+userInfo.userID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		Log.i("jsStr aboutus", "jsStr =="+jsStr);
		if(jsStr == null){
			return null;
		}
		
		JSONObject json = null;
		JSONObject return_data = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			
			
			return_data = json.getJSONObject("return_data");
			detail.url = return_data.getString("url");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	
	public MainArray qualityproducts(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"qualityproductlist.php?"
										+"&userID="+userInfo.userID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		
		MainArray detail = null;
		
		try {
			jsStr = HttpUtils.doGet(url);
			
			if(jsStr == null){
				return null;
			}
			
			Log.i("jsStr 首页", "jsStr =="+jsStr);
			
			JSONObject json = null;
			JSONObject return_data = null;
			JSONArray list = null;
			JSONArray banner = null;
			JSONObject list_item;
			JSONObject banner_item;
			json = new JSONObject(jsStr);
			detail = new MainArray();
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			detail.item = new ArrayList<List_item>();
			detail.item_banner = new ArrayList<Banner_item>();
			List_item info = null;
			Banner_item info2 = null;
			return_data = json.getJSONObject("return_data");
			
			banner = return_data.getJSONArray("banner");
			for(int i=0;i<banner.length();i++){
				banner_item = banner.getJSONObject(i);
				info2 = new Banner_item();
				
				info2.bannerImg = banner_item.getString("bannerImg");
				info2.bannerTitle = banner_item.getString("bannerTitle");
				info2.bannerUrl = banner_item.getString("bannerUrl");
				
				detail.item_banner.add(info2);
				
			}
			
			list = return_data.getJSONArray("list");
			for(int i=0;i<list.length();i++){
				list_item = list.getJSONObject(i);
				info = new List_item();
				
				info.title = list_item.getString("title");
				info.subtitle = list_item.getString("subtitle");
				info.productID = list_item.getString("productID");
				info.minInvestment = list_item.getString("minInvestment");
				info.repaymentType = list_item.getString("repaymentType");
				info.progress = list_item.getString("progress");
				info.extraIncome = list_item.getString("extraIncome");
				info.deadline = list_item.getString("deadline");
				info.type = list_item.getString("type");
				info.borrowStatus = list_item.getString("borrowStatus");
				
				info.awardState = list_item.getString("awardState");
				info.awardStr = list_item.getString("awardStr");
				
				detail.item.add(info);
				
			}
			
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	
	public ActionResultInfo resetPasswd(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"resetpassword.php?"
												+"password="+userInfo.passwd
												+"&userID="+userInfo.userID
												+"&platformID="+userInfo.platformID
												+"&version="+userInfo.version
												+"&signature="+userInfo.signature
												+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr 重设密码 ", "jsStr =="+jsStr);
		JSONObject json = null;
		
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
		
	}
	
	public ProductArray getProductList(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"list.php?"
										+"&productStatus="+userInfo.productStatus
										+"&userID="+userInfo.userID
										+"&page="+userInfo.page
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		
		ProductArray detail = null;
		
		try {
			jsStr = HttpUtils.doGet(url);
			
			if(jsStr == null){
				return null;
			}
			
			Log.i("jsStr 项目列表", "jsStr =="+jsStr);
			
			JSONObject json = null;
			JSONArray return_data = null;
			JSONObject object_item = null;
			
			json = new JSONObject(jsStr);
			detail = new ProductArray();
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			detail.page = json.getString("page");
			detail.count = json.getString("count");
			detail.item = new ArrayList<ProductList_item>();
			
			
			
			return_data = json.getJSONArray("return_data");
			
			ProductList_item info = null;
			
			
			for(int i=0;i<return_data.length();i++){
				object_item = return_data.getJSONObject(i);
				
				info = new ProductList_item();
				
				info.title = object_item.getString("title");
				info.subtitle = object_item.getString("subtitle");
				info.productID = object_item.getString("productID");
				info.borrowSatus = object_item.getString("borrowStatus");
				info.yields = object_item.getString("yields");
				info.daynum = object_item.getString("daynum");
				info.minInvestment = object_item.getString("minInvestment");
				info.progress = object_item.getString("progress");
				info.insurancesState = object_item.getString("insurancesState");
				info.mortgageState = object_item.getString("mortgageState");
				info.explain = object_item.getString("explain");
				info.description = object_item.getString("description");
				info.type = object_item.getString("type");
				info.awardState = object_item.getString("awardState");//freeMoney
				info.freeMoney = object_item.getString("freeMoney");
				info.awardStr = object_item.getString("awardStr");
				detail.item.add(info);
				
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
//	public ProductArray getDetail(UserInfo userInfo){
//		String url = "http://testinterface.8518.com/app/ceb/detail.php?"
//										+"&userID="+userInfo.userID
//										+"&productID="+userInfo.productID
//										+"&platformID="+userInfo.platformID
//										+"&version="+userInfo.version
//										+"&signature="+userInfo.signature
//										+"&udid="+userInfo.udid;
//		
//		Log.i("url", url);
//		String jsStr = "";
//		
//		ProductArray detail = null;
//		
//		try {
//			jsStr = HttpUtils.doGet(url);
//			
//			Log.i("jsStr 超额宝详情", "jsStr =="+jsStr);
//			
//			JSONObject json = null;
//			JSONArray return_data = null;
//			JSONObject object_item = null;
//			
//			json = new JSONObject(jsStr);
//			detail = new ProductArray();
//			
//			detail.ret = json.getString(META_CODE);
//			detail.msg = json.getString(META_MESSAGE);
//			detail.page = json.getString("page");
//			detail.count = json.getString("count");
//			detail.item = new ArrayList<ProductList_item>();
//			
//			
//			
//			return_data = json.getJSONArray("return_data");
//			
//			ProductList_item info = null;
//			
//			
//			for(int i=0;i<return_data.length();i++){
//				object_item = return_data.getJSONObject(i);
//				
//				info = new ProductList_item();
//				
//				info.title = object_item.getString("title");
//				info.subtitle = object_item.getString("subtitle");
//				info.productID = object_item.getString("productID");
//				info.yields = object_item.getString("yields");
//				info.daynum = object_item.getString("daynum");
//				info.minInvestment = object_item.getString("minInvestment");
//				info.progress = object_item.getString("progress");
//				info.insurancesState = object_item.getString("insurancesState");
//				info.mortgageState = object_item.getString("mortgageState");
//				info.explain = object_item.getString("explain");
//				info.description = object_item.getString("description");
//				
//				detail.item.add(info);
//				
//			}
//			
//			
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return detail;
//	}
	
	public CebDetail getCebDetail(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"detail.php?"
										+"&userID="+userInfo.userID
										+"&productID="+userInfo.productID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		
		CebDetail detail = null;
		
		try {
			jsStr = HttpUtils.doGet(url);
			
			if(jsStr == null){
				return null;
			}
			
			Log.i("jsStr CebDetail", "jsStr =="+jsStr);
			
			JSONObject json = null;
			JSONObject return_data = null;
			JSONObject baseInfo = null;
			JSONObject personalAccount = null;
			JSONObject productDetails = null;
			JSONObject investmentRecord = null;
			JSONObject repaymentPlan = null;
			JSONArray investmentList = null;
			JSONObject investmentList_item = null;
			JSONArray repaymentList = null;
			JSONObject repaymentList_item = null;
			JSONObject riskControl = null;
//			JSONObject object_item = null;
			
			json = new JSONObject(jsStr);
			detail = new CebDetail();
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
//			detail.page = json.getString("page");
//			detail.count = json.getString("count");
//			detail.item = new ArrayList<ProductList_item>();
			
			
			
			return_data = json.getJSONObject("return_data");
			baseInfo = return_data.getJSONObject("baseInfo");
			personalAccount = baseInfo.getJSONObject("personalAccount");
			riskControl = return_data.getJSONObject("riskControl");
			detail.title = baseInfo.getString("title");
			detail.subtitle = baseInfo.getString("subtitle");
			detail.accountBalance = personalAccount.getString("accountBalance");
			detail.annualIncome = baseInfo.getString("annualIncome");
			detail.progress = baseInfo.getString("progress");
			detail.borrowMoney = baseInfo.getString("borrowMoney"); 
			detail.maxInvestment = baseInfo.getString("maxInvestment"); 
			detail.minInvestment = baseInfo.getString("minInvestment");
			detail.totalMoney = baseInfo.getString("totalMoney");
			detail.alreadyInvest = baseInfo.getString("alreadyInvest");
			detail.period = baseInfo.getString("period");
			detail.repayment = baseInfo.getString("repayment");
			detail.securityState = baseInfo.getString("securityState");
			detail.mortgageState = baseInfo.getString("mortgageState");
			detail.moneyState = baseInfo.getString("moneyState");
			detail.info = baseInfo.getString("info");
			detail.increaseMoney = baseInfo.getString("increaseMoney");
			detail.borrowPass = baseInfo.getString("borrowPass");
			
			detail.type = baseInfo.getString("type");
			detail.awardStr = baseInfo.getString("awardStr");
			detail.startDate = baseInfo.getString("startDate");
			detail.endDate = baseInfo.getString("endDate");//freeMoney
			detail.freeMoney = baseInfo.getString("freeMoney");
			
			detail.investmentList_item = new ArrayList<InvestmentList_item>();
			detail.repaymentList_item = new ArrayList<RepaymentList_item>();
			
			productDetails = return_data.getJSONObject("productDetails");
			
			detail.pdHtml = productDetails.getString("pdHtml");
			
			
			investmentRecord = return_data.getJSONObject("investmentRecord");
			repaymentPlan  = return_data.getJSONObject("repaymentPlan");
			detail.numInvestors = investmentRecord.getString("numInvestors");
			detail.aveAmount = investmentRecord.getString("aveAmount");
			detail.lastAmount = investmentRecord.getString("lastAmount");
			if(Integer.parseInt(investmentRecord.getString("numInvestors")) > 0){
				investmentList = investmentRecord.getJSONArray("investmentList");
				InvestmentList_item info_investment = null;
				for(int i=0;i<investmentList.length();i++){
					investmentList_item = investmentList.getJSONObject(i);
					
					info_investment = new InvestmentList_item();
					
					info_investment.investUsername = investmentList_item.getString("investUsername");
					info_investment.totalAmount = investmentList_item.getString("totalAmount");
					info_investment.investmentDate = investmentList_item.getString("investmentDate");
					
					detail.investmentList_item.add(info_investment);
				}
				
			}
			
			
			repaymentList = repaymentPlan.getJSONArray("investmentList");
			RepaymentList_item info_repayment = null;
			
			
			for(int i=0;i<repaymentList.length();i++){
				repaymentList_item = repaymentList.getJSONObject(i);
				
				info_repayment = new RepaymentList_item();
				
				info_repayment.repaymentDate = repaymentList_item.getString("repaymentDate");
				info_repayment.maturityYield = repaymentList_item.getString("maturityYield");
				info_repayment.principal = repaymentList_item.getString("principal");
				
				detail.repaymentList_item.add(info_repayment);
			}
			
			detail.rcdHtml = riskControl.getString("rcdHtml");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	public ActionResultInfo validate(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"validate.php?"
												+"phonenum="+userInfo.mobilePhone
												+"&userID="+userInfo.userID
												+"&platformID="+userInfo.platformID
												+"&version="+userInfo.version
												+"&signature="+userInfo.signature
												+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		Log.i("jsStr 用户验证 ", "jsStr =="+jsStr);
		if(jsStr == null){
			return null;
		}
		
		JSONObject json = null;
		JSONObject return_data = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			
			return_data = json.getJSONObject("return_data");
			detail.state = return_data.getString("state");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
		
	}
	
	public ActionResultInfo getPersoncenter(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"center.php?"
										+"userID="+userInfo.userID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr 我的资产", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONObject return_data = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			
			
			return_data = json.getJSONObject("return_data");
			detail.cumulativeRevenue = return_data.getString("cumulativeRevenue");
			detail.availableBalance = return_data.getString("availableBalance");
			detail.collectRevenue = return_data.getString("collectRevenue");
			detail.username = return_data.getString("username");
			detail.userAccountType = return_data.getString("userAccountType");
			detail.cardType = return_data.getString("cardType");
			detail.megTotal = return_data.getString("megTotal");
			detail.content = return_data.getString("content");
			detail.totalAssets = return_data.getString("totalAssets");
//			detail.pid = return_data.getString("pid");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	public YongjinList getYongjinData(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"awardlist.php?"
										+"userID="+userInfo.userID
										+"&type="+userInfo.type
										+"&page="+userInfo.page
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr 佣金列表", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONArray return_data = null;
		JSONArray list = null;
		JSONObject object_item = null;
		YongjinList detail = null;
		
		detail = new YongjinList();
		
		
		try {
			json = new JSONObject(jsStr);
			
			
			
			return_data = json.getJSONArray("return_data");
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			detail.page = json.getString("page");
			detail.count = json.getString("count");
			detail.item = new ArrayList<Yongjin_item>();
				
//			list = return_data.getJSONArray("list");
			Yongjin_item info = null;
			
			if(return_data != null & return_data.length() > 0){
				for(int i=0;i<return_data.length();i++){
					object_item = return_data.getJSONObject(i);
					
					info = new Yongjin_item();
					
					info.title = object_item.getString("title");
					info.id = object_item.getString("id");
					info.awardType = object_item.getString("awardType");
					info.source = object_item.getString("source");
					info.validDate = object_item.getString("validDate");
					info.orderID = object_item.getString("orderID");
					info.state = object_item.getString("state");
					info.money = object_item.getString("money");
					
					
					detail.item.add(info);
					
				}
			}else{
				return null;
			}
			
				return detail;
			
				
				
				
			} catch (Exception e) {
			//	detail.ret = "null";
				e.printStackTrace();
			}
			
			
			
		
		return null;
	}
	
	
	public HuikuanList getToubiaoData(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"investlist.php?"
										+"&userID="+userInfo.userID
										+"&productType="+userInfo.productType
										+"&type="+userInfo.type
										+"&page="+userInfo.page
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr 投标中", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONArray return_data = null;
		JSONObject object_item = null;
		HuikuanList detail = null;
		
		detail = new HuikuanList();
		
		
		try {
			json = new JSONObject(jsStr);
			
			return_data = json.getJSONArray("return_data");
			
			if(return_data.length() != 0){
				detail.ret = json.getString(META_CODE);
				detail.msg = json.getString(META_MESSAGE);
				detail.page = json.getString("page");
				detail.count = json.getString("count");
				detail.item = new ArrayList<Huikuan_item>();
				
				
				
				Huikuan_item info = null;
				
				
				for(int i=0;i<return_data.length();i++){
					object_item = return_data.getJSONObject(i);
					
					info = new Huikuan_item();
					
					info.title = object_item.getString("title");
					info.subtitle = object_item.getString("subtitle");
					info.date = object_item.getString("date");
					info.productId = object_item.getString("productId");
					info.investId = object_item.getString("investId");
					info.investmentAmount = object_item.getString("investmentAmount");
					info.state = object_item.getString("state");
		//			info.amountReceived = object_item.getString("amountReceived");
		//			info.collectAmount = object_item.getString("collectAmount");
					info.amountRevenue = object_item.getString("amountRevenue");
					info.planType = object_item.getString("planType");
					info.contractsType = object_item.getString("contractsType");
					info.awardStr = object_item.getString("awardStr");
					if(object_item.has("awardState")){
						info.awardState = object_item.getString("awardState");
					}
					
					detail.item.add(info);
					
				}
				return detail;
			}
			
			
			
	//		return detail;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public HuikuanList getDuifuData(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"investlist.php?"
										+"&userID="+userInfo.userID
										+"&productType="+userInfo.productType
										+"&type="+userInfo.type
										+"&page="+userInfo.page
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr 兑付", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONArray return_data = null;
		JSONObject object_item = null;
		HuikuanList detail = null;
		
		detail = new HuikuanList();
		
		
		try {
			json = new JSONObject(jsStr);
			
			return_data = json.getJSONArray("return_data");
			
			if(return_data.length() != 0){
				detail.ret = json.getString(META_CODE);
				detail.msg = json.getString(META_MESSAGE);
				detail.page = json.getString("page");
				detail.count = json.getString("count");
				detail.item = new ArrayList<Huikuan_item>();
				
				
				
				Huikuan_item info = null;
				
				
				for(int i=0;i<return_data.length();i++){
					object_item = return_data.getJSONObject(i);
					
					info = new Huikuan_item();
					
					info.title = object_item.getString("title");
					info.subtitle = object_item.getString("subtitle");
					info.productId = object_item.getString("productId");
					info.investId = object_item.getString("investId");
					info.date = object_item.getString("date");
					info.investmentAmount = object_item.getString("investmentAmount");
					info.amountReceived = object_item.getString("amountReceived");
					info.collectAmount = object_item.getString("collectAmount");
					info.amountRevenue = object_item.getString("amountRevenue");
					info.planType = object_item.getString("planType");
					info.contractsType = object_item.getString("contractsType");
					if(object_item.has("awardState")){
						info.awardState = object_item.getString("awardState");
					}
					info.awardStr = object_item.getString("awardStr");
					
					detail.item.add(info);
					
				}
				return detail;
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public InvestplanList getInvestplanData(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"investplan.php?"
										+"&userID="+userInfo.userID
										+"&productId="+userInfo.productID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr 投资计划", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONArray return_data = null;
		JSONObject object_item = null;
		InvestplanList detail = null;
		
		detail = new InvestplanList();
		
		
		try {
			json = new JSONObject(jsStr);
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			
			return_data = json.getJSONArray("return_data");
		
			
			
			detail.item = new ArrayList<Investplan_item>();
			
			
			
			Investplan_item info = null;
			
			
			for(int i=0;i<return_data.length();i++){
				object_item = return_data.getJSONObject(i);
				
				info = new Investplan_item();
				
				info.title = object_item.getString("title");
				info.date = object_item.getString("date");
				info.money = object_item.getString("money");
				info.interestReceive = object_item.getString("interestReceive");
				info.principalReceive = object_item.getString("principalReceive");
				
				detail.item.add(info);
				
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	public ActionResultInfo getContract(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"investcontact.php?"
										+"investId="+userInfo.investId
										+"&userID="+userInfo.userID
										+"&productId="+userInfo.productID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr 借款合同", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONObject return_data = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			return_data = json.getJSONObject("return_data");
			
			if(return_data.length() == 0){
				Log.i("return_data = 0", "没数据");
				return null;
			}
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			detail.url = return_data.getString("url");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	public ActionResultInfo getAgreement(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"agreement.php?"
										+"type="+userInfo.type
										+"&userID="+userInfo.userID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr 用户协议", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONObject return_data = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			
			return_data = json.getJSONObject("return_data");
			
			if(return_data.length() == 0){
				Log.i("return_data = 0", "没数据");
				return null;
			}
			
			
			detail.url = return_data.getString("url");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	
	public ActionResultInfo getTixianRule(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"drawmoneyrule.php?"
										+"&userID="+userInfo.userID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr 提现规则", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONObject return_data = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			return_data = json.getJSONObject("return_data");
			
			if(return_data.length() == 0){
				Log.i("return_data = 0", "没数据");
				return null;
			}
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			detail.url = return_data.getString("url");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	public ActionResultInfo getChongzhiRule(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"rechargeDescript.php?"
										+"&userID="+userInfo.userID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr 充值限额", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONObject return_data = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			return_data = json.getJSONObject("return_data");
			
			if(return_data.length() == 0){
				Log.i("return_data = 0", "没数据");
				return null;
			}
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			detail.url = return_data.getString("url");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	public TradeDetailList getTradeDetail(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"deal.php?"
										+"&userID="+userInfo.userID
										+"&page="+userInfo.page
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr 交易明细", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONArray return_data = null;
		JSONObject object_item = null;
		TradeDetailList detail = null;
		
		detail = new TradeDetailList();
		
		
		try {
			json = new JSONObject(jsStr);
			
			return_data = json.getJSONArray("return_data");
			
			if(return_data.length() == 0){
				Log.i("return_data = 0", "没数据");
				return null;
			}
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			detail.page = json.getString("page");
			detail.count = json.getString("count");
			detail.item = new ArrayList<TradeDetail_item>();
			
			
			
			TradeDetail_item info = null;
			
			
			for(int i=0;i<return_data.length();i++){
				object_item = return_data.getJSONObject(i);
				
				info = new TradeDetail_item();
				
				info.title = object_item.getString("title");
				info.date = object_item.getString("date");
				info.type = object_item.getString("type");
				info.money = object_item.getString("money");
				info.typeinfo = object_item.getString("typeinfo");
				
				detail.item.add(info);
				
			}
			
			
			return detail;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public BankCardList getBankCardList(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"bank.php?"
										+"&userID="+userInfo.userID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr 银行卡管理", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONArray return_data = null;
		JSONObject object_item = null;
		BankCardList detail = null;
		
		detail = new BankCardList();
		
		
		try {
			json = new JSONObject(jsStr);
			
			detail.ret = json.getInt(META_CODE);
			if(detail.ret != 0){
				return null;
			}
			detail.msg = json.getString(META_MESSAGE);
			detail.item = new ArrayList<BankCard_item>();
			
			return_data = json.getJSONArray("return_data");
			
			if(return_data == null ||return_data.length() == 0){
				Log.i("return_data = 0", "没数据");
				return null;
			}
			
			
			
			BankCard_item info = null;
			
			for(int i=0;i<return_data.length();i++){
				object_item = return_data.getJSONObject(i);
				
				info = new BankCard_item();
				
				info.bankname = object_item.getString("bankname");
				info.bankIcon = object_item.getString("bankIcon");
				info.bankCard = object_item.getString("bankCard");
				info.bankID = object_item.getString("bankID");
				info.isDefault = object_item.getString("isDefault");
				info.expressFlag = object_item.getString("expressFlag");
				
//				if(info.bankState != null &&info.bankState.equals("Y")){
					detail.item.add(info);
//				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}

	public BankCardList getBankCard(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"mention.php?"
										+"&userID="+userInfo.userID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		if(jsStr == null){
			return null;
		}
		Log.i("jsStr 提现银行卡管理", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONObject return_data = null;
		JSONObject object_item = null;
		JSONArray bankList = null;
		BankCardList detail = null;
		
		detail = new BankCardList();
		
		
		try {
			json = new JSONObject(jsStr);
			
			detail.ret = json.getInt(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			
			return_data = json.getJSONObject("return_data");
			
			bankList = return_data.getJSONArray("bankList");
			if(return_data.length() == 0){
				Log.i("return_data = 0", "没数据");
				return null;
			}
			
			
			detail.balance = return_data.getString("balance");
			
			detail.item = new ArrayList<BankCard_item>();
			
			BankCard_item info = null;
			
			for(int i=0;i<bankList.length();i++){
				object_item = bankList.getJSONObject(i);
				
				info = new BankCard_item();
				
				info.bankname = object_item.getString("bankname");
				info.bankIcon = object_item.getString("bankIcon");
				info.bankCard = object_item.getString("bankCard");
				info.bankID = object_item.getString("bankID");
				info.withdrawbank = object_item.getString("withdrawbank");
				info.isDefault = object_item.getString("isDefault");
				info.expressFlag = object_item.getString("expressFlag");
				
				if(info.expressFlag != null && info.expressFlag.equals("Y")){
					Log.i("快捷", "快捷");
					if(detail.item != null)
						detail.item.clear();
					detail.item.add(info);
					return detail;
				}else if(info.expressFlag != null && !info.expressFlag.equals("Y")){
//					if(detail.item != null)
//						detail.item.clear();
					Log.i("快捷", "快捷");
					detail.item.add(info);
				}
				
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	public ActionResultInfo gettixianhuifu(String tiurl){
		String url = tiurl;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		if(jsStr == null){
			return null;
		}
		Log.i("jsStr 提现huifu", "jsStr =="+jsStr);
		
		JSONObject json = null;
		ActionResultInfo detail = null;
		detail = new ActionResultInfo();
		
		
			try {
				json = new JSONObject(jsStr);
				detail.ret = json.getString("code");
				detail.msg = json.getString("msg");
				
				return detail;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		return null;
	}
	
	
	public ActionResultInfo alterPasswd(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"settingpassword.php?"
										+"&userID="+userInfo.userID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&oldpassword="+userInfo.oldpassword
										+"&password="+userInfo.password
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		if(jsStr == null){
			return null;
		}
		Log.i("jsStr 修改密码", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONObject object_item = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	public ActionResultInfo getAccountBalance(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"account.php?"
										+"&userID="+userInfo.userID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		if(jsStr == null){
			return null;
		}
		Log.i("jsStr 账户余额", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONObject return_data = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			
			return_data = json.getJSONObject("return_data");
			
			detail.withdrawalMoney = return_data.getString("withdrawalMoney");
			detail.frozenSum = return_data.getString("frozenSum");
			detail.totalAssets = return_data.getString("totalAssets");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	public ActionResultInfo getFeedback(UserInfo userInfo){
		String url =SppaConstant.getIPmobilev11()+"feedback.php?"
												+"type="+userInfo.type
												+"&content="+userInfo.content
												+"&mail="+userInfo.mail
												+"&userID="+userInfo.userID
												+"&platformID="+userInfo.platformID
												+"&version="+userInfo.version
												+"&signature="+userInfo.signature
												+"&udid="+userInfo.udid;
		
		Log.i("url 意见反馈", "url =="+url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
//		jsStr = HttpUtils.doPost(url+uu, uu);
//		try {
//			jsStr = BaseAuthenicationHttpClient.doRequest(_context, url,null);
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		if(jsStr == null){
			return null;
		}
		Log.i("jsStr 意见反馈", "jsStr =="+jsStr);
		JSONObject json = null;
		
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
		
	}
	
	public MessageArray getMessage(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"message.php?"
										+"&userID="+userInfo.userID
										+"&page="+userInfo.page
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		
		MessageArray detail = null;
		
		try {
			jsStr = HttpUtils.doGet(url);
			if(jsStr == null){
				return null;
			}
			Log.i("jsStr 我的消息", "jsStr =="+jsStr);
			
			JSONObject json = null;
			JSONArray return_data = null;
			JSONObject object_item = null;
			
			json = new JSONObject(jsStr);
			detail = new MessageArray();
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			detail.page = json.getString("page");
			detail.count = json.getString("count");
			detail.item = new ArrayList<Message_item>();
			
			
			
			return_data = json.getJSONArray("return_data");
			
			Message_item info = null;
			
			
			for(int i=0;i<return_data.length();i++){
				object_item = return_data.getJSONObject(i);
				
				info = new Message_item();
				
				info.title = object_item.getString("title");
				info.date = object_item.getString("date");
				info.type = object_item.getString("type");
				info.content = object_item.getString("content");
				info.msgID = object_item.getString("msgID");
				
				detail.item.add(info);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	public NoticeArray getOsMessage(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"notice.php?"
										+"userID="+userInfo.userID
										+"&page="+userInfo.page
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		
		NoticeArray detail = null;
		
		try {
			jsStr = HttpUtils.doGet(url);
			if(jsStr == null){
				return null;
			}
			Log.i("jsStr 系统公告", "jsStr =="+jsStr);
			
			JSONObject json = null;
			JSONArray return_data = null;
			JSONObject object_item = null;
			
			json = new JSONObject(jsStr);
			detail = new NoticeArray();
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			detail.page = json.getString("page");
			detail.count = json.getString("count");
			detail.item = new ArrayList<Notice_item>();
			
			
			
			return_data = json.getJSONArray("return_data");
			
			Notice_item info = null;
			
			
			for(int i=0;i<return_data.length();i++){
				object_item = return_data.getJSONObject(i);
				
				info = new Notice_item();
				
				info.title = object_item.getString("title");
				info.date = object_item.getString("date");
				info.noticeID = object_item.getString("noticeID");
				info.content = object_item.getString("content");
				info.htmlurl = object_item.getString("htmlurl");
				
				detail.item.add(info);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	
	public ActivityList getHotActivity(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"activitylist.php?"
										+"&userID="+userInfo.userID
										+"&page="+userInfo.page
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr 热门活动", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONArray return_data = null;
		JSONObject object_item = null;
		ActivityList detail = null;
		
		detail = new ActivityList();
		
		
		try {
			json = new JSONObject(jsStr);
			
			detail.ret = json.getInt(META_CODE);
			
			if(detail.ret != 0){
				return null;
			}
			detail.msg = json.getString(META_MESSAGE);
			detail.item = new ArrayList<Activity_item>();
			
			return_data = json.getJSONArray("return_data");
			
//			if(return_data == null ||return_data.length() == 0){
//				Log.i("return_data = 0", "没数据");
//				return null;
//			}
			
			
			
			Activity_item info = null;
			
			for(int i=0;i<return_data.length();i++){
				object_item = return_data.getJSONObject(i);
				
				info = new Activity_item();
				
				info.title = object_item.getString("title");
				info.date = object_item.getString("date");
				info.img = object_item.getString("img");
				info.content = object_item.getString("content");
				info.jumpUrl = object_item.getString("jumpUrl");
				detail.item.add(info);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	public ActionResultInfo getThaw(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"thaw.php?"
										+"userID="+userInfo.userID
										+"&investId="+userInfo.productID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr 解冻", "jsStr =="+jsStr);
		
		JSONObject json = null;
//		JSONArray return_data = null;
//		JSONObject object_item = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
			try {
				json = new JSONObject(jsStr);
				
				detail.ret = json.getString("ret");
				detail.msg = json.getString("message");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		//		return detail;
		
		return detail;
	}
	
	
	public ActionResultInfo getUpdate(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"messageupdate.php?"
										+"userID="+userInfo.userID
										+"&msgID="+userInfo.msgID 
										+"&readStatus="+userInfo.readStatus
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		ActionResultInfo detail = null;
		detail = new ActionResultInfo();
		try {
			jsStr = HttpUtils.doGet(url);
			if(jsStr == null){
				return null;
			}
			Log.i("jsStr 消息状态", "jsStr =="+jsStr);
			
			JSONObject json = null;
			JSONArray return_data = null;
			JSONObject object_item = null;
			
			json = new JSONObject(jsStr);
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			
			
//			
//			return_data = json.getJSONArray("return_data");
//			
//			Message_item info = null;
//			
//			
//			for(int i=0;i<return_data.length();i++){
//				object_item = return_data.getJSONObject(i);
//				
//				info = new Message_item();
//				
//				info.title = object_item.getString("title");
//				info.date = object_item.getString("date");
//				info.type = object_item.getString("type");
//				info.content = object_item.getString("content");
//				info.msgID = object_item.getString("msgID");
//				
//				detail.item.add(info);
				
	//		}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	
	
	public HuikuanList getHuikuanData(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"investlist.php?"
										+"&userID="+userInfo.userID
										+"&productType="+userInfo.productType
										+"&type="+userInfo.type
										+"&page="+userInfo.page
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr 回款中", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONArray return_data = null;
		JSONObject object_item = null;
		HuikuanList detail = null;
		
		detail = new HuikuanList();
		
		
		try {
			json = new JSONObject(jsStr);
			
			
			
			return_data = json.getJSONArray("return_data");
			
			if(return_data.length() != 0){
				detail.ret = json.getString(META_CODE);
				detail.msg = json.getString(META_MESSAGE);
				detail.page = json.getString("page");
				detail.count = json.getString("count");
				detail.item = new ArrayList<Huikuan_item>();
				
				Huikuan_item info = null;
				
				
				for(int i=0;i<return_data.length();i++){
					object_item = return_data.getJSONObject(i);
					
					info = new Huikuan_item();
					
					info.title = object_item.getString("title");
					info.subtitle = object_item.getString("subtitle");
					info.date = object_item.getString("date");
					info.productId = object_item.getString("productId");
					info.investId = object_item.getString("investId");
					info.investmentAmount = object_item.getString("investmentAmount");
					info.amountReceived = object_item.getString("amountReceived");
					info.collectAmount = object_item.getString("collectAmount");
					info.amountRevenue = object_item.getString("amountRevenue");
					info.planType = object_item.getString("planType");
					info.contractsType = object_item.getString("contractsType");
					info.awardState = object_item.getString("awardState");
					info.awardStr = object_item.getString("awardStr");
					if(object_item.has("awardState")){
						info.awardState = object_item.getString("awardState");
					}
					
					
					detail.item.add(info);
					
				}
				return detail;
			}
				
				
				
			} catch (Exception e) {
			//	detail.ret = "null";
				e.printStackTrace();
			}
			
			
			
		
		return null;
	}
	
	
	public ActionResultInfo getAccountCenter(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"accountcenter.php?"
										+"&userID="+userInfo.userID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		if(jsStr == null){
			return null;
		}
		Log.i("jsStr 账户中心", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONObject return_data = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			
			return_data = json.getJSONObject("return_data");
			
			detail.username = return_data.getString("username");
			detail.payAccount = return_data.getString("payAccount");
			detail.phone = return_data.getString("phone");
			detail.iconurl = return_data.getString("iconurl");
			detail.userAccountType = return_data.getString("userAccountType");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	public ActionResultInfo getLingqu(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"awardreceive.php?"
										+"awardID="+userInfo.awardID
										+"&userID="+userInfo.userID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr 领取奖励", "jsStr =="+jsStr);
		
		JSONObject json = null;
		//JSONObject return_data = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
//			return_data = json.getJSONObject("return_data");
//			
//			if(return_data.length() == 0){
//				Log.i("return_data = 0", "没数据");
//				return null;
//			}
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
//			detail.url = return_data.getString("url");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	public ActionResultInfo getIsartner(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"invitefriend.php?"
										+"userID="+userInfo.userID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr 是否代理人", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONObject return_data = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			
			if(json.has("return_data")){
				return_data = json.getJSONObject("return_data");
				if(return_data.has("partnerType")){
					detail.partnerType = return_data.getString("partnerType");
				}
			}else{
				return null;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	public ActionResultInfo getJoin(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"inviteapply.php?"
										+"userID="+userInfo.userID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr 申请为代理人", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONObject return_data = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			
//			if(json.has("return_data")){
//				return_data = json.getJSONObject("return_data");
//				if(return_data.has("partnerType")){
//					detail.partnerType = return_data.getString("partnerType");
//				}
//			}else{
//				return null;
//			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	
	public InviteList getInviteList(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"invitelist.php?"
										+"type="+userInfo.type
										+"&userID="+userInfo.userID
										+"&page="+userInfo.page
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		Log.i("jsStr invitelist", "jsStr =="+jsStr);
		if(jsStr == null){
			return null;
		}
		
		JSONObject json = null;
		JSONObject return_data = null;
		JSONArray noPartnerList = null;
		JSONObject noPartner_item = null;
		JSONArray pointsList = null;
		JSONObject points_item = null;
		JSONArray brokerageList = null;
		JSONObject brokerage_item = null;
		InviteList detail = null;
		
		detail = new InviteList();
		
		
		try {
			json = new JSONObject(jsStr);
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			if(json.has("page")){
				detail.page = json.getString("page");
			}
			if(json.has("count")){
				detail.count = json.getString("count");
			}
			if(json.has("return_data")){
				return_data = json.getJSONObject("return_data");
				
				if(return_data.has("invitenum")){
					detail.invitenum = return_data.getString("invitenum");
				}
				if(return_data.has("pid")){
					detail.pid = return_data.getString("pid");
				}
				if(return_data.has("number")){
					detail.number = return_data.getString("number");
				}
				if(return_data.has("content")){
					detail.content = return_data.getString("content");
				}
				if(return_data.has("noPartnerList")){
					noPartnerList = return_data.getJSONArray("noPartnerList");
					detail.noPartner_item = new ArrayList<NoPartner>();
					if(noPartnerList.length() > 0){
						NoPartner info_nopa = null;
						for(int i = 0;i< noPartnerList.length();i++){
							noPartner_item = noPartnerList.getJSONObject(i);
							info_nopa = new NoPartner();
							info_nopa.noPartnerAccount = noPartner_item.getString("noPartnerAccount");
							info_nopa.noPartnerRegisterType = noPartner_item.getString("noPartnerRegisterType");
							info_nopa.noPartnerDepositType = noPartner_item.getString("noPartnerDepositType");
							info_nopa.noPartnerInvestType = noPartner_item.getString("noPartnerInvestType");
							info_nopa.noPartnerRewardPoints = noPartner_item.getString("noPartnerRewardPoints");
							
							detail.noPartner_item.add(info_nopa);
						}
					}else{
						
						
					}
					
					
				}
				if(return_data.has("pointsList")){
					pointsList = return_data.getJSONArray("pointsList");
					detail.points_item = new ArrayList<NoPartner>();
					
					NoPartner info_poi = null;
					for(int i = 0;i< pointsList.length();i++){
						points_item = pointsList.getJSONObject(i);
						info_poi = new NoPartner();
						info_poi.noPartnerAccount = points_item.getString("noPartnerAccount");
						info_poi.noPartnerRegisterType = points_item.getString("noPartnerRegisterType");
						info_poi.noPartnerDepositType = points_item.getString("noPartnerDepositType");
						info_poi.noPartnerInvestType = points_item.getString("noPartnerInvestType");
						info_poi.noPartnerRewardPoints = points_item.getString("noPartnerRewardPoints");
						
						detail.points_item.add(info_poi);
					}
				}
				if(return_data.has("brokerageList")){
					brokerageList = return_data.getJSONArray("brokerageList");
					detail.brokerage_item = new ArrayList<Brokerage>();
					
					Brokerage info_brok = null;
					for(int i = 0;i< brokerageList.length();i++){
						brokerage_item = brokerageList.getJSONObject(i);
						info_brok = new Brokerage();
						info_brok.brokerageAccount = brokerage_item.getString("brokerageAccount");
						info_brok.investMoney = brokerage_item.getString("investMoney");
						info_brok.investData = brokerage_item.getString("investData");
						info_brok.brokerage = brokerage_item.getString("brokerage");
						
						detail.brokerage_item.add(info_brok);
					}
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	public ActionResultInfo getInviteRule(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"inviterule.php?"
										+"userID="+userInfo.userID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("InviteRule", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		Log.i("jsStr InviteRule", "jsStr =="+jsStr);
		if(jsStr == null){
			return null;
		}
		
		JSONObject json = null;
		JSONObject return_data = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			
			
			return_data = json.getJSONObject("return_data");
			detail.url = return_data.getString("url");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	public ActionResultInfo getAwardRule(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"productawardinfo.php?"
										+"userID="+userInfo.userID
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&productID="+userInfo.productID
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("AwardRule", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		Log.i("jsStr AwardRule", "jsStr =="+jsStr);
		if(jsStr == null){
			return null;
		}
		
		JSONObject json = null;
		JSONObject return_data = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			
			
			return_data = json.getJSONObject("return_data");
			detail.url = return_data.getString("url");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	public ActionResultInfo getShare(UserInfo userInfo){
		String url = SppaConstant.getIPmobilev11()+"sharefriend.php?"
										+"userID="+userInfo.userID
										+"&type="+userInfo.type
										+"&platformID="+userInfo.platformID
										+"&version="+userInfo.version
										+"&signature="+userInfo.signature
										+"&udid="+userInfo.udid;
		
		Log.i("url", url);
		String jsStr = "";
		jsStr = HttpUtils.doGet(url);
		
		if(jsStr == null){
			return null;
		}
		
		Log.i("jsStr 分享", "jsStr =="+jsStr);
		
		JSONObject json = null;
		JSONObject return_data = null;
		ActionResultInfo detail = null;
		
		detail = new ActionResultInfo();
		
		
		try {
			json = new JSONObject(jsStr);
			
			detail.ret = json.getString(META_CODE);
			detail.msg = json.getString(META_MESSAGE);
			
			if(json.has("return_data")){
				return_data = json.getJSONObject("return_data");
					detail.pid = return_data.getString("pid");
					detail.title = return_data.getString("title");
					detail.content = return_data.getString("content");
					detail.outerContent = return_data.getString("outerContent");
					detail.imgurl = return_data.getString("imgurl");
					detail.htmlurl = return_data.getString("htmlurl");
			}else{
				return null;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return detail;
	}
	
	
	
	
	
	
	
}
