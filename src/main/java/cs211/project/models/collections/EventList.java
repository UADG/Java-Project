package cs211.project.models.collections;

import cs211.project.models.Event;

import java.util.ArrayList;

public class EventList {
    private ArrayList<Event> events;
    private ArrayList<Event> search;
    private ArrayList<ArrayList<String>> activities ;
    private ArrayList<String> activity;

    public EventList() {
        events = new ArrayList<>();
        activities = new ArrayList<>();
    }
    public void addNewEvent(String eventName, String startDate, String endDate, String startTime, String endTime,
                            int ticket, int participantNum, String detail, String timeTeam, String timeParticipant) {
        eventName = eventName.trim();
        detail = detail.trim();
        if (!eventName.equals("")) {
            Event exist = findEventByEventName(eventName);
            if (exist == null) {
                events.add(new Event(eventName.trim(), startDate, endDate, startTime, endTime, ticket, participantNum, detail.trim(), timeTeam, timeParticipant));
            }
        }
    }
    public Event findEventByEventName(String eventName) {
        for (Event event : events) {
            if (event.isEvent(eventName)) {
                return event;
            }
        }
        return null;
    }
    public ArrayList<Event> getEvents(){
        return events;
    }

    public void searchEvent(String searchText){
        search = new ArrayList<>();
        searchText = searchText.toLowerCase();
        for (Event event : events) {
            if (event.getEventName().toLowerCase().contains(searchText)) {
                search.add(event);
            }
        }
    }
    public ArrayList<Event> getSearch(){
        return search;
    }

    public void addActivity(String nameActivity, String date, String timeStart, String timeStop,String userName,String teamName, String status){
        activity = new ArrayList<>();
        activity.add(nameActivity);
        activity.add(date);
        activity.add(timeStart);
        activity.add(timeStop);
        activity.add(userName);
        activity.add(teamName);
        activity.add(status);
        activities.add(activity);
    }
    public ArrayList<ArrayList<String>> getActivities(){
        return activities;
    }
}
