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
public class FamilyFragment extends Fragment {

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


    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.words_list, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);


        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word(R.drawable.family_father, "отец", "father", R.raw.family_father));
        words.add(new Word(R.drawable.family_mother, "мать", "mother", R.raw.family_mother));
        words.add(new Word(R.drawable.family_younger_sister, "сестра", "sister", R.raw.family_sister));
        words.add(new Word(R.drawable.family_younger_brother, "брат", "brother", R.raw.family_brother));
        words.add(new Word(R.drawable.family_son, "сын", "son", R.raw.family_son));
        words.add(new Word(R.drawable.family_daughter, "дочь", "daughter", R.raw.family_daughter));
        words.add(new Word(R.drawable.family_older_brother, "дядя", "uncle", R.raw.family_uncle));
        words.add(new Word(R.drawable.family_older_sister, "тетя", "aunt", R.raw.family_aunt));
        words.add(new Word(R.drawable.family_grandmother, "бабушка", "grandmother", R.raw.family_grandmother));
        words.add(new Word(R.drawable.family_grandfather, "дедушка", "grandfather", R.raw.family_grandfather));



        WordAdapter itemsAdapter = new WordAdapter(getActivity(), words, R.color.category_family);

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


