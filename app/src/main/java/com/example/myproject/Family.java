package com.example.myproject;

import java.util.ArrayList;

public class Family {

    private String createdBy;
    private String fId;
    private ArrayList<Task> currentFamilyTasks;
    private ArrayList<String> uIds;
    private String address;
    private String name;

    public Family(){
    }

    public Family(String fId, String address, String uId, String createdBy, String name){
        this.address = address;
        this.uIds = new ArrayList<>();
        uIds.add(uId);
        this.createdBy = createdBy;
        this.name = name;
    }

    public String getFId(){
        return fId;
    }
    public String getCreatedBy(){
        return createdBy;
    }
    public ArrayList<Task> getCurrentFamilyTasks(){
        return currentFamilyTasks;
    }
    public ArrayList<String> getUIds(){
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
    public void addTask(Task task){
        currentFamilyTasks.add(task);
    }

    public void setFId(String fId){
        this.fId = fId;
    }
    public void setCreatedBy(String createdBy){
        this.createdBy = createdBy;
    }
    public void setTaskTypes(ArrayList<Task> currentFamilyTasks){
        this.currentFamilyTasks = currentFamilyTasks;
    }
    public void setUIds(ArrayList<String> uIds){
        this.uIds = uIds;
        }
    public void setAddress(String address){
        this.address = address;
    }
    public void setName(String name){
        this.name = name;
    }
}
