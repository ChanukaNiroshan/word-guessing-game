package com.example.guessinggame;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private String playerName;
    private int score = 100;
    private String secretWord = "";
    private int attempts = 0;
    private final int maxAttempts = 10;

    private TextView scoreLabel, feedback;
    private EditText guessInput;
    private Button guessButton, clueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        playerName = getIntent().getStringExtra("playerName");


        scoreLabel = findViewById(R.id.scoreLabel);
        guessInput = findViewById(R.id.guessInput);
        guessButton = findViewById(R.id.guessButton);
        clueButton = findViewById(R.id.clueButton);
        feedback = findViewById(R.id.feedback);


        startGame();


        guessButton.setOnClickListener(view -> {
            String guess = guessInput.getText().toString();
            if (!guess.isEmpty()) {
                checkGuess(guess);
            }
        });


        clueButton.setOnClickListener(view -> {
            if (score >= 5) {
                score -= 5;  // Deduct 5 points for using a clue
                showClue();
            } else {
                feedback.setText("Not enough points for a hint!");
            }
        });
    }


    private void startGame() {
        secretWord = getSecretWord();  // Fetch word
        score = 100;
        attempts = 0;
        updateScore();
        feedback.setText("Hello, " + playerName + "! Try guessing the word.");
    }


    private String getSecretWord() {
        List<String> words = new ArrayList<>();
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.world_list);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }


    private void checkGuess(String guess) {
        attempts++;
        if (guess.equalsIgnoreCase(secretWord)) {
            feedback.setText("Congratulations " + playerName + "! You correctly guessed the word: " + secretWord + "!");
            resetGame();
        } else {
            score -= 10;
            feedback.setText("Wrong guess! Try again.");
            updateScore();
        }
        if (attempts >= maxAttempts) {
            feedback.setText("Sorry, you've used all your attempts. The word was: " + secretWord);
            resetGame();
        }
    }


    private void showClue() {
        feedback.setText("Hint: The first letter is '" + secretWord.charAt(0) + "'.");
        updateScore();
    }


    private void updateScore() {
        scoreLabel.setText("Score: " + score);
    }


    private void resetGame() {
        new android.os.Handler().postDelayed(this::startGame, 3000); // Restart after 3 seconds
    }
}
