package cs211.project.models;

import cs211.project.services.ActivityListFileDatasource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Activity {
    private String activityName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTimeActivity;
    private LocalTime endTimeActivity;
    private String teamName;
    private String participantName;
    private String status;
    private String eventName;
    private Team team;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    public Activity(String activityName, LocalDate startDate, LocalDate endDate, LocalTime startTimeActivity, LocalTime endTimeActivity, String teamName,String participantName, String status, String eventName) {
        this.activityName = activityName;
        this.startDate = startDate;
        this.endDate = endDate;
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
    public String getStartDate(){return startDate.format(dateFormatter);}
    public String getEndDate(){return endDate.format(dateFormatter);}
    public String getDate(){return startDate.format(dateFormatter)+endDate.format(dateFormatter);}
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

    public boolean checkTimeActivity(LocalTime startTimeActivity, LocalTime endTimeActivity, LocalDate startDate,LocalDate endDate){
        if(this.startDate.equals(startDate) && this.endDate.equals(endDate)){
            if(startDate.equals(endDate)) {
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
            else{
                return true;
            }
        }
        return false;
    }

    public void setActivityStatus(String status){
        this.status = status;
    }
    public boolean userIsParticipant(String participantName){
        return this.participantName.equals(participantName);
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
