package com.mosio.codechallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

import java.util.ArrayList;
import java.util.Objects;

public class questions extends AppCompatActivity {

    String key_of_the_room,admin_of_the_room;
    TextView quest,number,pass,done;
    ArrayList<String> question_list=new ArrayList<>();
    DatabaseReference reference,room_ref;
    FirebaseAuth auth;
    FirebaseUser user;
    int counter=1,count=0;
    int points=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Window window = questions.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(questions.this, R.color.white));

        key_of_the_room=getIntent().getStringExtra("key_of_the_room");
        admin_of_the_room=getIntent().getStringExtra("admin_of_the_room");

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        quest=findViewById(R.id.quest);
        number=findViewById(R.id.textView4);
        pass=findViewById(R.id.pass);
        done=findViewById(R.id.done);

        reference= FirebaseDatabase.getInstance().getReference().child("questions");
        room_ref= FirebaseDatabase.getInstance().getReference().child("Rooms");

        reference.child(admin_of_the_room).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot ds: snapshot.getChildren())
                        question_list.add(snapshot.child(Objects.requireNonNull(ds.getKey())).getValue(String.class));
                    String text="Question 1";
                    number.setText(text);
                    quest.setText(question_list.get(0));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        pass.setOnClickListener(v->{
            count++;
            counter++;
            if(count<question_list.size()){
                points=points-10;
                String text="Question "+counter;
                number.setText(text);
                quest.setText(question_list.get(count));
            }
            else{
                final_call();
            }
        });

        done.setOnClickListener(v->{
            count++;
            counter++;
            points=points+10;
            if(count==question_list.size()-1){
                String text="FINISH";
                done.setText(text);
            }
            if(count<question_list.size()){
                String text="Question "+counter;
                number.setText(text);
                quest.setText(question_list.get(count));
            }
            else{
                final_call();
            }
        });

    }

    private void final_call() {
        reference.child(key_of_the_room).child("leaderboard").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String push_key=reference.push().getKey();
                room_ref.child(key_of_the_room).child("leaderboard").child(push_key).setValue(user.getUid()+"/"+points);
                Intent intent=new Intent(questions.this,leaderboard.class);
                intent.putExtra("key_of_the_room",key_of_the_room);
                intent.putExtra("admin_of_the_room",admin_of_the_room);
                startActivity(intent);
                finish();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}