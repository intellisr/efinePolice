package com.google.efinepolice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.bson.Document;

import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class MainActivity extends AppCompatActivity {

    private App app;
    private EditText email;
    private EditText pw;
    private User user;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;
    private String emailAd;
    public String Email;
    public String PW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        pw = findViewById(R.id.pw);

        Realm.init(this);
        String appID = "efine-mjrwv"; // replace this with your App ID
        app = new App(new AppConfiguration.Builder(appID)
                .build());

        SharedPreferences sharePref= PreferenceManager.getDefaultSharedPreferences(this);
        emailAd=sharePref.getString("email",null);

        if(emailAd != null){
            Intent intent=new Intent(this, Home.class);
            startActivity(intent);
        }

    }

    public void loginApp(View view){

        Email=email.getText().toString();
        PW=pw.getText().toString();
        Log.v("AUTH", "EM "+Email+" pw "+PW);
        Credentials emailPasswordCredentials = Credentials.emailPassword(Email,PW);
        app.loginAsync(emailPasswordCredentials, it -> {
            if (it.isSuccess()) {
                Log.v("AUTH", "Successfully authenticated using an email and password.");
                user = app.currentUser();
                mongoClient = user.getMongoClient("mongodb-atlas");
                mongoDatabase = mongoClient.getDatabase("EfineDB");
                mongoCollection = mongoDatabase.getCollection("Police");

                Document queryFilter = new Document().append("Email",Email);
                mongoCollection.findOne(queryFilter).getAsync(result -> {
                    if(result.isSuccess()) {
                        Document resultdata = result.get();

                        if(Email.equals(resultdata.getString("Email"))){
                            SharedPreferences sharePref= PreferenceManager.getDefaultSharedPreferences(this);
                            SharedPreferences.Editor editor = sharePref.edit();
                            editor.putString("email",Email);
                            editor.apply();

                            Intent intent=new Intent(this, Home.class);
                            startActivity(intent);
                        }


                    } else {
                        Toast.makeText(getApplicationContext(),"Not found",Toast.LENGTH_LONG).show();
                        Log.v("Data",result.getError().toString());
                    }
                });

            } else {
                Log.e("AUTH", it.getError().toString());
            }
        });
    }
}