package net.oschina.app.bean;

import java.io.Serializable;

import net.oschina.app.AppException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 二维码扫描实体类
 * @author 火蚁 (http://my.oschina.net/LittleDY)
 * @version 1.0
 * @created 2014-3-17
 */
@SuppressWarnings("serial")
public class BarCode extends Entity implements Serializable{
	
	public final static String NODE_REQURE_LOGIN = "require_login";
	public final static String NODE_TYPE = "type"; 
	public final static String NODE_URL = "url";
	public final static String NODE_TITLE = "title";
	
	public final static byte LOGIN_IN = 0x00;// 登录
	public final static byte SIGN_IN = 0x01;// 签到
	
	private boolean requireLogin;// 是否需要登录
	private int type;// 类型
	private String url;// url地址
	private String title;// 标题
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isRequireLogin() {
		return requireLogin;
	}

	public void setRequireLogin(boolean requireLogin) {
		this.requireLogin = requireLogin;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public static BarCode parse(String barCodeString) throws AppException {
		BarCode barCode = new BarCode();
		try {
			// 由字符串创建json对象
			JSONObject jsonObject = new JSONObject(barCodeString);
			// 取数据操作
			if (!jsonObject.isNull(NODE_REQURE_LOGIN)) {
				barCode.setRequireLogin(jsonObject.getBoolean(NODE_REQURE_LOGIN));
			} else {
				barCode.setUrl("www.oschina.net");
			}
			if (!jsonObject.isNull(NODE_TYPE)) {
				barCode.setType(jsonObject.getInt(NODE_TYPE));
			} else {
				barCode.setType(1);
			}
			if (!jsonObject.isNull(NODE_URL)) {
				barCode.setUrl(jsonObject.getString(NODE_URL));
			} else {
				barCode.setUrl("www.oschina.net");
			}
			if (!jsonObject.isNull(NODE_TITLE)) {
				barCode.setTitle(jsonObject.getString(NODE_TITLE));
			} else {
				barCode.setTitle("");
			}
		} catch (JSONException e) {
			// 抛出一个json解析错误的异常
			throw AppException.json(e);
		} 
        return barCode;       
	}
	
	@Override
	public String toString() {
		return "Barcode [requireLogin=" + requireLogin + ", type=" + type
				+ ", url=" + url + "]";
	}
}
