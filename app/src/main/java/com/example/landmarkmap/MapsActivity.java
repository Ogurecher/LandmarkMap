package com.example.landmarkmap;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_CODE_CREATE_LANDMARK = 1;
    private static final int REQUEST_CODE_SEARCH = 2;
    private static final int REQUEST_CODE_INFO = 3;
    private static final int RESULT_CODE_LANDMARK_CREATED = 1;
    private static final int RESULT_CODE_LANDMARK_EDITED = 2;
    private static final int CAMERA_ZOOM = 12;

    private GoogleMap map;
    private HashMap<String, MarkerOptions> markerOptionsMap = new HashMap<>();
    private HashMap<String, Marker> markerMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        FloatingActionButton buttonToSearch = findViewById(R.id.buttonToSearch);
        buttonToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSearchIntent = new Intent(MapsActivity.this, SearchActivity.class);
                toSearchIntent.putExtra("markers", markerOptionsMap);
                startActivityForResult(toSearchIntent, REQUEST_CODE_SEARCH);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //StorageManager.saveToFile(MapsActivity.this, getString(R.string.save_file_name), markerOptionsMap);
        markerOptionsMap = StorageManager.loadFromFile(MapsActivity.this, getString(R.string.save_file_name));
    }


    @Override
    protected void onPause() {
        super.onPause();
        StorageManager.saveToFile(MapsActivity.this, getString(R.string.save_file_name), markerOptionsMap);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (REQUEST_CODE_CREATE_LANDMARK) : {
                if (resultCode == EditActivity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    LatLng point = (LatLng) bundle.get("point");
                    String name = bundle.getString("name");
                    String comment = bundle.getString("comment");
                    MarkerOptions markerOptions = new MarkerOptions().position(point).title(name).snippet(comment);
                    Marker marker = map.addMarker(markerOptions);
                    markerMap.put(marker.getId(), marker);
                    markerOptionsMap.put(marker.getId(), markerOptions);
                }
                break;
            } case (REQUEST_CODE_SEARCH) : {
                if (resultCode == SearchActivity.RESULT_OK) {
                    MarkerOptions marker = (MarkerOptions) data.getExtras().get("marker");
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), CAMERA_ZOOM));
                }
                break;
            } case (REQUEST_CODE_INFO) : {
                if (resultCode == RESULT_CODE_LANDMARK_CREATED) {
                    String markerId = data.getStringExtra("id");
                    markerMap.get(markerId).remove();
                    markerMap.remove(markerId);
                    markerOptionsMap.remove(markerId);
                } else if (resultCode == RESULT_CODE_LANDMARK_EDITED) {
                    String markerId = data.getStringExtra("id");
                    String markerName = data.getStringExtra("name");
                    String markerComment = data.getStringExtra("comment");

                    Marker marker = markerMap.get(markerId);
                    MarkerOptions markerOptions = markerOptionsMap.get(markerId);
                    marker.setTitle(markerName);
                    marker.setSnippet(markerComment);
                    markerOptions.title(markerName);
                    markerOptions.snippet(markerComment);

                    markerMap.get(markerId).remove();
                    markerMap.remove(markerId);
                    markerOptionsMap.remove(markerId);
                    marker = map.addMarker(markerOptions);
                    markerMap.put(marker.getId(), marker);
                    markerOptionsMap.put(marker.getId(), markerOptions);
                }
                break;
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        for (HashMap.Entry<String, MarkerOptions> entry : markerOptionsMap.entrySet()) {
            Marker marker = map.addMarker(entry.getValue());
            markerMap.put(marker.getId(), marker);
        }

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                Intent createLandmarkIntent = new Intent(MapsActivity.this, EditActivity.class);
                createLandmarkIntent.putExtra("point", point);
                startActivityForResult(createLandmarkIntent, REQUEST_CODE_CREATE_LANDMARK);
            }
        });

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent toInfoIntent = new Intent(MapsActivity.this, LandmarkInfoActivity.class);
                toInfoIntent.putExtra("id", marker.getId());
                toInfoIntent.putExtra("name", marker.getTitle());
                toInfoIntent.putExtra("comment", marker.getSnippet());
                startActivityForResult(toInfoIntent, REQUEST_CODE_INFO);
            }
        });
    }
}
