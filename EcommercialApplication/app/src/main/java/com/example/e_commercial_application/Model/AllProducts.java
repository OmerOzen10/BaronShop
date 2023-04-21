package com.example.e_commercial_application.Model;

import java.io.Serializable;

public class AllProducts implements Serializable {

    private String ProductName,ProductImg;
    private double ProductPrice;
    private float ProductRate;

    private int number = 1;

    public AllProducts(){

    }

    public AllProducts(String productName, String productImg, double productPrice, float productRate) {
        ProductName = productName;
        ProductImg = productImg;
        ProductPrice = productPrice;
        ProductRate = productRate;
    }

    public float getProductRate() {
        return ProductRate;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
