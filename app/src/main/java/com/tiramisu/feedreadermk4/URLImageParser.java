package com.tiramisu.feedreadermk4;

import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class URLImageParser implements ImageGetter {
    Context context;
    TextView container;

    public URLImageParser(TextView container, Context context) {
        this.context = context;
        this.container = container;
    }

    public Drawable getDrawable(String source) {
        if(source.matches("data:image.*base64.*")) {
            String base_64_source = source.replaceAll("data:image.*base64", "");
            byte[] data = Base64.decode(base_64_source, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Drawable image = new BitmapDrawable(context.getResources(), bitmap);

            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            float multiplier = (float)image.getIntrinsicWidth()/(float)image.getIntrinsicHeight();
            float newWidth = 336*metrics.density;
            float newHeight = newWidth/multiplier;
            image.setBounds(0, 0, 0 + (int) newWidth, 0 + (int) newHeight);
            return image;
        } else {
            URLDrawable urlDrawable = new URLDrawable();
            ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);
            asyncTask.execute(source);
            return urlDrawable; //return reference to URLDrawable where We will change with actual image from the src tag
        }
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        URLDrawable urlDrawable;

        public ImageGetterAsyncTask(URLDrawable d) {
            this.urlDrawable = d;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            return fetchDrawable(source);
        }

        @Override
        protected void onPostExecute(Drawable result) {
            // set the correct bound according to the result from HTTP call
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            float multiplier = (float)result.getIntrinsicWidth()/(float)result.getIntrinsicHeight();
            float newWidth = 336*metrics.density;
            float newHeight = newWidth/multiplier;
            Log.d("height", "" + result.getIntrinsicHeight());
            Log.d("width",""+result.getIntrinsicWidth());
            urlDrawable.setBounds(0, 0, (int) newWidth, (int) newHeight);

            // change the reference of the current drawable to the result
            // from the HTTP call
            urlDrawable.drawable = result;

            // redraw the image by invalidating the container
            URLImageParser.this.container.invalidate();

            // For ICS
            URLImageParser.this.container.setHeight((URLImageParser.this.container.getHeight()
                    + (int)newHeight));

            // Pre ICS
            URLImageParser.this.container.setEllipsize(null);
        }

        public Drawable fetchDrawable(String urlString) {
            try {
                InputStream is = (InputStream) new URL(urlString).getContent();
                Drawable drawable = Drawable.createFromStream(is, "src");
                Log.d("Getter", Integer.valueOf(drawable.getIntrinsicWidth()).toString());
                Log.d("Getter", Integer.valueOf(drawable.getIntrinsicHeight()).toString());
                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                float multiplier = (float)drawable.getIntrinsicWidth()/(float)drawable.getIntrinsicHeight();
                Log.d("Getter", Float.valueOf(multiplier).toString());
                float newWidth = 336*metrics.density;
                float newHeight = newWidth/multiplier;
                Log.d("Getter", Float.valueOf(newWidth).toString());
                Log.d("Getter", Float.valueOf(newHeight).toString());
                drawable.setBounds(0, 0, (int) newWidth, (int) newHeight);
                return drawable;
            } catch (Exception e) {
                return null;
            }
        }
    }
}