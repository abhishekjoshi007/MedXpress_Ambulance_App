package com.example.vssut.ui.notifications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.vssut.CurrentLocation;
import com.example.vssut.MyApplication;
import com.example.vssut.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import static com.firebase.ui.auth.AuthUI.TAG;

public class NotificationsFragment extends Fragment {
    FirebaseFirestore db;
    AlertDialog.Builder alertdialogbox;
    String documentid;
    ImageView map;
    Handler handler;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 5000);
            CurrentLocation.getCurrentLocation(getActivity()).addOnCompleteListener(new OnCompleteListener<Pair<Boolean, LatLng>>() {
                @Override
                public void onComplete(@NonNull Task<Pair<Boolean, LatLng>> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().first) {

                            FirebaseFirestore.getInstance().collection("ambulance")
                                    .document(MyApplication.ambulanceid).update("currentLatLng", new GeoPoint
                                    (task.getResult().second.latitude, task.getResult().second.longitude));
                        }
                    }
                }

            });

        }
    };

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        map = root.findViewById(R.id.imagemap);
        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        alertdialogbox = new AlertDialog.Builder(getContext());
        db = FirebaseFirestore.getInstance();
        final CollectionReference docRef = db.collection("requests");


        docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Error", "Listen failed.", e);
                    return;
                }

                if (snapshot != null) {

                    alertdialogbox.setMessage("Do you want to accept" + " the request?")
                            .setCancelable(false)
                            .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getContext(), "You rejected the request", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    map.setVisibility(View.VISIBLE);

                                    CurrentLocation.getCurrentLocation(getActivity()).addOnCompleteListener(new OnCompleteListener<Pair<Boolean, LatLng>>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Pair<Boolean, LatLng>> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().first) {

                                                    FirebaseFirestore.getInstance().collection("ambulance")
                                                            .document(MyApplication.ambulanceid).update("startLatLng", new GeoPoint
                                                            (task.getResult().second.latitude, task.getResult().second.longitude));


                                                }
                                            }
                                        }
                                    });
                                    map.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + MyApplication.lati + "," + MyApplication.longi);
                                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                            mapIntent.setPackage("com.google.android.apps.maps");
                                            startActivity(mapIntent);
                                        }
                                    });
                                    Toast.makeText(getContext(), "You accepted the request", Toast.LENGTH_SHORT).show();
                                    MyApplication.updateapplication();
                                    textView.setText("Patient Name - " + MyApplication.patient + "\nAge - " + MyApplication.age + "\nGender - " +
                                            MyApplication.gender + "\nBlood Group - " + MyApplication.bloodgroup);
                                }
                            });
                    AlertDialog alert = alertdialogbox.create();
                    alert.setTitle("Emergency");
                    alert.show();
                    Log.d("error", "Current data: ");
                } else {
                    Log.d("error", "Current data: null");
                }
            }
        });

        handler = new Handler();
        handler.postDelayed(runnable, 5000);

        return root;
    }

}