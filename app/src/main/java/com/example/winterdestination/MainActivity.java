package com.example.winterdestination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private FirebaseAuth auth;

    private RecyclerView recyclerView;
    private ArrayList<TouristDestination> list;
    Spinner spinner;
    String selectedDestination = "none";
    private MyAdapter adapter;

    ArrayList<String> destinations;
    TextView welcomeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        welcomeText = findViewById(R.id.textView);
        db = FirebaseDatabase.getInstance();
        destinations = new ArrayList<>();

        spinner = findViewById(R.id.spinner);
        loadWelcomeText();
        loadContent();
    }

    public void showDestination(View view){
        HashMap<String, Object> content = new HashMap<>();
        if (!selectedDestination.equals("none")){
            for (TouristDestination destination: list) {
                if (destination.getName().equals(selectedDestination)) {
                    content.put("name", destination.getName());
                    content.put("description", destination.getDescription());
                    content.put("image", destination.getImage());
                    content.put("longitude", destination.getLongitude());
                    content.put("latitude", destination.getLatitude());
                }
            }
            Intent intent = new Intent(this, DestinationActivity.class);
            intent.putExtra("content", content);
            startActivity(intent);
        }
        else
            Toast.makeText(this, "No destination selected!", Toast.LENGTH_SHORT).show();
    }

    private void loadContent() {
        ref = FirebaseDatabase.getInstance().getReference().child("touristDestinations");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                destinations.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    TouristDestination destination = new TouristDestination(dataSnapshot.child("name").getValue(String.class),dataSnapshot.child("description").getValue(String.class),dataSnapshot.child("image").getValue(String.class),dataSnapshot.child("longitude").getValue(double.class),dataSnapshot.child("latitude").getValue(double.class));
                    list.add(destination);
                    destinations.add(destination.getName());
                }
                adapter = new MyAdapter(MainActivity.this,list);
                recyclerView.setAdapter(adapter);
                ArrayAdapter<String> ad = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, destinations);
                ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(ad);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDestination = adapterView.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void loadWelcomeText() {
        ref = db.getReference().child("welcomeMessage");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String text = snapshot.getValue(String.class);
                welcomeText.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                welcomeText.setText("Welcome!");
            }
        });
    }
    public void logOut(View view){
        auth.signOut();
        Toast.makeText(MainActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}