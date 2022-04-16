package com.mosio.codechallenge.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mosio.codechallenge.R;
import com.mosio.codechallenge.Room;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class roomAdapter extends RecyclerView.Adapter<roomAdapter.ViewHolder> {

    ArrayList<String> dp_list,name_list;
    Context context;
    public roomAdapter(Room room, ArrayList<String> dp_list, ArrayList<String> name_list) {
        this.context=room;
        this.dp_list=dp_list;
        this.name_list=name_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design,parent,false);
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

    }

    @Override
    public int getItemCount() {
        return dp_list.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        CircleImageView profile_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.textView2);
            profile_image=itemView.findViewById(R.id.profile_image);
        }
    }
}
