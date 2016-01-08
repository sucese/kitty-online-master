package net.oschina.app.api;

import org.json.JSONObject;

public class ApiResponse {
    protected Object _data;
    protected String _message;
    protected int _errorCode;
    protected boolean _isOk;
    private long _total;
    private String _serverTime;
    private boolean isCanceled;

    public ApiResponse(JSONObject json) {
	if (json != null) {
	    setData(json.optJSONObject("data") == null ? json
		    .optJSONArray("data") : json.optJSONObject("data"));
	    setMessage(json.optString("result_desc"));
	    setErrorCode(json.optInt("result_code"));
	    setOk(getErrorCode() == 0);
	    setServerTime(json.optString("timestamp"));
	}
    }

    public Object getData() {
	return _data;
    }

    public void setData(Object _data) {
	this._data = _data;
    }

    public boolean isOk() {
	return _isOk;
    }

    public void setOk(boolean _isOk) {
	this._isOk = _isOk;
    }

    public String getMessage() {
	return _message;
    }

    public void setMessage(String _message) {
	this._message = _message;
    }

    public int getErrorCode() {
	return _errorCode;
    }

    public void setErrorCode(int _errorCode) {
	this._errorCode = _errorCode;
    }

    @Override
    public String toString() {
	return "data:" + getData() + " message:" + getMessage() + " errocode:"
		+ _errorCode;
    }

    public void update(ApiResponse response) {
	_message = response.getMessage();
    }

    public void setTotal(long total) {
	_total = total;
    }

    public long getTotal() {
	return _total;
    }

    public String getServerTime() {
	return _serverTime;
    }

    public void setServerTime(String _serverTime) {
	this._serverTime = _serverTime;
    }

    public boolean isCanceled() {
	return isCanceled;
    }

    public void setCanceled(boolean isCanceled) {
	this.isCanceled = isCanceled;
    }
}
