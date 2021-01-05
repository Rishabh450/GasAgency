package com.example.sudikshagasagency.Adapter;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sudikshagasagency.Fragment.EditFragment;
import com.example.sudikshagasagency.ModalClass.Record;
import com.example.sudikshagasagency.R;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{
    List<Record> list;
    Context ctx;
    public RecordAdapter(List<Record> list,Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view,parent,false);
        return new RecordAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.ViewHolder holder, int position) {
        Record r = list.get(position);
        holder.t.setText("Name - " + r.getName());
        holder.t1.setText("Date - " + r.getDate());
        holder.t2.setText("Cylinder Type - " +r.getCylindertype());
        holder.t3.setText("Rate - " +r.getRate());
        holder.t4.setText("Cylinder Given - " +r.getTotalcylinder());
        holder.t5.setText("Cylinder Returned - " +r.getCylinderreturned());
        holder.t6.setText("Amount Given - " +r.getAmountgiven());
        holder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new EditFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, myFragment).addToBackStack(null).commit();
                Bundle args = new Bundle();
                args.putString("id", r.getTime());
                myFragment.setArguments(args);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView t,t1,t2,t3,t4,t5,t6;
        View mview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t = itemView.findViewById(R.id.name);
            t1 = itemView.findViewById(R.id.date);
            t2 = itemView.findViewById(R.id.type);
            t3 = itemView.findViewById(R.id.rate);
            t4 = itemView.findViewById(R.id.given);
            t5 = itemView.findViewById(R.id.returned);
            t6 = itemView.findViewById(R.id.amount_given);
            mview = itemView;
        }
    }
}
