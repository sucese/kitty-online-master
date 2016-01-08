package net.oschina.app.team.bean;

import net.oschina.app.bean.Entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressWarnings("serial")
@XStreamAlias("active")
public class TeamMyActive extends Entity {

    @XStreamAlias("type")
    private String type;
    @XStreamAlias("reply")
    private String reply;
    @XStreamAlias("createTime")
    private String createTime;
    @XStreamAlias("body")
    private Body body;
    @XStreamAlias("author")
    private Author author;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @XStreamAlias("body")
    class Body extends Entity {
        @XStreamAlias("detail")
        private String detail;
        @XStreamAlias("code")
        private String code;
        @XStreamAlias("codeType")
        private String codeType;
        @XStreamAlias("image")
        private String image;
        @XStreamAlias("imageOrigin")
        private String imageOrigin;

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCodeType() {
            return codeType;
        }

        public void setCodeType(String codeType) {
            this.codeType = codeType;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImageOrigin() {
            return imageOrigin;
        }

        public void setImageOrigin(String imageOrigin) {
            this.imageOrigin = imageOrigin;
        }

    }

    @XStreamAlias("author")
    class Author extends Entity {
        @XStreamAlias("name")
        private String name;
        @XStreamAlias("portrait")
        private String portrait;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }
    }

}
