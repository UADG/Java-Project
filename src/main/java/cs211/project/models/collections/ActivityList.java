package cs211.project.models.collections;

import cs211.project.models.Activity;

import java.time.LocalTime;
import java.util.ArrayList;

public class ActivityList {
    private ArrayList<Activity> allActivities;
    private ArrayList<Activity> activities;


    public ActivityList(){
        allActivities = new ArrayList<>();
        activities = new ArrayList<>();
    }
    public void addActivity(String activityName, String date, LocalTime startTimeActivity, LocalTime endTimeActivity, String teamName,String participantName, String status, String eventName){
        activityName = activityName.trim();
        allActivities.add(new Activity(activityName, date, startTimeActivity, endTimeActivity, teamName, participantName, status, eventName));
        if(!activities.isEmpty()){
            activities.add(new Activity(activityName, date, startTimeActivity, endTimeActivity, teamName, participantName, status, eventName));
        }
    }

    public void addActivity(Activity activity){
        activities.add(activity);
    }

    public void addCommentInActivity(String activityName, String comment) {
        activityName = activityName.trim();
        comment = comment.trim();
        System.out.println(activityName + comment);
        if (!activityName.equals("") && !comment.equals("")) {
            Activity exist = findActivityByName(activityName);
            System.out.println(exist);
            if (exist != null) {
                exist.addComment(comment);
                System.out.println(exist.getComment());
            }
        }

    }

    public Activity findActivityByName(String name) {
        for(Activity activity: allActivities){
            if(activity.getActivityName().equals(name)){
                return activity;
            }
        }
        return null;
    }

    public void findActivityInEvent(String eventName){
        for(Activity activity: allActivities){
            if(activity.getEventName().equals((eventName))){
                activities.add(activity);
            }
        }

    }
    public boolean checkActivity(String activityName, String date, LocalTime startTimeActivity, LocalTime endTimeActivity){
        if(!activities.isEmpty()) {
            for (Activity activity : activities) {
                if (activity.isActivity(activityName)) {
                    return false;
                } else if (activity.checkTimeActivity(startTimeActivity, endTimeActivity, date)) {
                    return false;
                }
            }
        } else {
            if (endTimeActivity.isBefore(startTimeActivity)) {
                return false;
            }
            else if (endTimeActivity.equals(startTimeActivity)) {
                return false;
            }
        }
        return true;
    }
    public void addParticipant(String id) {
        for(Activity activity : activities){
            if(activity.getParticipantName().isEmpty()){
                activity.setParticipantName(id);
                break;
            }
        }
    }
    public void setActivityStatus(String activityName, String status) {
        for(Activity activity : activities){
            if(activity.getActivityName().equals(activityName)){
                activity.setActivityStatus(status);
                break;
            }
        }
        for (Activity activity : allActivities) {
            for (Activity activity2 : activities) {
                if(activity.getEventName().equals(activity2.getEventName()) && activity.getActivityName().equals(activity2.getActivityName())){
                    if(!activity.getActivityName().equals(activity2.getActivityName())){
                        activity.setActivityStatus(activity2.getStatus());
                        break;
                    }
                }
            }
        }
    }
    public boolean userIsParticipant(String id) {
        for (Activity activity : activities) {
            if(activity.getParticipantName().equals(id)){
                return false;
            }
        }
        return true;
    }
    public ArrayList<Activity> getAllActivities(){
        return allActivities;
    }
    public ArrayList<Activity> getActivities(){
        return activities;
    }
}
