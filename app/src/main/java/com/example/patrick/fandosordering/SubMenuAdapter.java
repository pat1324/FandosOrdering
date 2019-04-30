package com.example.patrick.fandosordering;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Adapter that allows the display of submenu items to the user. Reusable activity, is used for all
 * submenus
 */
public class SubMenuAdapter extends BaseAdapter {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private Context mCurrentContext;
    private ArrayList<String> menuList;
    private String foodtype;

    public SubMenuAdapter(Context con, String type, ArrayList<String> food) {
        mCurrentContext = con;
        foodtype = type;
        menuList = food;
        mDatabase.child("Food").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Iterable<DataSnapshot> foodChildren = snapshot.child(foodtype).getChildren();
                for (DataSnapshot d : foodChildren) {
                    menuList.add(d.getValue().toString());
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public int getCount() { return menuList.size(); }//returns the size of the list of food items
    @Override
    public Object getItem(int i) { return menuList.get(i); }//returns string at the given index
    @Override
    public long getItemId(int i) { return i; }

    /*
    Create a space for each item in the list and set its text accordingly to its attributes
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // Check if view already exists. If not inflate it
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mCurrentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_food_item, null);
        }
        //initialise text view
        TextView typeView = view.findViewById(R.id.foodCategoryTextView);

        String foodtype = menuList.get(i);
        typeView.setText(foodtype);
        return view;
    }
}
