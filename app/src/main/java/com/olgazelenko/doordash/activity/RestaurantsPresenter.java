package com.olgazelenko.doordash.activity;

import android.util.Log;

import com.olgazelenko.doordash.R;
import com.olgazelenko.doordash.model.Restaurant;
import com.olgazelenko.doordash.network.RestaurantService;
import com.olgazelenko.doordash.network.RetrofitClientInstance;

import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsPresenter implements RestaurantsPresenterInterface {

    RestaurantsMainViewInterface mvi;
    private String TAG = "RestaurantsPresenter";
    private final String URL_REQUEST = "URL_REQUEST";
    private RestaurantService service;

    public RestaurantsPresenter() {
        this.service = RetrofitClientInstance.getClient().create(RestaurantService.class);
    }

    @Override
    public void setView(RestaurantsMainViewInterface mvi) {
        this.mvi = mvi;
    }

    @Override
    public void getRestaurants() {
        /*Create handle for the RetrofitInstance interface*/
        Call<List<Restaurant>> call = service.getRestaurantList(37.422740, -122.139956, 0, 20);
            call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if (!response.isSuccessful()) {
                    Log.d(URL_REQUEST, response.raw().request().url().toString());
                    mvi.displayError(R.string.msg_error);
                }
                else
                    mvi.displayRestaurant(response.body());
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                if (t instanceof IOException)
                    mvi.displayError(R.string.no_internet_connection);
                else
                    mvi.displayError(R.string.msg_error);
            }
        });
    }
}





