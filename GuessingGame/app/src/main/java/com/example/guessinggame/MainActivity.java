package com.example.guessinggame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String playerName;
    private TextView welcomeText;
    private EditText playerNameInput;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        welcomeText = findViewById(R.id.welcomeText);
        playerNameInput = findViewById(R.id.playerName);
        startButton = findViewById(R.id.startButton);


        SharedPreferences preferences = getSharedPreferences("GuessingGamePrefs", MODE_PRIVATE);
        playerName = preferences.getString("playerName", "");
        if (!playerName.isEmpty()) {
            playerNameInput.setText(playerName);
        }


        startButton.setOnClickListener(view -> {
            playerName = playerNameInput.getText().toString();
            if (!playerName.isEmpty()) {
                startGame();
            }
        });
    }


    private void startGame() {

        SharedPreferences.Editor editor = getSharedPreferences("GuessingGamePrefs", MODE_PRIVATE).edit();
        editor.putString("playerName", playerName);
        editor.apply();


        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("playerName", playerName);
        startActivity(intent);
    }
}
