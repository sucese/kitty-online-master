package net.oschina.app.bean;

import net.oschina.app.AppException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 签到返回结果实体类
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月22日 下午1:43:13
 *
 */
public class SingInResult {

	public final static String NODE_MES = "msg";
	public final static String NODE_ERROR = "error";

	private String message;// 成功消息
	private String errorMes;// 错误消息
	private boolean ok;// 是否成功

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorMes() {
		return errorMes;
	}

	public void setErrorMes(String errorMes) {
		this.errorMes = errorMes;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public static SingInResult parse(String jsonStr) throws AppException {
		SingInResult jsonResult = new SingInResult();
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			// 如果有错误信息则表示不成功
			if (jsonObject.isNull(NODE_ERROR)) {
				jsonResult.setOk(true);
			}
			if (!jsonObject.isNull(NODE_ERROR)) {
				jsonResult.setErrorMes(jsonObject.getString(NODE_ERROR));
			}
			if (!jsonObject.isNull(NODE_MES)) {
				jsonResult.setMessage(jsonObject.getString(NODE_MES));
			}
		} catch (JSONException e) {
			// 抛出一个json解析错误的异常
			throw AppException.json(e);
		}
		return jsonResult;
	}
}
