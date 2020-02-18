package com.example.vssut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class regis extends AppCompatActivity {
    ArrayList<String> typeofambulance = new ArrayList<String>();
    Spinner ambulancetype;

    FirebaseFirestore db;
    TextInputEditText name;
    TextInputEditText age;
    TextInputEditText city;
    TextInputEditText state;
    TextInputEditText vehiclenumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);
        typeofambulance.add("Free");
        typeofambulance.add("Fixed Hospital");
        typeofambulance.add("Government Hospitals");


        Intent regis = getIntent();
        name = findViewById(R.id.nametext);
        age = findViewById(R.id.dobtext);
        Button submit=findViewById(R.id.submitbutton);
        city = findViewById(R.id.citytext);
        state = findViewById(R.id.statetext);
        ambulancetype=findViewById(R.id.toa);
        vehiclenumber = findViewById(R.id.vehicletext);

        ArrayAdapter spinner = new ArrayAdapter(regis.this, R.layout.support_simple_spinner_dropdown_item,typeofambulance);
        ambulancetype.setAdapter(spinner);

submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("name", name.getText().toString());
        data.put("DOB", age.getText().toString());
        data.put("city", city.getText().toString());
        data.put("state", state.getText().toString());

        db.collection("drivers").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(regis.this, "success", Toast.LENGTH_SHORT).show();
                            CollectionReference ambulanceRef = db.collection("ambulance");
                            Query query = ambulanceRef.whereEqualTo("number_plate", vehiclenumber.getText().toString());
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                                        MyApplication.ambulanceid = task.getResult().getDocuments().get(0).getReference().getId();
                                        FirebaseFirestore.getInstance()
                                                .collection("drivers")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .update("ambulance_id",MyApplication.ambulanceid);
                                    } else {
                                        FirebaseFirestore.getInstance()
                                                .collection("ambulance")
                                                .add(vehiclenumber.getText().toString()).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(regis.this, "vehicle number registered", Toast.LENGTH_SHORT).show();
                                                    Intent intenttowork = new Intent(regis.this, work.class);

                                                    startActivity(intenttowork);
                                                } else {
                                                    Toast.makeText(regis.this, "Vehicle not registered", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });


                        }
                    }
                });

    }
});

    }
}
