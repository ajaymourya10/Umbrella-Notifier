package com.udit.umbrellanotify.Model;

public class Sys {
    private int type;
    private int id;
    private double message;
    private int sunrise;
    private int sunset;
    private String country;

    public Sys(int type, int id, double message,int sunrise, int sunset, String country) {
        this.type = type;
        this.id = id;
        this.message = message;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.country = country;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

