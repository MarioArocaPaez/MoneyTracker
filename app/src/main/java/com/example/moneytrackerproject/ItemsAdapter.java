package com.example.moneytrackerproject;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

// The objective of the ItemsAdapter class is to display the information of the items given in
// dataList into the retrieve_layout.
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    private Context context;
    private List<Data> dataList;
    private String postId;
    private String note;
    private int amount;
    private String item;

    public ItemsAdapter(Context context, List<Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.retrieve_layout, parent, false);
        return new ItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Data data = dataList.get(position);
        holder.item.setText("Item: " + data.getItem());
        holder.quantity.setText("Spent: " + data.getQuantity());
        holder.date.setText("Date: " + data.getDate());
        holder.notes.setText("Notes: " + data.getNotes());

        switch (data.getItem()){
            case "Car":
                holder.imageView.setImageResource(R.drawable.car);
                break;
            case "House":
                holder.imageView.setImageResource(R.drawable.home);
                break;
            case "Food":
                holder.imageView.setImageResource(R.drawable.food);
                break;
            case "Communication":
                holder.imageView.setImageResource(R.drawable.communication);
                break;
            case "Sport":
                holder.imageView.setImageResource(R.drawable.sport);
                break;
            case "Entertainment":
                holder.imageView.setImageResource(R.drawable.entertainment);
                break;
            case "Bills":
                holder.imageView.setImageResource(R.drawable.bills);
                break;
            case "Hygiene":
                holder.imageView.setImageResource(R.drawable.hygiene);
                break;
            case "Pets":
                holder.imageView.setImageResource(R.drawable.pets);
                break;
            case "Presents":
                holder.imageView.setImageResource(R.drawable.presents);
                break;
            case "Restaurants":
                holder.imageView.setImageResource(R.drawable.restaurants);
                break;
            case "Clothes":
                holder.imageView.setImageResource(R.drawable.clothes);
                break;
            case "Health":
                holder.imageView.setImageResource(R.drawable.health);
                break;
            case "Taxi":
                holder.imageView.setImageResource(R.drawable.taxi);
                break;
            case "Transportation":
                holder.imageView.setImageResource(R.drawable.transportation);
                break;
            case "Other":
                holder.imageView.setImageResource(R.drawable.others);
                break;
        }

        // Setting the behaviour of each item to display an update layout when clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postId = data.getId();
                note = data.getNotes();
                amount = data.getQuantity();
                item = data.getItem();

                updateData();
            }
        });
    }

    // Method to allow changes in the data of a certain item
    private void updateData() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View myView = inflater.inflate(R.layout.update_layout, null);

        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();

        final TextView mItems = myView.findViewById(R.id.item);
        final EditText mAmount = myView.findViewById(R.id.amount);
        final EditText mNote = myView.findViewById(R.id.note);

        mItems.setText(item);

        mAmount.setText(String.valueOf(amount));
        mAmount.setSelection(String.valueOf(amount).length());

        mNote.setText(note);
        mNote.setSelection(note.length());

        Button updateBtn = myView.findViewById(R.id.update);
        Button deleteBtn = myView.findViewById(R.id.delete);

        // Update a certain expense's information
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = Integer.parseInt(mAmount.getText().toString());
                note = mNote.getText().toString();

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());

                Data data = new Data(item, date, postId, note, amount);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user");
                reference.child(postId).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Updated successfully!",Toast.LENGTH_SHORT).show();
                        } else {
                          Toast.makeText(context, "Failed " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });

        // Delete an expense
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user");
                reference.child(postId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Deleted successfully!",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to delete " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView item;
        public TextView quantity;
        public TextView date;
        public TextView notes;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            item = itemView.findViewById(R.id.item);
            quantity = itemView.findViewById(R.id.quantity);
            date = itemView.findViewById(R.id.date);
            notes = itemView.findViewById(R.id.note);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
