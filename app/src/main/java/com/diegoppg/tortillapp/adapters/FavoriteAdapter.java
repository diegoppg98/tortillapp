package com.diegoppg.tortillapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diegoppg.tortillapp.R;
import com.diegoppg.tortillapp.modelo.Tortilla;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private final List<Tortilla> listFavorites;

    public FavoriteAdapter(ArrayList<Tortilla> listFavorites) {
        this.listFavorites = listFavorites;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.favTortillaName.setText(listFavorites.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                Toast.makeText(view.getContext(), listFavorites.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        /*
        // implement setOnClickListener event on item view with lambda expression
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Clicked on: " + listFavorites.get(position).getName(), Toast.LENGTH_SHORT).show();
        });
        */
    }

    @Override
    public int getItemCount() {
        return listFavorites.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView favTortillaName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            favTortillaName = itemView.findViewById(R.id.favTortillaName);
        }
    }

}
