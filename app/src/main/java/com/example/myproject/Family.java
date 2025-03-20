package com.example.myproject;

import java.util.ArrayList;

public class Family {
    private String fId;
    private String pass;
    private ArrayList<String> taskTypes;
    private ArrayList<String> uIds;
    private String address;
    private String name;

    public Family(){
    }

    public Family(String address, String uId, String pass, String name){
        this.address = address;
        this.uIds = new ArrayList<>();
        uIds.add(uId);
        this.pass = pass;
        this.name = name;
    }

    public String getfId(){
        return fId;
    }
    public String getPass(){
        return pass;
    }
    public ArrayList<String> getTaskTypes(){
        return taskTypes;
    }
    public ArrayList<String> getuIds(){
        return uIds;
        }
    public String getAddress(){
        return address;
    }
    public String getName(){
        return name;
    }

    public void addMember(String uId){
        uIds.add(uId);
    }
    public void setfId(String fId){
        this.fId = fId;
    }
    public void setPass(String pass){
        this.pass = pass;
    }
    public void setTaskTypes(ArrayList<String> taskTypes){
        this.taskTypes = taskTypes;
    }
    public void setuIds(ArrayList<String> uIds){
        this.uIds = uIds;
        }
    public void setAddress(String address){
        this.address = address;
    }
    public void setName(String name){
        this.name = name;
    }
}
