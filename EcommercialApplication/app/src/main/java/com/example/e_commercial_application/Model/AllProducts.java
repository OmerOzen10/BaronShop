package com.example.e_commercial_application.Model;

import java.io.Serializable;

public class AllProducts implements Serializable {

    private String ProductName,ProductImg;
    private double ProductPrice;

    public AllProducts(){

    }

    public AllProducts(String productName, String productImg, double productPrice) {
        ProductName = productName;
        ProductImg = productImg;
        ProductPrice = productPrice;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getProductImg() {
        return ProductImg;
    }

    public double getProductPrice() {
        return ProductPrice;
    }
}
