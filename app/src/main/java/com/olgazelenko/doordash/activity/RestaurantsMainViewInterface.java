package com.olgazelenko.doordash.activity;

import com.olgazelenko.doordash.model.Restaurant;

import java.util.List;
import android.support.annotation.NonNull;


public interface RestaurantsMainViewInterface {

    void showToast(String s);

    void displayRestaurant(@NonNull List<Restaurant> restaurants);

    void displayError(int resid);

}
