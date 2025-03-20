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
    public Task(int Id, String endTime, int taskTypes, boolean isCompleted, String responsible, boolean isTaken, int points, int numDelays, String fId, String disc){
        this.Id = Id;
        this.endTime = endTime;
        this.taskTypes = taskTypes;
        this.isCompleted = isCompleted;
        this.responsible = responsible;
        this.isTaken = isTaken;
        this.points = points;
        this.numDelays = numDelays;
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
}
