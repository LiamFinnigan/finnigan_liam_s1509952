package com.example.finnigan_liam_s1509952;

// Liam Finnigan - S1509952 - MPD CW 2020

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RoadworkMessage roadworkMessage = (RoadworkMessage) getIntent().getSerializableExtra("RoadworkMessage.class");
        Log.e("book",roadworkMessage.getTitle());
        TextView titleTextView = (TextView) findViewById(R.id.textViewTitle);
        titleTextView.setText("" + roadworkMessage.getTitle());
        TextView descriptionTextView = (TextView) findViewById(R.id.textViewDescription);
        descriptionTextView.setText("" + roadworkMessage.getDescription());
        TextView linkTextView = (TextView) findViewById(R.id.textViewLink);
        linkTextView.setText("" + roadworkMessage.getLink());
        TextView geoTextView = (TextView) findViewById(R.id.textViewGeo);
        geoTextView.setText("" + roadworkMessage.getGeoPoint());
        TextView pubDateTextView = (TextView) findViewById(R.id.textViewPubDate);
        pubDateTextView.setText("" + roadworkMessage.getPublishedDate());

        findViewById(R.id.textViewGeo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) findViewById(R.id.textViewGeo);
                Intent intent = new Intent(DetailActivity.this, MapsActivity.class);
                intent.putExtra("geoPoint", textView.getText().toString());
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

}

