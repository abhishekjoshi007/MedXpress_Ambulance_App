package com.example.vssut;

import android.app.Application;
import android.database.Cursor;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class MyApplication extends Application {
    public static String ambulanceid;
    public static String documentid;
    public static Double lati, longi;
    public static String patient,age,gender,bloodgroup;
    static Cursor cursor;
    public static DocumentReference patientreference;

    public static void updateapplication() {

        FirebaseFirestore.getInstance().collection("requests")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots.getDocuments().get(0).getId() == null) {
                            System.out.println("NO RECORDS");
                        } else {
                             documentid = queryDocumentSnapshots.getDocuments().get(0).getId();
                            GeoPoint location;
                            location = (GeoPoint) queryDocumentSnapshots.getDocuments().get(0).getData().get("latLng");
                            lati = location.getLatitude();
                            patientreference= (DocumentReference) queryDocumentSnapshots.getDocuments().get(0).getData().get("latLng");
                            longi = location.getLongitude();
                            FirebaseFirestore.getInstance().collection("requests").document(documentid)
                                    .update("assignedAmbulance", ambulanceid);
                            patientreference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    patient=task.getResult().getString("fullName");

                                    age=task.getResult().getString("age");
                                    bloodgroup=task.getResult().getString("bloodGroup");
                                    gender=task.getResult().getString("gender");
                                }
                            });



                        }

                    }
                });


    }
}
