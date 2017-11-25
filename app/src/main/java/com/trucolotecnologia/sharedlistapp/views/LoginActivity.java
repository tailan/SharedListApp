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
import com.trucolotecnologia.sharedlistapp.R;
import com.trucolotecnologia.sharedlistapp.models.SharedList;
import com.trucolotecnologia.sharedlistapp.storage.PrefUtils;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    public TextView tvPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        boolean isAuthenticated = !PrefUtils.getFromPrefs(this,PrefUtils.KEY_PHONE_NUMBER,"").isEmpty();

        if(isAuthenticated)
            openActivity(SharedListsActivity.class);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onResume() {
        super.onResume();
        tvPhoneNumber = findViewById(R.id.etPhoneNumber);
        tvPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        tvPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus)
                    ((EditText)view).setText("+55");

            }
        });
    }


    public void autenticar(View v){


        PrefUtils.saveToPrefs(this,PrefUtils.KEY_PHONE_NUMBER, tvPhoneNumber.getText().toString());

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            openActivity(SharedListsActivity.class);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }
}
