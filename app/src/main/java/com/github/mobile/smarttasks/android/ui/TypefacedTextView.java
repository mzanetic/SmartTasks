package com.github.mobile.smarttasks.android.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.github.mobile.smarttasks.R;

/**
 * TextView with font loading
 */
public class TypefacedTextView extends TextView {


    public TypefacedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) return;

        FontManager fontManager = FontManager.getInstance();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TypefacedTextView);
        String fontName = typedArray.getString(R.styleable.TypefacedTextView_typeface);
        typedArray.recycle();


        if (fontName != null) {
            setTypeface(fontManager.getFont(context, fontName));
        }
    }

    public TypefacedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
