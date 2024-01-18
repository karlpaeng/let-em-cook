package dev.karl.letemcook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.w3c.dom.Text;

public class Splashr extends AppCompatActivity {

    String apiKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashr);

        getWindow().setStatusBarColor(ContextCompat.getColor(Splashr.this, R.color.orange));
        getWindow().setNavigationBarColor(ContextCompat.getColor(Splashr.this, R.color.orange));

//        TextView tv = findViewById(R.id.splasher);
        ConstraintLayout cl = findViewById(R.id.cl04);

        FirebaseApp.initializeApp(this);
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();

        remoteConfig.setConfigSettingsAsync(remoteConfigSettings);

        apiKey = "";

        remoteConfig.fetchAndActivate().addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                apiKey = remoteConfig.getString("apiKey");

            }else{
                Toast.makeText(Splashr.this, "Failed to connect to Remote Config. Try again.", Toast.LENGTH_SHORT).show();
                //diaAlert("Connect to the Internet", "This application requires an Internet connection, connect to a Wi-Fi or turn on Mobile Data, and try again.");
            }
        });

        VideoView videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.splash; // Replace with your video file
        Uri uri = Uri.parse(videoPath);

        // Set up the VideoView
        videoView.setVideoURI(uri);
        
        videoView.setOnPreparedListener(mediaPlayer -> {
            cl.setVisibility(View.INVISIBLE);
        });

        // Set a listener to detect when the video playback is complete
        videoView.setOnCompletionListener(mp -> {
            // Start a new activity after the video playback is complete
            if(isNetworkAvailable(Splashr.this)){
                new Handler().postDelayed(() -> {
                    //tv.setText("1\\4 cup");
                    if(!apiKey.equals("")){
                        Intent appContent = new Intent(Splashr.this, Search.class);
                        appContent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        appContent.putExtra("apikey", apiKey);
                        //textView.setText(categsArray[4]);
                        startActivity(appContent);
                    }else{
                        diaAlert("Error", "Failed to connect to Remote Config. Try again.");
                    }

                }, 3000);
            }else{
                diaAlert("Connect to the Internet", "This application requires an Internet connection, connect to a Wi-Fi or turn on Mobile Data, and try again.");
            }
        });

        // Start playing the video
        videoView.start();



    }

    private boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getNetworkCapabilities(cm.getActiveNetwork()).hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        } catch (Exception e) {
            return false;
        }
    }
    private void diaAlert(String t, String c){
        AlertDialog.Builder builder = new AlertDialog.Builder(Splashr.this);
        View v = getLayoutInflater().inflate(R.layout.dialog_alert, null);

        TextView tvTitle, tvContent, okBtn;

        tvTitle = v.findViewById(R.id.diaAlertTitle);
        tvContent = v.findViewById(R.id.diaAlertContent);
        okBtn = v.findViewById(R.id.diaAlertOkBtn);

        tvTitle.setText(t);
        tvContent.setText(c);

        builder.setView(v);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialog.getWindow().setGravity(Gravity.CENTER);

        alertDialog.show();

        okBtn.setOnClickListener(view -> {
            //
            alertDialog.dismiss();
            //prompt username
            //finishAffinity();

            Intent intent = new Intent(Splashr.this, Splashr.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            intent.putExtra("apikey", apiKey);
            //textView.setText(categsArray[4]);
            startActivity(intent);
        });


    }

}