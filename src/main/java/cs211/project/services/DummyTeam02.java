package cs211.project.services;

import cs211.project.models.Team;

public class DummyTeam02 {
    public Team team1;

    public DummyTeam02(){
        team1 = new Team("SecondeTeam",5);
        team1.addStaffInTeam("01","a");
        team1.addStaffInTeam("02","b");
        team1.addStaffInTeam("03","c");
        team1.addStaffInTeam("04","d");
    }

    public Team getTeamDummy(){
        return team1;
    }

    public String getName(){
        return team1.getTeamName();
    }
}
