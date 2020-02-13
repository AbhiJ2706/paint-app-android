package com.example.paint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

public class Shape {

    public float x;
    public float y;
    public boolean init;
    public int color;
    private boolean[] type;

    public Shape(float x, float y, Config.Shape s, int color){
        this.x = x;
        this.y = y;
        this.color = color;
        this.init = true;
        this.type = new boolean[]{
                s == Config.Shape.CIRCLE,
                s == Config.Shape.SQUARE,
                s == Config.Shape.LINE,
                s == Config.Shape.POLYGON,
        };
    }

    void finish(float x, float y, Canvas c, Paint p, int sides, Config.PenType pen){
        p.setColor(this.color);
        if (this.type[0]){

            double r = Math.pow(Math.pow((y - this.y), 2) + Math.pow((x - this.x), 2), 0.5);
            if (pen.equals(Config.PenType.SHAPE_FILL)) {
                p.setStyle(Paint.Style.FILL);
            } else {
                p.setStyle(Paint.Style.STROKE);
            }
            c.drawCircle(this.x, this.y, (float) r, p);

        } else if (this.type[1]){

            double r = Math.pow(Math.pow((y - this.y), 2) + Math.pow((x - this.x), 2), 0.5);
            double d1 = Math.pow(Math.pow((y - (this.y - 100)), 2) + Math.pow((x - this.x), 2), 0.5);
            double rotationNum = Math.pow(100, 2) + Math.pow(r, 2) - Math.pow(d1, 2);
            double rotationDen = 2 * r * 100;
            float angle = (float) Math.acos(rotationNum/rotationDen);
            double size = Math.sqrt(2 * Math.pow(r, 2));
            double drawX = this.x - size/2;
            double drawY = this.y - size/2;
            angle = (float) Math.toDegrees(angle);
            angle = (x < this.x) ? (360 - angle) : angle;

            if (pen.equals(Config.PenType.SHAPE_FILL)) {
                p.setStyle(Paint.Style.FILL);
            } else {
                p.setStyle(Paint.Style.STROKE);
            }

            c.save();
            c.rotate((angle - 135), this.x, this.y);
            c.drawRect(new Rect((int) drawX, (int) drawY, (int) (drawX + size), (int) (drawY + size)), p);
            c.restore();

        } else if (this.type[2]){

            c.drawLine(this.x, this.y, x, y, p);

        } else if (this.type[3]){

            double r = Math.pow(Math.pow((y - this.y), 2) + Math.pow((x - this.x), 2), 0.5);
            double d1 = Math.pow(Math.pow((y - (this.y - 100)), 2) + Math.pow((x - this.x), 2), 0.5);

            double rotationNum = Math.pow(100, 2) + Math.pow(r, 2) - Math.pow(d1, 2);
            double rotationDen = 2 * r * 100;
            float angle = (float) Math.acos(rotationNum/rotationDen);
            angle = (float) Math.toDegrees(angle);
            angle = (x < this.x) ? (360 - angle) : angle;

            p.setAntiAlias(true);
            Path path = genPath(sides, (float) r);

            if (pen.equals(Config.PenType.SHAPE_FILL)) {
                p.setStyle(Paint.Style.FILL_AND_STROKE);
            } else {
                p.setStyle(Paint.Style.STROKE);
            }

            c.save();
            c.rotate((angle - 120), this.x, this.y);
            c.drawPath(path, p);
            c.restore();

        }
    }

    private Path genPath(int sides, float r){
        Path path = new Path();
        double angle = 2.0 * Math.PI / sides;
        path.moveTo((float)(this.x + (r * Math.cos(0.0))),(float)(this.y + (r * Math.sin(0.0))));
        for (int i = 0; i < sides; i++) {
            path.lineTo((float)(this.x + (r * Math.cos(angle * i))), (float)(this.y + (r * Math.sin(angle * i))));
        }
        path.close();
        return path;

    }
}
