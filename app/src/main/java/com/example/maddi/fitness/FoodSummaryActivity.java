package com.example.maddi.fitness;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;

import com.natasa.progressviews.CircleProgressBar;

/**
 * Created by maddi on 4/20/2016.
 */
public class FoodSummaryActivity extends AppCompatActivity {
    float food_fat;
    float food_carbs;
    float food_protein;
    float max_fat = 70f;
    float max_carbs = 300f;
    float max_protein = 180f;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodsummary);
        final CircleProgressBar fats = findViewById(R.id.fats_progress);
        final CircleProgressBar carbs = findViewById(R.id.carbs_progress);
        final CircleProgressBar protein = findViewById(R.id.protein_progress);

        food_carbs = Food_MyRecyclerViewAdapter.totalcarbs;
        food_fat = Food_MyRecyclerViewAdapter.totalfat;
        food_protein = Food_MyRecyclerViewAdapter.totalprotein;
        Log.d("Food Summary", String.valueOf(food_carbs) + String.valueOf(food_fat) + String.valueOf(food_protein));

        // Animation
        TranslateAnimation translation;
        translation = new TranslateAnimation(0f, 0F, 0f, 180);
        translation.setStartOffset(100);
        translation.setDuration(2000);
        translation.setFillAfter(true);
        translation.setInterpolator(new BounceInterpolator());

        TranslateAnimation translation1;
        translation1 = new TranslateAnimation(0f, 0F, 0f, 370);
        translation1.setStartOffset(100);
        translation1.setDuration(2000);
        translation1.setFillAfter(true);
        translation1.setInterpolator(new BounceInterpolator());

        // Fats Progress Bar
        if (food_fat > 0) {
            fats.setProgress((100 * (food_fat)) / max_fat);
        } else
            fats.setProgress((100 * (LoginActivity.user_fat)) / max_fat);
        fats.setWidthProgressBackground(25);
        fats.setWidthProgressBarLine(25);
        if (food_fat > 0) {
            fats.setText(String.valueOf(food_fat));
        } else {
            fats.setText(String.valueOf(LoginActivity.user_fat));
        }
        fats.setTextSize(35);
        fats.setBackgroundColor(Color.LTGRAY);
        fats.setRoundEdgeProgress(true);
        fats.startAnimation(translation);

        // Carbs Progress Bar
        if (food_carbs > 0) {
            carbs.setProgress((100 * (food_carbs)) / max_carbs);
        } else
            carbs.setProgress((100 * (LoginActivity.user_carbs)) / max_carbs);
        carbs.startAnimation(translation);
        carbs.setWidthProgressBackground(25);
        carbs.setWidthProgressBarLine(25);
        if (food_carbs > 0) {
            carbs.setText(String.valueOf(food_carbs));
        } else {
            carbs.setText(String.valueOf(LoginActivity.user_carbs));
        }
        carbs.setTextSize(35);
        carbs.setBackgroundColor(Color.LTGRAY);
        carbs.setRoundEdgeProgress(true);

        // protein Progress Bar
        if (food_protein > 0) {
            protein.setProgress((100 * (food_protein)) / max_protein);
        } else
            protein.setProgress((100 * (LoginActivity.user_protein)) / max_protein);
        protein.setWidthProgressBackground(25);
        protein.setWidthProgressBarLine(25);
        if (food_protein > 0) {
            protein.setText(String.valueOf(food_protein));
        } else {
            protein.setText(String.valueOf(LoginActivity.user_protein));
        }
        protein.setTextSize(35);
        protein.setBackgroundColor(Color.LTGRAY);
        protein.setRoundEdgeProgress(true);
        protein.setAnimation(translation1);

    }

    private int getDisplayHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
}
