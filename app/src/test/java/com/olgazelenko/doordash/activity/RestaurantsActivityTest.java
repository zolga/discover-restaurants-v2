package com.olgazelenko.doordash.activity;

import io.reactivex.Observable;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.olgazelenko.doordash.R;
import com.olgazelenko.doordash.model.Restaurant;
import com.olgazelenko.doordash.network.RestaurantService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.olgazelenko.doordash.RestServiceTestHelper.enqueueResponse;
import static io.reactivex.internal.util.NotificationLite.getValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
    public class RestaurantsActivityTest extends  ActivityInstrumentationTestCase2<RestaurantsActivity> {
    @Mock
    RestaurantsActivity restaurantsActivity;
    MockWebServer mockServer;

    @Mock
    RestaurantService restaurantService;

    public RestaurantsActivityTest() {
        super(RestaurantsActivity.class);
    }
    @Before
    public void setUp() throws Exception {
        mockServer = new MockWebServer();
        restaurantService = new Retrofit.Builder()
                .baseUrl(mockServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RestaurantService.class);

        restaurantsActivity = Robolectric.setupActivity(RestaurantsActivity.class);
//        assertTrue(activity.getTitle().toString().equals(String.valueOf(R.string.activity_label_disover)));

//        restaurantsActivity = getActivity();
//        restaurantsActivity.setContentView(R.layout.restaurants_activity);
    }

    @Test
    public void restaurantsList_NoRestuarant_EmptyViewMessage()
    {
        List<Restaurant> restaurants = new ArrayList<>();
        restaurantsActivity.displayRestaurant(restaurants);

        TextView emptyView = restaurantsActivity.findViewById(R.id.empty_view);
        String result = emptyView.getText().toString();
        String expected = restaurantsActivity.getString(R.string.msg_no_restaurants);
        assertEquals(result,expected);
    }

    @Test
    public void restaurantsList_IOException_EmptyViewMessage()
    {
        List<Restaurant> restaurants = null;
        restaurantsActivity.displayRestaurant(restaurants);

        TextView emptyView = restaurantsActivity.findViewById(R.id.empty_view);
        String result = emptyView.getText().toString();
        String expected = restaurantsActivity.getString(R.string.no_internet_connection);
        assertEquals(result,expected);
    }
}