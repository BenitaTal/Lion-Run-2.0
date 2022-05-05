package com.example.hw1.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.fragment.app.Fragment;


import com.example.hw1.classes.GameManager;
import com.example.hw1.classes.PlayerLocation;
import com.example.hw1.R;
import com.example.hw1.callBacks.CallBack_Map;
import com.example.hw1.utils.DataManager;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Collections;

public class Fragment_List extends Fragment{
    private View view;
    private TableLayout table;
    private ArrayList<TableRow> tableRows;
    private ArrayList<GameManager> topTenList;
    private CallBack_Map mapCallback = null;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);

        topTenList = new ArrayList<>();

        setTopTen();
        findViews();
        addTableRows();

        return view;
    }

    private void addTableRows() {
        if (tableRows == null){
            tableRows = new ArrayList<>();
        }
        setHeaders();
        if(topTenList.size() == 0){
            TableRow tableRow = new TableRow(view.getContext());
            MaterialTextView noUsersTXT = new MaterialTextView(view.getContext());
            //noUsersTXT.setText("No users records yet...");
            noUsersTXT.setPadding(16, 16, 16, 16);
            tableRow.addView(noUsersTXT);

            table.addView(tableRow);
        }else {
            for (GameManager user : this.topTenList) {
                TableRow tableRow = new TableRow(view.getContext());
                MaterialTextView nameColData = new MaterialTextView(view.getContext());
                MaterialTextView scoreColData = new MaterialTextView(view.getContext());
                MaterialTextView locationColData = new MaterialTextView(view.getContext());

                nameColData.setText(user.getPlayerName());
                scoreColData.setText(String.valueOf(user.getScore()));
                locationColData.setText(user.getLocation().getAddressName());

                nameColData.setPadding(16,16,16,16 );
                scoreColData.setPadding(16,16,16,16);
                locationColData.setPadding(16,16,16,16);
                locationColData.setTextColor(Color.BLUE);
                locationColData.setOnClickListener(e -> handleLocationClick(user.getLocation()));

                tableRow.addView(nameColData);
                tableRow.addView(scoreColData);
                tableRow.addView(locationColData);

                table.addView(tableRow);
            }
        }

    }

    private void setHeaders() {
        TableRow tableRow = new TableRow(view.getContext());

        MaterialTextView nameCol = new MaterialTextView(view.getContext());
        MaterialTextView scoreCol = new MaterialTextView(view.getContext());
        MaterialTextView locationCol = new MaterialTextView(view.getContext());

        nameCol.setText("Name");
        nameCol.setTextSize(20);
        nameCol.setPadding(16,16,16,16);

        scoreCol.setText("Score");
        scoreCol.setTextSize(20);
        scoreCol.setPadding(16,16,16,16);

        locationCol.setText("Location");
        locationCol.setTextSize(20);
        locationCol.setPadding(16,16,16,16);

        tableRow.addView(nameCol);
        tableRow.addView(scoreCol);
        tableRow.addView(locationCol);

        table.addView(tableRow);
    }


    private void setTopTen() {
        ArrayList <GameManager> users = DataManager.getInstance().getUsersData();
        if (users != null){
            Collections.sort(users);
            int iterationLength = Math.min(10, users.size());
            for (int i = 0; i < iterationLength; i++) {
                topTenList.add(users.get(i));
            }
        }
    }

    private void handleLocationClick(PlayerLocation location) {
        mapCallback.addMarkerToMap(location.getLongitude(),
                location.getLatitude(), location.getAddressName());
    }

    public void registerMapCallback(CallBack_Map callback){
        this.mapCallback = callback;
    }
    private void findViews() {
        table = view.findViewById(R.id.game_TBL_List);

    }

}
