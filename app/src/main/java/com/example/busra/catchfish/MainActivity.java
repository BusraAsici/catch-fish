package com.example.busra.catchfish;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView scoreText;
    TextView timeText;
    TextView bestScoreText;

    int score;

    Integer[] images = {
            R.drawable.fish_1,
            R.drawable.fish_2,
            R.drawable.fish_3,
            R.drawable.fish_4,
            R.drawable.fish_5,
            R.drawable.fish_6,
            R.drawable.fish_7,
            R.drawable.fish_8

    };

    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3, imageView4, imageView5,
            imageView6, imageView7, imageView8, imageView9,
            imageView10, imageView11, imageView12, imageView13,
            imageView14, imageView15, imageView16;

    Handler handler;
    Runnable runnable;

    ImageView[] imageArray;

    static int winScore;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.remove_score, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_bestScore) {
            SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("com.example.busra.catchfish", Context.MODE_PRIVATE);
            sharedPreferences.edit().remove("bestScore").apply();
            bestScoreText.setText("Best Score: " + score);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize
        scoreText = (TextView) findViewById(R.id.scoreText);
        timeText = (TextView) findViewById(R.id.timeText);
        bestScoreText = findViewById(R.id.bestScoreText);
        bestScoreText.setText("Best Score: " + score);


        timeText.setText("2232");
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        imageView6 = findViewById(R.id.imageView6);
        imageView7 = findViewById(R.id.imageView7);
        imageView8 = findViewById(R.id.imageView8);
        imageView9 = findViewById(R.id.imageView9);
        imageView10 = findViewById(R.id.imageView10);
        imageView11 = findViewById(R.id.imageView11);
        imageView12 = findViewById(R.id.imageView12);
        imageView13 = findViewById(R.id.imageView13);
        imageView14 = findViewById(R.id.imageView14);
        imageView15 = findViewById(R.id.imageView15);
        imageView16 = findViewById(R.id.imageView16);

        imageArray = new ImageView[]{imageView1, imageView2, imageView3, imageView4, imageView5,
                imageView6, imageView7, imageView8, imageView9,
                imageView10, imageView11, imageView12, imageView13,
                imageView14, imageView15, imageView16};


        hideImages();

        score = 0;

        new CountDownTimer(20000, 1000) {

            //Record the highest score with SharedPreferences
            SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("com.example.busra.catchfish", Context.MODE_PRIVATE);
            int bestScore = sharedPreferences.getInt("bestScore", 0);

            @Override
            public void onTick(long millisUntilFinished) {
                timeText.setText("Time: " + millisUntilFinished / 1000);
                bestScoreText.setText("Best Score: " + bestScore);

            }

            @Override
            public void onFinish() {
                timeText.setText("TIME OFF");

                handler.removeCallbacks(runnable);

                for (ImageView image : imageArray) {
                    image.setVisibility(View.INVISIBLE);
                }

                bestScoreText.setText("Best Score: " + bestScore);
                if (score >= bestScore) {
                    sharedPreferences.edit().putInt("bestScore", score).apply();
                    Toast.makeText(MainActivity.this, "Your score is best score! \nYou WIN :)", Toast.LENGTH_LONG).show();
                    winScore = bestScore;

                }

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Restart?");
                if (score >= bestScore) {
                    alert.setMessage("YOU WIN :)\nYour score is " + score + "\nAre you sure to restart?");
                } else {
                    alert.setMessage("Your score is " + score + "\nYou didn't win. \nAre you sure to restart?");
                }
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // restart
                        Intent intent = getIntent();
                        finish(); // finish program
                        startActivity(intent);// recall activity
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (score >= bestScore) {
                            Toast.makeText(MainActivity.this, "YOU WIN :)\nGame is finished ", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Game over!", Toast.LENGTH_LONG).show();

                        }
                        moveTaskToBack(true); //exiting the application
                    }
                });
                alert.show();

            }
        }.start();


    }

    public void increaseScore(View view) {
        ++score;
        scoreText.setText("Score: " + score);

    }

    public void hideImages() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                for (ImageView image : imageArray) {
                    image.setVisibility(View.INVISIBLE);
                }
                // we make the whole series invisible every second
                // we will find a random value and it will be randomly seen in the array
                // Fish can be seen in random places
                Random random = new Random();
                int i = random.nextInt(16);
                random = new Random();
                imageArray[i].setImageResource(images[random.nextInt(images.length)]);
                imageArray[i].setVisibility(View.VISIBLE);

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnable);


    }
}
