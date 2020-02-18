package com.example.vssut;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;
import java.util.List;

public class LoginHelperActivity extends AppCompatActivity {

    private final int AUTH_REQUEST_CODE = 10;
    private final int REGISTRATION_REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.PhoneBuilder().setWhitelistedCountries(Collections.singletonList("IN")).build()
        );
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.mipmap.ic_launcher)
                        .build(),
                AUTH_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AUTH_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    IdpResponse response = IdpResponse.fromResultIntent(data);
                    if (response != null) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user == null) {
                            Toast.makeText(this, "Please try again.", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_CANCELED);
                            finish();
                        } else {
                            Toast.makeText(this, "Login successful.", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Login canceled.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    finish();
                } else if (data == null) {
                    Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    finish();
                }
                break;
            case REGISTRATION_REQUEST_CODE:
                setResult(resultCode);
                finish();
                break;
        }
    }
}
