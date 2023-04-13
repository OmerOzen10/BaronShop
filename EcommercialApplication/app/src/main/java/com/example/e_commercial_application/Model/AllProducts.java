package com.example.e_commercial_application.Model;

public class AllProducts {

    private String ProductNameAll,ProductImgAll;
    private double ProductPriceAll;

    public AllProducts(){

    }

    public AllProducts(String productNameAll, String productImgAll, double productPriceAll) {
        ProductNameAll = productNameAll;
        ProductImgAll = productImgAll;
        ProductPriceAll = productPriceAll;
    }

    public String getProductNameAll() {
        return ProductNameAll;
    }

    public String getProductImgAll() {
        return ProductImgAll;
    }

    public double getProductPriceAll() {
        return ProductPriceAll;
    }
}
