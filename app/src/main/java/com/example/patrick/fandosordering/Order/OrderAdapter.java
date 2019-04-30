package com.example.patrick.fandosordering.Order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.patrick.fandosordering.R;
import java.util.ArrayList;

/**
 * The adapter to be used to display favorites stored in the database as a list for users to view
 */
public class OrderAdapter extends BaseAdapter {

    private Context mCurrentContext;
    private ArrayList<Order> orderList;

    public OrderAdapter(Context con, ArrayList<Order> orders){
        mCurrentContext = con;
        orderList = orders;
    }

    @Override
    public int getCount() { return orderList.size(); }//returns the size of the list of orders
    @Override
    public Object getItem(int i) { return orderList.get(i); }//returns an order at the given index
    @Override
    public long getItemId(int i) { return i; }

    /*
    Create a space for each item in the list and set its text accordingly to the order line
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // Check if view already exists. If not inflate it
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mCurrentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_favorites, null);
        }
        //initialise text view
        TextView orderView = view.findViewById(R.id.favorites_item);
        orderView.setText(orderList.get(i).getOrder());
        return view;
    }
}
