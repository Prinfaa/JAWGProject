package com.example.dell_pc.microstationreceivealarm.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class UnitItem {
    private String ID;
    private String name;
    private String linkman;
    private String phone;
    private String goods;
    private String position;


    public UnitItem(String ID, String name, String linkman, String phone, String goods, String position) {
        setID(ID);
        setName(name);
        setGoods(goods);
        setLinkman(linkman);
        setPhone(phone);
        setPosition(position);

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getGoods() {
        return goods;
    }

    public String getLinkman() {
        return linkman;
    }

    public String getPhone() {
        return phone;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}

