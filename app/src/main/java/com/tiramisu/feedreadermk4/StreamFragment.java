package com.tiramisu.feedreadermk4;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by ASUS on 17-01-2015.
 */
public class StreamFragment extends Fragment implements ClickListener, View.OnClickListener {
    List<StreamResult> streamResults = new ArrayList<StreamResult>();
    StreamResultAdapter streamAdapter;
    PodcastStreamResultAdapter podcastStreamAdapter;
    String conSummary;
    String conTitle;
    String conVisual;
    String conEnclosure;
    String conAlternate;
    public MediaPlayer mediaPlayer = null;
    public int playerStatus = 0;
    int i;
    Thread streamThreadInstance;
    RecyclerView recyclerView;
    boolean flag = true;
    ImageButton playPause, subscribe;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_stream, container, false);

        String acTitle = "hi";
        try {
            ActivityInfo activityInfo = getActivity().getPackageManager().getActivityInfo(getActivity().getComponentName(), PackageManager.GET_META_DATA);
            acTitle = activityInfo.name.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layout.getLayoutParams();

        //Toast.makeText(getActivity(), acTitle, Toast.LENGTH_SHORT).show();
        if(acTitle.equals("com.tiramisu.feedreadermk4.SubscriptionActivity")){
            params.setMargins(0, 0, 0, 0);
        }

        recyclerView = (RecyclerView) layout.findViewById(R.id.frag_recycler_view);
        //subscribe = (ImageButton) getActivity().findViewById(R.id.subscribe_button);
        //subscribe.setVisibility(View.VISIBLE);
        String contentType = this.getArguments().getString("contentType");
        if (contentType.equals("audio")) {
            Log.d("Podcast", "yay");
            podcastStreamAdapter = new PodcastStreamResultAdapter(getActivity(), streamResults);
            podcastStreamAdapter.setClickListener(this);
            recyclerView.setAdapter(podcastStreamAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        else {
            streamAdapter = new StreamResultAdapter(getActivity(), streamResults);
            streamAdapter.setClickListener(this);
            recyclerView.setAdapter(streamAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }


        String streamId = this.getArguments().getString("streamId");
        Log.d("Frag", " " + streamId);
        startStreamThread(streamId);
        //subscribe.setOnClickListener(this);
        return layout;
    }

    public void downloadStreamResult(String url) {
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
            Log.d("Frag", Integer.valueOf(connection.getResponseCode()).toString());
            if (connection.getResponseCode() == 200) {

                inputStream = connection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = bufferedReader.readLine();
                bufferedReader.close();

                JSONObject rootObject = new JSONObject(line);
                String streamId = rootObject.getString("id");
                JSONArray jsonArray = (JSONArray) rootObject.get("items");
                JSONObject jObj;
                String[] imUrl = new String[jsonArray.length()];
                final String contentType = this.getArguments().getString("contentType");
                for (i = 0; i < jsonArray.length(); i++) {
                    jObj = (JSONObject) jsonArray.get(i);
                    conSummary = " ";
                    conTitle = " ";
                    conVisual = "";
                    conEnclosure = " ";
                    conAlternate = " ";
                    imUrl[i] = " ";
                    JSONArray altArray;
                    JSONObject altObject;
                    StreamResult streamResult = new StreamResult(null, null, null, null, null, null);
                    streamResults.add(streamResult);

                    if (jObj.has("summary")) {
                        streamResults.get(i).content = (new JSONObject(jObj.getString("summary"))).getString("content");
                    }
                    if (jObj.has("content")) {
                        streamResults.get(i).content = (new JSONObject(jObj.getString("content"))).getString("content");
                    }
                    if (jObj.has("title")) {
                        streamResults.get(i).title = jObj.getString("title");
                    }
                    if (jObj.has("alternate")) {
                        altArray = (JSONArray) jObj.get("alternate");
                        altObject = (JSONObject) altArray.get(0);
                        streamResults.get(i).itemUrl = altObject.getString("href");
                    }
                    if (jObj.has("enclosure")) {
                        altArray = (JSONArray) jObj.get("enclosure");
                        altObject = (JSONObject) altArray.get(0);
                        streamResults.get(i).enContent = altObject.getString("href");
                    }
                    if (jObj.has("visual")) {
                        conVisual = (new JSONObject(jObj.getString("visual"))).getString("url");
                        imUrl[i] = conVisual;
                    }
                    if (getActivity() == null) {
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(contentType.equals("audio")){
                                podcastStreamAdapter.notifyDataSetChanged();
                            }
                            else{
                                streamAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }

                for (i = 0; i < jsonArray.length(); i++) {
                    conVisual = imUrl[i];
                    streamResults.get(i).setThumbnail(new ImageLoaderTask().execute(conVisual).get());     //Sometimes loading immages can cause the app to crash. This is because cyberoam blocks the links
                    if (getActivity() == null) {
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(contentType.equals("audio")){
                                podcastStreamAdapter.notifyDataSetChanged();
                            }
                            else{
                                streamAdapter.notifyDataSetChanged();
                            }
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
            flag = false;
        }
    }

    @Override
    public void itemClicked(View view, int position) {

    }

    @Override
    public void entryClicked(View view, int position) {


        String title = streamResults.get(position).getTitle();
        String content = streamResults.get(position).getContent();
        String itemUrl = streamResults.get(position).getItemUrl();
        ArticleFragment articleFragment = new ArticleFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("content", content);
        args.putString("itemUrl", itemUrl);
        articleFragment.setArguments(args);
        transaction.add(R.id.activity_layout, articleFragment, "article");
        transaction.addToBackStack("AddArticle");
        transaction.commit();
    }

    @Override
    public void podEntryClicked(View view, int position) {
        String st = streamResults.get(position).getEnContent();



        MediaControlsFragment mediaControlsFragment = new MediaControlsFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle args = new Bundle();
        args.putString("enContent", st);
        mediaControlsFragment.setArguments(args);
        transaction.add(R.id.activity_layout, mediaControlsFragment, "medCon");
        transaction.addToBackStack("AddMedCon");
        transaction.commit();



        //MediaPlayerTask mediaPlayerTask = new MediaPlayerTask();
        //mediaPlayerTask.execute(st);

/*
        playPause = (ImageButton) view.findViewById(R.id.podPP);
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        try {
            if(!mediaPlayer.isPlaying()){
                if(playerStatus == 0) {
                    mediaPlayer.setDataSource(st);
                    mediaPlayer.prepare();
                    playerStatus = 1;

                    playPause.setImageResource(R.drawable.podcast_pause_68dp);
                    Log.d("PlayerStatus", "0");
                }
                Log.d("PlayerStatus", "1");
                mediaPlayer.start();
            }
            else{
                mediaPlayer.pause();
                playerStatus =2;
                playPause.setImageResource(R.drawable.podcast_play_68dp);
                Log.d("PlayerStatus", "2");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }

    @Override
    public void subscribe(View view, int position) {

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
    }

    public class streamThread implements Runnable {
        String streamId;

        public streamThread(String streamId) {
            this.streamId = streamId;
        }

        @Override
        public void run() {
            try {
                while (flag)
                    downloadStreamResult("https://cloud.feedly.com/v3/streams/" + URLEncoder.encode(streamId, "UTF-8") + "/contents");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public void startStreamThread(String s) {

        if (!s.equals(null) || !s.equals("")) {
            streamThreadInstance = new Thread(new streamThread(s));
            streamThreadInstance.interrupt();
            streamThreadInstance.start();
            streamResults.clear();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.app_bar);
        toolbar.setTitle("");
        SearchView searchView = (SearchView) getActivity().findViewById(R.id.search_widget);
        searchView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.add(Menu.NONE, R.id.action_subscribe,Menu.NONE,"Subscribe");
        item.setIcon(R.drawable.podcast_download_white_24dp);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
