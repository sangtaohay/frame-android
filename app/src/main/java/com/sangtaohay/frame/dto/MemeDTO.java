package com.sangtaohay.memestudio.dto;

import android.content.Context;

public class MemeDTO {
    public Integer id;
    public String name;
    public String imageName;
    public String textTop;
    public String textBottom;
    public String textBottomLeft;
    public String textBottomRight;
    public int backgroundColor;
    public int createdAt;
    public int position;
    public int imageWidth;
    public int imageHeight;
    public String thumb;
    public boolean isFav;

    public int getImageResourceId(Context context) {
        return context.getResources().getIdentifier(this.imageName, "drawable", context.getPackageName());
    }
}
