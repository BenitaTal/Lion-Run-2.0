package com.example.hw1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hw1.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class Activity_Menu extends AppCompatActivity {
    private static final String BACKGROUND_URL = "https://images.hdqwalls.com/wallpapers/running-lion-4k-ma.jpg";
    private static final String USER_KEY  = "user";

    private MaterialButton arrowGameBtn;
    private MaterialButton sensorGameBtn;
    private MaterialButton topTenBtn;
    private MaterialButton exitBtn;
    private TextInputEditText nameLog;
    private ImageView main_IMG_back1;


    private TextView title;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        String str =bundle.getString(USER_KEY);

        setContentView(R.layout.activity_menu);
        setGlide();
        findViews();


        arrowGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceActivity("buttons");
            }
        });

        sensorGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceActivity("sensor");
            }
        });

        topTenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceActivityToTonTen();
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitGame();
            }
        });
    }

    private void replaceActivity(String game) {
        Intent intent = new Intent(this, Activity_Game.class);

        bundle.putString("game",game);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void replaceActivityToTonTen() {
        Intent intent = new Intent(this, Activity_Top_Ten.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


    private void findViews() {
        arrowGameBtn = (MaterialButton) findViewById(R.id.game_BTN_arrow_game);
        sensorGameBtn = (MaterialButton) findViewById(R.id.game_BTN_sensor_game);
        topTenBtn = (MaterialButton) findViewById(R.id.game_BTN_record_table);
        exitBtn = (MaterialButton) findViewById(R.id.game_BTN_exit);
        title = (TextView) findViewById(R.id.game_Title);

    }
    private void setGlide(){
        main_IMG_back1 = findViewById(R.id.game_IMG_background_Menu);
        Glide
                .with(this)
                .load(BACKGROUND_URL)
                .placeholder(R.drawable.img_placeholder)
                .into(main_IMG_back1);
    }

    private void exitGame() {
        Toast.makeText(getApplicationContext(),"Game is over", Toast.LENGTH_LONG).show();
        Handler handler = new Handler();
        handler.postDelayed(() -> System.exit(0), 1000);
        finish();
    }

}
