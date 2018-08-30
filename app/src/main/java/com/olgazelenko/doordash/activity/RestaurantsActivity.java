package com.olgazelenko.doordash.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.olgazelenko.doordash.R;
import com.olgazelenko.doordash.adapter.RestaurantAdaptor;
import com.olgazelenko.doordash.model.Restaurant;
import com.olgazelenko.doordash.network.RestaurantService;
import com.olgazelenko.doordash.utils.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantsActivity extends AppCompatActivity implements RestaurantsMainViewInterface {
    private final String URL_REQUEST = "URL_REQUEST";
    private final String TAG = "RestaurantsActivity";

    @BindView(R.id.list)
    RecyclerView restaurantRecyclerView;

    @BindView(R.id.empty_view)
    TextView emptyView;

    private RestaurantsPresenter restaurantsPresenter;
    private RestaurantAdaptor adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurants_activity);
        InitViewById();

        setupMVP();
        setupViews();
        getRestaurantsList();
    }

    private void InitViewById() {
        this.setTitle(getResources().getString(R.string.activity_label_disover));
        ButterKnife.bind(this);
        restaurantRecyclerView.addItemDecoration(new DividerItemDecoration(
                getApplicationContext()
        ));
    }


    public void setupMVP() {
        restaurantsPresenter = new RestaurantsPresenter();
        restaurantsPresenter.setView(this);
    }

    public void setupViews() {
        restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void getRestaurantsList() {
        restaurantsPresenter.getRestaurants();
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(RestaurantsActivity.this, str, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayRestaurant(List<Restaurant> restaurants) {
        if (restaurants != null && restaurants.size() > 0) {
            adapter = new RestaurantAdaptor(RestaurantsActivity.this , restaurants);
            restaurantRecyclerView.setAdapter(adapter);
        } else {
            emptyView.setText(R.string.msg_no_restaurants);
        }
    }

    @Override
    public void displayError(int resid) {

        emptyView.setText(resid);

    }
}




