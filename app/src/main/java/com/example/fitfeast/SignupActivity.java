package com.example.fitfeast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignupActivity extends AppCompatActivity {

    EditText emailEt, passwordEt;
    Button signupBtn, googleSignupBtn;

    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        signupBtn = findViewById(R.id.signupBtn);
        googleSignupBtn = findViewById(R.id.googleSignupBtn);

        auth = FirebaseAuth.getInstance();

        // Google Sign-In config
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        signupBtn.setOnClickListener(v -> signupWithEmail());
        googleSignupBtn.setOnClickListener(v -> signupWithGoogle());
    }

    // -------- EMAIL SIGNUP --------
    private void signupWithEmail() {

        String email = emailEt.getText().toString().trim();
        String pass = passwordEt.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pass.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, OnboardingActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    // -------- GOOGLE SIGNUP --------
    private void signupWithGoogle() {
        Intent intent = googleSignInClient.getSignInIntent();
        launcher.launch(intent);
    }

    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    try {
                        GoogleSignInAccount account =
                                GoogleSignIn.getSignedInAccountFromIntent(result.getData())
                                        .getResult(ApiException.class);

                        firebaseAuthWithGoogle(account.getIdToken());

                    } catch (ApiException e) {
                        Toast.makeText(this, "Google signup failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(this, "Google signup success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, OnboardingActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
