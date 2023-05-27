package com.example.e_commercial_application.Model;

import java.io.Serializable;

public class DiscountedProducts extends AllProducts implements Serializable {

    private double OldPrice;

    public DiscountedProducts(){}

    public DiscountedProducts(String productName, String productImg, double productPrice, float productRate, String id, double oldPrice) {
        super(productName, productImg, productPrice, productRate, id);
        OldPrice = oldPrice;
    }

    public double getOldPrice() {
        return OldPrice;
    }

    public void setOldPrice(double oldPrice) {
        OldPrice = oldPrice;
    }
}
