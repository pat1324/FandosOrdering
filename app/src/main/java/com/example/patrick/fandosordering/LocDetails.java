package com.example.patrick.fandosordering;

import com.google.android.gms.maps.model.LatLng;

class LocDetails {
    String mLocName;
    LatLng mLatLng;

    public LocDetails(String name, LatLng pos){
        mLocName = name;
        mLatLng = pos;
    }

    @Override
    public String toString() {

        return "" + mLocName + mLatLng;
    }
}
