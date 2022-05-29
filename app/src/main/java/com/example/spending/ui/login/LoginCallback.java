package com.example.spending.ui.login;

import com.google.firebase.auth.FirebaseUser;

public interface LoginCallback {
    void onLoginSuccess(FirebaseUser user);
    void onLoginFailure(String error);
}
