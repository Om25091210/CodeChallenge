package com.mosio.codechallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mosio.codechallenge.Adapter.roomAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Room extends AppCompatActivity {

    String room_name,text_link,key_room;
    TextView share,link,start;
    String pushkey;
    FirebaseAuth auth;
    ArrayList<String> dp_list=new ArrayList<>();
    ArrayList<String> name_list=new ArrayList<>();
    Uri deep_link_uri;
    RecyclerView recyclerView;
    FirebaseUser user;
    ArrayList<String> list=new ArrayList<>();
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Window window = Room.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Room.this, R.color.white));

        room_name=getIntent().getStringExtra("name_of_room");
        text_link=getIntent().getStringExtra("link_of_the_room");
        key_room=getIntent().getStringExtra("key_of_the_room");

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        reference= FirebaseDatabase.getInstance().getReference().child("Rooms");

        share=findViewById(R.id.share);
        start=findViewById(R.id.start);
        link=findViewById(R.id.link);
        recyclerView=findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(linearLayoutManager);

        if(text_link!=null)
            link.setText(text_link);
        if(key_room!=null) {
            reference.child(key_room).child(user.getUid()).setValue(user.getUid());
            get_participants(key_room,user.getUid());
        }
        share.setOnClickListener(v->{
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("link ",text_link);
            clipboard.setPrimaryClip(clip);

            String title ="*"+room_name+" \uD83D\uDE0E*"+"\n\n"+"Join this room to code together for position."; //Text to be shared
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, title+"\n\n" +link.getText().toString().trim()+"\n\n"+"This is a playstore link to download.. " + "https://play.google.com/store/apps/details?id=" + getPackageName());
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        });

        deep_link_uri = getIntent().getData();//deep link value
        if(deep_link_uri!=null){
            if (deep_link_uri.toString().equals("https://tic-tac-toe-online")){
                Toast.makeText(Room.this, "wrong link 1", Toast.LENGTH_SHORT).show();
            }
            else if(deep_link_uri.toString().equals("http://tic-tac-toe-online")){
                Toast.makeText(Room.this, "wrong link 2", Toast.LENGTH_SHORT).show();
            }
            else if(deep_link_uri.toString().equals("tic-tac-toe-online")){
                Toast.makeText(Room.this, "wrong link 3", Toast.LENGTH_SHORT).show();
            }
            else{
                // if the uri is not null then we are getting the
                // path segments and storing it in list.
                List<String> parameters = deep_link_uri.getPathSegments();
                // after that we are extracting string from that parameters.
                if(parameters!=null) {
                    if(parameters.size()>1) {
                        String param = parameters.get(parameters.size() - 1);
                        String uid = parameters.get(parameters.size() - 2);
                        // on below line we are setting
                        // that string to our text view
                        // which we got as params.
                        Log.e("deep_link_value", param + "");
                        Log.e("deep_link_value_uid", uid + "");
                        reference.child(param).child(user.getUid()).setValue(user.getUid());
                        link.setText(deep_link_uri.toString());
                        get_participants(param,uid);
                        if(!uid.equals(user.getUid()))
                            start.setVisibility(View.GONE);
                    }
                    else{
                        Toast.makeText(Room.this, "Wrong link 4", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        start.setOnClickListener(v->{
            reference.child(key_room).child("game").setValue("on");
            if(list.size()>1){
                reference.child(key_room).child("game").setValue("on");
            }
        });

    }

    private void get_participants(String key, String uid){
        reference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(!Objects.equals(ds.getKey(), "game") || !Objects.equals(ds.getKey(),"leaderboard")) {
                        list.add(ds.getKey());
                    }
                }
                get_details();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        reference.child(key).child("game").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Intent intent = new Intent(Room.this, brain.class);
                    intent.putExtra("pushkey_of_the_room", key);
                    intent.putExtra("admin_of_the_room", uid);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    private void get_details(){
        DatabaseReference users=FirebaseDatabase.getInstance().getReference().child("users");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dp_list.clear();
                name_list.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    if(list.contains(ds.getKey())){
                        String dp=snapshot.child(ds.getKey()).child("dplink").getValue(String.class);
                        String name=snapshot.child(ds.getKey()).child("name").getValue(String.class);
                        dp_list.add(dp);
                        name_list.add(name);
                    }
                }
                roomAdapter roomAdapter=new roomAdapter(Room.this,dp_list,name_list);
                if(recyclerView!=null)
                    recyclerView.setAdapter(roomAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }


}