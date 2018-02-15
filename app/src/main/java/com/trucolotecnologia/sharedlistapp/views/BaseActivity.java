package com.trucolotecnologia.sharedlistapp.views;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trucolotecnologia.sharedlistapp.models.SharedList;
import com.trucolotecnologia.sharedlistapp.models.User;
import com.trucolotecnologia.sharedlistapp.storage.PrefUtils;

/**
 * Created by eafdecision8 on 25/11/17.
 */

public class BaseActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private User currentUser;

    public void openActivity(Class<?> calledActivity) {
        Intent myIntent = new Intent(this, calledActivity);
        this.startActivity(myIntent);
    }

    public void openActivity(Class<?> calledActivity, String key, String value) {
        Intent myIntent = new Intent(this, calledActivity);
        myIntent.putExtra(key,value);
        this.startActivity(myIntent);
    }

    public User getCurrentUser()
    {
        if(currentUser == null)
            this.currentUser = new User(PrefUtils.getFromPrefs(this,PrefUtils.KEY_NOME,""),PrefUtils.getFromPrefs(this,PrefUtils.KEY_EMAIL,""));
        return currentUser;
    }

    public FirebaseAuth getFireBaseAuth()
    {
        if(mAuth == null)
            mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }
    public DatabaseReference getDataBase()
    {
        if(mDatabase == null)
            mDatabase = FirebaseDatabase.getInstance().getReference();
        return mDatabase;
    }


    public void showMessage(Context ctx, String title, DialogInterface.OnClickListener onclickpositive ){

        AlertDialog.Builder alert = new AlertDialog.Builder(ctx);

        alert.setTitle(title);
        alert.setPositiveButton("Sim", onclickpositive);
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });

        alert.show();
    }
}
