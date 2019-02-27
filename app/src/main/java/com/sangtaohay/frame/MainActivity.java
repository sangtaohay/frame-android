package com.sangtaohay.frame;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout frame = findViewById(R.id.framePhoto);
        frame.setRotation(-20);

        RelativeLayout bg = findViewById(R.id.bg);
        Bitmap tempbg = BitmapFactory.decodeResource(getResources(),R.drawable.image); //Load a background.
        Bitmap final_Bitmap = Blurer.fastblur(tempbg, 0.5f, 10);
        bg.setBackgroundDrawable(new BitmapDrawable(final_Bitmap));

    }


}
