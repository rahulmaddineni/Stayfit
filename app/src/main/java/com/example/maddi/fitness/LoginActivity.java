package com.example.maddi.fitness;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 0;
    private static final String TAG = "LoginActivity";
    public static String USER_ID = "";
    public static String USER_EMAIL = "";
    public static String USER_NAME = "";
    public static float mSeries1 = 0f;
    public static float mSeries2 = 0f;
    public static float calRef = 0f;
    public static float user_fat = 0f;
    public static float user_carbs = 0f;
    public static float user_protein = 0f;
    // Authentication providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build());
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (String provider : AuthUI.SUPPORTED_PROVIDERS) {
            Log.v(this.getClass().getName(), provider);
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                updateInfo();
            }
        };

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.stayfit)
                        .build(),
                RC_SIGN_IN);
    }


    private void startUpTasks() {
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        //  If the activity has never started before...
        if (isFirstRun) {
            //  Launch app intro
            Intent i = new Intent(LoginActivity.this, AppIntroActivity.class);
            startActivity(i);

            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isFirstRun", false).commit();

            initializeUserInfo();
        } else {
            getUserInfo();
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(myIntent);
        }
    }

    private void initializeUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();

        DatabaseReference usersRef = mDatabase.child("Users");
        DatabaseReference stepsRef = mDatabase.child("Steps");
        DatabaseReference caloriesRef = mDatabase.child("Calories");

        User newUser = new User("", "", "", 0, "", 0, 0, 0);
        usersRef.child(userId).setValue(newUser);

        Steps steps = new Steps(0);
        stepsRef.child(userId).setValue(steps);

        Calories calories = new Calories(0, 0, 0, 0);
        caloriesRef.child(userId).setValue(calories);
    }

    private DatabaseReference getUsersRef(String ref) {
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        return mDatabase.child("Users").child(userId).child(ref);
    }

    private DatabaseReference getCaloriesRef(String ref) {
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        return mDatabase.child("Calories").child(userId).child(ref);
    }

    private void getUserInfo() {
        getUsersRef("stepgoal").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSeries1 = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        getUsersRef("caloriegoal").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSeries2 = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        getUsersRef("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                USER_NAME = (String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        getCaloriesRef("totalcalories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                calRef = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        getCaloriesRef("totalfat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_fat = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        getCaloriesRef("totalcarbs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_carbs = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        getCaloriesRef("totalprotein").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_protein = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void updateInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            USER_ID = user.getUid();
            USER_EMAIL = user.getEmail();
            // Picasso.with(ActivityFUIAuth.this).load(user.getPhotoUrl()).into(imgProfile);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == Activity.RESULT_OK) {
                Log.d(this.getClass().getName(), "This user signed in with " + response.getProviderType());
                startUpTasks();
                updateInfo();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, "Signin cancelled", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "Check network connection and try again", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(this, "Unexpected Error, we are trying to resolve the issue. Please check back soon", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }
}
