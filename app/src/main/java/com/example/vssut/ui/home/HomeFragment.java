package com.example.vssut.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.vssut.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ArrayList<String> name;
    ArrayList<String> location;
    ArrayList<String> emergency;
    FirebaseFirestore db;
    ListView records;
    List<DocumentSnapshot> listviewlist;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        records=root.findViewById(R.id.list);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {


            }
        });
db=FirebaseFirestore.getInstance();
 db.collection("requests").whereEqualTo("assignedAmbulance",FirebaseFirestore.getInstance().collection("ambulance").document(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                .whereEqualTo("status",4)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                      listviewlist=queryDocumentSnapshots.getDocuments();
                      if(listviewlist==null){
                          Toast.makeText(getContext(), "No request are there", Toast.LENGTH_SHORT).show();
                      }
                    }
                });


        if(listviewlist!=null){
            ArrayAdapter listview=new ArrayAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item,listviewlist);

            records.setAdapter(listview);
        }
        return root;
    }
}