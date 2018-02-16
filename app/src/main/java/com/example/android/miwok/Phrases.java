package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class Phrases extends AppCompatActivity {
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
        final ArrayList<Word> words = new ArrayList<>();
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        words.add(new Word("where are you going?","A dónde vas?",R.raw.whereareyougoing));
        words.add(new Word("what is your name?","cuál es tu nombre?",R.raw.whatisyourname));
        words.add(new Word("my name is","mi nombre es",R.raw.mynameis));
        words.add(new Word("how are you feeling","como te sientes",R.raw.howareyoufeeling));
        words.add(new Word("am feeling good","Me siento bien",R.raw.amfeelinggood));
        words.add(new Word("are you coming?","vienes",R.raw.areyoucoming));
        words.add(new Word("yes,I am coming","sí, vengo",R.raw.yescoming));
        words.add(new Word("i am coming","Vengo",R.raw.amcoming));
        words.add(new Word("lets go","vamonos",R.raw.letsgo));
        words.add(new Word("come here","ven aca",R.raw.comehere));
        WordAdapter listAdapter = new WordAdapter(this,words,R.color.category_phrases);
        ListView listview = (ListView) findViewById(R.id.list);
        listview.setAdapter(listAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word word = words.get(i);

                releaseMediaPlayer();
                int reqResult= am.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(reqResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                { mediaPlayer = MediaPlayer.create(Phrases.this,word.getAudioId());
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

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}
