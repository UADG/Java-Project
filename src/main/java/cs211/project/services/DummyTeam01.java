package cs211.project.services;

import cs211.project.models.Team;

public class DummyTeam01 {
    public Team team1;

    public DummyTeam01(){
        team1 = new Team("bada",23);
        team1.addStaffInTeam("01","ibest");
        team1.addStaffInTeam("02","iwin");
        team1.addStaffInTeam("03","ijim");
        team1.addStaffInTeam("04","iai");
    }

    public Team getTeamDummy(){
        return team1;
    }

    public String getName(){
        return team1.getTeamName();
    }
}
