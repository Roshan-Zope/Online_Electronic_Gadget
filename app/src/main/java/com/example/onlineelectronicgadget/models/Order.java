package com.example.onlineelectronicgadget.models;

import java.util.List;

public class Order {
    private String uid, date;
    private List<Product> products;

    public Order(String uid, String date, List<Product> products) {
        this.uid = uid;
        this.date = date;
        this.products = products;
    }

    public Order() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Order{" +
                "uid='" + uid + '\'' +
                ", date='" + date + '\'' +
                ", products=" + products +
                '}';
    }
}
