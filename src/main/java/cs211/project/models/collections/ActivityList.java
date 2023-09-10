package cs211.project.models.collections;

import cs211.project.models.Activity;

import java.time.LocalTime;
import java.util.ArrayList;

public class ActivityList {
    private ArrayList<Activity> activities;

    public ActivityList(){
        activities =new ArrayList<>();
    }
    public void addActivity(String activityName, String date, LocalTime startTimeActivity, LocalTime endTimeActivity, String teamName,String participantName, String status){
        activityName = activityName.trim();
        if(checkActivity(activityName, date, startTimeActivity, endTimeActivity)) {
            activities.add(new Activity(activityName, date, startTimeActivity, endTimeActivity, teamName, participantName, status));
        }
    }
    public boolean checkActivity(String activityName, String date, LocalTime startTimeActivity, LocalTime endTimeActivity){
        for(Activity activity: activities){
            if(activity.isActivity(activityName)){
                return false;
            }
            else if(activity.checkTimeActivity(startTimeActivity,endTimeActivity)){
                return false;
            }
        }
        return true;
    }
    public ArrayList<Activity> getActivities(){
        return activities;
    }
}