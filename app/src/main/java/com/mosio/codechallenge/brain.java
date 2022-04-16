package com.mosio.codechallenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class brain extends AppCompatActivity {

    String key_of_room,admin_of_room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brain);

        Window window = brain.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(brain.this, R.color.white));

        key_of_room=getIntent().getStringExtra("pushkey_of_the_room");
        admin_of_room=getIntent().getStringExtra("admin_of_the_room");

        new Handler(Looper.myLooper()).postDelayed(() -> {
            Intent intent=new Intent(brain.this,questions.class);
            intent.putExtra("key_of_the_room",key_of_room);
            intent.putExtra("admin_of_the_room",admin_of_room);
            startActivity(intent);
            finish();
        },4000);

    }

    @Override
    public void onBackPressed() {

    }
}