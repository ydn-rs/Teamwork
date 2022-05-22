package ru.yudin_r.teamwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ru.yudin_r.teamwork.tools.Database;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (isOnline(SplashActivity.this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (new Database().getId() == null) {
                        startActivity(new Intent(SplashActivity.this, EmailActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                    finish();
                }
            }, 2500);
        } else {
            Toast.makeText(SplashActivity.this, "Ошибка интернет-подключения",
                    Toast.LENGTH_LONG).show();
        }


    }

    public boolean isOnline(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }
}