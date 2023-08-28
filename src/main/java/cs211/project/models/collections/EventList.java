package cs211.project.models.collections;

import cs211.project.models.Event;

import java.util.ArrayList;
import java.util.Date;

public class EventList {
    private ArrayList<Event> events;
    public EventList() {
        events = new ArrayList<>();
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
}
