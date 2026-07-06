package com.example.a3dify;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.HashMap;
import java.util.Map;

/*
 * CloudDatabase
 * ══════════════════════════════════════════════════════════════
 * Manages all Firebase Realtime Database operations for 3Dify.
 *
 * Database URL: https://dify-32a7e-default-rtdb.firebaseio.com/
 *
 * Structure in the cloud:
 *
 *   3dify/
 *     users/
 *       {uid}/
 *         username: "Alex"
 *         email: "alex@email.com"
 *         createdAt: 1234567890
 *         completedCount: 3
 *     progress/
 *       {uid}/
 *         {tutorialId}: true
 *     feedback/
 *       {uid}/
 *         {pushId}/
 *           text: "Great app"
 *           rating: 5
 *           timestamp: 1234567890
 *
 * Usage:
 *   CloudDatabase cloud = CloudDatabase.getInstance();
 *   cloud.saveUserProfile(uid, username, email);
 *
 * Works alongside SQLite — SQLite is local cache,
 * Realtime Database is the cloud layer.
 * ══════════════════════════════════════════════════════════════
 */
public class CloudDatabase {

    // Your Firebase Realtime Database URL
    private static final String DATABASE_URL =
        "https://dify-32a7e-default-rtdb.firebaseio.com/";

    // Root reference to the database
    private final DatabaseReference rootRef;

    // Singleton instance
    private static CloudDatabase instance;

    public static synchronized CloudDatabase getInstance() {
        if (instance == null) {
            instance = new CloudDatabase();
        }
        return instance;
    }

    private CloudDatabase() {
        // Get the database instance using your specific URL
        FirebaseDatabase database = FirebaseDatabase.getInstance(DATABASE_URL);

        // Enable offline persistence — data is cached locally when no internet
        // This means the app works offline and syncs when reconnected
        database.setPersistenceEnabled(true);

        rootRef = database.getReference("3dify");
    }

    // ════════════════════════════════════════════════════════════
    //  USER PROFILE
    // ════════════════════════════════════════════════════════════

    /*
     * Saves a new user profile to the cloud when they register.
     * Path: 3dify/users/{uid}/
     *
     * Called from RegisterActivity after Firebase Auth creates the account
     * and SQLite saves the local copy.
     */
    public void saveUserProfile(String uid, String username, String email) {
        Map<String, Object> profile = new HashMap<>();
        profile.put("username",        username);
        profile.put("email",           email);
        profile.put("createdAt",       System.currentTimeMillis());
        profile.put("completedCount",  0);
        profile.put("streakDays",      0);

        rootRef.child("users")
               .child(uid)
               .setValue(profile)
               .addOnSuccessListener(unused ->
                   android.util.Log.d("CloudDB", "User profile saved to cloud: " + uid)
               )
               .addOnFailureListener(e ->
                   android.util.Log.e("CloudDB", "Failed to save profile: " + e.getMessage())
               );
    }

    /*
     * Updates the username in the cloud when changed in AccountActivity.
     * Only updates the username field, leaving other fields untouched.
     * Path: 3dify/users/{uid}/username
     */
    public void updateUsername(String uid, String newUsername) {
        rootRef.child("users")
               .child(uid)
               .child("username")
               .setValue(newUsername)
               .addOnSuccessListener(unused ->
                   android.util.Log.d("CloudDB", "Username updated in cloud: " + newUsername)
               )
               .addOnFailureListener(e ->
                   android.util.Log.e("CloudDB", "Failed to update username: " + e.getMessage())
               );
    }

    /*
     * Updates the completed tutorial count in the cloud.
     * Called every time a tutorial is marked as done.
     * Path: 3dify/users/{uid}/completedCount
     */
    public void updateCompletedCount(String uid, int newCount) {
        rootRef.child("users")
               .child(uid)
               .child("completedCount")
               .setValue(newCount)
               .addOnSuccessListener(unused ->
                   android.util.Log.d("CloudDB", "Completed count updated: " + newCount)
               )
               .addOnFailureListener(e ->
                   android.util.Log.e("CloudDB", "Failed to update count: " + e.getMessage())
               );
    }

    // ════════════════════════════════════════════════════════════
    //  TUTORIAL PROGRESS
    // ════════════════════════════════════════════════════════════

    /*
     * Marks a tutorial as complete in the cloud.
     * Path: 3dify/progress/{uid}/{tutorialId} = true
     *
     * Called from TutorialDetailActivity when user taps Mark as Done.
     * The tutorial ID is the key, value is always true.
     */
    public void markTutorialComplete(String uid, String tutorialId) {
        rootRef.child("progress")
               .child(uid)
               .child(tutorialId)
               .setValue(true)
               .addOnSuccessListener(unused ->
                   android.util.Log.d("CloudDB", "Tutorial marked complete in cloud: " + tutorialId)
               )
               .addOnFailureListener(e ->
                   android.util.Log.e("CloudDB", "Failed to mark complete: " + e.getMessage())
               );
    }

    /*
     * Reads all completed tutorials for a user from the cloud.
     * Used to sync progress when the user logs in on a different device.
     *
     * @param uid      The user's Firebase UID
     * @param listener Callback that receives the data snapshot
     *
     * Usage:
     *   cloud.getProgress(uid, snapshot -> {
     *       for (DataSnapshot child : snapshot.getChildren()) {
     *           String tutorialId = child.getKey(); // completed tutorial
     *       }
     *   });
     */
    public void getProgress(String uid, ProgressListener listener) {
        rootRef.child("progress")
               .child(uid)
               .addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot snapshot) {
                       if (listener != null) listener.onLoaded(snapshot);
                   }
                   @Override
                   public void onCancelled(DatabaseError error) {
                       android.util.Log.e("CloudDB", "Progress load failed: " + error.getMessage());
                   }
               });
    }

    // Callback interface for progress data
    public interface ProgressListener {
        void onLoaded(DataSnapshot snapshot);
    }

    // ════════════════════════════════════════════════════════════
    //  FEEDBACK
    // ════════════════════════════════════════════════════════════

    /*
     * Saves feedback to the cloud.
     * Path: 3dify/feedback/{uid}/{auto-generated key}/
     *
     * Uses push() to generate a unique key automatically.
     * This means multiple feedback entries are all preserved.
     * Called from UserFeedbackActivity.
     */
    public void saveFeedback(String uid, String feedbackText, int rating) {
        Map<String, Object> feedback = new HashMap<>();
        feedback.put("text",      feedbackText);
        feedback.put("rating",    rating);
        feedback.put("timestamp", System.currentTimeMillis());

        rootRef.child("feedback")
               .child(uid)
               .push()               // auto-generates a unique key
               .setValue(feedback)
               .addOnSuccessListener(unused ->
                   android.util.Log.d("CloudDB", "Feedback saved to cloud")
               )
               .addOnFailureListener(e ->
                   android.util.Log.e("CloudDB", "Failed to save feedback: " + e.getMessage())
               );
    }

    // ════════════════════════════════════════════════════════════
    //  COMPLAINTS
    // ════════════════════════════════════════════════════════════

    /*
     * Saves a complaint to the cloud.
     * Path: 3dify/complaints/{uid}/{auto-generated key}/
     * Called from ComplainsActivity.
     */
    public void saveComplaint(String uid, String complaintText) {
        Map<String, Object> complaint = new HashMap<>();
        complaint.put("text",      complaintText);
        complaint.put("status",    "pending");
        complaint.put("timestamp", System.currentTimeMillis());

        rootRef.child("complaints")
               .child(uid)
               .push()
               .setValue(complaint)
               .addOnSuccessListener(unused ->
                   android.util.Log.d("CloudDB", "Complaint saved to cloud")
               )
               .addOnFailureListener(e ->
                   android.util.Log.e("CloudDB", "Failed to save complaint: " + e.getMessage())
               );
    }
}