package cs211.project.services;

import cs211.project.models.Team;

public class DummyTeam03 {
    public Team team1;

    public DummyTeam03(){
        team1 = new Team("ThirdTeam",5);
        team1.addStaffInTeam("05","ada");
        team1.addStaffInTeam("02","bvd");
        team1.addStaffInTeam("03","fdw");
        team1.addStaffInTeam("09","dre");
    }

    public Team getTeamDummy(){
        return team1;
    }

    public String getName(){
        return team1.getTeamName();
    }
}
