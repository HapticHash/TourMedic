package com.classify.dummy;

/**
 * Created by ADMIN on 03-12-2017.
 */

public class unseen_class {
    public String desc;
    public String imageurl;
    public String place;

    public unseen_class()
    {

    }

    public unseen_class(String desc, String imageurl, String place) {
        this.desc = desc;
        this.imageurl = imageurl;
        this.place = place;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
