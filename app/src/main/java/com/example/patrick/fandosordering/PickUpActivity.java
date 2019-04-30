package com.example.patrick.fandosordering;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Simple activity that displays the message "Preparing" to user until the status of the order is
 * updated on firebase with "ready". The return button is disabled until the status of the order is
 * ready
 */
public class PickUpActivity extends AppCompatActivity {

    private TextView orderStatusText;
    private Button returnButton;
    DatabaseReference fbase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up);

        //Gets store and delivery address from shared preferences file
        SharedPreferences src_dest = getSharedPreferences("orderFromTo", MODE_PRIVATE);
        String delivery_address = src_dest.getString("DeliveryAddress", null);
        String store_address = src_dest.getString("StoreAddress", null);

        returnButton = findViewById(R.id.anotherOrderButton2);
        returnButton.setEnabled(false);
        orderStatusText = findViewById(R.id.orderStatusTextView);
        orderStatusText.setText("Preparing");
        fbase = FirebaseDatabase.getInstance().getReference().child("store location").child(store_address).child(delivery_address).child("status");
        fbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(String.class).equals("ready")){
                    orderStatusText.setText("Ready for Pickup!");
                    returnButton.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clears shared preferences for preparation for the next order
                SharedPreferences.Editor orderEditor = getSharedPreferences("MyOrder", MODE_PRIVATE).edit().clear();
                orderEditor.commit();
                SharedPreferences.Editor src_dest_edit = getSharedPreferences("orderFromTo", MODE_PRIVATE).edit().clear();
                src_dest_edit.commit();
                SharedPreferences.Editor counterEdit = getSharedPreferences("counter", MODE_PRIVATE).edit().clear();
                counterEdit.commit();
                SharedPreferences.Editor orderTypeEditor = getSharedPreferences("orderType", MODE_PRIVATE).edit().clear();
                orderTypeEditor.commit();

                Intent intent = new Intent(view.getContext(), StartActivity.class);
                startActivity(intent);
            }
        });
    }
}
