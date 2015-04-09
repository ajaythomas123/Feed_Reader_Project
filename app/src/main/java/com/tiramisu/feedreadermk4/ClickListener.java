package com.tiramisu.feedreadermk4;

import android.view.View;

/**
 * Created by ASUS on 17-01-2015.
 */
public interface ClickListener {
    public void itemClicked(View view, int position);
    public void entryClicked(View view, int position);
    public void podEntryClicked(View view, int position);
    public void subscribe(View view, int position);
}
