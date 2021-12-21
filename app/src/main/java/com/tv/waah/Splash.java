package com.tv.waah;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import com.tv.waah.data.OnAppUpdateListener;
import com.tv.waah.ui.MainActivity;
import com.tv.waah.ui.SettingsFragment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import edu.mit.mobile.android.utils.StreamUtils;


public class Splash extends FragmentActivity {

    ImageView imageView;
    ProgressBar progressBar;
    Handler handler = new Handler();
    int check=1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView = findViewById(R.id.logo);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        Animation animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);

        Animation animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);

        SharedPreferences sharedPreferences = getSharedPreferences("time", 0);
        String userValidityDate = sharedPreferences.getString("time",null);
        SettingsFragment.LANG = sharedPreferences.getString("language","English (default)");

        animFadeIn.reset();
        imageView.clearAnimation();
        imageView.startAnimation(animFadeIn);
        animFadeOut.reset();
        imageView.clearAnimation();
        imageView.startAnimation(animFadeOut);
        animFadeIn.reset();
        imageView.clearAnimation();
        imageView.startAnimation(animFadeIn);

        if(userValidityDate == null){
            check=0;
        }
        else {
            Calendar cal = Calendar.getInstance();

            int year = Integer.parseInt(userValidityDate.substring(6)); // this is deprecated
            int month = Integer.parseInt(userValidityDate.substring(3, 5));
            int day = Integer.parseInt(userValidityDate.substring(0, 2));

            Calendar validDate = Calendar.getInstance();
            validDate.set(year, month, day);

            Calendar currentDate = Calendar.getInstance();

            if (currentDate.after(validDate)) {
                check = 0;
            }

        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                AppUpdateChecker appUpdateChecker = new AppUpdateChecker(getBaseContext(),"https://parrot-tv.azurewebsites.net/api/serverversion?email=a@a.com",null);
                //appUpdateChecker.checkForUpdates();
                    if (check != 0) {
                        Intent intent = new Intent(Splash.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Splash.this, Login.class);
                        startActivity(intent);
                    }
            }
        }, 7000);
    }
    public static class AppUpdateChecker {
        private final static String TAG = AppUpdateChecker.class.getSimpleName();

        public static final String SHARED_PREFERENCES_NAME = "appupdater.preferences";
        public static final String
                PREF_ENABLED = "enabled",
                PREF_MIN_INTERVAL = "min_interval",
                PREF_LAST_UPDATED = "last_checked";

        private final String mVersionListUrl;
        private int currentAppVersion;

        private JSONObject pkgInfo;
        private final Context mContext;

        private final OnAppUpdateListener mUpdateListener;
        private SharedPreferences mPrefs;

        private static final int MILLISECONDS_IN_MINUTE = 60000;


        public AppUpdateChecker(Context context, String versionListUrl, OnAppUpdateListener updateListener) {
            mContext = context;
            mVersionListUrl = versionListUrl;
            mUpdateListener = updateListener;

            try {
                currentAppVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            } catch (final PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Cannot get version for self! Who am I?! What's going on!? I'm so confused :-(");
                return;
            }

            mPrefs = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            // defaults are kept in the preference file for ease of tweaking
            // TODO put this on a thread somehow
            PreferenceManager.setDefaultValues(mContext, SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE, R.xml.preferences, false);
        }

        // min interval is stored as a string so a preference editor could potentially edit it using a text edit widget

        public int getMinInterval(){
            return Integer.valueOf(mPrefs.getString(PREF_MIN_INTERVAL, "60"));
        }

        public void setMinInterval(int minutes){
            mPrefs.edit().putString(PREF_MIN_INTERVAL, String.valueOf(minutes)).commit();
        }

        public boolean getEnabled(){
            return mPrefs.getBoolean(PREF_ENABLED, true);
        }

        public void setEnabled(boolean enabled){
            mPrefs.edit().putBoolean(PREF_ENABLED, enabled).commit();
        }

        /**
         * You normally shouldn't need to call this, as {@link #checkForUpdates()} checks it before doing any updates.
         *
         * @return true if the updater should check for updates
         */
        public boolean isStale(){
            return System.currentTimeMillis() - mPrefs.getLong(PREF_LAST_UPDATED, 0) > getMinInterval() * MILLISECONDS_IN_MINUTE;
        }

        /**
         * Checks for updates if updates haven't been checked for recently and if checking is enabled.
         */
        public void checkForUpdates(){
            if (mPrefs.getBoolean(PREF_ENABLED, true) && isStale()){
                forceCheckForUpdates();
            }
        }

        /**
         * Checks for updates regardless of when the last check happened or if checking for updates is enabled.
         */
        public void forceCheckForUpdates(){
            Log.d(TAG, "checking for updates...");
            if (versionTask == null){
                versionTask = new GetVersionJsonTask();
                versionTask.execute(mVersionListUrl);
            }else{
                Log.w(TAG, "checkForUpdates() called while already checking for updates. Ignoring...");
            }
        }

        // why oh why is the JSON API so poorly integrated into java?
        @SuppressWarnings("unchecked")
        private void triggerFromJson(JSONObject jo) throws JSONException {

            final ArrayList<String> changelog = new ArrayList<String>();

            // keep a sorted map of versionCode to the version information objects.
            // Most recent is at the top.
            final TreeMap<Integer, JSONObject> versionMap =
                    new TreeMap<Integer, JSONObject>(new Comparator<Integer>() {
                        public int compare(Integer object1, Integer object2) {
                            return object2.compareTo(object1);
                        };
                    });

            for (final Iterator<String> i = jo.keys(); i.hasNext(); ){
                final String versionName = i.next();
                if (versionName.equals("package")){
                    pkgInfo = jo.getJSONObject(versionName);
                    continue;
                }
                final JSONObject versionInfo = jo.getJSONObject(versionName);
                versionInfo.put("versionName", versionName);

                final int versionCode = versionInfo.getInt("versionCode");
                versionMap.put(versionCode, versionInfo);
            }
            final int latestVersionNumber = versionMap.firstKey();
            final String latestVersionName = versionMap.get(latestVersionNumber).getString("versionName");
            final Uri downloadUri = Uri.parse(pkgInfo.getString("downloadUrl"));

            if (currentAppVersion > latestVersionNumber){
                Log.d(TAG, "We're newer than the latest published version ("+latestVersionName+"). Living in the future...");
                mUpdateListener.appUpdateStatus(true, latestVersionName, null, downloadUri);
                return;
            }

            if (currentAppVersion == latestVersionNumber){
                Log.d(TAG, "We're at the latest version ("+currentAppVersion+")");
                mUpdateListener.appUpdateStatus(true, latestVersionName, null, downloadUri);
                return;
            }

            // construct the changelog. Newest entries are at the top.
            for (final Map.Entry<Integer, JSONObject> version: versionMap.headMap(currentAppVersion).entrySet()){
                final JSONObject versionInfo = version.getValue();
                final JSONArray versionChangelog = versionInfo.optJSONArray("changelog");
                if (versionChangelog != null){
                    final int len = versionChangelog.length();
                    for (int i = 0; i < len; i++){
                        changelog.add(versionChangelog.getString(i));
                    }
                }
            }

            mUpdateListener.appUpdateStatus(false, latestVersionName, changelog, downloadUri);
        }

        private class VersionCheckException extends Exception {
            private static final long serialVersionUID = 397593559982487816L;

            public VersionCheckException(String msg) {
                super(msg);
            }
        }
        /*
         * Send off an intent to start the download of the app.
         */
        public void startUpgrade(){
            try {
                final Uri downloadUri = Uri.parse(pkgInfo.getString("downloadUrl"));
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, downloadUri));
            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }

        private GetVersionJsonTask versionTask;
        private class GetVersionJsonTask extends AsyncTask<String, Integer, JSONObject> {
            private String errorMsg = null;

            @Override
            protected void onProgressUpdate(Integer... values) {
                Log.d(TAG, "update check progress: " + values[0]);
                super.onProgressUpdate(values);
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                publishProgress(0);
                final DefaultHttpClient hc = new DefaultHttpClient();
                final String url = params[0];
                final HttpGet req = new HttpGet(url);
                JSONObject jo = null;
                try {
                    publishProgress(50);
                    final HttpResponse res = hc.execute(req);

                    final StatusLine status = res.getStatusLine();
                    final int statusCode = status.getStatusCode();
                    if (statusCode == HttpStatus.SC_NOT_FOUND) {
                        throw new VersionCheckException(url + " " + status.getReasonPhrase());
                    }
                    if (statusCode != HttpStatus.SC_OK){
                        final HttpEntity e = res.getEntity();
                        if (e.getContentType().getValue().equals("text/html") || e.getContentLength() > 40){
                            // long response body. Serving HTML...
                            throw new VersionCheckException("Got a HTML instead of expected JSON.");
                        }
                        throw new VersionCheckException("HTTP " + res.getStatusLine().getStatusCode() + " "+ res.getStatusLine().getReasonPhrase());
                    }

                    final HttpEntity ent = res.getEntity();

                    jo = new JSONObject(StreamUtils.inputStreamToString(ent.getContent()));
                    ent.consumeContent();
                    mPrefs.edit().putLong(PREF_LAST_UPDATED, System.currentTimeMillis()).commit();

                } catch (final Exception e) {
                    //e.printStackTrace();

                    errorMsg = e.getClass().getSimpleName() + ": " + e.getLocalizedMessage();
                }finally {
                    publishProgress(100);
                }
                return jo;
            }

            //@Override
            protected void onPostExecute(JSONObject result) {
                if (result == null){
                    Log.e(TAG, errorMsg);
                }else{
                    try {
                        triggerFromJson(result);

                    } catch (final JSONException e) {
                        Log.e(TAG, "Error in JSON version file.", e);
                    }
                }
                versionTask = null; // forget about us, we're done.
            };
        };
    }
}