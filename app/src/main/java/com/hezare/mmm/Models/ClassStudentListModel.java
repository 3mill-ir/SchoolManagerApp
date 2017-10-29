package com.hezare.mmm.Models;

import android.view.View;

/**
 * Created by amirhododi on 8/2/2017.
 */
public class ClassStudentListModel {
    private String title,StudentID,F_OvliaID ;
    private Boolean header ;


    public ClassStudentListModel() {
    }

    public ClassStudentListModel(String title,Boolean header,String StudentID,String F_OvliaID) {
        this.title = title;
        this.header = header;
        this.StudentID = StudentID;
        this.F_OvliaID = F_OvliaID;




    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }



    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String StudentID) {
        this.StudentID = StudentID;
    }


  public String getF_OvliaID() {
        return F_OvliaID;
    }

    public void setF_OvliaID(String F_OvliaID) {
        this.F_OvliaID = F_OvliaID;
    }




    public Boolean getHeader() {
        return header;
    }

    public void setHeader(Boolean header) {
        this.header = header;
    }



}