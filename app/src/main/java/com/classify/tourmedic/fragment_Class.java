package com.classify.tourmedic;

/**
 * Created by ADMIN on 25-11-2017.
 */

public class fragment_Class {

    public String address;
    public String description;
    public String imageurl;
    public String lat;
    public String lon;
    public String no;
    public String placename;
    public String rating;
    public String trip_no;
    public String tripname;
    public String destination;
    public String enddate;
    public String startdate;


    public fragment_Class()
    {}

    public fragment_Class(String address, String description, String imageurl, String lat, String lon, String no, String placename, String rating) {
        this.address = address;
        this.description = description;
        this.imageurl = imageurl;
        this.lat = lat;
        this.lon = lon;
        this.no = no;
        this.placename = placename;
        this.rating = rating;
    }

    public fragment_Class(String imageurl, String lat, String lon, String placename, String rating) {
        this.imageurl = imageurl;
        this.lat = lat;
        this.lon = lon;
        this.placename = placename;
        this.rating = rating;
    }



    /*public fragment_Class(String tripname) {
        this.tripname = tripname;
    }*/

    public fragment_Class(String trip_no, String tripname) {
        this.trip_no = trip_no;
        this.tripname = tripname;
    }

    public fragment_Class(String destination, String enddate, String startdate) {
        this.destination = destination;
        this.enddate = enddate;
        this.startdate = startdate;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPlacename() {
        return placename;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
    }

    public String getTripname() {
        return tripname;
    }

    public void setTripname(String tripname) {
        this.tripname = tripname;
    }

    public String getTrip_no() {
        return trip_no;
    }

    public void setTrip_no(String trip_no) {
        this.trip_no = trip_no;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

}
