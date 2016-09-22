package com.example.android.miwok;

/**
 * Created by arakon01 on 14.08.2016.
 */
public class Word {

    private int mImageResourceId = NO_IMAGE_PROVIDED;
    private String mRusTranslation;
    private String mEngTranslation;
    private static final int NO_IMAGE_PROVIDED = -1;
    private int mAudioResourceId;

    public Word(String rusTranslation, String engTranslation, int getAudioResourceId) {
        mRusTranslation = rusTranslation;
        mEngTranslation = engTranslation;
        mAudioResourceId = getAudioResourceId;
    }

    public Word(int imageResourceId, String rusTranslation, String engTranslation, int audioResourceId) {
        mImageResourceId = imageResourceId;
        mRusTranslation = rusTranslation;
        mEngTranslation = engTranslation;
        mAudioResourceId = audioResourceId;
    }


    public int getImageResourceId() {
        return mImageResourceId;
    }

    public String getRusTranslation() {
        return mRusTranslation;
    }

    public String getEngTranslation() {
        return mEngTranslation;
    }

    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

    public int getAudioResourceId() {
        return mAudioResourceId;
    }
}


