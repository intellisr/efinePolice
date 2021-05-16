package com.google.efinepolice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.bson.Document;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class LicenceResult extends AppCompatActivity {

    private App app;
    private User user;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;
    private MongoCollection<Document> mongoCollection2;
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
    public int count=0;
    public TextView det;
    public Button button;
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
        button = findViewById(R.id.button);
        det=findViewById(R.id.search);

        button.setVisibility(View.INVISIBLE);


        mongoCollection2 = mongoDatabase.getCollection("Report");

        Document queryFilter2  = new Document("licence", licenceNo).append("paid", false);
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection2.find(queryFilter2).iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                Log.v("EXAMPLE", "successfully found all plants for Store 42:");

                for (MongoCursor<Document> it = results; it.hasNext(); ) {
                    Document result = it.next();
                    count++;
                }

                Document queryFilter = new Document().append("LicenceNo",licenceNo);
                mongoCollection.findOne(queryFilter).getAsync(result -> {
                    if(result.isSuccess()) {
                        Document resultdata = result.get();

                        if(count >0){det.setText("Search Result : "+count+" pending payments");}else{det.setText("Search Result : No pending payments");}
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
                        button.setVisibility(View.VISIBLE);
                        Log.v("SRA", "Name: " + resultdata.getString("BloodGroup"));

                    } else {
                        Toast.makeText(getApplicationContext(),"Not found",Toast.LENGTH_LONG).show();
                        Log.v("SRA",result.getError().toString());
                    }
                });
            } else {
                Log.e("EXAMPLE", "failed to find documents with: ", task.getError());
            }
        });




    }

    public void report(View view){
        Intent intent=new Intent(this, ReportViolation.class);
        intent.putExtra("licence", licenceNo);
        startActivity(intent);
    }
}