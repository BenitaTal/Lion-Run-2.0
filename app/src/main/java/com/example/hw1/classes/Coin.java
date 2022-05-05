package com.example.hw1.classes;


public class Coin {

    private int locRowCoin;
    private int locColCoin;

    public Coin(){
        this.locRowCoin = 0;
        this.locColCoin = 0;
    }

    public void placeTheCoin(int gameRow,int gameCol){
        int rangeRow = gameRow - 1;
        int randRow = (int)(Math.random() * rangeRow);

        int rangeCol = gameCol - 1;
        int randCol = (int)(Math.random() * rangeCol);

        setLocRowCoin(randRow);
        setLocColCoin(randCol);
    }

    public boolean IsLionOnCoin(int lionRowLoc, int lionColLoc){
        if ((locRowCoin == lionRowLoc) && (locColCoin == lionColLoc))
            return true;
        else
            return false;

    }

    public boolean IsHunterOnCoin(int hunterRowLoc, int hunterColLoc){
        if ((locRowCoin == hunterRowLoc) && (locColCoin == hunterColLoc))
            return true;
        else
            return false;

    }

    public int getLocRowCoin() {
        return locRowCoin;
    }

    public void setLocRowCoin(int locRowCoin) {
        this.locRowCoin = locRowCoin;
    }

    public int getLocColCoin() {
        return locColCoin;
    }

    public void setLocColCoin(int locColCoin) {
        this.locColCoin = locColCoin;
    }
}

