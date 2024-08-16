package com.example.VoyaGo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.VoyaGo.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DynamicActivity extends AppCompatActivity {

    private EditText destinationEditText;
    private ListView plannedListView;
    private ArrayAdapter<String> plannedAdapter;
    private ArrayList<String> plannedList;
    private ArrayList<Boolean> checkedStates;
    private DBHelper dbHelper;
    private String selectedItem;
    private String email;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private byte[] imageBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);

        destinationEditText = findViewById(R.id.editTextDestination);
        plannedListView = findViewById(R.id.listViewPlanned);
        Button addButton = findViewById(R.id.addButton);
        Button cameraButton = findViewById(R.id.cameraButton);

        plannedList = new ArrayList<>();
        checkedStates = new ArrayList<>(); // Initialize checkbox states

        dbHelper = new DBHelper(this);

        selectedItem = getIntent().getStringExtra("selectedItem");
        email = getIntent().getStringExtra("email");

        loadPlannedDestinations(); // Load planned destinations and checkbox states

        plannedAdapter = new ArrayAdapter<>(this, R.layout.item_planned_destination, R.id.textViewDestination, plannedList);

        plannedListView.setAdapter(plannedAdapter);

        plannedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                checkedStates.set(position, !checkedStates.get(position));
                checkedStates.set(position, !checkedStates.get(position));
                plannedAdapter.notifyDataSetChanged();
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String destination = destinationEditText.getText().toString().trim();
                if (!destination.isEmpty()) {
                    plannedList.add(destination);
                    checkedStates.add(false);
                    dbHelper.insertPlannedDestination(email, selectedItem, destination, false); // Insert unchecked
                    plannedAdapter.notifyDataSetChanged();
                    destinationEditText.setText("");
                }
            }
        });
    }


    private void loadPlannedDestinations() {
        plannedList.clear();
        checkedStates.clear();
        plannedList.addAll(dbHelper.getPlannedDestinations(email, selectedItem));
        for (String destination : plannedList) {
            checkedStates.add(dbHelper.isDestinationChecked(email, selectedItem, destination));
        }
    }

    // Launch camera intent
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            imageBytes = stream.toByteArray();

            if (imageBytes != null) {
                dbHelper.insertItemImage(email, selectedItem, imageBytes);
            }
        }
    }
}