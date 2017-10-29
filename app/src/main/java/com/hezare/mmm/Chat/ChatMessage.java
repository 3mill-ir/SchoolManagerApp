package com.hezare.mmm.Chat;

/**
 * Created by himanshusoni on 06/09/15.
 */
public class ChatMessage {
    private boolean isImage, isMine,isHistory;
    private String content,id,date;

    public ChatMessage(String message, boolean mine, boolean image,boolean isHistory,String id,String date) {
        content = message;
        isMine = mine;
        isImage = image;
        this.isHistory = isHistory;
        this.id = id;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public boolean isMine() {
        return isMine;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setIsImage(boolean isImage) {
        this.isImage = isImage;
    }


    public boolean isHistory() {
        return isHistory;
    }

    public void setHistory(boolean isHistory) {
        this.isHistory = isHistory;
    }
}
