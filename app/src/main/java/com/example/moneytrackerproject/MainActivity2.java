package com.example.moneytrackerproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {
    TextView tvCar, tvHouse, tvFood, tvCommunication, tvSport, tvEntertainment, tvBills, tvHygiene,
    tvPets, tvPresents, tvRestaurants, tvClothes, tvHealth, tvTaxi, tvTransportation, tvOther;
    PieChart pieChart;
    private RecyclerView recyclerView;

    Button btnChangeAct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        createPieChart();


        /*
        tvCar = findViewById(R.id.tvCar);
        tvHouse = findViewById(R.id.tvHouse);
        tvFood = findViewById(R.id.tvFood);
        tvCommunication = findViewById(R.id.tvCommunication);
        tvSport = findViewById(R.id.tvSport);
        tvEntertainment = findViewById(R.id.tvEntertainment);
        tvBills = findViewById(R.id.tvBills);
        tvHygiene = findViewById(R.id.tvHygiene);
        tvPets = findViewById(R.id.tvPets);
        tvPresents = findViewById(R.id.tvPresents);
        tvRestaurants = findViewById(R.id.tvRestaurants);
        tvClothes = findViewById(R.id.tvClothes);
        tvHealth = findViewById(R.id.tvHealth);
        tvTaxi = findViewById(R.id.tvTaxi);
        tvTransportation = findViewById(R.id.tvTransportation);
        tvOther = findViewById(R.id.tvOther);

         */

        pieChart = findViewById(R.id.piechart);

        btnChangeAct = findViewById(R.id.btnChangeAct);

        btnChangeAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //recyclerView = findViewById(R.id.recyclerView);

        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        //recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(linearLayoutManager);

        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user");
        //Query query = reference.orderByChild("item");
    }

    public void createPieChart() {
        int totalSpent = 0;
        Map<String, Integer> res = new HashMap<>();
        for (Data d: MainActivity.dataList) {
            totalSpent += d.getQuantity();

            if (res.containsKey(d.getItem())) {
                res.put(d.getItem(), res.get(d.getItem())+d.getQuantity());
            } else {
                res.put(d.getItem(), d.getQuantity());
            }
        }
        int i = 226;
        //for (String e: res.keySet()) {
        //    pieChart.addPieSlice(
        //            new PieModel(
        //                    e,
        //                    res.get(e),
        //                    Color.parseColor("#FFA" + String.valueOf(i))));
        //    i+=40;

        //}
    }
}