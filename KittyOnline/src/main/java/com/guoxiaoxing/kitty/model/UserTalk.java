package com.guoxiaoxing.kitty.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户说说实体类
 */

@AVClassName("UserTalk")
public class UserTalk extends AVObject {

    public static final Creator CREATOR = AVObjectCreator.instance;

    public static final String IMAGE_URLS = "imageUrls";


    public List<String> getImageUrlList() {
        return getList(IMAGE_URLS);
    }

    public void setImageUrlList(ArrayList<String> imageUrlList) {
        put(IMAGE_URLS, imageUrlList);
    }

}
