package cs211.project.models;

import cs211.project.models.collections.StaffList;

import java.util.ArrayList;
import java.util.HashMap;

public class Team {
    protected String teamName;
    protected int numberOfStaff;
   protected int numberOfStaffLeft;
    protected StaffList staffs;
    protected ArrayList<String> bannedStaff;
    protected ScheduleTeam schedule;


    public Team(String teamName, int numberOfStaff){
        this.teamName = teamName;
        this.numberOfStaff = numberOfStaff;
        numberOfStaffLeft = numberOfStaff;
        staffs = new StaffList();
        bannedStaff = new ArrayList<>();
        createTeamInCSV();
    }

    public Team(String teamName, int numberOfStaff, int numberOfStaffLeft){
        this(teamName,numberOfStaff);
        this.numberOfStaffLeft = numberOfStaffLeft;
    }

    public void addStaffInTeam(Staff staff){
        staffs.addStaff(staff);
    }

    public void addStaffInTeam(String id, String name){
        staffs.addStaff(id, name);
    }

    public void addStaffInTeam(String id){
        staffs.addStaff(id,"TestFileWriterDummy");
    }

    public void deleteStaff(String id){
        staffs.deleteStaff(id);
    }
    public void banStaffInTeam(String id){
        Staff exist = staffs.checkStaffInList(id);
        if(exist!=null){
            bannedStaff.add(exist.getId());
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
//        return left;
        for(Staff staff: left.getStaffList()){
            System.out.println(staff);
        }
        return left;
    }

    public void createTeamInCSV(){

    }

    public String getTeamName(){
        return teamName;
    }

    public StaffList getStaffsInTeam(){
        return staffs;
    }

    public StaffList getStaffs(){
        return staffs;
    }

    public ArrayList<String> getBannedStaff(){
        return bannedStaff;
    }

    public int getNumberOfStaff(){return numberOfStaff;}
    public int getNumberOfStaffLeft(){return numberOfStaffLeft;}

    @Override
    public String toString() {
        if(numberOfStaff > 1)return teamName+" "+"have "+numberOfStaff+" peoples";
        else return teamName+" "+"has "+numberOfStaff+" people";
    }
}
