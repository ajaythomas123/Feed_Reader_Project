package com.tiramisu.feedreadermk4;


import android.media.MediaPlayer;
import android.media.AudioManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class MediaControlsFragment extends Fragment implements View.OnClickListener, MediaPlayer.OnPreparedListener{

    MediaPlayer mediaPlayer = null;
    int playerStatus;
    String enContent;
    ImageButton playPauseButton, fforwardButton, rewindButton, stopButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_media_controls, container, false);
        playPauseButton = (ImageButton) layout.findViewById(R.id.mpCon_playPause);
        stopButton = (ImageButton) layout.findViewById(R.id.mpCon_stop);
        enContent = getArguments().getString("enContent");
        playPauseButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        return layout;
    }


    @Override
    public void onClick(View v) {
        //Toast.makeText(getActivity(), enContent, Toast.LENGTH_SHORT).show();
        if(v.getId() == R.id.mpCon_playPause) {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            try {
                if (!mediaPlayer.isPlaying()) {
                    if (playerStatus == 0) {
                        mediaPlayer.setDataSource(enContent);
                        mediaPlayer.setOnPreparedListener(this);
                        mediaPlayer.prepareAsync();
                        playerStatus = 1;
                        playPauseButton.setImageResource(R.drawable.podcast_pause_68dp);


                        Log.d("PlayerStatus", "0");
                    }
                    if (playerStatus == 2) {
                        mediaPlayer.start();
                        playPauseButton.setImageResource(R.drawable.podcast_pause_68dp);
                    }
                    Log.d("PlayerStatus", "1");

                } else {
                    mediaPlayer.pause();
                    playerStatus = 2;
                    playPauseButton.setImageResource(R.drawable.podcast_play_68dp);
                    Log.d("PlayerStatus", "2");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(v.getId() == R.id.mpCon_stop){
            if(mediaPlayer != null){
                mediaPlayer.stop();
                playPauseButton.setImageResource(R.drawable.podcast_play_68dp);
                mediaPlayer.release();
                mediaPlayer = null;
                playerStatus = 0;
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();

    }
}
