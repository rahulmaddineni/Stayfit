package com.example.maddi.fitness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//import com.firebase.client.DataSnapshot;
//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseError;
//import com.firebase.client.ValueEventListener;

/**
 * Created by sunny on 23-Apr-16.
 */

public class SetGoalActivity extends AppCompatActivity {
    public static float mSeries = 0f;
    public static float mSeries1 = 0f;
    float go;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setgoal);
//        final Firebase ref = new Firebase("https://healthkit.firebaseio.com/Users/"+LoginActivity.USER_ID);

        final EditText stepgoal = (EditText) findViewById(R.id.et1);
        final EditText caloriegoal = (EditText) findViewById(R.id.et2);

        Button setgoal = (Button) findViewById(R.id.setgoal);
        setgoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepgoal.getText().toString().length() == 0) {
                    stepgoal.setError("Set Steps Goal");
                    return;
                } else if (caloriegoal.getText().toString().length() == 0) {
                    caloriegoal.setError("Set Calorie Goal!");
                    return;
                }

//                ref.child("stepgoal").setValue(stepgoal.getText().toString());
//                ref.child("caloriegoal").setValue(caloriegoal.getText().toString());
//                final Firebase[] sref = {ref.child("stepgoal")};
//                sref[0].addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        System.out.println(dataSnapshot.getValue());
//                        Log.d("COming", "in in");
//                        mSeries = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
//                        Log.d("mSeries", (String.valueOf(mSeries)));
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//
//                    }
//                });
//                final Firebase[] cref = {ref.child("caloriegoal")};
//                cref[0].addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        System.out.println(dataSnapshot.getValue());
//                        Log.d("COming", "in in");
//                        mSeries1 = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
//                        Log.d("mSeries1", (String.valueOf(mSeries1)));
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//
//                    }
//                });
                Intent myIntent = new Intent(SetGoalActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

      /*  final Firebase[] cref = {ref.child("stepgoal")};
        cref[0].addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());
                go = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));

                Log.d("Step Goal to set", String.valueOf(STEP_GOAL));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/
    }
}
