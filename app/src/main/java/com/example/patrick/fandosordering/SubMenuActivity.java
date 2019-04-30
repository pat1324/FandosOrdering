package com.example.patrick.fandosordering;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.SharedPreferences;


import java.util.ArrayList;

/**
 * Displays the sub menu from selecting a food category in the main menu. Clicking on an item will
 * prompt the user to decide whether the item is added to the cart
 */
public class SubMenuActivity extends AppCompatActivity {

    private ArrayList<String> menuList;
    private ListView mListView;
    private SubMenuAdapter adapter;
    int line;

    SharedPreferences order;
    SharedPreferences.Editor orderEditor;

    SharedPreferences order_number;
    SharedPreferences.Editor order_number_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_categories);

        order = getSharedPreferences("MyOrder",MODE_PRIVATE);
        orderEditor = order.edit();

        order_number = getSharedPreferences("counter", MODE_PRIVATE);
        line = order_number.getInt("count", 0);

        Bundle extras = getIntent().getExtras();
        String foodtype = extras.getString("Food");
        mListView = findViewById(R.id.foodCategoriesListView);
        menuList = new ArrayList<>();

        adapter = new SubMenuAdapter(this, foodtype, menuList);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(view.getContext());
                builder.setTitle("Add to order?");
                builder.setMessage("Add this item to your order?");
                builder.setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //add to cart
                                orderEditor.putString("order" + String.valueOf(line++), menuList.get(position));
                                orderEditor.commit();
                                order_number_editor = order_number.edit();
                                order_number_editor.putInt("count", line).commit();
                                //System.out.println("added");
                            }
                        });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ordering, menu);
        return true;
    }

    //takes the user to the cart activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
