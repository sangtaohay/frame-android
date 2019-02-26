package com.sangtaohay.animationbrowser;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;

public class AnimationBrowser extends HorizontalScrollView {

    private int animationCode;

    public int getAnimationCode() {
        return animationCode;
    }

    public void setAnimationCode(int animationCode) {
        this.animationCode = animationCode;
    }

    View rootView;
    Button btn0, btn1,btn2, btn3, btn4, btn5, btn6, btn7;

    public AnimationBrowser(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context) {
        rootView = inflate(context, R.layout.animation_browser, this);
        btn0 = rootView.findViewById(R.id.btnAnimation0);
        btn1 = rootView.findViewById(R.id.btnAnimation1);
        btn2 = rootView.findViewById(R.id.btnAnimation2);
        btn3 = rootView.findViewById(R.id.btnAnimation3);
        btn4 = rootView.findViewById(R.id.btnAnimation4);
        btn5 = rootView.findViewById(R.id.btnAnimation5);
        btn6 = rootView.findViewById(R.id.btnAnimation6);
        btn7 = rootView.findViewById(R.id.btnAnimation7);
        btn0.setOnClickListener(onClickListener);
        btn1.setOnClickListener(onClickListener);
        btn2.setOnClickListener(onClickListener);
        btn3.setOnClickListener(onClickListener);
        btn4.setOnClickListener(onClickListener);
        btn5.setOnClickListener(onClickListener);
        btn6.setOnClickListener(onClickListener);
        btn7.setOnClickListener(onClickListener);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        resetView();
        switch (animationCode){
            case 0: btn0.setAlpha(0.8f); break;
            case 1: btn1.setAlpha(0.8f); break;
            case 2: btn2.setAlpha(0.8f); break;
            case 3: btn3.setAlpha(0.8f); break;
            case 4: btn4.setAlpha(0.8f); break;
            case 5: btn5.setAlpha(0.8f); break;
        }
    }

    View.OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            resetView();

            if(view.getId() == R.id.btnAnimation0){
                btn0.setAlpha(0.8f);
                animationCode = 0;
            }if(view.getId() == R.id.btnAnimation1){
                btn1.setAlpha(0.8f);
                animationCode = 1;
            }else if(view.getId() == R.id.btnAnimation2){
                btn2.setAlpha(0.8f);
                animationCode = 2;
            }else if(view.getId() == R.id.btnAnimation3){
                btn3.setAlpha(0.8f);
                animationCode = 3;
            }else if(view.getId() == R.id.btnAnimation4){
                btn4.setAlpha(0.8f);
                animationCode = 4;
            }else if(view.getId() == R.id.btnAnimation5){
                btn5.setAlpha(0.8f);
                animationCode = 5;
            }else if(view.getId() == R.id.btnAnimation6){
                animationCode = 6;
            }else if(view.getId() == R.id.btnAnimation7){
                animationCode = 7;
            }
            rootView.performClick();
        };
    };
    private void resetView(){
        btn0.setAlpha(0.5f);
        btn1.setAlpha(0.5f);
        btn2.setAlpha(0.5f);
        btn3.setAlpha(0.5f);
        btn4.setAlpha(0.5f);
        btn5.setAlpha(0.5f);
        btn6.setAlpha(0.5f);
        btn7.setAlpha(0.5f);
    }
}
