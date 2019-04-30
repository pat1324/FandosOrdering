package com.example.patrick.fandosordering;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;


/**
 * Fragment that contains map logic for selection of store to order from
 */
public class MapControlFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private OnMapClicked mListener;
    private ArrayList<LocDetails> mSavedLocations;
    private LatLng mCurrentLoc;
    private DatabaseReference fbase;
    private SharedPreferences src_dest;
    private SharedPreferences.Editor src_dest_edit;
    private Address raw_address;
    private String address;
    private Geocoder geocoder;

    public MapControlFragment() {
        mSavedLocations = new ArrayList<>();
        mCurrentLoc = new LatLng(-37.814161, 145.126128);//instead of this, in future need location of user. Currently unfixed
    }

    public void initFragment(OnMapClicked listener,
                             ArrayList<LocDetails> locations) {
        mListener = listener;
        mSavedLocations = locations;
    }

    //Prepares the map view
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            //Set click listeners to markers. On click saves the address of the store,
            //the address of the user and takes the user to the menu activity
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            //save address data for delivery use
                            System.out.println(marker.getTitle());
                            src_dest = getContext().getSharedPreferences("orderFromTo", Context.MODE_PRIVATE);
                            src_dest_edit = src_dest.edit();
                            try {
                                //currently hard coded to simplify implementation of tracking
                                raw_address = geocoder.getFromLocation(-37.814161,145.126128,1).get(0);
                                address = raw_address.getAddressLine(0);
                                address += ", ";
                                address += raw_address.getAddressLine(1);
                                address += ", ";
                                address += raw_address.getAddressLine(2);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            src_dest_edit.putString("DeliveryAddress", address);
                            src_dest_edit.putString("StoreAddress", marker.getTitle());
                            src_dest_edit.commit();
                            Intent intent = new Intent(getActivity(), FoodCategoriesActivity.class);
                            startActivity(intent);
                        }
                    });
                    return false;
                }
            });
        }
        mMap.setOnMapLongClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                mCurrentLoc, 15));
        updateMapMarkers();
    }

    //Updates the map to display markers of store locations
    public void updateMapMarkers() {
        if (mMap != null) {
            mMap.clear();
            //currently only marks 1 store location
            fbase = FirebaseDatabase.getInstance().getReference().child("store location").child("711 Station Street Box Hill VIC 3128 Australia");
            fbase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println(dataSnapshot.child("latitude").getValue(double.class).toString());
                    LatLng loc = new LatLng(dataSnapshot.child("latitude").getValue(double.class),
                            dataSnapshot.child("longitude").getValue(double.class));
                    mMap.addMarker(new MarkerOptions().position(loc).title(dataSnapshot.getKey()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    //moves the map to center on the location passed into the method
    public void setFocus(LatLng loc) {
        mCurrentLoc = loc;
        if(mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
        }
    }

    @Override
    public void onMapLongClick(final LatLng latLng) {

    }

    public interface OnMapClicked {
        void onMapClicked(String locName, LatLng position);
    }

}