package com.tiramisu.feedreadermk4;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class DownloadsActivity extends BaseActivity {
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads);
        super.onCreateDrawer(getResources().getString(R.string.downloads_title));
        searchView = (SearchView) toolbar.findViewById(R.id.search_widget);
        searchView.setVisibility(View.GONE);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_INVALID;
    }
}
