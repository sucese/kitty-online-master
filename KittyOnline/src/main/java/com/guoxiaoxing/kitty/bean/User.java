package com.guoxiaoxing.kitty.bean;

import com.avos.avoscloud.AVUser;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * 登录用户实体类
 *
 * @author guoxiaoxing
 */
@SuppressWarnings("serial")
@XStreamAlias("user")
public class User extends AVUser implements Serializable {

    public final static int RELATION_ACTION_DELETE = 0x00;// 取消关注
    public final static int RELATION_ACTION_ADD = 0x01;// 加关注

    public final static int RELATION_TYPE_BOTH = 0x01;// 双方互为粉丝
    public final static int RELATION_TYPE_FANS_HIM = 0x02;// 你单方面关注他
    public final static int RELATION_TYPE_NULL = 0x03;// 互不关注
    public final static int RELATION_TYPE_FANS_ME = 0x04;// 只有他关注我

    private final static String ID = "id";
    private final static String FAN = "fan";
    private final static String ATTENTION = "attention";
    private final static String RELATIONSHIP = "relationship";
    private final static String FACE = "face";
    private final static String GENDER = "gender";
    private final static String BIRTHDAY = "birthday";
    private final static String LOCATION = "location";
    private final static String ADDRESS = "address";
    private final static String SIGNATURE = "signature";



    public int getId() {
        return getInt(ID);
    }

    public void setId(int id) {
        put(ID, id);
    }

    public int getFan() {
        return getInt(FAN);
    }

    public void setFan(int fan) {
        put(FAN, fan);
    }

    public int getAttention() {
        return getInt(ATTENTION);
    }

    public void setAttention(int attention) {
        put(ATTENTION, attention);
    }

    public int getRelationship() {
        return getInt(RELATIONSHIP);
    }

    public void setRelationship(int relationship) {
        put(SIGNATURE, relationship);
    }

    public String getFace() {
        return getString(FACE);
    }

    public void setFace(String face) {
        put(FACE, face);
    }

    public String getGender() {
        return getString(GENDER);
    }

    public void setGender(String gender) {
        put(GENDER, gender);
    }

    public String getBirthday() {
        return getString(BIRTHDAY);
    }

    public void setBirthday(String birthday) {
        put(BIRTHDAY, birthday);
    }

    public String getLocation() {
        return getString(LOCATION);
    }

    public void setLocation(String name) {
        put(LOCATION, name);
    }

    public String getAddress() {
        return getString(ADDRESS);
    }

    public void setAddress(String address) {
        put(ADDRESS, address);
    }

    public String getSignature() {
        return getString(SIGNATURE);
    }

    public void setSignature(String signature) {
        put(SIGNATURE, signature);
    }




}