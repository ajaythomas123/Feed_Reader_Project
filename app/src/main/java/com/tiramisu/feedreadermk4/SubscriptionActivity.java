package com.tiramisu.feedreadermk4;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SubscriptionActivity extends BaseActivity implements ClickListener{
    SearchView searchView;
    TextView textView;
    int subCounter;
    int rowVisibility = 0;
    List<SearchResult> searchResults = new ArrayList<SearchResult>();
    SearchResultAdapter searchAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        super.onCreateDrawer(getResources().getString(R.string.subscriptions_title));
        searchView = (SearchView) toolbar.findViewById(R.id.search_widget);
        searchView.setVisibility(View.GONE);


        SharedPreferences sharedPreferences = getSharedPreferences("Subscriptions", Context.MODE_PRIVATE);
        subCounter = sharedPreferences.getInt("subCounter", 0);
        if(subCounter != 0)
        {
            //Toast.makeText(this, Integer.valueOf(subCounter).toString(), Toast.LENGTH_SHORT).show();
            for(int i = 0; i < subCounter; i++){
                int j = i+1;
                SearchResult searchResult = new SearchResult(null, null, null, null, null, null, null);
                searchResults.add(searchResult);
                searchResults.get(i).feedId = sharedPreferences.getString("feedId" + Integer.valueOf(j).toString(), "null");
                searchResults.get(i).title = sharedPreferences.getString("title" + Integer.valueOf(j).toString(), "null");
                searchResults.get(i).description = sharedPreferences.getString("description" + Integer.valueOf(j).toString(), "null");
                searchResults.get(i).contentType = sharedPreferences.getString("contentType" + Integer.valueOf(j).toString(), "null");
                searchResults.get(i).subscribers = sharedPreferences.getString("subscribers" + Integer.valueOf(j).toString(), "null");
            }
            /*for(int i = 0; i < subCounter; i++){
                Toast.makeText(this, searchResults.get(i).feedId, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, searchResults.get(i).title, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, searchResults.get(i).description, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, searchResults.get(i).contentType, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, searchResults.get(i).subscribers, Toast.LENGTH_SHORT).show();

            }*/

        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        searchAdapter = new SearchResultAdapter(this, searchResults);
        searchAdapter.setClickListener(this);
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subscription, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_SUB;
    }

    @Override
    public void itemClicked(View view, int position) {
        String s = searchResults.get(position).getFeedId();
        String contentType = searchResults.get(position).getContentType();
        Log.d("Podcast", contentType);
        StreamFragment streamFragment = new StreamFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle args = new Bundle();
        args.putString("streamId", s);
        args.putString("contentType", contentType);
        streamFragment.setArguments(args);
        transaction.add(R.id.activity_layout, streamFragment, "stream");
        transaction.addToBackStack("AddStream");
        searchView.setVisibility(View.GONE);
        toolbar.setTitle(searchResults.get(position).getTitle());
        //searchThreadInstance.interrupt();
        transaction.commit();
    }

    @Override
    public void entryClicked(View view, int position) {

    }

    @Override
    public void podEntryClicked(View view, int position) {

    }

    @Override
    public void subscribe(View view, int position) {

    }
}
