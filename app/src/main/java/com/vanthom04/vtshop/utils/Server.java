package com.vanthom04.vtshop.utils;

public class Server {
    public static final String BASE_URL = "https://2afd-115-75-223-20.ngrok-free.app/api/v1";

    // SLIDES
    public static final String GET_ALL_SLIDES = BASE_URL + "/slides";

    // AUTHENTICATION
    public static final String REGISTER_USER_URL = BASE_URL + "/auth/register";
    public static final String LOGIN_USER_URL = BASE_URL + "/auth/login";
    public static final String VERIFY_ACCOUNT = BASE_URL + "/auth/verify";
    public static final String GET_USER_BY_ID = BASE_URL + "/users/";

    // BRANDS
    public static final String GET_ALL_BRANDS = BASE_URL + "/brands";
    public static final String GET_PRODUCTS_BY_BRAND = BASE_URL + "/brands/";

    // CATEGORIES
    public static final String GET_ALL_CATEGORIES = BASE_URL + "";

    // PRODUCTS
    public static final String GET_ALL_PRODUCTS = BASE_URL + "/products";
    public static final String GET_PRODUCT_BY_ID = BASE_URL + "/products/";

    // CARTS
    public static final String GET_CART_BY_USER = BASE_URL + "/carts/";
    public static final String ADD_PRODUCT_TO_CART = BASE_URL + "/carts/";
    public static final String REMOVE_PRODUCT_TO_CART = BASE_URL + "/carts/";
    public static final String UPDATE_QUANTITY_PRODUCT_CART = BASE_URL + "/carts/";
}
