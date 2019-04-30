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
 * Adapter that obtains menu data from firebase to display to users
 * This adapter obtains the main menu categories, eg. Beef, Fish, Chicken
 */
public class FoodAdapter extends BaseAdapter {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private Context mCurrentContext;
    private ArrayList<String> foodList;

    public FoodAdapter(Context con, ArrayList<String> food) {
        mCurrentContext = con;
        foodList = food;

        //Obtains data from firebase under the "Food" node
        mDatabase.child("Food").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Iterable<DataSnapshot> foodChildren = snapshot.getChildren();
                for (DataSnapshot d : foodChildren) {
                    foodList.add(d.getKey());
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public int getCount() { return foodList.size(); }//returns the size of the list of food items
    @Override
    public Object getItem(int i) { return foodList.get(i); }//returns string at the given index
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
        TextView typeView = (TextView) view.findViewById(R.id.foodCategoryTextView);

        String foodtype = foodList.get(i);
        typeView.setText(foodtype);
        //Changes the text color depending on what category it is
        if (foodtype.equals("Beef")){
            typeView.setTextColor(Color.parseColor("#7a5230")); //brown
        }
        else if (foodtype.equals("Chicken")){
            typeView.setTextColor(Color.parseColor("#bd00ff")); // purple
        }
        else if (foodtype.equals("Lamb")){
            typeView.setTextColor(Color.parseColor("#ff0000")); // red
        }
        else if (foodtype.equals("Seafood")){
            typeView.setTextColor(Color.parseColor("#0000ff")); // blue
        }
        else if (foodtype.equals("Vegetarian")){
            typeView.setTextColor(Color.parseColor("#228B22")); // green
        }
        else if (foodtype.equals("Dessert")){
            typeView.setTextColor(Color.parseColor("#000000")); // black
        }
        else if (foodtype.equals("Drink")){
            typeView.setTextColor(Color.parseColor("#f2a409")); // orange
        }
        return view;
    }
}
