package com.example.fitfeast;

public class UserProfile {
    public String uid;
    public String email;
    public boolean onboardingCompleted;

    public UserProfile() {}

    public UserProfile(String uid, String email) {
        this.uid = uid;
        this.email = email;
        this.onboardingCompleted = false;
    }
}
