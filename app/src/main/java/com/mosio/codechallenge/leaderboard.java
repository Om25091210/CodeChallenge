package com.mosio.codechallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mosio.codechallenge.Adapter.leaderAdapter;
import com.mosio.codechallenge.Adapter.roomAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class leaderboard extends AppCompatActivity {


    String key_of_the_room,admin_of_the_room;
    DatabaseReference reference;
    RecyclerView recyclerView;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView play_again;
    TextView score;
    ArrayList<String> list_key=new ArrayList<>();
    ArrayList<Integer> list_values=new ArrayList<>();
    ArrayList<String> dp_list=new ArrayList<>();
    ArrayList<String> name_list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        Window window = leaderboard.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(leaderboard.this, R.color.white));

        key_of_the_room=getIntent().getStringExtra("key_of_the_room");
        admin_of_the_room=getIntent().getStringExtra("admin_of_the_room");

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        play_again=findViewById(R.id.start);
        score=findViewById(R.id.score);
        play_again.setOnClickListener(v->{
            Intent intent=new Intent(leaderboard.this,Home.class);
            startActivity(intent);
            finish();
        });
        reference= FirebaseDatabase.getInstance().getReference().child("Rooms");

        recyclerView=findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setLayoutManager(linearLayoutManager);

        fetch(key_of_the_room);
    }

    private void fetch(String key_of_the_room){
        reference.child(key_of_the_room).child("leaderboard").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list_key.clear();
                    list_values.clear();
                    for(DataSnapshot ds:snapshot.getChildren()) {
                        String ast=snapshot.child(ds.getKey()).getValue(String.class);
                        for(int i=0;i<ast.length();i++){
                            if(ast.charAt(i)=='/') {
                                String sub1 = ast.substring(0, i);
                                String sub2 = ast.substring(i + 1);
                                list_key.add(sub1);
                                list_values.add(Integer.parseInt(sub2));
                                if(sub1.equals(user.getUid())){
                                    score.setText(sub2);
                                }
                            }
                        }
                    }
                    get_details();
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
                    if(list_key.contains(ds.getKey())){
                        String dp=snapshot.child(Objects.requireNonNull(ds.getKey())).child("dplink").getValue(String.class);
                        String name=snapshot.child(ds.getKey()).child("name").getValue(String.class);
                        dp_list.add(dp);
                        name_list.add(name);
                    }
                }
                Collections.reverse(dp_list);
                Collections.reverse(name_list);
                Log.e("points",list_values+"");
                leaderAdapter leaderAdapter=new leaderAdapter(leaderboard.this,list_values,dp_list,name_list);
                leaderAdapter.notifyDataSetChanged();
                if(recyclerView!=null)
                    recyclerView.setAdapter(leaderAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}