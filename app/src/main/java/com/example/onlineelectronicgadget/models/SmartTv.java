package com.example.onlineelectronicgadget.models;

import java.util.List;

public class SmartTv extends Product {
    private String screenSize, resolution, displayTechnology, os, ports, connectivity, smartFeatures, sound, dimension, color, warranty;
    private double weight;

    public SmartTv() {}

    public SmartTv(String id, String brand, String model, long price, String description, List<String> imagesId,
                   int stocks, double rating, List<String> reviews, String category, String screenSize, String resolution,
                   String displayTechnology, String os, String ports, String connectivity, String smartFeatures,
                   String sound, double weight, String dimension, String color, String warranty) {
        super(id, brand, model, price, description, imagesId, stocks, rating, reviews, category);
        this.screenSize = screenSize;
        this.resolution = resolution;
        this.displayTechnology = displayTechnology;
        this.os = os;
        this.ports = ports;
        this.connectivity = connectivity;
        this.smartFeatures = smartFeatures;
        this.sound = sound;
        this.weight = weight;
        this.dimension = dimension;
        this.color = color;
        this.warranty = warranty;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getDisplayTechnology() {
        return displayTechnology;
    }

    public void setDisplayTechnology(String displayTechnology) {
        this.displayTechnology = displayTechnology;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getPorts() {
        return ports;
    }

    public void setPorts(String ports) {
        this.ports = ports;
    }

    public String getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(String connectivity) {
        this.connectivity = connectivity;
    }

    public String getSmartFeatures() {
        return smartFeatures;
    }

    public void setSmartFeatures(String smartFeatures) {
        this.smartFeatures = smartFeatures;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    @Override
    public String toString() {
        return super.toString() + "SmartTv{" +
                "screenSize='" + screenSize + '\'' +
                ", resolution='" + resolution + '\'' +
                ", displayTechnology='" + displayTechnology + '\'' +
                ", os='" + os + '\'' +
                ", ports='" + ports + '\'' +
                ", connectivity='" + connectivity + '\'' +
                ", smartFeatures='" + smartFeatures + '\'' +
                ", sound='" + sound + '\'' +
                ", weight=" + weight +
                ", dimension='" + dimension + '\'' +
                ", color='" + color + '\'' +
                ", warranty='" + warranty + '\'' +
                '}';
    }
}
