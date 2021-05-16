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

import java.util.Date;


import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

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
    private MongoCollection<Document> mongoCollection2;
    public Report report;
    public  int CurrentPoints;

    private static final int MY_CAMERA_REQUEST_CODE = 100;

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

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, MY_CAMERA_REQUEST_CODE);
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

        mongoCollection2 = mongoDatabase.getCollection("points");
        Document queryFilter2  = new Document("Lid", licence);
        mongoCollection2.findOne(queryFilter2).getAsync(result -> {
            if(result.isSuccess()) {
                Document resultdata = result.get();
                CurrentPoints=resultdata.getInteger("points");
                Toast.makeText(getApplicationContext(),"Remaining points :"+CurrentPoints,Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(),"Not found",Toast.LENGTH_LONG).show();
                Log.v("Data",result.getError().toString());
            }
        });


    }


    public void report(View view){

            String violationType = String.valueOf(type.getSelectedItemPosition());
            String dis = discription.getText().toString();

            Document reportDoc  = new Document("_id", new ObjectId()).append("type", violationType).append("description", dis).append("licence", licence).append("police", emailAd).append("latitude", latitude).append("longitude", longitude).append("date", new Date()).append("paid", false);
            mongoCollection.insertOne(reportDoc).getAsync(task -> {
                if (task.isSuccess()) {
                    Toast.makeText(getApplicationContext(),"Reported Successfully",Toast.LENGTH_LONG).show();
                    int minPoints=pointCalc(violationType);
                    int points=CurrentPoints-minPoints;
                    Log.v("SRA", "min points: " + points);
                    update(licence,points);
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

    public void update(String Lid,int point){
        Document queryFilter = new Document("Lid",Lid);
        Document updateDocument = new Document("Lid",Lid).append("points",point);
        mongoCollection2.updateOne(queryFilter, updateDocument).getAsync(task -> {
            if (task.isSuccess()) {
                long count = task.get().getModifiedCount();
                if (count == 1) {
                    Log.v("EXAMPLE", "successfully updated a document.");
                } else {
                    Log.v("EXAMPLE", "did not update a document.");
                }
            } else {
                Log.e("EXAMPLE", "failed to update document with: ", task.getError());
            }
        });
    }

    public int pointCalc(String type){
        int id = Integer.parseInt(type);
        int value;
        if(id < 24){
            value=1;
        }else if(24 < id && id < 29){
            value=2;
        }else if(id > 28 ){
            value=5;
        }else{
            value=3;
        }
        return value;
    }


}