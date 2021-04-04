package com.google.efinepolice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.security.Timestamp;
import java.util.Date;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class ReportViolation extends AppCompatActivity {

    private String licence;
    private String emailAd;
    private Spinner type;
    private EditText discription;
    private Location gps_loc;
    private Location final_loc;
    private double latitude;
    private double longitude;
    private App app;
    private User user;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        licence = getIntent().getStringExtra("licence");
        SharedPreferences sharePref= PreferenceManager.getDefaultSharedPreferences(this);
        emailAd=sharePref.getString("email",null);


        type = (Spinner)findViewById(R.id.spinner);
        discription =(EditText)findViewById(R.id.editTextTextMultiLine);

        // type spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.TypeArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        try {

            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gps_loc != null) {
            final_loc = gps_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        }


        String appID = "efine-mjrwv"; // replace this with your App ID
        app = new App(new AppConfiguration.Builder(appID)
                .build());

        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("EfineDB");
        mongoCollection = mongoDatabase.getCollection("Report");



    }


    public void report(View view){

            String violationType = type.getSelectedItem().toString();
            String dis = discription.getText().toString();

            Report report = new Report(new ObjectId(),violationType,dis,licence,emailAd,latitude,longitude,new Date());
            Log.v("SRA", "object " + report.getDescription());
        mongoCollection.insertOne(report).getAsync(task -> {
            if (task.isSuccess()) {
                Toast.makeText(getApplicationContext(),"Reported Successfully",Toast.LENGTH_LONG).show();
                Log.v("SRA", "successfully inserted a document with id: " + task.get().getInsertedId());
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            } else {
                Log.e("SRA", "failed to insert documents with: " + task.getError().getErrorMessage());
                Toast.makeText(getApplicationContext(),"Something Wrong",Toast.LENGTH_LONG).show();
            }
        });

    }


}