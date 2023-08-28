package cs211.project.services;

import cs211.project.models.collections.EventList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventHardCode {
    public EventList readData(){
        EventList eventList = new EventList();

        LocalDateTime startDate = LocalDateTime.of(2023, 5, 5, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 5, 5, 0, 0);
        LocalDateTime startTime = LocalDateTime.of(2023, 5, 5, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 5, 5, 12, 0);
        LocalDateTime timeTeam = LocalDateTime.of(2023, 5, 5, 9, 0);
        LocalDateTime timeParticipant = LocalDateTime.of(2023, 5, 5, 9, 30);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formattedStartDate = startDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);
        String formattedStartTime = startTime.format(formatter);
        String formattedEndTime = endTime.format(formatter);
        String formattedTimeTeam = timeTeam.format(formatter);
        String formattedTimeParticipant = timeParticipant.format(formatter);

        eventList.addNewEvent("Fes", formattedStartDate, formattedEndDate, formattedStartTime,
                formattedEndTime, 20, 10, "abc", formattedTimeTeam, formattedTimeParticipant);

        return eventList;
    }
}
