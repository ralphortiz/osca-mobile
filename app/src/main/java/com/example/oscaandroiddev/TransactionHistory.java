package com.example.oscaandroiddev;

public class TransactionHistory {
    private String transDate;
    private String companyName;
    private String branch;
    private String business_type;
    private String desc;
    private String vatExemptPrice;
    private String disPrice;
    private String payPrice;

    public TransactionHistory() {
    }

    public TransactionHistory(String transDate, String companyName, String branch, String business_type, String desc, String vatExemptPrice, String disPrice, String payPrice) {
        this.transDate = transDate;
        this.companyName = companyName;
        this.branch = branch;
        this.business_type = business_type;
        this.desc = desc;
        this.vatExemptPrice = vatExemptPrice;
        this.disPrice = disPrice;
        this.payPrice = payPrice;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVatExemptPrice() {
        return vatExemptPrice;
    }

    public void setVatExemptPrice(String vatExemptPrice) {
        this.vatExemptPrice = vatExemptPrice;
    }

    public String getDisPrice() {
        return disPrice;
    }

    public void setDisPrice(String disPrice) {
        this.disPrice = disPrice;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    @Override
    public String toString() {
        return desc + companyName;
    }
}
