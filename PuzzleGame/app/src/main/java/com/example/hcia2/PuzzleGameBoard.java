package com.example.hcia2;

import android.graphics.drawable.Drawable;

public class PuzzleGameBoard {

    private PuzzleGameTile[][] mTileGrid;
    private int mRows;
    private int mColumns;

    public PuzzleGameBoard(int size){
        this(size, size);
    }

    public PuzzleGameBoard(int rows, int columns){
        if(rows <= 0 || columns <= 0){
            throw new IllegalArgumentException(
                    "GameBoard must have width/height dimensions greater than 0");
        }
        mRows = rows;
        mColumns = columns;
        mTileGrid = new PuzzleGameTile[mRows][mColumns];
    }

    public int getRowsCount(){
        return mRows;
    }
    public int getColumnsCount(){
        return mColumns;
    }

    public int getTotalTileCount(){
        return mRows * mColumns;
    }

    public void setTile(PuzzleGameTile tile, int row, int col)
    {
       // throw new IndexOutOfBoundsException(row, col);
        mTileGrid[row][col] = tile;
    }

    public PuzzleGameTile getTile(int row, int col){
        return mTileGrid[row][col];
    }

    //判断tile是否为空
    public boolean isEmptyTile(int row, int col){
        // TODO: 2019/5/18
        if(mTileGrid[row][col] != null)
        {
            return true;
        }
        return false;
    }
    public void reset() {
        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mColumns; j++) {
                mTileGrid[i][j] = null;
            }
        }
    }

    /*
     * Checks if two tiles neighbor eachother on the gameboard along the vertical and horizontal
     * axes:
     *      // We want to check the neighbors along the horizontal and vertical axis
     * The 1's below represent the neighbors of a tile called X. 0's are not neighbors)
     * |0 | 1 | 0
     * |1 | X | 1
     * |0 | 1 | 0
     * @param firstTileRow - the first tile's row index
     * @param firstTileCol - the first tile's col index
     * @param secondTileRow - the second tile's row index
     * @param secondTileCol - the second tile's col index
     * @return true if the tiles are neighbors
     */
    public boolean areTilesNeighbors(
            int firstTileRow,
            int firstTileCol,
            int secondTileRow,
            int secondTileCol) {
        throwOutOfBoundsExceptionIfNecessary(firstTileRow, firstTileCol);
        throwOutOfBoundsExceptionIfNecessary(secondTileRow, secondTileCol);
        // TODO - check if tiles are neighborss
        return false;

    }

    /*
     * checks if the given row, col position is within the bounds of the game board
     * @param row
     * @param col
     * @return true if the row, col is within the game board
     */
    public boolean isWithinBounds(int row, int col) {
        if (row < 0 || row >= mRows || col < 0 || col >= mColumns) {
            return false;
        }
        return true;
    }

    /*
     * Swaps the tile in the game board at (firstTileRow, firstTileCol) with the tile at
     * (secondTileRow, secondTileCol) if they are within the bounds of the game board
     * @param firstTileRow - row index of first tile
     * @param firstTileCol - col index of first tile
     * @param secondTileRow - row index of second tile
     * @param secondTileCol - col index of second tile
     */
    public final void swapTiles(
            int firstTileRow,
            int firstTileCol,
            int secondTileRow,
            int secondTileCol) {
        throwOutOfBoundsExceptionIfNecessary(firstTileRow, firstTileCol);
        throwOutOfBoundsExceptionIfNecessary(secondTileRow, secondTileCol);

        // TODO - swap two tiles at the given indices

        PuzzleGameTile puzzleGameTile = getTile(firstTileRow,firstTileCol);
        setTile(getTile(secondTileRow,secondTileCol),firstTileRow,firstTileCol);
        setTile(puzzleGameTile,secondTileRow,secondTileCol);
    }

    /*
     * Helper to check if a row, col is within bounds and throw an out of bounds exception if necessary
     * @param row
     * @param col
     * @throws IndexOutOfBoundsException when the row, col combination is out of bounds of the game
     * board
     */
    private final void throwOutOfBoundsExceptionIfNecessary(
            int row,
            int col)
            throws IndexOutOfBoundsException {
        if (!isWithinBounds(row, col)) {
            throw new IndexOutOfBoundsException("row,col combination is out of board's dimensions");
        }
    }

}
