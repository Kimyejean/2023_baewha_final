/*
package com.example.refrigerator_2023final;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessaging";

    */
/**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     *//*

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this app's subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // Implement the logic to send the registration token to your server if needed.
        // This method will be called when a new token is generated.
    }
}
*/
