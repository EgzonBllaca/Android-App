package com.example.finalandroidassignment;

public class TouristDestination {
    private String name, description, image;
    private double longitude,latitude;

    public TouristDestination(String name, String description, String imageUrl, double longitude, double latitude) {
        this.name = name;
        this.description = description;
        this.image = imageUrl;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
