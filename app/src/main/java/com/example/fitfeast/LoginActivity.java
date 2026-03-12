package com.example.fitfeast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    EditText emailEt, passwordEt;
    Button loginBtn, googleBtn;
    TextView tvSignup;

    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        // 🔹 AUTO LOGIN CHECK
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        // UI binding
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        loginBtn = findViewById(R.id.loginBtn);
        googleBtn = findViewById(R.id.googleBtn);
        tvSignup = findViewById(R.id.tvSignup);

        // 🔹 Google Sign-In config
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // 🔹 Email login
        loginBtn.setOnClickListener(v -> emailLogin());

        // 🔹 Google login
        googleBtn.setOnClickListener(v -> googleLogin());

        // 🔹 Signup redirect
        tvSignup.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, SignupActivity.class))
        );
    }

    // ================= EMAIL LOGIN =================
    private void emailLogin() {

        String email = emailEt.getText().toString().trim();
        String pass = passwordEt.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Enter email & password", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, OnboardingActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    // ================= GOOGLE LOGIN =================
    private void googleLogin() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        launcher.launch(signInIntent);
    }

    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            try {
                                GoogleSignInAccount account =
                                        GoogleSignIn.getSignedInAccountFromIntent(result.getData())
                                                .getResult(ApiException.class);

                                firebaseAuthWithGoogle(account.getIdToken());

                            } catch (ApiException e) {
                                Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential =
                GoogleAuthProvider.getCredential(idToken, null);

        auth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(this, "Google login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, OnboardingActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
