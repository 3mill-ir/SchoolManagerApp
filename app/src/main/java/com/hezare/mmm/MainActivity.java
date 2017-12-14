package com.hezare.mmm;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hezare.mmm.Adapters.ClassListAdapter;
import com.hezare.mmm.Adapters.ClassStudentListAdapter;
import com.hezare.mmm.Adapters.Drawer_List_Adapter;
import com.hezare.mmm.Chat.ChatActivity;
import com.hezare.mmm.Models.ClassListModel;
import com.hezare.mmm.Models.ClassListSubModel;
import com.hezare.mmm.Models.ClassStudentListModel;
import com.hezare.mmm.WebSide.Parser;
import com.hezare.mmm.WebSide.SendRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static int[] icons = {R.drawable.ic_edit_black_24dp, R.drawable.ic_info_black_24dp, R.drawable.ic_exit_to_app_black_24dp};
    public static String[] items = {"ویرایش رمز عبور", "درباره ما", "خروج از حساب کاربری"};
    LinearLayout studentslay, classnamelay;
    DrawerLayout drawer;
    ListView ListDrawer;
    ProgressDialog loading;
    int ratenum = 5;
    private List<ClassListModel> classlist = new ArrayList<>();
    private List<ClassListSubModel> classlistsub = new ArrayList<>();
    private ClassListAdapter mAdapterclass;
    private List<ClassStudentListModel> studentlist = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private ClassStudentListAdapter mAdapterstudent;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Utli.StrictMode();
        Utli.changeFont(getWindow().getDecorView());
        ((TextView)findViewById(R.id.name)).setText(PreferenceManager.getDefaultSharedPreferences(this).getString("Name",""));
        init();

        BottomNavigationView bottombar=(BottomNavigationView)findViewById(R.id.navigation);
        disableShiftMode(bottombar);
        bottombar.setSelectedItemId(R.id.students);
      /*  TextView smallstudent = (TextView) bottombar.findViewById(R.id.students).findViewById(R.id.smallLabel);
        TextView largestudent = (TextView) bottombar.findViewById(R.id.students).findViewById(R.id.largeLabel);
        largestudent.setTextSize(20);
        smallstudent.setTextSize(15);
        TextView smallclass = (TextView) bottombar.findViewById(R.id.classname).findViewById(R.id.smallLabel);
        TextView largeclass = (TextView) bottombar.findViewById(R.id.classname).findViewById(R.id.largeLabel);
        largeclass.setTextSize(20);
        smallclass.setTextSize(15);*/
        bottombar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.classname:
                        ShowClass();
                        break;
                    case R.id.students:
                     //   TabType=1;
                        ShowStudent();
                        break;

                }

                return true;

            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        findViewById(R.id.opendrawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.END);
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ListDrawer=(ListView) navigationView.findViewById(R.id.drawer_slidermenu);
        ListDrawer.setAdapter(new Drawer_List_Adapter(this, items,icons));
        ListDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        ShowChange();
                        break;


                    case 1:
                        startActivity(new Intent(MainActivity.this,About.class));
                        break;

                    case 2:
                        final AlertDialog alt = new AlertDialog.Builder(MainActivity.this).create();
                        alt.setTitle(Html.fromHtml("<p style=\"color:red;\">خروج</p>"));
                        alt.setMessage("تمایل دارید از حساب کاربری خود خارج شوید؟");
                        alt.setButton(Dialog.BUTTON_POSITIVE, "آره", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MakeDialog(DilogType.LOADING, null);
                                SendRequest.SendPostLogOut();
                                new SendRequest().setOnLogOutCompleteListner(new SendRequest.OnLogOutCompleteListner() {
                                    @Override
                                    public void OnLogOutCompleteed(String response) {
                                        try {
                                            JSONObject oneObject = Parser.Parse(response).getJSONObject(0);
                                            String Text = oneObject.getString("Text");
                                            String Key = oneObject.getString("Key");
                                            String Name = oneObject.getString("Option");
                                            if (Key.trim().matches("OK")) {
                                                if (loading.isShowing()) {
                                                    loading.dismiss();
                                                }
                                                Toast.makeText(MainActivity.this, Text, Toast.LENGTH_SHORT).show();
                                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.clear();
                                                editor.commit();
                                                finish();

                                            } else {
                                                MakeDialog(DilogType.ERROR, Text);

                                            }

                                        } catch (JSONException e) {

                                        }
                                    }
                                });
                                new SendRequest().setOnLogOutErrorListner(new SendRequest.OnLogOutErrorListner() {
                                    @Override
                                    public void OnLogOutErrored(String response) {
                                        if (response.trim().contains("connectionError")) {
                                            MakeDialog(DilogType.ERROR, "خطا در اتصال به سرور!");

                                        } else {
                                            MakeDialog(DilogType.ERROR, "خطایی پیش آمده!");

                                        }
                                    }
                                });


                            }
                        });

                        alt.setButton(Dialog.BUTTON_NEGATIVE, "نه", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alt.dismiss();

                            }
                        });
                        alt.show();
                }
            }
        });







        MakeDialog(DilogType.LOADING, null);
        SendRequest.GetPostListeDaneshAmoozaneMadrese(PreferenceManager.getDefaultSharedPreferences(this).getString("ID","1"));
        new SendRequest().setOnListeDaneshAmoozaneMadreseCompleteListner(new SendRequest.OnListeDaneshAmoozaneMadreseCompleteListner() {
            @Override
            public void OnListeDaneshAmoozaneMadreseCompleteed(String response) {
                JSONArray j =Parser.Parse(response);
                List<String> contacts = new ArrayList<>();
                Log.e("response",response);


                for (int i1=0; i1 < j.length(); i1++)
                {
                    try {
                        JSONObject oneObject = j.getJSONObject(i1);
                        String FullName = oneObject.getString("FullName");
                        String ID = oneObject.getString("ID");
                        String F_OvliaID = oneObject.getString("ChatId");
                        if (loading.isShowing()) {
                            loading.dismiss();
                        }

                        Log.e("Students : ",FullName+"#"+ID);
                      //  USerID=ID;
                        String Name=FullName+"*"+ID+"#"+F_OvliaID;
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
        new SendRequest().setOnListeDaneshAmoozaneMadreseErrorListner(new SendRequest.OnListeDaneshAmoozaneMadreseErrorListner() {
            @Override
            public void OnListeDaneshAmoozaneMadreseErrored(String response) {
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
        final SharedPreferences pref = getSharedPreferences("ShowDialog", 0);
        Log.i("showDialog", String.valueOf(pref.getBoolean("ShowDialog", false)));
        if (pref.getBoolean("ShowDialog", false)) {
            try {
                new AppUpdate(this).check_Version();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }




    }

    private void ShowStudent() {
        studentslay.setVisibility(View.VISIBLE);
        classnamelay.setVisibility(View.GONE);
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
                    String F_OvliaID = name.substring(name.lastIndexOf('#') + 1);
                    String stid=stidold.replace("#"+F_OvliaID,"");
                    Log.e("stname",stname);
                    Log.e("F_OvliaID",F_OvliaID);
                    Log.e("stid",stid);

                    movie = new ClassStudentListModel(stname,false,stid,F_OvliaID);
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
        classnamelay=(LinearLayout)findViewById(R.id.classnameslay);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapterclass = new ClassListAdapter(classlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(false);
        mAdapterclass.setHasStableIds(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapterclass);






        LoadMagateh();



      //  MakeDialog(DilogType.LOADING, null);








        mAdapterclass.setOnClickListner(new ClassListAdapter.OnClickListner() {
            @Override
            public void OnClicked(View view, int position, List<ClassListSubModel> moviesList) {
                Intent det=new Intent(MainActivity.this,Doros.class);
                det.putExtra("title",moviesList.get(position).getTitle());
                det.putExtra("id",moviesList.get(position).getID());
                startActivity(det);

            }
        });



        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getApplicationContext());

        recyclerView2 = (RecyclerView) findViewById(R.id.recycler_dview);
        mAdapterstudent = new ClassStudentListAdapter(studentlist);
        recyclerView2.setLayoutManager(mLayoutManager2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(mAdapterstudent);


        mAdapterstudent.setOnMaliListner(new ClassStudentListAdapter.OnMaliListner() {
            @Override
            public void OnMalied(View view, int position, List<ClassStudentListModel> moviesList) {
                Toast.makeText(MainActivity.this, "این امکان در ورژن بعدی اضافه خواهد شد", Toast.LENGTH_SHORT).show();
            }
        });

        mAdapterstudent.setOnRateListner(new ClassStudentListAdapter.OnRateListner() {
            @Override
            public void OnRateed(View view, final int position, final List<ClassStudentListModel> moviesList) {
                final Dialog dl = new Dialog(MainActivity.this);
                dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dl.setContentView(R.layout.dialog_rate);
                final AppCompatSpinner haftedarsi=(AppCompatSpinner)dl.findViewById(R.id.haftedarsi);
                final EditText moth=(EditText)dl.findViewById(R.id.month);
                final EditText day=(EditText)dl.findViewById(R.id.day);
                final EditText detailst=(EditText)dl.findViewById(R.id.details);
                final CheckBox rate=(CheckBox)dl.findViewById(R.id.starch);
                final CheckBox faild=(CheckBox)dl.findViewById(R.id.faildch);
                final SeekBar RateSeek=(SeekBar)dl.findViewById(R.id.seekBar);
                ((TextView)dl.findViewById(R.id.studentname)).setText(moviesList.get(position).getTitle());
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String daystr = preferences.getString("Day", "");
                String monthstr = preferences.getString("Month", "");
                day.setText(daystr);
                moth.setText(monthstr);
                dl.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dl.dismiss();
                    }
                });

                rate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            //takhirlay.setVisibility(View.VISIBLE);
                            faild.setChecked(false);
                        }else{
                            //  takhirlay.setVisibility(View.GONE);
                            faild.setChecked(true);

                        }
                    }
                });
                faild.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            rate.setChecked(false);
                            //takhirlay.setVisibility(View.GONE);
                        }else{
                            rate.setChecked(true);

                        }
                    }
                });

                dl.findViewById(R.id.saveandexit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (moth.getText().toString().matches("")) {
                            Toast.makeText(MainActivity.this, "ماه نباید خالی باشد!", Toast.LENGTH_SHORT).show();
                            return;
                        }  else if (day.getText().toString().matches("")) {
                            Toast.makeText(MainActivity.this, "روز نباید خالی باشد!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int dayint=Integer.parseInt(day.getText().toString());
                        if(dayint<10){
                            day.setText("");
                            day.setText("0"+dayint);
                        }
                        int monthint=Integer.parseInt(moth.getText().toString());
                        if(monthint<10){
                            moth.setText("");
                            moth.setText("0"+monthint);
                        }

                        if(rate.isChecked()){
                            ratenum=RateSeek.getProgress()+1;
                        }else{
                            ratenum=(RateSeek.getProgress()+1)*-1;

                        }


                        MakeDialog(DilogType.LOADING, null);
                        int week=haftedarsi.getSelectedItemPosition()+1;
                        SendRequest.SendPostTashvighoTanbiheKelasi(moviesList.get(position).getStudentID(),ratenum+"",detailst.getText().toString(),week+"",moth.getText().toString()+"/"+day.getText().toString());
                        new SendRequest().setOnTashvighoTanbiheKelasiCompleteListner(new SendRequest.OnTashvighoTanbiheKelasiCompleteListner() {
                            @Override
                            public void OnTashvighoTanbiheKelasiCompleteed(String response) {

                                try {
                                    JSONObject oneObject = Parser.Parse(response).getJSONObject(0);
                                    String Text = oneObject.getString("Text");
                                    String Key = oneObject.getString("Key");
                                    if (Key.matches("OK")) {
                                        if (loading.isShowing()) {
                                            loading.dismiss();
                                            dl.dismiss();
                                            Toast.makeText(MainActivity.this, Text, Toast.LENGTH_SHORT).show();
                                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("Day",day.getText().toString());
                                            editor.putString("Month",moth.getText().toString());
                                            editor.apply();
                                            dl.dismiss();
                                        }

                                    }else{
                                        MakeDialog(DilogType.ERROR,Text);

                                    }


                                }catch (JSONException e){
                                    MakeDialog(DilogType.ERROR,"خطایی در ثبت پیش آمده!");


                                }



                            }
                        });
                        new SendRequest().setOnTashvighoTanbiheKelasiErrorListner(new SendRequest.OnTashvighoTanbiheKelasiErrorListner() {
                            @Override
                            public void OnTashvighoTanbiheKelasiErrored(String response) {
                                if (response.trim().contains("connectionError")) {
                                    MakeDialog(DilogType.ERROR, "خطا در اتصال به سرور!");

                                } else {
                                    MakeDialog(DilogType.ERROR, "خطایی پیش آمده!");

                                }
                            }
                        });



                    }
                });




                dl.show();
            }
        });


        mAdapterstudent.setOnChatListner(new ClassStudentListAdapter.OnChatListner() {
            @Override
            public void OnChated(View view, int position, List<ClassStudentListModel> moviesList) {
                Intent chat=new Intent(MainActivity.this,ChatActivity.class);
                chat.putExtra("name",moviesList.get(position).getTitle());
                chat.putExtra("F_OvliaID",moviesList.get(position).getF_OvliaID());
                startActivity(chat);
            }
        });

/*

        mAdapterstudent.setOnRateClickListner(new ClassStudentListAdapter.OnClickRateListner() {
            @Override
            public void OnClicked(View view, int position, List<ClassStudentListModel> moviesList) {
                final Dialog dl = new Dialog(MainActivity.this);
                dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dl.setContentView(R.layout.dialog_rate);
                final CheckBox rate=(CheckBox)dl.findViewById(R.id.starch);
                final CheckBox faild=(CheckBox)dl.findViewById(R.id.faildch);
                ((TextView)dl.findViewById(R.id.studentname)).setText(moviesList.get(position).getTitle());
                rate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            //takhirlay.setVisibility(View.VISIBLE);
                            faild.setChecked(false);
                        }else{
                          //  takhirlay.setVisibility(View.GONE);

                        }
                    }
                });
                faild.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            rate.setChecked(false);
                            //takhirlay.setVisibility(View.GONE);
                        }else{

                        }
                    }
                });
                dl.show();
            }
        });

        mAdapterstudent.setOnChatClickListner(new ClassStudentListAdapter.OnClickChatListner() {
            @Override
            public void OnClicked(View view, int position, List<ClassStudentListModel> moviesList) {
                startActivity(new Intent(MainActivity.this,ChatActivity.class));
            }
        });

*/


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
    }

    private void LoadMagateh() {
        if(!classlist.isEmpty()){
            classlistsub.clear();
            classlist.clear();
        }
        SendRequest.GetPostListeMaghateBeHamraheKelasHa(PreferenceManager.getDefaultSharedPreferences(this).getString("ID","1"));
        new SendRequest().setOnListeMaghateBeHamraheKelasHaCompleteListner(new SendRequest.OnListeMaghateBeHamraheKelasHaCompleteListner() {
            @Override
            public void OnListeMaghateBeHamraheKelasHaCompleteed(String response) {
                JSONArray j =Parser.Parse(response);
                JSONArray jArray=null;


                for (int i1=0; i1 < j.length(); i1++)
                {
                    try {
                        JSONObject oneObject = j.getJSONObject(i1);
                        String MaghtaName = oneObject.getString("MaghtaName");
                        jArray = oneObject.getJSONArray("KelasHa");
                        String NaameKelas = null;
                        Log.e("MaghtaName", MaghtaName);
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jArrayoneObject = jArray.getJSONObject(i);
                            NaameKelas = jArrayoneObject.getString("NaameKelas");
                            String ID = jArrayoneObject.getString("ID");
                            String HuzurGhiabStatus = jArrayoneObject.getString("HuzurGhiabStatus");
                            Log.e("NaameKelas", NaameKelas);



                            ClassListSubModel subModel = new ClassListSubModel(NaameKelas,ID,HuzurGhiabStatus);
                            classlistsub.add(subModel);




                        }
                        Log.e("Size",classlistsub.size()+"");
                        ClassListModel movie = new ClassListModel(MaghtaName, classlistsub,classlistsub.size(),jArray.length());
                        classlist.add(movie);
                        mAdapterclass.notifyDataSetChanged();


                       /* ClassListModel movie = new ClassListModel("کلاس : "+i);
                        classlist.add(movie);*/


                    } catch (JSONException e) {
                        MakeDialog(DilogType.ERROR, "تاکنون کلاسی  ثبت نشده!");
                        Log.e("HuzurGhiabStatus",e.getMessage());
                    }
                }
                if (loading.isShowing()) {
                    loading.dismiss();
                    loading.dismiss();

                }

                if(j.length()<1){
                    MakeDialog(DilogType.ERROR, "تاکنون کلاسی ثبت نشده!");

                }
            }
        });
        new SendRequest().setOnListeMaghateBeHamraheKelasHaErrorListner(new SendRequest.OnListeMaghateBeHamraheKelasHaErrorListner() {
            @Override
            public void OnListeMaghateBeHamraheKelasHaErrored(String response) {
                if (response.trim().contains("connectionError")) {
                    MakeDialog(DilogType.ERROR, "خطا در اتصال به سرور!");

                } else {
                    MakeDialog(DilogType.ERROR, "خطایی پیش آمده!");

                }
            }
        });
    }

    private void ShowClass() {



        classnamelay.setVisibility(View.VISIBLE);
        studentslay.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.END)){
            drawer.closeDrawer(GravityCompat.END);

        }else{
            finish();

        }
    }

    private void MakeDialog(DilogType type, String Text) {
        if (type == DilogType.LOADING) {
            loading = new ProgressDialog(MainActivity.this);
            loading.setMessage("درحال دریافت....");
            loading.setCancelable(false);
            loading.show();
        } else if (type == DilogType.ERROR) {
            if (loading.isShowing()) {
                loading.dismiss();
            }
            final AlertDialog alt = new AlertDialog.Builder(MainActivity.this).create();
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

    private void ShowChange() {
        final Dialog dl = new Dialog(MainActivity.this);
        dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dl.setContentView(R.layout.changepass);
        final TextInputLayout oldWrapper = (TextInputLayout) dl.findViewById(R.id.oldWrapper);
        final TextInputLayout newWrapper = (TextInputLayout) dl.findViewById(R.id.newWrapper);
        final Button change = (Button) dl.findViewById(R.id.change);
        final Button cancel = (Button) dl.findViewById(R.id.cancel);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old = oldWrapper.getEditText().getText().toString().trim();
                final String newe = newWrapper.getEditText().getText().toString().trim();
                if (old.matches("")) {
                    oldWrapper.setError("رمز عبور فعلی نباید خالی باشد");
                    return;
                } else {
                    oldWrapper.setErrorEnabled(false);

                }
                if (newe.matches("")) {
                    newWrapper.setError("رمز عبور جدید نباید خالی باشد");
                    return;
                } else {
                    newWrapper.setErrorEnabled(false);
                }


                MakeDialog(DilogType.LOADING, null);
                SendRequest.SendPostChangePass(old,newe);
                new SendRequest().setOnChangePassCompleteListner(new SendRequest.OnChangePassCompleteListner() {
                    @Override
                    public void OnChangePassCompleteed(String response) {
                        try {
                            JSONObject oneObject = Parser.Parse(response).getJSONObject(0);
                            String Text = oneObject.getString("Text");
                            String Key = oneObject.getString("Key");
                            if (Key.trim().matches("Success")) {
                                if (loading.isShowing()) {
                                    loading.dismiss();
                                }
                                dl.dismiss();
                                Toast.makeText(MainActivity.this, Text, Toast.LENGTH_SHORT).show();
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.commit();
                                startActivity(new Intent(MainActivity.this,Login.class));
                                finish();

                            } else {
                                MakeDialog(DilogType.ERROR, Text);

                            }

                        } catch (JSONException e) {

                        }
                    }
                });
                new SendRequest().setOnChangePassErrorListner(new SendRequest.OnChangePassErrorListner() {
                    @Override
                    public void OnChangePassErrored(String response) {
                        if (response.trim().contains("connectionError")) {
                            MakeDialog(DilogType.ERROR, "خطا در اتصال به سرور!");

                        } else {
                            MakeDialog(DilogType.ERROR, "خطایی پیش آمده!");

                        }
                    }
                });


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.dismiss();
            }
        });

        dl.show();

    }

    @Override
    public void onRestart() {
        super.onRestart();
        LoadMagateh();


    }

    public enum DilogType {
        LOADING,
        ERROR
    }}
