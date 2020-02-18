package com.example.vssut;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mauth;
    Button emer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mauth = FirebaseAuth.getInstance();
        if (mauth.getCurrentUser() == null) {
            Intent intenttosignin = new Intent(MainActivity.this, com.example.vssut.LoginHelperActivity.class);
            startActivityForResult(intenttosignin, 10);
        } else {
            FirebaseFirestore.getInstance().collection("drivers")
                    .document(mauth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                                Intent intenttowork = new Intent(MainActivity.this,work.class);
                                startActivity(intenttowork);
                            } else {
                                Intent intenttoregis = new Intent(MainActivity.this,regis.class);
                                startActivity(intenttoregis);
                            }
                        }
                    });

        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(resultCode==RESULT_OK){
                Toast.makeText(this, "sign in successful", Toast.LENGTH_SHORT).show();
                Intent intenttoregis=new Intent(MainActivity.this,regis.class);
                startActivity(intenttoregis);
            }
            else{
                Toast.makeText(this, "sign in failed", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
