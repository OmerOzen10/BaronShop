package com.example.e_commercial_application.Model;

import java.io.Serializable;

public class AllProducts implements Serializable {

    private String ProductName,ProductImg;
    private double ProductPrice;
    private float ProductRate;
    private int number = 1;
    private String id;
    private String favStatus;

    public AllProducts(){}

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public void setProductImg(String productImg) {
        ProductImg = productImg;
    }

    public void setProductPrice(double productPrice) {
        ProductPrice = productPrice;
    }

    public void setProductRate(float productRate) {
        ProductRate = productRate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AllProducts(String productName, String productImg, double productPrice, float productRate, String id) {
        ProductName = productName;
        ProductImg = productImg;
        ProductPrice = productPrice;
        ProductRate = productRate;
        this.id = id;
    }

    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }

    public String getFavStatus() {
        return favStatus;
    }

    public String getId() {
        return id;
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
