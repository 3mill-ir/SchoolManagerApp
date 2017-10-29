package com.hezare.mmm.Models;

/**
 * Created by amirhododi on 8/2/2017.
 */
public class DorosListModel {
    private String NameDars ;
    private String BarnameHaftegiId ;



    public DorosListModel() {
    }

    public DorosListModel(String NameDars , String BarnameHaftegiId) {
        this.NameDars = NameDars;
        this.BarnameHaftegiId = BarnameHaftegiId;




    }

    public String getNameDars() {
        return NameDars;
    }

    public void setNameDars(String NameDars) {
        this.NameDars = NameDars;
    }


 public String getBarnameHaftegiId() {
        return BarnameHaftegiId;
    }

    public void setBarnameHaftegiId(String BarnameHaftegiId) {
        this.BarnameHaftegiId = BarnameHaftegiId;
    }




}