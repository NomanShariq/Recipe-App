package com.todocode.recipesdemo.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.todocode.recipesdemo.Manager.KeysManager;
import com.todocode.recipesdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private SharedPreferences userEmailSharedPrefs;
    private String url;
    private RequestQueue queue;
    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences admobBanner, admobNative, admobVideo, admobInterstitial, facebookBanner, facebookNative, facebookInterstitial, facebookVideo, bannerType, interstitialType, videoType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mProgressBar = (ProgressBar) findViewById(R.id.splash_progressbar);
        userEmailSharedPrefs = getSharedPreferences("userEmailSharedPrefs", MODE_PRIVATE);
        url = getResources().getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        admobBanner = getSharedPreferences("admobBanner", MODE_PRIVATE);
        admobNative = getSharedPreferences("admobNative", MODE_PRIVATE);
        admobVideo = getSharedPreferences("admobVideo", MODE_PRIVATE);
        admobInterstitial = getSharedPreferences("admobInterstitial", MODE_PRIVATE);
        facebookBanner = getSharedPreferences("facebookBanner", MODE_PRIVATE);
        facebookNative = getSharedPreferences("facebookNative", MODE_PRIVATE);
        facebookInterstitial = getSharedPreferences("facebookInterstitial", MODE_PRIVATE);
        facebookVideo = getSharedPreferences("facebookVideo", MODE_PRIVATE);
        bannerType = getSharedPreferences("bannerType", MODE_PRIVATE);
        interstitialType = getSharedPreferences("interstitialType", MODE_PRIVATE);
        videoType = getSharedPreferences("videoType", MODE_PRIVATE);
        Sprite threeBounce = new ThreeBounce();
        threeBounce.setColor(getResources().getColor(R.color.colorAccent));
        mProgressBar.setIndeterminateDrawable(threeBounce);
        int SPLASH_DISPLAY_LENGTH = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isNetworkConnected()) {
                    getAds();
                    getUserSituation();

                } else {
                    showAlertDialogConnectionProblem();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
}

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void showAlertDialogConnectionProblem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.splash_connection_problem_title));
        builder.setIcon(R.drawable.ic_wifi);
        builder.setMessage(getString(R.string.splash_connection_problem_message));
        builder.setPositiveButton(getString(R.string.splash_connection_problem_retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SplashScreenActivity.this.recreate();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getUserSituation() {
        String userEmailStr = userEmailSharedPrefs.getString("userEmailSharedPrefs","");
        if (!userEmailStr.equals("")) {
            
            final String userToCheck = userEmailStr;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/chefs/situation", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        // No Errors
                        if (success.equals("loggedSuccess")){
                            Intent homePage = new Intent(SplashScreenActivity.this, MainActivity.class);
                            homePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homePage);
                            finish();
                        } if(success.equals("loggedError")) {
                            userEmailSharedPrefs.edit().putString("userEmailSharedPrefs", "").apply();
                            if (LoginManager.getInstance() != null) {
                                LoginManager.getInstance().logOut();
                            }
                            // Logout Google
                            if (mGoogleSignInClient != null) {
                                mGoogleSignInClient.signOut();
                            }
                            Intent registerPage = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                            registerPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(registerPage);
                            finish();
                        }
                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", userToCheck);
                    params.put("key", KeysManager.getMd5(getResources().getString(R.string.my_preference_permission_key)));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            Intent loginPage = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
            loginPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginPage);
            finish();
        }
    }

    private void getAds() {
        String adsUrl = url+"/api/ads/all/get";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, adsUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject ads = jsonArray.getJSONObject(i);
                                final String NativeAdId = ads.getString("admob_native");
                                final String AdmobVideoAdId = ads.getString("admob_video");
                                final String BannerAdId = ads.getString("admob_banner");
                                final String InterstitialAdId = ads.getString("admob_interstitial");
                                final String facebookNativeAdId = ads.getString("fb_native");
                                final String facebookBannerAdId = ads.getString("fb_banner");
                                final String facebookVideoAdId = ads.getString("fb_video");
                                final String facebookInterstitialAdId = ads.getString("fb_interstitial");
                                final String BottomBannerType = ads.getString("banner_type");
                                final String videoTypeType = ads.getString("video_type");
                                final String interstitialTypeType = ads.getString("interstitial_type");
                                admobBanner.edit().putString("admobBanner", BannerAdId).apply();
                                bannerType.edit().putString("bannerType", BottomBannerType).apply();
                                admobNative.edit().putString("admobNative", NativeAdId).apply();
                                admobVideo.edit().putString("admobVideo", AdmobVideoAdId).apply();
                                admobInterstitial.edit().putString("admobInterstitial", InterstitialAdId).apply();
                                facebookBanner.edit().putString("facebookBanner", facebookBannerAdId).apply();
                                facebookNative.edit().putString("facebookNative", facebookNativeAdId).apply();
                                facebookVideo.edit().putString("facebookVideo", facebookVideoAdId).apply();
                                videoType.edit().putString("videoType", videoTypeType).apply();
                                interstitialType.edit().putString("interstitialType", interstitialTypeType).apply();
                                facebookInterstitial.edit().putString("facebookInterstitial", facebookInterstitialAdId).apply();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }
}