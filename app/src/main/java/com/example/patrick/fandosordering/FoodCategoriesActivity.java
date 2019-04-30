package com.example.patrick.fandosordering;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Displays the main categories of food items to the user in a list. Items can be clicked to take
 * the user to the submenu
 */
public class FoodCategoriesActivity extends AppCompatActivity {

    private ListView mListView;
    private FoodAdapter adapter;
    private ArrayList<String> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_categories);

        mListView = (ListView) findViewById(R.id.foodCategoriesListView);

        // Initialize the food list
        foodList = new ArrayList<>();
        adapter = new FoodAdapter(this, foodList);
        mListView.setAdapter(adapter);

        //takes the user to the submenu activity displaying the clicked category's items
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String foodtype = foodList.get(position);//the category to view
                Bundle bundle = new Bundle();
                bundle.putString("Food", foodtype);
                Intent intent = new Intent(view.getContext(), SubMenuActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ordering, menu);
        return true;
    }

    //this method sends the user to the order cart activity where the user can view the current
    //items ordered as well as options to favorite or send order
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
