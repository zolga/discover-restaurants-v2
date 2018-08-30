package com.olgazelenko.doordash.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.olgazelenko.doordash.R;
import com.olgazelenko.doordash.model.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RestaurantAdaptor extends RecyclerView.Adapter<RestaurantAdaptor.RestaurantsViewHolder> {

    public static final String FAVORITES = "Favorits";
    private Context context;
    private List<Restaurant> RestaurantData;

    public RestaurantAdaptor(Context context, List<Restaurant> RestaurantData) {
        this.RestaurantData = RestaurantData;
        this.context = context;
    }

    @Override
    public RestaurantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.restaurant_list_item, null);
        return new RestaurantsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder holder, final int position) {
        holder.restaurant_name.setText(RestaurantData.get(position).getName());

        String foodTypeList = getTagsList(RestaurantData.get(position).getDescription());
        holder.description.setText(foodTypeList);
        holder.status.setText(RestaurantData.get(position).getStatus());

        String id = Integer.toString(RestaurantData.get(position).getId());
        String isFavoriteStatus = getIsFavoriteById(id);
        if (isFavoriteStatus != null)
            holder.isFavotire.setImageResource(R.drawable.ic_favorite_black_full);
        else
            holder.isFavotire.setImageResource(R.drawable.ic_favorite_start);

        holder.isFavotire.setTag(id);

        Picasso.get()
                .load(RestaurantData.get(position).getlogoUrl())
                .placeholder(R.drawable.placeholder_logo)
                .error(R.drawable.error_logo).into(holder.logoUrl);
    }

    private String getIsFavoriteById(String id) {
        SharedPreferences prefs = this.context.getApplicationContext().getSharedPreferences(FAVORITES, MODE_PRIVATE);
        return prefs.getString(id, null);
    }

    @Override
    public int getItemCount() {
        return RestaurantData.size(); // size of the list items
    }

    private String getTagsList(ArrayList<String> tags) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tags.size(); i++) {
            sb.append(tags.get(i)).append(", ");
        }

        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 2);
        return sb.toString();
    }

    class RestaurantsViewHolder extends RecyclerView.ViewHolder {
        TextView restaurant_name, description, status;
        ImageView logoUrl, isFavotire;

        private RestaurantsViewHolder(View itemView) {
            super(itemView);
            restaurant_name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            logoUrl = itemView.findViewById(R.id.logoUrl);
            status = itemView.findViewById(R.id.status);
            isFavotire = itemView.findViewById(R.id.favorite);

            isFavotire.setOnClickListener(new View.OnClickListener() {
                // The code in this method will be executed when the numbers category is clicked on.
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor prefs = context.getApplicationContext().getSharedPreferences(FAVORITES, MODE_PRIVATE).edit();
                    String restaurantId = (String) view.getTag();
                    Drawable.ConstantState current = ((ImageView)view).getDrawable().getConstantState();
                    Drawable.ConstantState stateIsFavorite = ContextCompat.getDrawable(context, R.drawable.ic_favorite_black_full).getConstantState();

                    if (current.equals(stateIsFavorite)) {
                        isFavotire.setImageResource(R.drawable.ic_favorite_start);
                        prefs.remove(restaurantId);
                    }
                    else{
                        isFavotire.setImageResource(R.drawable.ic_favorite_black_full);
                        prefs.putString(restaurantId, "true");
                    }

                    prefs.apply();
                }
                });
        }
    }
}
