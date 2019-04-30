package com.example.patrick.fandosordering;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * An activity that holds the map fragment that allows the order to be tracked
 * Estimated time is currently not working
 */
public class DeliveryTrackingActivity extends FragmentActivity implements OnMapReadyCallback {

    private TrackingMapFragment mMapFragment;
    private FrameLayout layout;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_tracking);

        mMapFragment = new TrackingMapFragment();
        layout = findViewById(R.id.map_tracker);
        getSupportFragmentManager().beginTransaction().add(layout.getId(),
                mMapFragment).commit();//adds map to activity

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }
}
