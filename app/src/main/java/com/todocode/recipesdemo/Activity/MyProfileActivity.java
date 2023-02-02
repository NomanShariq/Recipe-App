package com.todocode.recipesdemo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.login.LoginManager;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.squareup.picasso.Picasso;
import com.todocode.recipesdemo.R;

import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {
    private CircleImageView profileImage;
    private TextView name, email, memberSince;
    private Button editProfile, logout;
    private ImageView trustedImage;
    private CardView addRecipe, myRecipes, favorites, inviteFriends;
    GoogleSignInClient mGoogleSignInClient;
    // Bottom Banner Ads Start
    private SharedPreferences admobBanner, facebookBanner, bannerType;
    private String bannerBottomTypeStr;
    private LinearLayout adsLinear;
    private AdView bannerAdmobAdView;
    // Bottom Banner Ads End
    private SharedPreferences userEmailSharedPrefs, userUsernameSharedPrefs, userAvatarUrlSharedPrefs, userTrustedSharedPrefs, userMemberSince, userIdSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = findViewById(R.id.my_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.my_profile));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        userEmailSharedPrefs = getSharedPreferences("userEmailSharedPrefs", MODE_PRIVATE);
        userUsernameSharedPrefs = getSharedPreferences("userUsernameSharedPrefs", MODE_PRIVATE);
        userAvatarUrlSharedPrefs = getSharedPreferences("userAvatarUrlSharedPrefs", MODE_PRIVATE);
        userTrustedSharedPrefs = getSharedPreferences("userTrustedSharedPrefs", MODE_PRIVATE);
        userMemberSince = getSharedPreferences("userMemberSince", MODE_PRIVATE);
        userIdSharedPrefs = getSharedPreferences("userIdSharedPrefs", MODE_PRIVATE);
        profileImage = (CircleImageView) findViewById(R.id.profile_picture);
        Picasso.get().load(userAvatarUrlSharedPrefs.getString("userAvatarUrlSharedPrefs", "")).fit().centerInside().into(profileImage);
        name = (TextView) findViewById(R.id.profile_name);
        name.setText(userUsernameSharedPrefs.getString("userUsernameSharedPrefs", ""));
        email = (TextView) findViewById(R.id.profile_email);
        email.setText(userEmailSharedPrefs.getString("userEmailSharedPrefs", ""));
        memberSince = (TextView) findViewById(R.id.profile_since);
        memberSince.setText("Member since : "+userMemberSince.getString("userMemberSince", ""));
        editProfile = (Button) findViewById(R.id.edit_infos_btn);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(MyProfileActivity.this,EditProfileActivity.class);
                startActivity(edit);
            }
        });
        logout = (Button) findViewById(R.id.profile_logout_btn);
        trustedImage = (ImageView) findViewById(R.id.trusted_badge_profile);
        if (userTrustedSharedPrefs.getString("userTrustedSharedPrefs","").equals("yes")) {
            trustedImage.setVisibility(View.VISIBLE);
        }
        addRecipe = (CardView) findViewById(R.id.linear_new_recipe);
        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(MyProfileActivity.this,AddNewRecipeActivity.class);
                startActivity(add);
                //finish();
            }
        });
        myRecipes = (CardView) findViewById(R.id.linear_my_recipes);
        myRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myrecipes = new Intent(MyProfileActivity.this,MyRecipesActivity.class);
                startActivity(myrecipes);
                //finish();
            }
        });
        favorites = (CardView) findViewById(R.id.linear_my_followers);
        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fav = new Intent(MyProfileActivity.this,MyFavoritesRecipes.class);
                startActivity(fav);
                //finish();
            }
        });
        inviteFriends = (CardView) findViewById(R.id.linear_invite_friends);
        inviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_TEXT, "Download this APP From : http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                startActivity(Intent.createChooser(intent, "Invite Friends"));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change Shared Preferences
                userEmailSharedPrefs.edit().putString("userEmailSharedPrefs", "").apply();
                userIdSharedPrefs.edit().putString("userIdSharedPrefs", "").apply();
                if (mGoogleSignInClient != null) {
                    mGoogleSignInClient.signOut();
                }
                if (LoginManager.getInstance() != null) {
                    LoginManager.getInstance().logOut();
                }
                Intent loginPage = new Intent(MyProfileActivity.this, WelcomeActivity.class);
                loginPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginPage);
                finishAffinity();
            }
        });
        // Banner Bottom
        admobBanner = getSharedPreferences("admobBanner", MODE_PRIVATE);
        facebookBanner = getSharedPreferences("facebookBanner", MODE_PRIVATE);
        bannerType = getSharedPreferences("bannerType",MODE_PRIVATE);
        bannerBottomTypeStr = bannerType.getString("bannerType", "");
        if (bannerBottomTypeStr.equals("admob")) {
            MobileAds.initialize(MyProfileActivity.this, getString(R.string.admob_app_id));
            admobBanner = getSharedPreferences("admobBanner",MODE_PRIVATE);
            adsLinear = (LinearLayout) findViewById(R.id.bottom_banner_main_activity);
            bannerAdmobAdView = new AdView(this);
            bannerAdmobAdView.setAdUnitId(admobBanner.getString("admobBanner", ""));
            bannerAdmobAdView.setAdSize(AdSize.FULL_BANNER);
            adsLinear.addView(bannerAdmobAdView);
            adsLinear.setGravity(Gravity.CENTER_HORIZONTAL);
            AdRequest adRequest = new AdRequest.Builder().build();
            bannerAdmobAdView.loadAd(adRequest);
            bannerAdmobAdView.setAdListener(new AdListener(){
                @Override
                public void onAdLoaded() {
                    adsLinear.setVisibility(View.VISIBLE);
                }
            });
        } else if(bannerBottomTypeStr.equals("facebook")) {
            facebookBanner = getSharedPreferences("facebookBanner",MODE_PRIVATE);
            AudienceNetworkAds.initialize(this);
            AdSettings.addTestDevice("e5076253-9872-42cc-a0bd-fa714b019713");
            com.facebook.ads.AdView facebookAdView = new com.facebook.ads.AdView(this, facebookBanner.getString("facebookBanner", null), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.bottom_banner_main_activity);
            adContainer.addView(facebookAdView);
            facebookAdView.loadAd();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}