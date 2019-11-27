
package com.e.lesson_4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Vector;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    public   List<Task> list;
   private OnItemClickListener onItemClickListener;
   private AdapterView.OnItemLongClickListener onItemLongClickListener;




    public TaskAdapter(List<Task> list)
    {

        this.list =list;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_task,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
        }


   @Override
    public int getItemCount() {
        return list.size();
    }





    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textTitle;
        TextView textDesc;
        Task task;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle=itemView.findViewById(R.id.textTitleAdap);
            textDesc=itemView.findViewById(R.id.textDescAdap);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        onItemClickListener.onItemLongClick(getAdapterPosition());
                        return false;
                    }
                });
        }
        public void bind (Task task){
            this.task=task;
            textTitle.setText(task.getTitle());
            textDesc.setText(task.getDesc());
        }



    }
}
