package com.example.patrick.fandosordering;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple activity that displays a thank you message for ordering
 */
public class ThankYouActivity extends AppCompatActivity {

    private TextView thankYouText;
    private Button returnButton;
    SharedPreferences.Editor orderEditor;
    SharedPreferences.Editor src_dest_edit;
    SharedPreferences.Editor counterEdit;
    SharedPreferences.Editor orderTypeEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        //clears shared preferences for preparation for the next order
        orderEditor = getSharedPreferences("MyOrder", MODE_PRIVATE).edit().clear();
        orderEditor.commit();
        src_dest_edit = getSharedPreferences("orderFromTo", MODE_PRIVATE).edit().clear();
        src_dest_edit.commit();
        counterEdit = getSharedPreferences("counter", MODE_PRIVATE).edit().clear();
        counterEdit.commit();
        orderTypeEditor = getSharedPreferences("orderType", MODE_PRIVATE).edit().clear();
        orderTypeEditor.commit();

        thankYouText = findViewById(R.id.thankYouTextView);

        returnButton = findViewById(R.id.anotherOrderButton);

        //returns the user to the start activity
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), StartActivity.class);
                startActivity(intent);
            }
        });
    }
}
