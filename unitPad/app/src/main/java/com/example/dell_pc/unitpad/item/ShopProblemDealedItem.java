package com.example.dell_pc.unitpad.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class ShopProblemDealedItem {
    private String shopID;
    private String shopName;
    private String parentID;
    private String timestamp;
    private String imageName;
    private String problem;
    private String dealPic;
    private String dealTimestamp;


    public ShopProblemDealedItem(String shopID, String shopName, String parentID, String timestamp, String imageName, String problem, String dealPic, String dealTimestamp) {
        setShopID(shopID);
        setShopName(shopName);
        setParentID(parentID);
        setTimestamp(timestamp);
        setImageName(imageName);
        setProblem(problem);
        setDealPic(dealPic);
        setDealTimestamp(dealTimestamp);
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

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getProblem() {
        return problem;
    }

    public String getDealPic() {
        return dealPic;
    }

    public String getDealTimestamp() {
        return dealTimestamp;
    }

    public void setDealPic(String dealPic) {
        this.dealPic = dealPic;
    }

    public void setDealTimestamp(String dealTimestamp) {
        this.dealTimestamp = dealTimestamp;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}

