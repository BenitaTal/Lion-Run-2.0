package com.example.hw1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hw1.classes.GameManager;
import com.example.hw1.R;
import com.example.hw1.utils.DataManager;
import com.google.android.material.button.MaterialButton;


public class Activity_Log extends AppCompatActivity {
    private static final String BACKGROUND_URL = "https://images.hdqwalls.com/wallpapers/running-lion-4k-ma.jpg";
    private static final String USER_KEY  = "user";
    private MaterialButton submitLog;
    private EditText nameLog;
    private Bundle bundle = null;
    private ImageView main_IMG_back1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        initPage();



    }
    private void setUserData() {
        String name = nameLog.getText().toString();

        GameManager game = DataManager.getInstance().getUserByName(name);
        if (game == null) {
            game = new GameManager();
            game.setPlayerName(name);
        }

        bundle.putString(USER_KEY, game.userToJson());
    }

    private void setGlide(){
        main_IMG_back1 = findViewById(R.id.game_IMG_background);
        Glide
                .with(this)
                .load(BACKGROUND_URL)
                .placeholder(R.drawable.img_placeholder)
                .into(main_IMG_back1);
    }

    private void initPage() {
        DataManager.init(this);
        if (bundle == null){
            bundle = new Bundle();
        }
        setViews();
    }
    private void setViews() {
        setGlide();
        findViews();

    }


    private void openActivity() {
        Intent newIntent = new Intent(this, Activity_Menu.class);
        newIntent.putExtras(bundle);
        startActivity(newIntent);
        finish();
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }


    public void findViews(){
        nameLog = findViewById(R.id.game_log);
        submitLog = (MaterialButton) findViewById(R.id.game_BTN_log_submit);

        submitLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
                setUserData();
                openActivity();
            }
        });
    }

}
