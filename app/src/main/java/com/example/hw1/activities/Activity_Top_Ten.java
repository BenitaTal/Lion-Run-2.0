package com.example.hw1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hw1.fragments.Fragment_List;
import com.example.hw1.fragments.Fragment_Map;
import com.example.hw1.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class Activity_Top_Ten extends AppCompatActivity {
    private static final String USER_KEY  = "user";

    private MaterialTextView top_ten_LBL_title;
    private MaterialButton top_ten_BTN_back;
    private MaterialTextView top_ten_LBL_map;


    Bundle bundle;

    private Fragment_List fragment_list;
    private Fragment_Map fragment_map;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);
        bundle = getIntent().getExtras();
        String str =bundle.getString(USER_KEY);

        loadFragmentMap();
        loadFragmentUsers();
        findViews();

        top_ten_BTN_back.setOnClickListener(backToMenuBtn);

    }

    private View.OnClickListener backToMenuBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            replaceActivity();
        }
    };

    private void replaceActivity() {
        Intent intent = new Intent(this, Activity_Menu.class);
        intent.putExtras(bundle);
        startActivity(intent);
        /// try
        finish();
    }


    private void loadFragmentMap() {
        fragment_map = new Fragment_Map();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.game_LAY_map, fragment_map).commit();
    }

    private void loadFragmentUsers() {
        fragment_list = new Fragment_List();
        fragment_list.registerMapCallback((lon, lat, addressName) ->
                fragment_map.addMarkerToMap(lon, lat, addressName));

        getSupportFragmentManager().beginTransaction().
                replace(R.id.game_LAY_list, fragment_list).commit();
    }

    private void findViews() {
        top_ten_LBL_title = findViewById(R.id.game_top_ten_LBL_title);
        top_ten_BTN_back = findViewById(R.id.game_top_ten_BTN_back);
        top_ten_LBL_map = findViewById(R.id.game_top_ten_LBL_map);

    }


}
