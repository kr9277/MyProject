package com.example.myproject;

public class User {
    private String name;
    private String uId;
    private String fId;
    private int points;
    private boolean parent;

    public User(){

    }
    public User(String name, String fId, boolean parent, int points, String uId){
        this.name = name;
        this.fId = fId;
        this.parent = parent;
        this.points = points;
        this.uId = uId;
    }
    public String getName(){
        return name;
    }
    public String getuId(){
        return uId;
    }
    public String getfId(){
        return fId;
    }
    public int getPoints(){
        return points;
    }
    public boolean getParent(){
        return parent;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setuId(String uId){
        this.uId = uId;
    }
    public void setfId(String fId){
        this.fId = fId;
        }
    public void setPoints(int points){
        this.points = points;
    }
    public void setParent(boolean parent){
        this.parent = parent;
    }
}
