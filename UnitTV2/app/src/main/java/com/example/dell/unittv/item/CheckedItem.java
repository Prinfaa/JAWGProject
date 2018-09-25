package com.example.dell.unittv.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class CheckedItem {
    private String cardNo;
    private String Position;
    private String dateTime;
    private String imageName;
    private String Fac;
    private String problemID;
    private String problem;



    public CheckedItem(String cardNo, String Position, String dateTime, String imageName, String Fac, String problem, String problemID) {
        setCardNo(cardNo);
        setPosition(Position);
        setDateTime(dateTime);
        setImageName(imageName);
        setFac(Fac);
        setProblem(problem);
        setProblemID(problemID);


    }

    private void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardNo() {
        return cardNo;
    }




    public String getPosition() {
        return Position;
    }

    public void setPosition(String Position) {
        this.Position = Position;
    }


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getImageName(){
        return imageName;
    }

    public void setImageName(String imageName){
        this.imageName = imageName;
    }

    public String getFac(){
        return Fac;
    }

    public void setFac(String Fac){
        this.Fac = Fac;
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

}

