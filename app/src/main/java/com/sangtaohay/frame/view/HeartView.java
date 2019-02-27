package com.sangtaohay.frame.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.sangtaohay.frame.R;

public class HeartView extends View {

    private static int WIDTH = 1000;
    private static int HEIGHT = 600;

    private Path path;
    private Paint paint;

    private int top;
    private int left;


 public HeartView(Context context) {
        super(context);
        init(context, null, 0);
    }
    public HeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }
    public HeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }
    private void init(Context context, AttributeSet attrs, int defStyle) {
        path = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        top=10;
        left=10;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Fill the canvas with background color
        canvas.drawColor(Color.TRANSPARENT);
        paint.setShader(null);

        // Defining of  the heart path starts
        path.moveTo(left+WIDTH/2, top+HEIGHT/4); // Starting point
        // Create a cubic Bezier cubic left path
        path.cubicTo(left+WIDTH/5,top,
                left+WIDTH/4,top+4*HEIGHT/5,
                left+WIDTH/2, top+HEIGHT);
        // This is right Bezier cubic path
        path.cubicTo(left+3*WIDTH/4,top+4*HEIGHT/5,
                left+4*WIDTH/5,top,
                left+WIDTH/2, top+HEIGHT/4);

//        paint.setColor(Color.RED); // Set with heart color
        Bitmap fillBMP = BitmapFactory.decodeResource(getResources(), R.drawable.image); //Load a background.
        Matrix matrix = new Matrix();
        matrix.postScale(1f, 1f);
        Bitmap croppedBitmap = Bitmap.createBitmap(fillBMP, fillBMP.getWidth()/2 - 600, fillBMP.getHeight()/2 - 600,700, 700, matrix, true);
        Shader shader = new BitmapShader(croppedBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        paint.setShader(shader);
        paint.setStyle(Paint.Style.FILL); // Fill with heart color
        canvas.drawPath(path, paint); // Actual drawing happens here

        // Draw Blue Boundary
        paint.setShader(null);
        paint.setColor(Color.RED); // Change the boundary color
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);

    }
}
