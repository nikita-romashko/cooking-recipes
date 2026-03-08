package com.example.cookingrecipes;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class RecipeTimer {

    private CountDownTimer timer;
    private long remainingTime;
    private long initialTime;
    private boolean isRunning;

    private final Context context;
    private final TextView display;
    private final int soundResId;
    private final int normalColor;

    public RecipeTimer(Context context, TextView display, int soundResId, long initialTime) {
        this.context = context;
        this.display = display;
        this.soundResId = soundResId;
        this.initialTime = initialTime;
        this.remainingTime = initialTime;
        this.normalColor = display.getCurrentTextColor();
        updateDisplay(initialTime);
    }

    public void start() {
        if (isRunning) {
            return;
        }

        timer = new CountDownTimer(remainingTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                updateDisplay(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                remainingTime = 0L;
                isRunning = false;
                updateDisplay(0L);
                playSound();
            }
        };
        timer.start();
        isRunning = true;
    }

    public void pause() {
        if (!isRunning) {
            return;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        isRunning = false;
    }

    public void reset() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        isRunning = false;
        remainingTime = initialTime;
        updateDisplay(remainingTime);
    }

    public void setInitialTime(long initialTime) {
        this.initialTime = initialTime;
        if (!isRunning) {
            remainingTime = initialTime;
            updateDisplay(remainingTime);
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isAtInitial() {
        return remainingTime == initialTime;
    }

    public void release() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        isRunning = false;
    }

    private void playSound() {
        MediaPlayer player = MediaPlayer.create(context, soundResId);
        if (player == null) {
            return;
        }
        player.setOnCompletionListener(MediaPlayer::release);
        player.start();
    }

    private void updateDisplay(long millisUntilFinished) {
        display.setText(formatTime(millisUntilFinished));
        if (millisUntilFinished < 60000L) {
            display.setTextColor(Color.RED);
        } else {
            display.setTextColor(normalColor);
        }
    }

    private String formatTime(long millisUntilFinished) {
        NumberFormat format = new DecimalFormat("00");
        long hour = (millisUntilFinished / 3600000) % 24;
        long min = (millisUntilFinished / 60000) % 60;
        long sec = (millisUntilFinished / 1000) % 60;
        return format.format(hour) + ":" + format.format(min) + ":" + format.format(sec);
    }
}
