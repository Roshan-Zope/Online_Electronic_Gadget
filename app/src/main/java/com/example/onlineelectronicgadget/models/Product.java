package com.example.onlineelectronicgadget.models;

import java.util.Arrays;

public class Product {
    private int id;
    private String brand;
    private String model;
    private long price;
    private String description;
    private int[] imagesId;
    private int stocks;
    private double rating;
    private String[] reviews;

    public Product(int id, String brand, String model, long price, String description, int[] imagesId,
                   int stocks, double rating, String[] reviews) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.description = description;
        this.imagesId = imagesId;
        this.stocks = stocks;
        this.rating = rating;
        this.reviews = reviews;
    }

    public String[] getReviews() {
        return reviews;
    }

    public void setReviews(String[] reviews) {
        this.reviews = reviews;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getStocks() {
        return stocks;
    }

    public void setStocks(int stocks) {
        this.stocks = stocks;
    }

    public int[] getImagesId() {
        return imagesId;
    }

    public void setImagesId(int[] imagesId) {
        this.imagesId = imagesId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", imagesId=" + Arrays.toString(imagesId) +
                ", stocks=" + stocks +
                ", rating=" + rating +
                ", reviews=" + Arrays.toString(reviews) +
                '}';
    }
}
