package com.example.onlineelectronicgadget.models;

public class Order {
    private String uid, date;
    private Product product;

    public Order(String uid, String date, Product product) {
        this.uid = uid;
        this.date = date;
        this.product = product;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Order{" +
                "uid='" + uid + '\'' +
                ", date='" + date + '\'' +
                ", product=" + product +
                '}';
    }
}
