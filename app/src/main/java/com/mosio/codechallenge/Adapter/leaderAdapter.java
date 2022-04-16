package com.mosio.codechallenge.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mosio.codechallenge.R;
import com.mosio.codechallenge.leaderboard;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class leaderAdapter extends RecyclerView.Adapter<leaderAdapter.ViewHolder>{

    Context context;
    ArrayList<String> dp_list,name_list;
    ArrayList<Integer> list_values;

    public leaderAdapter(leaderboard leaderboard, ArrayList<Integer> list_values,ArrayList<String> dp_list,ArrayList<String> name_list) {
        this.context=leaderboard;
        this.dp_list=dp_list;
        this.list_values=list_values;
        this.name_list=name_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design_2,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .asBitmap()
                .load(dp_list.get(position))
                .placeholder(R.drawable.ic_camera)
                .into(holder.profile_image);

        holder.name.setText(name_list.get(position));
        /*String text=list_values.get(position)+"";
        holder.points.setText(text);*/
        /*if(Collections.max(list_values).equals(list_values.get(position)))
            holder.crown.setVisibility(View.VISIBLE);
        else
            holder.crown.setVisibility(View.GONE);*/
    }

    @Override
    public int getItemCount() {
        return dp_list.size();
    }


    protected static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        CircleImageView profile_image;
        TextView points;
        ImageView crown;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.textView2);
            profile_image=itemView.findViewById(R.id.profile_image);
            points=itemView.findViewById(R.id.points);
            crown=itemView.findViewById(R.id.imageView3);
        }
    }
}
