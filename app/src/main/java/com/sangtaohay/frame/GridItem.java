package com.sangtaohay.frame;

public class GridItem {
    private int memeId;
    private String image;
    private String thumb;
    private String title;
    private String textTop;
    private String textBottom;
    private int imageWidth;
    private int imageHeight;
    private String textBottomLeft;
    private String textBottomRight;

    public GridItem() {
        super();
    }

    public int getMemeId() {
        return memeId;
    }

    public void setMemeId(int memeId) {
        this.memeId = memeId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextBottom() {
        return textBottom;
    }

    public String getTextTop() {
        return textTop;
    }

    public void setTextBottom(String textBottom) {
        this.textBottom = textBottom;
    }

    public void setTextTop(String textTop) {
        this.textTop = textTop;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getTextBottomLeft() {
        return textBottomLeft;
    }

    public void setTextBottomLeft(String textBottomLeft) {
        this.textBottomLeft = textBottomLeft;
    }

    public String getTextBottomRight() {
        return textBottomRight;
    }

    public void setTextBottomRight(String textBottomRight) {
        this.textBottomRight = textBottomRight;
    }
}
