package com.workspace.adminpanels.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.workspace.adminpanels.Adapter.pembayaranAdapter;
import com.workspace.adminpanels.Model.pembayaranModel;
import com.workspace.adminpanels.R;

import java.util.ArrayList;

public class PembayaranFragment extends Fragment {

    RecyclerView rvPembayaran;
    DatabaseReference dbPembayaran;
    ArrayList<pembayaranModel> pembayaranList;
    pembayaranAdapter adapter;
    String userId = "";

    public PembayaranFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pembayaran, container, false);

        rvPembayaran = v.findViewById(R.id.rvd_pembayaran);

        rvPembayaran.setHasFixedSize(true);
        rvPembayaran.setLayoutManager(new LinearLayoutManager(getContext()));
        pembayaranList = new ArrayList<>();


        dbPembayaran = FirebaseDatabase.getInstance().getReference("Data").child("Transaksi").child(userId).child("Pembayaran");
        dbPembayaran.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    pembayaranModel pembayaranMod = ds.getValue(pembayaranModel.class);
                    pembayaranList.add(pembayaranMod);
                }
                adapter = new pembayaranAdapter(pembayaranList, getContext());
                rvPembayaran.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }
}