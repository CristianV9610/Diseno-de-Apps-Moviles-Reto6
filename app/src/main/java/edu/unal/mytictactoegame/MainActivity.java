package edu.unal.mytictactoegame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TicTacToeGame mGame;
    //private TicTacToeGame mGameDifficulty;
    private Button mBoardButtons[];
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBoardButtons = new Button[TicTacToeGame.BoardSize];
        mBoardButtons[0] = findViewById(R.id.one);
        mBoardButtons[1] = findViewById(R.id.two);
        mBoardButtons[2] = findViewById(R.id.three);
        mBoardButtons[3] = findViewById(R.id.four);
        mBoardButtons[4] = findViewById(R.id.five);
        mBoardButtons[5] = findViewById(R.id.six);
        mBoardButtons[6] = findViewById(R.id.seven);
        mBoardButtons[7] = findViewById(R.id.eight);
        mBoardButtons[8] = findViewById(R.id.nine);

        mInfoTextView = findViewById(R.id.information);
        mTieCountView = findViewById(R.id.tiecount);
        mYourCountView = findViewById(R.id.humancount);
        mMyCountView = findViewById(R.id.androidcount);

        mGame = new TicTacToeGame();
        mYourCount = 0;
        mMyCount = 0;
        mTieCount = 0;

        startNewGame();
    }

    private void startNewGame(){

        mGame.clearBoard();
        mGameOver = false;

        for (int i=0; i< mBoardButtons.length; i++){
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }
        mInfoTextView.setText(R.string.your_turn);
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

    private class ButtonClickListener implements View.OnClickListener {
        int location;

        public ButtonClickListener(int location){
            this.location = location;
        }

        public void onClick (View view) {
            if (mBoardButtons[location].isEnabled()) {
                setMove(TicTacToeGame.HUMAN_PLAYER, location);

                int winner = mGame.checkForWinner();
                if (winner == 0) {
                    mInfoTextView.setText(R.string.my_turn);
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }
                if (winner == 0)
                    mInfoTextView.setText(R.string.your_turn);
                else if (winner == 1) {
                    mInfoTextView.setText(R.string.tie);
                    if(!mGameOver) {
                        mTieCount++;
                    }
                    mTieCountView.setText(String.valueOf(mTieCount));
                    mGameOver = true;
                }
                else if (winner == 2) {
                    mInfoTextView.setText(R.string.you_win);
                    if(!mGameOver) {
                        mYourCount++;
                    }
                    mYourCountView.setText(String.valueOf(mYourCount));
                    mGameOver = true;
                }
                else {
                    mInfoTextView.setText(R.string.i_win);
                    if(!mGameOver) {
                        mMyCount++;
                    }
                    mMyCountView.setText(String.valueOf(mMyCount));
                    mGameOver = true;
                }
            }
        }
    }

    private void setMove(char player, int move) {
        if(!mGameOver) {
            mGame.setMove(player, move);
            mBoardButtons[move].setEnabled(false);
            mBoardButtons[move].setText(String.valueOf(player));
            if (player == TicTacToeGame.HUMAN_PLAYER)
                mBoardButtons[move].setTextColor(Color.rgb(0, 200, 0));
            else {
                mBoardButtons[move].setTextColor(Color.rgb(200, 0, 0));
            }
        }
    }





}


