package com.example.a3dify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DatabaseHelper
 * ══════════════════════════════════════════════════════════
 * Central SQLite database manager for 3Dify.
 *
 * Handles all on-device data storage:
 *   - User profiles (username, email — linked to Firebase Auth UID)
 *   - Learning progress (completed tutorials, streak days)
 *   - User feedback submissions
 *   - Complaint submissions
 *   - Generated reports
 *   - App settings (dark mode, notifications, etc.)
 *
 * Firebase Auth handles authentication only.
 * Everything else is stored here locally using SQLite,
 * which is built into Android and requires no billing.
 *
 * Usage anywhere in the app:
 *   DatabaseHelper db = DatabaseHelper.getInstance(context);
 *   db.saveUserProfile(uid, username, email);
 * ══════════════════════════════════════════════════════════
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // ── Database configuration ──────────────────────────────────
    private static final String DATABASE_NAME    = "a3dify.db";
    private static final int    DATABASE_VERSION = 1;

    // ── Table names ─────────────────────────────────────────────
    public static final String TABLE_USERS      = "users";
    public static final String TABLE_PROGRESS   = "progress";
    public static final String TABLE_FEEDBACK   = "feedback";
    public static final String TABLE_COMPLAINTS = "complaints";
    public static final String TABLE_REPORTS    = "reports";
    public static final String TABLE_SETTINGS   = "settings";

    // ── Column names shared across tables ───────────────────────
    public static final String COL_ID           = "id";
    public static final String COL_UID          = "uid";
    public static final String COL_CREATED_AT   = "created_at";

    // ── Users table columns ─────────────────────────────────────
    public static final String COL_USERNAME     = "username";
    public static final String COL_EMAIL        = "email";

    // ── Progress table columns ──────────────────────────────────
    public static final String COL_TUTORIAL_ID  = "tutorial_id";
    public static final String COL_COMPLETED    = "completed";   // 0 or 1
    public static final String COL_STREAK       = "streak_days";

    // ── Feedback table columns ──────────────────────────────────
    public static final String COL_FEEDBACK     = "feedback_text";
    public static final String COL_RATING       = "rating";      // 1–5

    // ── Complaints table columns ────────────────────────────────
    public static final String COL_COMPLAINT    = "complaint_text";
    public static final String COL_STATUS       = "status";      // pending / resolved

    // ── Reports table columns ───────────────────────────────────
    public static final String COL_REPORT_TYPE  = "report_type";
    public static final String COL_REPORT_DATA  = "report_data";

    // ── Settings table columns ──────────────────────────────────
    public static final String COL_SETTING_KEY  = "setting_key";
    public static final String COL_SETTING_VAL  = "setting_value";

    // ── Singleton — one instance shared across the whole app ────
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    // Private constructor — use getInstance() instead
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // ────────────────────────────────────────────────────────────
    //  onCreate — runs once when the database file is first created
    // ────────────────────────────────────────────────────────────
    @Override
    public void onCreate(SQLiteDatabase db) {

        // ── USERS TABLE ──────────────────────────────────────────
        // One row per registered user.
        // COL_UID links this record to the Firebase Auth account.
        db.execSQL(
                "CREATE TABLE " + TABLE_USERS + " (" +
                        COL_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_UID        + " TEXT UNIQUE NOT NULL, " +
                        COL_USERNAME   + " TEXT NOT NULL, " +
                        COL_EMAIL      + " TEXT NOT NULL, " +
                        COL_CREATED_AT + " INTEGER" +
                        ")"
        );

        // ── PROGRESS TABLE ───────────────────────────────────────
        // One row per tutorial the user has interacted with.
        // COL_COMPLETED: 0 = in progress, 1 = finished
        db.execSQL(
                "CREATE TABLE " + TABLE_PROGRESS + " (" +
                        COL_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_UID         + " TEXT NOT NULL, " +
                        COL_TUTORIAL_ID + " TEXT NOT NULL, " +
                        COL_COMPLETED   + " INTEGER DEFAULT 0, " +
                        COL_STREAK      + " INTEGER DEFAULT 0, " +
                        COL_CREATED_AT  + " INTEGER" +
                        ")"
        );

        // ── FEEDBACK TABLE ───────────────────────────────────────
        // Stores submissions from UserFeedbackActivity.
        db.execSQL(
                "CREATE TABLE " + TABLE_FEEDBACK + " (" +
                        COL_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_UID        + " TEXT, " +
                        COL_FEEDBACK   + " TEXT NOT NULL, " +
                        COL_RATING     + " INTEGER DEFAULT 5, " +
                        COL_CREATED_AT + " INTEGER" +
                        ")"
        );

        // ── COMPLAINTS TABLE ─────────────────────────────────────
        // Stores submissions from ComplainsActivity.
        db.execSQL(
                "CREATE TABLE " + TABLE_COMPLAINTS + " (" +
                        COL_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_UID        + " TEXT, " +
                        COL_COMPLAINT  + " TEXT NOT NULL, " +
                        COL_STATUS     + " TEXT DEFAULT 'pending', " +
                        COL_CREATED_AT + " INTEGER" +
                        ")"
        );

        // ── REPORTS TABLE ────────────────────────────────────────
        // Stores generated reports from ReportActivity.
        db.execSQL(
                "CREATE TABLE " + TABLE_REPORTS + " (" +
                        COL_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_UID         + " TEXT, " +
                        COL_REPORT_TYPE + " TEXT, " +
                        COL_REPORT_DATA + " TEXT, " +
                        COL_CREATED_AT  + " INTEGER" +
                        ")"
        );

        // ── SETTINGS TABLE ───────────────────────────────────────
        // Key-value store. One row per setting per user.
        // Example rows:
        //   uid="abc123", key="dark_mode",     value="true"
        //   uid="abc123", key="notifications", value="false"
        db.execSQL(
                "CREATE TABLE " + TABLE_SETTINGS + " (" +
                        COL_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_UID         + " TEXT NOT NULL, " +
                        COL_SETTING_KEY + " TEXT NOT NULL, " +
                        COL_SETTING_VAL + " TEXT NOT NULL" +
                        ")"
        );

        // Seed default settings for new installs
        seedDefaultSettings(db);
    }

    // ────────────────────────────────────────────────────────────
    //  onUpgrade — runs when DATABASE_VERSION increases
    //  During development, dropping and recreating is fine.
    //  Before release you would write migration SQL instead.
    // ────────────────────────────────────────────────────────────
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        onCreate(db);
    }

    // ════════════════════════════════════════════════════════════
    //  USER PROFILE METHODS
    // ════════════════════════════════════════════════════════════

    /**
     * Saves a new user profile after Firebase Auth creates the account.
     * Called from SignUpActivity.
     *
     * @param uid      The Firebase Auth UID — links SQLite to the Auth account
     * @param username The username the user chose at registration
     * @param email    The user's email address
     * @return true if saved successfully, false if an error occurred
     */
    public boolean saveUserProfile(String uid, String username, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_UID,        uid);
        values.put(COL_USERNAME,   username);
        values.put(COL_EMAIL,      email);
        values.put(COL_CREATED_AT, System.currentTimeMillis());

        // CONFLICT_REPLACE: if this UID already exists, update it instead of failing
        long result = db.insertWithOnConflict(
                TABLE_USERS, null, values, SQLiteDatabase.CONFLICT_REPLACE
        );
        return result != -1;
    }

    /**
     * Retrieves a user's profile row as a Cursor.
     * Always close the Cursor after reading from it.
     *
     * Example usage:
     *   Cursor c = db.getUserProfile(uid);
     *   if (c != null && c.moveToFirst()) {
     *       String name = c.getString(c.getColumnIndexOrThrow(COL_USERNAME));
     *       c.close();
     *   }
     */
    public Cursor getUserProfile(String uid) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                TABLE_USERS,
                null,                    // SELECT * (all columns)
                COL_UID + " = ?",        // WHERE uid = ?
                new String[]{uid},       // bind the uid value
                null, null, null
        );
    }

    /**
     * Updates only the username field for an existing user.
     * Used in AccountActivity when the user edits their profile.
     */
    public boolean updateUsername(String uid, String newUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, newUsername);
        int rowsAffected = db.update(
                TABLE_USERS, values,
                COL_UID + " = ?",
                new String[]{uid}
        );
        return rowsAffected > 0;
    }

    // ════════════════════════════════════════════════════════════
    //  PROGRESS METHODS
    // ════════════════════════════════════════════════════════════

    /**
     * Marks a tutorial as completed for a user.
     * Called from the Tutorial Detail screen when user taps "Mark Done".
     */
    public boolean markTutorialComplete(String uid, String tutorialId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_UID,         uid);
        values.put(COL_TUTORIAL_ID, tutorialId);
        values.put(COL_COMPLETED,   1);
        values.put(COL_CREATED_AT,  System.currentTimeMillis());
        long result = db.insertWithOnConflict(
                TABLE_PROGRESS, null, values, SQLiteDatabase.CONFLICT_REPLACE
        );
        return result != -1;
    }

    /**
     * Returns the number of tutorials a user has completed.
     * Used on the Progress screen to show the completed count.
     */
    public int getCompletedCount(String uid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_PROGRESS +
                        " WHERE " + COL_UID + " = ? AND " + COL_COMPLETED + " = 1",
                new String[]{uid}
        );
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // ════════════════════════════════════════════════════════════
    //  FEEDBACK METHODS
    // ════════════════════════════════════════════════════════════

    /**
     * Saves a feedback submission from UserFeedbackActivity.
     *
     * @param uid          Firebase UID (or "guest" for guest users)
     * @param feedbackText The feedback message the user typed
     * @param rating       Star rating 1–5
     */
    public boolean saveFeedback(String uid, String feedbackText, int rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_UID,        uid);
        values.put(COL_FEEDBACK,   feedbackText);
        values.put(COL_RATING,     rating);
        values.put(COL_CREATED_AT, System.currentTimeMillis());
        long result = db.insert(TABLE_FEEDBACK, null, values);
        return result != -1;
    }

    /**
     * Returns all feedback entries, newest first.
     * Used in ReportActivity to display a history.
     */
    public Cursor getAllFeedback() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM " + TABLE_FEEDBACK +
                        " ORDER BY " + COL_CREATED_AT + " DESC",
                null
        );
    }

    // ════════════════════════════════════════════════════════════
    //  COMPLAINTS METHODS
    // ════════════════════════════════════════════════════════════

    /**
     * Saves a complaint from ComplainsActivity.
     */
    public boolean saveComplaint(String uid, String complaintText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_UID,        uid);
        values.put(COL_COMPLAINT,  complaintText);
        values.put(COL_STATUS,     "pending");
        values.put(COL_CREATED_AT, System.currentTimeMillis());
        long result = db.insert(TABLE_COMPLAINTS, null, values);
        return result != -1;
    }

    /**
     * Returns all complaints for a specific user, newest first.
     */
    public Cursor getUserComplaints(String uid) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                TABLE_COMPLAINTS, null,
                COL_UID + " = ?",
                new String[]{uid},
                null, null,
                COL_CREATED_AT + " DESC"
        );
    }

    // ════════════════════════════════════════════════════════════
    //  REPORTS METHODS
    // ════════════════════════════════════════════════════════════

    /**
     * Saves a report entry. Called from ReportActivity.
     *
     * @param reportType  Category label e.g. "Progress Report", "Feedback Summary"
     * @param reportData  The actual report content as a formatted string
     */
    public boolean saveReport(String uid, String reportType, String reportData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_UID,         uid);
        values.put(COL_REPORT_TYPE, reportType);
        values.put(COL_REPORT_DATA, reportData);
        values.put(COL_CREATED_AT,  System.currentTimeMillis());
        long result = db.insert(TABLE_REPORTS, null, values);
        return result != -1;
    }

    /**
     * Returns all reports for a user, newest first.
     */
    public Cursor getUserReports(String uid) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                TABLE_REPORTS, null,
                COL_UID + " = ?",
                new String[]{uid},
                null, null,
                COL_CREATED_AT + " DESC"
        );
    }

    // ════════════════════════════════════════════════════════════
    //  SETTINGS METHODS
    // ════════════════════════════════════════════════════════════

    /**
     * Reads a setting value for a user.
     * Returns defaultValue if the key has never been saved.
     *
     * Example:
     *   String darkMode = db.getSetting(uid, "dark_mode", "true");
     */
    public String getSetting(String uid, String key, String defaultValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_SETTINGS,
                new String[]{COL_SETTING_VAL},
                COL_UID + " = ? AND " + COL_SETTING_KEY + " = ?",
                new String[]{uid, key},
                null, null, null
        );
        if (cursor != null && cursor.moveToFirst()) {
            String value = cursor.getString(0);
            cursor.close();
            return value;
        }
        if (cursor != null) cursor.close();
        return defaultValue;
    }

    /**
     * Saves or updates a single setting for a user.
     * CONFLICT_REPLACE means if the key already exists it updates it,
     * so you never get duplicate setting rows.
     *
     * Example:
     *   db.saveSetting(uid, "dark_mode", "false");
     */
    public void saveSetting(String uid, String key, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_UID,         uid);
        cv.put(COL_SETTING_KEY, key);
        cv.put(COL_SETTING_VAL, value);
        db.insertWithOnConflict(
                TABLE_SETTINGS, null, cv, SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    /**
     * Seeds the default settings when the app is first installed.
     * Called from onCreate so these exist before the user changes anything.
     */
    private void seedDefaultSettings(SQLiteDatabase db) {
        String[][] defaults = {
                {"dark_mode",        "true"},
                {"notifications",    "true"},
                {"daily_reminder",   "true"},
                {"sound_effects",    "true"}
        };
        for (String[] pair : defaults) {
            ContentValues cv = new ContentValues();
            cv.put(COL_UID,         "default");
            cv.put(COL_SETTING_KEY, pair[0]);
            cv.put(COL_SETTING_VAL, pair[1]);
            db.insert(TABLE_SETTINGS, null, cv);
        }
    }
}