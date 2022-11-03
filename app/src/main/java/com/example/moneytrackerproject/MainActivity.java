package com.example.moneytrackerproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
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

    private Toolbar toolbar;
    private TextView quantityTextView;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private FirebaseAuth mAuth;
    private DatabaseReference ref;
    private String onlineUserId = "";
    //private ProgressBar loader;

    public ItemsAdapter itemsAdapter;
    public List<Data> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Balance");

        quantityTextView = findViewById(R.id.totalQuantitySpent);
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.floatAcBut);

        mAuth = FirebaseAuth.getInstance();
        //onlineUserId = mAuth.getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("expenses").child(onlineUserId);
        //loader = new ProgressBar(this); //TODO: implement progress bar (https://stackoverflow.com/questions/45373007/progressdialog-is-deprecated-what-is-the-alternate-one-to-use)

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemSpentOn();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        dataList = new ArrayList<>();
        itemsAdapter = new ItemsAdapter(MainActivity.this, dataList);
        recyclerView.setAdapter(itemsAdapter);

        readItems();

    }

    private void readItems(){

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("expenses").child(onlineUserId);
        Query query = reference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Data data = snapshot.getValue(Data.class);
                    dataList.add(data);
                }

                itemsAdapter.notifyDataSetChanged();

                int totalQuantity = 0;
                for(DataSnapshot ds: snapshot.getChildren()){
                    Map<String,Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
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


    // Method to display the input dialog for a new item
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

        final EditText amount = myView.findViewById(R.id.amount);
        final EditText notes = myView.findViewById(R.id.note);
        final Button saveBtn = myView.findViewById(R.id.save);
        final Button cancelBtn = myView.findViewById(R.id.cancel);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mAmount = amount.getText().toString();
                String mNotes = notes.getText().toString();
                String item = itemsSpinner.getSelectedItem().toString();

                if (mAmount.isEmpty()){
                    amount.setError("Amount required!");
                    return;
                }

                if (item.equals("Select item")) {
                    Toast.makeText(MainActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();
                }

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

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}