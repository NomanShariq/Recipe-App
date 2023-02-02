package com.todocode.recipesdemo.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.todocode.recipesdemo.Manager.KeysManager;
import com.todocode.recipesdemo.Manager.PermissionManager;
import com.todocode.recipesdemo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompleteProfileActivity extends AppCompatActivity {

    private TextInputLayout username, paypal;
    private Spinner gender, vegetarian;
    private Button save, editSocial;
    private CircleImageView profilePic;
    private ImageView changePRofileImage, btnAllowPermissions;
    private static final int PICK_IMAGE = 1;
    Uri imageurl;
    private Bitmap bitmap;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    private ProgressBar progressBarImage;
    private String url;
    private AwesomeValidation validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        btnAllowPermissions = (ImageView) findViewById(R.id.allow_permission);
        if (new PermissionManager(CompleteProfileActivity.this).checkPreference()) {
            btnAllowPermissions.setVisibility(View.GONE);
        }
        Toolbar toolbar = findViewById(R.id.complete_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.complete_profile));
        validator = new AwesomeValidation(ValidationStyle.BASIC);
        url = getResources().getString(R.string.domain_name);
        username = (TextInputLayout) findViewById(R.id.complete_profile_username);
        paypal = (TextInputLayout) findViewById(R.id.paypal_url_edit_text);
        profilePic = (CircleImageView) findViewById(R.id.profile_image);
        save = (Button) findViewById(R.id.save_button);
        gender = (Spinner) findViewById(R.id.spinner_gender);
        vegetarian = (Spinner) findViewById(R.id.spinner_vegetarian);
        username.getEditText().setText(getIntent().getStringExtra("username"));
        Picasso.get().load(getIntent().getStringExtra("image")).fit().centerInside().into(profilePic);
        progressBarImage = (ProgressBar) findViewById(R.id.image_progress_bar_edit);
        changePRofileImage = (ImageView) findViewById(R.id.change_image);
        btnAllowPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
        changePRofileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
            }
        });
        setupRules();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validator.validate()) {
                    if (Objects.requireNonNull(paypal.getEditText()).getText().toString().isEmpty()) {
                        changeInfos();
                        validator.clear();
                    } else {
                        if(paypal.getEditText().getText().toString().contains("paypal.me")) {
                            changeInfos();
                            validator.clear();
                        } else {
                            Toast.makeText(CompleteProfileActivity.this, getResources().getString(R.string.paypal_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    public void setupRules() {
        validator.addValidation(this, R.id.complete_profile_username, RegexTemplate.NOT_EMPTY, R.string.register_username_error);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION : {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED   ) {
                    new PermissionManager(CompleteProfileActivity.this).writePreference();
                    // Hide Image
                    btnAllowPermissions.setVisibility(View.GONE);
                }
                return;
            }
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(CompleteProfileActivity.this, new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_WRITE_PERMISSION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            assert data != null;
            imageurl = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageurl);
                bitmap = BitmapFactory.decodeStream(inputStream);
                profilePic.setImageBitmap(bitmap);
                uploadImage();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToStr(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        String encodedImg = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return encodedImg;
    }

    private void uploadImage() {
        progressBarImage.setVisibility(View.VISIBLE);
        final String imageData = imageToStr(bitmap);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/chefs/image/upload", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBarImage.setVisibility(View.GONE);
                Toast.makeText(CompleteProfileActivity.this, getResources().getString(R.string.image_changed), Toast.LENGTH_SHORT).show();
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
                // Check that image is added
                params.put("image", imageData);
                params.put("email", getIntent().getStringExtra("email"));
                params.put("key", KeysManager.getMd5(getResources().getString(R.string.my_preference_permission_key)));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(CompleteProfileActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void changeInfos() {
        final String usernameStr = Objects.requireNonNull(username.getEditText()).getText().toString();
        final String paypalStr = Objects.requireNonNull(paypal.getEditText()).getText().toString();
        final String genderStr = gender.getSelectedItem().toString();
        final String vegetarianStr = vegetarian.getSelectedItem().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/chef/complete", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
                        Toast.makeText(CompleteProfileActivity.this, getResources().getString(R.string.infos_saved), Toast.LENGTH_SHORT).show();
                        Intent social = new Intent(CompleteProfileActivity.this, CompleteSocialMediaActivity.class);
                        social.putExtra("email", getIntent().getStringExtra("email"));
                        social.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(social);
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
                params.put("email", getIntent().getStringExtra("email"));
                params.put("username", usernameStr);
                params.put("paypal", paypalStr);
                params.put("gender", genderStr);
                params.put("vegetarian", vegetarianStr);
                params.put("key", KeysManager.getMd5(getResources().getString(R.string.my_preference_permission_key)));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(CompleteProfileActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.complete_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.skip_now:
                Intent social = new Intent(CompleteProfileActivity.this, CompleteSocialMediaActivity.class);
                social.putExtra("email", getIntent().getStringExtra("email"));
                social.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(social);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}