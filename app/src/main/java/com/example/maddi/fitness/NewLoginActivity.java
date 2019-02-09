//package com.example.maddi.fitness;
//
//        import android.os.Bundle;
//        import android.support.annotation.NonNull;
//        import android.support.design.widget.Snackbar;
//        import android.support.v7.app.AppCompatActivity;
//        import android.util.Log;
//
//        import android.content.Intent;
//        import android.view.View;
//        import android.widget.Toast;
//
//        import com.google.android.gms.auth.api.signin.GoogleSignIn;
//        import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//        import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//        import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//        import com.google.android.gms.common.SignInButton;
//        import com.google.android.gms.common.api.ApiException;
//        import com.google.android.gms.tasks.OnCompleteListener;
//        import com.google.android.gms.tasks.Task;
//        import com.google.firebase.auth.AuthCredential;
//        import com.google.firebase.auth.AuthResult;
//        import com.google.firebase.auth.FirebaseAuth;
//        import com.google.firebase.auth.FirebaseUser;
//        import com.google.firebase.auth.GoogleAuthProvider;
//
//public class NewLoginActivity extends AppCompatActivity {
//    private static final String TAG = "LoginActivity";
//    private static final int REQUEST_LOGIN = 0;
//
//    SignInButton _loginButton;
//    GoogleSignInClient mGoogleSignInClient;
//    FirebaseAuth mAuth;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.google_client_id))
//                .requestEmail()
//                .build();
//
//        // Build a GoogleSignInClient with the options specified by gso.
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
//        // Initialize Firebase Auth
//        mAuth = FirebaseAuth.getInstance();
//
//        setContentView(R.layout.activity_login_new);
//
//        _loginButton = (SignInButton) findViewById(R.id.btn_login);
//
//        _loginButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                login();
//            }
//        });
//    }
//
//    public void login() {
//        Log.d(TAG, "Login");
//
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, REQUEST_LOGIN);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        // updateUI(currentUser);
//        // Proceed to this.finish() in activityresult send REQUEST_LOGGED_IN
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_LOGIN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                // Google Sign In failed
//                Log.w(TAG, "Google sign in failed", e);
//                Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            //updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            //updateUI(null);
//                        }
//                    }
//                });
//    }
//}