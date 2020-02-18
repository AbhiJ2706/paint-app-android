package com.example.paint;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    private final int UNDO_AMOUNT = 50;
    private SeekBar thick;
    private SeekBar sides;
    private ImageView i;
    private Canvas c;
    private Bitmap b;
    private Paint p;
    private int thickness = 1;
    private int numSides = 3;
    private Config.PenType penMode = Config.PenType.DRAW;
    private Config.Shape shapeType;
    private Bitmap b2 = null;
    private Thread draw = null;
    private Thread returnShape = null;
    private PopupWindow pw;
    private ArrayList<Shape> shapes;
    private int prevX = 0;
    private int prevY = 0;
    private int rgbR, rgbG, rgbB;
    private int PaintColor = Color.argb(0xFF, 0, 0, 0);
    private LinearLayout l;
    private int bgColor;
    private int undoModifier = 2;
    private int redoModifier = 1;
    private ArrayList<Bitmap> undoBitmaps = new ArrayList<>();
    private Bitmap undo_temp_bitmap;
    private String textInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thick = findViewById(R.id.seekBar3);
        i = findViewById(R.id.imageView);
        p = new Paint();
        c = new Canvas();
        l = findViewById(R.id.l1);
        l.setVisibility(View.GONE);
        shapes = new ArrayList<>();
        b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        i.post(new Runnable() {
            @Override
            public void run() {
                b = Bitmap.createBitmap(i.getWidth(), i.getHeight(), Bitmap.Config.ARGB_8888);
                c = new Canvas(b);
                i.setImageBitmap(b);
                chooseBG();
            }
        });
        thick = findViewById(R.id.seekBar3);
        thick.setMax(15);
        thick.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                p.setStrokeWidth(arg1 + 1);
                thickness = arg1 + 1;
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

        sides = findViewById(R.id.seekBar8);
        sides.setMax(10);
        sides.setMin(3);
        sides.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                numSides = arg1;
                if (numSides == 4){
                    numSides = 5;
                }
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

        l.post(new Runnable() {
            @Override
            public void run() {
                SeekBar s1 = findViewById(R.id.seekBar7);
                s1.setMax(0xFF);
                s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

                    @Override
                    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                        rgbR = arg1;
                        PaintColor = Color.argb(0xFF, rgbR, rgbG, rgbB);
                        GradientDrawable gradientDrawable = (GradientDrawable) l.getBackground().mutate();
                        gradientDrawable.setColor(PaintColor);
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

                SeekBar s2 = findViewById(R.id.seekBar2);
                s2.setMax(0xFF);
                s2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

                    @Override
                    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                        rgbG = arg1;
                        PaintColor = Color.argb(0xFF, rgbR, rgbG, rgbB);
                        GradientDrawable gradientDrawable = (GradientDrawable) l.getBackground().mutate();
                        gradientDrawable.setColor(PaintColor);
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

                SeekBar s3 = findViewById(R.id.seekBar6);
                s3.setMax(0xFF);
                s3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

                    @Override
                    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                        rgbB = arg1;
                        PaintColor = Color.argb(0xFF, rgbR, rgbG, rgbB);
                        GradientDrawable gradientDrawable = (GradientDrawable) l.getBackground().mutate();
                        gradientDrawable.setColor(PaintColor);
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

            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Config.height = displayMetrics.heightPixels;
        Config.width = displayMetrics.widthPixels;
        Config.offset = Config.height/10;

    }


    @Override
    public boolean onTouchEvent(MotionEvent e){
        c = new Canvas(b);
        i.setImageBitmap(b);
        switch(e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (penMode == Config.PenType.SHAPE_FILL || penMode == Config.PenType.SHAPE_STROKE) {
                    for (Shape s : shapes) {
                        s.init = false;
                    }
                    shapes.add(new Shape(e.getX(), e.getY() - Config.offset, shapeType, PaintColor));
                } else {

                }
            case MotionEvent.ACTION_MOVE:
                switch(penMode) {
                    case DRAW:
                        synchronized (new Object()){
                            if (returnShape != null) {
                                returnShape.interrupt();
                            }
                            p.setColor(PaintColor);
                            if (prevX != 0) {
                                p.setStrokeWidth((float) thickness);
                                c.drawLine((float) prevX, (float) prevY, e.getX(), e.getY() - Config.offset, p);
                            }
                            prevX = (int) e.getX();
                            prevY = (int) (e.getY() - Config.offset);
                            Log.d("Doot", Integer.toString(Config.offset));

                            returnShape = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(100);
                                        prevX = 0;
                                        if (undo_temp_bitmap != null) {
                                            undoBitmaps.add(undo_temp_bitmap);
                                            undo_temp_bitmap = null;
                                        }
                                        if (undoBitmaps.size() > UNDO_AMOUNT) {
                                            undoBitmaps.remove(0);
                                        }
                                        final Bitmap b_copy = b;
                                        undoBitmaps.add(b_copy);
                                        undoModifier = 2;
                                        Log.d("added", Integer.toString(undoBitmaps.size()));
                                    } catch (InterruptedException e) {
                                    }
                                }
                            };
                            returnShape.start();
                        }
                        break;
                    case SHAPE_FILL:
                    case SHAPE_STROKE:
                        if (draw != null) {
                            draw.interrupt();
                        }
                        b2 = Bitmap.createBitmap(i.getWidth(), i.getHeight(), Bitmap.Config.ARGB_8888);
                        c = new Canvas(b2);
                        for (Shape s : shapes) {
                            if (s.init) {
                                s.finish(e.getX(), e.getY() - Config.offset, c, p, numSides, penMode, textInput);
                            }
                        }
                        if (b2 != null) {
                            combine(b, b2, 0);
                        }
                        break;
                    case ERASE:
                        p.setColor(bgColor);
                        p.setStyle(Paint.Style.FILL);
                        c.drawCircle(e.getX(), e.getY() - Config.offset, thickness, p);
                }
            case MotionEvent.ACTION_CANCEL:
                if (penMode == Config.PenType.SHAPE_FILL || penMode == Config.PenType.SHAPE_STROKE) {
                    final Thread render = new Thread() {
                        @Override
                        public void run() {
                            if (b2 != null) {
                                if (undo_temp_bitmap != null) {
                                    undoBitmaps.add(undo_temp_bitmap);
                                    undo_temp_bitmap = null;
                                }
                                b = combine(b, b2);
                                if (undoBitmaps.size() > UNDO_AMOUNT) {
                                    undoBitmaps.remove(0);
                                }
                                final Bitmap b_copy = b;
                                undoBitmaps.add(b_copy);
                                undoModifier = 2;
                            }
                        }
                    };
                    draw = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(100);
                                render.start();
                            } catch (InterruptedException e) {
                            }
                        }
                    };
                    draw.start();
                }
        }
        return false;
    }

    private Bitmap combine(Bitmap b, Bitmap b2){
        Bitmap bmOverlay = Bitmap.createBitmap(b.getWidth(), b.getHeight(), b.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        i.setImageBitmap(bmOverlay);
        canvas.drawBitmap(b, new Matrix(), null);
        canvas.drawBitmap(b2, 0, 0, null);
        return bmOverlay;
    }

    private void combine(Bitmap b, Bitmap b2, int x){
        Bitmap bmOverlay = Bitmap.createBitmap(b.getWidth(), b.getHeight(), b.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        i.setImageBitmap(bmOverlay);
        canvas.drawBitmap(b, new Matrix(), null);
        canvas.drawBitmap(b2, 0, 0, null);
    }

    public void undo(View v) {
        b = Bitmap.createBitmap(i.getWidth(), i.getHeight(), Bitmap.Config.ARGB_8888);
        c = new Canvas(b);
        i.setImageBitmap(b);
        if (undoBitmaps.size() - undoModifier < 0){
            c.drawBitmap(undoBitmaps.get(0), null, new Rect(0, 0, i.getWidth(), i.getHeight()), p);
        } else {
            c.drawBitmap(undoBitmaps.get(undoBitmaps.size() - undoModifier), null, new Rect(0, 0, i.getWidth(), i.getHeight()), p);
            undoModifier++;
        }
        undo_temp_bitmap = b;
    }

    public void redo (View v) {
        b = Bitmap.createBitmap(i.getWidth(), i.getHeight(), Bitmap.Config.ARGB_8888);
        c = new Canvas(b);
        i.setImageBitmap(b);
        if (undoBitmaps.size() - undoModifier + redoModifier < undoBitmaps.size()){
            c.drawBitmap(undoBitmaps.get(undoBitmaps.size() - undoModifier + redoModifier), null, new Rect(0, 0, i.getWidth(), i.getHeight()), p);
        }
        if (undoBitmaps.size() > UNDO_AMOUNT) {
            undoBitmaps.remove(0);
        }
        final Bitmap b_copy = b;
        undoBitmaps.add(b_copy);
        redoModifier ++;
    }

    public void setEraserType(View v){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pw = new PopupWindow(inflater.inflate(R.layout.eraser_popup, null, false), Config.width, 200, true);
        pw.showAtLocation(this.findViewById(R.id.imageView), Gravity.BOTTOM, 0, 300);
    }

    public void shapeOrPen(View v){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pw = new PopupWindow(inflater.inflate(R.layout.shapeorpen_popup, null, false), Config.width, 200, true);
        pw.showAtLocation(this.findViewById(R.id.imageView), Gravity.BOTTOM, 0, 300);
    }

    public void chooseShape(){
        pw.dismiss();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pw = new PopupWindow(inflater.inflate(R.layout.shape_popup, null, false), Config.width, 200, true);
        pw.showAtLocation(this.findViewById(R.id.imageView), Gravity.BOTTOM, 0, 300);
    }

    public void goBack(View v) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pw = new PopupWindow(inflater.inflate(R.layout.goback_popup, null, false), Config.width, 200, true);
        pw.showAtLocation(this.findViewById(R.id.imageView), Gravity.TOP, 0, 300);
    }

    public void chooseFill(View v){
        penMode = Config.PenType.SHAPE_FILL;
        chooseShape();
    }

    public void chooseStroke(View v){
        penMode = Config.PenType.SHAPE_STROKE;
        chooseShape();
    }

    public void undoOrRedo(View v) {
        pw.dismiss();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pw = new PopupWindow(inflater.inflate(R.layout.undoredo_popup, null, false), Config.width, 200, true);
        pw.showAtLocation(this.findViewById(R.id.imageView), Gravity.BOTTOM, 0, 300);
    }

    public void fillOrStroke(View v) {
        pw.dismiss();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pw = new PopupWindow(inflater.inflate(R.layout.strokeorfill_popup, null, false), Config.width, 200, true);
        pw.showAtLocation(this.findViewById(R.id.imageView), Gravity.BOTTOM, 0, 300);
    }

    public void setColor(View view){
        i.setAlpha((float) 0.1);
        LinearLayout l = findViewById(R.id.l1);
        l.setVisibility(View.VISIBLE);
    }

    public void finishedSettingColor(View view){
        i.setAlpha((float) 1.0);
        LinearLayout l = findViewById(R.id.l1);
        l.setVisibility(View.GONE);
    }

    public void setEraser(View v){
        penMode = Config.PenType.ERASE;
        pw.dismiss();
    }

    public void clear(View view){
        pw.dismiss();
        b = Bitmap.createBitmap(i.getWidth(), i.getHeight(), Bitmap.Config.ARGB_8888);
        c = new Canvas(b);
        i.setImageBitmap(b);
        chooseBG();
    }

    public void chooseBG(){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pw = new PopupWindow(inflater.inflate(R.layout.colorpreset_popup, null, false), Config.width, 1200, true);
        pw.showAtLocation(this.findViewById(R.id.imageView), Gravity.CENTER, 0, -40);
    }

    public void setPen(View v){
        Toast t = Toast.makeText(i.getContext(), R.string.Thickness_toast, Toast.LENGTH_LONG);
        t.show();
        penMode = Config.PenType.DRAW;
        pw.dismiss();
    }

    public void setCircle(View v){
        shapeType = Config.Shape.CIRCLE;
        pw.dismiss();
    }

    public void setSquare(View v){
        shapeType = Config.Shape.SQUARE;
        pw.dismiss();
    }

    public void setLine(View v){
        shapeType = Config.Shape.LINE;
        pw.dismiss();
    }

    public void setText(View v) {
        shapeType = Config.Shape.TEXT;
        final LinearLayout l = findViewById(R.id.textinput);
        l.setVisibility(View.VISIBLE);
        EditText text = findViewById(R.id.editText1);
        textInput = text.getText().toString();
        Button b = findViewById(R.id.button35);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l.setVisibility(View.GONE);
            }
        });
    }

    public void setPolygon(View v){
        Toast t = Toast.makeText(i.getContext(), R.string.Sides_toast, Toast.LENGTH_LONG);
        t.show();
        shapeType = Config.Shape.POLYGON;
        pw.dismiss();
    }

    public void save(View v){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/req_images");
            myDir.mkdirs();
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String fname = "Image-" + n + ".jpg";
            File file = new File(myDir, fname);
            Log.i(TAG, "" + file);
            if (file.exists())
                file.delete();
            try {
                file.createNewFile();
            } catch (IOException e){}
            try {
                FileOutputStream out = new FileOutputStream(file);
                b.compress(Bitmap.CompressFormat.JPEG, 90, out);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    final Uri contentUri = Uri.fromFile(file);
                    scanIntent.setData(contentUri);
                    sendBroadcast(scanIntent);
                } else {
                    final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                    sendBroadcast(intent);
                }
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast t = Toast.makeText(i.getContext(), R.string.Saved_toast, Toast.LENGTH_LONG);
            t.show();
        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        pw.dismiss();
    }

    public void loadImage(View v) {

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap b3 = BitmapFactory.decodeFile(picturePath);
            int h = b3.getHeight();
            int w = b3.getWidth();
            float ratio = (float) h / (float) w;
            Log.d("19289182", Integer.toString(h) + " " + Integer.toString(w) + " " + Float.toString(ratio));

            float trueW = i.getWidth();
            float trueH = i.getHeight();
            if (w > h){
                trueW = (float) i.getWidth();
                trueH = trueW*ratio;
            } else if (h > w) {
                trueH = (float) i.getHeight();
                trueW = trueH/ratio;

            }
            c.drawBitmap(b3, null, new Rect((int) ((i.getWidth()/2) - (trueW/2)), (int) ((i.getHeight()/2) - (trueH/2)), (int) ((i.getWidth()/2) + (trueW/2)), (int) ((i.getHeight()/2) + (trueH/2))), p);
            bgColor = Color.WHITE;
            pw.dismiss();

        }
    }

    public void setBGToWhite(View v){
        c.drawColor(ContextCompat.getColor(this, R.color.white));
        pw.dismiss();
        bgColor = ContextCompat.getColor(this, R.color.white);
    }

    public void setBGToLGrey(View v){
        c.drawColor(ContextCompat.getColor(this, R.color.lgrey));
        pw.dismiss();
        bgColor = ContextCompat.getColor(this, R.color.lgrey);
    }

    public void setBGToGray(View v){
        c.drawColor(ContextCompat.getColor(this, R.color.gray));
        pw.dismiss();
        bgColor = ContextCompat.getColor(this, R.color.gray);
    }

    public void setBGToDGrey(View v){
        c.drawColor(ContextCompat.getColor(this, R.color.dgrey));
        pw.dismiss();
        bgColor = ContextCompat.getColor(this, R.color.dgrey);
    }

    public void setBGToBlack(View v){
        c.drawColor(ContextCompat.getColor(this, R.color.black));
        pw.dismiss();
        bgColor = ContextCompat.getColor(this, R.color.black);
    }

    public void setBGToDPurple(View v){
        c.drawColor(ContextCompat.getColor(this, R.color.dpurple));
        pw.dismiss();
        bgColor = ContextCompat.getColor(this, R.color.dpurple);
    }

    public void setBGToBlue(View v){
        c.drawColor(ContextCompat.getColor(this, R.color.blue));
        pw.dismiss();
        bgColor = ContextCompat.getColor(this, R.color.blue);
    }

    public void setBGToSBlue(View v){
        c.drawColor(ContextCompat.getColor(this, R.color.sblue));
        pw.dismiss();
        bgColor = ContextCompat.getColor(this, R.color.sblue);
    }

    public void setBGToLBlue(View v){
        c.drawColor(ContextCompat.getColor(this, R.color.lblue));
        pw.dismiss();
        bgColor = ContextCompat.getColor(this, R.color.lblue);
    }

    public void setBGToGreen(View v){
        c.drawColor(ContextCompat.getColor(this, R.color.green));
        pw.dismiss();
        bgColor = ContextCompat.getColor(this, R.color.green);
    }

    public void setBGToDGreen(View v){
        c.drawColor(ContextCompat.getColor(this, R.color.dgreen));
        pw.dismiss();
        bgColor = ContextCompat.getColor(this, R.color.dgreen);
    }

    public void setBGToDRed(View v){
        c.drawColor(ContextCompat.getColor(this, R.color.red));
        pw.dismiss();
        bgColor = ContextCompat.getColor(this, R.color.red);
    }

    public void setBGToRed(View v){
        c.drawColor(ContextCompat.getColor(this, R.color.dred));
        pw.dismiss();
        bgColor = ContextCompat.getColor(this, R.color.dred);
    }

    public void setBGToOrange(View v){
        c.drawColor(ContextCompat.getColor(this, R.color.orange));
        pw.dismiss();
        bgColor = ContextCompat.getColor(this, R.color.orange);
    }

    public void setBGToYellow(View v){
        c.drawColor(ContextCompat.getColor(this, R.color.yellow));
        pw.dismiss();
        bgColor = ContextCompat.getColor(this, R.color.yellow);
    }

    public void setBGToLYellow(View v){
        c.drawColor(ContextCompat.getColor(this, R.color.lyellow));
        pw.dismiss();
        bgColor = ContextCompat.getColor(this, R.color.lyellow);
    }

}
