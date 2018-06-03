package com.shiv_ndroid98.cinebox;


import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class MySpannable  extends ClickableSpan{

    private boolean isUnderline = false;

    /**
     * Constructor
     */
    public MySpannable(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {

        ds.setUnderlineText(isUnderline);
        ds.setColor(Color.parseColor("#343434"));

    }

    @Override
    public void onClick(View view) {

    }
}
