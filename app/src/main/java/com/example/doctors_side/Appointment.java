package com.example.doctors_side;


public class Appointment {
    private String time;
    private String description;
    private int priority;

    public Appointment()
    {

    }

    public Appointment(String  time,String description,int priority)
    {
        this.time=time;
        this.description=description;
        this.priority=priority;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
