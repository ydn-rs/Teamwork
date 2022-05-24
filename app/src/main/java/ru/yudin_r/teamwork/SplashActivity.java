package ru.yudin_r.teamwork;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import ru.yudin_r.teamwork.tools.Database;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView factTv = findViewById(R.id.factTv);

        String[] facts = new String[5];
        facts[0] = "Вы сегодня хорошо выглядите!";
        facts[1] = "Сегодня вы особенно выделяетесь из наших пользователей!";
        facts[2] = "Самое время нам работать";
        facts[3] = "У нас даже сервер лёг, от вашей неотразимой внешности";
        facts[4] = "Хорошего дня!";

        int random = new Random().nextInt(facts.length);
        factTv.setText(facts[random]);

        if (isOnline(SplashActivity.this)) {
            new Handler().postDelayed(() -> {
                if (new Database().getId() == null) {
                    startActivity(new Intent(SplashActivity.this, EmailActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }, 2500);
        } else {
            Toast.makeText(SplashActivity.this, "Ошибка интернет-подключения",
                    Toast.LENGTH_LONG).show();
        }
    }

    public boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}