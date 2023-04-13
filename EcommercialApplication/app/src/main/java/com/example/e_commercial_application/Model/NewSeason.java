package com.example.e_commercial_application.Model;

public class NewSeason {

    private String ProductName;
    private double ProductPrice;
    private String ProductImg;

    // no-argument constructor required by Firebase Firestore SDK
    public NewSeason() {
    }

    public NewSeason(String productName, double productPrice,String productImg) {
        ProductName = productName;
        ProductPrice = productPrice;
        ProductImg = productImg;
    }


    public String getProductName() {
        return ProductName;
    }

    public double getProductPrice() {
        return ProductPrice;
    }

    public String getProductImg() {
        return ProductImg;
    }
    // getters and setters for title and image
}

