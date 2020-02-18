package com.example.vssut;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class startscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startscreen);

        final Intent startscreen = getIntent();
        Button startbutton1 = findViewById(R.id.startbutton);
        Button startbutton2 = findViewById(R.id.startbutton2);

        startbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intenttosignin = new Intent(startscreen.this, com.example.vssut.LoginHelperActivity.class);
                startActivityForResult(intenttosignin, 10);
                ;
            }
        });

        startbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(startscreen.this, "This Facility is in progress until then use Ambulance facility", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(resultCode==RESULT_OK){
                Toast.makeText(this, "sign in successful", Toast.LENGTH_SHORT).show();
                Intent intenttoregis=new Intent(startscreen.this,regis.class);
                startActivity(intenttoregis);
            }
            else{
                Toast.makeText(this, "sign in failed", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
