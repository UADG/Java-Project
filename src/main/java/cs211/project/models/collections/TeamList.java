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
    public void deleteTeam(Event event, String teamName){
        for(Team team: teams){
            System.out.println(team.getEvent().getEventName() + team.getTeamName());
            if(team.getTeamName().contains(teamName) && team.getEvent().getEventName().contains(event.getEventName())){
                System.out.println("team :"+team.getTeamName());
                teams.remove(team);
                break;
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
        boolean first = true;
        Team lowest = null;
        int min = 0;
        int result = 0;

        if(teams.isEmpty()) return null;
        else{
            for(Team team : teams){
                if(team.getNumberOfStaffLeft() > 0){
                    if(first){
                        min = team.getNumberOfStaff()-team.getNumberOfStaffLeft();
                        first = false;
                    }
                    result = team.getNumberOfStaff()-team.getNumberOfStaffLeft();

                    if(result<=min){
                        min = result;
                        lowest = team;
                    }
                }
            }


            return lowest;
        }
    }

    public void addCommentInTeam(String teamName, String comment) {
        teamName = teamName.trim();
        comment = comment.trim();
        if (!teamName.equals("") && !comment.equals("")) {
            Team exist = checkTeamExist(teamName);
            if (exist != null) {
                exist.addComment(comment);
            }
        }

    }

    public ArrayList<Team> getTeams(){
        return teams;
    }
}
