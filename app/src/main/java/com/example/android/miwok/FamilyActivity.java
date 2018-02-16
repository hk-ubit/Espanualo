package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    AudioManager am;
    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focus) {
            if (focus == AudioManager.AUDIOFOCUS_GAIN)
            {
                mediaPlayer.start();
            }
            if((focus== AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) ||(focus== AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK))
            {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            }
            if(focus == AudioManager.AUDIOFOCUS_LOSS)
            {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("father","padre",R.drawable.family_father,R.raw.father));
        words.add(new Word("mother","madre",R.drawable.family_mother,R.raw.madre));
        words.add(new Word("son","hijo",R.drawable.family_son,R.raw.son));
        words.add(new Word("daughter","HIJA",R.drawable.family_daughter,R.raw.daughter));
        words.add(new Word("elder brother","HERMANO MAYOR",R.drawable.family_older_brother,R.raw.elderbro));
        words.add(new Word("younger brother","HERMANO MAS JOVEN",R.drawable.family_younger_brother,R.raw.youngerbro));
        words.add(new Word("elder sister","HERMANA MAYOR",R.drawable.family_older_sister,R.raw.eldersis));
        words.add(new Word("younger sister","HERMANA MENOR",R.drawable.family_younger_sister,R.raw.youngersis));
        words.add(new Word("grand mother","ABUELA",R.drawable.family_grandmother,R.raw.grandmom));
        words.add(new Word("grand father","ABUELO",R.drawable.family_grandfather,R.raw.granddad));
        WordAdapter listAdapter = new WordAdapter(this,words,R.color.category_family);
        final ListView listview = (ListView) findViewById(R.id.list);
        listview.setAdapter(listAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word word = words.get(i);
                releaseMediaPlayer();
               int reqResult= am.requestAudioFocus(afChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(reqResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                { mediaPlayer = MediaPlayer.create(FamilyActivity.this,word.getAudioId());
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        releaseMediaPlayer();
                    }
                });}
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;
            am.abandonAudioFocus(afChangeListener);
        }
    }
}
