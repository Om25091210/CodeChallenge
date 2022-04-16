package com.mosio.codechallenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity {

    TextView next;
    EditText name;
    FirebaseAuth auth;
    FirebaseUser user;
    String pushkey;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Window window = Home.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Home.this, R.color.white));

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        reference= FirebaseDatabase.getInstance().getReference().child("Rooms");

        pushkey=reference.push().getKey();
        String text_link="https://code-challenge.com/mosio/"+user.getUid()+"/"+pushkey;

        next=findViewById(R.id.textView23);
        name=findViewById(R.id.name);

        next.setOnClickListener(v->{

            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("link ",text_link);
            clipboard.setPrimaryClip(clip);

            Intent intent=new Intent(Home.this,Room.class);
            intent.putExtra("name_of_room",name.getText().toString().trim());
            intent.putExtra("link_of_the_room",text_link);
            intent.putExtra("key_of_the_room",pushkey);
            startActivity(intent);

        });
    }
}