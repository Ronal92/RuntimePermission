package com.jinwoo.android.runtimepermission;

import java.util.ArrayList;

/**
 *  전화번호부 POJO(Pure Old Java Object)
 *
 *  Created by JINWOO on 2017-02-01.
 */


public class Contact {
    private int id;
    private String name;
    private ArrayList<String> tel;

    public Contact(){
        tel = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getTel() {
        return tel;
    }

    public String getTelOne(){
        if( tel.size() >0) {
            return tel.get(0);
        }
        else
            return null;
    }

    public void setTel(ArrayList<String> tel) {
        this.tel = tel;
    }

    public void addTel(String tel){
        this.tel.add(tel);
    }

    public void removeTel(String tel){
        this.tel.remove(tel);
    }
}
