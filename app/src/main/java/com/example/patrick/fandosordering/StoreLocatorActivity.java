package com.example.patrick.fandosordering;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DatabaseReference;

/**
 * Displays the store locator map. Stores are marked on the map and can be clicked to select them
 * as the store to order from. Also includes an address autocomplete bar at the top of the activity
 * for easier selecting of delivery address.
 */
public class StoreLocatorActivity extends FragmentActivity implements OnMapReadyCallback{

    private static final String TAG = "StoreLocatorActivity";
    private MapControlFragment mMapFragment;
    private FrameLayout mPrimaryFrame;
    private GoogleMap map;
    private DatabaseReference fbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storelocator);

        mMapFragment = new MapControlFragment();
        mPrimaryFrame = findViewById(R.id.primaryFrame);
        getSupportFragmentManager().beginTransaction().add(mPrimaryFrame.getId(),
                mMapFragment).commit();//adds map to activity

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMapFragment.setFocus(place.getLatLng());
                Log.i(TAG, "Place: " + place.getName());
            }
            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });


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

