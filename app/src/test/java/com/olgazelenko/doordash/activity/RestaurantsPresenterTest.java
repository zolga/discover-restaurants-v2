package com.olgazelenko.doordash.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;
import android.util.Log;

import com.olgazelenko.doordash.R;
import com.olgazelenko.doordash.RestServiceTestHelper;
import com.olgazelenko.doordash.model.Restaurant;
import com.olgazelenko.doordash.network.RestaurantService;
import com.olgazelenko.doordash.network.RetrofitClientInstance;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subscribers.TestSubscriber;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static io.reactivex.internal.util.NotificationLite.getValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class RestaurantsPresenterTest extends InstrumentationTestCase {
    @Mock
    RestaurantService restaurantService;

    @Mock
    Call<List<Restaurant>> call;

    @Mock
    RestaurantsActivity restaurantsActivity;
    RestaurantsMainViewInterface restaurantsActivityInterface;
    RestaurantsPresenter presenter;
    @Mock
    RecyclerView recyclerView;

    MockWebServer mockServer;

    @Before
    public void createService() {
        mockServer = new MockWebServer();
        restaurantService = new Retrofit.Builder()
                .baseUrl(mockServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RestaurantService.class);

        presenter = new RestaurantsPresenter();
//        presenter.setView(restaurantsActivityInterface);

        restaurantsActivity = new RestaurantsActivity();
        restaurantsActivity.setupMVP();
    }

    @After
    public void stopService() throws IOException {
        mockServer.shutdown();
    }

    @Test
    public void restaurantListAdaptor_AdaptorLoadedTheList() throws IOException {
        enqueueResponse("quote_200_ok_response.json");

        call = getValue(restaurantService.getRestaurantList(37.422740, -122.139956, 0, 20));
        List<Restaurant> restaurants = call.execute().body();

        presenter.mvi.displayRestaurant(restaurants);
    }

    @Test
    public void restaurantList_200OkResponse_InvokesCorrectApiCalls() throws Exception {
        enqueueResponse("quote_200_ok_response.json");

        call = getValue(restaurantService.getRestaurantList(37.422740, -122.139956, 0, 20));
        List<Restaurant> restaurants = call.execute().body();

        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getPath(), is("/restaurant/?lat=37.42274&lng=-122.139956&offset=0&limit=20"));

        assertThat(restaurants.size(), is(2));

        Restaurant restaurant = restaurants.get(0);
        assertThat(restaurant.getName(), is("name1"));

        Restaurant restaurant2 = restaurants.get(1);
        assertThat(restaurant2.getName(), is("name2"));
    }

    @Test
    public void restaurantList_IOExceptionThenSuccess_RetriedApiCall() throws IOException {
        enqueueResponse("quote_empty_file.json");

        call = getValue(restaurantService.getRestaurantList(37.422740, -122.139956, 0, 20));
        try {
            List<Restaurant> restaurants = call.execute().body();
            assertThat(restaurants.size(), is(2));
        }catch (IOException e){

        }
    }

    @Test
    public void restaurantListWithLimit_200OkResponse_InvokesCorrectApiCalls() throws Exception {
        enqueueResponse("quote_200_ok_response.json");

        call = getValue(restaurantService.getRestaurantList(37.422740, -122.139956, 0, 1));
        List<Restaurant> restaurants = call.execute().body();

        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getPath(), is("/restaurant/?lat=37.42274&lng=-122.139956&offset=0&limit=20"));

        assertThat(restaurants.size(), is(1));

        Restaurant restaurant = restaurants.get(0);
        assertThat(restaurant.getName(), is("name1"));
    }
    private void enqueueResponse(String fileName) {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(RestServiceTestHelper.readFile( this.getClass().getClassLoader(), fileName)));
    }

}