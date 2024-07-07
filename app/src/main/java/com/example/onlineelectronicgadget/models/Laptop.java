package com.example.onlineelectronicgadget.models;

import java.util.List;

public class Laptop extends Product {
    private String processor, ram, storage, graphics, display, os, batteryLife, ports, dimension, color, warranty;
    private double weight;

    public Laptop(String id, String brand, String model, long price, String description, List<String> imagesId,
                  int stocks, double rating, List<String> reviews, String category, String processor, String ram, String storage,
                  String graphics, String display, String os, String batteryLife, double weight,
                  String dimension, String color, String ports, String warranty) {

        super(id, brand, model, price, description, imagesId, stocks, rating, reviews, category);
        this.processor = processor;
        this.ram = ram;
        this.storage = storage;
        this.graphics = graphics;
        this.display = display;
        this.os = os;
        this.batteryLife = batteryLife;
        this.weight = weight;
        this.dimension = dimension;
        this.color = color;
        this.ports = ports;
        this.warranty = warranty;
    }

    public Laptop() {
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getGraphics() {
        return graphics;
    }

    public void setGraphics(String graphics) {
        this.graphics = graphics;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
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

    public String getPorts() {
        return ports;
    }

    public void setPorts(String ports) {
        this.ports = ports;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    @Override
    public String toString() {
        return super.toString() + "Laptop{" +
                "processor='" + processor + '\'' +
                ", ram='" + ram + '\'' +
                ", storage='" + storage + '\'' +
                ", graphics='" + graphics + '\'' +
                ", display='" + display + '\'' +
                ", os='" + os + '\'' +
                ", batteryLife='" + batteryLife + '\'' +
                ", ports='" + ports + '\'' +
                ", weight=" + weight +
                ", dimension='" + dimension + '\'' +
                ", color='" + color + '\'' +
                ", warranty='" + warranty + '\'' +
                '}';
    }
}
