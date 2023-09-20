package cs211.project.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import cs211.project.models.collections.ActivityList;
import cs211.project.models.collections.TeamList;
import cs211.project.services.ActivityHardCode;
import cs211.project.services.ActivityListFileDatasource;
import cs211.project.services.EventListFileDatasource;
import cs211.project.services.TeamListFileDatasource;

public class Event {
    private String eventName;
    private LocalDate startDate ;
    private LocalDate endDate;
    private String startTime;
    private String endTime;
    private int ticket;
    private int participantNum;
    private String detail;
    private String timeTeam;
    private String timeParticipant;
    private int ticketBuy;
    private int participantJoin;
    private String eventManager;
    private ArrayList<String> arrayStartTimeActivity;
    private ArrayList<String> arrayMinute;
    private ArrayList<String> arrayDateActivity;
    private ArrayList<ArrayList<String >> arr ;
    private ActivityList activitys;
    private TeamList teams;

public Event(String eventName, LocalDate startDate, LocalDate endDate, String startTime, String endTime,
             int ticket, int participantNum, String detail, String timeTeam,
             String timeParticipant, String eventManager){
        this.eventName = eventName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime =startTime;
        this.endTime = endTime;
        this.ticket = ticket;
        this.participantNum = participantNum;
        this.detail = detail;
        this.timeTeam = timeTeam;
        this.timeParticipant = timeParticipant;
        ticketBuy = 0;
        participantJoin = 0;
        this.eventManager = eventManager;
        ActivityHardCode datasource = new ActivityHardCode();
        arr = datasource.readData();
    }

    public Event(String eventName){
        this.eventName = eventName;
        loadEventInfo();
    }

    public String getEventName() {
        return eventName;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public String getStartTime() {
        return startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public int getTicket() {
        return ticket;
    }
    public int getParticipantNum() {
        return participantNum;
    }
    public String getDetail() {
        return detail;
    }
    public String getTimeTeam() {
        return timeTeam;
    }
    public String getTimeParticipant() {
        return timeParticipant;
    }
    public String getEventManager() {
        return eventManager;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public void setTicket(int ticket) {
        this.ticket = ticket;
    }
    public void setParticipantNum(int participantNum) {
        this.participantNum = participantNum;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
    public void setTimeTeam(String timeTeam) {
        this.timeTeam = timeTeam;
    }
    public void setTimeParticipant(String timeParticipant) {
        this.timeParticipant = timeParticipant;
    }
    public void ticketBuy() {
        ticketBuy += 1;
    }
    public int getTicketLeft() {
        return ticket - ticketBuy;
    }
    public int getParticipantLeft() {
        return participantNum-participantJoin;
    }
    public void participantJoin() {
        participantJoin += 1;
    }
    public ArrayList<String> getArrayHour() {
        ArrayList<String> arrayStartTimeActivity = new ArrayList<>();
        int startHour = Integer.parseInt(startTime.split(":")[0]);
        int endHour = Integer.parseInt(endTime.split(":")[0]);

        for (int i = startHour; i <= endHour; i++) {
            arrayStartTimeActivity.add(String.valueOf(i));
        }
        return arrayStartTimeActivity;
    }
    public ArrayList<String> getArrayMinute() {
        ArrayList<String> arrayMinute = new ArrayList<>();
        for(int i = 0; i < 60; i++){
            arrayMinute.add(String.valueOf(i));
        }
        return arrayMinute;
    }
    public ArrayList<String> getArrayDate() {
        ArrayList<String> arrayDateActivity = new ArrayList<>();

        String startDateString = startDate.toString();
        String endDateString = endDate.toString();

        int startDay = Integer.parseInt(startDateString.split("-")[2]);
        int endDay = Integer.parseInt(endDateString.split("-")[2]);

        for (int i = startDay; i <= endDay; i++) {
            arrayDateActivity.add(String.valueOf(i));
        }

        return arrayDateActivity;
    }
    //    public ArrayList<String> getArrayDate() {
//        ArrayList<String> arrayDateActivity = new ArrayList<>();
//            int startDay = Integer.parseInt(startDate.split("-")[2]);
//            int endDay = Integer.parseInt(endDate.split("-")[2]);
//
//            for (int i = startDay; i <= endDay; i++) {
//                arrayDateActivity.add(String.valueOf(i));
//            }
//        return arrayDateActivity;
//    }
    public ArrayList<ArrayList<String>> getActivity(){
        return arr;
    }
    @Override
    public String toString() {
        return eventName;
    }

    public boolean isEvent(String eventName) {
        return this.eventName.equals(eventName);
    }

    public Event loadEventInfo() {
        EventListFileDatasource list = new EventListFileDatasource("data", "event-list.csv");
        for(Event event : list.readData().getEvents()){
            if(event.getEventName().equals(eventName)){
                this.eventName = event.getEventName();
                this.startDate = event.getStartDate();
                this.endDate = event.getEndDate();
                this.startTime = event.getStartTime();
                this.endTime = event.getEndTime();
                this.ticket = event.getTicket();
                this.participantNum = event.getParticipantNum();
                this.participantJoin = event.getParticipantLeft();
                this.detail = event.getDetail();
                this.timeTeam = event.getTimeTeam();
                this.timeParticipant = event.getTimeParticipant();
                ticketBuy = event.getTicketLeft();
            }
        }
        return this;
    }

    public ActivityList loadActivityInEvent() {
        ActivityListFileDatasource data = new ActivityListFileDatasource("data","activity-list.csv");
        activitys = data.readData();
        activitys.findActivityInEvent(eventName);

        return activitys;
    }

    public TeamList loadTeamInEvent() {
        TeamListFileDatasource data = new TeamListFileDatasource("data", "team.csv");
        TeamList list = data.readData();
        ArrayList<String> nameTeamInEvent = new ArrayList<>();
        teams = new TeamList();
        loadActivityInEvent();
        for(Activity activity:activitys.getActivities()){
            nameTeamInEvent.add(activity.getTeamName());
        }

        for(String name: nameTeamInEvent){
            for(Team team : list.getTeams()){
                if(team.getTeamName().equals(name) && team.getEvent().getEventName().equals(eventName)){
                    teams.addTeam(team);
                }
            }
        }


        return  teams;
    }
}
