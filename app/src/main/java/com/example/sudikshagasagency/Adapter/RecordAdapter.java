package com.example.sudikshagasagency.Adapter;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item,parent,false);
        return new RecordAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.ViewHolder holder, int position) {
        Record r = list.get(position);
        holder.tvName.setText(r.getName());
        holder.tvDate.setText(r.getDate());
        holder.tvOfferCategory.setText(r.getCylindertype());
        holder.tvRate.setText(ctx.getString(R.string.rupee_symbol)+ String.valueOf((r.getRate())));
        holder.amtReturn.setText(ctx.getString(R.string.rupee_symbol)+ String.valueOf(r.getAmountgiven()));
        holder.returned.setText(String.valueOf(r.getCylinderreturned()));
        holder.taken.setText(String.valueOf(r.getTotalcylinder()));
        holder.tvMinComission.setText(ctx.getString(R.string.rupee_symbol)+ String.valueOf(r.getTotal_amount()));
        holder.amtDue.setText(ctx.getString(R.string.rupee_symbol)+  String.valueOf( r.getTotal_amount()- r.getAmountgiven()));
        holder.tvId.setText("ID : " + r.getCode());

        Glide.with(ctx)
                .load(r.getPicture())
                .apply(RequestOptions.circleCropTransform())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        return false;
                    }
                })
                .into(holder.dp);

        if( r.getTotal_amount()- r.getAmountgiven()==0)
        {
            holder.status_image.setImageResource(R.drawable.ic_successful);
        }
        else
        {
            holder.status_image.setImageResource(R.drawable.ic_paymentimg);

        }
        holder.t8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new EditFragment();
                activity.getSupportFragmentManager().beginTransaction().add(R.id.frame_container, myFragment).addToBackStack(null).commit();
                Bundle args = new Bundle();
                args.putString("id", r.getTimeStamp());
                myFragment.setArguments(args);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvDate,tvOfferCategory,tvRate,amtReturn,returned,taken,amtDue,t8,tvMinComission,tvId;
        View mview; ImageView status_image,dp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvOfferCategory = itemView.findViewById(R.id.tvOfferCategory);
            tvRate = itemView.findViewById(R.id.tvRate);
            amtReturn = itemView.findViewById(R.id.tvAmtReturned);
            returned = itemView.findViewById(R.id.tvDeliveryIn);
            taken = itemView.findViewById(R.id.tvOfferType);
            amtDue = itemView.findViewById(R.id.tvAmtDue);
            t8 = itemView.findViewById(R.id.view_edit_order_id);
            tvMinComission = itemView.findViewById(R.id.tvMinComission);
            status_image = itemView.findViewById(R.id.status_image);
            tvId = itemView.findViewById(R.id.tvId);
            dp = itemView.findViewById(R.id.civProfileImage);

            mview = itemView;
        }
    }
}
