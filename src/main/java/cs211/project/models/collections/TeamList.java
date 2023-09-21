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

    public Team findLowestStaffTeam(){
        boolean found = false;
        int min;
        Team lowestStaff;
        if(teams.size() == 0) {
            return null;
        }
        else {
            min = teams.get(0).getNumberOfStaff() - teams.get(0).getNumberOfStaffLeft();
            lowestStaff = teams.get(0);
        }
        for(Team team : teams){
            int result = team.getNumberOfStaff() - team.getNumberOfStaffLeft();
            if(result<min && team.getNumberOfStaffLeft()>0){
                min = result;
                lowestStaff = team;
                System.out.println(lowestStaff.getTeamName());
            }
        }
        return lowestStaff;
    }

    public void addCommentInTeam(String teamName, String comment) {
        teamName = teamName.trim();
        comment = comment.trim();
        System.out.println(teamName + comment);
        if (!teamName.equals("") && !comment.equals("")) {
            Team exist = checkTeamExist(teamName);
            System.out.println(exist);
            if (exist != null) {
                exist.addComment(comment);
                System.out.println(exist.getComment());
            }
        }

    }

    public ArrayList<Team> getTeams(){
        return teams;
    }
}
