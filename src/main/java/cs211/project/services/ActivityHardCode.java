package cs211.project.services;

import java.util.ArrayList;

public class ActivityHardCode implements Datasource<ArrayList<ArrayList<String>>>{
    @Override
    public ArrayList<ArrayList<String>> readData() {
        ArrayList<ArrayList<String>> arr = new ArrayList<>();
        ArrayList<String> a1,a2;
        a1 = new ArrayList<>();
        a1.add("activity1");
        a1.add("18");
        a1.add("14:00");
        a1.add("15:30");
        a1.add("team1");
        a1.add("participant1");
        a1.add("0");
        a2 = new ArrayList<>();
        a2.add("activity2");
        a2.add("19");
        a2.add("11:00");
        a2.add("12:00");
        a2.add("team2");
        a2.add("participant2");
        a2.add("0");

        return arr;
    }

    @Override
    public void writeData(ArrayList<ArrayList<String>> data) {
        
    }
}