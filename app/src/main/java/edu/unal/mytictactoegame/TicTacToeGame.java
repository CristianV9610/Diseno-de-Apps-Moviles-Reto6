package edu.unal.mytictactoegame;

import java.util.Random;
import java.util.Arrays;

public class TicTacToeGame {

    private char mBoard[] = {'1','2','3','4','5','6','7','8','9'};
    public static final char HUMAN_PLAYER = 'O';
    public static final char COMPUTER_PLAYER = 'X';
    public static final char OPEN_SPOT = ' ';
    public static final int BoardSize = 9;

    private Random mRand;

    public enum Difficulty {Easy, Harder, Expert};

    private static Difficulty mDifficultyLevel = Difficulty.Expert;

    public  Difficulty getDifficultyLevel() {
        return mDifficultyLevel;
    }

    public  void setDifficulty(Difficulty difficulty) {
        mDifficultyLevel = difficulty;
    }

    public TicTacToeGame(){
        mRand = new Random();
    }

    public void clearBoard(){
        Arrays.fill(mBoard, OPEN_SPOT);
    }

    public void setMove(char player, int location){
        if (mBoard[location] == OPEN_SPOT)
            mBoard[location]= player;
    }

    private int getRandomMove(){
        // Generate random move
        int move;
        do
        {
            move = mRand.nextInt(BoardSize);
        } while (mBoard[move] != OPEN_SPOT);
        return move;
    }

    private int getWinningMove(){
        int move = -1;
        //See if there's a move X can make to win
        for (int i = 0; i < BoardSize; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
                char curr = mBoard[i];
                mBoard[i] = COMPUTER_PLAYER;
                if (checkForWinner() == 3) {
                    //System.out.println("Computer is moving to " + (i + 1));
                    move = i;
                    break;
                }
                else
                    mBoard[i] = curr;
            }
        }
        return move;
    }

    private int getBlockingMove() {
        int move = -1;
        // See if there's a move X can make to block O from winning
        for (int i = 0; i < BoardSize; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
                char curr = mBoard[i];   // Save the current number
                mBoard[i] = HUMAN_PLAYER;
                if (checkForWinner() == 2) {
                    mBoard[i] = COMPUTER_PLAYER;
                    move = i;
                    break;
                }
                else
                    mBoard[i] = curr;
            }
        }
        return move;
    }

    public int getComputerMove() {
        int move = -1;
        switch (mDifficultyLevel){
            case Easy:
                move = getRandomMove();
                break;
            case Harder:
                move = getWinningMove();
                if (move == -1)
                    move = getRandomMove();
                break;
            case Expert:
                move = getWinningMove();
                if (move == -1)
                    move = getBlockingMove();
                if (move == -1)
                    move = getRandomMove();
                break;
        }
        return move;
    }

    public int checkForWinner(){
        //Check horizontal wins
        for (int i = 0; i <= 6; i += 3)	{
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i+1] == HUMAN_PLAYER &&
                    mBoard[i+2]== HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i+1]== COMPUTER_PLAYER &&
                    mBoard[i+2] == COMPUTER_PLAYER)
                return 3;
        }

        // Check vertical wins
        for (int i = 0; i <= 2; i++) {
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i+3] == HUMAN_PLAYER &&
                    mBoard[i+6]== HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i+3] == COMPUTER_PLAYER &&
                    mBoard[i+6]== COMPUTER_PLAYER)
                return 3;
        }

        // Check for diagonal wins
        if ((mBoard[0] == HUMAN_PLAYER &&
                mBoard[4] == HUMAN_PLAYER &&
                mBoard[8] == HUMAN_PLAYER) ||
                (mBoard[2] == HUMAN_PLAYER &&
                        mBoard[4] == HUMAN_PLAYER &&
                        mBoard[6] == HUMAN_PLAYER))
            return 2;
        if ((mBoard[0] == COMPUTER_PLAYER &&
                mBoard[4] == COMPUTER_PLAYER &&
                mBoard[8] == COMPUTER_PLAYER) ||
                (mBoard[2] == COMPUTER_PLAYER &&
                        mBoard[4] == COMPUTER_PLAYER &&
                        mBoard[6] == COMPUTER_PLAYER))
            return 3;

        // Check for tie
        for (int i = 0; i < BoardSize; i++) {
            // If we find a number, then no one has won yet
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER)
                return 0;
        }

        // If we make it through the previous loop, all places are taken, so it's a tie
        return 1;
    }

}

