package com.example.hcia2;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.AppCompatImageView;
import android.widget.ImageView;

public class PuzzleGameTileView extends AppCompatImageView {

    private int mTileId = -1;

    public PuzzleGameTileView(Context context){
        super(context);
    }

    public PuzzleGameTileView(Context context, int tileId, int minWidth, int minHeight){
        super(context);
        mTileId = tileId;
        init(minWidth, minHeight);
    }

    public void init(int minWidth, int minHeight){
        setMinimumHeight(minHeight);
        setMinimumWidth(minWidth);
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }

    public int getTileId(){
        return mTileId;
    }

    public void setTileId(int id){
        mTileId = id;
    }

    public void updateWithTile(PuzzleGameTile tile){
        setImageDrawable(tile.getDrawable());
    }
}
