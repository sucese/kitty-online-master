package net.oschina.app.service;

import android.os.Parcel;
import android.os.Parcelable;

public class PublicCommentTask implements Parcelable {
	private int catalog;
	private int id;
	private int uid;
	private String content;
	private int isPostToMyZone;

	public PublicCommentTask() {
	}

	public PublicCommentTask(Parcel source) {
		catalog = source.readInt();
		id = source.readInt();
		uid = source.readInt();
		content = source.readString();
		isPostToMyZone = source.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(catalog);
		dest.writeInt(id);
		dest.writeInt(uid);
		dest.writeString(content);
		dest.writeInt(isPostToMyZone);
	}

	public int getCatalog() {
		return catalog;
	}

	public void setCatalog(int catalog) {
		this.catalog = catalog;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getIsPostToMyZone() {
		return isPostToMyZone;
	}

	public void setIsPostToMyZone(int isPostToMyZone) {
		this.isPostToMyZone = isPostToMyZone;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<PublicCommentTask> CREATOR = new Creator<PublicCommentTask>() {

		@Override
		public PublicCommentTask[] newArray(int size) {
			return new PublicCommentTask[size];
		}

		@Override
		public PublicCommentTask createFromParcel(Parcel source) {
			return new PublicCommentTask(source);
		}
	};
}
