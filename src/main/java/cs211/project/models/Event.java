package cs211.project.models;

import java.util.ArrayList;
import cs211.project.services.ActivityHardCode;

public class Event {
    private String eventName;
    private String startDate ;
    private String endDate;
    private String startTime;
    private String endTime;
    private int ticket;
    private int participantNum;
    private String detail;
    private String timeTeam;
    private String timeParticipant;
    private int ticketBuy;
    private int participantJoin;
    private ArrayList<String> arrayStartTimeActivity;
    private ArrayList<String> arrayEndTimeActivity;
    private ArrayList<String> arrayDateActivity;


    private ArrayList<ArrayList<String >> arr ;

    public Event(String eventName, String startDate, String endDate, String startTime, String endTime,
                 int ticket, int participantNum, String detail, String timeTeam, String timeParticipant){
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
        ActivityHardCode datasource = new ActivityHardCode();
        arr = datasource.readData();
    }

    public String getEventName(){
        return eventName;
    }
    public String getStartDate() {
        return startDate;
    }
    public String getEndDate() {
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
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(String endDate) {
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
    public void ticketBuy(){ticketBuy += 1;
    }
    public int getTicketLeft(){return ticket - ticketBuy;}
    public int getParticipantLeft(){return participantNum-participantJoin;}
    public void participantJoin(){participantJoin += 1;}
    public ArrayList<String> getArrayStartTimeActivity(){
        for(int i = Integer.parseInt(startTime); i < Integer.parseInt(endTime); i++){
            arrayStartTimeActivity.add(String.valueOf(i));
        }
        return arrayStartTimeActivity;
    }
    public ArrayList<String> getArrayEndTimeActivity(){
        for(int i = Integer.parseInt(endTime); i < Integer.parseInt(endTime); i++){
            arrayEndTimeActivity.add(String.valueOf(i));
        }
        return arrayEndTimeActivity;
    }
    public ArrayList<String> getArrayDate(){
        for(int i = Integer.parseInt(startDate); i < Integer.parseInt(endDate); i++){
            arrayDateActivity.add(String.valueOf(i));
        }
        return arrayDateActivity;
    }
    public ArrayList<ArrayList<String >> getActivity(){
        return arr;
    }
    @Override
    public String toString(){
        return "Name: " + eventName;}
    public boolean isEvent(String eventName) {
        return this.eventName.equals(eventName);
    }
}
