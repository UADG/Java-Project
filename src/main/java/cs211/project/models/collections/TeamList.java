package cs211.project.models.collections;

import java.util.ArrayList;
import cs211.project.models.*;
import cs211.project.services.DummyTeam01;
import cs211.project.services.TeamListHardCode;

public class TeamList {
    private ArrayList<Team> teams;
    public TeamList(){
        teams = new ArrayList<>();
    }

    public void addTeam(String name, int num){
        name = name.trim();
        if(!name.equals("")){
            Team exist = checkTeamExist(name);
            if(exist == null){
                teams.add(new Team(name, num));
            }
        }
    }

    public void addTeam(Team team){
        String name = team.getTeamName().trim();
        if(!name.equals("")){
            Team exist = checkTeamExist(name);
            if(exist == null){
                teams.add(team);
            }
        }
    }


    public Team checkTeamExist(String name){
        for(Team team : teams){
            if(team.getTeamName().equals(name)){
                return team;
            }
        }
        return null;
    }

    public ArrayList<Team> getTeams(){
        return teams;
    }
}
