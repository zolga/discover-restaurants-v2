package com.olgazelenko.doordash.network;

import com.olgazelenko.doordash.model.Restaurant;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestaurantService {

    @GET("restaurant/")
    Call<List<Restaurant>> getRestaurantList(
            @Query("lat") double lat,
            @Query("lng") double lng,
            @Query("offset") int offset,
            @Query("limit") int limit
    );
}

