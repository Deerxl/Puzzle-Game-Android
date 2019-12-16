package com.example.hcia2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.print.PrinterId;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int DEFAULT_BOARD_SIZE = 4;
    private static final int TILE_IMAGE_ID = R.drawable.kitty;

    private PuzzleGameState mGameState = PuzzleGameState.NONE;
    private int mPuzzleBoardSize = DEFAULT_BOARD_SIZE;     // n x n
    private PuzzleGameBoard mPuzzleGameBoard;
    private  PuzzleGameTile puzzleGameTile;
    private PuzzleGameTileView[][] mPuzzleTileViews;

    private TextView mScoreTextView;
    private Button mNewGameButton;
    private EditText levelEditText;
    private TextView timeTextView;
    private TableLayout tableLayout;
    private TextView stepTextView;

    private int time;
    private int step;
    private Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {

        }
    };

    //四个方向
    private int[][] dir = {
            {0, 1},
            {1, 0},
            {0, -1},
            {-1, 0}
    };

    private final View.OnClickListener mNewGameButtonOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            // TODO: 2019/5/17 开始一个新游戏
            setLevel();
            startNewGame();
        }
    };

    private final View.OnClickListener mGameTileOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            //view即为传递进来的触发对象
            // TODO: 2019/5/17 移动tile到空tile 更新tile状态 是否win


            if(mGameState != PuzzleGameState.PLAYING){
                return;
            }

            PuzzleGameTileView puzzleGameTileView = (PuzzleGameTileView)view;
            int id = puzzleGameTileView.getTileId();
            int row = id / mPuzzleBoardSize;
            int col = id % mPuzzleBoardSize;
            puzzleGameTile = mPuzzleGameBoard.getTile(row, col);

            for(int i = 0; i< 4; i++){
                int nextRow = row + dir[i][0];
                int nextCol = col + dir[i][1];
                if(mPuzzleGameBoard.isWithinBounds(nextRow, nextCol)){
                    if(mPuzzleGameBoard.getTile(nextRow,nextCol).isEmpty()){
                        mPuzzleGameBoard.swapTiles(row, col, nextRow, nextCol);
                        mPuzzleTileViews[row][col].updateWithTile(mPuzzleGameBoard.getTile(row, col));
                        mPuzzleTileViews[nextRow][nextCol].updateWithTile(mPuzzleGameBoard.getTile(nextRow, nextCol));
                        stepTextView.setText(String.valueOf(++step));
                        break;
                    }

                }
            }

            refreshGameBoardView();
            updateGameState();

            upDateScore();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: 2019/5/17

        mScoreTextView = (TextView)findViewById(R.id.text_score);
        tableLayout = (TableLayout)findViewById(R.id.tableLayout);
        mNewGameButton = (Button)findViewById(R.id.newGameBtn);
        levelEditText = (EditText)findViewById(R.id.LevelEditText);
        timeTextView = (TextView)findViewById(R.id.timeTextView);
        stepTextView = (TextView)findViewById(R.id.stepTextView);


        mNewGameButton.setOnClickListener(mNewGameButtonOnClickListener);

        initGames();
        updateGameState();

    }

    private void initGames() {
        mPuzzleGameBoard = new PuzzleGameBoard(mPuzzleBoardSize, mPuzzleBoardSize);

        Bitmap fullImageBitmap = BitmapFactory.decodeResource(getResources(), TILE_IMAGE_ID);

        //切图片
        int fullImageWidth = fullImageBitmap.getWidth();
        int fullImageHeight = fullImageBitmap.getHeight();
        int squareImageSize = (fullImageWidth > fullImageHeight) ? fullImageWidth : fullImageHeight;

        fullImageBitmap = Bitmap.createScaledBitmap(
                fullImageBitmap,
                squareImageSize,
                squareImageSize,
                false);

        // TODO: 2019/5/17  计算小方块的大小
        int singleImageSize = squareImageSize / mPuzzleBoardSize;

        // TODO: 2019/5/17  创建小方块
        //        Also ensure the last tile (the bottom right tile) is set to be an "empty" tile
        //       (i.e. not filled with an section of the original image)

        PuzzleImageUtil puzzleImageUtil = new PuzzleImageUtil();

        for (int i = 0; i < mPuzzleBoardSize; i++){
            for(int j = 0;j< mPuzzleBoardSize; j++){
                Bitmap bm = PuzzleImageUtil.getSubdivisionOfBitmap(
                        fullImageBitmap,
                        singleImageSize,
                        singleImageSize,
                        i,
                        j
                );

                Drawable drawable = new BitmapDrawable(getResources(), bm);
                puzzleGameTile = new PuzzleGameTile(i * mPuzzleBoardSize + j, drawable);
                mPuzzleGameBoard.setTile(puzzleGameTile, i, j);
            }
        }

        // TODO: 2019/5/17   createPuzzleTileViews with the appropriate width, height
        createPuzzleTileViews(singleImageSize, singleImageSize);
    }

    private void createPuzzleTileViews(int minTileViewWidth, int minTileViewHeight) {
        int rowsCount = mPuzzleGameBoard.getRowsCount();
        int colsCount = mPuzzleGameBoard.getColumnsCount();

        // TODO Set up TileViews (that will be what the user interacts with
        //  Make sure each tileView gets a click listener for interaction
        //  Be sure to set the appropriate LayoutParams so that your tileViews
        //  So that they fit your gameBoard properly

        //设置每一个 小view 添加 监听事件
        mPuzzleTileViews = new PuzzleGameTileView[mPuzzleBoardSize][mPuzzleBoardSize];
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < colsCount; j++) {
                mPuzzleTileViews[i][j] = new PuzzleGameTileView(
                        this,
                        i * mPuzzleBoardSize + j,
                        minTileViewWidth,
                        minTileViewHeight);
                mPuzzleTileViews[i][j].setOnClickListener(mGameTileOnClickListener);
            }
        }

        //tableRow
        for(int i = 0; i< mPuzzleBoardSize; i++){
            TableRow tableRow = new TableRow(this);
            TableLayout.LayoutParams layoutParams =
                    new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            tableRow.setLayoutParams(layoutParams);

            //添加每一个小方块视图
            for(int j = 0; j < mPuzzleBoardSize; j++){
                puzzleGameTile = mPuzzleGameBoard.getTile(i, j);
                mPuzzleTileViews[i][j].setImageDrawable(puzzleGameTile.getDrawable());
                TableRow.LayoutParams layoutParams1 = new TableRow.LayoutParams(0);
                layoutParams1.weight = 1;
                mPuzzleTileViews[i][j].setAdjustViewBounds(true);
                layoutParams1.setMargins(1,1,1,1);
                mPuzzleTileViews[i][j].setLayoutParams(layoutParams1);

                tableRow.addView(mPuzzleTileViews[i][j]);
            }
            tableLayout.addView(tableRow);
        }
    }

    private void shufflePuzzleTiles(){
        // TODO: 2019/5/18  把小方块随机排列
        //找到空方块
        int x = 0, y = 0;
        for(int i = 0; i < mPuzzleBoardSize; i++){
            for(int j = 0; j < mPuzzleBoardSize; j++){
                if(mPuzzleGameBoard.getTile(i, j).isEmpty()){
                    x = i;
                    y = j;
                }
            }
        }
        //产生的随机方向有小方块，则和旁边的交换  用空方块一直循环
        int nextx, nexty;
        for(int i = 0; i< 100; i++){
            Random random = new Random();
            int type = random.nextInt(4);  //产生 0-3 随机数
            nextx = x + dir[type][0];
            nexty = y + dir[type][1];

            if(mPuzzleGameBoard.isWithinBounds(nextx, nexty)){
                mPuzzleGameBoard.swapTiles(x, y, nextx, nexty);
                mPuzzleTileViews[x][y].updateWithTile(mPuzzleGameBoard.getTile(x, y));
                mPuzzleTileViews[nextx][nexty].updateWithTile(mPuzzleGameBoard.getTile(nextx,nexty));
                x = nextx;
                y = nexty;
            }
        }
    }

    private void resetEmptyTileLocation(){
        // TODO: 2019/5/18 右下角置为空方块

        PuzzleGameTile puzzleGameTile = mPuzzleGameBoard.getTile(mPuzzleBoardSize -1, mPuzzleBoardSize -1);
        puzzleGameTile.setIsEmpty(true);
        mPuzzleGameBoard.setTile(puzzleGameTile, mPuzzleBoardSize -1, mPuzzleBoardSize -1);
    }

    private void updateGameState(){
        // TODO: 2019/5/18  更新方块 处理win的情况 更新分数

        if(hasWonGame() && mGameState == PuzzleGameState.PLAYING){
            mGameState = PuzzleGameState.WON;

            for(int i = 0; i < mPuzzleBoardSize; i++){
                for(int j = 0 ;j < mPuzzleBoardSize; j++){
                    mPuzzleTileViews[i][j].setVisibility(View.VISIBLE);
                }
            }

            Toast.makeText(MainActivity.this, "You win!", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshGameBoardView(){
        // TODO: 2019/5/18 更新 puzzleTileView画板中的信息
        //如果是空方块，则 视图不显示。
        for(int i =0; i< mPuzzleBoardSize; i++){
            for(int j = 0; j< mPuzzleBoardSize; j++){
                if(mPuzzleGameBoard.getTile(i ,j).isEmpty()){
                    mPuzzleTileViews[i][j].setVisibility(View.INVISIBLE);
                }else{
                    mPuzzleTileViews[i][j].setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private boolean hasWonGame(){
        // TODO: 2019/5/18 判断小方块是否归位
        for(int i = 0; i < mPuzzleBoardSize; i++){
            for(int j = 0; j< mPuzzleBoardSize; j++){
                if(mPuzzleGameBoard.getTile(i, j).getOrderIndex() != (i * mPuzzleBoardSize + j)){
                    return false;
                }
            }
        }
        return true;
    }

    private void upDateScore(){
        // TODO: 2019/5/18  更新分数

        mScoreTextView.setText(String.valueOf(mPuzzleBoardSize * step * 10 - time));
    }

    private void startNewGame(){
        // TODO: 2019/5/18  打乱小方块顺序，显示开始信息，修改游戏状态

        //如果不在游戏中，需要置右下角为空方块
        if(mGameState != PuzzleGameState.PLAYING){
            resetEmptyTileLocation();
        }
        shufflePuzzleTiles();
        refreshGameBoardView();
        mGameState = PuzzleGameState.PLAYING;

        step = 0;
        stepTextView.setText(String.valueOf(step));

        time = 0;
        timeCount();

        upDateScore();

        Toast.makeText(MainActivity.this, "游戏开始!", Toast.LENGTH_SHORT).show();
    }

    private void timeCount(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mGameState == PuzzleGameState.PLAYING){
                    try{
                        Thread.sleep(1000);    // 1s
                        handler.sendEmptyMessage(1);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what){
                case 1:
                    time++;
                    timeTextView.setText(String.valueOf(time));
                    break;
                default:
                    break;
            }
        }
    };

    private void setLevel(){
        int level = Integer.parseInt(levelEditText.getText().toString());
        if(level < 3){
            Toast.makeText(MainActivity.this, "level must >=3 !", Toast.LENGTH_SHORT).show();
            return;
        }else{
            mPuzzleBoardSize = level;
            tableLayout.removeAllViews();
            mGameState = PuzzleGameState.NONE;
            initGames();
            updateGameState();
        }

        /*mPuzzleBoardSize = level;
        tableLayout.removeAllViews();
        mGameState = PuzzleGameState.NONE;
        initGames();
        updateGameState();*/
    }
}
