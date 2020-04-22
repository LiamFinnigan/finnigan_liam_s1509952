package com.example.finnigan_liam_s1509952;

// Liam Finnigan - S1509952 - MPD CW 2020

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private float lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String geoPoint = (String) getIntent().getSerializableExtra("geoPoint");
        String[] geoPoints = geoPoint.split(" ");
        lat = new Float(geoPoints[0]);
        lng = new Float(geoPoints[1]);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker at the incident
        LatLng Event = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(Event).title("Area of Event"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng),14.0f));
    }
}
