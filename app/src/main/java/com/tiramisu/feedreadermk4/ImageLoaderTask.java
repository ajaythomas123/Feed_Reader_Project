package com.tiramisu.feedreadermk4;

import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * Created by ASUS on 16-01-2015.
 */
public class ImageLoaderTask extends AsyncTask<String, Void, Bitmap>{

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        return SearchActivity.getBitmapFromURL(url);
    }
}
