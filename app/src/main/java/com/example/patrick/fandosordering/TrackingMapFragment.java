package com.example.patrick.fandosordering;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Displays the map that tracks the order. Displays a marker depending on the latitude and longitude
 * specified in firebase. Automatically centres on the current location of the delivery.
 */
public class TrackingMapFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener {

    private Geocoder geocoder;
    private GoogleMap mMap;
    private SharedPreferences src_dest;
    private DatabaseReference fbase;
    private LatLng current_position;

    public TrackingMapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map_control, container, false);
        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        mapFrag.getMapAsync(this);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap map){
        mMap = map;
        if (mMap != null) {

        }
        updateMapMarkers();
    }

    //contains the tracking logic of the map
    public void updateMapMarkers() {
        if (mMap != null) {
            mMap.clear();
        }
        src_dest = getContext().getSharedPreferences("orderFromTo", MODE_PRIVATE);
        String delivery_address = src_dest.getString("DeliveryAddress", null);
        String store_address = src_dest.getString("StoreAddress", null);
        fbase = FirebaseDatabase.getInstance().getReference().child("store location").child(store_address).child(delivery_address).child("status");

        fbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //assuming that when the cooking status changes it will be a latlng. Since the coordinates come from
                //driver gps it shouldn't be anything else
                if (dataSnapshot.getValue(String.class).equals("preparing")){
                    //do nothing
                }
                else if (dataSnapshot.getValue(String.class).equals("arrived")){
                    Intent intent = new Intent(getActivity(), ThankYouActivity.class);
                    startActivity(intent);

                    //TODO: delete order off of firebase
                }
                else {
                    String[] latlng = dataSnapshot.getValue(String.class).split(",");
                    if (latlng.length == 2) {
                        current_position = new LatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1]));
                        mMap.addMarker(new MarkerOptions().position(current_position));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current_position, 15));
                    }
                    else{
                        //TODO remove marker off map
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    @Override
    public void onMapLongClick(final LatLng latLng) {

    }




}
