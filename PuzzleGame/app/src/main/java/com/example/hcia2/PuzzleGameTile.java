package com.example.hcia2;

import android.graphics.drawable.Drawable;

public class PuzzleGameTile {

    public static final int INVALID_TILE_INDEX = -1;

    private int mOrderIndex  = INVALID_TILE_INDEX;
    private boolean mIsEmpty = false;
    private Drawable mDrawable = null;

    public PuzzleGameTile(){

    }

    public PuzzleGameTile(int tileOrderIndex, Drawable drawable){
        this(tileOrderIndex, drawable, false);
    }

    public PuzzleGameTile(int tileOrderIndex, Drawable drawable, boolean isEmpty){
        mOrderIndex = tileOrderIndex;
        mDrawable = drawable;
        mIsEmpty = isEmpty;
    }

    public int getOrderIndex(){
        return mOrderIndex;
    }

    public void setOrderIndex(int index){
        mOrderIndex = index;
    }

    public void setIsEmpty(boolean isEmpty){
        mIsEmpty = isEmpty;
    }

    public boolean isEmpty(){
        return mIsEmpty;
    }

    public void setDrawable(Drawable drawable){
        mDrawable = drawable;
    }

    public Drawable getDrawable(){
        return mDrawable;
    }
}
