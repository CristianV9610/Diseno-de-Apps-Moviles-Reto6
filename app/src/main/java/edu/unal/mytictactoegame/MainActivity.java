package edu.unal.mytictactoegame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.view.MotionEvent;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TicTacToeGame mGame;
    private BoardView mBoardView;

    private TextView mInfoTextView;
    private TextView mTieCountView;
    private TextView mYourCountView;
    private TextView mMyCountView;

    private boolean mGameOver;
    private int mYourCount;
    private int mMyCount;
    private int mTieCount;

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;
    static final int DIALOG_ABOUT_ID = 2;

    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mComputerMediaPlayer;
    MediaPlayer mTieMediaPlayer;
    MediaPlayer mIWinMediaPlayer;
    MediaPlayer mYouWinMediaPlayer;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;

            if (!mGameOver && setMove(TicTacToeGame.HUMAN_PLAYER, pos)) {

                int winner = mGame.checkForWinner();

                if (winner == 0) {
                    mInfoTextView.setText(R.string.my_turn);
                    mComputerMediaPlayer.start();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            int move = mGame.getComputerMove();
                            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                            int winner = mGame.checkForWinner();
                            mBoardView.invalidate();
                            if (winner == 0){
                                mInfoTextView.setText(R.string.your_turn);
                                mHumanMediaPlayer.start();
                            }
                            else{
                                if (winner == 1){
                                    mTieMediaPlayer.start();
                                    mInfoTextView.setText(R.string.tie);
                                    if(!mGameOver) {
                                        mTieCount++;
                                    }
                                    mTieCountView.setText(String.valueOf(mTieCount));
                                    mGameOver = true;
                                }

                                else if (winner == 2){
                                    mYouWinMediaPlayer.start();
                                    mInfoTextView.setText(R.string.you_win);
                                    if(!mGameOver) {
                                        mYourCount++;
                                    }
                                    mYourCountView.setText(String.valueOf(mYourCount));
                                    mGameOver = true;
                                }

                                else{
                                    mIWinMediaPlayer.start();
                                    mInfoTextView.setText(R.string.i_win);
                                    if(!mGameOver) {
                                        mMyCount++;
                                    }
                                    mMyCountView.setText(String.valueOf(mMyCount));
                                    mGameOver = true;
                                }
                            }
                        }
                    }, 750);
                }
                else{
                    if (winner == 1){
                        mTieMediaPlayer.start();
                        mInfoTextView.setText(R.string.tie);
                        if(!mGameOver) {
                            mTieCount++;
                        }
                        mTieCountView.setText(String.valueOf(mTieCount));
                        mGameOver = true;
                    }

                    else if (winner == 2){
                        mYouWinMediaPlayer.start();
                        mInfoTextView.setText(R.string.you_win);
                        if(!mGameOver) {
                            mYourCount++;
                        }
                        mYourCountView.setText(String.valueOf(mYourCount));
                        mGameOver = true;
                    }

                    else{
                        mIWinMediaPlayer.start();
                        mInfoTextView.setText(R.string.i_win);
                        if(!mGameOver) {
                            mMyCount++;
                        }
                        mMyCountView.setText(String.valueOf(mMyCount));
                        mGameOver = true;
                    }
                }
            }
            return false;
        }
    };



    private void startNewGame(){

        mGame.clearBoard();
        mBoardView.invalidate();
        mGameOver = false;

//        for (int i=0; i< mBoardButtons.length; i++){
//            mBoardButtons[i].setText("");
//            mBoardButtons[i].setEnabled(true);
//            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
//        }

        mInfoTextView.setText(R.string.your_turn);
    }

    private boolean setMove(char player, int location) {
        if(!mGameOver && mGame.setMove(player, location)) {
            mBoardView.invalidate();
            return true;
        }
        return false;
    }

    @Override
    protected void onResume(){
        super.onResume();

        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.your_turn);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.my_turn);
        mTieMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.is_tie);
        mIWinMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.i_win);
        mYouWinMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.you_win);
    }

    @Override
    protected void onPause(){
        super.onPause();

        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
        mTieMediaPlayer.release();
        mIWinMediaPlayer.release();
        mYouWinMediaPlayer.release();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mBoardButtons = new Button[TicTacToeGame.BoardSize];
//        mBoardButtons[0] = findViewById(R.id.one);
//        mBoardButtons[1] = findViewById(R.id.two);
//        mBoardButtons[2] = findViewById(R.id.three);
//        mBoardButtons[3] = findViewById(R.id.four);
//        mBoardButtons[4] = findViewById(R.id.five);
//        mBoardButtons[5] = findViewById(R.id.six);
//        mBoardButtons[6] = findViewById(R.id.seven);
//        mBoardButtons[7] = findViewById(R.id.eight);
//        mBoardButtons[8] = findViewById(R.id.nine);

        mInfoTextView = findViewById(R.id.information);
        mTieCountView = findViewById(R.id.tiecount);
        mYourCountView = findViewById(R.id.humancount);
        mMyCountView = findViewById(R.id.androidcount);

        mGame = new TicTacToeGame();
        mBoardView = findViewById(R.id.board);
        mBoardView.setGame(mGame);

        mYourCount = 0;
        mMyCount = 0;
        mTieCount = 0;

        mBoardView.setOnTouchListener(mTouchListener);


        startNewGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
            case R.id.about:
                showDialog(DIALOG_ABOUT_ID);
                return true;
        }
        return false;
    }

    @Override
    protected Dialog onCreateDialog(int id){
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int selected = 2;
        TicTacToeGame.Difficulty DifficultyLevel;

        switch(id) {
            case DIALOG_DIFFICULTY_ID:
                builder.setTitle(R.string.difficulty_choose);
                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)
                };

                DifficultyLevel = mGame.getDifficultyLevel();

                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();
                                switch(item) {
                                    case 0:
                                        mGame.setDifficulty(TicTacToeGame.Difficulty.Easy);
                                        break;
                                    case 1:
                                        mGame.setDifficulty(TicTacToeGame.Difficulty.Harder);
                                        break;
                                    case 2:
                                        mGame.setDifficulty(TicTacToeGame.Difficulty.Expert);
                                        break;
                                }

                                Toast.makeText(getApplicationContext(), levels[item],
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog = builder.create();
                break;
            case DIALOG_QUIT_ID:
                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id){
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();
                break;
            case DIALOG_ABOUT_ID:
                //AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                Context context = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.about_dialog, null);
                builder.setView(layout);
                builder.setPositiveButton("OK", null);
                dialog = builder.create();
                break;
        }
        return dialog;

    }







}


