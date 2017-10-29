package com.hezare.mmm.Adapters;

/**
 * Created by amirhododi on 8/2/2017.
 */

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hezare.mmm.App;
import com.hezare.mmm.Models.ClassListModel;
import com.hezare.mmm.Models.ClassListSubModel;
import com.hezare.mmm.R;

import java.util.List;

public class ClassSubListAdapter extends RecyclerView.Adapter<ClassSubListAdapter.MyViewHolder> {
    OnClickListner onCardClickListner;
    private  List<ClassListSubModel> moviesList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,matn;
        public ImageView icon;
        public RelativeLayout click;
        public CardView bg;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.class_grade_item);
            click = (RelativeLayout) view.findViewById(R.id.classgradeclick);
            bg = (CardView) view.findViewById(R.id.card_view);


        }
    }


    public ClassSubListAdapter(List<ClassListSubModel> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.classgradegrid_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ClassListSubModel ClassListModel = moviesList.get(position);
        holder.title.setText(ClassListModel.getTitle());
        holder.title.setTypeface(Typeface.createFromAsset(App.getContext().getAssets(), "font.ttf"));
        if(ClassListModel.getStat().matches("1")){
            holder.bg.setCardBackgroundColor(Color.parseColor("#71fe6e"));
        }else{
            holder.bg.setCardBackgroundColor(Color.parseColor("#ffffff"));

        }

        //holder.icon.setText(ElanatListModel.getMatn());




        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCardClickListner.OnClicked(v, position,moviesList);
            }
        });







    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public interface OnClickListner {
        void OnClicked(View view, int position, List<ClassListSubModel> moviesList);
    }

    public void setOnClickListner(OnClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }

}

