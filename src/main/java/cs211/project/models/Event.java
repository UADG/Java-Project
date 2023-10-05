package cs211.project.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.ActivityList;
import cs211.project.models.collections.TeamList;
import cs211.project.services.*;

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
    private int ticketBuy = 0;
    private int participantJoin = 0;
    private String eventManager;
    private String picURL;
    private ArrayList<String> arrayStartTimeActivity;
    private ArrayList<String> arrayMinute;
    private ArrayList<String> arrayDateActivity;
    private ArrayList<ArrayList<String >> arr ;
    private ActivityList activitys;
    private TeamList teams;

public Event(String eventName, LocalDate startDate, LocalDate endDate, String startTime, String endTime,
             int ticket, int participantNum, String detail, String timeTeam,
             String timeParticipant,int ticketBuy, int participantJoin, String picURL, String eventManager){
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
        this.ticketBuy = ticketBuy;
        this.participantJoin = participantJoin;
        this.eventManager = eventManager;
        this.picURL = picURL;
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
    public int getTicketBuy() {
        return ticketBuy;
    }
    public int getParticipantJoin() {
        return participantJoin;
    }
    public String getEventManager(){return eventManager;}

    public String getPicURL() {
        return picURL;
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
    public void setPicURL(String picURL) {
        this.picURL = picURL;
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
        for (int i = 0; i < 24; i++) {
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
    public ArrayList<ArrayList<String >> getActivity(){
        return arr;
    }
    @Override
    public String toString() {
        return eventName;
    }

    public boolean isEvent(String eventName) {
        return this.eventName.equals(eventName);
    }
    public boolean isEventManager(String username) {
        return this.eventManager.equals(username);
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
    public AccountList loadUserInEvent(){
        UserEventListFileDatasource data = new UserEventListFileDatasource("data","user-joined-event.csv");
        AccountList accounts = data.readData();
        AccountList accountsInEvent = new AccountList();
        for(Account account: accounts.getAccount()) {
            for(String eventName: accounts.findAccountByUsername(account.getUsername()).getAllEventUser()){
                if(this.eventName.equals(eventName)){
                    accountsInEvent.addNewAccount(account);
                    break;
                }
            }
        }
        return accountsInEvent;
    }

    public Boolean checkParticipantIsFull(){
        loadActivityInEvent();
        for(Activity activity: activitys.getActivities()){
            if(activity.getParticipantName().equals("")){
                return true;
            }
        }
        return false;
    }
    public Boolean checkTimeActivity(LocalDateTime startActivityTime, LocalDateTime endActivityTime) {
        LocalTime startLocalTime = LocalTime.parse(startTime);
        LocalDate localDate = startDate;
        LocalTime endLocalTime = LocalTime.parse(endTime);
        LocalDateTime end = LocalDateTime.of(endDate, endLocalTime);

        while (!localDate.isAfter(endDate)) {
            LocalDateTime startLocalDateTime = LocalDateTime.of(localDate, startLocalTime);
            System.out.println(localDate);
            LocalDateTime endLocalDateTime;
            if(startLocalDateTime.getHour() > end.getHour()){
                endLocalDateTime = LocalDateTime.of(LocalDate.from(startLocalDateTime).plusDays(1), endLocalTime);
            }
            else{
                endLocalDateTime = LocalDateTime.of(LocalDate.from(startLocalDateTime), endLocalTime);
            }
            if (!startLocalDateTime.isAfter(startActivityTime) && endLocalDateTime.isAfter(startActivityTime) && startLocalDateTime.isBefore(endActivityTime) && !endLocalDateTime.isBefore(endActivityTime)) {
                return true;
            }
            localDate = localDate.plusDays(1);
        }
        return false;
    }
}
