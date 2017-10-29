package com.hezare.mmm;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.hezare.mmm.Adapters.DorosListAdapter;
import com.hezare.mmm.Models.DorosListModel;
import com.hezare.mmm.WebSide.Parser;
import com.hezare.mmm.WebSide.SendRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Doros extends AppCompatActivity{


    private List<DorosListModel> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DorosListAdapter mAdapter;
    public enum DilogType {
        LOADING,
        ERROR
    }
    ProgressDialog loading;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doros);
        Utli.StrictMode();
        Utli.changeFont(getWindow().getDecorView());
        ((TextView)findViewById(R.id.classname)).setText("کلاس "+getIntent().getStringExtra("title"));
        id=getIntent().getStringExtra("id");
        DoLoad();












        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new DorosListAdapter(movieList);
         RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);




        mAdapter.setOnClickListner(new DorosListAdapter.OnClickListner() {
            @Override
            public void OnClicked(View view, int position, List<DorosListModel> moviesList) {
                Intent details=new Intent(Doros.this,Details.class);
                details.putExtra("title",moviesList.get(position).getNameDars());
                details.putExtra("id",id);
                details.putExtra("BarnameHaftegiId",moviesList.get(position).getBarnameHaftegiId());
              /*  details.putExtra("title",moviesList.get(position).getTitle());
                details.putExtra("matn",moviesList.get(position).getMatn());
                details.putExtra("date",moviesList.get(position).getDate());*/
                startActivity(details);
            }
        });





       /* ClassStudentListModel movie = new ClassStudentListModel("رضا محمدی");
        movieList.add(movie);

        movie = new ClassStudentListModel("محمد علیزاده");
        movieList.add(movie);
        movie = new ClassStudentListModel("علی رضایی");
        movieList.add(movie);
        movie = new ClassStudentListModel("آرین حاجی زاده");
        movieList.add(movie);
        movie = new ClassStudentListModel("محمد جواد نژاد");
        movieList.add(movie);
        movie = new ClassStudentListModel("صدرا مختاری");
        movieList.add(movie);
        movie = new ClassStudentListModel("امیر حدودی آذر");
        movieList.add(movie);
        movie = new ClassStudentListModel("مبین حسینی");
        movieList.add(movie);
        movie = new ClassStudentListModel("سینا نعمتی");
        movieList.add(movie);
        movie = new ClassStudentListModel("مهدی بابایی");
        movieList.add(movie);
        movie = new ClassStudentListModel("صادق رضالو");
        movieList.add(movie);
        movie = new ClassStudentListModel("آرین دوستی");
        movieList.add(movie);
        movie = new ClassStudentListModel("پوریا شمس");
        movieList.add(movie);



        mAdapter.notifyDataSetChanged();*/







    }
    private void DoLoad() {
        MakeDialog(DilogType.LOADING, null);
        SendRequest.GetPostListDoros(id);
        new SendRequest().setOnListDorosCompleteListner(new SendRequest.OnListDorosCompleteListner() {
            @Override
            public void OnListDorosCompleteed(String response) {
                if (loading.isShowing()) {
                    loading.dismiss();
                }
                Log.e("Json : ",response);
                try {
                    JSONArray DorosArray = Parser.Parse(response);

                    for (int i=0; i < DorosArray.length(); i++) {
                        JSONObject DorosArrayObj = DorosArray.getJSONObject(i);
                        String  NameDars = DorosArrayObj.getString("NameDars");
                        String  BarnameHaftegiId = DorosArrayObj.getString("BarnameHaftegiId");
                       DorosListModel movie = new DorosListModel(NameDars,BarnameHaftegiId);
                        movieList.add(movie);
                        mAdapter.notifyDataSetChanged();


                    }

                    if(DorosArray.length()<1){
                        MakeDialog(DilogType.ERROR, "درسی در سیستم ثبت نشده!");

                    }


                } catch (Exception e) {
                    MakeDialog(DilogType.ERROR, "خطایی پیش آمده!");
                    Log.e("Json : ",e.getMessage());


                }
            }
        });
        new SendRequest().setOnListDorosErrorListner(new SendRequest.OnListDorosErrorListner() {
            @Override
            public void OnListDorosErrored(String response) {
                if (response.trim().contains("connectionError")) {
                    MakeDialog(DilogType.ERROR, "خطا در اتصال به سرور!");

                } else {
                    MakeDialog(DilogType.ERROR, "خطایی پیش آمده!");

                }
            }
        });


    }
    private void MakeDialog(DilogType type, String Text) {
        if (type == DilogType.LOADING) {
            loading = new ProgressDialog(Doros.this);
            loading.setMessage("درحال بارگذاری...");
            loading.setCancelable(false);
            loading.show();
        } else if (type == DilogType.ERROR) {
            if (loading.isShowing()) {
                loading.dismiss();
            }
            final AlertDialog alt = new AlertDialog.Builder(Doros.this).create();
            alt.setTitle(Html.fromHtml("<p style=\"color:red;\">خطا!</p>"));
            alt.setMessage(Text);
            alt.setButton(Dialog.BUTTON_POSITIVE, "تمام", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alt.dismiss();

                }
            });
            alt.show();
        }
    }

}

