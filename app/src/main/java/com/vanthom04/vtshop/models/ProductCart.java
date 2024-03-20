package com.vanthom04.vtshop.models;

import java.io.Serializable;

public class ProductCart implements Serializable {
    private final String productId;
    private final String name;
    private final String thumbnail;
    private final int price;
    private final int quantity;

    public ProductCart(String productId, String name, String thumbnail, int price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.thumbnail = thumbnail;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }
    public String getThumbnail() {
        return thumbnail;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
