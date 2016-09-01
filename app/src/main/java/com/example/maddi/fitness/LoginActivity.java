package com.example.maddi.fitness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.auth.core.AuthProviderType;
import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;
import com.firebase.ui.auth.core.FirebaseLoginError;

import java.util.Map;

public class LoginActivity extends FirebaseLoginBaseActivity {

    Firebase firebaseRef;
    EditText userNameET;
    EditText passwordET;
    String mName;
    PackageInfo info;


    /* String Constants */
    private static final String FIREBASEREF = "https://healthkit.firebaseio.com";
    private static final String FIREBASE_ERROR = "Firebase Error";
    private static final String USER_ERROR = "User Error";
    private static final String LOGIN_SUCCESS = "Login Success";
    private static final String USER_CREATION_SUCCESS =  "Successfully created user";
    private static final String USER_CREATION_ERROR =  "User creation error";
    private static final String EMAIL_INVALID =  "email is invalid :";

    public static String USER_ID = "";
    public static String USER_EMAIL = "";
    public static String USER_NAME = "";
    public static float mSeries1 = 0f;
    public static float mSeries2 = 0f;
    public static float calRef = 0f;
    public static float user_fat = 0f;
    public static float user_carbs = 0f;
    public static float user_protein = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        firebaseRef = new Firebase(FIREBASEREF);
        super.onCreate(savedInstanceState);
        // Initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        CallbackManager callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        userNameET = (EditText)findViewById(R.id.edit_text_email);
        passwordET = (EditText)findViewById(R.id.edit_text_password);
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.showFirebaseLoginPrompt();
            }
        });
        Button createButton = (Button) findViewById(R.id.button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
       /* Button forgotButton = (Button) findViewById(R.id.forgot);
        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase ref = new Firebase("https://healthkit.firebaseio.com/Users");
                ref.resetPassword("email here", new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        // password reset email sent
                    }
                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // error encountered
                    }
                });
            }
        });*/
    }

    @Override
    protected void onFirebaseLoginProviderError(FirebaseLoginError firebaseLoginError) {
        Snackbar snackbar = Snackbar.
                make(userNameET, FIREBASE_ERROR + firebaseLoginError.message, Snackbar.LENGTH_SHORT);
        snackbar.show();
        resetFirebaseLoginPrompt();
    }

    @Override
    protected void onFirebaseLoginUserError(FirebaseLoginError firebaseLoginError) {
        Snackbar snackbar = Snackbar
                .make(userNameET, USER_ERROR + firebaseLoginError.message, Snackbar.LENGTH_SHORT);
        snackbar.show();
        resetFirebaseLoginPrompt();
    }

    @Override
    public Firebase getFirebaseRef() {
        return firebaseRef;
    }

    @Override
    public void onFirebaseLoggedIn(AuthData authData) {
        String ab = authData.getUid();
        String em = (String) authData.getProviderData().get("email");
        USER_ID = ab;
        USER_EMAIL = em;
        switch (authData.getProvider()) {
            case "password":
                mName = (String) authData.getProviderData().get("email");
                break;
            default:
                mName = (String) authData.getProviderData().get("displayName");
                break;
        }

        //Toast.makeText(getApplicationContext(), ab, Toast.LENGTH_SHORT).show();
        // Starting Activity only first time
        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

        //  If the activity has never started before...
        if (isFirstStart) {
            //  Launch app intro
            Intent i = new Intent(LoginActivity.this, AppIntroActivity.class);
            startActivity(i);

            //  Make a new preferences editor
            SharedPreferences.Editor e = getPrefs.edit();

            //  Edit preference to make it false because we don't want this to run again
            e.putBoolean("firstStart", false);

            //  Apply changes
            e.apply();

            // Create User Info
            Firebase ref = new Firebase("https://healthkit.firebaseio.com/Users");
            // if(ref.child(ab.toString())== null) {
            ref.child(ab);
            ref.child(ab).child("name").setValue("");
            ref.child(ab).child("phone").setValue("");
            // ref.child(ab).child("email").setValue("");
            ref.child(ab).child("gender").setValue("");
            ref.child(ab).child("age").setValue("0");
            ref.child(ab).child("height").setValue("0");
            ref.child(ab).child("weight").setValue("0");
            ref.child(ab).child("stepgoal").setValue("0");
            ref.child(ab).child("caloriegoal").setValue("0");
            //}
            // Create Step Info
            Firebase sref = new Firebase("https://healthkit.firebaseio.com/Steps");
            //if(sref.child(ab.toString())== null) {
            sref.child(ab);
            sref.child(ab).child("totalsteps").setValue("0");
            //}
            // Create Calorie Info
            Firebase cref = new Firebase("https://healthkit.firebaseio.com/Calories");
            //if(cref.child(ab.toString())== null) {
            cref.child(ab);
            cref.child(ab).child("totalcalories").setValue("0");
            cref.child(ab).child("totalfat").setValue("0");
            cref.child(ab).child("totalcarbs").setValue("0");
            cref.child(ab).child("totalprotein").setValue("0");
            //}
        }
        else {
            Firebase mref = new Firebase("https://healthkit.firebaseio.com/Users/"+LoginActivity.USER_ID);
            final Firebase[] msref = {mref.child("stepgoal")};
            msref[0].addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println(dataSnapshot.getValue());
                    Log.d("COming", "in in");
                    mSeries1 = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
                    Log.d("mSeries", (String.valueOf(mSeries1)));
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            final Firebase[] mcref = {mref.child("caloriegoal")};
            mcref[0].addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println(dataSnapshot.getValue());
                    Log.d("COming", "in in");
                    mSeries2 = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
                    Log.d("mSeries", (String.valueOf(mSeries2)));
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            final Firebase[] ncref = {mref.child("name")};
            ncref[0].addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println(dataSnapshot.getValue());
                    Log.d("COming", "in in");
                    USER_NAME = (String.valueOf(dataSnapshot.getValue()));
                    Log.d("mSeries", (String.valueOf(mSeries2)));
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            Firebase calorieref = new Firebase("https://healthkit.firebaseio.com/Calories/"+LoginActivity.USER_ID);
            final Firebase[] totcalref = {calorieref.child("totalcalories")};
            totcalref[0].addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println(dataSnapshot.getValue());
                    Log.d("COming", "in in");
                    calRef = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
                    Log.d("User Calories", (String.valueOf(calRef)));
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
                    user_fat = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
                    Log.d("User Calories", (String.valueOf(calRef)));
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
                    user_carbs = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
                    Log.d("User Calories", (String.valueOf(calRef)));
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
                    user_protein = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
                    Log.d("User Calories", (String.valueOf(calRef)));
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(myIntent);
        }
    }

    @Override
    public void onFirebaseLoggedOut() {
        //Toast.makeText(getApplicationContext(), "LOGOUT SUCCESS", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // All providers are optional! Remove any you don't want.
        setEnabledAuthProvider(AuthProviderType.PASSWORD);
        setEnabledAuthProvider(AuthProviderType.GOOGLE);
        setEnabledAuthProvider(AuthProviderType.FACEBOOK);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    // Validate email address for new accounts.
    private boolean isEmailValid(String email) {
        boolean isGoodEmail = (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            userNameET.setError(EMAIL_INVALID + email);
            return false;
        }
        return true;
    }

    // create a new user in Firebase
    public void createUser() {
        if(userNameET.getText() == null ||  !isEmailValid(userNameET.getText().toString())) {
            return;
        }
        firebaseRef.createUser(userNameET.getText().toString(), passwordET.getText().toString(),
                new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        Snackbar snackbar = Snackbar.make(userNameET, USER_CREATION_SUCCESS, Snackbar.LENGTH_SHORT);
                        snackbar.show();

                        // Firebase ref = new Firebase("https://healthkit.firebaseio.com/Users");
                        // ref.child().setValue("3157447509");
                        // Firebase cref = ref.child("Age");

                    }
                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Snackbar snackbar = Snackbar.make(userNameET, USER_CREATION_ERROR, Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                });
    }
}
