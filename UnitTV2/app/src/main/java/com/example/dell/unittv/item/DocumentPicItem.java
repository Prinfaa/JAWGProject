package com.example.dell.unittv.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class DocumentPicItem {
    private String id;
    private String picUrl;
    private String type;
    private String dateTime;


    public DocumentPicItem(String id, String picUrl, String type, String dateTime) {
        setDateTime(dateTime);
        setId(id);
        setPicUrl(picUrl);
        setType(type);
    }

    public String getId() {
        return id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getType() {
        return type;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setType(String type) {
        this.type = type;
    }
}

