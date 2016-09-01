package com.example.maddi.fitness;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by maddi on 3/21/2016.
 */
public class Food_RecyclerFrag_Main extends AppCompatActivity {
    //private boolean mSidePanel;
    private Toolbar mToolbar;
    private android.support.v7.app.ActionBar mActionBar;

    public static float calRef1 = 0f;
    public static float user_fat1 = 0f;
    public static float user_carbs1 = 0f;
    public static float user_protein1 = 0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_frag_change_main);
        //Load common fragment
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rcfrag_main, Food_RecyclerView_Main.newInstance()).commit();
        }
        mToolbar = (Toolbar) findViewById(R.id.recycler_toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

        Firebase calorieref = new Firebase("https://healthkit.firebaseio.com/Calories/"+LoginActivity.USER_ID);
        final Firebase[] totcalref = {calorieref.child("totalcalories")};
        totcalref[0].addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());
                Log.d("COming", "in in");
                calRef1 = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
                Log.d("FoodRecy Cal", (String.valueOf(calRef1)));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        final Firebase[] totcalfat = {calorieref.child("totalfat")};
        totcalfat[0].addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());
                Log.d("COming", "in in");
                user_fat1 = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
                Log.d("FoodRecy", (String.valueOf(user_fat1)));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        final Firebase[] totcalcarbs = {calorieref.child("totalcarbs")};
        totcalcarbs[0].addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());
                Log.d("COming", "in in");
                user_carbs1 = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
                Log.d("FoodRecy Carbs", (String.valueOf(user_carbs1)));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        final Firebase[] totcalprotein = {calorieref.child("totalprotein")};
        totcalprotein[0].addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());
                Log.d("COming", "in in");
                user_protein1 = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
                Log.d("FoodRecy Protein", (String.valueOf(user_protein1)));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
