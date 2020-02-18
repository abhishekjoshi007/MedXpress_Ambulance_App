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

public class JoinNow extends AppCompatActivity {

    EditText fullname, phonenumber, email, password;
    Button join, signin;
    FirebaseAuth mauth;
    String uid,data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_now);

        join = findViewById(R.id.button3);
        signin = findViewById(R.id.button4);

        fullname = findViewById(R.id.editText);
        phonenumber=findViewById(R.id.editText3);
        email=findViewById(R.id.editText2);
        password=findViewById(R.id.editText4);

        mauth=FirebaseAuth.getInstance();


        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isEmpty(email.getText().toString())||isEmpty(fullname.getText().toString())||isEmpty(phonenumber.getText().toString())||!isValidPassword(password.getText().toString().trim())||password.getText().toString().length()<8){

                    fullname.setError("fill it");
                    phonenumber.setError("fill it");
                    email.setError("Fill your Email or Password");
                    password.setError("Password must have atleast 8 digits which must include a lowercase letter,an uppercase letter,a number and a special character");

                }
                else {
                    email.setError(null);
                    password.setError(null);

                    mauth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(JoinNow.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        System.out.println("create user");
                                        FirebaseUser user = mauth.getCurrentUser();
                                        uid = mauth.getUid();

                                        //send the data to database



                                        Intent sendcode = new Intent(JoinNow.this, signin.class);
                                        sendcode.putExtra("fullname",fullname.getText().toString());
                                        startActivity(sendcode);
                                    } else {
                                        Toast.makeText(JoinNow.this, "Authentication failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }    }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign3=new Intent(JoinNow.this,signin.class);
                startActivity(sign3);
            }
        });
    }


    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}
