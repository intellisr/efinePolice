package com.google.efinepolice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class IdView extends AppCompatActivity {

    public PoliceUser eUser;
    public TextView id;
    public TextView email;
    public TextView na;
    public TextView rank;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_view);

        eUser= (PoliceUser) getIntent().getSerializableExtra("User");

        id = findViewById(R.id.licence);
        email = findViewById(R.id.nic);
        na = findViewById(R.id.name);
        rank = findViewById(R.id.surname);


        id.setText("Police ID No : "+eUser.getIdNo());
        email.setText("Email : "+eUser.getEmail());
        na.setText("Name : "+eUser.getName());
        rank.setText("Rank : "+eUser.getRankNo());

    }
}