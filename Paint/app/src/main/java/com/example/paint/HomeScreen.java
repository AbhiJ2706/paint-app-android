package com.example.paint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class HomeScreen extends AppCompatActivity {

    int tutDone;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_home_screen);

        sharedPref = HomeScreen.this.getPreferences(Context.MODE_PRIVATE);

        tutDone = sharedPref.getInt(getString(R.string.tut_done_key), 0);

    }

    public void goToPaint(View v){
        if (tutDone == 0) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.tut_done_key), 1);
            editor.apply();

            Intent k = new Intent(this, tutorial.class);
            startActivity(k);
        } else {
            Intent k = new Intent(this, MainActivity.class);
            startActivity(k);
        }

    }
    public void goToTut(View v){
        Intent k = new Intent(this, tutorial.class);
        startActivity(k);
    }
}