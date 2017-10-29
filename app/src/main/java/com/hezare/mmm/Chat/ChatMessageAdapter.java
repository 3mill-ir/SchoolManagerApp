package com.hezare.mmm.Chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.hezare.mmm.R;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.himanshusoni.chatmessageview.ChatMessageView;


/**
 * Created by himanshusoni on 06/09/15.
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MessageHolder> {
    private final String TAG = "ChatMessageAdapter";
    private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1;
    private int stat=0;
    private List<ChatMessage> mMessages;
    private Context mContext;
    private boolean show=false;
    private boolean recent=false;
    public ChatMessageAdapter(Context context, List<ChatMessage> data) {
        mContext = context;
        mMessages = data;
    }

    @Override
    public int getItemCount() {
        return mMessages == null ? 0 : mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage item = mMessages.get(position);

        if (item.isMine()) return MY_MESSAGE;
        else return OTHER_MESSAGE;
    }

   public void setItemState(int position,int status) {
       // ChatMessage item = mMessages.get(position);

        stat=status;
        show=false;
        notifyItemChanged(mMessages.size() - 1);


      //  return item;
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MY_MESSAGE) {
            return new MessageHolder(LayoutInflater.from(mContext).inflate(R.layout.item_mine_message, parent, false));
        } else {
            return new MessageHolder(LayoutInflater.from(mContext).inflate(R.layout.item_other_message, parent, false));
        }
    }

    public void add(ChatMessage message,boolean update) {
      /*  Collections.sort(mMessages, new Comparator<ChatMessage>(){
            public int compare(ChatMessage obj1, ChatMessage obj2) {
                // ## Ascending order
              //  return obj1.getContent().compareToIgnoreCase(obj2.getContent()); // To compare string values
                // return Integer.valueOf(obj1.getId()).compareTo(Integer.valueOf(obj1.getId())); // To compare integer values

                // ## Descending order
                // return obj2.getContent().compareToIgnoreCase(obj1.getContent()); // To compare string values
                int obj1id=Integer.valueOf(obj1.getId());
                int obj2id=Integer.valueOf(obj2.getId());
               //  return Integer.valueOf(obj1id).compareTo(obj2id);
                 return Integer.valueOf(obj1id).compareTo(obj2id); // To compare integer values
            }
        });*/
       if(update){
           mMessages.add(0, message);
           notifyItemInserted(0);
       }else{
           mMessages.add(message);
           notifyDataSetChanged();
       }
     //   mRecyclerView.smoothScrollToPosition(0);
        //Collections.reverse(mMessages);
      /*  mMessages.add(message);
        notifyDataSetChanged();*/
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(final MessageHolder holder, final int position) {
        ChatMessage chatMessage = mMessages.get(position);
        Log.e("id",chatMessage.getId()+"");
        if (chatMessage.isImage()) {
            holder.ivImage.setVisibility(View.VISIBLE);
            holder.tvMessage.setVisibility(View.GONE);

            holder.ivImage.setImageResource(R.drawable.img_sample);
        } else {
            holder.ivImage.setVisibility(View.GONE);
            holder.tvMessage.setVisibility(View.VISIBLE);

            holder.tvMessage.setText(chatMessage.getContent());
        }
       /* if(getItemViewType(position)==MY_MESSAGE) {
            if (!show) {
                if (stat == 1) {
                    holder.ivStat.setVisibility(View.VISIBLE);
                    holder.ivStat.setImageResource(R.drawable.sendok);
                    Log.e("Send","OK");

                    show = true;

                } else if (stat == 3) {
                    holder.ivStat.setVisibility(View.VISIBLE);
                    holder.ivStat.setImageResource(R.drawable.senderr);
                    Log.e("Send","Err");
                    show = true;


                }
                if(chatMessage.isHistory()){
                    holder.ivStat.setVisibility(View.INVISIBLE);
                    show = true;


                }
            }


        }*/


        holder.tvTime.setText(chatMessage.getDate());

        holder.chatMessageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;
        ImageView ivImage;
        ImageView ivStat;
        ChatMessageView chatMessageView;

        MessageHolder(View itemView) {
            super(itemView);
            chatMessageView = (ChatMessageView) itemView.findViewById(R.id.chatMessageView);
            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            ivStat = (ImageView) itemView.findViewById(R.id.ic_stat);
        }
    }
}