package com.example.firebaseproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    ArrayList<Notes> list  ;

    public NotesAdapter(ArrayList<Notes> list) {

        this.list = list ;
    }

    @Override
    public NotesViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notes, parent, false);

        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotesAdapter.NotesViewHolder holder, int position) {
        Notes n = list.get(position) ;

        holder.tvTitle.setText(n.getTitle());
        holder.tvSubtitle.setText(n.getSubtitle());
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder {
         TextView tvTitle , tvSubtitle ;

        public NotesViewHolder( View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle) ;
            tvSubtitle = (TextView) itemView.findViewById(R.id.tvSubTitle) ;
        }


    }
}
