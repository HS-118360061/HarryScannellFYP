package com.example.rowingappfyp;

import android.widget.Toast;

import java.security.PublicKey;

public class UserStatsDBModel {

    //I learned to do this whole class from [ref 1].
    public int SessionID;
    public float Distance;
    public  String Speed;
    public int Hours;
    public int Minutes;
    public int Seconds;

    //Constructors


    public UserStatsDBModel(int sessionID, float addDistanceUp, String speed, int hours, int minutes,int seconds) {
        SessionID = sessionID;
        Distance = addDistanceUp;
        Speed = speed;
        Hours = hours;
        Minutes = minutes;
        Seconds = seconds;
    }

    //public UserStatsDBModel(float distance) {
    //   Distance = distance;
   // }

    public UserStatsDBModel(String speed) {
        Speed = speed;
    }

    //public UserStatsDBModel(int SessionID, float Distance, String Speed, int Hours, int Minutes, int Seconds) {
    //}

    public UserStatsDBModel(UserStatsDBModel sendToDb) {
    }


//Getters & Setters


    public float getDistance() {
        return Distance;
    }

    public void setDistance(float distance) {
        Distance = distance;
    }

    public int getSessionID() {
        return SessionID;
    }

    public void setSessionID(int sessionID) {
        SessionID = sessionID;
    }

    public String getSpeed() {
        return Speed;
    }

    public void setSpeed(String speed) {
        Speed = speed;
    }

    public int getHours() {
        return Hours;
    }

    public void setHours(int hours) {
        Hours = hours;
    }

    public int getMinutes() {
        return Minutes;
    }

    public void setMinutes(int minutes) {
        Minutes = minutes;
    }

    public int getSeconds() {
        return Seconds;
    }

    public void setSeconds(int seconds) {
        Seconds = seconds;
    }

//TOString if you want to print it e.g in a list

    @Override
    public String toString() {
        return "UserStatsDBModel{" +
                "Distance=" + Distance +
                ", SessionID=" + SessionID +
                ", Speed='" + Speed + '\'' +
                ", Hours=" + Hours +
                ", Minutes=" + Minutes +
                ", Seconds=" + Seconds +
                '}';
    }
}
