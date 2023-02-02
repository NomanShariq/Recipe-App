package com.todocode.recipesdemo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.todocode.recipesdemo.R;

import java.util.Locale;
import java.util.Objects;

public class TermsOfUseActivity extends AppCompatActivity {
    private TextView privacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_use);
        Toolbar toolbar = findViewById(R.id.privacy_toolbar);
        toolbar.setTitle(getString(R.string.drawer_menu_terms));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        privacyPolicy = (TextView) findViewById(R.id.privacy_policy_text);
        privacyPolicy.setText(Html.fromHtml(getString(R.string.terms_of_use_text)));
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