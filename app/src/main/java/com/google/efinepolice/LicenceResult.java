package com.google.efinepolice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.bson.Document;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class LicenceResult extends AppCompatActivity {

    private App app;
    private User user;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;
    public TextView li;
    public TextView nic;
    public TextView na;
    public TextView sna;
    public TextView dob;
    public TextView address;
    public TextView issued;
    public TextView expired;
    public TextView bGroup;
    public TextView spec;
    private String licenceNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licence_result);

        licenceNo = getIntent().getStringExtra("licence");

        String appID = "efine-mjrwv"; // replace this with your App ID
        app = new App(new AppConfiguration.Builder(appID)
                .build());

        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("EfineDB");
        mongoCollection = mongoDatabase.getCollection("dLicence");

        li = findViewById(R.id.licence);
        nic = findViewById(R.id.nic);
        na = findViewById(R.id.name);
        sna = findViewById(R.id.surname);
        dob = findViewById(R.id.dob);
        address = findViewById(R.id.address);
        issued = findViewById(R.id.issued);
        expired = findViewById(R.id.expired);
        bGroup = findViewById(R.id.bgroup);
        spec = findViewById(R.id.spec);


        Document queryFilter = new Document().append("LicenceNo",licenceNo);
        mongoCollection.findOne(queryFilter).getAsync(result -> {
            if(result.isSuccess()) {
                Document resultdata = result.get();
                String txt="Hello "+resultdata.getString("Name");
                li.setText("Licence No : "+resultdata.getString("LicenceNo"));
                nic.setText("NIC No : "+resultdata.getString("NIC"));
                na.setText("Name : "+resultdata.getString("Name"));
                sna.setText("Surname : "+resultdata.getString("Surname"));
                dob.setText("Date of birth : "+resultdata.getString("DOB"));
                address.setText("Address : "+resultdata.getString("Address"));
                issued.setText("Issued Date : "+resultdata.getString("Issued"));
                expired.setText("Expired Date : "+resultdata.getString("Expired"));
                bGroup.setText("Blood Group : "+resultdata.getString("BloodGroup"));
                if(resultdata.getBoolean("Spectacles")){
                    spec.setText("Spectacles Required");
                }

                Log.v("SRA", "Name: " + resultdata.getString("BloodGroup"));

            } else {
                Toast.makeText(getApplicationContext(),"Not found",Toast.LENGTH_LONG).show();
                Log.v("SRA",result.getError().toString());
            }
        });

    }

    public void report(View view){
        Intent intent=new Intent(this, ReportViolation.class);
        intent.putExtra("licence", licenceNo);
        startActivity(intent);
    }
}