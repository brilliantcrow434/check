package com.example.notesapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.FolderModal;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class FolderAdapter extends FirestoreRecyclerAdapter<FolderModal, FolderAdapter.MyViewHolder> {

    Context context;

    public FolderAdapter(@NonNull FirestoreRecyclerOptions<FolderModal> options, Context context) {
        super(options);
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull FolderModal model) {
holder.folderName.setText(model.getFolderName());
holder.timeStamp.setText((ConvertTime.timestampToString(model.getTimestamp())));
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Add your views here
        TextView folderName,  timeStamp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folderName);

            timeStamp = itemView.findViewById(R.id.timestamp);
            // Initialize your views here
        }


    }
}
