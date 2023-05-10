package com.example.e_commercial_application.Model;

import java.io.Serializable;

public class DiscountedProducts implements Serializable {

    private String ProductName,ProductImg;
    private double ProductPrice;
    private float ProductRate;
    private double OldPrice;
    private int number = 1;
    private String id;

    private String favStatus;

    public DiscountedProducts(){
    }

    public DiscountedProducts(String productName, String productImg, double productPrice, float productRate, double oldPrice) {
        ProductName = productName;
        ProductImg = productImg;
        ProductPrice = productPrice;
        ProductRate = productRate;
        OldPrice = oldPrice;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductImg() {
        return ProductImg;
    }

    public void setProductImg(String productImg) {
        ProductImg = productImg;
    }

    public double getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(double productPrice) {
        ProductPrice = productPrice;
    }

    public float getProductRate() {
        return ProductRate;
    }

    public void setProductRate(float productRate) {
        ProductRate = productRate;
    }

    public double getOldPrice() {
        return OldPrice;
    }

    public void setOldPrice(double oldPrice) {
        OldPrice = oldPrice;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }
}
