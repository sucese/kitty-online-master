package net.oschina.app.team.bean;

import net.oschina.app.bean.Entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 团队属性
 * 
 * @author kymjs
 * 
 */
@SuppressWarnings("serial")
@XStreamAlias("team")
public class Team extends Entity {
    @XStreamAlias("type")
    private String type;
    @XStreamAlias("status")
    private String status;
    @XStreamAlias("name")
    private String name;
    @XStreamAlias("ident")
    private String ident;
    @XStreamAlias("createTime")
    private String createTime;
    @XStreamAlias("about")
    private About about = new About();

    public About getAbout() {
        return about;
    }

    public void setAbout(About about) {
        this.about = about;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @XStreamAlias("about")
    public class About extends Entity {
        @XStreamAlias("createTime")
        String sign;
        @XStreamAlias("address")
        String address;
        @XStreamAlias("telephone")
        String telephone;
        @XStreamAlias("email")
        String email;
        @XStreamAlias("qq")
        String qq;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }
    }
}
