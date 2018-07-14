package ru.makproductions.apocalypseweatherapp;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

public class TroikaTypefaceSpan extends TypefaceSpan {
    private Typeface newTypeface;

    public TroikaTypefaceSpan(String family,Typeface typefaceSpan) {
        super(family);
        this.newTypeface = typefaceSpan;
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        apply(paint,newTypeface);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        apply(ds,newTypeface);
    }
    private static void apply(Paint paint, Typeface tf) {
        int oldStyle;

        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        int fake = oldStyle & ~tf.getStyle();

        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(tf);
    }
}
