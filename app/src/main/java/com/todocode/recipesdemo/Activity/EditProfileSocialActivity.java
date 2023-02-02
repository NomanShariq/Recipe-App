package com.todocode.recipesdemo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.textfield.TextInputLayout;
import com.todocode.recipesdemo.Manager.KeysManager;
import com.todocode.recipesdemo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class EditProfileSocialActivity extends AppCompatActivity {
    private String url;
    private SharedPreferences userEmailSharedPrefs, userFacebookSharedPrefs, userTwitterSharedPrefs, userInstagramSharedPrefs, userMemberSince;
    private TextInputLayout facebook, twitter, instagram;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_social);
        Toolbar toolbar = findViewById(R.id.edit_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.edit_social));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        url = getResources().getString(R.string.domain_name);
        userEmailSharedPrefs = getSharedPreferences("userEmailSharedPrefs",MODE_PRIVATE);
        userFacebookSharedPrefs = getSharedPreferences("userFacebookSharedPrefs", MODE_PRIVATE);
        userTwitterSharedPrefs = getSharedPreferences("userTwitterSharedPrefs", MODE_PRIVATE);
        userInstagramSharedPrefs = getSharedPreferences("userInstagramSharedPrefs", MODE_PRIVATE);
        facebook = (TextInputLayout) findViewById(R.id.facebook_url_edit_text);
        facebook.getEditText().setText(userFacebookSharedPrefs.getString("userFacebookSharedPrefs", ""));
        twitter = (TextInputLayout) findViewById(R.id.twitter_url_edit_text);
        twitter.getEditText().setText(userTwitterSharedPrefs.getString("userTwitterSharedPrefs", ""));
        instagram = (TextInputLayout) findViewById(R.id.instagram_url_edit_text);
        instagram.getEditText().setText(userInstagramSharedPrefs.getString("userInstagramSharedPrefs", ""));
        save = (Button) findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    changeInfos();
            }
        });
    }

    private void changeInfos() {
        final String facebookStr = Objects.requireNonNull(facebook.getEditText()).getText().toString();
        final String twitterStr = Objects.requireNonNull(twitter.getEditText()).getText().toString();
        final String instagramStr = Objects.requireNonNull(instagram.getEditText()).getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/chef/social/complete", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
                        Toast.makeText(EditProfileSocialActivity.this, getResources().getString(R.string.infos_saved), Toast.LENGTH_SHORT).show();
                        finish();
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", userEmailSharedPrefs.getString("userEmailSharedPrefs", ""));
                params.put("facebook", facebookStr);
                params.put("twitter", twitterStr);
                params.put("instagram", instagramStr);
                params.put("key", KeysManager.getMd5(getResources().getString(R.string.my_preference_permission_key)));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfileSocialActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}