package com.trucolotecnologia.sharedlistapp.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by eafdecision8 on 25/11/17.
 */

public class BaseActivity extends AppCompatActivity {

    public void openActivity(Class<?> calledActivity) {
        Intent myIntent = new Intent(this, calledActivity);
        this.startActivity(myIntent);
    }
}
