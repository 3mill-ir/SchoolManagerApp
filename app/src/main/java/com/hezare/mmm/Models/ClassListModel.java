package com.hezare.mmm.Models;

import java.util.List;

/**
 * Created by amirhododi on 8/2/2017.
 */
public class ClassListModel {
    private String title ;
    private List<ClassListSubModel> sub;
    private int cnt;
    private int cnttotal;
    public ClassListModel() {
    }

    public ClassListModel(String title,List<ClassListSubModel> sub ,int cnt,int cnttotal) {
        this.title = title;
        this.sub = sub;
        this.cnt = cnt;
        this.cnttotal = cnttotal;




    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }


    public List<ClassListSubModel> getSub() {
        return sub;
    }

    public void setSub(List<ClassListSubModel> sub) {
        this.sub = sub;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }



    public int getCnttotal() {
        return cnttotal;
    }

    public void setCnttotal(int cnttotal) {
        this.cnttotal = cnttotal;
    }

}