package com.todocode.recipesdemo.Manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.todocode.recipesdemo.R;

public class PermissionManager {
    private Context context;
    private SharedPreferences sharedPreferences;

    private void getSharedPreference() {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.my_permissions), Context.MODE_PRIVATE);
    }
    public PermissionManager(Context context) {
        this.context = context;
        getSharedPreference();
    }

    public void writePreference() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.my_permissions_key), "INIT_OK");
        editor.commit();
    }

    public boolean checkPreference() {
        boolean status = false;
        if (sharedPreferences.getString(context.getString(R.string.my_permissions_key), "null").equals("null")){
            status = false;
        } else {
            status = true;
        }
        return status;
    }

    public void clearPreference() {
        sharedPreferences.edit().clear().commit();
    }
}



