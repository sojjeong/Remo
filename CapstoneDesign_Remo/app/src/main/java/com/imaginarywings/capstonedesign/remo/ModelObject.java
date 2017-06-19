package com.imaginarywings.capstonedesign.remo;

public enum ModelObject {

    COVER(R.string.cover, R.layout.tutorial_cover),
    CAMERA(R.string.camera, R.layout.tutorial_camera),
    PHOTOSPOT(R.string.photospot, R.layout.tutorial_photospot);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}