package com.olgazelenko.doordash.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Restaurant {

    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("tags")
    private ArrayList<String> description;

    @SerializedName("cover_img_url")
    String logoUrl;

    @SerializedName("status_type")
    String status;

    @SerializedName("delivery_fee")
    Double fee;

    boolean isFavorite;

    public boolean getIsFavorite() {
        return this.isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getDescription() {
        return this.description;
    }

    public void setDescription(ArrayList<String> description) {
        this.description = description;
    }

    public String getlogoUrl() {
        return this.logoUrl;
    }

    public void setlogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getFee() {
        return this.fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

}