package com.example.cookingrecipes;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
    private VideoView videoViewRecipe;
    private MediaController mediaController;

    private RecipeTimer timer1;
    private RecipeTimer timer2;
    private RecipeTimer timer3;
    private EditText editTimer1Input;

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
        videoViewRecipe = findViewById(R.id.videoViewRecipe);

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
            videoViewRecipe.setVisibility(View.VISIBLE);

            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.omlet_video);

            videoViewRecipe.setVideoURI(videoUri);

            mediaController = new MediaController(this);
            videoViewRecipe.setMediaController(mediaController);
            mediaController.setMediaPlayer(videoViewRecipe);
            mediaController.setAnchorView(videoViewRecipe);

        } else {
            buttonPlayRecipe.setVisibility(View.GONE);
            videoViewRecipe.setVisibility(View.GONE);
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

        setupTimers();
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
        releaseTimers();
    }

    public void play(View view){
        videoViewRecipe.start();
    }
    public void pause(View view){
        videoViewRecipe.pause();
    }
    public void stop(View view){
        videoViewRecipe.stopPlayback();
        videoViewRecipe.resume();
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

    private void setupTimers() {
        TextView timer1Display = findViewById(R.id.textTimer1Display);
        TextView timer2Display = findViewById(R.id.textTimer2Display);
        TextView timer3Display = findViewById(R.id.textTimer3Display);

        editTimer1Input = findViewById(R.id.editTimer1Input);

        timer1 = new RecipeTimer(this, timer1Display, R.raw.timer1_sound, 5 * 60 * 1000L);
        timer2 = new RecipeTimer(this, timer2Display, R.raw.timer2_sound, 3 * 60 * 1000L);
        timer3 = new RecipeTimer(this, timer3Display, R.raw.timer3_sound, 90 * 1000L);

        Button timer1Start = findViewById(R.id.buttonTimer1Start);
        Button timer1Pause = findViewById(R.id.buttonTimer1Pause);
        Button timer1Reset = findViewById(R.id.buttonTimer1Reset);
        Button timer2Start = findViewById(R.id.buttonTimer2Start);
        Button timer2Pause = findViewById(R.id.buttonTimer2Pause);
        Button timer2Reset = findViewById(R.id.buttonTimer2Reset);
        Button timer3Start = findViewById(R.id.buttonTimer3Start);
        Button timer3Pause = findViewById(R.id.buttonTimer3Pause);
        Button timer3Reset = findViewById(R.id.buttonTimer3Reset);

        timer1Start.setOnClickListener(v -> {
            if (!applyTimer1Input(true)) {
                return;
            }
            timer1.start();
        });
        timer1Pause.setOnClickListener(v -> timer1.pause());
        timer1Reset.setOnClickListener(v -> {
            applyTimer1Input(true);
            timer1.reset();
        });

        timer2Start.setOnClickListener(v -> timer2.start());
        timer2Pause.setOnClickListener(v -> timer2.pause());
        timer2Reset.setOnClickListener(v -> timer2.reset());

        timer3Start.setOnClickListener(v -> timer3.start());
        timer3Pause.setOnClickListener(v -> timer3.pause());
        timer3Reset.setOnClickListener(v -> timer3.reset());
    }

    private boolean applyTimer1Input(boolean showError) {
        if (editTimer1Input == null || timer1 == null) {
            return true;
        }
        if (!timer1.isAtInitial()) {
            return true;
        }
        String input = editTimer1Input.getText() != null ? editTimer1Input.getText().toString().trim() : "";
        if (input.isEmpty()) {
            return true;
        }

        Long parsed = parseTimeToMillis(input);
        if (parsed == null) {
            if (showError) {
                Toast.makeText(this, R.string.timer_invalid_format, Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        timer1.setInitialTime(parsed);
        return true;
    }

    private Long parseTimeToMillis(String input) {
        String[] parts = input.split(":");
        if (parts.length < 2 || parts.length > 3) {
            return null;
        }

        try {
            long hours = 0L;
            long minutes;
            long seconds;

            if (parts.length == 2) {
                minutes = Long.parseLong(parts[0]);
                seconds = Long.parseLong(parts[1]);
            } else {
                hours = Long.parseLong(parts[0]);
                minutes = Long.parseLong(parts[1]);
                seconds = Long.parseLong(parts[2]);
            }

            if (minutes < 0 || seconds < 0 || minutes >= 60 || seconds >= 60 || hours < 0) {
                return null;
            }

            long totalSeconds = hours * 3600L + minutes * 60L + seconds;
            if (totalSeconds <= 0L) {
                return null;
            }

            return totalSeconds * 1000L;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void releaseTimers() {
        if (timer1 != null) {
            timer1.release();
        }
        if (timer2 != null) {
            timer2.release();
        }
        if (timer3 != null) {
            timer3.release();
        }
    }

}
