package com.sangtaohay.memestudio.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class TextRotateAnimationImpl {
    private static final int IMAGE_WIDTH = 640;
    static String textColorLeft = "#ff669900";
    static String textColorRight = "#ffff4444";


    private static Rect r = new Rect();

    private static final int CANVAS_HEIGHT= 64;

    public static Bitmap generateMeme2(Context context, Bitmap source, String textTop, String textBottom, String textBottomLeft, String textBottomRight, boolean pollMode, int currentFrame, int totalFrame) {

        int IMAGE_HEIGHT = source.getHeight();
        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(getFontColor(currentFrame));
        paintText.setTextSize(getFontSize(currentFrame));
        paintText.setStyle(Paint.Style.FILL);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/impact.ttf");
        paintText.setTypeface(font);
        paintText.setShadowLayer(2.0f, 2.0f, 2.0f, Color.BLACK);

        int heightDiff = (IMAGE_HEIGHT-source.getHeight())/2;
        if(heightDiff< 0) heightDiff = 0;

        Bitmap background = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas newCanvas = new Canvas(background);

        Rect src = new Rect(0,0,source.getWidth()-1, source.getHeight()-1);
        Rect dest = new Rect(0,0,IMAGE_WIDTH-1, IMAGE_HEIGHT * IMAGE_WIDTH/source.getWidth()-1);

        newCanvas.drawBitmap(source,src, dest, new Paint(Paint.ANTI_ALIAS_FLAG));
        newCanvas.rotate(getAngle(currentFrame), IMAGE_WIDTH/2, IMAGE_HEIGHT/2);

        newCanvas.getClipBounds(r);
        int cWidth = r.width();
        paintText.setTextAlign(Paint.Align.LEFT);
        paintText.getTextBounds(textTop, 0, textTop.length(), r);
        float xTop = cWidth / 2f - r.width() / 2f - r.left;

        newCanvas.drawText(textTop.toUpperCase(), xTop, 50 + heightDiff, paintText);

        if(!pollMode && textBottom != null && !textBottom.isEmpty()) {
            paintText.getTextBounds(textBottom, 0, textBottom.length(), r);
            float xBottom = cWidth / 2f - r.width() / 2f - r.left;

            newCanvas.drawText(textBottom.toUpperCase(), xBottom, IMAGE_HEIGHT - 5 - heightDiff, paintText);
        }else if(pollMode && textBottomLeft != null && !textBottomLeft.isEmpty() && textBottomRight != null && !textBottomRight.isEmpty()) {
            paintText.getTextBounds(textBottomLeft, 0, textBottomLeft.length(), r);
            paintText.setColor(Color.parseColor(textColorLeft));
            float xBottomLeft = cWidth / 4f - r.width() / 2f - r.left;
            newCanvas.drawText(textBottomLeft.toUpperCase(), xBottomLeft, IMAGE_HEIGHT - 5 - heightDiff, paintText);

            paintText.setColor(Color.parseColor(textColorRight));
            paintText.getTextBounds(textBottomRight, 0, textBottomRight.length(), r);
            float xBottomRight = cWidth / 4f * 3f - r.width() / 2f - r.left;
            newCanvas.drawText(textBottomRight.toUpperCase(), xBottomRight, IMAGE_HEIGHT - 5 - heightDiff, paintText);
        }
        return background;
    }
    public static Bitmap generateAnimatedTextTopFrame(Context context, String textTop, int currentFrame) {
        int IMAGE_HEIGHT = Math.min(640, CANVAS_HEIGHT);
        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(getFontColor(currentFrame));
        paintText.setTextSize(getFontSize(currentFrame));
        paintText.setStyle(Paint.Style.FILL);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/impact.ttf");
        paintText.setTypeface(font);
        paintText.setShadowLayer(1.0f, 1.0f, 1.0f, Color.BLACK);

        int heightDiff = (IMAGE_HEIGHT-CANVAS_HEIGHT)/2;
        if(heightDiff< 0) heightDiff = 0;

        Bitmap background = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas newCanvas = new Canvas(background);
        newCanvas.rotate(getAngle(currentFrame), IMAGE_WIDTH/2, IMAGE_WIDTH/2);

        newCanvas.getClipBounds(r);
        int cWidth = r.width();
        paintText.setTextAlign(Paint.Align.LEFT);
        paintText.getTextBounds(textTop, 0, textTop.length(), r);
        float xTop = cWidth / 2f - r.width() / 2f - r.left;

        newCanvas.drawText(textTop.toUpperCase(), xTop, 50 + heightDiff, paintText);
        return background;
    }
    public static Bitmap generateAnimatedTextBottomFrame(Context context, String textBottom, boolean pollMode, String textBottomLeft, String textBottomRight, int currentFrame) {
        int IMAGE_HEIGHT = Math.min(640, CANVAS_HEIGHT);
        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(getFontColor(currentFrame));
        paintText.setTextSize(getFontSize(currentFrame));
        paintText.setStyle(Paint.Style.FILL);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/impact.ttf");
        paintText.setTypeface(font);
        paintText.setShadowLayer(1.0f, 1.0f, 1.0f, Color.BLACK);

        int heightDiff = (IMAGE_HEIGHT-CANVAS_HEIGHT)/2;
        if(heightDiff< 0) heightDiff = 0;

        Bitmap background = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas newCanvas = new Canvas(background);
        newCanvas.rotate(getAngle(currentFrame), IMAGE_WIDTH/2, IMAGE_WIDTH/2);


        newCanvas.getClipBounds(r);
        int cWidth = r.width();
        paintText.setTextAlign(Paint.Align.LEFT);

        if(!pollMode && textBottom != null && !textBottom.isEmpty()) {
            paintText.getTextBounds(textBottom, 0, textBottom.length(), r);
            float xBottom = cWidth / 2f - r.width() / 2f - r.left;

            newCanvas.drawText(textBottom.toUpperCase(), xBottom, IMAGE_HEIGHT - 5 - heightDiff, paintText);
        }else if(pollMode && textBottomLeft != null && !textBottomLeft.isEmpty() && textBottomRight != null && !textBottomRight.isEmpty()) {
            paintText.getTextBounds(textBottomLeft, 0, textBottomLeft.length(), r);
            paintText.setColor(Color.parseColor(textColorLeft));
            float xBottomLeft = cWidth / 4f - r.width() / 2f - r.left;
            newCanvas.drawText(textBottomLeft.toUpperCase(), xBottomLeft, IMAGE_HEIGHT - 5 - heightDiff, paintText);

            paintText.setColor(Color.parseColor(textColorRight));
            paintText.getTextBounds(textBottomRight, 0, textBottomRight.length(), r);
            float xBottomRight = cWidth / 4f * 3f - r.width() / 2f - r.left;
            newCanvas.drawText(textBottomRight.toUpperCase(), xBottomRight, IMAGE_HEIGHT - 5 - heightDiff, paintText);
        }
        return background;
    }
    private static float getFontSize(int currentFrame){
        float fontSize = 46f;
        return  fontSize;
    }
    private static int getFontColor(int currentFrame){
        String textColor = "#ffffff";
        return  Color.parseColor(textColor);
    }
    private static int getAngle(int currentFrame){
        int fontSize = 0;
        switch (currentFrame){
            case 0: fontSize = 3;break;
            case 1: fontSize = 6;break;
            case 2: fontSize = 3;break;
            case 3: fontSize = 0;break;
            case 4: fontSize = -3;break;
            case 5: fontSize = -6;break;
            case 6: fontSize = -3;break;
            case 7: fontSize = 0;break;
            case 8: fontSize = 3;break;
            case 9: fontSize = 6;break;
            case 10: fontSize = 3;break;
            case 11: fontSize = 2;break;
            case 12: fontSize = 1;break;
            case 13: fontSize = 0;break;
            case 14: fontSize = -3;break;
            case 15: fontSize = -5;break;
            case 16: fontSize = -4;break;
            case 17: fontSize = -3;break;
            case 18: fontSize = -1;break;
            case 19: fontSize = 2;break;
            case 20: fontSize = 5;break;
            case 21: fontSize = 2;break;
            case 22: fontSize = 1;break;
            case 23: fontSize = 0;break;
        }
        return  fontSize;
    }
}
