package com.example.patrick.fandosordering;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * The landing activity of the app. Allows the user to navigate to the favorites list or select an
 * order method of either delivery or pickup upon which the user is taken to the store locator map
 */
public class StartActivity extends AppCompatActivity {

    public TextView orderTypePrompt;
    public TextView orText;
    public Button favorites_button;
    public Button pickup_button;
    public Button delivery_button;
    SharedPreferences orderType;
    SharedPreferences.Editor orderType_editor;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        orderTypePrompt = findViewById(R.id.orderType_prompt_textview);
        orText = findViewById(R.id.or_textview);
        favorites_button = findViewById(R.id.favorites_button);
        pickup_button = findViewById(R.id.pickup_button);
        delivery_button = findViewById(R.id.delivery_button);
        orderType = getSharedPreferences("orderType", MODE_PRIVATE);
        orderType_editor = orderType.edit();

        delivery_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                orderType_editor.putString("type", "delivery").commit();
                Intent intent = new Intent(view.getContext(), StoreLocatorActivity.class);
                startActivity(intent);
            }
        });

        pickup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderType_editor.putString("type", "pickup").commit();
                Intent intent = new Intent(view.getContext(), StoreLocatorActivity.class);
                startActivity(intent);
            }
        });

        favorites_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderType_editor.putString("type", "favorite").commit();
                Intent intent = new Intent(view.getContext(), ViewFavoritesActivity.class);
                startActivity(intent);
            }
        });

    }
}
