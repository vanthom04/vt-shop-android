package com.vanthom04.vtshop.models;

public class Product {
    private final String productId;
    private final String name;
    private final int price;
    private final String thumbnail;

    public Product(String productId, String name, int price, String thumbnail) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.thumbnail = thumbnail;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
