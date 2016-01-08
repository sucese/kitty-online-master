package net.oschina.app.bean;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 更新实体类
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年11月10日 下午12:56:27
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("oschina")
public class Update implements Serializable {

	@XStreamAlias("update")
	private UpdateBean update;

	public UpdateBean getUpdate() {
		return update;
	}

	public void setUpdate(UpdateBean update) {
		this.update = update;
	}

	@XStreamAlias("update")
	public class UpdateBean implements Serializable {
		@XStreamAlias("wp7")
		private String wp7;
		@XStreamAlias("ios")
		private String ios;
		@XStreamAlias("android")
		private AndroidBean android;

		public String getWp7() {
			return wp7;
		}

		public void setWp7(String wp7) {
			this.wp7 = wp7;
		}

		public String getIos() {
			return ios;
		}

		public void setIos(String ios) {
			this.ios = ios;
		}

		public AndroidBean getAndroid() {
			return android;
		}

		public void setAndroid(AndroidBean android) {
			this.android = android;
		}

	}

	@XStreamAlias("android")
	public class AndroidBean implements Serializable {
		@XStreamAlias("versionCode")
		private int versionCode;
		@XStreamAlias("versionName")
		private String versionName;
		@XStreamAlias("downloadUrl")
		private String downloadUrl;
		@XStreamAlias("updateLog")
		private String updateLog;
		@XStreamAlias("coverUpdate")
		private String coverUpdate;
		@XStreamAlias("coverStartDate")
		private String coverStartDate;
		@XStreamAlias("coverEndDate")
		private String coverEndDate;
		@XStreamAlias("coverURL")
		private String coverURL;

		public int getVersionCode() {
			return versionCode;
		}

		public void setVersionCode(int versionCode) {
			this.versionCode = versionCode;
		}

		public String getVersionName() {
			return versionName;
		}

		public void setVersionName(String versionName) {
			this.versionName = versionName;
		}

		public String getDownloadUrl() {
			return downloadUrl;
		}

		public void setDownloadUrl(String downloadUrl) {
			this.downloadUrl = downloadUrl;
		}

		public String getUpdateLog() {
			return updateLog;
		}

		public void setUpdateLog(String updateLog) {
			this.updateLog = updateLog;
		}

		public String getCoverUpdate() {
			return coverUpdate;
		}

		public void setCoverUpdate(String coverUpdate) {
			this.coverUpdate = coverUpdate;
		}

		public String getCoverStartDate() {
			return coverStartDate;
		}

		public void setCoverStartDate(String coverStartDate) {
			this.coverStartDate = coverStartDate;
		}

		public String getCoverEndDate() {
			return coverEndDate;
		}

		public void setCoverEndDate(String coverEndDate) {
			this.coverEndDate = coverEndDate;
		}

		public String getCoverURL() {
			return coverURL;
		}

		public void setCoverURL(String coverURL) {
			this.coverURL = coverURL;
		}
	}
}
