package com.example.dell.unittv.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class ShopProblemNotDealItem {
    private String shopID;
    private String shopName;
    private String parentID;
    private String timestamp;
    private String imageName;
    private String problem;


    public ShopProblemNotDealItem(String shopID, String shopName, String parentID, String timestamp, String imageName, String problem) {
        setShopID(shopID);
        setShopName(shopName);
        setParentID(parentID);
        setTimestamp(timestamp);
        setImageName(imageName);
        setProblem(problem);

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

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopName() {
        return shopName;
    }
}

