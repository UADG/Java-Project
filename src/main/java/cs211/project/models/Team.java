package cs211.project.models;

import cs211.project.models.collections.AccountList;
import cs211.project.models.collections.StaffList;
import cs211.project.models.collections.TeamList;
import cs211.project.services.AccountListDatasource;
import cs211.project.services.TeamListFileDatasource;

import java.util.ArrayList;
public class Team {
    private String teamName;
    private int numberOfStaff;
    private int numberOfStaffLeft;
    private TeamListFileDatasource teamData;
    private TeamListFileDatasource teamListFileDatasource;
    private StaffList staffs;
    private ArrayList<String> bannedStaff;
    private Event event;
    private String comment;
    private String firstComment;
    private StaffList left;
    private TeamList teamlist;
    private ArrayList<String> teamComboBox;
    private AccountListDatasource accountListDatasource;
    private AccountList accountList;
    private Staff exist;

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

    public void addStaffInTeam(String id){
        accountListDatasource = new AccountListDatasource("data","user-info.csv");
        accountList = accountListDatasource.readData();
        for(Account account : accountList.getAccount()){
            if(account.isId(Integer.parseInt(id))) staffs.addStaff(new Staff(account));
        }
    }

    public void banStaffInTeam(String id){
        exist = staffs.checkStaffInList(id);
        if(exist!=null){
            bannedStaff.add(exist.getId());
            numberOfStaffLeft++;
        }
    }

    public StaffList getStaffThatNotBan(){
        left = new StaffList();
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
        teamListFileDatasource = new TeamListFileDatasource("data","team.csv");
        teamListFileDatasource.writeData(this);
    }

    public String getTeamName(){
        return teamName;
    }

    public void setFirstComment(String name) {
        firstComment = name;
    }

    public StaffList getStaffs(){
        return staffs;
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

    public ArrayList<String> getUserInTeam(int id){
        teamData = new TeamListFileDatasource("data", "team.csv");
        teamlist = teamData.readData();
        teamComboBox = new ArrayList<>();
        for(Team team:teamlist.getTeams()){
            for(Staff staff: team.getStaffs().getStaffList()){
                if(staff.isId(Integer.toString(id))){
                    teamComboBox.add(team.getEvent().getEventName());
                }
            }
        }
        return teamComboBox;
    }

    public ArrayList<String> getListTeam(int id){
        teamData = new TeamListFileDatasource("data", "team.csv");
        teamlist = teamData.readData();
        teamComboBox = new ArrayList<>();
        for(Team team:teamlist.getTeams()){
            for(Staff staff: team.getStaffs().getStaffList()){
                if(staff.isId(Integer.toString(id))){
                    teamComboBox.add(team.getTeamName());
                }
            }
        }
        return teamComboBox;
    }

    @Override
    public String toString() {
        return teamName;
    }
}
