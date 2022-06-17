package com.example.civiladvocacyapp;

import java.io.Serializable;

public class Official implements Serializable {

    private String office;
    private String name;
    private String party;
    private String i_url;
    private String address;
    private String num;
    private String email;
    private String website;
    private String facebook;
    private String youtube;
    private String twitter;
//Dummy Data
    Official() {
        this.office = "President of United States ";
        this.name = "Joseph R. Biden";
        this.party = "Republican";
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public void setI_url(String i_url) {
        this.i_url = i_url;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getOffice() {
        return office;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String getI_url() {
        return i_url;
    }

    public String getAddress() {
        return address;
    }

    public String getNum() {
        return num;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getYoutube() {
        return youtube;
    }
}
