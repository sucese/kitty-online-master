package net.oschina.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 评论实体类
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月14日 下午3:29:22
 *
 */
@SuppressWarnings("serial")
@XStreamAlias("comment")
public class Comment extends Entity implements Parcelable {

	public static final String BUNDLE_KEY_COMMENT = "bundle_key_comment";
	public static final String BUNDLE_KEY_ID = "bundle_key_id";
	public static final String BUNDLE_KEY_CATALOG = "bundle_key_catalog";
	public static final String BUNDLE_KEY_BLOG = "bundle_key_blog";
	public static final String BUNDLE_KEY_OPERATION = "bundle_key_operation";
	
	public static final int OPT_ADD = 1;
	public static final int OPT_REMOVE = 2;
	
	public final static int CLIENT_MOBILE = 2;
	public final static int CLIENT_ANDROID = 3;
	public final static int CLIENT_IPHONE = 4;
	public final static int CLIENT_WINDOWS_PHONE = 5;
	
	@XStreamAlias("portrait")
	private String portrait;
	
	@XStreamAlias("content")
	private String content;
	
	@XStreamAlias("author")
	private String author;
	
	@XStreamAlias("authorid")
	private int authorId;
	
	@XStreamAlias("pubDate")
	private String pubDate;
	
	@XStreamAlias("appclient")
	private int appClient;
	
	@XStreamAlias("replies")
	private List<Reply> replies = new ArrayList<Reply>();
	
	@XStreamAlias("refers")
	private List<Refer> refers = new ArrayList<Refer>();
	
	@SuppressWarnings("unchecked")
	public Comment(Parcel source) {
		id = source.readInt();
		portrait = source.readString();
		author = source.readString();
		authorId = source.readInt();
		pubDate = source.readString();
		appClient = source.readInt();
		content = source.readString();

		replies = source.readArrayList(Reply.class.getClassLoader());
		refers = source.readArrayList(Refer.class.getClassLoader());
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(portrait);
		dest.writeString(author);
		dest.writeInt(authorId);
		dest.writeString(pubDate);
		dest.writeInt(appClient);
		dest.writeString(content);

		dest.writeList(replies);
		dest.writeList(refers);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public int getAppClient() {
		return appClient;
	}

	public void setAppClient(int appClient) {
		this.appClient = appClient;
	}

	public List<Reply> getReplies() {
		return replies;
	}

	public void setReplies(List<Reply> replies) {
		this.replies = replies;
	}

	public List<Refer> getRefers() {
		return refers;
	}

	public void setRefers(List<Refer> refers) {
		this.refers = refers;
	}

	@XStreamAlias("reply")
	public static class Reply implements Serializable, Parcelable {
		@XStreamAlias("rauthor")
		public String rauthor;
		@XStreamAlias("rpubDate")
		public String rpubDate;
		@XStreamAlias("rcontent")
		public String rcontent;
		
		public Reply() {
		}

		public Reply(Parcel source) {
			rauthor = source.readString();
			rpubDate = source.readString();
			rcontent = source.readString();
		}
		
		public String getRauthor() {
			return rauthor;
		}
		public void setRauthor(String rauthor) {
			this.rauthor = rauthor;
		}
		public String getRpubDate() {
			return rpubDate;
		}
		public void setRpubDate(String rpubDate) {
			this.rpubDate = rpubDate;
		}
		public String getRcontent() {
			return rcontent;
		}
		public void setRcontent(String rcontent) {
			this.rcontent = rcontent;
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(rauthor);
			dest.writeString(rpubDate);
			dest.writeString(rcontent);
		}

		@Override
		public int describeContents() {
			return 0;
		}

		public static final Parcelable.Creator<Reply> CREATOR = new Creator<Reply>() {

			@Override
			public Reply[] newArray(int size) {
				return new Reply[size];
			}

			@Override
			public Reply createFromParcel(Parcel source) {
				return new Reply(source);
			}
		};
		
	}
	
	@XStreamAlias("refer")
	public static class Refer implements Serializable, Parcelable {
		
		@XStreamAlias("refertitle")
		public String refertitle;
		@XStreamAlias("referbody")
		public String referbody;
		
		public Refer() {
		}

		public Refer(Parcel source) {
			referbody = source.readString();
			refertitle = source.readString();
		}
		
		public String getRefertitle() {
			return refertitle;
		}
		public void setRefertitle(String refertitle) {
			this.refertitle = refertitle;
		}
		public String getReferbody() {
			return referbody;
		}
		public void setReferbody(String referbody) {
			this.referbody = referbody;
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(referbody);
			dest.writeString(refertitle);
		}

		@Override
		public int describeContents() {
			return 0;
		}

		public static final Parcelable.Creator<Refer> CREATOR = new Creator<Comment.Refer>() {

			@Override
			public Refer[] newArray(int size) {
				return new Refer[size];
			}

			@Override
			public Refer createFromParcel(Parcel source) {
				return new Refer(source);
			}
		};
	}
	
	public static final Parcelable.Creator<Comment> CREATOR = new Creator<Comment>() {

		@Override
		public Comment[] newArray(int size) {
			return new Comment[size];
		}

		@Override
		public Comment createFromParcel(Parcel source) {
			return new Comment(source);
		}
	};
}
