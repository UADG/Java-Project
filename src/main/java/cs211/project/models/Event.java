package cs211.project.models;
import java.util.Date;
public class Event {
    private String eventName;
    private Date startDate;
    private Date endDate;
    private Date startTime;
    private Date endTime;
    private int ticket;
    private int participantNum;
    private String detail;
    private Date timeTeam;
    private Date timeParticipant;

    public Event(String eventName, Date startDate, Date endDate, Date startTime, Date endTime,
                 int ticket, int participantNum, String detail, Date timeTeam, Date timeParticipant){
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
    }

    public String getEventName(){
        return eventName;
    }
    public Date getStartDate() {
        return startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public Date getStartTime() {
        return startTime;
    }
    public Date getEndTime() {
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
    public Date getTimeTeam() {
        return timeTeam;
    }
    public Date getTimeParticipant() {
        return timeParticipant;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(Date endTime) {
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
    public void setTimeTeam(Date timeTeam) {
        this.timeTeam = timeTeam;
    }
    public void setTimeParticipant(Date timeParticipant) {
        this.timeParticipant = timeParticipant;
    }
    public void ticketBuy(){ticket -= 1;}
    public void participantJoin(){participantNum -= 1;}
    @Override
    public String toString(){
        return "Name: " + eventName;}
    public boolean isEvent(String eventName) {
        return this.eventName.equals(eventName);
    }
}
