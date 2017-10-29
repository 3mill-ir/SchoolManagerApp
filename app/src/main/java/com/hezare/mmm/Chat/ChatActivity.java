package com.hezare.mmm.Chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.hezare.mmm.R;
import com.hezare.mmm.Utli;
import com.hezare.mmm.WebSide.Parser;
import com.hezare.mmm.WebSide.SendRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;



    private ChatMessageAdapter mAdapter;
    String name,F_OvliaID;
    boolean loading=false;
    ProgressBar loadingpb;
    int cnt=1;
    boolean update=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Utli.StrictMode();
        Utli.changeFont(getWindow().getDecorView());
        name=getIntent().getStringExtra("name");
        F_OvliaID=getIntent().getStringExtra("F_OvliaID");
        Log.e("F_OvliaID",F_OvliaID);
        ((TextView)findViewById(R.id.student_name)).setText(""+name);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        loadingpb = (ProgressBar) findViewById(R.id.loading);

        DoLoadRecent(cnt);
        final LinearLayoutManager  l=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(l);

        mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        mRecyclerView.setHasFixedSize(false);
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);

        final ChatBarView chatBarView = (ChatBarView) findViewById(R.id.chatbar);
        chatBarView.setSendClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mEditTextMessage = chatBarView.getMessageText();
                String message = mEditTextMessage.toString();
                if (TextUtils.isEmpty(message)) {
                    return;
                }
                sendMessage(message);
            }
        });


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if(l.findFirstVisibleItemPosition()==0){
                        if(!loading){
                            cnt++;
                            loadingpb.setVisibility(View.VISIBLE);
                            loading=true;
                            update=true;
                            DoLoadRecent(cnt);

                        }
                    }


            }
        });



    }

    private void DoLoadRecent(int i) {
        Log.e("CNT",i+"");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ChatActivity.this);
        final String CHATID = preferences.getString("CHATID", "");
        Log.e("CHATID",CHATID);

        SendRequest.SendPostReadChat(CHATID,F_OvliaID,i);
        new SendRequest().setOnReadChatCompleteListner(new SendRequest.OnReadChatCompleteListner() {
            @Override
            public void OnReadChatCompleteed(String response) {
                JSONArray j = Parser.Parse(response);
                for (int i1=0; i1 < j.length(); i1++) {
                    try {
                        JSONObject oneObject = j.getJSONObject(i1);
                        String ID = oneObject.getString("ID");
                        String Type = oneObject.getString("Type");
                        String Text = oneObject.getString("Text");
                        String Tarikh = oneObject.getString("Tarikh");
                        if(Type.trim().matches("1")){
                            ChatMessage chatMessage = new ChatMessage(Text, true, false,true,ID,Tarikh);
                            mAdapter.add(chatMessage,update);



                        }else{
                            ChatMessage chatMessage = new ChatMessage(Text, false, false,true,ID,Tarikh);
                            mAdapter.add(chatMessage,update);
                        }


                    } catch (JSONException e) {
                    }

                }
                if(loading){

                }else{
                    mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

                }
                loadingpb.setVisibility(View.INVISIBLE);
                loading=false;
                update=false;



            }
        });
        new SendRequest().setOnReadChatErrorListner(new SendRequest.OnReadChatErrorListner() {
            @Override
            public void OnReadChatErrored(String response) {
                loadingpb.setVisibility(View.INVISIBLE);
                loading=false;
                if (response.trim().contains("connectionError")) {
                    //   mAdapter.setItemState(mAdapter.getItemCount() - 1,3);

                    //err

                } else {
                    //      mAdapter.setItemState(mAdapter.getItemCount() - 1,3);

                    //err

                }
            }
        });
    }

    private void sendMessage(final String message) {
       // mAdapter.setItemState(mAdapter.getItemCount() - 1,0);
        String date = new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(new Date());

        ChatMessage chatMessage = new ChatMessage(message, true, false,false,(mAdapter.getItemCount() - 1)+"",date);
        mAdapter.add(chatMessage,update);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ChatActivity.this);
        final String CHATID = preferences.getString("CHATID", "");
        Log.e("CHATID",CHATID);

        SendRequest.SendPostSendChat(message,CHATID,F_OvliaID);
        new SendRequest().setOnSendChatCompleteListner(new SendRequest.OnSendChatCompleteListner() {
            @Override
            public void OnSendChatCompleteed(String response) {
                Log.e("response",response);

                if (response.trim().replace("\"","").matches("OK")) {

                   /* ChatMessage a=  mAdapter.getItemState(mAdapter.getItemCount() - 1);
                    Toast.makeText(ChatActivity.this, a.getContent(), Toast.LENGTH_SHORT).show();*/

                    mAdapter.setItemState(mAdapter.getItemCount() - 1,1);

                    final Toast toast = Toast.makeText(getApplicationContext(), "ارسال شد", Toast.LENGTH_SHORT);
                    toast.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 500);
                    mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

                    //ok
                } else {
                    //err
                    mAdapter.setItemState(mAdapter.getItemCount() - 1,3);
                    final Toast toast = Toast.makeText(getApplicationContext(), "خطا در ارسال", Toast.LENGTH_SHORT);
                    toast.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 500);

                }
            }
        });
        new SendRequest().setOnSendChatErrorListner(new SendRequest.OnSendChatErrorListner() {
            @Override
            public void OnSendChatErrored(String response) {
                if (response.trim().contains("connectionError")) {
                    mAdapter.setItemState(mAdapter.getItemCount() - 1,3);

                    //err

                } else {
                    mAdapter.setItemState(mAdapter.getItemCount() - 1,3);

                    //err

                }
            }
        });




       // mimicOtherMessage(message);
    }

    private void mimicOtherMessage(String message) {
        String date = new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(new Date());

        ChatMessage chatMessage = new ChatMessage(message, false, false,false,(mAdapter.getItemCount() - 1)+"",date);
        mAdapter.add(chatMessage,update);

        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private void sendMessage() {
        String date = new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(new Date());
        ChatMessage chatMessage = new ChatMessage(null, true, true,false,(mAdapter.getItemCount() - 1)+"",date);
        mAdapter.add(chatMessage,update);

        mimicOtherMessage();
    }

    private void mimicOtherMessage() {
        String date = new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(new Date());
        ChatMessage chatMessage = new ChatMessage(null, false, true,false,(mAdapter.getItemCount() - 1)+"",date);
        mAdapter.add(chatMessage,update);

        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }


}
