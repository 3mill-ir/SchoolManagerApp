package com.hezare.mmm.Adapters;

/**
 * Created by amirhododi on 8/2/2017.
 */

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.hezare.mmm.App;
import com.hezare.mmm.Models.ClassStudentListModel;
import com.hezare.mmm.R;

import java.util.List;

public class ClassStudentListDetailsAdapter extends RecyclerView.Adapter<ClassStudentListDetailsAdapter.MyViewHolder> {
    OnClickListner onCardClickListner;
    private  List<ClassStudentListModel> moviesList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,alfabet;
        public RelativeLayout click;
        public LinearLayout item;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            alfabet = (TextView) view.findViewById(R.id.header);
            click = (RelativeLayout) view.findViewById(R.id.studentclick);


        }
    }


    public ClassStudentListDetailsAdapter(List<ClassStudentListModel> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.studentlist_row_hozorgyab, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ClassStudentListModel ClassStudentListModel = moviesList.get(position);
        holder.alfabet.setText(ClassStudentListModel.getTitle());
        holder.title.setText(ClassStudentListModel.getTitle());
        holder.title.setTypeface(Typeface.createFromAsset(App.getContext().getAssets(), "font.ttf"));
        holder.alfabet.setTypeface(Typeface.createFromAsset(App.getContext().getAssets(), "font.ttf"));

        if(ClassStudentListModel.getHeader()){
         /*   String name=ClassStudentListModel.getTitle();
            String lastName = "";
            String firstName= "";
            if(name.split("\\w+").length>1){

                lastName = name.substring(name.lastIndexOf(" ")+1);
                //   holder.alfabet.setText(lastName.substring(0,1));

            }*/
            holder.alfabet.setVisibility(View.VISIBLE);
            holder.title.setVisibility(View.GONE);

        }else{
            holder.alfabet.setVisibility(View.GONE);
            holder.title.setVisibility(View.VISIBLE);

        }


        if(!ClassStudentListModel.getHeader()){
            holder.click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCardClickListner.OnClicked(v, position,moviesList);
                }
            });

        }




    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void updateList(List<ClassStudentListModel> list){
        moviesList=list;
        notifyDataSetChanged();
    }
    public interface OnClickListner {
        void OnClicked(View view, int position, List<ClassStudentListModel> moviesList);
    }

    public void setOnClickListner(OnClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }
}

