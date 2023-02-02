package com.todocode.recipesdemo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.squareup.picasso.Picasso;
import com.todocode.recipesdemo.Activity.AllRecipesActivity;
import com.todocode.recipesdemo.Activity.SingleRecipeActivity;
import com.todocode.recipesdemo.Model.Recipe;
import com.todocode.recipesdemo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllRecipesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Object> recipes;
    private OnItemClickListener myListener;
    private static final int TYPE_RECIPE = 0;
    private static final int TYPE_ADMOB = 1;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(AllRecipesAdapter.OnItemClickListener recipesListener) {
        myListener = recipesListener;
    }

    public AllRecipesAdapter(Context context, List<Object> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    public class LatestRecipesHolder extends RecyclerView.ViewHolder {
        private TextView title, chef, totalTime, recipeViews, recipeCategoryName;
        private ImageView recipeimage;
        private CircleImageView chefImage;

        public LatestRecipesHolder(@NonNull View itemView, final AllRecipesAdapter.OnItemClickListener recipesListener) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.recipeTitle);
            recipeCategoryName = (TextView) itemView.findViewById(R.id.recipeCategoryName);
            chefImage = (CircleImageView) itemView.findViewById(R.id.chefImage);
            recipeimage = (ImageView) itemView.findViewById(R.id.recipe_image);
            totalTime = (TextView) itemView.findViewById(R.id.recipeTime);
            chef = (TextView) itemView.findViewById(R.id.chefName);
            recipeViews = (TextView) itemView.findViewById(R.id.recipe_views);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recipesListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recipesListener.onItemClick(position);
                        }
                    }
                }
            });
        }
        public void setDetails(final Recipe recipe) {
            title.setText(recipe.getTitle());
            chef.setText(recipe.getChef_username());
            totalTime.setText(recipe.getTime());
            recipeViews.setText(String.valueOf(recipe.getViews()));
            recipeCategoryName.setText(recipe.getCategory_name());
            Picasso.get().load(recipe.getImage_url()).fit().centerInside().into(recipeimage);
            Picasso.get().load(recipe.getChef_image()).fit().centerInside().into(chefImage);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ADMOB:
                View unifiedNativeLayoutView = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.ad_unified,
                        parent, false);
                return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
            case TYPE_RECIPE:
            default:
                View view = LayoutInflater.from(context).inflate(R.layout.single_recipe_layout, parent, false);
                return new LatestRecipesHolder(view,myListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_ADMOB:
                UnifiedNativeAd nativeAd = (UnifiedNativeAd) recipes.get(position);
                populateNativeAdView(nativeAd, ((UnifiedNativeAdViewHolder) holder).getAdView());
                break;
            case TYPE_RECIPE:
                // fall through
            default:
                LatestRecipesHolder menuItemHolder = (LatestRecipesHolder) holder;
                final Recipe photo = (Recipe) recipes.get(position);
                menuItemHolder.setDetails(photo);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent recipe = new Intent(context, SingleRecipeActivity.class);
                        recipe.putExtra("id", photo.getId());
                        recipe.putExtra("title", photo.getTitle());
                        recipe.putExtra("description", photo.getDescription());
                        recipe.putExtra("time", photo.getTime());
                        recipe.putExtra("servings", photo.getServings());
                        recipe.putExtra("calories", photo.getCalories());
                        recipe.putExtra("image_url", photo.getImage_url());
                        recipe.putExtra("video_url", photo.getVideo_url());
                        recipe.putExtra("rating", photo.getRating());
                        recipe.putExtra("category_id", photo.getCategory_id());
                        recipe.putExtra("category_name", photo.getCategory_name());
                        recipe.putExtra("chef_username", photo.getChef_username());
                        recipe.putExtra("chef_id", photo.getChef_id());
                        recipe.putExtra("chef_image", photo.getChef_image());
                        recipe.putExtra("paypal", photo.getChef_paypal());
                        recipe.putExtra("email", photo.getChef_email());
                        recipe.putExtra("chef_trusted", photo.getChef_trusted());
                        recipe.putExtra("meal_name", photo.getMeal_name());
                        recipe.putExtra("cuisine_name", photo.getCuisine_name());
                        recipe.putExtra("views", photo.getViews());
                        recipe.putExtra("gender", photo.getChef_gender());
                        recipe.putExtra("vegetarian", photo.getChef_vegetarian());
                        recipe.putExtra("facebook", photo.getChef_facebook());
                        recipe.putExtra("twitter", photo.getChef_twitter());
                        recipe.putExtra("instagram", photo.getChef_instagram());
                        recipe.putExtra("member_since", photo.getChef_member_since());
                        context.startActivity(recipe);
                    }
                });
        }
    }

    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class UnifiedNativeAdViewHolder extends RecyclerView.ViewHolder {

        private UnifiedNativeAdView adView;

        public UnifiedNativeAdView getAdView() {
            return adView;
        }

        UnifiedNativeAdViewHolder(View view) {
            super(view);
            adView = (UnifiedNativeAdView) view.findViewById(R.id.ad_view);

            // The MediaView will display a video asset if one is present in the ad, and the
            // first image asset otherwise.
            adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

            // Register the view used for each individual asset.
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_icon));
            adView.setPriceView(adView.findViewById(R.id.ad_price));
            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
            adView.setStoreView(adView.findViewById(R.id.ad_store));
            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object recyclerViewItem = recipes.get(position);
        if (recyclerViewItem instanceof UnifiedNativeAd) {
            return TYPE_ADMOB;
        }
        return TYPE_RECIPE;
    }

}
