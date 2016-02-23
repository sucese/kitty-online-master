package com.guoxiaoxing.kitty.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * 广告栏实体类
 */

@AVClassName("AdvertisementBanner")
public class AdvertisementBanner extends AVObject {

    public static final Creator CREATOR = AVObjectCreator.instance;

    public static final String IMAGE_URL = "imageUrl";

    public String getImageUrl() {
        return getString(IMAGE_URL);
    }

    public void setImageUrl(String imageUrl) {
        put(IMAGE_URL, imageUrl);
    }
}
