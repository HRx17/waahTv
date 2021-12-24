package com.tv.waah;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tv.waah.RetrofitFiles.JsonPlaceHolderApi;
import com.tv.waah.RetrofitFiles.RetrofitClient;
import com.tv.waah.RetrofitFiles.Update;
import com.tv.waah.ui.MainActivity;
import com.tv.waah.ui.SettingsActivity;
import com.tv.waah.ui.SettingsFragment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Splash extends FragmentActivity {

    String email;
    public static String Currentversion="1.1";
    ImageView imageView;
    ProgressBar progressBar;
    Handler handler = new Handler();
    int check = 1;

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
        String userValidityDate = sharedPreferences.getString("time", null);
        email = sharedPreferences.getString("email", null);
        SettingsFragment.HINDI = sharedPreferences.getString("hindi", "On");
        SettingsFragment.SOUTH = sharedPreferences.getString("south", "On");
        SettingsFragment.MARATHI = sharedPreferences.getString("marathi", "On");

        animFadeIn.reset();
        imageView.clearAnimation();
        imageView.startAnimation(animFadeIn);
        animFadeOut.reset();
        imageView.clearAnimation();
        imageView.startAnimation(animFadeOut);
        animFadeIn.reset();
        imageView.clearAnimation();
        imageView.startAnimation(animFadeIn);

        if (userValidityDate == null) {
            check = 0;
        } else {
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
                if(downloadUpdate(getBaseContext())) {
                    if (check != 0) {
                        Intent intent = new Intent(Splash.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(Splash.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }, 7000);
    }

    private boolean downloadUpdate(final Context context) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RetrofitClient.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            Call<Update> call = jsonPlaceHolderApi.autoUpdate(email);
            call.enqueue(new Callback<Update>() {
                @Override
                public void onResponse(Call<Update> call, Response<Update> response) {
                    if (response.isSuccessful()) {
                        Update update = response.body();
                        assert update != null;
                        if (!update.getVersionCode().equals(Currentversion)) {
                            System.out.println(update.getVersionCode());
                            String upadte_url = update.getPackag();
                            String update_version = update.getChangelog();
                            Toast.makeText(context, "Downloading request on url :" + upadte_url, Toast.LENGTH_SHORT).show();
                            try {

                                URL url = new URL(upadte_url);
                                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                                c.setRequestMethod("GET");
                                c.setDoOutput(true);
                                c.connect();

                                String PATH = Environment.getExternalStorageDirectory() + "/download/";
                                File file = new File(PATH);
                                file.mkdirs();
                                File outputFile = new File(file, "app.apk");
                                FileOutputStream fos = new FileOutputStream(outputFile);

                                InputStream is = c.getInputStream();

                                byte[] buffer = new byte[1024];
                                int len1 = 0;
                                while ((len1 = is.read(buffer)) != -1) {
                                    fos.write(buffer, 0, len1);
                                }
                                fos.close();
                                is.close();

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(
                                        Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "app.apk")),
                                        "application/vnd.android.package-archive");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                                // System.exit(0);

                            }  catch (Exception e1) {
                            }
                           /*
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(upadte_url));
                            request.setDescription(update_version);
                            request.setTitle(context.getString(R.string.app_name));
                            //set destination
                            final Uri uri = Uri.parse(upadte_url);
                            request.setDestinationUri(uri);

                            // get download service and enqueue file
                            final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                            final long downloadId = manager.enqueue(request);
*/
                            progressBar.setVisibility(View.VISIBLE);
  /*                          new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    boolean downloading = true;
                                    while (downloading) {
                                        DownloadManager.Query q = new DownloadManager.Query();
                                        q.setFilterById(downloadId);
                                        Cursor cursor = manager.query(q);
                                        cursor.moveToFirst();

                                        final int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                                            downloading = false;
                                        }
                                        int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                                        if (bytes_total != 0) {
                                            final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);
                                            progressBar.setProgress(dl_progress);
                                        }
                                        cursor.close();
                                    }
                                }
                            }).start();


                            //set BroadcastReceiver to install app when .apk is downloaded
                            BroadcastReceiver onComplete = new BroadcastReceiver() {
                                public void onReceive(Context ctxt, Intent intent) {
                                    //BroadcastReceiver on Complete
                                    if (!(upadte_url.isEmpty())) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            Uri apkUri = Uri.parse(upadte_url);
                                            intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                                            intent.setData(apkUri);
                                            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        } else {
                                            Uri apkUri = Uri.parse(upadte_url);
                                            intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setDataAndType(apkUri, manager.getMimeTypeForDownloadedFile(downloadId));
                                            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        }
                                        context.startActivity(intent);
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    context.unregisterReceiver(this);
                                }
                            };*/
                           // context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                        }
                    }
                    else{
                        Toast.makeText(Splash.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Update> call, Throwable t) {
                    Toast.makeText(Splash.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch(Exception e){
                e.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        return true;
    }

    private HttpClient setupHttpClient() {
        return new HttpClient() {
            @Override
            public HttpParams getParams() {
                return null;
            }

            @Override
            public ClientConnectionManager getConnectionManager() {
                return null;
            }

            @Override
            public HttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
                return null;
            }

            @Override
            public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException {
                return null;
            }

            @Override
            public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
                return null;
            }

            @Override
            public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
                return null;
            }

            @Override
            public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
                return null;
            }

            @Override
            public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
                return null;
            }

            @Override
            public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
                return null;
            }

            @Override
            public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
                return null;
            }
        };
    }
}