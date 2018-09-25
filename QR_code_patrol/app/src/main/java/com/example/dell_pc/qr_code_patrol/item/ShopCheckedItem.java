package com.example.dell_pc.qr_code_patrol.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class ShopCheckedItem {
    private String cardNo;
    private String shopID;
    private String parentID;
    private String timestamp;
    private String imageName;
    private String problemID;
    private String problem;
    private String Statue;


    public ShopCheckedItem(String cardNo, String shopID, String parentID, String timestamp, String imageName, String problem, String problemID, String Statue) {
        setCardNo(cardNo);
        setShopID(shopID);
        setParentID(parentID);
        setTimestamp(timestamp);
        setImageName(imageName);
        setProblem(problem);
        setProblemID(problemID);
        setStatue(Statue);

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


    public void setProblemID(String problemID) {
        this.problemID = problemID;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getProblemID() {
        return problemID;
    }

    public String getProblem() {
        return problem;
    }

    public String getStatue(){
        return Statue;
    }

    public void setStatue(String Statue){
        this.Statue = Statue;
    }

}

