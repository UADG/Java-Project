package cs211.project.services;

import cs211.project.models.collections.TeamList;

public class TeamListHardCode implements Datasource<TeamList>{
    @Override
    public TeamList readData() {
        TeamList list = new TeamList();
        list.addTeam(new DummyTeam01().getTeamDummy());
        list.addTeam(new DummyTeam02().getTeamDummy());
        list.addTeam(new DummyTeam03().getTeamDummy());
        return list;
    }

    @Override
    public void writeData(TeamList data) {

    }
}
