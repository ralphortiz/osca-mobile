package com.example.oscaandroiddev;

public class AuthorizeItem {

    private int productID;
    private static int counter = 0;
    private String productName;
    private String productQuantity;
    private String additionalNotes;

    public AuthorizeItem() {
    }

    public AuthorizeItem(String productName, String productQuantity, String additionalNotes) {
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.additionalNotes = additionalNotes;
        this.productID = counter++;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    @Override
    public String toString() {
        return "Product: " + productName + "; " + "Quantity: " + productQuantity + "; " +  "Notes: " + additionalNotes + "\n\n";
    }
}
