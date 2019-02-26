package com.sangtaohay.memestudio.engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by john on 10/5/16.
 */

public class BitmapGenerator {
    private static final int IMAGE_WIDTH = 640;
    private static final int IMAGE_HEIGHT = 640;
    private static final float FONT_SIZE_MIN = 25;
    private static final float FONT_SIZE_MAX = 60;
    private static int LR_MARGIN = 20;
    private static int TB_MARGIN = 20;
    private static Rect r = new Rect();


    public static Bitmap generateImage(Context context, String text, String textColor, String bgColor) {
        Bitmap bm1;
        Bitmap newBitmap;
//        Resources resources = context.getResources();
//        float scale = resources.getDisplayMetrics().density;
        int bitmatHeight = 0;

        bm1 = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Config.ARGB_8888);
//        bm1.eraseColor(Color.parseColor("#0084ff"));
        bm1.eraseColor(Color.parseColor(bgColor));

        Config config = bm1.getConfig();
        if (config == null) {
            config = Config.ARGB_8888;
        }

//        newBitmap = Bitmap.createBitmap(bm1.getWidth(), bm1.getHeight(), config);
        Canvas newCanvas = new Canvas(bm1);

        newCanvas.drawBitmap(bm1, 0, 0, null);

        if (text != null) {


            List<String> listRow = asList(text.split("\n"));
            int height = 0;
            for (int i = 0; i < listRow.size(); i++) {
                Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintText.setColor(Color.parseColor(textColor));
                float testTextSize = 55f;
                paintText.setTextSize(testTextSize);
                paintText.setStyle(Paint.Style.FILL);
                String row = listRow.get(i);
                Rect rectText = new Rect();
                paintText.getTextBounds(row, 0, row.length(), rectText);
                int desiredWidth = IMAGE_WIDTH;
                float rectWith = rectText.width();
                float desiredTextSize = testTextSize * (desiredWidth - 2 * LR_MARGIN) / rectWith;

                paintText.setTextSize(desiredTextSize);
                paintText.getTextBounds(row, 0, row.length(), rectText);
                rectWith = rectText.width();
                height += rectText.height() + TB_MARGIN;
                bitmatHeight = height + rectText.height() / 2;
                float lineSpace = height;
                drawRow(newCanvas, row,
                        (desiredWidth / 2 - rectWith / 2) - LR_MARGIN / 2, lineSpace, paintText);
            }

//            Toast.makeText(context, "drawText: " + text, Toast.LENGTH_LONG).show();
            Log.v(" final heigth", "" + height);

        } else {
//            Toast.makeText(context, "caption empty!", Toast.LENGTH_LONG).show();
        }
        int cropHeight = Math.min(bitmatHeight, bm1.getHeight());
        Bitmap crop = Bitmap.createBitmap(bm1, 0, 0, bm1.getWidth(), cropHeight);
        return crop;
    }

    private static void drawRow(Canvas newCanvas, String text, float x, float y, Paint paint) {
        newCanvas.drawText(text, x, y, paint);
    }
    public static void generateMeme(Context context, Bitmap source, int index, String textTop, String textBottom) {
        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        String textColor = "#ffffff";
        paintText.setColor(Color.parseColor(textColor));
        float testTextSize = 32f;
        paintText.setTextSize(testTextSize);
        paintText.setStyle(Paint.Style.FILL);



        Bitmap bitmap = Bitmap.createBitmap(source,0,0, source.getWidth(), source.getHeight());
        Bitmap textLayer = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas newCanvas = new Canvas(textLayer);


        newCanvas.drawText(textBottom, 50, 300, paintText);


        File root = context.getExternalFilesDir(null);
        File myDir = new File(root + "/");
        File file = new File(myDir, "bitmap"+index+".jpg");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        textLayer.compress(Bitmap.CompressFormat.JPEG, 90, out);
    }
    public static Bitmap generateMeme2(Context context, Bitmap source, String textTop, String textBottom, String textBottomLeft, String textBottomRight, boolean pollMode) {
        int IMAGE_HEIGHT = Math.min(640, source.getHeight());
        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        String textColor = "#ffffff";
        paintText.setColor(Color.parseColor(textColor));
        float testTextSize = 52f;
        paintText.setTextSize(testTextSize);
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

        newCanvas.getClipBounds(r);
        int cHeight = r.height();
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
            String textColorLeft = "#ff669900";
            paintText.setColor(Color.parseColor(textColorLeft));
            float xBottomLeft = cWidth / 4f - r.width() / 2f - r.left;
            newCanvas.drawText(textBottomLeft.toUpperCase(), xBottomLeft, IMAGE_HEIGHT - 5 - heightDiff, paintText);

            String textColorRight = "#ffff4444";
            paintText.setColor(Color.parseColor(textColorRight));
            paintText.getTextBounds(textBottomRight, 0, textBottomRight.length(), r);
            float xBottomRight = cWidth / 4f * 3f - r.width() / 2f - r.left;
            newCanvas.drawText(textBottomRight.toUpperCase(), xBottomRight, IMAGE_HEIGHT - 5 - heightDiff, paintText);
        }
        return background;
    }
}
