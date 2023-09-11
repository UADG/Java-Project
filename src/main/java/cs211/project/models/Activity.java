package cs211.project.models;

import cs211.project.models.collections.ActivityList;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Activity {
    private String activityName;
    private String date;
    private LocalTime startTimeActivity;
    private LocalTime endTimeActivity;
    private String teamName;
    private String participantName;
    private String status;
    private String eventName;

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    public Activity(String activityName, String date, LocalTime startTimeActivity, LocalTime endTimeActivity, String teamName,String participantName, String status, String eventName) {
        this.activityName = activityName;
        this.date = date;
        this.startTimeActivity = startTimeActivity;
        this.endTimeActivity = endTimeActivity;
        this.teamName = teamName;
        this.participantName = participantName;
        this.status = status;
        this.eventName = eventName;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getDate() {
        return date;
    }

    public String getStartTimeActivity() {
        return startTimeActivity.format(timeFormatter);
    }

    public String getEndTimeActivity() {
        return endTimeActivity.format(timeFormatter);
    }

    public String getTeamName() {
        return teamName;
    }

    public String getParticipantName() {
        return participantName;
    }

    public String getStatus() {
        return status;
    }
    public boolean isActivity(String activityName) {
        return this.activityName.equals(activityName);
    }

    public String getEventName() {
        return eventName;
    }

    public boolean checkTimeActivity(LocalTime startTimeActivity, LocalTime endTimeActivity, String date){
        if(this.date.equals(date)){
            if (endTimeActivity.isBefore(startTimeActivity)) {
                return true;
            } else if (this.startTimeActivity.isBefore(endTimeActivity) && this.endTimeActivity.isAfter(startTimeActivity)) {
                return true;
            } else if (this.startTimeActivity.isAfter(startTimeActivity) && this.startTimeActivity.isBefore(endTimeActivity)) {
                return true;
            } else if (endTimeActivity.equals(startTimeActivity)) {
                return true;
            }
            return false;
        }
        return false;
    }
}
