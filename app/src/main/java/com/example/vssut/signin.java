package com.example.vssut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.text.TextUtils.isEmpty;

public class signin extends AppCompatActivity {

        EditText number, code;
        Button sendcode;

        String Phonenumber;

        FirebaseAuth mauth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_signin);

            Intent signin2 = getIntent();
            mauth = FirebaseAuth.getInstance();

            number = findViewById(R.id.editTextnumber);


            sendcode = findViewById(R.id.sendcodebutton);

            sendcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (isEmpty(number.getText().toString()) || number.getText().toString().length() < 10) {

                        number.setError("fill it");
                    } else {

                        number.setError(null);

                        Phonenumber =  number.getText().toString();

                        Intent verify = new Intent(signin.this, verify.class);
                        verify.putExtra("phone",Phonenumber);
                        startActivity(verify);


                    }
                }
            });
        }
    }

