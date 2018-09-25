package com.example.dell_pc.qr_code_patrol.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class ProblemItem {
    private String cardNo;
    private String problemID;
    private String problem;
    private boolean isChecked;


    public ProblemItem(String cardNo, String problemID, String problem, boolean isChecked) {
        setCardNo(cardNo);
        setProblemID(problemID);
        setProblem(problem);
        setChecked(isChecked);
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getProblem() {
        return problem;
    }

    public String getProblemID() {
        return problemID;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public void setProblemID(String problemID) {
        this.problemID = problemID;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}

