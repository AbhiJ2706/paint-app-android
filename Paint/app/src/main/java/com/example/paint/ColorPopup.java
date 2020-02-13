package com.example.paint;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;

public class ColorPopup extends Activity {
    private int rgbR, rgbG, rgbB, PaintColor;
    private LinearLayout colorLayoutBg;
    private PopupWindow pw;

    public ColorPopup(SeekBar s2) {
        SeekBar s1 = s2;
        s1.setMax(0xFF);
        s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                rgbR = arg1;
                PaintColor = 10000*rgbR + 100*rgbG + rgbB;
                colorLayoutBg.setBackgroundColor(PaintColor);
                Log.d("color1", Integer.toString(PaintColor));

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

        });

        Log.d("ran!", "ran!");

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pw = new PopupWindow(inflater.inflate(R.layout.color_popup, null, false), Config.width, 200, true);
        pw.showAtLocation(findViewById(R.id.imageView), Gravity.CENTER, 0, 0);

    }




}
