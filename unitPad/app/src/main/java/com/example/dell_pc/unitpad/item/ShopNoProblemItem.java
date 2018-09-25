package com.example.dell_pc.unitpad.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class ShopNoProblemItem {
    private String id;
    private String cardNo;
    private String shopID;
    private String shopName;
    private String parentID;
    private String timestamp;
    private String imageName;


    public ShopNoProblemItem(String id, String cardNo, String shopID, String shopName, String parentID, String timestamp, String imageName) {
        setId(id);
        setCardNo(cardNo);
        setShopID(shopID);
        setShopName(shopName);
        setParentID(parentID);
        setTimestamp(timestamp);
        setImageName(imageName);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    private void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getParentID() {
        return parentID;
    }

    public String getShopID() {
        return shopID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageName(){
        return imageName;
    }

    public void setImageName(String imageName){
        this.imageName = imageName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}

