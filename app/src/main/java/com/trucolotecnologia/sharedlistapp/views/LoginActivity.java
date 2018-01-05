package com.trucolotecnologia.sharedlistapp.views;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.trucolotecnologia.sharedlistapp.R;
import com.trucolotecnologia.sharedlistapp.models.User;
import com.trucolotecnologia.sharedlistapp.storage.PrefUtils;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    public EditText etEmail;
    public EditText etNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etNome = findViewById(R.id.etNome);

        boolean isAuthenticated = !PrefUtils.getFromPrefs(this, PrefUtils.KEY_EMAIL, "").isEmpty();


         if(isAuthenticated) {
             //loadUserFromSharedPreferences();
             openActivity(SharedListsActivity.class);
         }

    }
//
//    private void loadUserFromSharedPreferences() {
//        this.currentUser = new User(PrefUtils.getFromPrefs(this,PrefUtils.KEY_NOME,""),PrefUtils.getFromPrefs(this,PrefUtils.KEY_EMAIL,""));
//    }

    public void autenticar(View v) {

        final String nome = etNome.getText().toString();
        final String mail = etEmail.getText().toString();
        final String uID = mail.split("@")[0];

        PrefUtils.saveToPrefs(this, PrefUtils.KEY_EMAIL, mail);
        PrefUtils.saveToPrefs(this, PrefUtils.KEY_NOME, nome);

        if (getFireBaseAuth().getCurrentUser() == null)
            loginUserFirebase(mail,uID,nome);



    }

    private void loginUserFirebase(final String mail,final String uID,final String nome){
        getFireBaseAuth().signInWithEmailAndPassword(mail, uID)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            writeNewUser(getCurrentUser(), uID);
                            openActivity(SharedListsActivity.class);

                        } else {
                            createUserFirebase(mail,uID,nome);
                        }

                    }
                });
    }
    private void createUserFirebase(final String mail,final String uID,final String nome){
        getFireBaseAuth().createUserWithEmailAndPassword(mail, uID)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            writeNewUser(getCurrentUser(), uID);
                            openActivity(SharedListsActivity.class);

                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    private void writeNewUser(User user, String uid) {

        getDataBase().child("users").child(uid).setValue(user);
    }
}
