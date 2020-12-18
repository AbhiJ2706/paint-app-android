package com.example.paint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class tutorial extends AppCompatActivity {

    public final int[] images = {R.drawable.tutscreen1, R.drawable.tutscreen2, R.drawable.tutscreen3, R.drawable.tutscreen4, R.drawable.tutscreen5};
    public final int[] flavor_text = {R.string.tuttext1, R.string.tuttext2, R.string.tuttext3, R.string.tuttext4, R.string.tuttext5};

    private int index = 0;
    private int ending = 5;

    public ImageView i;

    public Button b;

    public TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        i = findViewById(R.id.imageView3);
        b = findViewById(R.id.tutbutton);
        t = findViewById(R.id.tuttextv);
        i.setImageResource(images[0]);
    }

    public void scroll(View v){
        if (index < ending - 2){
            index ++;
            i.setImageResource(images[index]);
            t.setText(flavor_text[index]);
        } else if (index == ending - 2) {
            b.setText(R.string.draw);
            index ++;
            i.setImageResource(images[index]);
            t.setText(flavor_text[index]);
        } else if (index == ending - 1) {
            Intent k = new Intent(this, MainActivity.class);
            startActivity(k);
        }
    }
}