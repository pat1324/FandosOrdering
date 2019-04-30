package com.example.patrick.fandosordering;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

/**
 * The adapter to be used to display the items in the cart to the user
 */
public class CartAdapter extends BaseAdapter {

    private Context mCurrentContext;
    private ArrayList<String> orderList;

    public CartAdapter(Context con, ArrayList<String> order) {
        mCurrentContext = con;
        orderList = order;
    }

    @Override
    public int getCount() { return orderList.size(); }//returns the size of the list of food items
    @Override
    public Object getItem(int i) { return orderList.get(i); }//returns string at the given index
    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mCurrentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_cart_items, null);
        }
        TextView orderline = view.findViewById(R.id.foodItemTextView);
        Button remove_button = view.findViewById(R.id.removeButton);

        //Removes an item when its associated remove button is clicked
        remove_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                orderList.remove(i);
                SharedPreferences order = mCurrentContext.getSharedPreferences("MyOrder", Context.MODE_PRIVATE);
                SharedPreferences.Editor orderedit = order.edit();
                orderedit.clear().commit();
                for (int line = 0; line < orderList.size(); line++){
                    orderedit.putString("order" + line, orderList.get(line)).commit();
                }
                SharedPreferences counter = mCurrentContext.getSharedPreferences("counter", Context.MODE_PRIVATE);
                SharedPreferences.Editor countedit = counter.edit();
                countedit.putInt("count", orderList.size()).commit();
                notifyDataSetChanged();
            }
        });
        orderline.setText(orderList.get(i));
        return view;
    }


}
