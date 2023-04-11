package com.example.e_commercial_application.Model;

public class NewSeason {

    private String ProductName;
    private String ProductPrice;
    private String ProductImg;

    // no-argument constructor required by Firebase Firestore SDK
    public NewSeason() {
    }

    public NewSeason(String productName, String productPrice,String productImg) {
        ProductName = productName;
        ProductPrice = productPrice;
        ProductImg = productImg;
    }


    public String getProductName() {
        return ProductName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public String getProductImg() {
        return ProductImg;
    }
    // getters and setters for title and image
}

