package cs211.project.models;

import cs211.project.services.ActivityListFileDatasource;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Activity {
    private String activityName;
    private String date;
    private LocalTime startTimeActivity;
    private LocalTime endTimeActivity;
    private String teamName;
    private String participantName;
    private String status;
    private String eventName;
    private Team team;
    private String comment;
    private String firstComment;

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
        comment = "";
        firstComment = "";
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
    public Team getTeam(){return team;}

    public String getComment() {
        return comment;
    }

    public void addComment(String comment) {
        this.comment += comment;
    }

    public boolean checkFirstComment(String comment) {
        if (firstComment.equals(comment)) {
            return true;
        }
        firstComment = comment;
        return false;
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

    public void setActivityStatus(String status){
        this.status = status;
    }
    public boolean userIsParticipant(String id){
        return this.participantName.equals(id);
    }
    public void updateTeamInActivity(Team team){
        this.team = team;
        ActivityListFileDatasource datasource = new ActivityListFileDatasource("data","activity-list.csv");
        datasource.updateTeamInActivity(activityName,team);
    }
    public void setParticipantName(String participantName){
        this.participantName = participantName;
    }
    public void deleteActivity(){
        this.activityName = "";
    }
}
