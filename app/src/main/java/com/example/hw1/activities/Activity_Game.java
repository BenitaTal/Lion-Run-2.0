package com.example.hw1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hw1.classes.Coin;
import com.example.hw1.classes.GameManager;
import com.example.hw1.R;
import com.example.hw1.classes.SensorMaker;
import com.example.hw1.classes.SoundMaker;
import com.example.hw1.utils.DataManager;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Activity_Game extends AppCompatActivity {
    private static final String USER_KEY  = "user";
    private final int numOfRow =6;
    private final int numOfCol =5;

    private Handler handler;
    private int direction = -1;
    private final int animalDir = 0;

    private int locRowAnimal;
    private int locColAnimal;
    private int locRowHunter;
    private int locColHunter;

    private ImageView[][] matrixImage;
    private ImageView[] game_IMG_hearts;
    private MaterialButton[] game_BTN_arrows;
    private GameManager gameManager;
    private Coin coin;
    private SoundMaker sound;
    private TextView textView;
    private String game;

    private SensorMaker sensorMaker;
    private SensorManager sensorManager;
    private ImageView joystickImage;
    Bundle bundle;
    private boolean sensorGame = false;

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        String str =bundle.getString(USER_KEY);



        gameManager = new GameManager();
        coin = new Coin();
        sound = new SoundMaker();

        gameManager.setGameManager(GameManager.fromJsonToUser(bundle.getString(USER_KEY)));


        game = bundle.getString("game");
        if(game.equals("buttons")){
            setContentView(R.layout.activity_game_arrow);
            findViews();
            init();
            initPlayerButtons();
            buttonsListener();
            runProg();
        }else{
            setContentView(R.layout.activity_game_sensor);
            sensorMaker = new SensorMaker();
            sensorGame = true;
            initSensors();
            findViews();
            init();
            runProg();
            joystickImage = findViewById(R.id.game_IMG_joystick);
        }
    }
    private void setLocation() {
        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = location -> handleUserLocation();

    }


    private void handleUserLocation(){
        double lon = 0;
        double lat = 0;

        int fineLocationStatus = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationStatus = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if ((fineLocationStatus != PackageManager.PERMISSION_GRANTED) &&
                (coarseLocationStatus != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    101);
        }

        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isGPSEnabled) {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        }
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            lon = location.getLongitude();
            lat = location.getLatitude();
        }
        String addressName = "LOCATION";


        /*
        i try to run this code below for 3 days, it seem to be a problem

         */
//        try {
//            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//            List<Address> addList = geocoder.getFromLocation(lat, lon, 1);
//            Address add = addList.get(0);
//            addressName = add.getLocality();
//        }catch (IOException e){
//            addressName = "LOCATION";
//        }

        gameManager.setUserLocation(lon, lat, addressName);
    }


    public void initSensors() {
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorMaker.setSensorManager(sensorManager);
        sensorMaker.initSensor();
    }

    private void initPlayerButtons() {
        game_BTN_arrows = new MaterialButton[]{
                findViewById(R.id.game_BTN_up),    // 0
                findViewById(R.id.game_BTN_left),  // 1
                findViewById(R.id.game_BTN_right), // 2
                findViewById(R.id.game_BTN_down)   // 3
        };
    }



    private void init(){
        setLocation();
        textView.setText(bundle.getString("Score"));
        // to place the character
        startPos();

    }

    private void startPos(){
        locRowAnimal = 5;
        locColAnimal = 2;
        locRowHunter = 0;
        locColHunter = 2;
        matrixImage[locRowAnimal][locColAnimal].setVisibility(View.VISIBLE);
        matrixImage[locRowHunter][locColHunter].setImageResource(R.drawable.ic_hunter);
        matrixImage[locRowHunter][locColHunter].setVisibility(View.VISIBLE);

        // place the coin in random position
        startCoinPos();
    }
    private void startCoinPos(){
        // place the coin in random spot
        this.coin.placeTheCoin(numOfRow,numOfCol);
        matrixImage[coin.getLocRowCoin()][coin.getLocColCoin()].setImageResource(R.drawable.ic_coin);
        matrixImage[coin.getLocRowCoin()][coin.getLocColCoin()].setVisibility(View.VISIBLE);
    }

    private void hunterOnCoin(){
        // if the coin is in the same spot as the hunter , change the spot again
        if(coin.IsHunterOnCoin(locRowHunter,locColHunter)){
            //reduce the score by 5 when the hunter take the coin
            gameManager.ScoreReduceByFive();

            matrixImage[coin.getLocRowCoin()][coin.getLocColCoin()].setImageResource(R.drawable.ic_hunter);
            matrixImage[coin.getLocRowCoin()][coin.getLocColCoin()].setVisibility(View.VISIBLE);
            startCoinPos();
        }
    }

    private void lionEat(){
        if (coin.IsLionOnCoin(locRowAnimal,locColAnimal)){
            //make sound
            sound.setMpAndPlay((ContextWrapper) getApplicationContext(),R.raw.lion_eat);

            //get +10 to score
            gameManager.ScoreUpByTen();
            //replace the invisible coin image with the invisible lion image
            matrixImage[coin.getLocRowCoin()][coin.getLocColCoin()].setImageResource(R.drawable.ic_lion);

            //always keep coin in the map
            startCoinPos();
        }
    }


    private boolean checkIfOk(int direction, int character) {
        switch (direction) {
            case 0: //up
                return ((character == animalDir) ? locRowAnimal : locRowHunter) > 0;
            case 1: //left
                return ((character == animalDir) ? locColAnimal : locColHunter) > 0;
            case 2: //right
                return ((character == animalDir) ? locColAnimal : locColHunter) < (numOfCol - 1);
            case 3: //down
                return ((character == animalDir) ? locRowAnimal : locRowHunter) < (numOfRow - 1);

        }
        return false;
    }

    private void runProg() {
            TickTok();
            randomHunterPos();
    }

    private void buttonsListener() {
            for (int i = 0; i < game_BTN_arrows.length; ++i) {
                int finalI = i;
                game_BTN_arrows[i].setOnClickListener(v -> animalMove(finalI));
            }

    }

    private void TickTok() {
            handler = new Handler();
            handler.postDelayed(() -> {
                if (direction != -1) {
                    if (game.equals("buttons")){
                        animalMove(direction);
                    }
                }
                gameManager.ScoreUpByOne();
                textView.setText(String.valueOf(gameManager.getScore()));
                if (gameManager.getLives() > 0){
                    runProg();
                }

            }, 1000);

    }

    private void StopTickTok() {
        handler.removeCallbacksAndMessages(null);
    }


    private void animalMove(int i){
            switch(i){
                case 0: // up
                    if (checkIfOk(0, animalDir)){
                        matrixImage[locRowAnimal][locColAnimal].setVisibility(View.INVISIBLE);
                        locRowAnimal--;
                        direction = 0;
                        matrixImage[locRowAnimal][locColAnimal].setVisibility(View.VISIBLE);
                        getHit();
                        lionEat();
                    }

                    break;

                case 1: // left
                    if (checkIfOk(1, animalDir)){
                        matrixImage[locRowAnimal][locColAnimal].setVisibility(View.INVISIBLE);
                        locColAnimal--;
                        direction = 1;
                        matrixImage[locRowAnimal][locColAnimal].setVisibility(View.VISIBLE);
                        getHit();
                        lionEat();

                    }

                    break;

                case 2: // right
                    if (checkIfOk(2, animalDir)){
                        matrixImage[locRowAnimal][locColAnimal].setVisibility(View.INVISIBLE);
                        locColAnimal++;
                        direction = 2;
                        matrixImage[locRowAnimal][locColAnimal].setVisibility(View.VISIBLE);
                        getHit();
                        lionEat();

                    }

                    break;

                case 3: // down
                    if (checkIfOk(3, animalDir)){
                        matrixImage[locRowAnimal][locColAnimal].setVisibility(View.INVISIBLE);
                        locRowAnimal++;
                        direction = 3;
                        matrixImage[locRowAnimal][locColAnimal].setVisibility(View.VISIBLE);
                        getHit();
                        lionEat();

                    }
                    break;

            }


    }

    private void lifeSetter() {
        gameManager.reduceLives();
        if (gameManager.getLives() == 2)
            game_IMG_hearts[2].setVisibility(View.INVISIBLE);

        else if (gameManager.getLives() == 1)
            game_IMG_hearts[1].setVisibility(View.INVISIBLE);

        else if (gameManager.getLives() == 0) {
            game_IMG_hearts[0].setVisibility(View.INVISIBLE);

            Toast.makeText(getApplicationContext(),
                    gameManager.getPlayerName() +" You Got " + gameManager.getScore() + " Points!",
                    Toast.LENGTH_LONG).show();
            // wait 1 second to see the toast
            //handler = new Handler(Looper.getMainLooper());
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() { }
            }, 1000);
            endGame();
        }
    }

    private void getHit(){
        if ((locRowHunter == locRowAnimal) && (locColHunter == locColAnimal)){
            // sound
            sound.setMpAndPlay((ContextWrapper) getApplicationContext(),R.raw.gun_shot);

            direction = -1;
            lifeSetter();
            if (gameManager.getLives() > 0){
                matrixImage[locRowHunter][locColHunter].setImageResource(R.drawable.ic_lion);
                matrixImage[locRowHunter][locColHunter].setVisibility(View.INVISIBLE);

                //for the coin
                matrixImage[coin.getLocRowCoin()][coin.getLocColCoin()].setImageResource(R.drawable.ic_lion);
                matrixImage[coin.getLocRowCoin()][coin.getLocColCoin()].setVisibility(View.INVISIBLE);
                startPos();
            }
        }
    }


    private void randomHunterPos(){
        // define the range
        int max = 3;
        int min = 0;
        int range = max - min + 1;

         int rand = (int)(Math.random() * range) + min;

         switch (rand){
             case 0: // up
                 if (checkIfOk(0,1)){
                     matrixImage[locRowHunter][locColHunter].setVisibility(View.INVISIBLE);
                     matrixImage[locRowHunter][locColHunter].setImageResource(R.drawable.ic_lion);
                     locRowHunter--;
                     matrixImage[locRowHunter][locColHunter].setImageResource(R.drawable.ic_hunter);
                     matrixImage[locRowHunter][locColHunter].setVisibility(View.VISIBLE);
                     getHit();
                     hunterOnCoin();
                 }

                 break;
             case 1: // left
                 if (checkIfOk(1,1)){
                     matrixImage[locRowHunter][locColHunter].setVisibility(View.INVISIBLE);
                     matrixImage[locRowHunter][locColHunter].setImageResource(R.drawable.ic_lion);
                     locColHunter--;
                     matrixImage[locRowHunter][locColHunter].setImageResource(R.drawable.ic_hunter);
                     matrixImage[locRowHunter][locColHunter].setVisibility(View.VISIBLE);
                     getHit();
                     hunterOnCoin();
                 }
                 break;
             case 2: // right
                 if (checkIfOk(2,1)){
                     matrixImage[locRowHunter][locColHunter].setVisibility(View.INVISIBLE);
                     matrixImage[locRowHunter][locColHunter].setImageResource(R.drawable.ic_lion);
                     locColHunter++;
                     matrixImage[locRowHunter][locColHunter].setImageResource(R.drawable.ic_hunter);
                     matrixImage[locRowHunter][locColHunter].setVisibility(View.VISIBLE);
                     getHit();
                     hunterOnCoin();
                 }
                 break;

             case 3: // down
                 if (checkIfOk(3,1)){
                     matrixImage[locRowHunter][locColHunter].setVisibility(View.INVISIBLE);
                     matrixImage[locRowHunter][locColHunter].setImageResource(R.drawable.ic_lion);
                     locRowHunter++;
                     matrixImage[locRowHunter][locColHunter].setImageResource(R.drawable.ic_hunter);
                     matrixImage[locRowHunter][locColHunter].setVisibility(View.VISIBLE);
                     getHit();
                     hunterOnCoin();
                 }
                 break;
         }
        }

    private void endGame() {
        //save the location
        handleUserLocation();
        //stop the clock
        StopTickTok();
        //save all the data
        DataManager.getInstance().updateUserData(gameManager);
        ///todo i need to do finish
        finish();
        replaceActivity();
        // todo toast about record
    }

    private void replaceActivity() {
        Intent intent = new Intent(this, Activity_Top_Ten.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private SensorEventListener accSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];

            if (x < -5){// move right
                animalMove(2);
            }
            else if (x > 5){// move left
                animalMove(1);
            }
            else if (y < -3){// move up
                animalMove(0);
            }
            else if (y > 5){// move down
                animalMove(3);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            // empty
        }
    };

    protected void onResume() {
        super.onResume();
        if (sensorGame){
            sensorMaker.getSensorManager().registerListener(accSensorEventListener, sensorMaker.getAccSensor(), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorGame){
            sensorMaker.getSensorManager().unregisterListener(accSensorEventListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sensorGame){
            //endGame();
        }
    }



    private void findViews() {

        matrixImage = new ImageView[][]{
                {findViewById(R.id.image00), findViewById(R.id.image01), findViewById(R.id.image02),findViewById(R.id.image03),findViewById(R.id.image04)},
                {findViewById(R.id.image10), findViewById(R.id.image11), findViewById(R.id.image12),findViewById(R.id.image13),findViewById(R.id.image14)},
                {findViewById(R.id.image20), findViewById(R.id.image21), findViewById(R.id.image22),findViewById(R.id.image23),findViewById(R.id.image24)},
                {findViewById(R.id.image30), findViewById(R.id.image31), findViewById(R.id.image32),findViewById(R.id.image33),findViewById(R.id.image34)},
                {findViewById(R.id.image40), findViewById(R.id.image41), findViewById(R.id.image42),findViewById(R.id.image43),findViewById(R.id.image44)},
                {findViewById(R.id.image50), findViewById(R.id.image51), findViewById(R.id.image52),findViewById(R.id.image53),findViewById(R.id.image54)}};

        textView = findViewById(R.id.textView);


        game_IMG_hearts = new ImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3)
        };


    }
}

