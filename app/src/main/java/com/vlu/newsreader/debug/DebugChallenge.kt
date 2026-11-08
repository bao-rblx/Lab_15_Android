package com.vlu.newsreader.debug

import android.util.Log
import com.vlu.newsreader.network.HttpClient

/**
 * STEP 6 – Intentional Debugging Challenge.
 *
 * Error #1 (NetworkOnMainThreadException):
 *   set TRIGGER_NETWORK_ON_MAIN_THREAD = true, run the app, filter Logcat by tag "DEBUG_CHALLENGE".
 *
 * Error #2 (SecurityException / missing INTERNET permission):
 *   comment out <uses-permission android:name="android.permission.INTERNET"/> in
 *   AndroidManifest.xml, uninstall the app from the emulator, run again, and watch Logcat
 *   (tag "NewsReader"). The UI shows the "permission missing" message.
 *
 * Set both back to normal before submitting.
 */
object DebugChallenge {

    const val TAG = "DEBUG_CHALLENGE"

    /** Flip to true ONLY while capturing the stack trace for the report. */
    const val TRIGGER_NETWORK_ON_MAIN_THREAD = false

    /** If true, the exception is re-thrown so Logcat shows a "FATAL EXCEPTION: main" crash. */
    const val CRASH_APP = false

    fun runNetworkOnMainThread() {
        try {
            // WRONG on purpose: blocking HTTP call executed directly on the UI thread.
            HttpClient.getBlocking(HttpClient.POSTS_URL)
        } catch (e: android.os.NetworkOnMainThreadException) {
            Log.e(TAG, "Reproduced NetworkOnMainThreadException", e)
            if (CRASH_APP) throw e
        }
    }
}
