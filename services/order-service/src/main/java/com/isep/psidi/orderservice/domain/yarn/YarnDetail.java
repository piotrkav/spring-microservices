package com.isep.psidi.orderservice.domain.yarn;

public class YarnDetail {

    private String codePercentage;
    private int numberOfCables;
    private int numberOfFilaments;
    private int NETitle;
    private int TMP;

    public String getCodePercentage() {
        return codePercentage;
    }

    public void setCodePercentage(String codePercentage) {
        this.codePercentage = codePercentage;
    }

    public int getNumberOfCables() {
        return numberOfCables;
    }

    public void setNumberOfCables(int numberOfCables) {
        this.numberOfCables = numberOfCables;
    }

    public int getNumberOfFilaments() {
        return numberOfFilaments;
    }

    public void setNumberOfFilaments(int numberOfFilaments) {
        this.numberOfFilaments = numberOfFilaments;
    }

    public int getNETitle() {
        return NETitle;
    }

    public void setNETitle(int NETitle) {
        this.NETitle = NETitle;
    }

    public int getTMP() {
        return TMP;
    }

    public void setTMP(int TMP) {
        this.TMP = TMP;
    }
}
