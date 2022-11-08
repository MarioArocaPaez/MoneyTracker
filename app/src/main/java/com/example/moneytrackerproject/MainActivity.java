package com.example.moneytrackerproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {

    // Declaration of variables used to create activity
    private Toolbar toolbar;
    private TextView quantityTextView;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private DatabaseReference ref;
    private String onlineUserId = "";

    private ItemsAdapter itemsAdapter;
    public static List<Data> dataList;

    Button btnChangeAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up the toolbar to have Balance written on it
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Money Tracker");

        //Set up quantity text, recycleView and float action button
        quantityTextView = findViewById(R.id.totalQuantitySpent);
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.floatAcBut);

        // Getting reference for database instance
        ref = FirebaseDatabase.getInstance().getReference().child("user").child(onlineUserId);


        // Define behaviour of float action button when clicked
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemSpentOn();
            }
        });

        // Setting linear layout manager for the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Creating an instance of ItemsAdapter object and setting it as the recyclerView adapter
        dataList = new ArrayList<>();
        itemsAdapter = new ItemsAdapter(MainActivity.this, dataList);
        recyclerView.setAdapter(itemsAdapter);

        btnChangeAct = findViewById(R.id.btnChangeAct);

        btnChangeAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        readItems();

    }

    // Method to retrieve the items from the Firebase database and display them on the retrieve
    // layout through the itemsAdapter, ordered by date.
    private void readItems(){

        // Retrieving the items from the database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user");
        Query query = reference.orderByChild("date");

        // Updating the dataList Array with the existing items
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    Data data = ds.getValue(Data.class);
                    dataList.add(data);
                }

                // Notify the adapter about the new items
                itemsAdapter.notifyDataSetChanged();

                // Update the balance
                int totalQuantity = 0;
                for(DataSnapshot ds: snapshot.getChildren()){
                    Map<String,Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("quantity");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalQuantity += pTotal;

                    quantityTextView.setText("Total spendings: " + totalQuantity + " €");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    // Method to display the input dialog for adding a new item and update the database
    private void addItemSpentOn() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.input_layout, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final Spinner itemsSpinner = myView.findViewById(R.id.spinner);
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.items));
        itemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemsSpinner.setAdapter(itemsAdapter);
        //TODO: Change amount to quantity
        final EditText amount = myView.findViewById(R.id.amount);
        final EditText notes = myView.findViewById(R.id.note);
        final Button saveBtn = myView.findViewById(R.id.save);
        final Button cancelBtn = myView.findViewById(R.id.cancel);

        // Define the save button behaviour
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mAmount = amount.getText().toString();
                String mNotes = notes.getText().toString();
                String item = itemsSpinner.getSelectedItem().toString();

                // Define behaviour in case any field is empty
                if (mAmount.isEmpty()){
                    amount.setError("Amount required!");
                    return;
                }

                if(notes.equals("")){
                    notes.setError("Notes required!");
                    return;
                }

                if (item.equalsIgnoreCase("Select item")) {
                    Toast.makeText(MainActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();
                }

                // Create Data object for new item and save it in the database
                else {

                    String id = ref.push().getKey();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar cal = Calendar.getInstance();
                    String date = dateFormat.format(cal.getTime());


                    Data data = new Data(item, date, id, mNotes, Integer.parseInt(mAmount));

                    ref.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MainActivity.this, "Failed to add note", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                dialog.dismiss();
            }
        });

        // Define the cancel button behaviour
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}