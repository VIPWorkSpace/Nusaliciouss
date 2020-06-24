package com.workspace.nusali.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.workspace.nusali.Activity.DeliveryLocationActivity;
import com.workspace.nusali.Activity.PaymentActivity;
import com.workspace.nusali.Adapter.ChartAdapter;
import com.workspace.nusali.Model.ChartModel;
import com.workspace.nusali.Model.OrderModel;

import com.workspace.nusali.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;


public class FragmentChart extends Fragment {
    private String userIdKey = "";
    private String userId = "";
    TextView namaPenerima, alamatPenerima, nomerPenerima, hint, ubahAlamat;
    DatabaseReference referenceChart, referenceDataDelivery;
    DatabaseReference referenceOrder;
    // DatabaseReference referencePay;

    Task<Void> referenceRemove;

    RecyclerView recyclerViewChart;
    ArrayList<ChartModel> chartList;
    ChartAdapter chartAdapter;

    int totalChart = 0;
    private Integer jumlahPesan = 0;
    Integer belanjaID = new Random().nextInt();
    String idTransaksi = belanjaID.toString();
    String toChart;

    public FragmentChart() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_chart, container, false);

        getUsernameLocal();

        //set button
        Button btnProses = v.findViewById(R.id.btn_proses_chart);
        namaPenerima = v.findViewById(R.id.nama_penerima);
        alamatPenerima = v.findViewById(R.id.alamat_penerima);
        nomerPenerima = v.findViewById(R.id.nomer_penerima);
        hint = v.findViewById(R.id.hint);
        ubahAlamat = v.findViewById(R.id.change_location);
        getDataDelivery();
        //ganti alamat
        ubahAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DeliveryLocationActivity.class);
                startActivity(intent);
            }
        });

        //set Recycler
        recyclerViewChart = v.findViewById(R.id.CheckoutRecycler);
        recyclerViewChart.setHasFixedSize(true);
        recyclerViewChart.setLayoutManager(new LinearLayoutManager(getContext()));
        chartList = new ArrayList<>();

        //LOAD RECYCLER KERANJANG
        referenceChart = FirebaseDatabase.getInstance().getReference().child("Keranjang").child(userId);
        referenceChart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ChartModel chartModel = dataSnapshot1.getValue(ChartModel.class);
                    chartList.add(chartModel);
                    chartAdapter = new ChartAdapter(getActivity(), chartList);
                    recyclerViewChart.setAdapter(chartAdapter);

                }

                for (int i = 0; i < chartList.size(); i++) {
//                    DataSnapshot snapshot = dataSnapshot.getChildren().iterator().next();
//                    ChartModel chartModel2 = snapshot.getValue(ChartModel.class);
                    totalChart += chartList.get(i).getTotal();
                    TextView tvTotal = v.findViewById(R.id.tv_total_chart);

                    toChart = Integer.toString(totalChart);
                    tvTotal.setText(toChart);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //PROSES PEMBAYARAN
        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToPesanan();


            }
        });
        return v;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

//    public void goToPayment() {
////        if (TextUtils.isEmpty(namaPenerima.getText().toString())) {
////            Toast.makeText(getActivity(), "Tanggal Kosong ! ", Toast.LENGTH_SHORT).show();
////        } else {
//
//        final String saveCurrentTime, saveCurrentDate;
//
//        Calendar calForDate = Calendar.getInstance();
//        final SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
//        saveCurrentDate = currentDate.format(calForDate.getTime());
//
//        final SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
//        saveCurrentTime = currentTime.format(calForDate.getTime());
//
//        referencePay = FirebaseDatabase.getInstance().getReference().child("Transaksi").child(userId).child("Pembayaran");
//        referencePay.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////
//
////                referencePay.getRef().child("idTransaksi").setValue(idTransaksi);
////                Integer totalBayar = Integer.parseInt(toChart);
////                referencePay.getRef().child("total").setValue(totalBayar);
////                referencePay.getRef().child("tanggal pesan").setValue(saveCurrentDate);
////                referencePay.getRef().child("waktu pesan").setValue(saveCurrentTime);
////                Intent intent = new Intent(getContext(), PaymentActivity.class);
////                startActivity(intent);
//                final String saveCurrentTime, saveCurrentDate;
//
//                Calendar calForDate = Calendar.getInstance();
//                final SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
//                saveCurrentDate = currentDate.format(calForDate.getTime());
//
//                final SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
//                saveCurrentTime = currentTime.format(calForDate.getTime());
//
//                Integer totalBayar = Integer.parseInt(toChart);
//                Integer idTrans = Integer.parseInt(idTransaksi);
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    ChartModel chartModel = dataSnapshot1.getValue(ChartModel.class);
//                    chartList.add(chartModel);
//                    for (int i = 0; i < chartList.size(); ) {
//                        Integer jumlah = chartList.get(i++).getJumlah();
//                        jumlahPesan = jumlahPesan + jumlah;
//                        String katering = chartList.get(i).getKatering();
//                        String tanggal = chartList.get(i).getTanggal();
//                        String waktu = chartList.get(i).getWaktu();
//
//                        PaymentModel payModel = new PaymentModel(idTrans, jumlahPesan, katering, tanggal, totalBayar, waktu, saveCurrentDate, saveCurrentTime);
//                       referencePay.getRef().child(idTransaksi).setValue(payModel).addOnCompleteListener(new OnCompleteListener<Void>() {
//                           @Override
//                           public void onComplete(@NonNull Task<Void> task) {
//
//                           }
//                       });
//
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    public void goToPesanan() {
        final String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        final SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        final SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());
        referenceOrder = FirebaseDatabase.getInstance().getReference("Transaksi").child(userId).child("Pesanan");
        referenceOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ChartModel chartModel = dataSnapshot1.getValue(ChartModel.class);
                    chartList.add(chartModel);
                    for (int i = 0; i < chartList.size(); i++) {
                        Integer idMenu = chartList.get(i).getId();
                        String menuId = String.valueOf(idMenu);
                        String judul = chartList.get(i).getJudul();
                        Integer jumlah = chartList.get(i).getJumlah();
                        String katering = chartList.get(i).getKatering();
                        String tanggal = chartList.get(i).getTanggal();
                        Integer total = chartList.get(i).getTotal();
                        String waktu = chartList.get(i).getWaktu();
//                                jumlahPesan += chartList.get(i).getTotal();
//                        dataSnapshot.getRef().child("idMenu").setValue(idMenu);
//                        dataSnapshot.getRef().child("judul").setValue(judul);
//                        dataSnapshot.getRef().child("jumlah").setValue(jumlah);
//                        dataSnapshot.getRef().child("katering").setValue(katering);
//                        dataSnapshot.getRef().child("tanggal").setValue(tanggal);
//                        dataSnapshot.getRef().child("total").setValue(total);
//                        dataSnapshot.getRef().child("waktu").setValue(waktu);
                       OrderModel orderModel = new OrderModel(idMenu, judul, jumlah, katering, tanggal, total, waktu);
                        referenceOrder.getRef().child(idTransaksi + " " + saveCurrentDate).child(menuId + " " + saveCurrentTime).setValue(orderModel);
                        referenceRemove = FirebaseDatabase.getInstance().getReference().child("Keranjang").child(userId).removeValue();
                        Intent intent = new Intent(getContext(), PaymentActivity.class);
                        startActivity(intent);
//                            intent.putExtra("idTrans", idTransaksi);
//                            intent.putExtra("pesanan", jumlahPesan);
//                            intent.putExtra("total", totalChart);
//                            intent.putExtra("Alamat", totalChart);
//                            intent.putExtra("Nama_Penerima", totalChart);


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getDataDelivery() {
        //load data yang ada
        referenceDataDelivery = FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("pengiriman");
        referenceDataDelivery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namaPenerima.setText(dataSnapshot.child("namaPenerima").getValue().toString());
                nomerPenerima.setText(dataSnapshot.child("nomerPenerima").getValue().toString());
                alamatPenerima.setText(dataSnapshot.child("alamatPenerima").getValue().toString());
                hint.setText(dataSnapshot.child("petunjuk").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(userIdKey, MODE_PRIVATE);
        userId = sharedPreferences.getString("firebaseKey", "");

    }
}