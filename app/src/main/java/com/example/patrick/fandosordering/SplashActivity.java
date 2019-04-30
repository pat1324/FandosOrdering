package com.example.patrick.fandosordering;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Activity that displays a splash screen. Sets up the counter for the order lines here to avoid
 * conflicts later in the app logic
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences order_line_count = getSharedPreferences("counter", MODE_PRIVATE);
        SharedPreferences.Editor counter = order_line_count.edit();
        counter.putInt("count", 0).commit();

        SharedPreferences order = getSharedPreferences("MyOrder", MODE_PRIVATE);
        SharedPreferences.Editor order_edit = order.edit().clear();
        order_edit.commit();

        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}
