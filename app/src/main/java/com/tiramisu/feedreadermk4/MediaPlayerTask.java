package com.tiramisu.feedreadermk4;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by ASUS on 06-02-2015.
 */
public class MediaPlayerTask extends AsyncTask<String, Void, Void> {

    boolean status;
    int myInt;
    StreamFragment streamFragment = new StreamFragment();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(streamFragment.mediaPlayer==null)
            Log.d("MDPlayer", "Null");
        streamFragment.mediaPlayer = new MediaPlayer();
        streamFragment.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            status = streamFragment.mediaPlayer.isPlaying();
            myInt = (status) ? 1 : 0;
            //if (!streamFragment.mediaPlayer.isPlaying()) {
                streamFragment.mediaPlayer.setDataSource(params[0]);
                streamFragment.mediaPlayer.prepare();
                streamFragment.mediaPlayer.start();
                //streamFragment.playerStatus = 1;
           // }
            /*else {
                Log.d("playerpause", "paused");
                streamFragment.mediaPlayer.pause();
                //streamFragment.playerStatus = 0;
            }*/

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
