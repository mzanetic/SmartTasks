package com.github.mobile.smarttasks.android.ui;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

public class FontManager {

    private static FontManager instance;

    private final Map<String, Typeface> fonts = new HashMap<>();

    public static FontManager getInstance() {
        if (instance == null) instance = new FontManager();
        return instance;
    }

    public Typeface getFont(Context context, String asset) {
        AssetManager manager = context.getAssets();

        // Typeface loading or retrieval
        synchronized (fonts) {
            if (fonts.containsKey(asset))
                return fonts.get(asset);

            Typeface font = Typeface.createFromAsset(manager, asset);
            fonts.put(asset, font);
        }

        return fonts.get(asset);
    }
}
