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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hezare.mmm.Adapters.ClassListAdapter;
import com.hezare.mmm.Adapters.ClassStudentListAdapter;
import com.hezare.mmm.Adapters.ClassStudentListDetailsAdapter;
import com.hezare.mmm.Adapters.ElanatListAdapter;
import com.hezare.mmm.Chat.ChatActivity;
import com.hezare.mmm.Fab.MyFab;
import com.hezare.mmm.Models.ClassListModel;
import com.hezare.mmm.Models.ClassStudentListModel;
import com.hezare.mmm.Models.ElanatListModel;
import com.hezare.mmm.WebSide.Parser;
import com.hezare.mmm.WebSide.SendRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Details extends AppCompatActivity {
    private List<ClassListModel> classlist = new ArrayList<>();
    private ClassListAdapter mAdapterclass;
    private List<ClassStudentListModel> studentlist = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private ClassStudentListDetailsAdapter mAdapterstudent;
    private EditText search;


    private List<ElanatListModel> elanatlist = new ArrayList<>();
    private ElanatListAdapter mAdapterelanat;


    int i = 0;


    LinearLayout studentslay,elanatlay;

    public enum DilogType {
        LOADING,
        ERROR
    }

    @Override
    protected void onDestroy() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("Day");
        editor.remove("Month");
        editor.apply();
        super.onDestroy();
    }

    ProgressDialog loading;
    String id;
    String BarnameHaftegiId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentlist);
        ((TextView)findViewById(R.id.texddtView)).setText("درس "+getIntent().getStringExtra("title"));
        id=getIntent().getStringExtra("id");
        BarnameHaftegiId=getIntent().getStringExtra("BarnameHaftegiId");
        Utli.StrictMode();
        Utli.changeFont(getWindow().getDecorView());
        init();

        BottomNavigationView bottombar=(BottomNavigationView)findViewById(R.id.navigation);
        disableShiftMode(bottombar);
        bottombar.setSelectedItemId(R.id.absent);
       /* TextView smallstudent = (TextView) bottombar.findViewById(R.id.absent).findViewById(R.id.smallLabel);
        TextView largestudent = (TextView) bottombar.findViewById(R.id.absent).findViewById(R.id.largeLabel);
        largestudent.setTextSize(20);
        smallstudent.setTextSize(15);
        TextView smallclass = (TextView) bottombar.findViewById(R.id.notifications).findViewById(R.id.smallLabel);
        TextView largeclass = (TextView) bottombar.findViewById(R.id.notifications).findViewById(R.id.largeLabel);
        largeclass.setTextSize(20);
        smallclass.setTextSize(15);*/
        bottombar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.notifications:
                        ShowElanat();
                        break;
                    case R.id.absent:
                     //   TabType=1;
                        ShowStudent();
                        break;
                    
                }

                return true;

            }
        });



        MakeDialog(DilogType.LOADING, null);
        SendRequest.GetPostListeDaneshAmoozaneClass(id);
        new SendRequest().setOnListClassStudentsCompleteListner(new SendRequest.OnListClassStudentsCompleteListner() {
            @Override
            public void OnListClassStudentsCompleteed(String response) {
                Log.e("Students : ",response);

                JSONArray j = Parser.Parse(response);
                List<String> contacts = new ArrayList<>();


                for (int i1=0; i1 < j.length(); i1++)
                {
                    try {
                        JSONObject oneObject = j.getJSONObject(i1);
                        String FirstName = oneObject.getString("FirstName");
                        String LastName = oneObject.getString("LastName");
                        String ID = oneObject.getString("ID");
                        if (loading.isShowing()) {
                            loading.dismiss();
                        }

                        //  USerID=ID;
                        String Name=FirstName+":"+LastName +"*"+ID;
                        contacts.add(Name);
                        // Log.e("Students : ",FirstName+"/"+LastName+"#"+ID);

                    } catch (JSONException e) {
                        MakeDialog(DilogType.ERROR, "تاکنون دانش آموزی  ثبت نشده!");
                    }
                }

                ParseNames(contacts);

                if(j.length()<1){
                    MakeDialog(DilogType.ERROR, "تاکنون دانش آموزی ثبت نشده!");

                }
            }
        });
        new SendRequest().setOnListClassStudentsErrorListner(new SendRequest.OnListClassStudentsErrorListner() {
            @Override
            public void OnListClassStudentsErrored(String response) {
                if (response.trim().contains("connectionError")) {
                    MakeDialog(DilogType.ERROR, "خطا در اتصال به سرور!");

                } else {
                    MakeDialog(DilogType.ERROR, "خطایی پیش آمده!");

                }
            }
        });


/*
        for (String name : a){
            Log.e("NAME",name);
            ClassListModel movie = new ClassListModel(name);
            movieList.add(movie);
            mAdapter.notifyDataSetChanged();
        }*/



    }

    private void ShowStudent() {
        studentslay.setVisibility(View.VISIBLE);
        elanatlay.setVisibility(View.GONE);
    }
    private void ShowElanat() {
        studentslay.setVisibility(View.GONE);
        elanatlay.setVisibility(View.VISIBLE);
        ReadElanat();

    }

    private void ReadElanat() {
        MakeDialog(DilogType.LOADING, null);
        SendRequest.SendPostReadElanat(id);
        new SendRequest().setOnReadElanatCompleteListner(new SendRequest.OnReadElanatCompleteListner() {
            @Override
            public void OnReadElanatCompleteed(String response) {
                Log.e("Elanat : ",response);
                elanatlist.clear();
                JSONArray j = Parser.Parse(response);
                if(j.length()<1){
                    MakeDialog(DilogType.ERROR, "تاکنون اعلانی ثبت نشده!");
                    return;
                }

                for (int i1=0; i1 < j.length(); i1++)
                {
                    try {
                        JSONObject oneObject = j.getJSONObject(i1);
                        String Matn = oneObject.getString("Matn");
                        String Movzoo = oneObject.getString("Movzoo");
                        String TarikheFarakhan = oneObject.getString("TarikheFarakhan");
                        if (loading.isShowing()) {
                            loading.dismiss();
                        }

                        ElanatListModel movie = new ElanatListModel(Movzoo,Matn,TarikheFarakhan);
                        elanatlist.add(movie);
                        mAdapterelanat.notifyDataSetChanged();

                    } catch (JSONException e) {
                        MakeDialog(DilogType.ERROR, "تاکنون اعلانی ثبت نشده!");
                    }
                }



            }
        });
        new SendRequest().setOnReadElanatErrorListner(new SendRequest.OnReadElanatErrorListner() {
            @Override
            public void OnReadElanatErrored(String response) {
                if (response.trim().contains("connectionError")) {
                    MakeDialog(DilogType.ERROR, "خطا در اتصال به سرور!");

                } else {
                    MakeDialog(DilogType.ERROR, "خطایی پیش آمده!");

                }
            }
        });
    }

    private void ParseNames(List<String> names) {

        for(char alphabet = 'ا'; alphabet <= 'ی';alphabet++) {
            List<String> contacts = getContactsWithLetter(alphabet,names);

            if (contacts.size() > 0) {
                //   ContactsSection contactsSection = new ContactsSection(String.valueOf(alphabet), contacts);
                //   sectionAdapter.addSection(contactsSection);
                Log.e("NAME","Header");
                ClassStudentListModel movie = new ClassStudentListModel(alphabet+"",true,null,null);
                studentlist.add(movie);
                for (String name : contacts){
                    Log.e("aaaa",name);
                    String stname = name.substring(0, name.lastIndexOf("*"));
                    String stidold = name.substring(name.lastIndexOf('*') + 1);

                    Log.e("stname",stname);
                    Log.e("stidold",stidold);


                    movie = new ClassStudentListModel(stname,false,stidold,null);
                    studentlist.add(movie);
                    mAdapterstudent.notifyDataSetChanged();
                }

            }}
    }
    private List<String> getContactsWithLetter(char letter, List<String> names) {
        List<String> contacts = new ArrayList<>();

        for (String contact : names) {
            String  lastName = contact.substring(contact.lastIndexOf(":")+1);
            if (lastName.charAt(0) == letter) {
                contacts.add(contact.replaceAll(":"," "));
            }
        }

        return contacts;
    }

    void filter(String text){
        List<ClassStudentListModel> temp = new ArrayList<>();
        for(ClassStudentListModel d: studentlist){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getTitle().contains(text)){
                temp.add(d);
            }
        }
        //update recyclerview
        mAdapterstudent.updateList(temp);



    }
    private void init() {
        search=(EditText)findViewById(R.id.stdudentsearchboxa);
        studentslay=(LinearLayout)findViewById(R.id.studentslay);
        elanatlay=(LinearLayout)findViewById(R.id.elanatlay);
        findViewById(R.id.allstudentsok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dl = new Dialog(Details.this);
                dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dl.setContentView(R.layout.dialog_absent);
                final CheckBox absent=(CheckBox)dl.findViewById(R.id.absentch);
                absent.setVisibility(View.GONE);
                final CheckBox takhirch=(CheckBox)dl.findViewById(R.id.takhirch);
                takhirch.setVisibility(View.GONE);
                final LinearLayout takhirlay=(LinearLayout)dl.findViewById(R.id.takjirlay);
                dl.findViewById(R.id.g).setVisibility(View.GONE);
                dl.findViewById(R.id.t).setVisibility(View.GONE);
                final AppCompatSpinner haftedarsiabsent=(AppCompatSpinner)dl.findViewById(R.id.haftedarsi);
                final EditText mothabsent=(EditText)dl.findViewById(R.id.month);
                final EditText dayabsent=(EditText)dl.findViewById(R.id.day);
                final EditText takhiredit=(EditText)dl.findViewById(R.id.takhiredit);
                final EditText detailsabsent=(EditText)dl.findViewById(R.id.details);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Details.this);
                String daystr = preferences.getString("Day", "");
                String monthstr = preferences.getString("Month", "");
                dayabsent.setText(daystr);
                mothabsent.setText(monthstr);

                ((TextView)dl.findViewById(R.id.studentname)).setText("تمام دانش آموزان");
                takhirch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            takhirlay.setVisibility(View.VISIBLE);
                            absent.setChecked(false);
                        }else{
                            takhirlay.setVisibility(View.GONE);
                            absent.setChecked(true);


                        }
                    }
                });
                absent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            takhirch.setChecked(false);
                            takhirlay.setVisibility(View.GONE);
                        }else{
                            takhirch.setChecked(true);
                            takhirlay.setVisibility(View.VISIBLE);
                        }
                    }
                });
                dl.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dl.dismiss();
                    }
                });
                dl.findViewById(R.id.saveandexit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mothabsent.getText().toString().matches("")) {
                            Toast.makeText(Details.this, "ماه نباید خالی باشد!", Toast.LENGTH_SHORT).show();
                            return;
                        }  else if (dayabsent.getText().toString().matches("")) {
                            Toast.makeText(Details.this, "روز نباید خالی باشد!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(takhirch.isChecked()){
                            if (takhiredit.getText().toString().matches("")) {
                                Toast.makeText(Details.this, "مدت تاخیر نباید خالی باشد!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        int dayint=Integer.parseInt(dayabsent.getText().toString());
                        if(dayint<10){
                            dayabsent.setText("");
                            dayabsent.setText("0"+dayint);
                        }
                        int monthint=Integer.parseInt(mothabsent.getText().toString());
                        if(monthint<10){
                            mothabsent.setText("");
                            mothabsent.setText("0"+monthint);
                        }
                        String takhir;

                        if(takhirch.isChecked()){
                            takhir=takhiredit.getText().toString();
                        }else{
                            takhir="-1";
                        }

                        MakeDialog(DilogType.LOADING,"درحال ثبت...");
                        int week=haftedarsiabsent.getSelectedItemPosition()+1;
                        SendRequest.SendPostAbsent("-1",BarnameHaftegiId,"1",detailsabsent.getText().toString(),week,mothabsent.getText().toString()+"/"+dayabsent.getText().toString());
                        new SendRequest().setOnAbsentCompleteListner(new SendRequest.OnAbsentCompleteListner() {
                            @Override
                            public void OnAbsentCompleteed(String response) {
                                try {
                                    JSONObject oneObject = Parser.Parse(response).getJSONObject(0);
                                    String Text = oneObject.getString("Text");
                                    String Key = oneObject.getString("Key");
                                    if (Key.matches("OK")) {
                                        if (loading.isShowing()) {
                                            loading.dismiss();
                                            dl.dismiss();
                                            Toast.makeText(Details.this, Text, Toast.LENGTH_SHORT).show();
                                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Details.this);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("Day",dayabsent.getText().toString());
                                            editor.putString("Month",mothabsent.getText().toString());
                                            editor.apply();
                                        }

                                    }else{
                                        MakeDialog(DilogType.ERROR,Text);

                                    }


                                }catch (JSONException e){
                                    MakeDialog(DilogType.ERROR,"خطایی در ثبت پیش آمده!");


                                }
                            }


                        });
                        new SendRequest().setOnAbsentErrorListner(new SendRequest.OnAbsentErrorListner() {
                            @Override
                            public void OnAbsentErrored(String response) {
                                if (response.trim().contains("connectionError")) {
                                    MakeDialog(DilogType.ERROR, "خطا در اتصال به سرور!");

                                } else {
                                    MakeDialog(DilogType.ERROR, "خطایی پیش آمده!");

                                };

                            }
                        });


                    }
                });
                dl.show();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_dview);

        mAdapterstudent = new ClassStudentListDetailsAdapter(studentlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapterstudent);



       /* for(char alphabet = 'ا'; alphabet <= 'ی';alphabet++) {
            List<String> contacts = getContactsWithLetter(alphabet);

            if (contacts.size() > 0) {
                //   ContactsSection contactsSection = new ContactsSection(String.valueOf(alphabet), contacts);
                //   sectionAdapter.addSection(contactsSection);
                Log.e("NAME","Header");
                ClassStudentListModel movie = new ClassStudentListModel(alphabet+"",true);
                studentlist.add(movie);
                for (String name : contacts){
                    Log.e("NAME",name);
                    movie = new ClassStudentListModel(name,false);
                    studentlist.add(movie);
                    mAdapterstudent.notifyDataSetChanged();
                }

            }}*/

        mAdapterstudent.setOnClickListner(new ClassStudentListDetailsAdapter.OnClickListner() {
            @Override
            public void OnClicked(View view, final int position, final List<ClassStudentListModel> moviesList) {
                final Dialog dl = new Dialog(Details.this);
                dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dl.setContentView(R.layout.dialog_absent);
                final CheckBox absent=(CheckBox)dl.findViewById(R.id.absentch);
                final CheckBox takhirch=(CheckBox)dl.findViewById(R.id.takhirch);
                final LinearLayout takhirlay=(LinearLayout)dl.findViewById(R.id.takjirlay);

                final AppCompatSpinner haftedarsiabsent=(AppCompatSpinner)dl.findViewById(R.id.haftedarsi);
                final EditText mothabsent=(EditText)dl.findViewById(R.id.month);
                final EditText dayabsent=(EditText)dl.findViewById(R.id.day);
                final EditText takhiredit=(EditText)dl.findViewById(R.id.takhiredit);
                final EditText detailsabsent=(EditText)dl.findViewById(R.id.details);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Details.this);
                String daystr = preferences.getString("Day", "");
                String monthstr = preferences.getString("Month", "");
                dayabsent.setText(daystr);
                mothabsent.setText(monthstr);

                ((TextView)dl.findViewById(R.id.studentname)).setText(moviesList.get(position).getTitle());
                takhirch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            takhirlay.setVisibility(View.VISIBLE);
                            absent.setChecked(false);
                        }else{
                            takhirlay.setVisibility(View.GONE);
                            absent.setChecked(true);


                        }
                    }
                });
                absent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            takhirch.setChecked(false);
                            takhirlay.setVisibility(View.GONE);
                        }else{
                            takhirch.setChecked(true);
                            takhirlay.setVisibility(View.VISIBLE);
                        }
                    }
                });
                dl.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dl.dismiss();
                    }
                });
                dl.findViewById(R.id.saveandexit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mothabsent.getText().toString().matches("")) {
                            Toast.makeText(Details.this, "ماه نباید خالی باشد!", Toast.LENGTH_SHORT).show();
                            return;
                        }  else if (dayabsent.getText().toString().matches("")) {
                            Toast.makeText(Details.this, "روز نباید خالی باشد!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(takhirch.isChecked()){
                            if (takhiredit.getText().toString().matches("")) {
                                Toast.makeText(Details.this, "مدت تاخیر نباید خالی باشد!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        int dayint=Integer.parseInt(dayabsent.getText().toString());
                        if(dayint<10){
                            dayabsent.setText("");
                            dayabsent.setText("0"+dayint);
                        }
                        int monthint=Integer.parseInt(mothabsent.getText().toString());
                        if(monthint<10){
                            mothabsent.setText("");
                            mothabsent.setText("0"+monthint);
                        }
                        String takhir;

                        if(takhirch.isChecked()){
                            takhir=takhiredit.getText().toString();
                        }else{
                            takhir="-1";
                        }

                        MakeDialog(DilogType.LOADING,"درحال ثبت...");
                        int week=haftedarsiabsent.getSelectedItemPosition()+1;
                        SendRequest.SendPostAbsent(moviesList.get(position).getStudentID(),BarnameHaftegiId,takhir,detailsabsent.getText().toString(),week,mothabsent.getText().toString()+"/"+dayabsent.getText().toString());
                        new SendRequest().setOnAbsentCompleteListner(new SendRequest.OnAbsentCompleteListner() {
                            @Override
                            public void OnAbsentCompleteed(String response) {
                                try {
                                    JSONObject oneObject = Parser.Parse(response).getJSONObject(0);
                                    String Text = oneObject.getString("Text");
                                    String Key = oneObject.getString("Key");
                                    if (Key.matches("OK")) {
                                        if (loading.isShowing()) {
                                            loading.dismiss();
                                            dl.dismiss();
                                            Toast.makeText(Details.this, Text, Toast.LENGTH_SHORT).show();
                                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Details.this);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("Day",dayabsent.getText().toString());
                                            editor.putString("Month",mothabsent.getText().toString());
                                            editor.apply();
                                        }

                                    }else{
                                        MakeDialog(DilogType.ERROR,Text);

                                    }


                                }catch (JSONException e){
                                    MakeDialog(DilogType.ERROR,"خطایی در ثبت پیش آمده!");


                                }
                            }


                        });
                        new SendRequest().setOnAbsentErrorListner(new SendRequest.OnAbsentErrorListner() {
                            @Override
                            public void OnAbsentErrored(String response) {
                                if (response.trim().contains("connectionError")) {
                                    MakeDialog(DilogType.ERROR, "خطا در اتصال به سرور!");

                                } else {
                                    MakeDialog(DilogType.ERROR, "خطایی پیش آمده!");

                                };

                            }
                        });


                    }
                });
                dl.show();

            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text


            }
        });








        recyclerView2 = (RecyclerView) findViewById(R.id.recycler_dviewd);

        mAdapterelanat = new ElanatListAdapter(elanatlist);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        recyclerView2.setLayoutManager(mLayoutManager2);
        recyclerView2.setHasFixedSize(false);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(mAdapterelanat);


        MyFab myFab = (MyFab)  findViewById(R.id.myFAB);
        myFab.attachToRecyclerView(recyclerView2);

        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              /*  i++;
                ElanatListModel movie = new ElanatListModel("تایتل اخبار "+i,"اینم هست متن اخبار که هر چند کاراکتر باشه مشکلی نیست و این متن برای تست اون هستش و برای اخبار شمارهاینم هست متن اخبار که هر چند کاراکتر باشه مشکلی نیست و این متن برای تست اون هستش و برای اخبار شمارهاینم هست متن اخبار که هر چند کاراکتر باشه مشکلی نیست و این متن برای تست اون هستش و برای اخبار شمارهاینم هست متن اخبار که هر چند کاراکتر باشه مشکلی نیست و این متن برای تست اون هستش و برای اخبار شمارهاینم هست متن اخبار که هر چند کاراکتر باشه مشکلی نیست و این متن برای تست اون هستش و برای اخبار شمارهاینم هست متن اخبار که هر چند کاراکتر باشه مشکلی نیست و این متن برای تست اون هستش و برای اخبار شمارهاینم هست متن اخبار که هر چند کاراکتر باشه مشکلی نیست و این متن برای تست اون هستش و برای اخبار شمارهاینم هست متن اخبار که هر چند کاراکتر باشه مشکلی نیست و این متن برای تست اون هستش و برای اخبار شمارهاینم هست متن اخبار که هر چند کاراکتر باشه مشکلی نیست و این متن برای تست اون هستش و برای اخبار شمارهاینم هست متن اخبار که هر چند کاراکتر باشه مشکلی نیست و این متن برای تست اون هستش و برای اخبار شماره "+i);
                elanatlist.add(movie);
                mAdapterelanat.notifyDataSetChanged();*/


                final Dialog dl = new Dialog(Details.this);
                dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dl.setContentView(R.layout.dialog_addelanat);
                final EditText title=(EditText)dl.findViewById(R.id.onvan);
                final EditText matn=(EditText)dl.findViewById(R.id.matn);
                final EditText month=(EditText)dl.findViewById(R.id.month);
                final EditText day=(EditText)dl.findViewById(R.id.day);
                dl.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(title.getText().toString().matches("")){
                            Toast.makeText(Details.this, "عنوان نباید خالی باشد", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(matn.getText().toString().matches("")){
                            Toast.makeText(Details.this, "توضیح نباید خالی باشد", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(month.getText().toString().matches("")){
                            Toast.makeText(Details.this, "ماه نباید خالی باشد", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(day.getText().toString().matches("")){
                            Toast.makeText(Details.this, "روز نباید خالی باشد", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        int dayint=Integer.parseInt(day.getText().toString());
                        if(dayint<10){
                            day.setText("");
                            day.setText("0"+dayint);
                        }
                        int monthint=Integer.parseInt(month.getText().toString());
                        if(monthint<10){
                            month.setText("");
                            month.setText("0"+monthint);
                        }

                        MakeDialog(DilogType.LOADING, null);
                        SendRequest.SendPostSendElanat(PreferenceManager.getDefaultSharedPreferences(Details.this).getString("ID","1"),id,title.getText().toString(),matn.getText().toString(),month.getText().toString()+"/"+day.getText().toString());
                        new SendRequest().setOnSendElanatCompleteListner(new SendRequest.OnSendElanatCompleteListner() {
                            @Override
                            public void OnSendElanatCompleteed(String response) {
                                Log.e("Elanat : ",response);


                                    try {
                                        JSONObject oneObject = Parser.Parse(response).getJSONObject(0);
                                        String Text = oneObject.getString("Text");
                                        String Key = oneObject.getString("Key");
                                        if (Key.trim().matches("OK")) {
                                            if (loading.isShowing()) {
                                                loading.dismiss();
                                            }

                                            Toast.makeText(Details.this, Text, Toast.LENGTH_SHORT).show();
                                            dl.dismiss();
                                            ReadElanat();

                                        }else{
                                            MakeDialog(DilogType.ERROR, Text);

                                        }



                                    } catch (JSONException e) {
                                        MakeDialog(DilogType.ERROR, "تاکنون اعلانی ثبت نشده!");
                                    }




                            }
                        });
                        new SendRequest().setOnSendElanatErrorListner(new SendRequest.OnSendElanatErrorListner() {
                            @Override
                            public void OnSendElanatErrored(String response) {
                                if (response.trim().contains("connectionError")) {
                                    MakeDialog(DilogType.ERROR, "خطا در اتصال به سرور!");

                                } else {
                                    MakeDialog(DilogType.ERROR, "خطایی پیش آمده!");

                                }
                            }
                        });




                    }
                });
                dl.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dl.dismiss();
                    }
                });
                dl.show();


            }
        });




    }



    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }
    private void MakeDialog(DilogType type, String Text) {
        if (type == DilogType.LOADING) {
            loading = new ProgressDialog(Details.this);
            loading.setMessage("درحال دریافت....");
            loading.setCancelable(false);
            loading.show();
        } else if (type == DilogType.ERROR) {
            if (loading.isShowing()) {
                loading.dismiss();
            }
            final AlertDialog alt = new AlertDialog.Builder(Details.this).create();
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
