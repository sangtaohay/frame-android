package com.sangtaohay.memestudio.dto;

import android.graphics.Bitmap;

public class GifFrame {
    private Bitmap bitmap;
    private int delay;
    public GifFrame(Bitmap bitmap, int delay){
        this.bitmap = bitmap;
        this.delay = delay;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
