package com.classify.dummy;

/**
 * Created by ADMIN on 29-11-2017.
 */

public class plan_class {
    public String from;
    public String plan_no;
    public String planname;
    public String to;
    public String totalplace;
    public String lat;
    public String lon;
    public String placename;

    public plan_class() {}

    public plan_class(String from, String plan_no, String planname, String to, String totalplace) {
        this.from = from;
        this.plan_no = plan_no;
        this.planname = planname;
        this.to = to;
        this.totalplace = totalplace;
    }

    public plan_class(String lat, String lon, String placename) {
        this.lat = lat;
        this.lon = lon;
        this.placename = placename;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPlan_no() {
        return plan_no;
    }

    public void setPlan_no(String plan_no) {
        this.plan_no = plan_no;
    }

    public String getPlanname() {
        return planname;
    }

    public void setPlanname(String planname) {
        this.planname = planname;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTotalplace() {
        return totalplace;
    }

    public void setTotalplace(String totalplace) {
        this.totalplace = totalplace;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getPlacename() {
        return placename;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
    }
}
