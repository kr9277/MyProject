package com.example.myproject;

public class Task {
    private int Id;
    private String endTime;
    private int taskTypes;
    private boolean isCompleted;
    private String responsible;
    private boolean isTaken;
    private int points;
    private int numDelays;
    private String fId;
    private String disc;

    public Task(){

    }
    public Task(int Id, String endTime, int taskTypes, String responsible, boolean isTaken, int points, String fId, String disc){
        this.Id = Id;
        this.endTime = endTime;
        this.taskTypes = taskTypes;
        this.isCompleted = false;
        this.responsible = "";
        this.isTaken = isTaken;
        this.points = points;
        this.numDelays = 0;
        this.fId = fId;
        this.disc = disc;
    }

    public int getId(){
        return Id;
    }
    public String getEndTime(){
        return endTime;
    }
    public int getTaskTypes() {
        return taskTypes;
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
    public String getfId(){
        return fId;
    }
    public String getDisc(){
        return disc;
    }
    public void updateNumDelays(){
        numDelays++;
    }
    public void setEndTime(String endTime){
        this.endTime = endTime;
    }
    public void setTaskTypes(int taskTypes){
        this.taskTypes = taskTypes;
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
    public void setfId(String fId){
        this.fId = fId;
    }
    public void setDisc(String disc){
        this.disc = disc;
    }
    public void setId(int Id){
        this.Id = Id;
    }
}
