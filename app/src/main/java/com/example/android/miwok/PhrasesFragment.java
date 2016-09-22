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
public class PhrasesFragment extends Fragment {

    private AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {


                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        mMediaPlayer.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        releaseMediaPlayer();
                    }
                }
            };

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };


    public PhrasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.words_list, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);


        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("Как тебя зовут?", "What is your name?", R.raw.what_is_your_name));
        words.add(new Word("Куда ты идешь?", "Where are you going?", R.raw.where_are_you_going));
        words.add(new Word("Меня зовут...", "My name is...", R.raw.my_name_is));
        words.add(new Word("Который час?", "What time is it now?", R.raw.what_time_is_it_now));
        words.add(new Word("Сколько тебе лет?", "How old are you?", R.raw.how_old_are_you));
        words.add(new Word("Удачи!", "Good luck!", R.raw.good_luck));
        words.add(new Word("Хорошего дня!", "Have a nice day!", R.raw.have_a_nice_day));
        words.add(new Word("Простите, я не могу.", "I'm sorry, I can't.", R.raw.im_sorry_i_cant));
        words.add(new Word("Могу ли я вам помочь?", "May I help you?", R.raw.may_i_help_you));
        words.add(new Word("Можно задать вам вопрос?", "May I ask you a question?", R.raw.may_i_ask_you_a_question));


        WordAdapter itemsAdapter = new WordAdapter(getActivity(), words, R.color.category_phrases);

        ListView listView = (ListView) rootView.findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();
                Word word = words.get(position);
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getAudioResourceId());
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);

                }
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
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}


