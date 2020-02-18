package com.example.vssut;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.widgets.Snapshot;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListviewAdapter extends BaseAdapter {

    Activity context;
    String[] name;
    String[] location;
    String[] reuirement;
    List<DocumentSnapshot> list;

    ListviewAdapter(Activity context,List<DocumentSnapshot> list) {
        this.context=  context;
        this.list=list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        LayoutInflater li=context.getLayoutInflater();
        View row=li.inflate(R.layout.rowlayout,null,true);
        final TextView patientname=row.findViewById(R.id.patientname);
        final TextView location=row.findViewById(R.id.patientlocation);
        final TextView emergency=row.findViewById(R.id.patientemergency);
        emergency.setText(list.get(i).getString("emergencyType"));
        list.get(i).getDocumentReference("patientReference")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    MyApplication.patient=task.getResult().getString("fullName");
                    patientname.setText(task.getResult().getString("fullName"));
                    MyApplication.age=task.getResult().getString("age");
                    MyApplication.bloodgroup=task.getResult().getString("bloodGroup");
                    MyApplication.gender=task.getResult().getString("gender");
                }
            }
        });
        return null;
    }
}
