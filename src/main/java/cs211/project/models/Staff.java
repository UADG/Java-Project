package cs211.project.models;

import cs211.project.models.collections.ActivityList;
import cs211.project.models.collections.EventList;
import cs211.project.models.collections.StaffList;
import cs211.project.models.collections.TeamList;
import cs211.project.services.ActivityListFileDatasource;
import cs211.project.services.EventListFileDatasource;
import cs211.project.services.TeamListFileDatasource;
import cs211.project.services.UserEventListFileDatasource;

import java.util.ArrayList;

public class Staff{
    private String id;
    private String name;
    private Team team;
    private String eventName;

    private ArrayList<String> bannedEvent;
    public Staff(String id, String name){
        this.id = id;
        this.name = name;
        bannedEvent = new ArrayList<>();
    }

    public Staff(Account account){
        this.id = Integer.toString(account.getId());
        this.name = account.getName();
        bannedEvent = new ArrayList<>();
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public boolean isId(String id){
        return this.getId().equals(id);
    }

    public void addEventThatGetBanned(String eventName){
        bannedEvent.add(eventName);
    }

    public void removeEventThatGetBanned(String eventName){
        bannedEvent.remove(eventName);
    }


//    public String loadNameFromId(){
//        UserEventListFileDatasource userAll = new UserEventListFileDatasource("data","user-info.csv");
//        for(Account account : userAll.readData().getAccount()){
//            if()
//        }
//    }

    public Team loadTeam(){
        TeamListFileDatasource dataList = new TeamListFileDatasource("data","team.csv");
        TeamList teamList= dataList.readData();
        for(Team team : teamList.getTeams()){
            for(Staff staff: team.getStaffs().getStaffList()){
                if(staff.isId(id)){
                    this.team = team;
                    return team;
                }
            }
        }

        return null;
    }

    public String loadEvent(){
        loadTeam();
        ActivityListFileDatasource list = new ActivityListFileDatasource("data", "activity-list.csv");
        ActivityList activityList = list.readData();
        for(Activity activity : activityList.getActivities()){
            if(activity.getTeam().getTeamName().equals(team.getTeamName())){
                eventName = activity.getEventName();
                return eventName;
            }
        }
        return null;
    }

    public ArrayList<String> getBannedEvent(){
        return bannedEvent;
    }

    @Override
    public String toString() {
        return name+" : "+id;
    }
}
