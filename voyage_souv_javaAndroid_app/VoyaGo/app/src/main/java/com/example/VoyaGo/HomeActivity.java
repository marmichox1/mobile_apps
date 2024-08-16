package com.example.VoyaGo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.VoyaGo.R;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    EditText itemEditText;
    Button addButton;
    ListView itemListView;
    ArrayAdapter<String> adapter;
    ArrayList<String> itemList;
    String email; // Store the email globally

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        email = getIntent().getStringExtra("email");

        itemEditText = findViewById(R.id.editText);
        addButton = findViewById(R.id.addButton);
        itemListView = findViewById(R.id.listView);

        itemList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        itemListView.setAdapter(adapter);

        // Add item to list and database
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItem = itemEditText.getText().toString();
                if (!newItem.isEmpty()) {
                    itemList.add(newItem);
                    adapter.notifyDataSetChanged();
                    itemEditText.setText("");

                    // Add the item to the database
                    DBHelper dbHelper = new DBHelper(HomeActivity.this);
                    boolean inserted = dbHelper.insertItem(email, newItem);
                    if (inserted) {
                        Toast.makeText(HomeActivity.this, "Item added to database", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HomeActivity.this, "Failed to add item to database", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Start DynamicActivity with clicked item
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = itemList.get(position);
                Intent intent = new Intent(HomeActivity.this, DynamicActivity.class);
                intent.putExtra("selectedItem", selectedItem);
                intent.putExtra("email", email); // Pass the email
                startActivity(intent);
            }
        });

        // Populate itemList from database
        DBHelper dbHelper = new DBHelper(this);
        itemList.addAll(dbHelper.getItems(email));
        adapter.notifyDataSetChanged();
    }
}
