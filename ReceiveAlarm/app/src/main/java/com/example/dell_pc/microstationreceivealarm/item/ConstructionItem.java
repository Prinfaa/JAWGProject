package com.example.dell_pc.microstationreceivealarm.item;

import android.graphics.Bitmap;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class ConstructionItem {
    private String ID;
    private String name;
    private String address;
    private String type;
    private String structure;
    private String area;
    private String layer;
    private String height;
    private String fac;
    private String goods;
    private String picUrl;


    public ConstructionItem(String ID, String name, String address, String type, String structure, String area, String layer, String height, String fac, String goods, String picUrl) {
        setAddress(address);
        setArea(area);
        setFac(fac);
        setGoods(goods);
        setHeight(height);
        setID(ID);
        setLayer(layer);
        setName(name);
        setPicUrl(picUrl);
        setStructure(structure);
        setType(type);

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getArea() {
        return area;
    }

    public String getFac() {
        return fac;
    }

    public String getGoods() {
        return goods;
    }

    public String getHeight() {
        return height;
    }

    public String getID() {
        return ID;
    }

    public String getLayer() {
        return layer;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getStructure() {
        return structure;
    }

    public String getType() {
        return type;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setFac(String fac) {
        this.fac = fac;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public void setType(String type) {
        this.type = type;
    }
}

