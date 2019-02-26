package com.sangtaohay.frame;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.sangtaohay.animationbrowser.AnimationBrowser;
import com.sangtaohay.memestudio.animation.TextColorAnimationImpl;
import com.sangtaohay.memestudio.animation.TextRotateAnimationImpl;
import com.sangtaohay.memestudio.animation.TextSlideAnimationImpl;
import com.sangtaohay.memestudio.animation.TextTypingAnimationImpl;
import com.sangtaohay.memestudio.animation.TextZoomAnimationImpl;
import com.sangtaohay.memestudio.dao.MemeDAO;
import com.sangtaohay.memestudio.db.MemeDbHelper;
import com.sangtaohay.memestudio.dto.GifFrame;
import com.sangtaohay.memestudio.engine.BitmapGenerator;
import com.sangtaohay.memestudio.engine.GifGenerator;
import com.sangtaohay.memestudio.engine.JpgGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DetailsActivity extends AppCompatActivity {
    private static final int ANIM_DURATION = 300;
    private TextView titleTextView;
    private ImageView imageView;

    private int mLeftDelta;
    private int mTopDelta;
    private float mWidthScale;
    private float mHeightScale;

    private FrameLayout frameLayout;
    private ColorDrawable colorDrawable;
    private TextView tvTop;
    private TextView tvBottom;
    private EditText edTop;
    private EditText edBottom;

    private TextView tvBottomLeft, tvBottomRight;
    private EditText edBottomLeft, edBottomRight;

    private RelativeLayout relativeLayout;

    private int thumbnailTop;
    private int thumbnailLeft;
    private int thumbnailWidth;
    private int thumbnailHeight;

    private int viewId;
    private String imageName;

    private MemeDbHelper mHelper;

    private int memeId = -1;

    private int imageWidth;
    private int imageHeight;
    private Button btnShare;
    private FrameLayout progressBar;

    private LinearLayout pollHolder;
    private LinearLayout pollHolderEd;
    private LinearLayout pollHolderCanvas;
    private boolean pollMode = false;
    private Button btnPollMode;
    private Button btnAnimate;
    private ImageView canvasTop, canvasBottom, canvasBottomLeft, canvasBottomRight;
    private Boolean gifOutput = false;
    private Boolean enableAnimate = false;
    private AnimationBrowser animationBrowser;
    private Boolean animationBrowserOpen = false;
    private AsyncTask<String, String, Integer> animationTask;
    private int animationCode = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Setting details screen layout
        setContentView(R.layout.activity_details_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //retrieves the thumbnail data
        Bundle bundle = getIntent().getExtras();
        thumbnailTop = bundle.getInt("top");
        thumbnailLeft = bundle.getInt("left");
        thumbnailWidth = bundle.getInt("width");
        thumbnailHeight = bundle.getInt("height");

        memeId = bundle.getInt("_id");
        String title = bundle.getString("title");
        String image = bundle.getString("image");
        final String textTop = bundle.getString("textTop");
        String textBottom = bundle.getString("textBottom");

        String textBottomLeft = bundle.getString("textBottomLeft");
        String textBottomRight = bundle.getString("textBottomRight");
        enableAnimate = bundle.getBoolean("enableAnimate",false);

        imageName = image;

        //initialize and set the image description
        titleTextView = findViewById(R.id.title);
        titleTextView.setText(Html.fromHtml(title));

        //Set image url
        imageView = findViewById(R.id.grid_item_image);
        Glide.with(this).load(image).into(imageView);
        imageView.buildDrawingCache();

        //Set the background color to black
        frameLayout = findViewById(R.id.main_background);
        colorDrawable = new ColorDrawable(Color.BLACK);
        frameLayout.setBackground(colorDrawable);

        // Only run the animation if we're coming from the parent activity, not if
        // we're recreated automatically by the window manager (e.g., device rotation)
        if (savedInstanceState == null) {
            ViewTreeObserver observer = imageView.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {
                    imageView.getViewTreeObserver().removeOnPreDrawListener(this);

                    // Figure out where the thumbnail and full size versions are, relative
                    // to the screen and each other
                    int[] screenLocation = new int[2];
                    imageView.getLocationOnScreen(screenLocation);
                    mLeftDelta = thumbnailLeft - screenLocation[0];
                    mTopDelta = thumbnailTop - screenLocation[1];

                    // Scale factors to make the large version the same size as the thumbnail
                    mWidthScale = (float) thumbnailWidth / imageView.getWidth();
                    mHeightScale = (float) thumbnailHeight / imageView.getHeight();

                    enterAnimation(imageView);

                    return true;
                }
            });
        }

        tvTop = findViewById(R.id.tvTop);
        tvBottom = findViewById(R.id.tvBottom);
        tvBottomLeft = findViewById(R.id.tvBottomLeft); tvBottomRight = findViewById(R.id.tvBottomRight);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/impact.ttf");
        tvTop.setTypeface(font);
        tvBottom.setTypeface(font);
        tvBottomLeft.setTypeface(font);tvBottomRight.setTypeface(font);
        tvTop.setOnClickListener(editMode);
        tvBottom.setOnClickListener(editMode);
        tvTop.setText(textTop);
        tvBottom.setText(textBottom);

        tvBottomLeft.setOnClickListener(editMode); tvBottomRight.setOnClickListener(editMode);

        tvBottomLeft.setText(textBottomLeft);tvBottomRight.setText(textBottomRight);

        edTop = findViewById(R.id.etTop);
        edTop.setOnFocusChangeListener(focusMode);
        edTop.setTypeface(font);


        edBottom = findViewById(R.id.etBottom);
        edBottom.setOnFocusChangeListener(focusMode);
        edBottom.setTypeface(font);

        edBottomLeft = findViewById(R.id.etBottomLeft);
        edBottomLeft.setOnFocusChangeListener(focusMode);
        edBottomLeft.setTypeface(font);
        edBottomRight = findViewById(R.id.etBottomRight);
        edBottomRight.setOnFocusChangeListener(focusMode);
        edBottomRight.setTypeface(font);

        relativeLayout = findViewById(R.id.relativeLayout);
        relativeLayout.setOnTouchListener(touchListener);

        mHelper = new MemeDbHelper(getApplicationContext());

        btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishEditing();
                animationBrowser.setVisibility(View.GONE);
                animationBrowserOpen = false;
                progressBar.setVisibility(View.VISIBLE);
                btnShare.setVisibility(View.INVISIBLE);
                invalidateScreen();
                new DetailsActivity.AsyncHttpTask().execute();
            }
        });

        imageWidth = bundle.getInt("imageWidth");
        imageHeight = bundle.getInt("imageHeight");
        progressBar = findViewById(R.id.frameLoading);

        pollHolder = findViewById(R.id.poll_holder);
        pollHolderEd = findViewById(R.id.poll_ed_holder);
        pollHolderCanvas = findViewById(R.id.poll_canvas_holder);

        btnPollMode = findViewById(R.id.btnSwitchMode);
        btnPollMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishEditing();
                if(!pollMode){
                    tvBottom.setVisibility(View.INVISIBLE);
                    pollHolder.setVisibility(View.VISIBLE);
                    btnPollMode.setBackgroundResource(android.R.drawable.ic_menu_gallery);
                    pollMode = true;
                }else{
                    tvBottom.setVisibility(View.VISIBLE);
                    pollHolder.setVisibility(View.INVISIBLE);
                    btnPollMode.setBackgroundResource(android.R.drawable.ic_menu_help);
                    pollMode = false;
                }
            }
        });

        canvasTop = findViewById(R.id.canvasTop);
        canvasBottom = findViewById(R.id.canvasBottom);
        canvasBottomLeft = findViewById(R.id.canvasBottomLeft);
        canvasBottomRight = findViewById(R.id.canvasBottomRight);

        animationBrowser = findViewById(R.id.animation_browser);
        animationBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationCode  = animationBrowser.getAnimationCode();
                finishEditing();
                if(0 != animationCode && animationCode < 6) {
                    if(animationTask != null) {
                        animationTask.cancel(true);
                    }
                    gifOutput = true;
                    animationTask = new DetailsActivity.AsyncAnimationTask().execute(String.valueOf(animationCode));
                }
            }
        });

        btnAnimate = findViewById(R.id.btnAnimate);
        btnAnimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishEditing();
                if(!animationBrowserOpen) {
                    animationBrowser.setVisibility(View.VISIBLE);
                    animationBrowserOpen=true;
                    animationBrowser.setAnimationCode(animationCode);
                }else{
                    animationBrowser.setVisibility(View.GONE);
                    animationBrowserOpen=false;
                }
            }
        });
        if(enableAnimate){
            btnAnimate.setVisibility(View.VISIBLE);
        }else{
            gifOutput = true;
        }
    }
    private String getImgCachePath(String url) {
        FutureTarget<File> futureTarget = Glide.with(getBaseContext()).load(url).downloadOnly(100, 100);
        try {
            File file = futureTarget.get();
            String path = file.getAbsolutePath();
            return path;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * The enter animation scales the picture in from its previous thumbnail
     * size/location.
     */
    public void enterAnimation(View imageView) {

        // Set starting values for properties we're going to animate. These
        // values scale and position the full size version down to the thumbnail
        // size/location, from which we'll animate it back up
        imageView.setPivotX(0);
        imageView.setPivotY(0);
        imageView.setScaleX(mWidthScale);
        imageView.setScaleY(mHeightScale);
        imageView.setTranslationX(mLeftDelta);
        imageView.setTranslationY(mTopDelta);

        // interpolator where the rate of change starts out quickly and then decelerates.
        TimeInterpolator sDecelerator = new DecelerateInterpolator();

        // Animate scale and translation to go from thumbnail to full size
        imageView.animate().setDuration(ANIM_DURATION).scaleX(1).scaleY(1).
                translationX(0).translationY(0).setInterpolator(sDecelerator);

        // Fade in the black background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0, 255);
        bgAnim.setDuration(ANIM_DURATION);
        bgAnim.start();

    }

    /**
     * The exit animation is basically a reverse of the enter animation.
     * This Animate image back to thumbnail size/location as relieved from bundle.
     *
     * @param endAction This action gets run after the animation completes (this is
     *                  when we actually switch activities)
     */
    public void exitAnimation(final Runnable endAction, View imageView) {

        TimeInterpolator sInterpolator = new AccelerateInterpolator();
        imageView.animate().setDuration(ANIM_DURATION).scaleX(mWidthScale).scaleY(mHeightScale).
                translationX(mLeftDelta).translationY(mTopDelta)
                .setInterpolator(sInterpolator).withEndAction(endAction);

        // Fade out background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0);
        bgAnim.setDuration(ANIM_DURATION);
        bgAnim.start();
    }

    @Override
    public void onBackPressed() {
        finishEditing();
        String top = tvTop.getText().toString();
        String bottom = tvBottom.getText().toString();
        if(top == null || top.isEmpty()){
            top = edTop.getText().toString();
        }
        if(bottom == null || bottom.isEmpty()){
            bottom = edBottom.getText().toString();
        }
        String bottomLeft = tvBottomLeft.getText().toString();
        String bottomRight = tvBottomRight.getText().toString();
        if(bottomLeft == null || bottomRight.isEmpty()){
            bottomLeft = edBottomLeft.getText().toString();
        }
        if(bottomRight == null || bottomRight.isEmpty()){
            bottomRight = edBottomRight.getText().toString();
        }
        MemeDAO.updateItem(mHelper,memeId, top, bottom,bottomLeft, bottomRight, 0);
        exitAnimation(new Runnable() {
            public void run() {
                finish();
            }
        }, imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;
        int ratio = widthPixels * 1000/imageWidth;
        int imageHeightPixels = imageHeight * ratio/1000;
        int height = Math.max(widthPixels, imageHeightPixels);
        RelativeLayout.LayoutParams frameParams = new RelativeLayout.LayoutParams(
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        height));
        frameLayout.setLayoutParams(frameParams);
        frameLayout.requestLayout();

        int density = Math.round(displayMetrics.density);

        Log.v("height" , String.valueOf(height));
        Log.v("imageHeightPixels" , String.valueOf(imageHeightPixels));
        Log.v("density" , String.valueOf(displayMetrics.density));
        Log.v("densityDpi" , String.valueOf(displayMetrics.densityDpi));


        // CODE FOR ADD MARGINS
        RelativeLayout.LayoutParams linearParams1 = new RelativeLayout.LayoutParams(
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT));
        linearParams1.setMargins(0, (height-imageHeightPixels)/2, 0, 0);
        tvTop.setLayoutParams(linearParams1);
        tvTop.requestLayout();

        RelativeLayout.LayoutParams linearParams2 = new RelativeLayout.LayoutParams(
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT));
        linearParams2.setMargins(0, height - (height-imageHeightPixels)/2 - 40* density, 0, 0);
        tvBottom.setLayoutParams(linearParams2);
        tvBottom.requestLayout();

        RelativeLayout.LayoutParams linearParams3 = new RelativeLayout.LayoutParams(
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT));
        linearParams3.setMargins(0, height - (height-imageHeightPixels)/2 - 36* density, 0, 0);
        pollHolder.setLayoutParams(linearParams3);
        pollHolder.requestLayout();

        canvasTop.setLayoutParams(linearParams1);
        canvasTop.requestLayout();

        canvasBottom.setLayoutParams(linearParams2);
        canvasBottom.requestLayout();
        pollHolderCanvas.setLayoutParams(linearParams3);
        pollHolderCanvas.requestLayout();
    }

    View.OnClickListener editMode = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finishEditing();
            viewId = view.getId();
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            switch (viewId){
                case R.id.tvTop:
                    Log.v("top", "top");
                    edTop.setVisibility(View.VISIBLE);
                    tvTop.setVisibility(View.INVISIBLE);
                    edTop.setText(tvTop.getText());
                    imm.showSoftInput(edTop, 0);
                    edTop.setSelectAllOnFocus(true);
                    int topPosition = edTop.length();
                    Editable etextTop = edTop.getText();
                    Selection.setSelection(etextTop, topPosition);
                    break;
                case R.id.tvBottom:
                    Log.v("bottom", "bottom");
                    edBottom.setVisibility(View.VISIBLE);
                    tvBottom.setVisibility(View.INVISIBLE);
                    edBottom.setText(tvBottom.getText());
                    imm.showSoftInput(edBottom, 0);
                    edBottom.setSelectAllOnFocus(true);
                    int bottomPosition = edBottom.length();
                    Editable etextBottom = edBottom.getText();
                    Selection.setSelection(etextBottom, bottomPosition);
                    break;
                case R.id.tvBottomLeft:
                    pollHolderEd.setVisibility(View.VISIBLE);edBottomLeft.setVisibility(View.VISIBLE);
                    pollHolder.setVisibility(View.INVISIBLE);
                    edBottomLeft.setText(tvBottomLeft.getText());
                    imm.showSoftInput(edBottomLeft, 0);
                    edBottomLeft.setSelectAllOnFocus(true);
                    int bottomLeftPosition = edBottomLeft.length();
                    Editable etextBottomLeft = edBottomLeft.getText();
                    Selection.setSelection(etextBottomLeft, bottomLeftPosition);
                    break;
                case R.id.tvBottomRight:
                    pollHolderEd.setVisibility(View.VISIBLE);edBottomRight.setVisibility(View.VISIBLE);
                    pollHolder.setVisibility(View.INVISIBLE);
                    edBottomRight.setText(tvBottomRight.getText());
                    imm.showSoftInput(edBottomRight, 0);
                    edBottomRight.setSelectAllOnFocus(true);
                    int bottomRightPosition = edBottomRight.length();
                    Editable etextBottomRight = edBottomRight.getText();
                    Selection.setSelection(etextBottomRight, bottomRightPosition);
                    break;
            }

        }
    };
    View.OnFocusChangeListener focusMode = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(!hasFocus){
                switch (viewId){
                    case R.id.tvTop:
                        tvTop.setText(edTop.getText());
                        tvTop.setVisibility(View.VISIBLE);
                        edTop.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.tvBottom:
                        tvBottom.setText(edBottom.getText());
                        tvBottom.setVisibility(View.VISIBLE);
                        edBottom.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.tvBottomLeft:
                        tvBottomLeft.setText(edBottomLeft.getText());
                        tvBottomLeft.setVisibility(View.VISIBLE);
                        edBottomLeft.setVisibility(View.INVISIBLE);
                        pollHolder.setVisibility(View.VISIBLE);
                        break;
                    case R.id.tvBottomRight:
                        tvBottomRight.setText(edBottomRight.getText());
                        tvBottomRight.setVisibility(View.VISIBLE);
                        edBottomRight.setVisibility(View.INVISIBLE);
                        pollHolder.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    };
    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            Rect outRect = new Rect();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (edTop.isFocused()) {
                    edTop.getGlobalVisibleRect(outRect);
                    if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                        edTop.clearFocus();
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                if (edBottom.isFocused()) {
                    edBottom.getGlobalVisibleRect(outRect);
                    if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                        edBottom.clearFocus();
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                if (edBottomLeft.isFocused()) {
                    edBottomLeft.getGlobalVisibleRect(outRect);
                    if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                        edBottomLeft.clearFocus();
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                if (edBottomRight.isFocused()) {
                    edBottomRight.getGlobalVisibleRect(outRect);
                    if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                        edBottomRight.clearFocus();
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
            return false;
        }
    };

    private void finishEditing(){
        if (edTop.isFocused()) {
            edTop.clearFocus();
        }
        if (edBottom.isFocused()) {
            edBottom.clearFocus();
        }
        if (edBottomLeft.isFocused()) {
            edBottomLeft.clearFocus();
        }
        if (edBottomRight.isFocused()) {
            edBottomRight.clearFocus();
        }
    }
    //Downloading data asynchronously
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
//                BitmapGenerator.generateMeme(getApplicationContext(), ((BitmapDrawable)imageView.getDrawable()).getBitmap());
            File file = new File(getImgCachePath(imageName));
            try {
                String textTop = String.valueOf(tvTop.getText());
                String textBottom = String.valueOf(tvBottom.getText());
                if(pollMode){
                    textBottom = "";
                }
                String textBottomLeft = String.valueOf(tvBottomLeft.getText());
                String textBottomRight = String.valueOf(tvBottomRight.getText());

                List<GifFrame> bitmaps = GifGenerator.extractFramesWithDelay(file);
                List<Bitmap> output = new ArrayList<>();
                if(!gifOutput) {
                    for (int index = 0; index < bitmaps.size(); index++) {
                        Bitmap out = BitmapGenerator.generateMeme2(getApplicationContext(), bitmaps.get(index).getBitmap(), textTop, textBottom, textBottomLeft, textBottomRight, pollMode);
                        output.add(out);
                    }
                    JpgGenerator.combineGifFrames(getApplicationContext(), output);
                }
                else if (!enableAnimate){ //animate background only, text still solid
                    for(GifFrame bitmap: bitmaps) {
                        Bitmap out = BitmapGenerator.generateMeme2(getApplicationContext(), bitmap.getBitmap(), textTop, textBottom, textBottomLeft, textBottomRight, pollMode);
                        output.add(out);
                    }
                    GifGenerator.combineGifFramesWithDelay(getApplicationContext(), output, bitmaps.get(0).getDelay());
                }
                else{
                    //count how many words in text
                    int totalFrame = 24;
                    for (int index = 0; index < totalFrame; index++) {
                        Bitmap out;
                        switch(animationCode){
                            case 1:
                                out = TextZoomAnimationImpl.generateMeme2(getApplicationContext(), bitmaps.get(0).getBitmap(), textTop, textBottom, textBottomLeft, textBottomRight, pollMode, index, totalFrame);
                                break;
                            case 2:
                                out = TextColorAnimationImpl.generateMeme2(getApplicationContext(), bitmaps.get(0).getBitmap(), textTop, textBottom, textBottomLeft, textBottomRight, pollMode, index, totalFrame);
                                break;
                            case 3:
                                out = TextRotateAnimationImpl.generateMeme2(getApplicationContext(), bitmaps.get(0).getBitmap(), textTop, textBottom, textBottomLeft, textBottomRight, pollMode, index, totalFrame);
                                break;
                            case 4:
                                out = TextSlideAnimationImpl.generateMeme2(getApplicationContext(), bitmaps.get(0).getBitmap(), textTop, textBottom, textBottomLeft, textBottomRight, pollMode, index, totalFrame);
                                break;
                            case 5:
                                out = TextTypingAnimationImpl.generateMeme2(getApplicationContext(), bitmaps.get(0).getBitmap(), textTop, textBottom, textBottomLeft, textBottomRight, pollMode, index, totalFrame);
                                break;
                            default:
                                out = BitmapGenerator.generateMeme2(getApplicationContext(), bitmaps.get(0).getBitmap(), textTop, textBottom, textBottomLeft, textBottomRight, pollMode);
                                break;

                        }
                        output.add(out);
                    }
                    GifGenerator.combineGifFramesDragon66(getApplicationContext(), output);
                }


                result = 1;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Lets update UI
            String outputName = "output.jpg", outputMeta = "image/jpeg";
            if(gifOutput){
                outputName = "output.gif";
                outputMeta = "image/gif";
            }
            if (result == 1) {
                progressBar.setVisibility(View.GONE);
                btnShare.setVisibility(View.VISIBLE);
                enableScreen();

                File root = getApplicationContext().getExternalFilesDir(null);
                File myDir = new File(root + "/");
                File file = new File(myDir, outputName);
                Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".my.package.name.provider", file);


                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType(outputMeta);
                startActivity(Intent.createChooser(shareIntent, "Send To"));
            }
        }
    }
    //Downloading data asynchronously
    public class AsyncAnimationTask extends AsyncTask<String, String, Integer> {
        int progress = 0; // top 1, bottom 2, bottomleft 3, bottomright 4
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            canvasTop.setVisibility(View.VISIBLE);
            if(!pollMode) {
                canvasBottom.setVisibility(View.VISIBLE);
            }
//            canvasBottomLeft.setVisibility(View.VISIBLE);
//            canvasBottomRight.setVisibility(View.VISIBLE);

            tvTop.setVisibility(View.INVISIBLE);
            if(!pollMode) {
                tvBottom.setVisibility(View.INVISIBLE);
            }
            invalidateScreen();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                //top
                String top = String.valueOf(tvTop.getText());
                String bottom = String.valueOf(tvBottom.getText());
                String animationCode = params[0];
                for (int i = 0; i<24; i++) {
                    publishProgress( top, bottom, String.valueOf(i), animationCode);
                    Thread.sleep(80);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            int animationCode = Integer.parseInt(values[3]);
            switch (animationCode){
                case 1:
                    canvasTop.setImageBitmap(TextZoomAnimationImpl.generateAnimatedTextTopFrame(getApplicationContext(), values[0], Integer.parseInt(values[2])));
                    canvasBottom.setImageBitmap(TextZoomAnimationImpl.generateAnimatedTextBottomFrame(getApplicationContext(), values[1], false, null, null, Integer.parseInt(values[2])));
                    break;
                case 2:
                    canvasTop.setImageBitmap(TextColorAnimationImpl.generateAnimatedTextTopFrame(getApplicationContext(), values[0], Integer.parseInt(values[2])));
                    canvasBottom.setImageBitmap(TextColorAnimationImpl.generateAnimatedTextBottomFrame(getApplicationContext(), values[1], false, null, null, Integer.parseInt(values[2])));
                    break;
                case 3:
                    canvasTop.setImageBitmap(TextRotateAnimationImpl.generateAnimatedTextTopFrame(getApplicationContext(), values[0], Integer.parseInt(values[2])));
                    canvasBottom.setImageBitmap(TextRotateAnimationImpl.generateAnimatedTextBottomFrame(getApplicationContext(), values[1], false, null, null, Integer.parseInt(values[2])));
                    break;
                case 4:
                    canvasTop.setImageBitmap(TextSlideAnimationImpl.generateAnimatedTextTopFrame(getApplicationContext(), values[0], Integer.parseInt(values[2])));
                    canvasBottom.setImageBitmap(TextSlideAnimationImpl.generateAnimatedTextBottomFrame(getApplicationContext(), values[1], false, null, null, Integer.parseInt(values[2])));
                    break;
                case 5:
                    canvasTop.setImageBitmap(TextTypingAnimationImpl.generateAnimatedTextTopFrame(getApplicationContext(), values[0], Integer.parseInt(values[2])));
                    canvasBottom.setImageBitmap(TextTypingAnimationImpl.generateAnimatedTextBottomFrame(getApplicationContext(), values[1], false, null, null, Integer.parseInt(values[2])));
                    break;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            canvasTop.setVisibility(View.INVISIBLE);
            if(!pollMode)
            canvasBottom.setVisibility(View.INVISIBLE);
            tvTop.setVisibility(View.VISIBLE);
            if(!pollMode)
            tvBottom.setVisibility(View.VISIBLE);
            enableScreen();
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
        }
    }
    private void invalidateScreen(){
        btnPollMode.setEnabled(false);
        btnAnimate.setEnabled(false);
    }
    private void enableScreen(){
        btnPollMode.setEnabled(true);
        btnAnimate.setEnabled(true);

    }
}
