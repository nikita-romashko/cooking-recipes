package com.example.cookingrecipes;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView textViewName;
    private TextView textViewDetails;

    private Button buttonPlayRecipe;

    private MediaPlayer recipeVoicePlayer;
    private String currentRecipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewName = findViewById(R.id.textViewName);
        textViewDetails = findViewById(R.id.textViewDetails);
        buttonPlayRecipe = findViewById(R.id.buttonPlayRecipe);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString("recipe_name");
            String details = extras.getString("recipe_details");

            currentRecipeName = name;

            textViewName.setText(name);
            textViewDetails.setText(details);
        }

        String omeletteName = getString(R.string.omelette_name);

        if (omeletteName.equals(currentRecipeName)) {
            buttonPlayRecipe.setVisibility(View.VISIBLE);
        } else {
            buttonPlayRecipe.setVisibility(View.GONE);
        }

        buttonPlayRecipe.setOnClickListener(v -> {
            try {
                if (recipeVoicePlayer != null) {
                    recipeVoicePlayer.release();
                    recipeVoicePlayer = null;
                }

                recipeVoicePlayer = MediaPlayer.create(this, R.raw.omlet);

                if (recipeVoicePlayer != null) {
                    recipeVoicePlayer.start();

                    buttonPlayRecipe.setText(R.string.playing);

                    recipeVoicePlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            buttonPlayRecipe.setText(R.string.play_recipe_voice);
                            mp.release();
                            recipeVoicePlayer = null;
                        }
                    });
                } else {
                    Toast.makeText(this, R.string.voice_file_not_found, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, R.string.voice_playback_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPlayback();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayback();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlayback();
    }

    private void stopPlayback() {
        if (recipeVoicePlayer != null) {
            if (recipeVoicePlayer.isPlaying()) {
                recipeVoicePlayer.stop();
            }
            recipeVoicePlayer.release();
            recipeVoicePlayer = null;

            if (buttonPlayRecipe != null) {
                buttonPlayRecipe.setText(R.string.play_recipe_voice);
            }
        }
    }

}