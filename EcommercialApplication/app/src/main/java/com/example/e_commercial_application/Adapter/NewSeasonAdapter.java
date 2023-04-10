package com.example.e_commercial_application.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commercial_application.Model.NewSeason;
import com.example.e_commercial_application.R;

import java.util.List;

public class NewSeasonAdapter extends RecyclerView.Adapter<NewSeasonAdapter.NewSeasonViewHolder> {

    private List<NewSeason> newSeasonList;


    public NewSeasonAdapter(List<NewSeason> newSeasonList) {
        this.newSeasonList = newSeasonList;
    }


    @NonNull
    @Override
    public NewSeasonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_season_layout,parent,false);
        return new NewSeasonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewSeasonViewHolder holder, int position) {
        holder.imageView.setImageResource(newSeasonList.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return newSeasonList.size();
    }

    public class NewSeasonViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        public NewSeasonViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.newSeasonImg);
        }
    }
}
