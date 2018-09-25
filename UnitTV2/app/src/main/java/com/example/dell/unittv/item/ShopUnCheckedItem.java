package com.example.dell.unittv.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class ShopUnCheckedItem {
    private String cardNo;
    private String shopID;
    private String shopName;
    private String parentID;


    public ShopUnCheckedItem(String cardNo, String shopID, String shopName, String parentID) {
        setCardNo(cardNo);
        setShopID(shopID);
        setShopName(shopName);
        setParentID(parentID);


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


    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopName() {
        return shopName;
    }
}

