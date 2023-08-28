package cs211.project.services;

import cs211.project.models.EventList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class EventHardCode {
    public EventList readData(){
        EventList eventList = new EventList();

        Date startDate = new Date(1234567890000L);
        Date endDate = new Date(1234567890000L);
        Date startTime = new Date(1234567890000L);
        Date endTime = new Date(1234567890000L);
        Date timeTeam = new Date(1234567890000L);
        Date timeParticipant = new Date(1234567890000L);

        eventList.addNewEvent("Fes", startDate, endDate, startTime, endTime, 20, 10, "abc", timeTeam, timeParticipant);

        return eventList;
    }
}
