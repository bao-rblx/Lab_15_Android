# Lab 15 – NewsReader (HTTP, JSON, Permissions)

Reads `https://jsonplaceholder.typicode.com/posts` (JSON array, 100 posts) and shows them in a RecyclerView.

| Requirement | Where |
|---|---|
| INTERNET permission, HTTPS only (`usesCleartextTraffic=false`) | `app/src/main/AndroidManifest.xml` |
| viewBinding, Gson 2.11.0, Coroutines 1.9.0, Lifecycle | `app/build.gradle.kts` |
| Data model | `model/NewsItem.kt` |
| HttpURLConnection + `withContext(Dispatchers.IO)` | `network/HttpClient.kt` |
| Manual (`JSONArray`) and Gson parsing | `parser/NewsJsonParser.kt` |
| ViewBinding, ProgressBar / list / error states, Retry | `MainActivity.kt`, `ui/NewsAdapter.kt` |
| Step 6 debugging challenge | `debug/DebugChallenge.kt`, `docs/Lab15_Debug_Report.pdf` |
| Unit tests (both parsers, 100 items, same result) | `app/src/test/.../NewsJsonParserTest.kt` |

The **Parser** button in the top bar switches between Gson and manual JSONArray parsing and reloads the list.

## Testing
- **Normal:** run the app. You should see "100 posts (parser: Gson)".
- **Airplane Mode:** turn it on and tap Retry. The app shows "No internet connection…" with a Retry button and does not crash.
- **Unit tests:** `./gradlew testDebugUnitTest`

## Debugging challenge (Step 6)
1. **NetworkOnMainThreadException:** set `TRIGGER_NETWORK_ON_MAIN_THREAD = true` in `DebugChallenge.kt`, run, then filter Logcat by `DEBUG_CHALLENGE`. Set `CRASH_APP = true` to also see the `FATAL EXCEPTION: main` crash.
2. **SecurityException:** comment out the `<uses-permission>` line, **uninstall** the app, run, then filter Logcat by `NewsReader`.

Revert both changes when you are done.
