package net.oschina.app.bean;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 便签数据bean（有重载equals()方法）
 * 
 * @author kymjs (https://github.com/kymjs)
 * 
 */
@XStreamAlias("sticky")
public class NotebookData extends Entity implements Serializable,
        Comparable<NotebookData> {
    private static final long serialVersionUID = 1L;

    @XStreamAlias("id")
    private int id;
    @XStreamAlias("iid")
    private int iid;
    @XStreamAlias("timestamp")
    private String unixTime;
    @XStreamAlias("updateTime")
    private String date;
    @XStreamAlias("content")
    private String content;
    @XStreamAlias("color")
    private String colorText;

    private String serverUpdateTime; // 服务器端需要，客户端无用
    private int color;

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) {
            return true;
        } else {
            if (o instanceof NotebookData) {
                NotebookData data = (NotebookData) o;
                try {
                    return (this.id == data.getId())
                            && (this.iid == data.getIid())
                            && (this.unixTime == data.getUnixTime())
                            && (this.date.equals(data.getDate()))
                            && (this.content == data.getContent())
                            && (this.color == data.getColor());
                } catch (NullPointerException e) {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(String time) {
        this.unixTime = time;
        setServerUpdateTime(time);
    }

    public String getColorText() {
        return colorText;
    }

    public void setColorText(String color) {
        this.colorText = color;
    }

    public int getColor() {
        // 客户端始终以当前手机上的颜色为准
        if ("blue".equals(colorText)) {
            this.color = 3;
        } else if ("red".equals(colorText)) {
            this.color = 2;
        } else if ("yellow".equals(colorText)) {
            this.color = 1;
        } else if ("purple".equals(colorText)) {
            this.color = 4;
        } else if ("green".equals(colorText)) {
            this.color = 0;
        }
        return color;
    }

    public String getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(String serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public void setColor(int color) {
        switch (color) {
        case 0:
            colorText = "green";
            break;
        case 1:
            colorText = "yellow";
            break;
        case 2:
            colorText = "red";
            break;
        case 3:
            colorText = "blue";
            break;
        case 4:
            colorText = "purple";
            break;
        default:
            this.color = color;
            break;
        }
    }

    @Override
    public int compareTo(NotebookData another) {
        return this.iid - another.getIid();
    }
}