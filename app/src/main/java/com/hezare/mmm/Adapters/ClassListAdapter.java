package com.hezare.mmm.Adapters;

/**
 * Created by amirhododi on 8/2/2017.
 */

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hezare.mmm.App;
import com.hezare.mmm.Models.ClassListModel;
import com.hezare.mmm.Models.ClassListSubModel;
import com.hezare.mmm.R;
import com.hezare.mmm.Utli;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.MyViewHolder> {
    OnClickListner onCardClickListner;
    private  List<ClassListModel> moviesList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,matn;
        public ImageView icon;
        public RelativeLayout click;
        public CardView bg;
        public RecyclerView recyclerView;
        public List<ClassListSubModel> movieList11 = new ArrayList<>();
        public ClassSubListAdapter mAdapter;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.header);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_sub);
            click = (RelativeLayout) view.findViewById(R.id.classheader);
        //    bg = (CardView) view.findViewById(R.id.card_view);


        }
    }


    public ClassListAdapter(List<ClassListModel> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.class_header_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ClassListModel ClassListModel = moviesList.get(position);
        holder.title.setText(ClassListModel.getTitle());
        holder.title.setTypeface(Typeface.createFromAsset(App.getContext().getAssets(), "font.ttf"));

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.click.setBackgroundColor(color);


        Log.e("CCNT",ClassListModel.getCnt()+"");


        if(holder.movieList11.size()<=0){
            showgrid(holder,ClassListModel);

        }




        //holder.icon.setText(ElanatListModel.getMatn());




     /*   holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCardClickListner.OnClicked(v, position,moviesList);
            }
        });
*/






    }

    private void showgrid(MyViewHolder holder, ClassListModel position) {
        holder.mAdapter = new ClassSubListAdapter(holder.movieList11);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(App.getContext(), 4);
        holder.recyclerView.setLayoutManager(mLayoutManager);
        holder.recyclerView.setHasFixedSize(false);
        holder.mAdapter.setHasStableIds(true);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(holder.mAdapter);


        List<ClassListSubModel> mm=position.getSub();

        for (int i=position.getCnt()-position.getCnttotal();  i < position.getCnt();i++){
            ClassListSubModel movie =mm.get(i);
            holder.movieList11.add(movie);
            holder.mAdapter.notifyDataSetChanged();
        }

        holder.mAdapter.setOnClickListner(new ClassSubListAdapter.OnClickListner() {
            @Override
            public void OnClicked(View view, int position, List<ClassListSubModel> moviesList) {
                onCardClickListner.OnClicked(view, position,moviesList);
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

