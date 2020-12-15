package com.example.paint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class tutorial extends AppCompatActivity {

    public final BitmapDrawable[] images = {};

    private int index = 0;
    private int ending = 5;

    public ImageView i;

    public Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
    }

    public void scroll(View v){
        if (index < ending){
            index ++;
            i.setImageDrawable(images[index]);
        } else if (index == ending - 2) {
            b.setText(R.string.draw);
            index ++;
            i.setImageDrawable(images[index]);
        } else if (index == ending - 1) {
            Intent k = new Intent(this, MainActivity.class);
            startActivity(k);
        }
    }
}