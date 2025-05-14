package com.example.myproject;

import java.util.Date;

public class Task {
    private String tId;
    private Date endTime;
    private boolean isCompleted;
    private String responsible;
    private boolean isTaken;
    private int points;
    private int numDelays;
    private String fId;
    private String disc;

    public Task(){

    }
    public Task(String tId, Date endTime, String disc, int points, String fId){
        this.tId = tId;
        this.endTime = endTime;
        this.isCompleted = false;
        this.responsible = "";
        this.isTaken = false;
        this.points = points;
        this.numDelays = 0;
        this.fId = fId;
        this.disc = disc;
    }

    public String getTId(){
        return tId;
    }
    public Date getEndTime(){
        return endTime;
    }
    public boolean getIsComplited(){
        return isCompleted;
    }
    public String getResponsible(){
        return responsible;
    }
    public boolean getIsTaken(){
        return isTaken;
    }
    public int getPoints(){
        return points;
    }
    public int getNumDelays(){
        return numDelays;
    }
    public String getFId(){
        return fId;
    }
    public String getDisc(){
        return disc;
    }
    public void updateNumDelays(){
        numDelays++;
    }
    public void setEndTime(Date endTime){
        this.endTime = endTime;
    }
    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
    public void setResponsible(String responsible){
        this.responsible = responsible;
    }
    public void setTaken(boolean isTaken){
        this.isTaken = isTaken;
        }
    public void setPoints(int points){
        this.points = points;
    }
    public void setFId(String fId){
        this.fId = fId;
    }
    public void setDisc(String disc){
        this.disc = disc;
    }
    public void setTId(String tId){
        this.tId = tId;
    }
}
