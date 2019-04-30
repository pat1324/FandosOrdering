package com.example.patrick.fandosordering;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.patrick.fandosordering.Order.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

/**
 * Holds the list of food items to be ordered and provides the user with options to register order
 * as a favorite, place the order or go back to the menu to continue ordering
 */
public class CartActivity extends AppCompatActivity {

    private Button returnButton;
    private Button orderButton;
    private Button addToFavoritesButton;
    SharedPreferences order;
    SharedPreferences src_dest;
    SharedPreferences.Editor orderEditor;
    SharedPreferences orderType;
    DatabaseReference fbase = FirebaseDatabase.getInstance().getReference();
    private FavoritesDatabaseHelper DBHelper;

    private ArrayList<String> cartList;
    private CartAdapter adapter;
    private ListView mListView;
    Map<String, ?> order_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        DBHelper = new FavoritesDatabaseHelper(getApplicationContext());

        mListView = findViewById(R.id.orderListView);
        cartList = new ArrayList<>();

        order = getSharedPreferences("MyOrder", MODE_PRIVATE);
        src_dest = getSharedPreferences("orderFromTo", MODE_PRIVATE);
        orderType = getSharedPreferences("orderType", MODE_PRIVATE);
        orderEditor = order.edit();

        //grab all values from sharedpreference MyOrder
        cartList.clear();
        order_map = order.getAll();
        for (Map.Entry<String, ?> entry : order_map.entrySet()) {
            cartList.add(entry.getValue().toString());
        }

        adapter = new CartAdapter(this, cartList);
        mListView.setAdapter(adapter);

        returnButton = findViewById(R.id.backToMenuButton);
        orderButton = findViewById(R.id.placeOrderButton);
        addToFavoritesButton = findViewById(R.id.addToFavoritesButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); //returns the user to the order menu
            }
        });

        /*
        Allows adding of the current order to the favorites database
         */
        addToFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(view.getContext());
                builder.setTitle("Favorites?");
                builder.setMessage("Register this order to favorites?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String orderList = create_orderline();
                                Order DBorder = new Order(0, orderList);
                                DBHelper.addOrder(DBorder);
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

        /*
        Sends order to firebase which can be read by store staff
         */
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //upload delivery address and order items to firebase
                if (cartList.isEmpty()){
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Order is Empty!");
                    builder.setMessage("Add some items to your order!");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    builder.create().show();
                }
                String address = src_dest.getString("DeliveryAddress", null);
                String final_order = create_orderline();
                String store_address = src_dest.getString("StoreAddress", null);
                fbase.child("store location").child(store_address).child(address).child("order").setValue(final_order);
                fbase.child("store location").child(store_address).child(address).child("status").setValue("preparing");
                orderEditor.clear().commit();
                //go to map tracking activity if order type is delivery, otherwise go to the pickup
                //activity
                if (getSharedPreferences("orderType", MODE_PRIVATE).getString("type", null).equals("delivery")){
                    Intent intent = new Intent(view.getContext(), DeliveryTrackingActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(view.getContext(), PickUpActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /*
    Method for building the order in string format
     */
    public String create_orderline(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < cartList.size(); i++){
            stringBuilder.append(cartList.get(i));
            if (i != cartList.size() - 1){
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }
}
