package com.example.patrick.fandosordering;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.patrick.fandosordering.Order.Order;
import com.example.patrick.fandosordering.Order.OrderAdapter;

import java.util.ArrayList;

/**
 * Displays the registered favorite orders to the user
 */
public class ViewFavoritesActivity extends AppCompatActivity {

    private ListView favoritesListView;
    private ArrayList<Order> orderList;
    private FavoritesDatabaseHelper DBHelper;
    private OrderAdapter orderAdapter;

    SharedPreferences.Editor orderEdit;
    SharedPreferences.Editor orderTypeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_favorites);

        DBHelper = new FavoritesDatabaseHelper(getApplicationContext());
        orderList = new ArrayList<>(DBHelper.getAllFavorites().values());

        favoritesListView = findViewById(R.id.favoritesList);
        orderAdapter = new OrderAdapter(this, orderList);
        favoritesListView.setAdapter(orderAdapter);

        //unfinished
        favoritesListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(view.getContext());
                builder.setTitle("Choose order type!");
                builder.setMessage("Delivery or Pickup?");
                builder.setPositiveButton("Pickup",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.setNeutralButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.setNegativeButton("Delivery",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create().show();
            }
        });


    }

    private void refreshListView() {
        orderAdapter.notifyDataSetChanged();
    }
}
