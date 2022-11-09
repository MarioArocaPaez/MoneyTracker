package com.example.moneytrackerproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.util.List;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {
    private Toolbar toolbar;
    PieChart pieChart;
    private RecyclerView recyclerView;
    private ItemsAdapter2 itemsAdapter2;
    List<Data> dataList = MainActivity.dataList;
    Map<String, Integer> res;

    Button btnChangeAct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Money Tracker");

        pieChart = findViewById(R.id.piechart);

        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        res = new HashMap<>();
        for (Data d: dataList) {

            if (res.containsKey(d.getItem())) {
                res.put(d.getItem(), (Integer) (res.get(d.getItem())+d.getQuantity())*100/MainActivity.totalQuantity);
            } else {
                res.put(d.getItem(), (Integer) (100*d.getQuantity()/MainActivity.totalQuantity));
            }
        }

        itemsAdapter2 = new ItemsAdapter2(MainActivity2.this, dataList, res);
        recyclerView.setAdapter(itemsAdapter2);

        btnChangeAct = findViewById(R.id.btnChangeAct);

        btnChangeAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });

        createPieChart();

    }

    public void createPieChart() {
        Map<String, String> colorsMap;
        colorsMap = new HashMap<>();
        colorsMap.put("Car", "#F44336");
        colorsMap.put("House", "#E91E63");
        colorsMap.put("Food", "#9C27B0");
        colorsMap.put("Communication", "#673AB7");
        colorsMap.put("Sport", "#3F51B5");
        colorsMap.put("Entertainment", "#2196F3");
        colorsMap.put("Bills", "#F5013D59");
        colorsMap.put("Hygiene", "#00BCD4");
        colorsMap.put("Pets", "#009688");
        colorsMap.put("Presents", "#4CAF50");
        colorsMap.put("Restaurants", "#8BC34A");
        colorsMap.put("Clothes", "#CDDC39");
        colorsMap.put("Health", "#4C460E");
        colorsMap.put("Taxi", "#FFC107");
        colorsMap.put("Transportation", "#FF9800");
        colorsMap.put("Other", "#FF5722");

        for (String e: res.keySet()) {
            pieChart.addPieSlice(
                    new PieModel(
                            e,
                            res.get(e),
                            Color.parseColor(colorsMap.get(e))));
        }

        pieChart.startAnimation();
    }
}