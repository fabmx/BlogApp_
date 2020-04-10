package com.example.blogapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.blogapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mapAPI;
    SupportMapFragment mapFragment;

    String courtId = "court";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mapAPI = googleMap;

        Double lat;
        Double lng;

        lat = getIntent().getExtras().getDouble("lat");
        lng = getIntent().getExtras().getDouble("long");

        courtId = getIntent().getStringExtra("courtId");

        LatLngBounds rome = new LatLngBounds(
                new LatLng(41.872389, 12.48018), new LatLng(41.872389, 12.48018));

        mapAPI.moveCamera(CameraUpdateFactory.newLatLngZoom(rome.getCenter(), 10));

        LatLng coord = new LatLng(lat, lng);
        mapAPI.addMarker(new MarkerOptions().position(coord).title(courtId));

    }
}
