package com.example.onlineelectronicgadget.models;

import java.util.List;

public class SmartWatches extends Product {
    private String processor, display, batteryLife, color, connectivity, sensor, waterResistance, warranty;
    private double weight;

    public SmartWatches() {}

    public SmartWatches(String id, String brand, String model, long price, String description, List<String> imagesId,
                        int stocks, double rating, List<String> reviews, String category, String processor, String display,
                        String batteryLife, double weight, String color, String connectivity, String sensor,
                        String waterResistance, String warranty) {
        super(id, brand, model, price, description, imagesId, stocks, rating, reviews, category);
        this.processor = processor;
        this.display = display;
        this.batteryLife = batteryLife;
        this.weight = weight;
        this.color = color;
        this.connectivity = connectivity;
        this.sensor = sensor;
        this.waterResistance = waterResistance;
        this.warranty = warranty;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getBatteryLife() {
        return batteryLife;
    }

    public void setBatteryLife(String batteryLife) {
        this.batteryLife = batteryLife;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(String connectivity) {
        this.connectivity = connectivity;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getWaterResistance() {
        return waterResistance;
    }

    public void setWaterResistance(String waterResistance) {
        this.waterResistance = waterResistance;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    @Override
    public String toString() {
        return "SmartWatches{" +
                "processor='" + processor + '\'' +
                ", display='" + display + '\'' +
                ", batteryLife='" + batteryLife + '\'' +
                ", weight=" + weight +
                ", color='" + color + '\'' +
                ", connectivity='" + connectivity + '\'' +
                ", sensor='" + sensor + '\'' +
                ", waterResistance='" + waterResistance + '\'' +
                ", warranty='" + warranty + '\'' +
                '}';
    }
}
