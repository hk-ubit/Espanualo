package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NumberFragment extends Fragment {

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


    public NumberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView = inflater.inflate(R.layout.word_list, container, false);
        am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("one","UNO",R.drawable.number_one,R.raw.one));
        words.add(new Word("two","DOS",R.drawable.number_two,R.raw.two));
        words.add(new Word("three","TRES",R.drawable.number_three,R.raw.three));
        words.add(new Word("four","LAS CUATRO",R.drawable.number_four,R.raw.four));
        words.add(new Word("five","CINCO",R.drawable.number_five,R.raw.five));
        words.add(new Word("six","SEIS",R.drawable.number_six,R.raw.six));
        words.add(new Word("seven","SIETE",R.drawable.number_seven,R.raw.seven));
        words.add(new Word("eight","OCHO",R.drawable.number_eight,R.raw.eight));
        words.add(new Word("nine","NUEVE",R.drawable.number_nine,R.raw.nine));
        words.add(new Word("ten","DIEZ",R.drawable.number_ten,R.raw.ten));





        WordAdapter itemsAdapter = new WordAdapter(getActivity(), words,R.color.category_numbers);

        ListView listView = (ListView) rootView.findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word word = words.get(i);

                releaseMediaPlayer();
                int reqResult= am.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(reqResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                { mediaPlayer = MediaPlayer.create(getActivity(),word.getAudioId());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            releaseMediaPlayer();
                        }
                    });}
            }
        });
        return rootView;

    }

    @Override
    public void onStop() {
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
