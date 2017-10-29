package com.hezare.mmm.Models;

/**
 * Created by amirhododi on 8/2/2017.
 */
public class ClassListSubModel {
    private String title ;
    private String ID ;
    private String Stat ;


    public ClassListSubModel() {
    }

    public ClassListSubModel(String title,String ID,String Stat ) {
        this.title = title;
        this.ID = ID;
        this.Stat = Stat;




    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    public String getStat() {
        return Stat;
    }

    public void setStat(String Stat) {
        this.Stat = Stat;
    }

}