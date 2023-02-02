package com.todocode.recipesdemo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.todocode.recipesdemo.Manager.KeysManager;
import com.todocode.recipesdemo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.KeyManager;

public class WelcomeActivity extends AppCompatActivity {
    private LoginButton facebookRealRegisterButton;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;
    private ProgressBar registerProgressBar, loginProgressBar;
    private SharedPreferences userEmailSharedPrefs;
    private String url;
    private AwesomeValidation validator;
    private CheckBox agreeBox;
    Button welcomeLogin, welcomeRegister, loginButton, registerButton, registerGoogle, registerFacebook, loginGoogle, loginFacebook;
    LinearLayout forgetLinear, linearOr, welcomeLinearBtn, linearOrRegister, welcomeLinearBtnRegister;
    TextInputLayout loginEmailEditText, loginPasswordEditText, registerUsernameEditText, registerEmailEditText, registerPasswordEditText;
    TextView newMember, alreadyMember, welcomeTitle, welcomeSubtitle, loginTitle, registerTitle, termsOfUse, privacyPolicy, registerEmailError, registerUsernameError, registerDeviceError, loginError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        registerProgressBar = (ProgressBar) findViewById(R.id.register_progress_bar);
        loginProgressBar = (ProgressBar) findViewById(R.id.login_progress_bar);
        userEmailSharedPrefs = getSharedPreferences("userEmailSharedPrefs", MODE_PRIVATE);
        url = getResources().getString(R.string.domain_name);
        validator = new AwesomeValidation(ValidationStyle.BASIC);
        registerUsernameError = (TextView) findViewById(R.id.register_username_error);
        registerEmailError = (TextView) findViewById(R.id.register_email_error);
        registerDeviceError = (TextView) findViewById(R.id.register_device_error);
        loginError = (TextView) findViewById(R.id.login_error);
        welcomeLogin = (Button) findViewById(R.id.welcome_login);
        welcomeRegister = (Button) findViewById(R.id.welcome_register);
        loginButton = (Button) findViewById(R.id.login_button);
        registerButton = (Button) findViewById(R.id.register_button);
        forgetLinear = (LinearLayout) findViewById(R.id.forget_linear);
        linearOr = (LinearLayout) findViewById(R.id.linear_or);
        welcomeLinearBtn = (LinearLayout) findViewById(R.id.welcome_linear_btn);
        linearOrRegister = (LinearLayout) findViewById(R.id.linear_or_register);
        welcomeLinearBtnRegister = (LinearLayout) findViewById(R.id.welcome_linear_btn_register);
        loginEmailEditText = (TextInputLayout) findViewById(R.id.login_email_edit_text);
        loginPasswordEditText = (TextInputLayout) findViewById(R.id.login_password_edit_text);
        registerUsernameEditText = (TextInputLayout) findViewById(R.id.register_username_edit_text);
        registerEmailEditText = (TextInputLayout) findViewById(R.id.register_email_edit_text);
        registerPasswordEditText = (TextInputLayout) findViewById(R.id.register_password_edit_text);
        newMember = (TextView) findViewById(R.id.new_member);
        alreadyMember = (TextView) findViewById(R.id.already_member);
        welcomeTitle = (TextView) findViewById(R.id.welcome_title);
        welcomeSubtitle = (TextView) findViewById(R.id.welcome_subtitle);
        loginTitle = (TextView) findViewById(R.id.login_title);
        registerTitle = (TextView) findViewById(R.id.register_title);
        termsOfUse = (TextView) findViewById(R.id.terms_of_use);
        termsOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terms = new Intent(WelcomeActivity.this, TermsOfUseActivity.class);
                startActivity(terms);
            }
        });
        privacyPolicy = (TextView) findViewById(R.id.privacy_policy);
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent privacy = new Intent(WelcomeActivity.this, PrivacyPolicyActivity.class);
                startActivity(privacy);
            }
        });
        agreeBox = (CheckBox) findViewById(R.id.agree_with_terms);
        if (agreeBox.isChecked()) {
            initUI();
        }
        agreeBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (agreeBox.isChecked()) {
                    initUI();
                } else {
                    welcomeLogin.setBackgroundColor(getResources().getColor(R.color.gray));
                    welcomeRegister.setBackgroundColor(getResources().getColor(R.color.gray));
                    welcomeLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.please_agree_with_terms), Toast.LENGTH_SHORT).show();
                        }
                    });
                    welcomeRegister.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.please_agree_with_terms), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupRulesForRegister();
                if (validator.validate()) {
                    registerNewUserWithForm();
                    validator.clear();
                }
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupRulesForLogin();
                if (validator.validate()) {
                    loginWithForm();
                    validator.clear();
                }
            }
        });
        // Facebook & Google (Register + Login)
        registerGoogle = (Button) findViewById(R.id.register_google);
        registerFacebook = (Button) findViewById(R.id.register_facebook);
        loginGoogle = (Button) findViewById(R.id.login_google);
        loginFacebook = (Button) findViewById(R.id.login_facebook);
        facebookRealRegisterButton = (LoginButton) findViewById(R.id.facebook_register_btn);
        // Facebook
        registerFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookRealRegisterButton.performClick();
            }
        });
        loginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookRealRegisterButton.performClick();
            }
        });
        facebookRealRegisterButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();
        facebookRealRegisterButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                facebookLoginInformation(accessToken);
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
            }
        });
        // Google
        registerGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resulrCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resulrCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        super.onActivityResult(requestCode, resulrCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            googleSignIn();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void googleSignIn() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(WelcomeActivity.this);
        if (acct != null) {
            final String personName = acct.getDisplayName();
            final String personEmail = acct.getEmail();
            final Uri personPhoto = acct.getPhotoUrl();
            registerEmailError.setVisibility(View.GONE);
            registerUsernameError.setVisibility(View.GONE);
            registerDeviceError.setVisibility(View.GONE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/chefs/add", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        // No Errors
                        if (success.equals("1")){
                            userEmailSharedPrefs.edit().putString("userEmailSharedPrefs", personEmail).apply();
                            Toast.makeText(WelcomeActivity.this, getString(R.string.register_success), Toast.LENGTH_LONG).show();
                            Intent homePage = new Intent(WelcomeActivity.this, CompleteProfileActivity.class);
                            homePage.putExtra("username", personName);
                            homePage.putExtra("email", personEmail);
                            assert personPhoto != null;
                            homePage.putExtra("image", personPhoto.toString());
                            homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homePage);
                            finish();
                        }
                        // Device Exist
                        else if (success.equals("deviceError")){
                            registerDeviceError.setVisibility(View.VISIBLE);
                        }
                        else {
                            userEmailSharedPrefs.edit().putString("userEmailSharedPrefs", personEmail).apply();
                            Toast.makeText(WelcomeActivity.this, getString(R.string.login_success), Toast.LENGTH_LONG).show();
                            // Player already Exists , Go To Main Activity
                            Intent homePage = new Intent(WelcomeActivity.this, MainActivity.class);
                            homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homePage);
                            finish();
                        }
                    } catch(JSONException e){
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
                    params.put("username", personName);
                    params.put("email", personEmail);
                    params.put("login_method", "google");
                    params.put("key", KeysManager.getMd5(getResources().getString(R.string.my_preference_permission_key)));
                    if (personPhoto!=null) {
                        final String personImageUrl = personPhoto.toString();
                        params.put("image_url", personImageUrl);
                    }
                    @SuppressLint("HardwareIds") final String device = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    params.put("device_id", device);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(WelcomeActivity.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }
    }

    private void facebookLoginInformation(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    // GET USER DATA
                    final String name = object.getString("name");
                    final String email = object.getString("email");
                    final String image = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    // New Player , Register Request & Go To Main Activity & Save Shared Prefs
                    registerEmailError.setVisibility(View.GONE);
                    registerUsernameError.setVisibility(View.GONE);
                    registerDeviceError.setVisibility(View.GONE);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/chefs/add", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                // No Errors
                                if (success.equals("1")){
                                    userEmailSharedPrefs.edit().putString("userEmailSharedPrefs", email).apply();
                                    Toast.makeText(WelcomeActivity.this, getString(R.string.register_success), Toast.LENGTH_LONG).show();
                                    Intent homePage = new Intent(WelcomeActivity.this, CompleteProfileActivity.class);
                                    homePage.putExtra("username", name);
                                    homePage.putExtra("email", email);
                                    homePage.putExtra("image", image);
                                    homePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(homePage);
                                    finish();
                                } else if (success.equals("deviceError")){
                                    registerDeviceError.setVisibility(View.VISIBLE);
                                }
                                else {
                                    userEmailSharedPrefs.edit().putString("userEmailSharedPrefs", email).apply();
                                    Toast.makeText(WelcomeActivity.this, getString(R.string.login_success), Toast.LENGTH_LONG).show();
                                    // Player already Exists , Go To Main Activity
                                    Intent homePage = new Intent(WelcomeActivity.this, MainActivity.class);
                                    homePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(homePage);
                                    finish();
                                }
                            } catch(JSONException e){
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
                            params.put("username", name);
                            params.put("email", email);
                            params.put("login_method", "facebook");
                            params.put("image_url", image);
                            @SuppressLint("HardwareIds") final String device = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                                    Settings.Secure.ANDROID_ID);
                            params.put("device_id", device);
                            params.put("key", KeysManager.getMd5(getResources().getString(R.string.my_preference_permission_key)));
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(WelcomeActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }

    private void initUI() {
        // If Login Button Clicked
        welcomeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide Welcome Elements
                welcomeLogin.setVisibility(View.GONE);
                welcomeRegister.setVisibility(View.GONE);
                forgetLinear.setVisibility(View.GONE);
                // Show Login Elements
                loginTitle.setVisibility(View.VISIBLE);
                loginEmailEditText.setVisibility(View.VISIBLE);
                loginPasswordEditText.setVisibility(View.VISIBLE);
                newMember.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.VISIBLE);
                linearOr.setVisibility(View.VISIBLE);
                welcomeLinearBtn.setVisibility(View.VISIBLE);
                // If New Member Clicked
                newMember.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Hide Login Elements
                        loginTitle.setVisibility(View.GONE);
                        loginEmailEditText.setVisibility(View.GONE);
                        loginPasswordEditText.setVisibility(View.GONE);
                        newMember.setVisibility(View.GONE);
                        loginButton.setVisibility(View.GONE);
                        linearOr.setVisibility(View.GONE);
                        welcomeLinearBtn.setVisibility(View.GONE);
                        // Show Register Elements
                        registerTitle.setVisibility(View.VISIBLE);
                        registerUsernameEditText.setVisibility(View.VISIBLE);
                        registerEmailEditText.setVisibility(View.VISIBLE);
                        registerPasswordEditText.setVisibility(View.VISIBLE);
                        alreadyMember.setVisibility(View.VISIBLE);
                        registerButton.setVisibility(View.VISIBLE);
                        linearOrRegister.setVisibility(View.VISIBLE);
                        welcomeLinearBtnRegister.setVisibility(View.VISIBLE);
                        // If Already Member Clicked
                        alreadyMember.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Show Login Elements
                                loginTitle.setVisibility(View.VISIBLE);
                                loginEmailEditText.setVisibility(View.VISIBLE);
                                loginPasswordEditText.setVisibility(View.VISIBLE);
                                newMember.setVisibility(View.VISIBLE);
                                loginButton.setVisibility(View.VISIBLE);
                                linearOr.setVisibility(View.VISIBLE);
                                welcomeLinearBtn.setVisibility(View.VISIBLE);
                                // Hide Register Elements
                                registerUsernameEditText.setVisibility(View.GONE);
                                registerEmailEditText.setVisibility(View.GONE);
                                registerPasswordEditText.setVisibility(View.GONE);
                                alreadyMember.setVisibility(View.GONE);
                                registerButton.setVisibility(View.GONE);
                                linearOrRegister.setVisibility(View.GONE);
                                welcomeLinearBtnRegister.setVisibility(View.GONE);
                                registerTitle.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        });
        // If Register Button clicked
        welcomeRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide Welcome Elements
                welcomeLogin.setVisibility(View.GONE);
                welcomeRegister.setVisibility(View.GONE);
                forgetLinear.setVisibility(View.GONE);
                // Show Register Elements
                registerTitle.setVisibility(View.VISIBLE);
                registerUsernameEditText.setVisibility(View.VISIBLE);
                registerEmailEditText.setVisibility(View.VISIBLE);
                registerPasswordEditText.setVisibility(View.VISIBLE);
                alreadyMember.setVisibility(View.VISIBLE);
                registerButton.setVisibility(View.VISIBLE);
                linearOrRegister.setVisibility(View.VISIBLE);
                welcomeLinearBtnRegister.setVisibility(View.VISIBLE);
                // If Already Member Clicked
                alreadyMember.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Show Login Elements
                        loginEmailEditText.setVisibility(View.VISIBLE);
                        loginPasswordEditText.setVisibility(View.VISIBLE);
                        newMember.setVisibility(View.VISIBLE);
                        loginButton.setVisibility(View.VISIBLE);
                        linearOr.setVisibility(View.VISIBLE);
                        welcomeLinearBtn.setVisibility(View.VISIBLE);
                        // Hide Register Elements
                        registerTitle.setVisibility(View.GONE);
                        registerUsernameEditText.setVisibility(View.GONE);
                        registerEmailEditText.setVisibility(View.GONE);
                        registerPasswordEditText.setVisibility(View.GONE);
                        alreadyMember.setVisibility(View.GONE);
                        registerButton.setVisibility(View.GONE);
                        linearOrRegister.setVisibility(View.GONE);
                        welcomeLinearBtnRegister.setVisibility(View.GONE);
                        // If New Member Clicked
                        newMember.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Hide Login Elements
                                loginEmailEditText.setVisibility(View.GONE);
                                loginPasswordEditText.setVisibility(View.GONE);
                                newMember.setVisibility(View.GONE);
                                loginButton.setVisibility(View.GONE);
                                linearOr.setVisibility(View.GONE);
                                welcomeLinearBtn.setVisibility(View.GONE);
                                // Show Register Elements
                                registerTitle.setVisibility(View.VISIBLE);
                                registerUsernameEditText.setVisibility(View.VISIBLE);
                                registerEmailEditText.setVisibility(View.VISIBLE);
                                registerPasswordEditText.setVisibility(View.VISIBLE);
                                alreadyMember.setVisibility(View.VISIBLE);
                                registerButton.setVisibility(View.VISIBLE);
                                linearOrRegister.setVisibility(View.VISIBLE);
                                welcomeLinearBtnRegister.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });
            }
        });
    }

    public void setupRulesForRegister() {
        validator.addValidation(this, R.id.register_username_edit_text, RegexTemplate.NOT_EMPTY, R.string.register_username_error);
        validator.addValidation(this, R.id.register_email_edit_text, Patterns.EMAIL_ADDRESS, R.string.register_email_error);
    }

    public void setupRulesForLogin() {
        validator.addValidation(this, R.id.login_password_edit_text, RegexTemplate.NOT_EMPTY, R.string.login_password_empty);
        validator.addValidation(this, R.id.login_email_edit_text, Patterns.EMAIL_ADDRESS, R.string.login_email_empty);
    }

    private void registerNewUserWithForm() {
        final String enteredUserName = Objects.requireNonNull(this.registerUsernameEditText.getEditText()).getText().toString().trim();
        final String enteredEmail = Objects.requireNonNull(this.registerEmailEditText.getEditText()).getText().toString().trim();
        final String enteredPassword = Objects.requireNonNull(this.registerPasswordEditText.getEditText()).getText().toString().trim();
        registerEmailError.setVisibility(View.GONE);
        registerUsernameError.setVisibility(View.GONE);
        registerDeviceError.setVisibility(View.GONE);
        registerButton.setVisibility(View.GONE);
        registerProgressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/chefs/add", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    registerProgressBar.setVisibility(View.GONE);
                    registerButton.setVisibility(View.VISIBLE);
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    // No Errors
                    if (success.equals("1")){
                        // Change Shared Preferences
                        userEmailSharedPrefs.edit().putString("userEmailSharedPrefs", enteredEmail).apply();
                        // Message Success
                        Toast.makeText(WelcomeActivity.this, getString(R.string.register_success), Toast.LENGTH_LONG).show();
                        // Go To Home Page
                        Intent homePage = new Intent(WelcomeActivity.this, CompleteProfileActivity.class);
                        homePage.putExtra("username", enteredUserName);
                        homePage.putExtra("email", enteredEmail);
                        homePage.putExtra("image", getResources().getString(R.string.domain_name)+"/img/chef.png");
                        homePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homePage);
                        finish();
                    } else {
                        if(success.equals("emailError")) {
                            registerEmailError.setVisibility(View.VISIBLE);
                        }
                        if(success.equals("usernameError")) {
                            registerUsernameError.setVisibility(View.VISIBLE);
                        }
                        if(success.equals("deviceError")) {
                            registerDeviceError.setVisibility(View.VISIBLE);
                        }
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                    registerProgressBar.setVisibility(View.GONE);
                    registerButton.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                registerProgressBar.setVisibility(View.GONE);
                registerButton.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", enteredUserName);
                params.put("email", enteredEmail);
                params.put("password", enteredPassword);
                @SuppressLint("HardwareIds") final String device = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                params.put("device_id", device);
                params.put("login_method", "email");
                params.put("key", KeysManager.getMd5(getResources().getString(R.string.my_preference_permission_key)));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(WelcomeActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void loginWithForm() {
        final String enteredEmail = Objects.requireNonNull(this.loginEmailEditText.getEditText()).getText().toString().trim();
        final String enteredPassword = Objects.requireNonNull(this.loginPasswordEditText.getEditText()).getText().toString().trim();
        loginError.setVisibility(View.GONE);
        loginButton.setVisibility(View.GONE);
        loginProgressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/chefs/login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loginProgressBar.setVisibility(View.GONE);
                    loginButton.setVisibility(View.VISIBLE);
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    // No Errors
                    if (success.equals("1")){
                        // Change Shared Preferences
                        userEmailSharedPrefs.edit().putString("userEmailSharedPrefs", enteredEmail).apply();
                        // Message Success
                        Toast.makeText(WelcomeActivity.this, getString(R.string.login_success), Toast.LENGTH_LONG).show();
                        // Go To Home Page
                        Intent homePage = new Intent(WelcomeActivity.this, MainActivity.class);
                        homePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homePage);
                        finish();
                    } else {
                        if(success.equals("emailError")) {
                            loginError.setVisibility(View.VISIBLE);
                        }
                        if(success.equals("passwordError")) {
                            loginError.setVisibility(View.VISIBLE);
                        }
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                    registerProgressBar.setVisibility(View.GONE);
                    registerButton.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                registerProgressBar.setVisibility(View.GONE);
                registerButton.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", enteredEmail);
                params.put("password", enteredPassword);
                params.put("key", KeysManager.getMd5(getResources().getString(R.string.my_preference_permission_key)));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(WelcomeActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    }