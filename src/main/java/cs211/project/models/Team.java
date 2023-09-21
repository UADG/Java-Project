package cs211.project.models;

import cs211.project.models.collections.StaffList;
import cs211.project.services.TeamListFileDatasource;

import java.util.ArrayList;
public class Team {
    protected String teamName;
    protected int numberOfStaff;
   protected int numberOfStaffLeft;
    protected StaffList staffs;
    protected ArrayList<String> bannedStaff;
    protected Event event;
    protected String eventName;
    private String comment;
    private String firstComment;



    public Team(String teamName, int numberOfStaff){
        this.teamName = teamName;
        this.numberOfStaff = numberOfStaff;
        numberOfStaffLeft = numberOfStaff;
        staffs = new StaffList();
        bannedStaff = new ArrayList<>();
        comment = "";
        firstComment = "";
    }

    public Team(String teamName, int numberOfStaff, int numberOfStaffLeft){
        this(teamName,numberOfStaff);
        this.numberOfStaffLeft = numberOfStaffLeft;
    }

    public Team(String teamName, int numberOfStaff, String eventName){
        this(teamName,numberOfStaff);
        event = new Event(eventName);
        event.loadEventInfo();
    }

    public Team(String teamName, int numberOfStaff, int numberOfStaffLeft, String eventName){
        this(teamName,numberOfStaff,numberOfStaffLeft);
        event = new Event(eventName);
        event.loadEventInfo();
    }

    public void addStaffInTeam(Staff staff){
        staffs.addStaff(staff);
        numberOfStaffLeft--;
    }

    public void addStaffInTeam(String id, String name){
        staffs.addStaff(id, name);
        numberOfStaffLeft--;
    }

    public void addStaffInTeam(String id){
        staffs.addStaff(id,"ThisIsFromTeam");
    }

    public void deleteStaff(String id){
        staffs.deleteStaff(id);
        numberOfStaffLeft++;
    }

    public void deleteStaff(Staff staff){
        deleteStaff(staff.getId());
        numberOfStaffLeft++;
    }

    public void banStaffInTeam(String id){
        Staff exist = staffs.checkStaffInList(id);
        if(exist!=null){
            bannedStaff.add(exist.getId());
            numberOfStaffLeft++;
        }
    }

    public StaffList getStaffThatNotBan(){
        StaffList left = new StaffList();
        for(Staff staff : staffs.getStaffList()){
            boolean ban = false;
            for(String banId : bannedStaff){
                if(staff.isId(banId)){
                    ban = true;
                    break;
                }
            }

            if(!ban){
                left.addStaff(staff);
            }
        }
        return left;
    }

    public void createTeamInCSV(){
        TeamListFileDatasource data = new TeamListFileDatasource("data","team.csv");
        data.writeData(this);
    }

    public String getTeamName(){
        return teamName;
    }

    public StaffList getStaffsInTeam(){
        return staffs;
    }

    public void setFirstComment(String name) {
        firstComment = name;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public StaffList getStaffs(){
        return staffs;
    }

    public ArrayList<String> getBannedStaff(){
        return bannedStaff;
    }

    public int getNumberOfStaff(){return numberOfStaff;}
    public int getNumberOfStaffLeft(){return numberOfStaffLeft;}
    public Event getEvent(){return  event;}

    public void setEvent(Event event){
        this.event = event;
    }

    public void setEvent(String eventName){
        event.setEventName(eventName);
        event.loadEventInfo();
    }

    public String getComment() {
        return comment;
    }

    public void addComment(String comment) {
        this.comment = comment;
    }

    public boolean checkFirstComment(String name) {
        if (!firstComment.equals(name)) {
            firstComment = name;
            return true;
        }
        return false;
    }


    @Override
    public String toString() {
        return teamName;
    }
}
