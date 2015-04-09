package com.tiramisu.feedreadermk4;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by ASUS on 16-01-2015.
 */
public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener, ClickListener {
    SearchView searchView;
    RecyclerView recyclerView;
    int i;
    Thread searchThreadInstance, streamThreadInstance;
    //Test Commit
    List<SearchResult> searchResults = new ArrayList<SearchResult>();
    List<StreamResult> streamResults = new ArrayList<StreamResult>();
    SearchResultAdapter searchAdapter;
    StreamResultAdapter streamAdapter;
    String conDescription = "description" + "\"" + ":";
    String conContentType = "contentType" + "\"" + ":";
    String conSIcon = "iconUrl" + "\"" + ":";
    String conLIcon = "visualUrl" + "\"" + ":";
    String conSummary;
    String conTitle;
    String conVisual;
    String conEnclosure;
    String conAlternate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        super.onCreateDrawer("Search");
        searchView = (SearchView) toolbar.findViewById(R.id.search_widget);
        searchView.setOnQueryTextListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        searchAdapter = new SearchResultAdapter(this, searchResults);
        streamAdapter = new StreamResultAdapter(this, streamResults);
        searchAdapter.setClickListener(this);
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void downloadSearchResult(String url) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;


        try {
            URL jURL = new URL(url);
            connection = (HttpURLConnection) jURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-length", "0");
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.connect();
            if (connection.getResponseCode() == 200) {

                inputStream = connection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = bufferedReader.readLine();
                bufferedReader.close();

                JSONObject rootObject = new JSONObject(line);
                JSONArray jsonArray = (JSONArray) rootObject.get("results");
//Log.d("blah", Integer.valueOf(jsonArray.length()).toString());
                JSONObject feedObj;
                String[] sIconUrl = new String[jsonArray.length()];
                String[] lIconUrl = new String[jsonArray.length()];

                for (i = 0; i < jsonArray.length(); i++) {
                    feedObj = (JSONObject) jsonArray.get(i);
                    String feedString = jsonArray.get(i).toString();
                    /*conDescription = "description" + "\"" + ":";
                    conContentType = "contentType" + "\"" + ":";
                    conSIcon = "iconUrl" + "\"" + ":";
                    conLIcon = "visualUrl" + "\"" + ":";*/
                    conDescription = " ";
                    conContentType = " ";
                    conSIcon = " ";
                    conLIcon = " ";


                    /*if (!(feedString.contains(conDescription))) {
                        conDescription = " ";
                    } else {
                        conDescription = feedObj.getString("description");
                    }

                    if (!(feedString.contains(conContentType))) {
                        conContentType = " ";
                    } else {
                        conContentType = feedObj.getString("contentType");
                    }

                    if (!(feedString.contains(conSIcon))) {
                        conSIcon = "https://lh3.googleusercontent.com/chB-XtkDWOgvBAPlZ4P_DrhLy-u1CiJ6P6BlaC3DjYsZUFsBHF2xrs3500JMIa9iZqckVLwCdjc=w1342-h523";
                        sIconUrl[i] = conSIcon;
                    } else {
                        conSIcon = feedObj.getString("iconUrl");
                        sIconUrl[i] = conSIcon;
                        //Log.d("Ajay", conSIcon);
                    }

                    if (!(feedString.contains(conLIcon))) {
                        conLIcon = "https://lh3.googleusercontent.com/chB-XtkDWOgvBAPlZ4P_DrhLy-u1CiJ6P6BlaC3DjYsZUFsBHF2xrs3500JMIa9iZqckVLwCdjc=w1342-h523";
                        lIconUrl[i] = conLIcon;
                    } else {
                        conLIcon = feedObj.getString("visualUrl");
                        lIconUrl[i] = conLIcon;
                    }*/

                    if (feedObj.has("description")) {
                        conDescription = feedObj.getString("description");
                    }

                    if (feedObj.has("contentType")) {
                        conContentType = feedObj.getString("contentType");
                    }

                    if (feedObj.has("iconUrl")) {
                        conSIcon = feedObj.getString("iconUrl");
                        sIconUrl[i] = conSIcon;
                    }

                    if (feedObj.has("visualUrl")) {
                        conLIcon = feedObj.getString("visualUrl");
                        lIconUrl[i] = conLIcon;
                    }


                    SearchResult searchResult = new SearchResult(null, null, null, null, null, null, null);
                    searchResults.add(searchResult);
                    searchResults.get(i).feedId = feedObj.getString("feedId");
                    searchResults.get(i).title = feedObj.getString("title");
                    searchResults.get(i).description = conDescription;
                    searchResults.get(i).contentType = conContentType;
                    searchResults.get(i).subscribers = feedObj.getString("subscribers");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            searchAdapter.notifyDataSetChanged();
                        }
                    });

                 }

                for (i = 0; i < jsonArray.length(); i++) {
                    conSIcon = sIconUrl[i];
                    conLIcon = lIconUrl[i];
                    //Log.d("Happy", Integer.valueOf(searchResults.size()).toString());
                    //Log.d("Ajay", "ab" + conSIcon);
                    searchResults.get(i).setsIcon(new ImageLoaderTask().execute(conSIcon).get());
                    searchResults.get(i).setlIcon(new ImageLoaderTask().execute(conLIcon).get());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            searchAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void itemClicked(View view, int position) {

        /*This method is called when and item in the SearchActivity is clicked*/
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
        searchThreadInstance.interrupt();
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
        //Toast.makeText(this, searchResults.get(position).title, Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getSharedPreferences("Subscriptions", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String[] subFeed = new String[5];
        int subCounter = 0;
        subCounter = sharedPreferences.getInt("subCounter", 0);
        subCounter = subCounter + 1;
        subFeed[0] = searchResults.get(position).feedId;
        subFeed[1] = searchResults.get(position).title;
        subFeed[2] = searchResults.get(position).description;
        subFeed[3] = searchResults.get(position).contentType;
        subFeed[4] = searchResults.get(position).subscribers;
        editor.putInt("subCounter", subCounter);
        editor.putString("feedId" + Integer.valueOf(subCounter).toString(), subFeed[0]);
        editor.putString("title" + Integer.valueOf(subCounter).toString(), subFeed[1]);
        editor.putString("description" + Integer.valueOf(subCounter).toString(), subFeed[2]);
        editor.putString("contentType" + Integer.valueOf(subCounter).toString(), subFeed[3]);
        editor.putString("subscribers" + Integer.valueOf(subCounter).toString(), subFeed[4]);
        editor.commit();
        Toast.makeText(this, "Subscribed!", Toast.LENGTH_SHORT).show();

    }

    public class searchThread implements Runnable {
        String query;

        public searchThread(String query) {
            this.query = query;
        }

        @Override
        public void run() {
            try {

                downloadSearchResult("http://cloud.feedly.com/v3/search/feeds?query=" + URLEncoder.encode(query, "UTF-8") + "&count=10");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_INVALID;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        if (!s.equals(null) || !s.equals("")) {
            searchThreadInstance = new Thread(new searchThread(s));
            searchThreadInstance.interrupt();
            searchThreadInstance.start();
            searchResults.clear();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    public static Bitmap getBitmapFromURL(String src) {
        Bitmap dstBitmap = null;
        Bitmap myBitmap = null;
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            if (connection.getResponseCode() != 200)
                return null;

            InputStream input = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(input);
            int width;
            int height;
            width = myBitmap.getWidth();
            height = myBitmap.getHeight();
            if (width >= height) {
                dstBitmap = Bitmap.createBitmap(myBitmap, width / 2 - height / 2, 0, height, height);
            } else {
                dstBitmap = Bitmap.createBitmap(myBitmap, 0, height / 2 - width / 2, width, width);
            }
            return Bitmap.createScaledBitmap(dstBitmap, 100, 100, false);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if ((myBitmap != null) && !myBitmap.isRecycled()) {
                myBitmap.recycle();
            }
            if ((dstBitmap != null) && !dstBitmap.isRecycled()) {
                dstBitmap.recycle();
            }
        }
    }


}
