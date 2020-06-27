package com.workspace.adminpanels.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.workspace.adminpanels.R;

public class PesananFragment extends Fragment {

    private RecyclerView rvPesanan;
    private DatabaseReference dbPesanan;

    public PesananFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_pesanan, container, false);

        rvPesanan = v.findViewById(R.id.rvd_pesanan);

        return v;
    }
}